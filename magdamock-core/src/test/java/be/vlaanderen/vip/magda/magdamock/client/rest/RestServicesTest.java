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
class RestServicesTest {

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
                                "/v1/socZek/socialeHuisvesting/dossiers/zoeken",
                                "",
                                "POST",
                                """
                                        {"dossierType":"RFH", "dossierNummer":"2023.01.11.0016"}
                                        """,
                                Map.of()
                        ),
                        "dossierzoeken post dossierNummer en dossierType",
                        "specific"
                ), Arguments.of(
                        new MagdaMockRestHandler.MockRestRequest(
                                "/v1/socZek/socialeHuisvesting/dossiers/zoeken",
                                "",
                                "POST",
                                """
                                        {"dossierType":"RFH", "dossierNummer":"2023.01.11.0016", "dossierStatus":"CHKO"}
                                        """,
                                Map.of()
                        ),
                        "dossierzoeken post dossierNummer, dossierType en dossierStatus",
                        "specific"
                ), Arguments.of(
                        new MagdaMockRestHandler.MockRestRequest(
                                "/v1/socZek/socialeHuisvesting/dossiers/zoeken",
                                "",
                                "POST",
                                """
                                        {"dossierStatus":"CHKO", "dossierType":"RFH"}
                                        """,
                                Map.of()
                        ),
                        "dossierzoeken post dossierType en dossierStatus",
                        "specific"
                ), Arguments.of(
                        new MagdaMockRestHandler.MockRestRequest(
                                "/v1/socZek/socialeHuisvesting/dossiers/zoeken",
                                "",
                                "POST",
                                """
                                        {"dossierStatus":"not found", "dossierType":"not found"}
                                        """,
                                Map.of()
                        ),
                        "dossierzoeken post",
                        "default"
                ), Arguments.of(
                        new MagdaMockRestHandler.MockRestRequest(
                                "/v1/socZek/socialeHuisvesting/dossiers/dossier/2017.03.17",
                                "",
                                "GET",
                                "",
                                Map.of()
                        ),
                        "dossierzoeken post dossierstatus mapping get dossiernummer",
                        "specific"
                ), Arguments.of(
                        new MagdaMockRestHandler.MockRestRequest(
                                "/v1/socZek/socialeHuisvesting/dossiers/dossier/not_found",
                                "",
                                "GET",
                                "",
                                Map.of()
                        ),
                        "dossierzoeken get",
                        "default"
                ), Arguments.of(
                        new MagdaMockRestHandler.MockRestRequest(
                                "/v1/socZek/socialeHuisvesting/dossiers/bijlage",
                                "",
                                "POST",
                                """
                                        {"bijlageIdentificatie":"12346789", "partijCode":"0484648468488", "dossierNummer":"2024.02.22.005"}
                                        """,
                                Map.of()
                        ),
                        "dossierBijlage post partijCode, bijlageIdentification en dossierNummer",
                        "specific"
                ), Arguments.of(
                        new MagdaMockRestHandler.MockRestRequest(
                                "/v1/socZek/socialeHuisvesting/dossiers/bijlage",
                                "",
                                "POST",
                                """
                                        {"bijlageIdentificatie":"12346789", "partijCode":"0484648468488"}
                                        """,
                                Map.of()
                        ),
                        "dossierBijlage post partijCode en bijlageIdentification",
                        "specific"
                ), Arguments.of(
                        new MagdaMockRestHandler.MockRestRequest(
                                "/v1/socZek/socialeHuisvesting/dossiers/bijlage",
                                "",
                                "POST",
                                """
                                        {"bijlageIdentificatie":"not found", "partijCode":"not found"}
                                        """,
                                Map.of()
                        ),
                        "dossierBijlage post",
                        "default"
                ), Arguments.of(
                        new MagdaMockRestHandler.MockRestRequest(
                                "/v1/socZek/socialeHuisvesting/dossiers/verwerkDossierActie",
                                "",
                                "POST",
                                """
                                        {"actie":"OFFER", "dossierNummer":"2024.02.22.005", "partijCode":"0643634986"}
                                        """,
                                Map.of()
                        ),
                        "verwerkDossierActie post partijCode, dossierNummer en actie",
                        "specific"
                ), Arguments.of(
                        new MagdaMockRestHandler.MockRestRequest(
                                "/v1/socZek/socialeHuisvesting/dossiers/verwerkDossierActie",
                                "",
                                "POST",
                                """
                                        {"actie":"OFFER", "partijCode":"0643634986"}
                                        """,
                                Map.of()
                        ),
                        "wijzigDossierRangschikkingen post partijCode en actie",
                        "specific"
                ), Arguments.of(
                        new MagdaMockRestHandler.MockRestRequest(
                                "/v1/socZek/socialeHuisvesting/dossiers/verwerkDossierActie",
                                "",
                                "POST",
                                """
                                        {"actie":"not found", "partijCode":"not found"}
                                        """,
                                Map.of()
                        ),
                        "verwerkDossierActie post",
                        "default"
                ), Arguments.of(
                        new MagdaMockRestHandler.MockRestRequest(
                                "/v1/socZek/socialeHuisvesting/dossiers/dossier/2023.07.06.0002",
                                "",
                                "PUT",
                                "",
                                Map.of()
                        ),
                        "dossierzoeken put dossiernummer",
                        "specific"
                ), Arguments.of(
                        new MagdaMockRestHandler.MockRestRequest(
                                "/v1/socZek/socialeHuisvesting/dossiers/dossier/not_found",
                                "",
                                "PUT",
                                "",
                                Map.of()
                        ),
                        "dossierzoeken put",
                        "default"
                ), Arguments.of(
                        new MagdaMockRestHandler.MockRestRequest(
                                "/v1/socZek/socialeHuisvesting/dossiers/bevestigDossierVerwerking",
                                "",
                                "POST",
                                """
                                        {"versieNummer":"24", "partijCode":"0643634986", "dossierNummer":"2024.02.22.005"}
                                        """,
                                Map.of()
                        ),
                        "bevestigDossierVerwerking post partijCode, dossiernummer en versieNummer",
                        "specific"
                ), Arguments.of(
                        new MagdaMockRestHandler.MockRestRequest(
                                "/v1/socZek/socialeHuisvesting/dossiers/bevestigDossierVerwerking",
                                "",
                                "POST",
                                """
                                        {"versieNummer":"24", "dossierNummer":"2024.02.22.005"}
                                        """,
                                Map.of()
                        ),
                        "bevestigDossierVerwerking post dossiernummer en versieNummer",
                        "specific"
                ), Arguments.of(
                        new MagdaMockRestHandler.MockRestRequest(
                                "/v1/socZek/socialeHuisvesting/dossiers/bevestigDossierVerwerking",
                                "",
                                "POST",
                                """
                                        {"versieNummer":"24 not found", "partijCode":"0643634986", "dossierNummer":"2024.02.22.005"}
                                        """,
                                Map.of()
                        ),
                        "bevestigDossierVerwerking post",
                        "default"
                ), Arguments.of(
                        new MagdaMockRestHandler.MockRestRequest(
                                "/v1/socZek/socialeHuisvesting/dossiers/bevestigDossierVerwerking",
                                "",
                                "POST",
                                """
                                        {"versieNummer":"24 not found", "partijCode":"0643634986 not found", "dossierNummer":"2024.02.22.005"}
                                        """,
                                Map.of()
                        ),
                        "bevestigDossierVerwerking post",
                        "default"
                ), Arguments.of(
                        new MagdaMockRestHandler.MockRestRequest(
                                "/v1/socZek/socialeHuisvesting/dossiers/wijzigDossierRangschikkingen",
                                "",
                                "POST",
                                """
                                        {"partijCode":"0643634986"}
                                        """,
                                Map.of()
                        ),
                        "wijzigDossierRangschikkingen post partijCode",
                        "specific"
                ), Arguments.of(
                        new MagdaMockRestHandler.MockRestRequest(
                                "/v1/socZek/socialeHuisvesting/dossiers/wijzigDossierRangschikkingen",
                                "",
                                "POST",
                                """
                                        {"partijCode":"not found"}
                                        """,
                                Map.of()
                        ),
                        "wijzigDossierRangschikkingen post",
                        "default"
                ), Arguments.of(
                        new MagdaMockRestHandler.MockRestRequest(
                                "/v1/socZek/socialeHuisvesting/dossiers/partijInformatieWijzigingen",
                                "",
                                "POST",
                                """
                                        {"partijCode":"0643634986"}
                                        """,
                                Map.of()
                        ),
                        "partijInformatieWijzigingen post partijCode",
                        "specific"
                ), Arguments.of(
                        new MagdaMockRestHandler.MockRestRequest(
                                "/v1/socZek/socialeHuisvesting/dossiers/partijInformatieWijzigingen",
                                "",
                                "POST",
                                """
                                        {"partijCode":"not found"}
                                        """,
                                Map.of()
                        ),
                        "partijInformatieWijzigingen post",
                        "default"
                ), Arguments.of(
                        new MagdaMockRestHandler.MockRestRequest(
                                "/v1/socZek/socialeHuisvesting/dossiers/zwarteLijstMeldingen",
                                "",
                                "POST",
                                """
                                        {"partijCode":"0400954844"}
                                        """,
                                Map.of()
                        ),
                        "zwarteLijstMeldingen post partijCode",
                        "specific"
                ), Arguments.of(
                        new MagdaMockRestHandler.MockRestRequest(
                                "/v1/socZek/socialeHuisvesting/dossiers/zwarteLijstMeldingen",
                                "",
                                "POST",
                                """
                                        {"partijCode":"not found"}
                                        """,
                                Map.of()
                        ),
                        "zwarteLijstMeldingen post",
                        "default"
                ), Arguments.of(
                        new MagdaMockRestHandler.MockRestRequest(
                                "/v1/socZek/socialeHuisvesting/dossiers/dossierMetBijlages/2023.07.06.0002",
                                "",
                                "GET",
                                "",
                                Map.of()
                        ),
                        "dossierMetBijlages get dossierNummer",
                        "specific"
                ), Arguments.of(
                        new MagdaMockRestHandler.MockRestRequest(
                                "/v1/socZek/socialeHuisvesting/dossiers/dossierMetBijlages/not_found",
                                "",
                                "GET",
                                "",
                                Map.of()
                        ),
                        "dossierMetBijlages get",
                        "default"
                ), Arguments.of(
                        new MagdaMockRestHandler.MockRestRequest(
                                "/v1/socZek/socialeHuisvesting/gemeentes",
                                "",
                                "GET",
                                "",
                                Map.of()
                        ),
                        "gemeentes get",
                        "default"
                ), Arguments.of(
                        new MagdaMockRestHandler.MockRestRequest(
                                "/v1/socZek/socialeHuisvesting/wijkenVoorGemeente/31005",
                                "",
                                "GET",
                                "",
                                Map.of()
                        ),
                        "wijkenVoorGemeente get",
                        "specific"
                ), Arguments.of(
                        new MagdaMockRestHandler.MockRestRequest(
                                "/v1/socZek/socialeHuisvesting/wijkenVoorGemeente/not_found",
                                "",
                                "GET",
                                "",
                                Map.of()
                        ),
                        "wijkenVoorGemeente get",
                        "default"
                ), Arguments.of(
                        new MagdaMockRestHandler.MockRestRequest(
                                "/v1/socZek/socialeHuisvesting/wijkenVoorGemeente/31005",
                                "",
                                "PUT",
                                "",
                                Map.of()
                        ),
                        "wijkenVoorGemeente put",
                        "specific"
                ), Arguments.of(
                        new MagdaMockRestHandler.MockRestRequest(
                                "/v1/socZek/socialeHuisvesting/wijkenVoorGemeente/not_found",
                                "",
                                "PUT",
                                "",
                                Map.of()
                        ),
                        "wijkenVoorGemeente put",
                        "default"
                ), Arguments.of(
                        new MagdaMockRestHandler.MockRestRequest(
                                "/v1/socZek/socialeHuisvesting/dossiers/zwarteLijstMeldingVoor",
                                "",
                                "POST",
                                """
                                        {"rijksregisternummer":"906111123456"}
                                        """,
                                Map.of()
                        ),
                        "zwarteLijstMeldingenVoor post rijkregisternummer",
                        "specific"
                ), Arguments.of(
                        new MagdaMockRestHandler.MockRestRequest(
                                "/v1/socZek/socialeHuisvesting/dossiers/zwarteLijstMeldingVoor",
                                "",
                                "POST",
                                """
                                        {"rijksregisternummer":"not found"}
                                        """,
                                Map.of()
                        ),
                        "zwarteLijstMeldingenVoor post rijkregisternummer",
                        "default"
                ), Arguments.of(
                        new MagdaMockRestHandler.MockRestRequest(
                                "/v1/organisaties/verenigingen/verenigingen/zoeken",
                                "q=doelgroep.maximumleeftijd:100",
                                "GET",
                                "",
                                Map.of()
                        ),
                        "verenigingen zoeken get param q",
                        "specific"
                ), Arguments.of(
                        new MagdaMockRestHandler.MockRestRequest(
                                "/v1/organisaties/verenigingen/verenigingen/zoeken",
                                "q=doelgroep.maximumleeftijd:notfound",
                                "GET",
                                "",
                                Map.of()
                        ),
                        "verenigingen zoeken get param q",
                        "default"
                ), Arguments.of(
                        new MagdaMockRestHandler.MockRestRequest(
                                "/v1/organisaties/verenigingen/verenigingen/V0001017",
                                "",
                                "GET",
                                "",
                                Map.of()
                        ),
                        "verenigingen/verenigingen get",
                        "specific"
                ),Arguments.of(
                        new MagdaMockRestHandler.MockRestRequest(
                                "/v1/organisaties/verenigingen/verenigingen/notfound",
                                "",
                                "GET",
                                "",
                                Map.of()
                        ),
                        "verenigingen/verenigingen get",
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
