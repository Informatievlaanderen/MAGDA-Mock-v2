package be.vlaanderen.vip.magda.magdamock.client.handlers;

import be.vlaanderen.vip.magda.magdamock.client.logging.LifecyclePhase;
import be.vlaanderen.vip.magda.magdamock.client.logging.RestLogHelper;
import be.vlaanderen.vip.magda.magdamock.config.WireMockData;
import be.vlaanderen.vip.magda.magdamock.utils.TimeoutUtil;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.http.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Slf4j
public class MagdaMockRestHandler extends AbstractMockHandler {
    private final String HEADER_KEY_CORRELATION_ID = "x-correlation-id";
    private boolean logRequestBody;

    public MagdaMockRestHandler(WireMockData wireMockData, TimeoutUtil timeoutUtil, boolean logRequestBody) {
        super(wireMockData, timeoutUtil);
        this.logRequestBody = logRequestBody;
    }

    public MockRestResponse sendRestRequest(MockRestRequest magdaRestRequest) {
        RestLogHelper.contextSetLifecyclePhase(LifecyclePhase.REQUEST_VALIDATION);
        Optional<MockRestResponse> validationError = validateRestRequest(magdaRestRequest);
        if (validationError.isPresent()) {
            // NOTE: currently there's no validation for content of rest requests
            return validationError.get();
        }

        RestLogHelper.contextSetLifecyclePhase(LifecyclePhase.REQUEST_PRE_PROCESSING);
        String correlationIdHeader = magdaRestRequest.headers().getOrDefault(HEADER_KEY_CORRELATION_ID, "");
        String correlationId = Optional.ofNullable(correlationIdHeader).orElse(UUID.randomUUID().toString());
        String dateHeader =  magdaRestRequest.headers().getOrDefault("date", "");
        timeoutUtil.timeout();

        RestLogHelper.contextSetLifecyclePhase(LifecyclePhase.RESPONSE_MAPPING);
        String query = magdaRestRequest.query();
        String path = magdaRestRequest.path();
        String method = magdaRestRequest.method();
        String requestBody = magdaRestRequest.requestBody();
        List<String> parts = new ArrayList<>();
        parts.add(wireMockServer.url(path));
        if (query != null && !query.isEmpty()) {
            parts.add(query);
        }
        RestLogHelper.contextSetRestServiceNameVersion(method, path);
        log.debug("Query: {}", query);
        log.debug("Path: {}", path);
        log.debug("Method: {}", method);
        if (logRequestBody)
            log.debug("Request body: {}", requestBody);

        String url = String.join("?", parts);
        Request mockRequest = createInternalWiremockRequest(url, method, requestBody, dateHeader, "application/json");
        Response response = routeRequest(mockRequest);
        RestLogHelper.contextSetLifecyclePhase(LifecyclePhase.RESPONSE_POST_PROCESSING);
        return parseRestResponse(magdaRestRequest, response, correlationId);
    }

    private Optional<MagdaMockRestHandler.MockRestResponse> validateRestRequest(MockRestRequest magdaRestRequest) {
        String correlationId = magdaRestRequest.headers.get(HEADER_KEY_CORRELATION_ID);
        if (correlationId == null ||  correlationId.isEmpty()) {
            log.error("Header parameter 'x-correlation-id' is required.");
            return Optional.of(new MockRestResponse(String.format("""
                    {
                      "type": "https://iv.api.vlaanderen.be/magda/statuscodes#400",
                      "status": 400,
                      "error": "Bad Request",
                      "message": "Header parameter 'x-correlation-id' is required.",
                      "instance": "%s %s"
                    }
                    """, magdaRestRequest.method, magdaRestRequest.path).getBytes(StandardCharsets.UTF_8),
                    400, Map.of(HEADER_KEY_CORRELATION_ID, List.of(Optional.ofNullable(correlationId).orElse(UUID.randomUUID().toString())), "Content-Type", List.of("application/json"))));
        }
        return Optional.empty();
    }

    private MagdaMockRestHandler.MockRestResponse parseRestResponse(MockRestRequest magdaRestRequest, Response response, String correlationId) {
        if (response.getStatus() == 666) {
            log.info("Received status 666 while parsing rest response");
            log.error("Response mapping is undefined");
            return new MockRestResponse(String.format("""
                    {
                      "type": "https://iv.api.vlaanderen.be/magda/statuscodes#500",
                      "status": 500,
                      "error": "Internal Server Error",
                      "message": "Response mapping is undefined.",
                      "instance": "%s %s"
                    }
                    """, magdaRestRequest.method, magdaRestRequest.path).getBytes(StandardCharsets.UTF_8),
                    500, Map.of(HEADER_KEY_CORRELATION_ID, List.of(correlationId), "Content-Type", List.of("application/json")));
        }
        Map<String, List<String>> headers = new HashMap<>();
        for (String headerName : response.getHeaders().keys()) {
            headers.put(headerName, response.getHeaders().getHeader(headerName).values());
        }
        if (!headers.containsKey("Content-Type")) {
            headers.put("Content-Type", List.of("application/json"));
        }
        headers.put(HEADER_KEY_CORRELATION_ID, List.of(correlationId));
        return new MockRestResponse(response.getBody(), response.getStatus(), headers);
    }

    public record MockRestResponse(
            byte[] body,
            Integer status,
            Map<String, List<String>> headers
    ) {
    }

    public record MockRestRequest(
            String path,
            String query,
            String method,
            String requestBody,
            Map<String, String> headers
    ) {
    }
}
