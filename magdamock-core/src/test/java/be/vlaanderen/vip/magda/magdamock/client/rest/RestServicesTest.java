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
                                        {"actie":"OFFER", "dossierNummer":"2024.02.22.005"}
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
                                        {"actie":"OFFER"}
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
                                        {"actie":"not found"}
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
                                        {"dossierNummer":"2024.02.22.005"}
                                        """,
                                Map.of()
                        ),
                        "bevestigDossierVerwerking post dossiernummer",
                        "specific"
                        , "application/json"
                ), Arguments.of(
                        new MagdaMockRestHandler.MockRestRequest(
                                "/v1/socZek/socialeHuisvesting/dossiers/wijzigDossierRangschikkingen",
                                "",
                                "POST",
                                """
                                        {"dossierNummer":"not found"}
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
                ), Arguments.of(
                        new MagdaMockRestHandler.MockRestRequest(
                                "/v1/socZek/socialeHuisvesting/woningen/ZYKTR",
                                "",
                                "GET",
                                "",
                                Map.of()
                        ),
                        "sociale huisvesting woningen get",
                        "specific"
                        , "application/json"
                ), Arguments.of(
                        new MagdaMockRestHandler.MockRestRequest(
                                "/v1/socZek/socialeHuisvesting/woningen/unknown",
                                "",
                                "GET",
                                "",
                                Map.of()
                        ),
                        "sociale huisvesting woningen get",
                        "default"
                        , "application/json"
                ), Arguments.of(
                        new MagdaMockRestHandler.MockRestRequest(
                                "/v1/socZek/socialeHuisvesting/woningen/ZYKTR",
                                "",
                                "PATCH",
                                "",
                                Map.of()
                        ),
                        "sociale huisvesting woningen patch",
                        "specific"
                        , "application/json"
                ), Arguments.of(
                        new MagdaMockRestHandler.MockRestRequest(
                                "/v1/socZek/socialeHuisvesting/woningen/unknown",
                                "",
                                "PATCH",
                                "",
                                Map.of()
                        ),
                        "sociale huisvesting woningen patch",
                        "default"
                        , "application/json"
                ), Arguments.of(
                        new MagdaMockRestHandler.MockRestRequest(
                                "/v1/socZek/socialeHuisvesting/woningen",
                                "gemeenteNISCode=23027&gebouweenheidId=16230836",
                                "GET",
                                "",
                                Map.of()
                        ),
                        "sociale huisvesting woningen get all",
                        "specific"
                        , "application/json"
                ), Arguments.of(
                        new MagdaMockRestHandler.MockRestRequest(
                                "/v1/socZek/socialeHuisvesting/woningen",
                                "",
                                "GET",
                                "",
                                Map.of()
                        ),
                        "sociale huisvesting woningen get all",
                        "default"
                        , "application/json"
                ), Arguments.of(
                        new MagdaMockRestHandler.MockRestRequest(
                                "/v1/socZek/socialeHuisvesting/woningen",
                                "",
                                "POST",
                                """
                                        {
                                          "eigenaarOrganisatieId": "0400898624",
                                          "gebouweenheidId": "16230836",
                                          "woonmaatschappijWoningId": "775",
                                          "woonmaatschappijWoningCode": "031030",
                                          "woonmaatschappijWoningGroepCode": "HAL031",
                                          "adres": {
                                            "straatnaam": "Labbeekstraat",
                                            "huisnummer": "100",
                                            "postcode": "1500",
                                            "gemeenteNISCode": "23027"
                                          },
                                          "bouwjaar": 1982,
                                          "bebouwingsType": "OB"
                                        }
                                        """,
                                Map.of()
                        ),
                        "sociale huisvesting woningen post all",
                        "specific"
                        , "application/json"
                ), Arguments.of(
                        new MagdaMockRestHandler.MockRestRequest(
                                "/v1/socZek/socialeHuisvesting/woningen",
                                "",
                                "POST",
                                "",
                                Map.of()
                        ),
                        "sociale huisvesting woningen post all",
                        "default"
                        , "application/json"
                ), Arguments.of(
                        new MagdaMockRestHandler.MockRestRequest(
                                "/v1/socZek/socialeHuisvesting/woningen/ZYKTR/woningkenmerkscores",
                                "woningKenmerkCategorie=ERP",
                                "GET",
                                "",
                                Map.of()
                        ),
                        "sociale huisvesting woningkenmerkscores get",
                        "specific"
                        , "application/json"
                ), Arguments.of(
                        new MagdaMockRestHandler.MockRestRequest(
                                "/v1/socZek/socialeHuisvesting/woningen/unknown/woningkenmerkscores",
                                "",
                                "GET",
                                "",
                                Map.of()
                        ),
                        "sociale huisvesting woningkenmerkscores get",
                        "default"
                        , "application/json"
                ), Arguments.of(
                        new MagdaMockRestHandler.MockRestRequest(
                                "/v1/socZek/socialeHuisvesting/woningen/ZYKTR/woningkenmerkscores",
                                "",
                                "PATCH",
                                "",
                                Map.of()
                        ),
                        "sociale huisvesting woningkenmerkscores patch",
                        "specific"
                        , "application/json"
                ), Arguments.of(
                        new MagdaMockRestHandler.MockRestRequest(
                                "/v1/socZek/socialeHuisvesting/woningen/unknown/woningkenmerkscores",
                                "",
                                "PATCH",
                                "",
                                Map.of()
                        ),
                        "sociale huisvesting woningkenmerkscores patch",
                        "default"
                        , "application/json"
                ), Arguments.of(
                        new MagdaMockRestHandler.MockRestRequest(
                                "/v1/socZek/socialeHuisvesting/woningen/ZYKTR/markthuurwaarden",
                                "",
                                "GET",
                                "",
                                Map.of()
                        ),
                        "sociale huisvesting markthuurwaarden get",
                        "specific"
                        , "application/json"
                ), Arguments.of(
                        new MagdaMockRestHandler.MockRestRequest(
                                "/v1/socZek/socialeHuisvesting/woningen/unknown/markthuurwaarden",
                                "",
                                "GET",
                                "",
                                Map.of()
                        ),
                        "sociale huisvesting markthuurwaarden get",
                        "default"
                        , "application/json"
                ), Arguments.of(
                        new MagdaMockRestHandler.MockRestRequest(
                                "/v1/socZek/socialeHuisvesting/woningen/markthuurwaarden/simulatie",
                                "",
                                "POST",
                                "",
                                Map.of()
                        ),
                        "sociale huisvesting markthuurwaarden post",
                        "default"
                        , "application/json"
                ), Arguments.of(
                        new MagdaMockRestHandler.MockRestRequest(
                                "/v1/socZek/socialeHuisvesting/woningen/ZYKTR/conditiescore",
                                "",
                                "GET",
                                "",
                                Map.of()
                        ),
                        "sociale huisvesting conditiescore get",
                        "specific"
                        , "application/json"
                ), Arguments.of(
                        new MagdaMockRestHandler.MockRestRequest(
                                "/v1/socZek/socialeHuisvesting/woningen/unknown/conditiescore",
                                "",
                                "GET",
                                "",
                                Map.of()
                        ),
                        "sociale huisvesting conditiescore get",
                        "default"
                        , "application/json"
                ), Arguments.of(
                        new MagdaMockRestHandler.MockRestRequest(
                                "/v1/socZek/socialeHuisvesting/woningen/conditiescore/simulatie",
                                "",
                                "POST",
                                "",
                                Map.of()
                        ),
                        "sociale huisvesting conditiescore post",
                        "default"
                        , "application/json"
                ), Arguments.of(
                        new MagdaMockRestHandler.MockRestRequest(
                                "/v1/socZek/socialeHuisvesting/woningen/ZYKTR/energiecorrectie",
                                "epcVersie=Huidig&datumVanaf=2020-06-01&datumTot=2026-06-30",
                                "GET",
                                "",
                                Map.of()
                        ),
                        "sociale huisvesting energiecorrectie get",
                        "specific"
                        , "application/json"
                ), Arguments.of(
                        new MagdaMockRestHandler.MockRestRequest(
                                "/v1/socZek/socialeHuisvesting/woningen/unknown/energiecorrectie",
                                "",
                                "GET",
                                "",
                                Map.of()
                        ),
                        "sociale huisvesting energiecorrectie get",
                        "default"
                        , "application/json"
                ), Arguments.of(
                        new MagdaMockRestHandler.MockRestRequest(
                                "/v1/socZek/socialeHuisvesting/woningen/ZYKTR/epcwaarden",
                                "",
                                "GET",
                                "",
                                Map.of()
                        ),
                        "sociale huisvesting epcwaarden get",
                        "specific"
                        , "application/json"
                ), Arguments.of(
                        new MagdaMockRestHandler.MockRestRequest(
                                "/v1/socZek/socialeHuisvesting/woningen/unknown/epcwaarden",
                                "",
                                "GET",
                                "",
                                Map.of()
                        ),
                        "sociale huisvesting epcwaarden get",
                        "default"
                        , "application/json"
                ), Arguments.of(
                        new MagdaMockRestHandler.MockRestRequest(
                                "/v1/socZek/socialeHuisvesting/woningen/ZYKTR/epcwaarden/brondata/970939B2-75B3-4F14-BDDE-2AB9ACFB6EEF",
                                "",
                                "GET",
                                "",
                                Map.of()
                        ),
                        "sociale huisvesting epcwaarden brondata get",
                        "specific"
                        , "application/json"
                ), Arguments.of(
                        new MagdaMockRestHandler.MockRestRequest(
                                "/v1/socZek/socialeHuisvesting/woningen/unknown/epcwaarden/brondata/unknown",
                                "",
                                "GET",
                                "",
                                Map.of()
                        ),
                        "sociale huisvesting epcwaarden brondata get",
                        "default"
                        , "application/json"
                ), Arguments.of(
                        new MagdaMockRestHandler.MockRestRequest(
                                "/v1/socZek/socialeHuisvesting/woningen/ZYKTR/epcwaarden/document/970939B2-75B3-4F14-BDDE-2AB9ACFB6EEF",
                                "",
                                "GET",
                                "",
                                Map.of()
                        ),
                        "sociale huisvesting epcwaarden document get",
                        "specific"
                        , "application/json"
                ), Arguments.of(
                        new MagdaMockRestHandler.MockRestRequest(
                                "/v1/socZek/socialeHuisvesting/woningen/unknown/epcwaarden/document/unknown",
                                "",
                                "GET",
                                "",
                                Map.of()
                        ),
                        "sociale huisvesting epcwaarden document get",
                        "default"
                        , "application/json"
                ), Arguments.of(
                        new MagdaMockRestHandler.MockRestRequest(
                                "/v1/socZek/socialeHuisvesting/woningen/ZYKTR/epcwaarden/certificaat",
                                "",
                                "POST",
                                "",
                                Map.of()
                        ),
                        "sociale huisvesting certificaat post",
                        "specific"
                        , "application/json"
                ), Arguments.of(
                        new MagdaMockRestHandler.MockRestRequest(
                                "/v1/socZek/socialeHuisvesting/woningen/unknown/epcwaarden/certificaat",
                                "",
                                "POST",
                                "",
                                Map.of()
                        ),
                        "sociale huisvesting certificaat post",
                        "default"
                        , "application/json"
                ), Arguments.of(
                        new MagdaMockRestHandler.MockRestRequest(
                                "/v1/socZek/socialeHuisvesting/woningen/ZYKTR/epcwaarden/markeerReferentieversie",
                                "",
                                "PATCH",
                                """
                                        {
                                          "status": "Huidig",
                                          "epcVersieId": "2058936"
                                        }
                                        """,
                                Map.of()
                        ),
                        "sociale huisvesting epcwaarden markeerReferentieversie patch",
                        "specific"
                        , "application/json"
                ), Arguments.of(
                        new MagdaMockRestHandler.MockRestRequest(
                                "/v1/socZek/socialeHuisvesting/woningen/unknown/epcwaarden/markeerReferentieversie",
                                "",
                                "PATCH",
                                "",
                                Map.of()
                        ),
                        "sociale huisvesting epcwaarden markeerReferentieversie patch",
                        "default"
                        , "application/json"
                ), Arguments.of(
                        new MagdaMockRestHandler.MockRestRequest(
                                "/v1/socZek/socialeHuisvesting/woningen/ZYKTR/epcwaarden/2058936",
                                "",
                                "DELETE",
                                "",
                                Map.of()
                        ),
                        "sociale huisvesting epcwaarden markeerReferentieversie delete",
                        "specific"
                        , "application/json"
                ), Arguments.of(
                        new MagdaMockRestHandler.MockRestRequest(
                                "/v1/socZek/socialeHuisvesting/woningen/unknown/epcwaarden/unknown",
                                "",
                                "DELETE",
                                "",
                                Map.of()
                        ),
                        "sociale huisvesting epcwaarden markeerReferentieversie delete",
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
    void testRestService(
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
