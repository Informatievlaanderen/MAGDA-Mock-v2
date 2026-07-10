package be.vlaanderen.vip.magda.magdamock.client.handlers;

import be.vlaanderen.vip.magda.magdamock.client.exceptions.MagdaMockRestException;
import be.vlaanderen.vip.magda.magdamock.config.WireMockData;
import be.vlaanderen.vip.magda.magdamock.utils.TimeoutUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.http.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Slf4j
public class MagdaMockRestHandler extends AbstractMockHandler {
    public MagdaMockRestHandler(WireMockData wireMockData, TimeoutUtil timeoutUtil) {
        super(wireMockData, timeoutUtil);
    }

    public MockRestResponse sendRestRequest(MockRestRequest magdaRestRequest) {
        String query = magdaRestRequest.query();
        String path = magdaRestRequest.path();
        String method = magdaRestRequest.method();
        String requestBody = magdaRestRequest.requestBody();
        String correlationIdHeader = magdaRestRequest.headers().getOrDefault("x-correlation-id", "");
        String dateHeader =  magdaRestRequest.headers().getOrDefault("date", "");
        timeoutUtil.timeout();
        List<String> parts = new ArrayList<>();
        parts.add(wireMockServer.url(path));
        if (query != null && !query.isEmpty()) {
            parts.add(query);
        }

        String correlationId = Optional.ofNullable(correlationIdHeader).orElse(UUID.randomUUID().toString());

        Optional<Pair<String, Integer>> validationRequest = validateRestJson(requestBody, true);
        if (validationRequest.isPresent()) {
            Pair<String, Integer> jsonNodeIntegerPair = validationRequest.get();
            return new MagdaMockRestHandler.MockRestResponse(jsonNodeIntegerPair.getLeft().getBytes(), jsonNodeIntegerPair.getRight(), Map.of("x-correlation-id", List.of(correlationId)));
        }

        String url = String.join("?", parts);
        Request mockRequest = createInternalWiremockRequest(url, method, requestBody, dateHeader, "application/json");
        Response response = routeRequest(mockRequest);
        return parseRestResponse(response, correlationId);
    }

    public Optional<Pair<String, Integer>> validateRestJson(String requestBody, boolean request) {
        // Not a valid json request -> 400, response -> 502
        int statusCode = request ? 400 : 502;
        try {
            mapper.readTree(requestBody);
        } catch (IOException e) {
            ObjectNode node = mapper.createObjectNode();
            node.put("errorMessage", e.getMessage());
            node.put("exceptionClass", e.getClass().getName());
            return Optional.of(Pair.of(node.toPrettyString(), statusCode));
        }
        return Optional.empty();
    }

    private MagdaMockRestHandler.MockRestResponse parseRestResponse(Response response, String correlationId) {
        if (response.getStatus() == 404) {
            log.info("Received status 404 while parsing rest response");
            return new MockRestResponse(response.getBody(), 404, Map.of("x-correlation-id", List.of(correlationId), "Content-Type", List.of("application/json")));
        }
        Map<String, List<String>> headers = new HashMap<>();
        for (String headerName : response.getHeaders().keys()) {
            headers.put(headerName, response.getHeaders().getHeader(headerName).values());
        }
        if (!headers.containsKey("Content-Type")) {
            headers.put("Content-Type", List.of("application/json"));
        }
        headers.put("x-correlation-id", List.of(correlationId));
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
