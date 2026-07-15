package be.vlaanderen.vip.magda.magdamock.client;

import be.vlaanderen.vip.magda.magdamock.client.handlers.MagdaMockRestHandler;
import be.vlaanderen.vip.magda.magdamock.client.handlers.MagdaMockSoapHandler;
import be.vlaanderen.vip.magda.magdamock.client.rest.MockRestMapping;
import be.vlaanderen.vip.magda.magdamock.client.rest.RestDirectoryHandler;
import be.vlaanderen.vip.magda.magdamock.client.soap.Domain;
import be.vlaanderen.vip.magda.magdamock.client.soap.SoapStubRegistrar;
import be.vlaanderen.vip.magda.magdamock.config.EmbeddedWireMockBuilder;
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
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.tuple.Pair;
import org.w3c.dom.Document;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Slf4j
public class MagdaMockConnection {
    private final MagdaMockRestHandler restHandler;
    private final MagdaMockSoapHandler soapHandler;
    private final ObjectMapper mapper;

    MagdaMockConnection(MagdaMockRestHandler restHandler, MagdaMockSoapHandler soapHandler) {
        this.restHandler = restHandler;
        this.soapHandler = soapHandler;
        mapper = new ObjectMapper();
    }

    public static MagdaMockConnection create(WireMockData wiremockServerData, SoapBodyValidator soapRequestValidator, SoapBodyValidator soapResponseValidator) {
        TimeoutUtil timeoutUtil = new NoopTimeoutUtil();
        return create(wiremockServerData, soapRequestValidator, soapResponseValidator, timeoutUtil);
    }

    public static MagdaMockConnection create(WireMockData wiremockServerData, SoapBodyValidator soapRequestValidator, SoapBodyValidator soapResponseValidator, TimeoutUtil timeoutUtil) {
        MagdaMockRestHandler restHandler = new MagdaMockRestHandler(wiremockServerData, timeoutUtil);
        MagdaMockSoapHandler soapHandler = new MagdaMockSoapHandler(wiremockServerData, timeoutUtil, soapRequestValidator, soapResponseValidator);
        return new MagdaMockConnection(restHandler, soapHandler);
    }

    public static MagdaMockConnection create(String restDataPath, String soapTestPath, String xsdPath) throws IOException {
        return create(restDataPath, soapTestPath, xsdPath, null, null);
    }

    public static MagdaMockConnection create(String restDataPath, String soapTestPath, String xsdPath, Integer minimumTimeoutMillis, Integer maximumTimeoutMillis) throws IOException {
        List<MockRestMapping> mappings = MockRestMapping.MAPPINGS;
        return create(restDataPath, soapTestPath, xsdPath, minimumTimeoutMillis, maximumTimeoutMillis, mappings);
    }

    public static MagdaMockConnection create(String restDataPath, String soapTestPath, String xsdPath, Integer minimumTimeoutMillis, Integer maximumTimeoutMillis, List<MockRestMapping> mappings) throws IOException {
        List<Domain> domains = SoapResourceUtil.loadDomainsFromPaths(SoapResourceUtil.resolvePaths(soapTestPath));
        WireMockData wireMockData = new EmbeddedWireMockBuilder().soapTestPath(soapTestPath).build();
        SoapStubRegistrar soapStubRegistrar = new SoapStubRegistrar(wireMockData.wireMockServer(), soapTestPath);
        domains.forEach(soapStubRegistrar::registerDomain);

        if (restDataPath != null && !restDataPath.isEmpty()) {
            Path restPath = Path.of(restDataPath);
            List<RestDirectoryHandler> directoryHandlers = mappings.stream().map(mockRestMapping -> new RestDirectoryHandler(mockRestMapping, wireMockData.wireMockServer(), restPath)).toList();
            directoryHandlers.forEach(RestDirectoryHandler::addAllStubs);
        }

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
    @Deprecated
    public Document sendDocument(Document xml) throws SoapValidationError {
        return Optional.ofNullable(sendSoapRequest(new MagdaMockSoapHandler.MockSoapRequest(xml))).map(MagdaMockSoapHandler.MockSoapResponse::document).orElse(null);
    }

    public MagdaMockSoapHandler.MockSoapResponse sendSoapRequest(MagdaMockSoapHandler.MockSoapRequest mockSoapRequest) {
        return soapHandler.sendSoapRequest(mockSoapRequest);
    }

    @Deprecated
    @SneakyThrows
    public Pair<JsonNode, Integer> sendRestRequest(String path, String query, String method, String requestBody) {
        MagdaMockRestHandler.MockRestResponse mockRestResponse = sendRestRequest(path, query, method, requestBody, "", "");
        JsonNode jsonBody = mapper.readTree(mockRestResponse.body());
        return Pair.of(jsonBody, mockRestResponse.status());
    }

    @Deprecated
    public MagdaMockRestHandler.MockRestResponse sendRestRequest(String path, String query, String method, String requestBody, String dateHeader, String correlationIdHeader) {
        Map<String, String> headers = new HashMap<>();
        if (dateHeader != null) {
            headers.put("date", dateHeader);
        }
        if (correlationIdHeader == null) {
            correlationIdHeader = UUID.randomUUID().toString();
        }
        headers.put("x-correlation-id", correlationIdHeader);
        return sendRestRequest(
                new MagdaMockRestHandler.MockRestRequest(path, query, method, requestBody, headers)
        );
    }

    public MagdaMockRestHandler.MockRestResponse sendRestRequest(MagdaMockRestHandler.MockRestRequest magdaRestRequest) {
        return restHandler.sendRestRequest(magdaRestRequest);
    }
}
