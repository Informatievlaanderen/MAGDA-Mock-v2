package be.vlaanderen.vip.magda.magdamock.client.handlers;

import be.vlaanderen.vip.magda.magdamock.client.logging.LifecyclePhase;
import be.vlaanderen.vip.magda.magdamock.client.logging.SoapLogHelper;
import be.vlaanderen.vip.magda.magdamock.client.patchers.SoapResponsePatcher;
import be.vlaanderen.vip.magda.magdamock.client.patchers.SoapResponsePatcherImpl;
import be.vlaanderen.vip.magda.magdamock.config.MappingLists;
import be.vlaanderen.vip.magda.magdamock.config.MockSoapMapping;
import be.vlaanderen.vip.magda.magdamock.config.WireMockData;
import be.vlaanderen.vip.magda.magdamock.exceptions.MagdaMockSoapException;
import be.vlaanderen.vip.magda.magdamock.filters.EmptyElementsFilter;
import be.vlaanderen.vip.magda.magdamock.filters.MagdaMockFilter;
import be.vlaanderen.vip.magda.magdamock.soap.SoapBodyValidator;
import be.vlaanderen.vip.magda.magdamock.soap.SoapValidationError;
import be.vlaanderen.vip.magda.magdamock.utils.MagdaMockDocument;
import be.vlaanderen.vip.magda.magdamock.utils.TimeoutUtil;
import com.github.tomakehurst.wiremock.http.HttpHeader;
import com.github.tomakehurst.wiremock.http.HttpHeaders;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.http.Response;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class MagdaMockSoapHandler extends AbstractMockHandler {
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
    private final SoapBodyValidator soapRequestValidator;
    private final SoapBodyValidator soapResponseValidator;
    private final boolean logRequestBody;
    private final SoapResponsePatcher soapResponsePatcher = new SoapResponsePatcherImpl();
    private final List<MagdaMockFilter> filters;
    private final Set<MagdaMockDocument.MagdaServiceIdentification> knownServiceIdentifications;

    public MagdaMockSoapHandler(WireMockData wireMockData, TimeoutUtil timeoutUtil, SoapBodyValidator soapRequestValidator, SoapBodyValidator soapResponseValidator, boolean logRequestBody) {
        super(wireMockData, timeoutUtil);
        this.soapRequestValidator = soapRequestValidator;
        this.soapResponseValidator = soapResponseValidator;
        this.logRequestBody = logRequestBody;
        this.filters = new ArrayList<>();
        this.filters.add(EmptyElementsFilter.getInstance());

        this.knownServiceIdentifications = MappingLists.SOAP_MAPPINGS.stream()
                .map(def -> new MagdaMockDocument.MagdaServiceIdentification(def.getService(), def.getVersion())).collect(Collectors.toSet());
    }


    public MockSoapResponse sendSoapRequest(MockSoapRequest mockSoapRequest) {
        Document xml = mockSoapRequest.document();
        MagdaMockDocument request = MagdaMockDocument.fromDocument(xml);
        SoapLogHelper.contextSetSoapServiceNameVersion(request);
        SoapLogHelper.contextSetReference(request);
        checkServiceExistsInMagdaMock(request);

        if (logRequestBody) {
            log.debug("Request body {}", request);
        }
        SoapLogHelper.contextSetLifecyclePhase(LifecyclePhase.REQUEST_VALIDATION);
        soapRequestValidator.validateXml(request);

        SoapLogHelper.contextSetLifecyclePhase(LifecyclePhase.REQUEST_PRE_PROCESSING);
        timeoutUtil.timeout();
        String dateHeader = getDateHeaderFromSoapRequest(request);

        SoapLogHelper.contextSetLifecyclePhase(LifecyclePhase.RESPONSE_MAPPING);
        String soapUrl = wireMockServer.url("/soap");
        Request mockRequest = createInternalWiremockRequest(soapUrl, "POST", request.toString(), new HttpHeaders(new HttpHeader("Date", dateHeader)), "text/xml");
        Response response = routeRequest(mockRequest);
        if (response.getStatus() == 404) {
            return null;
        }
        Document document = parseSoapResponse(response);

        SoapLogHelper.contextSetLifecyclePhase(LifecyclePhase.RESPONSE_POST_PROCESSING);
        Document patchedResponse = patchResponse(request, document);
        Document filteredResponse = filterResponse(request, patchedResponse);

        SoapLogHelper.contextSetLifecyclePhase(LifecyclePhase.RESPONSE_VALIDATION);
        Document checkedResponse = validateSoapResponse(request, filteredResponse);
        Document wrappedResponse = wrapInEnvelope(checkedResponse);
        Map<String, List<String>> headers = new HashMap<>();
        for (String headerName : response.getHeaders().keys()) {
            headers.put(headerName, response.getHeaders().getHeader(headerName).values());
        }
        headers.remove("Matched-Stub-Id");
        return new MockSoapResponse(wrappedResponse, 200, headers);
    }

    private Document filterResponse(MagdaMockDocument request, Document checkedResponse) {
        Document document = checkedResponse;
        for (MagdaMockFilter filter : filters) {
            document = filter.filter(request, document);
        }
        return document;
    }

    private Document validateSoapResponse(MagdaMockDocument request, Document document) throws SoapValidationError {
        Document response = validateSoapSender(request, document);
        soapResponseValidator.validateXml(MagdaMockDocument.fromDocument(response));
        return response;
    }

    private Document validateSoapSender(MagdaMockDocument request, Document response) {
        String identification = request.getValue("//Afzender/Identificatie");
        LocalDateTime now = LocalDateTime.now();

        if (identification == null || identification.isBlank()) {
            Node uitzonderingenNode = MagdaMockDocument.fromString(String.format("""
                                    <Uitzonderingen>
                                        <Uitzondering>
                                            <Identificatie>13001</Identificatie>
                                            <Oorsprong>MAGDA</Oorsprong>
                                            <Type>FOUT</Type>
                                            <Tijdstip>
                                                <Datum>%s</Datum>
                                                <Tijd>%s</Tijd>
                                            </Tijdstip>
                                            <Diagnose>Geen machtiging van de afzender in deze hoedanigheid voor de gevraagde dienst</Diagnose>
                                        </Uitzondering>
                                    </Uitzonderingen>
                    """, now.format(DATE_FORMAT), now.format(TIME_FORMAT))).getXml().getFirstChild();
            Node repliek = response.getElementsByTagName("Repliek").item(0);
            uitzonderingenNode = response.importNode(uitzonderingenNode, true);
            for (int i = 0; i < repliek.getChildNodes().getLength(); i++) {
                Node node = repliek.getChildNodes().item(i);
                if ("Antwoorden".equals(node.getLocalName())) {
                    node.getParentNode().removeChild(node);
                }
            }
            repliek.appendChild(uitzonderingenNode);
        }
        return response;
    }

    private String getDateHeaderFromSoapRequest(MagdaMockDocument request) {
        LocalDate date;
        try {
            String dateString = request.getValue("//Verzoek/Context/Bericht/Tijdstip/Datum").strip();
            date = LocalDate.parse(dateString);
            return DateTimeFormatter.RFC_1123_DATE_TIME.format(date.atStartOfDay(ZoneId.of("Europe/Brussels")));
        } catch (Exception e) {
            log.info("Unable to extract date and time from request");
        }
        return DateTimeFormatter.RFC_1123_DATE_TIME.format(OffsetDateTime.now());
    }

    private Document parseSoapResponse(Response response) {
        try {
            return MagdaMockDocument.fromString(response.getBodyAsString()).getXml();
        } catch (MagdaMockSoapException e) {
            log.error("Unable to parse soap response", e);
            log.error("File contents: {}", response.getBodyAsString());
            throw new MagdaMockSoapException("Response contains invalid XML content.", "Server", e.getMessage(), e.getCause());
        }
    }

    private Document patchResponse(MagdaMockDocument request, Document document) {
        return soapResponsePatcher.patchResponse(request, document).getXml();
    }

    private Document wrapInEnvelope(Document bodyDocument) {
        MagdaMockDocument magdaMockDocument = MagdaMockDocument.fromDocument(bodyDocument);
        var soap = """
                <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" >
                <soapenv:Header/>
                    <soapenv:Body>
                    %s
                    </soapenv:Body>
                </soapenv:Envelope>""".formatted(magdaMockDocument);

        return MagdaMockDocument.fromString(soap).getXml();
    }

    private void checkServiceExistsInMagdaMock(MagdaMockDocument request) {
        var serviceIdentification = request.getServiceIdentification();
        log.debug("Checking if service {} exists", serviceIdentification);
        if (!knownServiceIdentifications.contains(serviceIdentification)) {
            throw new MagdaMockSoapException(String.format("Response mapping is undefined for %s", serviceIdentification.getServiceNaam()), "Server", null);
        }
    }

    public record MockSoapResponse(
            Document document,
            Integer statusCode,
            Map<String, List<String>> headers
    ) {
    }

    public record MockSoapRequest(
            Document document
    ) {
    }
}
