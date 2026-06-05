package be.vlaanderen.vip.magda.magdamock.client.rest;

import be.vlaanderen.vip.magda.magdamock.client.MagdaMockConnection;
import be.vlaanderen.vip.magda.magdamock.client.handlers.MagdaMockRestHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

// NOTE: this test only contains test endpoints to ensure we have a controlled environment that is not influenced by the MockRestMapping configurations
public class RestServicesTest {

    MagdaMockConnection magdaMockConnection;

    @BeforeEach
    void setUp() throws IOException, URISyntaxException {
        Path path = Paths.get(getClass()
                .getClassLoader()
                .getResource("rest")
                .toURI());

        magdaMockConnection = MagdaMockConnection.create(path.toAbsolutePath().toString(), "", "", 0, 1, List.of(
                new MockRestMapping(List.of("mobility", "registrations", "test", "get"), List.of("plateNr", "vin"), 1, "/v1/mobility/registrations/%s", "GET"),
                new MockRestMapping(List.of("mobility", "registrations", "test", "delete"), List.of("plateNr", "vin"), 1, "/v1/mobility/registrations/%s", "DELETE")
        ));
    }

    @Test
    void testMobilityRegCountryCodeGet() {
        var response = magdaMockConnection.sendRestRequest(new MagdaMockRestHandler.MockRestRequest(
                "/v1/mobility/registrations/BE",
                "",
                "GET",
                "",
                Map.of()
        ));
        Assertions.assertNotNull(response);
        Assertions.assertEquals("/registrations/BE", response.body().get("self").textValue());
        Assertions.assertEquals(202, response.status());
    }

    @Test
    void testMobilityRegCountryCodeDelete() {
        var response = magdaMockConnection.sendRestRequest(new MagdaMockRestHandler.MockRestRequest(
                "/v1/mobility/registrations/BE",
                "",
                "DELETE",
                "",
                Map.of()
        ));
        Assertions.assertNotNull(response);
        Assertions.assertEquals(204, response.status());
    }

    @Test
    void testMobilityRegLicensePlateGet() {
        var response = magdaMockConnection.sendRestRequest(new MagdaMockRestHandler.MockRestRequest(
                "/v1/mobility/registrations/BE",
                "plateNr=MIJNAUTO",
                "GET",
                "",
                Map.of()
        ));
        Assertions.assertNotNull(response);
        Assertions.assertEquals("/registrations?plateNr=MIJNAUTO", response.body().get("self").textValue());
        Assertions.assertEquals(200, response.status());
    }

    @Test
    void testMobilityRegLicensePlateAndVinGet() {
        var response = magdaMockConnection.sendRestRequest(new MagdaMockRestHandler.MockRestRequest(
                "/v1/mobility/registrations/BE",
                "plateNr=MIJNAUTO&vin=MYVIN",
                "GET",
                "",
                Map.of()
        ));
        Assertions.assertNotNull(response);
        Assertions.assertEquals(418, response.status());
    }

}
