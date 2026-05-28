package be.vlaanderen.vip.magda.magdamock.client;

import be.vlaanderen.vip.magda.client.MagdaDocument;
import be.vlaanderen.vip.magda.client.connection.MagdaConnection;
import be.vlaanderen.vip.magda.client.domeinservice.MagdaRegistrationInfo;
import be.vlaanderen.vip.magda.client.rest.MagdaRestRequest;
import be.vlaanderen.vip.magda.magdamock.client.exceptions.MagdaMockRestException;
import be.vlaanderen.vip.magda.magdamock.client.soap.Domain;
import be.vlaanderen.vip.magda.magdamock.client.soap.SoapResponsePatcher;
import be.vlaanderen.vip.magda.magdamock.client.soap.SoapResponsePatcherImpl;
import be.vlaanderen.vip.magda.magdamock.client.soap.SoapStubRegistrar;
import be.vlaanderen.vip.magda.magdamock.config.EmbeddedWireMockBuilder;
import be.vlaanderen.vip.magda.magdamock.config.MockRestMagdaEndpoints;
import be.vlaanderen.vip.magda.magdamock.config.WireMockData;
import be.vlaanderen.vip.magda.magdamock.soap.LenientSoapBodyValidator;
import be.vlaanderen.vip.magda.magdamock.soap.SoapBodyValidator;
import be.vlaanderen.vip.magda.magdamock.soap.SoapRequestValidatorImpl;
import be.vlaanderen.vip.magda.magdamock.soap.SoapResponseValidatorImpl;
import be.vlaanderen.vip.magda.magdamock.soap.SoapValidationError;
import be.vlaanderen.vip.magda.magdamock.utils.NoopTimeoutUtil;
import be.vlaanderen.vip.magda.magdamock.utils.RandomTimeoutUtil;
import be.vlaanderen.vip.magda.magdamock.utils.SoapResourceUtil;
import be.vlaanderen.vip.magda.magdamock.utils.TimeoutUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.common.Urls;
import com.github.tomakehurst.wiremock.direct.DirectCallHttpServer;
import com.github.tomakehurst.wiremock.http.ContentTypeHeader;
import com.github.tomakehurst.wiremock.http.Cookie;
import com.github.tomakehurst.wiremock.http.FormParameter;
import com.github.tomakehurst.wiremock.http.HttpHeader;
import com.github.tomakehurst.wiremock.http.HttpHeaders;
import com.github.tomakehurst.wiremock.http.QueryParameter;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.http.RequestMethod;
import com.github.tomakehurst.wiremock.http.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

// NOTE: the implementation of MagdaConnection is to remain backwards compatible with magdamock.service
@Slf4j
public class MagdaMockConnection implements MagdaConnection {

    private final WireMockServer wireMockServer;
    private final ObjectMapper mapper;
    private final DirectCallHttpServer internalWiremockHttpServer;
    private final SoapResponsePatcher soapResponsePatcher = new SoapResponsePatcherImpl();
    private final SoapBodyValidator soapRequestValidator;
    private final SoapBodyValidator soapResponseValidator;
    private final TimeoutUtil timeoutUtil;
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");


    MagdaMockConnection(WireMockData wiremockServerData, SoapBodyValidator soapRequestValidator, SoapBodyValidator soapResponseValidator) {
        this.wireMockServer = wiremockServerData.wireMockServer();
        internalWiremockHttpServer = wiremockServerData.factory().getHttpServer();
        this.soapRequestValidator = soapRequestValidator;
        this.soapResponseValidator = soapResponseValidator;
        mapper = new ObjectMapper();
        this.timeoutUtil = new NoopTimeoutUtil();
    }

    MagdaMockConnection(WireMockData wiremockServerData, SoapBodyValidator soapRequestValidator, SoapBodyValidator soapResponseValidator, TimeoutUtil timeoutUtil) {
        this.wireMockServer = wiremockServerData.wireMockServer();
        internalWiremockHttpServer = wiremockServerData.factory().getHttpServer();
        this.soapRequestValidator = soapRequestValidator;
        this.soapResponseValidator = soapResponseValidator;
        mapper = new ObjectMapper();
        this.timeoutUtil = timeoutUtil;
    }

    public static MagdaMockConnection create(WireMockData wiremockServerData, SoapBodyValidator soapRequestValidator, SoapBodyValidator soapResponseValidator) {
        return new MagdaMockConnection(wiremockServerData, soapRequestValidator, soapResponseValidator);
    }

    public static MagdaMockConnection create(WireMockData wiremockServerData, SoapBodyValidator soapRequestValidator, SoapBodyValidator soapResponseValidator, TimeoutUtil timeoutUtil) {
        return new MagdaMockConnection(wiremockServerData, soapRequestValidator, soapResponseValidator, timeoutUtil);
    }

    public static MagdaMockConnection create(String testDataPath, String soapTestPath, String xsdPath) throws IOException {
        return create(testDataPath, soapTestPath, xsdPath, null, null);
    }

