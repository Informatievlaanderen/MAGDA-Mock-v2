package be.vlaanderen.vip.magda.magdamock.client.rest;

import be.vlaanderen.vip.magda.magdamock.client.MagdaMockConnection;
import be.vlaanderen.vip.magda.magdamock.client.handlers.MagdaMockRestHandler;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.arguments;

// NOTE: this test only contains test endpoints to ensure we have a controlled environment that is not influenced by the MockRestMapping configurations
class RestServicesTest {

    @Nested
    class Services {
        MagdaMockConnection magdaMockConnection;

        static Stream<Arguments> allRestServices(String filename) {
            ObjectMapper objectMapper = new ObjectMapper();

            try (var inputStream = RestServicesTest.class
                    .getClassLoader()
                    .getResourceAsStream(filename)) {

                if (inputStream == null) {
                    throw new IllegalStateException(String.format("Resource not found: %s", filename));
                }

                List<RestServiceArgument> cases = objectMapper.readValue(
                        inputStream,
                        new TypeReference<List<RestServiceArgument>>() {
                        }
                );

                return cases.stream()
                        .map(c -> arguments(
                                new MagdaMockRestHandler.MockRestRequest(
                                        c.path(),
                                        c.query(),
                                        c.method(),
                                        c.requestBody(),
                                        c.headers()
                                ),
                                c.expectedMessage(),
                                c.expectedMappingType(),
                                c.expectedContentTypeHeader()
                        ));
            } catch (IOException e) {
                throw new IllegalStateException("Unable to load REST service arguments from JSON", e);
            }
        }

        static Stream<Arguments> allMobilityServices() {
            return allRestServices("rest/test-definitions/mobility-services.json");
        }

        static Stream<Arguments> allOrganisatiesServices() {
            return allRestServices("rest/test-definitions/organisaties-services.json");
        }

        static Stream<Arguments> allSocZekServices() {
            return allRestServices("rest/test-definitions/soczek-services.json");
        }

        @BeforeEach
        void setUp() throws IOException, URISyntaxException {
            Path path = Paths.get(getClass()
                    .getClassLoader()
                    .getResource("rest")
                    .toURI());

            magdaMockConnection = MagdaMockConnection.create(path.toAbsolutePath().toString(), "", "");
        }

        @ParameterizedTest
        @MethodSource("allMobilityServices")
        @SneakyThrows
        void testMobilityRestService(
                MagdaMockRestHandler.MockRestRequest mockRestRequest,
                String expectedMessage,
                String expectedMappingType,
                String expectedContentTypeHeader
        ) {
            testRestService(mockRestRequest, expectedMessage, expectedMappingType, expectedContentTypeHeader);
        }

        @ParameterizedTest
        @MethodSource("allOrganisatiesServices")
        @SneakyThrows
        void testOrganisatiesRestService(
                MagdaMockRestHandler.MockRestRequest mockRestRequest,
                String expectedMessage,
                String expectedMappingType,
                String expectedContentTypeHeader
        ) {
            testRestService(mockRestRequest, expectedMessage, expectedMappingType, expectedContentTypeHeader);
        }

        @ParameterizedTest
        @MethodSource("allSocZekServices")
        @SneakyThrows
        void testSocZekRestService(
                MagdaMockRestHandler.MockRestRequest mockRestRequest,
                String expectedMessage,
                String expectedMappingType,
                String expectedContentTypeHeader
        ) {
            testRestService(mockRestRequest, expectedMessage, expectedMappingType, expectedContentTypeHeader);
        }

        void testRestService(
                MagdaMockRestHandler.MockRestRequest mockRestRequest,
                String expectedMessage,
                String expectedMappingType,
                String expectedContentTypeHeader
        ) throws Exception {
            Map<String, String> headers = new HashMap<>(mockRestRequest.headers());
            headers.putIfAbsent("x-correlation-id", UUID.randomUUID().toString());
            var restRequest = new MagdaMockRestHandler.MockRestRequest(
                    mockRestRequest.path(),
                    mockRestRequest.query(),
                    mockRestRequest.method(),
                    mockRestRequest.requestBody(),
                    headers
            );
            var response = magdaMockConnection.sendRestRequest(restRequest);
            Assertions.assertNotNull(response);
            JsonNode jsonBody = new ObjectMapper().readTree(response.body());
            Assertions.assertEquals(expectedMessage, jsonBody.get("message").textValue());
            Assertions.assertEquals(expectedMappingType, jsonBody.get("mappingType").textValue());
            Assertions.assertTrue(response.headers().get("Content-Type").contains(expectedContentTypeHeader));
            Assertions.assertEquals(200, response.status());
        }

        record RestServiceArgument(
                String path,
                String query,
                String method,
                String requestBody,
                Map<String, String> headers,
                String expectedMessage,
                String expectedMappingType,
                String expectedContentTypeHeader
        ) {
        }
    }

    @Nested
    class ExceptionTests {

        private MagdaMockConnection magdaMockConnection;

        @BeforeEach
        void setUp() throws IOException, URISyntaxException {
            Path path = Paths.get(getClass()
                    .getClassLoader()
                    .getResource("rest")
                    .toURI());

            magdaMockConnection = MagdaMockConnection.create(path.toAbsolutePath().toString(), "", "");
        }


        @Test
        public void whenUnknownServiceIsGiven_shouldReturn500() {
            var response = magdaMockConnection.sendRestRequest(
                    new MagdaMockRestHandler.MockRestRequest(
                            "/unknown/path",
                            "",
                            "TEST",
                            "",
                            Map.of("x-correlation-id", UUID.randomUUID().toString())
                    )
            );
            Assertions.assertEquals(500, response.status());
        }

        @Test
        public void whenCorrelationIdIsMissing_shouldReturn400() {
            var response = magdaMockConnection.sendRestRequest(
                    new MagdaMockRestHandler.MockRestRequest(
                            "/unknown/path",
                            "",
                            "TEST",
                            "",
                            Map.of()
                    )
            );
            Assertions.assertEquals(400, response.status());
        }
    }
}
