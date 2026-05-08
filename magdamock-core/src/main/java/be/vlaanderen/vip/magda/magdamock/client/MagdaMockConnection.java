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
import be.vlaanderen.vip.magda.magdamock.utils.SoapResourceUtil;
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

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class MagdaMockConnection implements MagdaConnection {

    private final WireMockServer wireMockServer;
    private final ObjectMapper mapper;
    private final DirectCallHttpServer internalWiremockHttpServer;
    private final SoapResponsePatcher soapResponsePatcher = new SoapResponsePatcherImpl();
    private final SoapBodyValidator soapRequestValidator;
    private final SoapBodyValidator soapResponseValidator;


    MagdaMockConnection(WireMockData wiremockServerData, SoapBodyValidator soapRequestValidator, SoapBodyValidator soapResponseValidator) {
        this.wireMockServer = wiremockServerData.wireMockServer();
        internalWiremockHttpServer = wiremockServerData.factory().getHttpServer();
        this.soapRequestValidator = soapRequestValidator;
        this.soapResponseValidator = soapResponseValidator;
        mapper = new ObjectMapper();
    }

    public static MagdaMockConnection create(WireMockData wiremockServerData, SoapBodyValidator soapRequestValidator, SoapBodyValidator soapResponseValidator) {
        return new MagdaMockConnection(wiremockServerData, soapRequestValidator, soapResponseValidator);
    }

    public static MagdaMockConnection create(String testDataPath, String soapTestPath, String xsdPath) throws IOException {
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
        return create(wireMockData, soapRequestValidator, soapResponseValidator);
    }

    @Override
    public Document sendDocument(Document xml) {
        MagdaDocument request = MagdaDocument.fromDocument(xml);
        Optional<Document> requestValidationError = soapRequestValidator.validateXml(request);
        if (requestValidationError.isPresent()) {
            return wrapInEnvelope(requestValidationError.get());
        }
        String dateHeader = getDateHeaderFromSoapRequest(request);
        String soapUrl = wireMockServer.url("/soap");
        Request mockRequest = createInternalWiremockRequest(soapUrl, "POST", request.toString(), dateHeader, "text/xml");
        Response response = routeRequest(mockRequest);
        if (response.getStatus() == 404) {
            return null;
        }
        Document document = parseSoapResponse(response);
        Document patchedResponse = patchResponse(request, document);
        Optional<Document> responseValidationError = soapResponseValidator.validateXml(MagdaDocument.fromDocument(patchedResponse));
        if (responseValidationError.isPresent()) {
            return wrapInEnvelope(responseValidationError.get());
        }
        return wrapInEnvelope(patchedResponse);
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
            return sendRestRequest(path, queryParams, method, "", dateHeader);
        } catch (URISyntaxException e) {
            throw new MagdaMockRestException("Error simulating REST call", e.getCause());
        }
    }

    @Override
    public Pair<JsonNode, Integer> sendRestRequest(String path, String query, String method, String requestBody) {
        return sendRestRequest(path, query, method, requestBody, "");
    }

    public Pair<JsonNode, Integer> sendRestRequest(String path, String query, String method, String requestBody, String dateHeader) {
        List<String> parts = new ArrayList<>();
        parts.add(wireMockServer.url(path));
        if (query != null && !query.isEmpty()) {
            parts.add(query);
        }

        Optional<Pair<JsonNode, Integer>> validationRequest = validateRestJson(requestBody, true);
        if (validationRequest.isPresent()) {
            return validationRequest.get();
        }

        String url = String.join("?", parts);
        Request mockRequest = createInternalWiremockRequest(url, method, requestBody, dateHeader, "application/json");
        Response response = routeRequest(mockRequest);
        Optional<Pair<JsonNode, Integer>> validationResponse = validateRestJson(response.getBodyAsString(), false);
        if (validationResponse.isPresent()) {
            return validationResponse.get();
        }

        return parseRestResponse(response);
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

    private Pair<JsonNode, Integer> parseRestResponse(Response response) {
        try {
            if (response.getStatus() == 404) {
                log.info("Received status 404 while parsing rest response");
                return Pair.of(null, 404);
            }
            return Pair.of(mapper.readTree(response.getBody()), response.getStatus());
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
}