    public static MagdaMockConnection create(String testDataPath, String soapTestPath, String xsdPath, Integer minimumTimeoutMillis, Integer maximumTimeoutMillis) throws IOException {
        List<Domain> domains = SoapResourceUtil.loadDomainsFromPaths(SoapResourceUtil.resolvePaths(soapTestPath));
        WireMockData wireMockData = EmbeddedWireMockBuilder.wireMockServer(testDataPath, soapTestPath);
        SoapStubRegistrar soapStubRegistrar = new SoapStubRegistrar(wireMockData.wireMockServer(), soapTestPath);
        domains.forEach(soapStubRegistrar::registerDomain);
        SoapBodyValidator soapRequestValidator, soapResponseValidator;
        if (xsdPath == null || xsdPath.isBlank()) {
            soapResponseValidator = soapRequestValidator = new LenientSoapBodyValidator();
        } else {
            soapRequestValidator = new SoapRequestValidatorImpl(xsdPath);
            soapResponseValidator = new SoapResponseValidatorImpl(xsdPath);
        }
        TimeoutUtil timeoutUtil = new NoopTimeoutUtil();
        if (minimumTimeoutMillis != null && maximumTimeoutMillis != null) {
            timeoutUtil = new RandomTimeoutUtil(minimumTimeoutMillis, maximumTimeoutMillis);
        }
        return create(wireMockData, soapRequestValidator, soapResponseValidator, timeoutUtil);
    }

    // NOTE: this function is to remain backwards compatible with magdamock.service
    @Override
    public Document sendDocument(Document xml) throws SoapValidationError {
        timeoutUtil.timeout();
        MagdaDocument request = MagdaDocument.fromDocument(xml);
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
        Document checkedResponse = validateSoapResponse(request, patchedResponse);
        return wrapInEnvelope(checkedResponse);
    }

    private Document validateSoapResponse(MagdaDocument request, Document response) throws SoapValidationError {
        response = validateSoapSender(request, response);
        soapResponseValidator.validateXml(MagdaDocument.fromDocument(response));
        return response;
    }

