package be.vlaanderen.vip.magda.magdamock.config;

import java.util.List;

public record MockRestMapping(List<String> folderPath, List<String> queryParameters, List<String> urlParameters,
                              List<String> requestBodyParameters, String url, String method, Integer priority,
                              boolean defaultOnly) {
    public static final List<MockRestMapping> MAPPINGS = List.of(
            // Mobility
            new MockRestMapping(List.of("v1", "mobility", "registrations", "get", "plateNr"), List.of("plateNr"), List.of(), List.of(), "/v1/mobility/registrations", "GET", 40),
            new MockRestMapping(List.of("v1", "mobility", "registrations", "get", "plateUID"), List.of("plateUID"), List.of(), List.of(), "/v1/mobility/registrations", "GET", 41),
            new MockRestMapping(List.of("v1", "mobility", "registrations", "get", "vin"), List.of("vin", "unifier"), List.of(), List.of(), "/v1/mobility/registrations", "GET", 42),
            new MockRestMapping(List.of("v1", "mobility", "registrations", "get", "nationalNr"), List.of("nationalNr"), List.of(), List.of(), "/v1/mobility/registrations", "GET", 43),
            new MockRestMapping(List.of("v1", "mobility", "registrations", "get", "companyNr"), List.of("companyNr"), List.of(), List.of(), "/v1/mobility/registrations", "GET", 44),
            new MockRestMapping(List.of("v1", "mobility", "registrations", "get"), List.of(), List.of(), List.of(), "/v1/mobility/registrations", "GET", 50, true),

            // Organisaties.verenigingen
            new MockRestMapping(List.of("v1", "organisaties", "verenigingen", "verenigingen", "zoeken", "get"), List.of("q"), List.of(), List.of(), "/v1/organisaties/verenigingen/verenigingen/zoeken", "GET", 40),
            new MockRestMapping(List.of("v1", "organisaties", "verenigingen", "verenigingen", "vCode", "get"), List.of(), List.of("vCode"), List.of(), "/v1/organisaties/verenigingen/verenigingen/{vCode}", "GET"),
            new MockRestMapping(List.of("v1", "organisaties", "verenigingen", "verenigingen", "vCode", "patch"), List.of(), List.of("vCode"), List.of(), "/v1/organisaties/verenigingen/verenigingen/{vCode}", "PATCH"),
            new MockRestMapping(List.of("v1", "organisaties", "verenigingen", "verenigingen", "vCode", "stop", "post"), List.of(), List.of("vCode"), List.of(), "/v1/organisaties/verenigingen/verenigingen/{vCode}/stop", "POST"),
            new MockRestMapping(List.of("v1", "organisaties", "verenigingen", "verenigingen", "vCode", "historiek", "get"), List.of(), List.of("vCode"), List.of(), "/v1/organisaties/verenigingen/verenigingen/{vCode}/historiek", "GET"),
            new MockRestMapping(List.of("v1", "organisaties", "verenigingen", "verenigingen", "feitelijkeverenigingen", "post"), List.of(), List.of(), List.of("naam"), "/v1/organisaties/verenigingen/verenigingen/feitelijkeverenigingen", "POST"),
            new MockRestMapping(List.of("v1", "organisaties", "verenigingen", "verenigingen", "kbo", "post"), List.of(), List.of(), List.of("kboNummer"), "/v1/organisaties/verenigingen/verenigingen/kbo", "POST"),
            new MockRestMapping(List.of("v1", "organisaties", "verenigingen", "verenigingen", "vzer", "post"), List.of(), List.of(), List.of("naam"), "/v1/organisaties/verenigingen/verenigingen/vzer", "POST"),
            new MockRestMapping(List.of("v1", "organisaties", "verenigingen", "verenigingen", "vCode", "contactgegevens", "contactgegevenId", "patch"), List.of(), List.of("vCode", "contactgegevens"), List.of(), "/v1/organisaties/verenigingen/verenigingen/{vCode}/contactgegevens/{contactgegevens}", "PATCH"),
            new MockRestMapping(List.of("v1", "organisaties", "verenigingen", "verenigingen", "vCode", "contactgegevens", "contactgegevenId", "delete"), List.of(), List.of("vCode", "contactgegevens"), List.of(), "/v1/organisaties/verenigingen/verenigingen/{vCode}/contactgegevens/{contactgegevens}", "DELETE"),
            new MockRestMapping(List.of("v1", "organisaties", "verenigingen", "verenigingen", "vCode", "kbo", "patch"), List.of(), List.of("vCode"), List.of(), "/v1/organisaties/verenigingen/verenigingen/{vCode}/kbo", "PATCH"),
            new MockRestMapping(List.of("v1", "organisaties", "verenigingen", "verenigingen", "vCode", "kbo", "contactgegevens", "contactgegevenId", "patch"), List.of(), List.of("vCode", "contactgegevenId"), List.of(), "/v1/organisaties/verenigingen/verenigingen/{vCode}/kbo/contactgegevens/{contactgegevenId}", "PATCH"),
            new MockRestMapping(List.of("v1", "organisaties", "verenigingen", "verenigingen", "vCode", "kbo", "locaties", "locatieId", "patch"), List.of(), List.of("vCode", "locatieId"), List.of(), "/v1/organisaties/verenigingen/verenigingen/{vCode}/kbo/locaties/{locatieId}", "PATCH"),
            new MockRestMapping(List.of("v1", "organisaties", "verenigingen", "verenigingen", "vCode", "lidmaatschappen", "post"), List.of(), List.of("vCode"), List.of(), "/v1/organisaties/verenigingen/verenigingen/{vCode}/lidmaatschappen", "POST"),
            new MockRestMapping(List.of("v1", "organisaties", "verenigingen", "verenigingen", "vCode", "lidmaatschappen", "lidmaatschapId", "patch"), List.of(), List.of("vCode", "lidmaatschapId"), List.of(), "/v1/organisaties/verenigingen/verenigingen/{vCode}/lidmaatschappen/{lidmaatschapId}", "PATCH"),
            new MockRestMapping(List.of("v1", "organisaties", "verenigingen", "verenigingen", "vCode", "lidmaatschappen", "lidmaatschapId", "delete"), List.of(), List.of("vCode", "lidmaatschapId"), List.of(), "/v1/organisaties/verenigingen/verenigingen/{vCode}/lidmaatschappen/{lidmaatschapId}", "DELETE"),
            new MockRestMapping(List.of("v1", "organisaties", "verenigingen", "verenigingen", "vCode", "locaties", "post"), List.of(), List.of("vCode"), List.of(), "/v1/organisaties/verenigingen/verenigingen/{vCode}/locaties", "POST"),
            new MockRestMapping(List.of("v1", "organisaties", "verenigingen", "verenigingen", "vCode", "locaties", "locatieId", "patch"), List.of(), List.of("vCode", "locatieId"), List.of(), "/v1/organisaties/verenigingen/verenigingen/{vCode}/locaties/{locatieId}", "PATCH"),
            new MockRestMapping(List.of("v1", "organisaties", "verenigingen", "verenigingen", "vCode", "locaties", "locatieId", "delete"), List.of(), List.of("vCode", "locatieId"), List.of(), "/v1/organisaties/verenigingen/verenigingen/{vCode}/locaties/{locatieId}", "DELETE"),
            new MockRestMapping(List.of("v1", "organisaties", "verenigingen", "verenigingen", "vCode", "subtype", "patch"), List.of(), List.of("vCode"), List.of(), "/v1/organisaties/verenigingen/verenigingen/{vCode}/subtype", "PATCH"),
            new MockRestMapping(List.of("v1", "organisaties", "verenigingen", "verenigingen", "vCode", "vertegenwoordigers", "post"), List.of(), List.of("vCode"), List.of(), "/v1/organisaties/verenigingen/verenigingen/{vCode}/vertegenwoordigers", "POST"),
            new MockRestMapping(List.of("v1", "organisaties", "verenigingen", "verenigingen", "vCode", "vertegenwoordigers", "vertegenwoordigerId", "patch"), List.of(), List.of("vCode", "vertegenwoordigerId"), List.of(), "/v1/organisaties/verenigingen/verenigingen/{vCode}/vertegenwoordigers/{vertegenwoordigerId}", "PATCH"),
            new MockRestMapping(List.of("v1", "organisaties", "verenigingen", "verenigingen", "vCode", "vertegenwoordigers", "vertegenwoordigerId", "delete"), List.of(), List.of("vCode", "vertegenwoordigerId"), List.of(), "/v1/organisaties/verenigingen/verenigingen/{vCode}/vertegenwoordigers/{vertegenwoordigerId}", "DELETE"),

            // SocZek HandicapVolledigeDossiers
            new MockRestMapping(List.of("v1", "socZek", "handicap", "volledigeDossiers", "get"), List.of("rrnr"), List.of(), List.of(), "/v1/socZek/handicap/volledigeDossiers", "GET"),

            // SocZek SocialeHuisvestingCIR
            new MockRestMapping(List.of("v1", "socZek", "socialeHuisvesting", "dossiers", "zoeken", "post"), List.of(), List.of(), List.of("dossierType", "dossierStatus", "dossierNummer"), "/v1/socZek/socialeHuisvesting/dossiers/zoeken", "POST"),
            new MockRestMapping(List.of("v1", "socZek", "socialeHuisvesting", "dossiers", "dossier", "dossierNummer", "get"), List.of(), List.of("dossierNummer"), List.of(), "/v1/socZek/socialeHuisvesting/dossiers/dossier/{dossierNummer}", "GET"),
            new MockRestMapping(List.of("v1", "socZek", "socialeHuisvesting", "dossiers", "dossier", "dossierNummer", "put"), List.of(), List.of("dossierNummer"), List.of(), "/v1/socZek/socialeHuisvesting/dossiers/dossier/{dossierNummer}", "PUT"),
            new MockRestMapping(List.of("v1", "socZek", "socialeHuisvesting", "dossiers", "bevestigDossierVerwerking", "post"), List.of(), List.of(), List.of("dossierNummer"), "/v1/socZek/socialeHuisvesting/dossiers/bevestigDossierVerwerking", "POST"),
            new MockRestMapping(List.of("v1", "socZek", "socialeHuisvesting", "dossiers", "bijlage", "post"), List.of(), List.of(), List.of("dossierNummer", "partijCode", "bijlageIdentificatie"), "/v1/socZek/socialeHuisvesting/dossiers/bijlage", "POST"),
            new MockRestMapping(List.of("v1", "socZek", "socialeHuisvesting", "dossiers", "dossierMetBijlages", "dossierNummer", "get"), List.of(), List.of("dossierNummer"), List.of(), "/v1/socZek/socialeHuisvesting/dossiers/dossierMetBijlages/{dossierNummer}", "GET"),
            new MockRestMapping(List.of("v1", "socZek", "socialeHuisvesting", "dossiers", "partijInformatieWijzigingen", "post"), List.of(), List.of(), List.of("partijCode"), "/v1/socZek/socialeHuisvesting/dossiers/partijInformatieWijzigingen", "POST"),
            new MockRestMapping(List.of("v1", "socZek", "socialeHuisvesting", "dossiers", "verwerkDossierActie", "post"), List.of(), List.of(), List.of("dossierNummer", "actie"), "/v1/socZek/socialeHuisvesting/dossiers/verwerkDossierActie", "POST"),
            new MockRestMapping(List.of("v1", "socZek", "socialeHuisvesting", "dossiers", "wijzigDossierRangschikkingen", "post"), List.of(), List.of(), List.of("partijCode"), "/v1/socZek/socialeHuisvesting/dossiers/wijzigDossierRangschikkingen", "POST"),
            new MockRestMapping(List.of("v1", "socZek", "socialeHuisvesting", "dossiers", "zwarteLijstMeldingen", "post"), List.of(), List.of(), List.of("partijCode"), "/v1/socZek/socialeHuisvesting/dossiers/zwarteLijstMeldingen", "POST"),
            new MockRestMapping(List.of("v1", "socZek", "socialeHuisvesting", "dossiers", "zwarteLijstMeldingVoor", "post"), List.of(), List.of(), List.of("rijksregisternummer"), "/v1/socZek/socialeHuisvesting/dossiers/zwarteLijstMeldingVoor", "POST"),
            new MockRestMapping(List.of("v1", "socZek", "socialeHuisvesting", "gemeentes", "get"), List.of(), List.of(), List.of(), "/v1/socZek/socialeHuisvesting/gemeentes", "GET", true),
            new MockRestMapping(List.of("v1", "socZek", "socialeHuisvesting", "gemeentes", "wijkenVoorGemeente", "gemeenteNisCode", "get"), List.of(), List.of("gemeenteNisCode"), List.of(), "/v1/socZek/socialeHuisvesting/gemeentes/wijkenVoorGemeente/{gemeenteNisCode}", "GET"),
            new MockRestMapping(List.of("v1", "socZek", "socialeHuisvesting", "gemeentes", "wijkenVoorGemeente", "gemeenteNisCode", "put"), List.of(), List.of("gemeenteNisCode"), List.of(), "/v1/socZek/socialeHuisvesting/gemeentes/wijkenVoorGemeente/{gemeenteNisCode}", "PUT"),

            // SocZek SocialeHuisvestingCWR
            new MockRestMapping(List.of("v1", "socZek", "socialeHuisvesting", "woningen", "vmswWoningId", "get"), List.of(), List.of("vmswWoningId"), List.of(), "/v1/socZek/socialeHuisvesting/woningen/{vmswWoningId}", "GET"),
            new MockRestMapping(List.of("v1", "socZek", "socialeHuisvesting", "woningen", "vmswWoningId", "patch"), List.of(), List.of("vmswWoningId"), List.of(), "/v1/socZek/socialeHuisvesting/woningen/{vmswWoningId}", "PATCH"),
            new MockRestMapping(List.of("v1", "socZek", "socialeHuisvesting", "woningen", "get"), List.of("gemeenteNISCode", "woonmaatschappijWoningId", "gebouweenheidId"), List.of(), List.of(), "/v1/socZek/socialeHuisvesting/woningen", "GET"),
            new MockRestMapping(List.of("v1", "socZek", "socialeHuisvesting", "woningen", "post"), List.of(), List.of(), List.of("gebouweenheidId"), "/v1/socZek/socialeHuisvesting/woningen", "POST"),
            new MockRestMapping(List.of("v1", "socZek", "socialeHuisvesting", "woningen", "vmswWoningId", "woningkenmerkscores", "get"), List.of("woningKenmerkCategorie"), List.of("vmswWoningId"), List.of(), "/v1/socZek/socialeHuisvesting/woningen/{vmswWoningId}/woningkenmerkscores", "GET"),
            new MockRestMapping(List.of("v1", "socZek", "socialeHuisvesting", "woningen", "vmswWoningId", "woningkenmerkscores", "patch"), List.of(), List.of("vmswWoningId"), List.of(), "/v1/socZek/socialeHuisvesting/woningen/{vmswWoningId}/woningkenmerkscores", "PATCH"),
            new MockRestMapping(List.of("v1", "socZek", "socialeHuisvesting", "woningen", "vmswWoningId", "markthuurwaarden", "get"), List.of(), List.of("vmswWoningId"), List.of(), "/v1/socZek/socialeHuisvesting/woningen/{vmswWoningId}/markthuurwaarden", "GET"),
            new MockRestMapping(List.of("v1", "socZek", "socialeHuisvesting", "woningen", "markthuurwaarden", "simulatie", "post"), List.of(), List.of(), List.of(), "/v1/socZek/socialeHuisvesting/woningen/markthuurwaarden/simulatie", "POST", true),
            new MockRestMapping(List.of("v1", "socZek", "socialeHuisvesting", "woningen", "vmswWoningId", "conditiescore", "get"), List.of(), List.of("vmswWoningId"), List.of(), "/v1/socZek/socialeHuisvesting/woningen/{vmswWoningId}/conditiescore", "GET"),
            new MockRestMapping(List.of("v1", "socZek", "socialeHuisvesting", "woningen", "conditiescore", "simulatie", "post"), List.of(), List.of(), List.of(), "/v1/socZek/socialeHuisvesting/woningen/conditiescore/simulatie", "POST", true),
            new MockRestMapping(List.of("v1", "socZek", "socialeHuisvesting", "woningen", "vmswWoningId", "energiecorrectie", "get"), List.of("epcVersie"), List.of("vmswWoningId"), List.of(), "/v1/socZek/socialeHuisvesting/woningen/{vmswWoningId}/energiecorrectie", "GET"),
            new MockRestMapping(List.of("v1", "socZek", "socialeHuisvesting", "woningen", "vmswWoningId", "epcwaarden", "get"), List.of(), List.of("vmswWoningId"), List.of(), "/v1/socZek/socialeHuisvesting/woningen/{vmswWoningId}/epcwaarden", "GET"),
            new MockRestMapping(List.of("v1", "socZek", "socialeHuisvesting", "woningen", "vmswWoningId", "epcwaarden", "brondata", "documentId", "get"), List.of(), List.of("vmswWoningId", "documentId"), List.of(), "/v1/socZek/socialeHuisvesting/woningen/{vmswWoningId}/epcwaarden/brondata/{documentId}", "GET"),
            new MockRestMapping(List.of("v1", "socZek", "socialeHuisvesting", "woningen", "vmswWoningId", "epcwaarden", "document", "documentId", "get"), List.of(), List.of("vmswWoningId", "documentId"), List.of(), "/v1/socZek/socialeHuisvesting/woningen/{vmswWoningId}/epcwaarden/document/{documentId}", "GET"),
            new MockRestMapping(List.of("v1", "socZek", "socialeHuisvesting", "woningen", "vmswWoningId", "epcwaarden", "certificaat", "post"), List.of(), List.of("vmswWoningId"), List.of(), "/v1/socZek/socialeHuisvesting/woningen/{vmswWoningId}/epcwaarden/certificaat", "POST"),
            new MockRestMapping(List.of("v1", "socZek", "socialeHuisvesting", "woningen", "vmswWoningId", "epcwaarden", "markeerReferentieversie", "patch"), List.of(), List.of("vmswWoningId"), List.of("epcVersieId"), "/v1/socZek/socialeHuisvesting/woningen/{vmswWoningId}/epcwaarden/markeerReferentieversie", "PATCH"),
            new MockRestMapping(List.of("v1", "socZek", "socialeHuisvesting", "woningen", "vmswWoningId", "epcwaarden", "epcVersieId", "delete"), List.of(), List.of("vmswWoningId", "epcVersieId"), List.of(), "/v1/socZek/socialeHuisvesting/woningen/{vmswWoningId}/epcwaarden/{epcVersieId}", "DELETE")
    );

    // url parameters should be marked with {parameterName}
    // priority: when set, overrides the default calculated priority (lower number = higher precedence in WireMock)
    public MockRestMapping(List<String> folderPath, List<String> queryParameters, List<String> urlParameters, List<String> requestBodyParameters, String url, String method) {
        this(folderPath, queryParameters, urlParameters, requestBodyParameters, url, method, 66, false);
    }

    public MockRestMapping(List<String> folderPath, List<String> queryParameters, List<String> urlParameters, List<String> requestBodyParameters, String url, String method, Integer priority) {
        this(folderPath, queryParameters, urlParameters, requestBodyParameters, url, method, priority, false);
    }

    public MockRestMapping(List<String> folderPath, List<String> queryParameters, List<String> urlParameters, List<String> requestBodyParameters, String url, String method, boolean defaultOnly) {
        this(folderPath, queryParameters, urlParameters, requestBodyParameters, url, method, 66, defaultOnly);
    }

    public String toPath() {
        return String.join("/", folderPath);
    }

    public String getId() {
        return String.format("%s %s", method, url);
    }
}
