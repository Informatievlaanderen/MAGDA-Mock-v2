package be.vlaanderen.vip.magda.magdamock.client.handlers;

import be.vlaanderen.vip.magda.magdamock.client.patchers.SoapResponsePatcher;
import be.vlaanderen.vip.magda.magdamock.client.patchers.SoapResponsePatcherImpl;
import be.vlaanderen.vip.magda.magdamock.config.WireMockData;
import be.vlaanderen.vip.magda.magdamock.filters.EmptyElementsFilter;
import be.vlaanderen.vip.magda.magdamock.filters.MagdaMockFilter;
import be.vlaanderen.vip.magda.magdamock.soap.LenientSoapBodyValidator;
import be.vlaanderen.vip.magda.magdamock.soap.SoapBodyValidator;
import be.vlaanderen.vip.magda.magdamock.soap.SoapValidationError;
import be.vlaanderen.vip.magda.magdamock.utils.MagdaMockDocument;
import be.vlaanderen.vip.magda.magdamock.utils.TimeoutUtil;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.http.Response;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class MagdaMockSoapHandler extends AbstractMockHandler {
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
    private final SoapBodyValidator soapRequestValidator;
    private final SoapBodyValidator soapResponseValidator;
    private final SoapResponsePatcher soapResponsePatcher = new SoapResponsePatcherImpl();
    private final List<MagdaMockFilter> filters;

    public MagdaMockSoapHandler(WireMockData wireMockData, TimeoutUtil timeoutUtil) {
        super(wireMockData, timeoutUtil);
        this.soapRequestValidator = new LenientSoapBodyValidator();
        this.soapResponseValidator = new LenientSoapBodyValidator();
        this.filters = new ArrayList<>();
    }

    public MagdaMockSoapHandler(WireMockData wireMockData, TimeoutUtil timeoutUtil, SoapBodyValidator soapRequestValidator, SoapBodyValidator soapResponseValidator) {
        super(wireMockData, timeoutUtil);
        this.soapRequestValidator = soapRequestValidator;
        this.soapResponseValidator = soapResponseValidator;
        this.filters = new ArrayList<>();
        this.filters.add(new EmptyElementsFilter());
    }

    public MockSoapResponse sendSoapRequest(MockSoapRequest mockSoapRequest) {
        Document xml = mockSoapRequest.document();
        timeoutUtil.timeout();
        MagdaMockDocument request = MagdaMockDocument.fromDocument(xml);
        soapRequestValidator.validateXml(request);
        String dateHeader = getDateHeaderFromSoapRequest(request);
        String soapUrl = wireMockServer.url("/soap");
        Request mockRequest = createInternalWiremockRequest(soapUrl, "POST", request.toString(), dateHeader, "text/xml");
        Response response = routeRequest(mockRequest);
        if (response.getStatus() == 404) {
            return null;
        }
        Document document = parseSoapResponse(response);
        Document patchedResponse = patchResponse(request, document);
        Document filteredResponse = filterResponse(request, patchedResponse);
        Document checkedResponse = validateSoapResponse(request, filteredResponse);
        Document wrappedResponse = wrapInEnvelope(checkedResponse);
        return new MockSoapResponse(wrappedResponse, 200);
    }

    private Document filterResponse(MagdaMockDocument request, Document checkedResponse) {
        Document document = checkedResponse;
        for (MagdaMockFilter filter : filters) {
            document = filter.filter(request, document);
        }
        return document;
    }

    private Document validateSoapResponse(MagdaMockDocument request, Document response) throws SoapValidationError {
        response = validateSoapSender(request, response);
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
        return "";
    }

    private Document parseSoapResponse(Response response) {
        return MagdaMockDocument.fromString(response.getBodyAsString()).getXml();
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

    public record MockSoapResponse(
            Document document,
            Integer statusCode
    ) {
    }

    public record MockSoapRequest(
            Document document
    ) {
    }
}