    private Document validateSoapSender(MagdaDocument request, Document response) {
        String identification = request.getValue("//Afzender/Identificatie");
        LocalDateTime now = LocalDateTime.now();

        if (identification == null || identification.isBlank()) {
            Node uitzonderingenNode = MagdaDocument.fromString(String.format("""
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

    private String getDateHeaderFromSoapRequest(MagdaDocument request) {
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


    private Document wrapInEnvelope(Document bodyDocument) {
        MagdaDocument magdaDocument = MagdaDocument.fromDocument(bodyDocument);
        var soap = """
                <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" >
                <soapenv:Header/>
                    <soapenv:Body>
                    %s
                    </soapenv:Body>
                </soapenv:Envelope>""".formatted(magdaDocument);

        return MagdaDocument.fromString(soap).getXml();
    }

    // NOTE: this function is to remain backwards compatible with magdamock.service
    @Override
    public Pair<JsonNode, Integer> sendRestRequest(MagdaRestRequest request, MagdaRegistrationInfo registrationInfo) {
        String queryParams = request.getUrlQueryParams().entrySet().stream().map((kv) -> String.format("%s=%s", kv.getKey(), kv.getValue())).collect(Collectors.joining("&"));
        String method = request.getMethod().name();

        try {
            String stubUrl = "http://stub";
            MockRestMagdaEndpoints endpoints = new MockRestMagdaEndpoints(new URI(stubUrl));
            List<String> parts = new ArrayList<>(Arrays.stream(endpoints.magdaUri(request.getDienst()).toString().split(stubUrl)).toList());
            parts.removeFirst();
            String path = String.join(stubUrl, parts);
            String dateHeader = request.getHeaders().get("Date");
            MockRestResponse mockRestResponse = sendRestRequest(path, queryParams, method, "", dateHeader, "");
            return Pair.of(mockRestResponse.body(), mockRestResponse.status());
        } catch (URISyntaxException e) {
            throw new MagdaMockRestException("Error simulating REST call", e.getCause());
        }
    }

    // NOTE: this function is to remain backwards compatible with magdamock.service
    @Override
    public Pair<JsonNode, Integer> sendRestRequest(String path, String query, String method, String requestBody) {
        MockRestResponse mockRestResponse = sendRestRequest(path, query, method, requestBody, "", "");
        return Pair.of(mockRestResponse.body(), mockRestResponse.status());
    }

    public MockRestResponse sendRestRequest(String path, String query, String method, String requestBody, String dateHeader, String correlationIdHeader) {
        timeoutUtil.timeout();
        List<String> parts = new ArrayList<>();
        parts.add(wireMockServer.url(path));
        if (query != null && !query.isEmpty()) {
            parts.add(query);
        }

        String correlationId = Optional.ofNullable(correlationIdHeader).orElse(UUID.randomUUID().toString());

        Optional<Pair<JsonNode, Integer>> validationRequest = validateRestJson(requestBody, true);
        if (validationRequest.isPresent()) {
            Pair<JsonNode, Integer> jsonNodeIntegerPair = validationRequest.get();
            return new MockRestResponse(jsonNodeIntegerPair.getLeft(), jsonNodeIntegerPair.getRight(), Map.of("x-correlation-id", List.of(correlationId)));
        }

        String url = String.join("?", parts);
        Request mockRequest = createInternalWiremockRequest(url, method, requestBody, dateHeader, "application/json");
        Response response = routeRequest(mockRequest);
        Optional<Pair<JsonNode, Integer>> validationResponse = validateRestJson(response.getBodyAsString(), false);
        if (validationResponse.isPresent()) {
            Pair<JsonNode, Integer> jsonNodeIntegerPair = validationResponse.get();
            return new MockRestResponse(jsonNodeIntegerPair.getLeft(), jsonNodeIntegerPair.getRight(), Map.of("x-correlation-id", List.of(correlationId)));
        }

        return parseRestResponse(response, correlationId);
    }

    public Optional<Pair<JsonNode, Integer>> validateRestJson(String requestBody, boolean request) {
        // Not a valid json request -> 400, response -> 502
        int statusCode = request ? 400 : 502;
        try {
            mapper.readTree(requestBody);
        } catch (IOException e) {
            ObjectNode node = mapper.createObjectNode();
            node.put("errorMessage", e.getMessage());
            node.put("exceptionClass", e.getClass().getName());
            return Optional.of(Pair.of(node, statusCode));
        }
        return Optional.empty();
    }

    private MockRestResponse parseRestResponse(Response response, String correlationId) {
        try {
            if (response.getStatus() == 404) {
                log.info("Received status 404 while parsing rest response");
                return new MockRestResponse(null, 404, Map.of("x-correlation-id", List.of(correlationId)));
            }
            return new MockRestResponse(mapper.readTree(response.getBody()), response.getStatus(), Map.of("x-correlation-id", List.of(correlationId)));
        } catch (IOException e) {
            throw new MagdaMockRestException("Error simulating REST call", e.getCause());
        }
    }

    private Document parseSoapResponse(Response response) {
        return MagdaDocument.fromString(response.getBodyAsString()).getXml();
    }

    private Document patchResponse(MagdaDocument request, Document document) {
        return soapResponsePatcher.patchResponse(request, document).getXml();
    }

    private Response routeRequest(Request request) {
        return internalWiremockHttpServer.stubRequest(request);
    }

    // As there need to be certain parameters filled in to avoid wiremock throwing nullpointers while templating, we create the request ourselves
    private Request createInternalWiremockRequest(String url, String method, String requestBody, String dateHeader, String contentType) {
        if (dateHeader == null) {
            dateHeader = "";
        }
        HttpHeaders httpHeaders = new HttpHeaders(new HttpHeader("Date", dateHeader));
        return new Request() {
            @Override
            public String getUrl() {
                return Urls.getPathAndQuery(url);
            }

            @Override
            public String getAbsoluteUrl() {
                return url;
            }

            @Override
            public RequestMethod getMethod() {
                return RequestMethod.fromString(method);
            }

            @Override
            public String getScheme() {
                return "";
            }

            @Override
            public String getHost() {
                return "";
            }

            @Override
            public int getPort() {
                return wireMockServer.port();
            }

            @Override
            public String getClientIp() {
                return "";
            }

            @Override
            public String getHeader(String key) {
                List<String> values = header(key).getValues();
                if (values.isEmpty()) {
                    return "";
                }
                return values.getFirst();
            }

            @Override
            public HttpHeader header(String key) {
                return getHeaders().getHeader(key);
            }

            @Override
            public ContentTypeHeader contentTypeHeader() {
                return new ContentTypeHeader(contentType);
            }

            @Override
            public HttpHeaders getHeaders() {
                return httpHeaders;
            }

            @Override
            public boolean containsHeader(String key) {
                return !getHeader(key).isEmpty();
            }

            @Override
            public Set<String> getAllHeaderKeys() {
                return getHeaders().keys();
            }

            @Override
            public QueryParameter queryParameter(String key) {
                return null;
            }

            @Override
            public FormParameter formParameter(String key) {
                return null;
            }

            @Override
            public Map<String, FormParameter> formParameters() {
                return Map.of();
            }

            @Override
            public Map<String, Cookie> getCookies() {
                return Map.of();
            }

            @Override
            public byte[] getBody() {
                return requestBody.getBytes();
            }

            @Override
            public String getBodyAsString() {
                return "";
            }

            @Override
            public String getBodyAsBase64() {
                return "";
            }

            @Override
            public boolean isMultipart() {
                return false;
            }

            @Override
            public Collection<Part> getParts() {
                return List.of();
            }

            @Override
            public Part getPart(String name) {
                return null;
            }

            @Override
            public boolean isBrowserProxyRequest() {
                return false;
            }

            @Override
            public Optional<Request> getOriginalRequest() {
                return Optional.empty();
            }

            @Override
            public String getProtocol() {
                return "";
            }
        };
    }


    public record MockRestResponse(
            JsonNode body,
            Integer status,
            Map<String, List<String>> headers
    ) {
    }
}
