package be.vlaanderen.vip.magda.magdamock.rest;

import be.vlaanderen.vip.magda.magdamock.config.MockRestMapping;
import com.github.erosb.kappa.core.exception.ResolutionException;
import com.github.erosb.kappa.core.validation.ValidationException;
import com.github.erosb.kappa.operation.validator.model.Request;
import com.github.erosb.kappa.operation.validator.model.Response;
import com.github.erosb.kappa.operation.validator.model.impl.Body;
import com.github.erosb.kappa.operation.validator.model.impl.DefaultRequest;
import com.github.erosb.kappa.operation.validator.model.impl.DefaultResponse;
import com.github.erosb.kappa.operation.validator.validation.RequestValidator;
import com.github.erosb.kappa.parser.OpenApi3Parser;
import com.github.erosb.kappa.parser.model.v3.OpenApi3;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.util.UriTemplate;

import java.io.File;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class RestOpenAPIValidator implements RestValidator {

    private final Path filesRoot;

    @SneakyThrows
    public RestOpenAPIValidator(Path filesRoot) {
        this.filesRoot = filesRoot;
    }

    private Optional<RequestValidator> getRequestValidator(String path, String method) throws ResolutionException, ValidationException {
        MockRestMapping mockRestMapping;
        Set<MockRestMapping> mockRestMappingStream = MockRestMapping.MAPPINGS.stream().filter(m ->
                new UriTemplate(m.url()).matches(path) && m.method().equals(method)).collect(Collectors.toSet());
        if (mockRestMappingStream.size() == 1) {
            mockRestMapping = mockRestMappingStream.iterator().next();
        } else {
            mockRestMapping = mockRestMappingStream.stream().filter(MockRestMapping::defaultOnly).findFirst().get();
        }

        Path openApiFilePath = filesRoot.resolve(mockRestMapping.getOpenApiPath());

        File file = openApiFilePath.toFile();
        if (file.exists()) {
            OpenApi3 apiDefinition = new OpenApi3Parser().parse(file, false);

            return Optional.of(new RequestValidator(apiDefinition));
        } else {
            log.warn("No openapi file found for {}", mockRestMapping.getId());
            return Optional.empty();
        }
    }

    public void validateRawRequest(
            String path,
            String query,
            String method,
            String requestBody,
            Map<String, Collection<String>> headers) throws ValidationException {
        try {
            Optional<RequestValidator> requestValidator = getRequestValidator(path, method);
            if (requestValidator.isEmpty()) {
                return;
            }
            RequestValidator validator = requestValidator.get();

            // 3. Translate the raw string method (e.g., "POST") to the Kappa Method enum
            Request.Method reqMethod = Request.Method.valueOf(method.toUpperCase());

            // 4. Construct the Kappa Request object using the Builder
            DefaultRequest.Builder builder = new DefaultRequest.Builder(path, reqMethod);

            // Note: The query string should be the raw string (e.g., "foo=bar&baz=1")
            if (query != null && !query.isEmpty()) {
                builder.query(query);
            }

            if (headers != null && !headers.isEmpty()) {
                // The builder expects a Map<String, Collection<String>> or Map<String, String>
                // depending on the precise version signature, but standard map headers work here.
                builder.headers(headers);
            }

            if (requestBody != null && !requestBody.isEmpty()) {
                // If you have a Content-Type, make sure it was provided in the headers map
                // so the validator knows how to parse the requestBody string!
                builder.body(Body.from(requestBody));
            }

            Request kappaRequest = builder.build();

            // 5. Execute the validation
            // If the validation succeeds, it returns silently.
            validator.validate(kappaRequest);

            log.debug("Request perfectly matches the OpenAPI contract!");
        } catch (IllegalArgumentException e) {
            log.warn("Invalid HTTP Method provided: {}", method);
        } catch (ResolutionException e) {
            throw new RuntimeException(e);
        }
    }

    public void validateResponseOnly(
            // Minimal request data needed for lookup
            String path,
            String method,
            // The actual response data to validate
            int statusCode,
            String resBody,
            Map<String, Collection<String>> resHeaders) throws ValidationException {

        try {
            Optional<RequestValidator> requestValidator = getRequestValidator(path, method);
            if (requestValidator.isEmpty()) {
                return;
            }
            RequestValidator validator = requestValidator.get();

            // 1. Build a bare-bones Request object just for context
            // You don't need the request body, headers, or query parameters here.
            Request.Method reqMethod = Request.Method.valueOf(method.toUpperCase());
            Request contextRequest = new DefaultRequest.Builder(path, reqMethod).build();

            // 2. Build the full Response object you want to validate
            DefaultResponse.Builder resBuilder = new DefaultResponse.Builder(statusCode);
            if (resHeaders != null) resBuilder.headers(resHeaders);
            if (resBody != null && !resBody.isEmpty()) {
                resBuilder.body(Body.from(resBody));
            }
            Response kappaResponse = resBuilder.build();

            // 3. Validate ONLY the response
            validator.validate(kappaResponse, contextRequest);

            log.debug("Response perfectly matches the OpenAPI contract!");
        } catch (ResolutionException e) {
            throw new RuntimeException(e);
        }
    }
}