package be.vlaanderen.vip.magda.magdamock.config;

import be.vlaanderen.vip.magda.magdamock.config.rest.RestBodyParameter;
import be.vlaanderen.vip.magda.magdamock.config.rest.RestHeaderParameter;
import be.vlaanderen.vip.magda.magdamock.config.rest.RestParameter;
import be.vlaanderen.vip.magda.magdamock.config.rest.RestPathParameter;
import be.vlaanderen.vip.magda.magdamock.config.rest.RestQueryParameter;

import java.util.List;

public record MockRestMapping(List<String> folderPath,
                              List<RestParameter> restParameters,
                              String url, String method, Integer priority,
                              boolean defaultOnly, MissingParameterConfiguration missingParameterConfiguration) {
    public static final List<MockRestMapping> MAPPINGS = List.of(
            // Company
            new MockRestMapping(List.of("v1", "company", "billRetainment", "retainmentObligations", "search", "post"), List.of(RestBodyParameter.of("/enterpriseIdentifiers/0")), "/v1/company/billRetainment/retainmentObligations/search", "POST"),

            // Mobility
            new MockRestMapping(List.of("v1", "mobility", "registrations", "get", "plateNr"), List.of(RestQueryParameter.of("plateNr")), "/v1/mobility/registrations", "GET", 40),
            new MockRestMapping(List.of("v1", "mobility", "registrations", "get", "plateUID"), List.of(RestQueryParameter.of("plateUID")), "/v1/mobility/registrations", "GET", 41),
            new MockRestMapping(List.of("v1", "mobility", "registrations", "get", "vin"), List.of(RestQueryParameter.of("vin"), RestQueryParameter.of("unifier")), "/v1/mobility/registrations", "GET", 42),
            new MockRestMapping(List.of("v1", "mobility", "registrations", "get", "nationalNr"), List.of(RestQueryParameter.of("nationalNr")), "/v1/mobility/registrations", "GET", 43),
            new MockRestMapping(List.of("v1", "mobility", "registrations", "get", "companyNr"), List.of(RestQueryParameter.of("companyNr")), "/v1/mobility/registrations", "GET", 44),
            new MockRestMapping(List.of("v1", "mobility", "registrations", "get"), List.of(), "/v1/mobility/registrations", "GET", 50, true, MissingParameterConfiguration.EmptyString),

            // Organisaties.verenigingen
            new MockRestMapping(List.of("v1", "organisaties", "verenigingen", "verenigingen", "zoeken", "get"), List.of(RestQueryParameter.of("q")), "/v1/organisaties/verenigingen/verenigingen/zoeken", "GET", 40),
            new MockRestMapping(List.of("v1", "organisaties", "verenigingen", "verenigingen", "vCode", "get"), List.of(RestPathParameter.of("vCode")), "/v1/organisaties/verenigingen/verenigingen/{vCode}", "GET"),
            new MockRestMapping(List.of("v1", "organisaties", "verenigingen", "verenigingen", "vCode", "patch"), List.of(RestPathParameter.of("vCode")), "/v1/organisaties/verenigingen/verenigingen/{vCode}", "PATCH"),
            new MockRestMapping(List.of("v1", "organisaties", "verenigingen", "verenigingen", "vCode", "stop", "post"), List.of(RestPathParameter.of("vCode")), "/v1/organisaties/verenigingen/verenigingen/{vCode}/stop", "POST"),
            new MockRestMapping(List.of("v1", "organisaties", "verenigingen", "verenigingen", "vCode", "historiek", "get"), List.of(RestPathParameter.of("vCode")), "/v1/organisaties/verenigingen/verenigingen/{vCode}/historiek", "GET"),
            new MockRestMapping(List.of("v1", "organisaties", "verenigingen", "verenigingen", "feitelijkeverenigingen", "post"), List.of(RestBodyParameter.of("naam")), "/v1/organisaties/verenigingen/verenigingen/feitelijkeverenigingen", "POST"),
            new MockRestMapping(List.of("v1", "organisaties", "verenigingen", "verenigingen", "kbo", "post"), List.of(RestBodyParameter.of("kboNummer")), "/v1/organisaties/verenigingen/verenigingen/kbo", "POST"),
            new MockRestMapping(List.of("v1", "organisaties", "verenigingen", "verenigingen", "vzer", "post"), List.of(RestBodyParameter.of("naam")), "/v1/organisaties/verenigingen/verenigingen/vzer", "POST"),
            new MockRestMapping(List.of("v1", "organisaties", "verenigingen", "verenigingen", "vCode", "contactgegevens", "contactgegevenId", "patch"), List.of(RestPathParameter.of("vCode"), RestPathParameter.of("contactgegevens")), "/v1/organisaties/verenigingen/verenigingen/{vCode}/contactgegevens/{contactgegevens}", "PATCH"),
            new MockRestMapping(List.of("v1", "organisaties", "verenigingen", "verenigingen", "vCode", "contactgegevens", "contactgegevenId", "delete"), List.of(RestPathParameter.of("vCode"), RestPathParameter.of("contactgegevens")), "/v1/organisaties/verenigingen/verenigingen/{vCode}/contactgegevens/{contactgegevens}", "DELETE"),
            new MockRestMapping(List.of("v1", "organisaties", "verenigingen", "verenigingen", "vCode", "kbo", "patch"), List.of(RestPathParameter.of("vCode")), "/v1/organisaties/verenigingen/verenigingen/{vCode}/kbo", "PATCH"),
            new MockRestMapping(List.of("v1", "organisaties", "verenigingen", "verenigingen", "vCode", "kbo", "contactgegevens", "contactgegevenId", "patch"), List.of(RestPathParameter.of("vCode"), RestPathParameter.of("contactgegevenId")), "/v1/organisaties/verenigingen/verenigingen/{vCode}/kbo/contactgegevens/{contactgegevenId}", "PATCH"),
            new MockRestMapping(List.of("v1", "organisaties", "verenigingen", "verenigingen", "vCode", "kbo", "locaties", "locatieId", "patch"), List.of(RestPathParameter.of("vCode"), RestPathParameter.of("locatieId")), "/v1/organisaties/verenigingen/verenigingen/{vCode}/kbo/locaties/{locatieId}", "PATCH"),
            new MockRestMapping(List.of("v1", "organisaties", "verenigingen", "verenigingen", "vCode", "lidmaatschappen", "post"), List.of(RestPathParameter.of("vCode")), "/v1/organisaties/verenigingen/verenigingen/{vCode}/lidmaatschappen", "POST"),
            new MockRestMapping(List.of("v1", "organisaties", "verenigingen", "verenigingen", "vCode", "lidmaatschappen", "lidmaatschapId", "patch"), List.of(RestPathParameter.of("vCode"), RestPathParameter.of("lidmaatschapId")), "/v1/organisaties/verenigingen/verenigingen/{vCode}/lidmaatschappen/{lidmaatschapId}", "PATCH"),
            new MockRestMapping(List.of("v1", "organisaties", "verenigingen", "verenigingen", "vCode", "lidmaatschappen", "lidmaatschapId", "delete"), List.of(RestPathParameter.of("vCode"), RestPathParameter.of("lidmaatschapId")), "/v1/organisaties/verenigingen/verenigingen/{vCode}/lidmaatschappen/{lidmaatschapId}", "DELETE"),
            new MockRestMapping(List.of("v1", "organisaties", "verenigingen", "verenigingen", "vCode", "locaties", "post"), List.of(RestPathParameter.of("vCode")), "/v1/organisaties/verenigingen/verenigingen/{vCode}/locaties", "POST"),
            new MockRestMapping(List.of("v1", "organisaties", "verenigingen", "verenigingen", "vCode", "locaties", "locatieId", "patch"), List.of(RestPathParameter.of("vCode"), RestPathParameter.of("locatieId")), "/v1/organisaties/verenigingen/verenigingen/{vCode}/locaties/{locatieId}", "PATCH"),
            new MockRestMapping(List.of("v1", "organisaties", "verenigingen", "verenigingen", "vCode", "locaties", "locatieId", "delete"), List.of(RestPathParameter.of("vCode"), RestPathParameter.of("locatieId")), "/v1/organisaties/verenigingen/verenigingen/{vCode}/locaties/{locatieId}", "DELETE"),
            new MockRestMapping(List.of("v1", "organisaties", "verenigingen", "verenigingen", "vCode", "subtype", "patch"), List.of(RestPathParameter.of("vCode")), "/v1/organisaties/verenigingen/verenigingen/{vCode}/subtype", "PATCH"),
            new MockRestMapping(List.of("v1", "organisaties", "verenigingen", "verenigingen", "vCode", "vertegenwoordigers", "post"), List.of(RestPathParameter.of("vCode")), "/v1/organisaties/verenigingen/verenigingen/{vCode}/vertegenwoordigers", "POST"),
            new MockRestMapping(List.of("v1", "organisaties", "verenigingen", "verenigingen", "vCode", "vertegenwoordigers", "vertegenwoordigerId", "patch"), List.of(RestPathParameter.of("vCode"), RestPathParameter.of("vertegenwoordigerId")), "/v1/organisaties/verenigingen/verenigingen/{vCode}/vertegenwoordigers/{vertegenwoordigerId}", "PATCH"),
            new MockRestMapping(List.of("v1", "organisaties", "verenigingen", "verenigingen", "vCode", "vertegenwoordigers", "vertegenwoordigerId", "delete"), List.of(RestPathParameter.of("vCode"), RestPathParameter.of("vertegenwoordigerId")), "/v1/organisaties/verenigingen/verenigingen/{vCode}/vertegenwoordigers/{vertegenwoordigerId}", "DELETE"),

            // Persoon
            new MockRestMapping(List.of("v1", "persoon", "handtekeningen", "get"), List.of(RestHeaderParameter.of("x-insz")), "/v1/persoon/handtekeningen", "GET"),

            // SocZek HandicapVolledigeDossiers
            new MockRestMapping(List.of("v1", "socZek", "handicap", "volledigeDossiers", "get"), List.of(RestQueryParameter.of("rrnr")), "/v1/socZek/handicap/volledigeDossiers", "GET"),

            // SocZek SocialeHuisvestingCIR
            new MockRestMapping(List.of("v1", "socZek", "socialeHuisvesting", "dossiers", "zoeken", "post"), List.of(RestBodyParameter.of("dossierType"), RestBodyParameter.of("dossierStatus"), RestBodyParameter.of("dossierNummer")), "/v1/socZek/socialeHuisvesting/dossiers/zoeken", "POST"),
            new MockRestMapping(List.of("v1", "socZek", "socialeHuisvesting", "dossiers", "dossier", "dossierNummer", "get"), List.of(RestPathParameter.of("dossierNummer")), "/v1/socZek/socialeHuisvesting/dossiers/dossier/{dossierNummer}", "GET"),
            new MockRestMapping(List.of("v1", "socZek", "socialeHuisvesting", "dossiers", "dossier", "dossierNummer", "put"), List.of(RestPathParameter.of("dossierNummer")), "/v1/socZek/socialeHuisvesting/dossiers/dossier/{dossierNummer}", "PUT"),
            new MockRestMapping(List.of("v1", "socZek", "socialeHuisvesting", "dossiers", "bevestigDossierVerwerking", "post"), List.of(RestBodyParameter.of("dossierNummer")), "/v1/socZek/socialeHuisvesting/dossiers/bevestigDossierVerwerking", "POST"),
            new MockRestMapping(List.of("v1", "socZek", "socialeHuisvesting", "dossiers", "bijlage", "post"), List.of(RestBodyParameter.of("dossierNummer"), RestBodyParameter.of("partijCode"), RestBodyParameter.of("bijlageIdentificatie")), "/v1/socZek/socialeHuisvesting/dossiers/bijlage", "POST"),
            new MockRestMapping(List.of("v1", "socZek", "socialeHuisvesting", "dossiers", "dossierMetBijlages", "dossierNummer", "get"), List.of(RestPathParameter.of("dossierNummer")), "/v1/socZek/socialeHuisvesting/dossiers/dossierMetBijlages/{dossierNummer}", "GET"),
            new MockRestMapping(List.of("v1", "socZek", "socialeHuisvesting", "dossiers", "partijInformatieWijzigingen", "post"), List.of(RestBodyParameter.of("partijCode")), "/v1/socZek/socialeHuisvesting/dossiers/partijInformatieWijzigingen", "POST"),
            new MockRestMapping(List.of("v1", "socZek", "socialeHuisvesting", "dossiers", "verwerkDossierActie", "post"), List.of(RestBodyParameter.of("dossierNummer"), RestBodyParameter.of("actie")), "/v1/socZek/socialeHuisvesting/dossiers/verwerkDossierActie", "POST"),
            new MockRestMapping(List.of("v1", "socZek", "socialeHuisvesting", "dossiers", "wijzigDossierRangschikkingen", "post"), List.of(RestBodyParameter.of("partijCode")), "/v1/socZek/socialeHuisvesting/dossiers/wijzigDossierRangschikkingen", "POST"),
            new MockRestMapping(List.of("v1", "socZek", "socialeHuisvesting", "dossiers", "zwarteLijstMeldingen", "post"), List.of(RestBodyParameter.of("partijCode")), "/v1/socZek/socialeHuisvesting/dossiers/zwarteLijstMeldingen", "POST"),
            new MockRestMapping(List.of("v1", "socZek", "socialeHuisvesting", "dossiers", "zwarteLijstMeldingVoor", "post"), List.of(RestBodyParameter.of("rijksregisternummer")), "/v1/socZek/socialeHuisvesting/dossiers/zwarteLijstMeldingVoor", "POST"),
            new MockRestMapping(List.of("v1", "socZek", "socialeHuisvesting", "gemeentes", "get"), List.of(), "/v1/socZek/socialeHuisvesting/gemeentes", "GET", true),
            new MockRestMapping(List.of("v1", "socZek", "socialeHuisvesting", "gemeentes", "wijkenVoorGemeente", "gemeenteNisCode", "get"), List.of(RestPathParameter.of("gemeenteNisCode")), "/v1/socZek/socialeHuisvesting/gemeentes/wijkenVoorGemeente/{gemeenteNisCode}", "GET"),
            new MockRestMapping(List.of("v1", "socZek", "socialeHuisvesting", "gemeentes", "wijkenVoorGemeente", "gemeenteNisCode", "put"), List.of(RestPathParameter.of("gemeenteNisCode")), "/v1/socZek/socialeHuisvesting/gemeentes/wijkenVoorGemeente/{gemeenteNisCode}", "PUT"),

            // SocZek SocialeHuisvestingCWR
            new MockRestMapping(List.of("v1", "socZek", "socialeHuisvesting", "woningen", "vmswWoningId", "get"), List.of(RestPathParameter.of("vmswWoningId")), "/v1/socZek/socialeHuisvesting/woningen/{vmswWoningId}", "GET"),
            new MockRestMapping(List.of("v1", "socZek", "socialeHuisvesting", "woningen", "vmswWoningId", "patch"), List.of(RestPathParameter.of("vmswWoningId")), "/v1/socZek/socialeHuisvesting/woningen/{vmswWoningId}", "PATCH"),
            new MockRestMapping(List.of("v1", "socZek", "socialeHuisvesting", "woningen", "get"), List.of(RestQueryParameter.of("gemeenteNISCode"), RestQueryParameter.of("woonmaatschappijWoningId"), RestQueryParameter.of("gebouweenheidId")), "/v1/socZek/socialeHuisvesting/woningen", "GET"),
            new MockRestMapping(List.of("v1", "socZek", "socialeHuisvesting", "woningen", "post"), List.of(RestBodyParameter.of("gebouweenheidId")), "/v1/socZek/socialeHuisvesting/woningen", "POST"),
            new MockRestMapping(List.of("v1", "socZek", "socialeHuisvesting", "woningen", "vmswWoningId", "woningkenmerkscores", "get"), List.of(RestPathParameter.of("vmswWoningId"), RestQueryParameter.of("woningKenmerkCategorie")), "/v1/socZek/socialeHuisvesting/woningen/{vmswWoningId}/woningkenmerkscores", "GET"),
            new MockRestMapping(List.of("v1", "socZek", "socialeHuisvesting", "woningen", "vmswWoningId", "woningkenmerkscores", "patch"), List.of(RestPathParameter.of("vmswWoningId")), "/v1/socZek/socialeHuisvesting/woningen/{vmswWoningId}/woningkenmerkscores", "PATCH"),
            new MockRestMapping(List.of("v1", "socZek", "socialeHuisvesting", "woningen", "vmswWoningId", "markthuurwaarden", "get"), List.of(RestPathParameter.of("vmswWoningId")), "/v1/socZek/socialeHuisvesting/woningen/{vmswWoningId}/markthuurwaarden", "GET"),
            new MockRestMapping(List.of("v1", "socZek", "socialeHuisvesting", "woningen", "markthuurwaarden", "simulatie", "post"), List.of(), "/v1/socZek/socialeHuisvesting/woningen/markthuurwaarden/simulatie", "POST", true),
            new MockRestMapping(List.of("v1", "socZek", "socialeHuisvesting", "woningen", "vmswWoningId", "conditiescore", "get"), List.of(RestPathParameter.of("vmswWoningId")), "/v1/socZek/socialeHuisvesting/woningen/{vmswWoningId}/conditiescore", "GET"),
            new MockRestMapping(List.of("v1", "socZek", "socialeHuisvesting", "woningen", "conditiescore", "simulatie", "post"), List.of(), "/v1/socZek/socialeHuisvesting/woningen/conditiescore/simulatie", "POST", true),
            new MockRestMapping(List.of("v1", "socZek", "socialeHuisvesting", "woningen", "vmswWoningId", "energiecorrectie", "get"), List.of(RestPathParameter.of("vmswWoningId"), RestQueryParameter.of("epcVersie")), "/v1/socZek/socialeHuisvesting/woningen/{vmswWoningId}/energiecorrectie", "GET"),
            new MockRestMapping(List.of("v1", "socZek", "socialeHuisvesting", "woningen", "vmswWoningId", "epcwaarden", "get"), List.of(RestPathParameter.of("vmswWoningId")), "/v1/socZek/socialeHuisvesting/woningen/{vmswWoningId}/epcwaarden", "GET"),
            new MockRestMapping(List.of("v1", "socZek", "socialeHuisvesting", "woningen", "vmswWoningId", "epcwaarden", "brondata", "documentId", "get"), List.of(RestPathParameter.of("vmswWoningId"), RestPathParameter.of("documentId")), "/v1/socZek/socialeHuisvesting/woningen/{vmswWoningId}/epcwaarden/brondata/{documentId}", "GET"),
            new MockRestMapping(List.of("v1", "socZek", "socialeHuisvesting", "woningen", "vmswWoningId", "epcwaarden", "document", "documentId", "get"), List.of(RestPathParameter.of("vmswWoningId"), RestPathParameter.of("documentId")), "/v1/socZek/socialeHuisvesting/woningen/{vmswWoningId}/epcwaarden/document/{documentId}", "GET"),
            new MockRestMapping(List.of("v1", "socZek", "socialeHuisvesting", "woningen", "vmswWoningId", "epcwaarden", "certificaat", "post"), List.of(RestPathParameter.of("vmswWoningId")), "/v1/socZek/socialeHuisvesting/woningen/{vmswWoningId}/epcwaarden/certificaat", "POST"),
            new MockRestMapping(List.of("v1", "socZek", "socialeHuisvesting", "woningen", "vmswWoningId", "epcwaarden", "markeerReferentieversie", "patch"), List.of(RestPathParameter.of("vmswWoningId"), RestBodyParameter.of("epcVersieId")), "/v1/socZek/socialeHuisvesting/woningen/{vmswWoningId}/epcwaarden/markeerReferentieversie", "PATCH"),
            new MockRestMapping(List.of("v1", "socZek", "socialeHuisvesting", "woningen", "vmswWoningId", "epcwaarden", "epcVersieId", "delete"), List.of(RestPathParameter.of("vmswWoningId"), RestPathParameter.of("epcVersieId")), "/v1/socZek/socialeHuisvesting/woningen/{vmswWoningId}/epcwaarden/{epcVersieId}", "DELETE")
    );

    // url parameters should be marked with {parameterName}
    // priority: when set, overrides the default calculated priority (lower number = higher precedence in WireMock)
    public MockRestMapping(List<String> folderPath, List<RestParameter> restParameters, String url, String method) {
        this(folderPath, restParameters, url, method, 66, false, MissingParameterConfiguration.EmptyString);
    }

    public MockRestMapping(List<String> folderPath, List<RestParameter> restParameters, String url, String method, Integer priority) {
        this(folderPath, restParameters, url, method, priority, false, MissingParameterConfiguration.EmptyString);
    }

    public MockRestMapping(List<String> folderPath, List<RestParameter> restParameters, String url, String method, boolean defaultOnly) {
        this(folderPath, restParameters, url, method, 66, defaultOnly, MissingParameterConfiguration.EmptyString);
    }

    public String toPath() {
        return String.join("/", folderPath);
    }

    public String getId() {
        return String.format("%s %s", method, url);
    }
}
