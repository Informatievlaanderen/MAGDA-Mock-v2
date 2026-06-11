package be.vlaanderen.vip.magda.magdamock.client.rest;

import be.vlaanderen.vip.magda.magdamock.client.MagdaMockConnection;
import be.vlaanderen.vip.magda.magdamock.client.handlers.MagdaMockRestHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.stream.Stream;

// NOTE: this test only contains test endpoints to ensure we have a controlled environment that is not influenced by the MockRestMapping configurations
public class RestServicesTest {

    MagdaMockConnection magdaMockConnection;

    static Stream<Arguments> testRestServices() throws IOException, URISyntaxException {
        return Stream.of(
                Arguments.of(
                        new MagdaMockRestHandler.MockRestRequest(
                                "/v1/mobility/registrations",
                                "plateNr=123ABC",
                                "GET",
                                "",
                                Map.of()
                        ),
                        "rest mobility plate mapping",
                        "specific"
                ),
                Arguments.of(
                        new MagdaMockRestHandler.MockRestRequest(
                                "/v1/mobility/registrations",
                                "plateNr=unknown",
                                "GET",
                                "",
                                Map.of()
                        ),
                        "rest mobility plate mapping",
                        "default"
                ), Arguments.of(
                        new MagdaMockRestHandler.MockRestRequest(
                                "/v1/mobility/registrations",
                                "vin=VIN-123",
                                "GET",
                                "",
                                Map.of()
                        ),
                        "rest mobility vin mapping",
                        "specific"
                ), Arguments.of(
                        new MagdaMockRestHandler.MockRestRequest(
                                "/v1/mobility/registrations",
                                "vin=default",
                                "GET",
                                "",
                                Map.of()
                        ),
                        "rest mobility plate mapping",
                        "default"
                ), Arguments.of(
                        new MagdaMockRestHandler.MockRestRequest(
                                "/v1/socZek/socialeHuisvesting/dossiers/zoeken",
                                "",
                                "POST",
                                """
                                        {"dossierStatus":"REG"}
                                        """,
                                Map.of()
                        ),
                        "dossierzoeken post dossierstatus mapping",
                        "specific"
                ), Arguments.of(
                        new MagdaMockRestHandler.MockRestRequest(
                                "/v1/socZek/socialeHuisvesting/dossiers/zoeken",
                                "",
                                "POST",
                                "",
                                Map.of()
                        ),
                        "dossierzoeken post dossierstatus mapping",
                        "default"
                )
        );
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
    @MethodSource("testRestServices")
    void testMobilityRegCountryCodeGet(
            MagdaMockRestHandler.MockRestRequest mockRestRequest,
            String expectedMessage,
            String expectedMappingType
    ) {
        var response = magdaMockConnection.sendRestRequest(mockRestRequest);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(expectedMessage, response.body().get("message").textValue());
        Assertions.assertEquals(expectedMappingType, response.body().get("mappingType").textValue());
        Assertions.assertEquals(200, response.status());
    }
}
