package be.vlaanderen.vip.magda.magdamock.client.rest;

import be.vlaanderen.vip.magda.magdamock.client.MagdaMockConnection;
import be.vlaanderen.vip.magda.magdamock.client.handlers.MagdaMockRestHandler;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
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
                        , "text/plain"
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
                        , "application/json"
                ), Arguments.of(
                        new MagdaMockRestHandler.MockRestRequest(
                                "/v1/mobility/registrations",
                                "vin=VIN&unifier=123",
                                "GET",
                                "",
                                Map.of()
                        ),
                        "rest mobility vin and unifier mapping",
                        "specific"
                        , "application/json"
                ), Arguments.of(
                        new MagdaMockRestHandler.MockRestRequest(
                                "/v1/mobility/registrations",
                                "plateUID=2345678",
                                "GET",
                                "",
                                Map.of()
                        ),
                        "rest mobility plateUID mapping",
                        "specific"
                        , "application/json"
                ), Arguments.of(
                        new MagdaMockRestHandler.MockRestRequest(
                                "/v1/mobility/registrations",
                                "plateUID=unknown",
                                "GET",
                                "",
                                Map.of()
                        ),
                        "rest mobility mapping",
                        "default"
                        , "application/json"
                ), Arguments.of(
                        new MagdaMockRestHandler.MockRestRequest(
                                "/v1/mobility/registrations",
                                "nationalNr=2345678",
                                "GET",
                                "",
                                Map.of()
                        ),
                        "rest mobility nationalNr mapping",
                        "specific"
                        , "application/json"
                ), Arguments.of(
                        new MagdaMockRestHandler.MockRestRequest(
                                "/v1/mobility/registrations",
                                "nationalNr=unknown",
                                "GET",
                                "",
                                Map.of()
                        ),
                        "rest mobility mapping",
                        "default"
                        , "application/json"
                ), Arguments.of(
                        new MagdaMockRestHandler.MockRestRequest(
                                "/v1/mobility/registrations",
                                "companyNr=2345678",
                                "GET",
                                "",
                                Map.of()
                        ),
                        "rest mobility companyNr mapping",
                        "specific"
                        , "application/json"
                ), Arguments.of(
                        new MagdaMockRestHandler.MockRestRequest(
                                "/v1/mobility/registrations",
                                "companyNr=unknown",
                                "GET",
                                "",
                                Map.of()
                        ),
                        "rest mobility mapping",
                        "default"
                        , "application/json"
                ), Arguments.of(
                        new MagdaMockRestHandler.MockRestRequest(
                                "/v1/mobility/registrations",
                                "",
                                "GET",
                                "",
                                Map.of()
                        ),
                        "rest mobility mapping",
                        "default"
                        , "application/json"
                ), Arguments.of(
                        new MagdaMockRestHandler.MockRestRequest(
                                "/v1/organisaties/verenigingen/verenigingen/zoeken",
                                "q=doelgroep.maximumleeftijd:100",
                                "GET",
                                "",
                                Map.of()
                        ),
                        "vereniging zoeken get query",
                        "specific"
                        , "application/json"
                ), Arguments.of(
                        new MagdaMockRestHandler.MockRestRequest(
                                "/v1/organisaties/verenigingen/verenigingen/zoeken",
                                "",
                                "GET",
                                "",
                                Map.of()
                        ),
                        "vereniging zoeken get query",
                        "default"
                        , "application/json"
                ), Arguments.of(
                        new MagdaMockRestHandler.MockRestRequest(
                                "/v1/organisaties/verenigingen/verenigingen/V0001031",
                                "q=doelgroep.maximumleeftijd:100",
                                "GET",
                                "",
                                Map.of()
                        ),
                        "vereniging zoeken get vcode",
                        "specific"
                        , "application/json"
                ), Arguments.of(
                        new MagdaMockRestHandler.MockRestRequest(
                                "/v1/organisaties/verenigingen/verenigingen/ongekend",
                                "",
                                "GET",
                                "",
                                Map.of()
                        ),
                        "vereniging zoeken get vcode",
                        "default"
                        , "application/json"
                ), Arguments.of(
                        new MagdaMockRestHandler.MockRestRequest(
                                "/v1/organisaties/verenigingen/verenigingen/V0001031",
                                "",
                                "PATCH",
                                "",
                                Map.of()
                        ),
                        "vereniging zoeken patch vcode",
                        "specific"
                        , "application/json"
                ), Arguments.of(
                        new MagdaMockRestHandler.MockRestRequest(
                                "/v1/organisaties/verenigingen/verenigingen/ongekend",
                                "",
                                "PATCH",
                                "",
                                Map.of()
                        ),
                        "vereniging zoeken patch vcode",
                        "default"
                        , "application/json"
                ), Arguments.of(
                        new MagdaMockRestHandler.MockRestRequest(
                                "/v1/organisaties/verenigingen/verenigingen/V0001031/stop",
                                "",
                                "POST",
                                "",
                                Map.of()
                        ),
                        "vereniging zoeken post vcode stop",
                        "specific"
                        , "application/json"
                ), Arguments.of(
                        new MagdaMockRestHandler.MockRestRequest(
                                "/v1/organisaties/verenigingen/verenigingen/ongekend/stop",
                                "",
                                "POST",
                                "",
                                Map.of()
                        ),
                        "vereniging zoeken post vcode stop",
                        "default"
                        , "application/json"
                ), Arguments.of(
                        new MagdaMockRestHandler.MockRestRequest(
                                "/v1/organisaties/verenigingen/verenigingen/V0001031/historiek",
                                "",
                                "GET",
                                "",
                                Map.of()
                        ),
                        "vereniging historiek get vcode",
                        "specific"
                        , "application/json"
                ), Arguments.of(
                        new MagdaMockRestHandler.MockRestRequest(
                                "/v1/organisaties/verenigingen/verenigingen/ongekend/historiek",
                                "",
                                "GET",
                                "",
                                Map.of()
                        ),
                        "vereniging historiek get vcode",
                        "default"
                        , "application/json"
                ), Arguments.of(
                        new MagdaMockRestHandler.MockRestRequest(
                                "/v1/organisaties/verenigingen/verenigingen/feitelijkeverenigingen",
                                "",
                                "POST",
                                """
                                        {"naam": "Vereniging zonder naam"}
                                        """,
                                Map.of()
                        ),
                        "vereniging feitelijkeverenigingen post",
                        "specific"
                        , "application/json"
                ), Arguments.of(
                        new MagdaMockRestHandler.MockRestRequest(
                                "/v1/organisaties/verenigingen/verenigingen/feitelijkeverenigingen",
                                "",
                                "POST",
                                "",
                                Map.of()
                        ),
                        "vereniging feitelijkeverenigingen post",
                        "default"
                        , "application/json"
                ), Arguments.of(
                        new MagdaMockRestHandler.MockRestRequest(
                                "/v1/organisaties/verenigingen/verenigingen/kbo",
                                "",
                                "POST",
                                """
                                        {"kboNummer": "0123465798"}
                                        """,
                                Map.of()
                        ),
                        "vereniging kbo post",
                        "specific"
                        , "application/json"
                ), Arguments.of(
                        new MagdaMockRestHandler.MockRestRequest(
                                "/v1/organisaties/verenigingen/verenigingen/kbo",
                                "",
                                "POST",
                                "",
                                Map.of()
                        ),
                        "vereniging kbo post",
                        "default"
                        , "application/json"
                ), Arguments.of(
                        new MagdaMockRestHandler.MockRestRequest(
                                "/v1/organisaties/verenigingen/verenigingen/vzer",
                                "",
                                "POST",
                                """
                                        {"naam": "Vereniging zonder naam"}
                                        """,
                                Map.of()
                        ),
                        "vereniging vzer post",
                        "specific"
                        , "application/json"
                ), Arguments.of(
                        new MagdaMockRestHandler.MockRestRequest(
                                "/v1/organisaties/verenigingen/verenigingen/vzer",
                                "",
                                "POST",
                                "",
                                Map.of()
                        ),
                        "vereniging vzer post",
                        "default"
                        , "application/json"
                ), Arguments.of(
                        new MagdaMockRestHandler.MockRestRequest(
                                "/v1/organisaties/verenigingen/verenigingen/V0123456/contactgegevens/1",
                                "",
                                "PATCH",
                                "",
                                Map.of()
                        ),
                        "vereniging contactgegevens patch",
                        "specific"
                        , "application/json"
                ), Arguments.of(
                        new MagdaMockRestHandler.MockRestRequest(
                                "/v1/organisaties/verenigingen/verenigingen/unknown/contactgegevens/1",
                                "",
                                "PATCH",
                                "",
                                Map.of()
                        ),
                        "vereniging contactgegevens patch",
                        "default"
                        , "application/json"
                ), Arguments.of(
                        new MagdaMockRestHandler.MockRestRequest(
                                "/v1/organisaties/verenigingen/verenigingen/V0123456/contactgegevens/1",
                                "",
                                "DELETE",
                                "",
                                Map.of()
                        ),
                        "vereniging contactgegevens delete",
                        "specific"
                        , "application/json"
                ), Arguments.of(
                        new MagdaMockRestHandler.MockRestRequest(
                                "/v1/organisaties/verenigingen/verenigingen/unknown/contactgegevens/1",
                                "",
                                "DELETE",
                                "",
                                Map.of()
                        ),
                        "vereniging contactgegevens delete",
                        "default"
                        , "application/json"
                ), Arguments.of(
                        new MagdaMockRestHandler.MockRestRequest(
                                "/v1/organisaties/verenigingen/verenigingen/V0123456/kbo",
                                "",
                                "PATCH",
                                "",
                                Map.of()
                        ),
                        "vereniging vcode patch kbo",
                        "specific"
                        , "application/json"
                ), Arguments.of(
                        new MagdaMockRestHandler.MockRestRequest(
                                "/v1/organisaties/verenigingen/verenigingen/unknown/kbo",
                                "",
                                "PATCH",
                                "",
                                Map.of()
                        ),
                        "vereniging vcode patch kbo",
                        "default"
                        , "application/json"
                ), Arguments.of(
                        new MagdaMockRestHandler.MockRestRequest(
                                "/v1/organisaties/verenigingen/verenigingen/V0123456/kbo/contactgegevens/1",
                                "",
                                "PATCH",
                                "",
                                Map.of()
                        ),
                        "vereniging vcode patch kbo contactgegevens",
                        "specific"
                        , "application/json"
                ), Arguments.of(
                        new MagdaMockRestHandler.MockRestRequest(
                                "/v1/organisaties/verenigingen/verenigingen/unknown/kbo/contactgegevens/1",
                                "",
                                "PATCH",
                                "",
                                Map.of()
                        ),
                        "vereniging vcode patch kbo contactgegevens",
                        "default"
                        , "application/json"
                ), Arguments.of(
                        new MagdaMockRestHandler.MockRestRequest(
                                "/v1/organisaties/verenigingen/verenigingen/V0123456/kbo/locaties/1",
                                "",
                                "PATCH",
                                "",
                                Map.of()
                        ),
                        "vereniging vcode patch kbo locaties",
                        "specific"
                        , "application/json"
                ), Arguments.of(
                        new MagdaMockRestHandler.MockRestRequest(
                                "/v1/organisaties/verenigingen/verenigingen/unknown/kbo/locaties/1",
                                "",
                                "PATCH",
                                "",
                                Map.of()
                        ),
                        "vereniging vcode patch kbo locaties",
                        "default"
                        , "application/json"
                ), Arguments.of(
                        new MagdaMockRestHandler.MockRestRequest(
                                "/v1/organisaties/verenigingen/verenigingen/V0123456/lidmaatschappen",
                                "",
                                "POST",
                                "",
                                Map.of()
                        ),
                        "vereniging vcode post lidmaatschappen",
                        "specific"
                        , "application/json"
                ), Arguments.of(
                        new MagdaMockRestHandler.MockRestRequest(
                                "/v1/organisaties/verenigingen/verenigingen/unknown/lidmaatschappen",
                                "",
                                "POST",
                                "",
                                Map.of()
                        ),
                        "vereniging vcode post lidmaatschappen",
                        "default"
                        , "application/json"
                ), Arguments.of(
                        new MagdaMockRestHandler.MockRestRequest(
                                "/v1/organisaties/verenigingen/verenigingen/V0123456/lidmaatschappen/1",
                                "",
                                "PATCH",
                                "",
                                Map.of()
                        ),
                        "vereniging vcode patch lidmaatschappen",
                        "specific"
                        , "application/json"
                ), Arguments.of(
                        new MagdaMockRestHandler.MockRestRequest(
                                "/v1/organisaties/verenigingen/verenigingen/unknown/lidmaatschappen/1",
                                "",
                                "PATCH",
                                "",
                                Map.of()
                        ),
                        "vereniging vcode patch lidmaatschappen",
                        "default"
                        , "application/json"
                ), Arguments.of(
                        new MagdaMockRestHandler.MockRestRequest(
                                "/v1/organisaties/verenigingen/verenigingen/V0123456/lidmaatschappen/1",
                                "",
                                "DELETE",
                                "",
                                Map.of()
                        ),
                        "vereniging vcode delete lidmaatschappen",
                        "specific"
                        , "application/json"
                ), Arguments.of(
                        new MagdaMockRestHandler.MockRestRequest(
                                "/v1/organisaties/verenigingen/verenigingen/unknown/lidmaatschappen/1",
                                "",
                                "DELETE",
                                "",
                                Map.of()
                        ),
                        "vereniging vcode delete lidmaatschappen",
                        "default"
                        , "application/json"
                ), Arguments.of(
                        new MagdaMockRestHandler.MockRestRequest(
                                "/v1/organisaties/verenigingen/verenigingen/V0123456/locaties",
                                "",
                                "POST",
                                "",
                                Map.of()
                        ),
                        "vereniging vcode post locaties",
                        "specific"
                        , "application/json"
                ), Arguments.of(
                        new MagdaMockRestHandler.MockRestRequest(
                                "/v1/organisaties/verenigingen/verenigingen/unknown/locaties",
                                "",
                                "POST",
                                "",
                                Map.of()
                        ),
                        "vereniging vcode post locaties",
                        "default"
                        , "application/json"
                ), Arguments.of(
                        new MagdaMockRestHandler.MockRestRequest(
                                "/v1/organisaties/verenigingen/verenigingen/V0123456/locaties/1",
                                "",
                                "PATCH",
                                "",
                                Map.of()
                        ),
                        "vereniging vcode patch locaties",
                        "specific"
                        , "application/json"
                ), Arguments.of(
                        new MagdaMockRestHandler.MockRestRequest(
                                "/v1/organisaties/verenigingen/verenigingen/unknown/locaties/1",
                                "",
                                "PATCH",
                                "",
                                Map.of()
                        ),
                        "vereniging vcode patch locaties",
                        "default"
                        , "application/json"
                ), Arguments.of(
                        new MagdaMockRestHandler.MockRestRequest(
                                "/v1/organisaties/verenigingen/verenigingen/V0123456/locaties/1",
                                "",
                                "DELETE",
                                "",
                                Map.of()
                        ),
                        "vereniging vcode delete locaties",
                        "specific"
                        , "application/json"
                ), Arguments.of(
                        new MagdaMockRestHandler.MockRestRequest(
                                "/v1/organisaties/verenigingen/verenigingen/unknown/locaties/1",
                                "",
                                "DELETE",
                                "",
                                Map.of()
                        ),
                        "vereniging vcode delete locaties",
                        "default"
                        , "application/json"
                ), Arguments.of(
                        new MagdaMockRestHandler.MockRestRequest(
                                "/v1/organisaties/verenigingen/verenigingen/V0123456/subtype",
                                "",
                                "PATCH",
                                "",
                                Map.of()
                        ),
                        "vereniging vcode patch subtype",
                        "specific"
                        , "application/json"
                ), Arguments.of(
                        new MagdaMockRestHandler.MockRestRequest(
                                "/v1/organisaties/verenigingen/verenigingen/unknown/subtype",
                                "",
                                "PATCH",
                                "",
                                Map.of()
                        ),
                        "vereniging vcode patch subtype",
                        "default"
                        , "application/json"
                ), Arguments.of(
                        new MagdaMockRestHandler.MockRestRequest(
                                "/v1/organisaties/verenigingen/verenigingen/V0123456/vertegenwoordigers",
                                "",
                                "POST",
                                "",
                                Map.of()
                        ),
                        "vereniging vcode post vertegenwoordigers",
                        "specific"
                        , "application/json"
                ), Arguments.of(
                        new MagdaMockRestHandler.MockRestRequest(
                                "/v1/organisaties/verenigingen/verenigingen/unknown/vertegenwoordigers",
                                "",
                                "POST",
                                "",
                                Map.of()
                        ),
                        "vereniging vcode post vertegenwoordigers",
                        "default"
                        , "application/json"
                ), Arguments.of(
                        new MagdaMockRestHandler.MockRestRequest(
                                "/v1/organisaties/verenigingen/verenigingen/V0123456/vertegenwoordigers/1",
                                "",
                                "PATCH",
                                "",
                                Map.of()
                        ),
                        "vereniging vcode patch vertegenwoordigers",
                        "specific"
                        , "application/json"
                ), Arguments.of(
                        new MagdaMockRestHandler.MockRestRequest(
                                "/v1/organisaties/verenigingen/verenigingen/unknown/vertegenwoordigers/1",
                                "",
                                "PATCH",
                                "",
                                Map.of()
                        ),
                        "vereniging vcode patch vertegenwoordigers",
                        "default"
                        , "application/json"
                ), Arguments.of(
                        new MagdaMockRestHandler.MockRestRequest(
                                "/v1/organisaties/verenigingen/verenigingen/V0123456/vertegenwoordigers/1",
                                "",
                                "DELETE",
                                "",
                                Map.of()
                        ),
                        "vereniging vcode delete vertegenwoordigers",
                        "specific"
                        , "application/json"
                ), Arguments.of(
                        new MagdaMockRestHandler.MockRestRequest(
                                "/v1/organisaties/verenigingen/verenigingen/unknown/vertegenwoordigers/1",
                                "",
                                "DELETE",
                                "",
                                Map.of()
                        ),
                        "vereniging vcode delete vertegenwoordigers",
                        "default"
                        , "application/json"
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
                        , "application/json"
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
                        , "application/json"
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
                        , "application/json"
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
                        , "application/json"
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
                        , "application/json"
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
                        , "application/json"
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
                        , "application/json"
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
                        , "application/json"
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
                        , "application/json"
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
                        , "application/json"
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
                        , "application/json"
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
                        , "application/json"
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
                        , "application/json"
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
                        , "application/json"
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
                        , "application/json"
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
                        , "application/json"
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
                        , "application/json"
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
                        , "application/json"
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
                        , "application/json"
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
                        , "application/json"
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
                        , "application/json"
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
                        , "application/json"
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
                        , "application/json"
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
                        , "application/json"
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
                        , "application/json"
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
                        , "application/json"
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
                        , "application/json"
                ), Arguments.of(
                        new MagdaMockRestHandler.MockRestRequest(
                                "/v1/socZek/socialeHuisvesting/gemeentes/wijkenVoorGemeente/31005",
                                "",
                                "GET",
                                "",
                                Map.of()
                        ),
                        "wijkenVoorGemeente get",
                        "specific"
                        , "application/json"
                ), Arguments.of(
                        new MagdaMockRestHandler.MockRestRequest(
                                "/v1/socZek/socialeHuisvesting/gemeentes/wijkenVoorGemeente/not_found",
                                "",
                                "GET",
                                "",
                                Map.of()
                        ),
                        "wijkenVoorGemeente get",
                        "default"
                        , "application/json"
                ), Arguments.of(
                        new MagdaMockRestHandler.MockRestRequest(
                                "/v1/socZek/socialeHuisvesting/gemeentes/wijkenVoorGemeente/31005",
                                "",
                                "PUT",
                                "",
                                Map.of()
                        ),
                        "wijkenVoorGemeente put",
                        "specific"
                        , "application/json"
                ), Arguments.of(
                        new MagdaMockRestHandler.MockRestRequest(
                                "/v1/socZek/socialeHuisvesting/gemeentes/wijkenVoorGemeente/not_found",
                                "",
                                "PUT",
                                "",
                                Map.of()
                        ),
                        "wijkenVoorGemeente put",
                        "default"
                        , "application/json"
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
                        , "application/json"
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
                        , "application/json"
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
    @SneakyThrows
    void testMobilityRegCountryCodeGet(
            MagdaMockRestHandler.MockRestRequest mockRestRequest,
            String expectedMessage,
            String expectedMappingType,
            String expectedContentTypeHeader
    ) {
        var response = magdaMockConnection.sendRestRequest(mockRestRequest);
        Assertions.assertNotNull(response);
        JsonNode jsonBody = new ObjectMapper().readTree(response.body());
        Assertions.assertEquals(expectedMessage, jsonBody.get("message").textValue());
        Assertions.assertEquals(expectedMappingType, jsonBody.get("mappingType").textValue());
        Assertions.assertTrue(response.headers().get("Content-Type").contains(expectedContentTypeHeader));
        Assertions.assertEquals(200, response.status());
    }
}
