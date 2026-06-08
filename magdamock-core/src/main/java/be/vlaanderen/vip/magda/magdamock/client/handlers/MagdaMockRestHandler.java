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

        Optional<Pair<JsonNode, Integer>> validationRequest = validateRestJson(requestBody, true);
        if (validationRequest.isPresent()) {
            Pair<JsonNode, Integer> jsonNodeIntegerPair = validationRequest.get();
            return new MagdaMockRestHandler.MockRestResponse(jsonNodeIntegerPair.getLeft(), jsonNodeIntegerPair.getRight(), Map.of("x-correlation-id", List.of(correlationId)));
        }

        String url = String.join("?", parts);
        Request mockRequest = createInternalWiremockRequest(url, method, requestBody, dateHeader, "application/json");
        Response response = routeRequest(mockRequest);
        Optional<Pair<JsonNode, Integer>> validationResponse = validateRestJson(response.getBodyAsString(), false);
        if (validationResponse.isPresent()) {
            Pair<JsonNode, Integer> jsonNodeIntegerPair = validationResponse.get();
            return new MagdaMockRestHandler.MockRestResponse(jsonNodeIntegerPair.getLeft(), jsonNodeIntegerPair.getRight(), Map.of("x-correlation-id", List.of(correlationId)));
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

    private MagdaMockRestHandler.MockRestResponse parseRestResponse(Response response, String correlationId) {
        try {
            if (response.getStatus() == 404) {
                log.info("Received status 404 while parsing rest response");
                return new MagdaMockRestHandler.MockRestResponse(null, 404, Map.of("x-correlation-id", List.of(correlationId)));
            }
            return new MagdaMockRestHandler.MockRestResponse(mapper.readTree(response.getBody()), response.getStatus(), Map.of("x-correlation-id", List.of(correlationId)));
        } catch (IOException e) {
            throw new MagdaMockRestException("Error simulating REST call", e.getCause());
        }
    }

    public record MockRestResponse(
            JsonNode body,
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
