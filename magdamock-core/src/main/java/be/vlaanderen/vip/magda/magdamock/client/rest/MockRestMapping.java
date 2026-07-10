package be.vlaanderen.vip.magda.magdamock.client.rest;

import java.util.List;

public record MockRestMapping(List<String> folderPath, List<String> queryParameters, Integer urlParametersSize,
                              List<String> requestBodyParameters, String url, String method, Integer priority,
                              boolean defaultOnly) {
    public static final List<MockRestMapping> MAPPINGS = List.of(
            // Mobility
            new MockRestMapping(List.of("v1", "mobility", "registrations", "get", "plateNr"), List.of("plateNr"), 0, List.of(), "/v1/mobility/registrations", "GET", 40),
            new MockRestMapping(List.of("v1", "mobility", "registrations", "get", "plateUID"), List.of("plateUID"), 0, List.of(), "/v1/mobility/registrations", "GET", 41),
            new MockRestMapping(List.of("v1", "mobility", "registrations", "get", "vin"), List.of("vin", "unifier"), 0, List.of(), "/v1/mobility/registrations", "GET", 42),
            new MockRestMapping(List.of("v1", "mobility", "registrations", "get", "nationalNr"), List.of("nationalNr"), 0, List.of(), "/v1/mobility/registrations", "GET", 43),
            new MockRestMapping(List.of("v1", "mobility", "registrations", "get", "companyNr"), List.of("companyNr"), 0, List.of(), "/v1/mobility/registrations", "GET", 44),
            new MockRestMapping(List.of("v1", "mobility", "registrations", "get"), List.of(), 0, List.of(), "/v1/mobility/registrations", "GET", 30, true),

            // Organisaties.verenigingen
            new MockRestMapping(List.of("v1", "organisaties", "verenigingen", "verenigingen", "zoeken", "get"), List.of("q"), 0, List.of(), "/v1/organisaties/verenigingen/verenigingen/zoeken", "GET", 40),
            new MockRestMapping(List.of("v1", "organisaties", "verenigingen", "verenigingen", "get"), List.of(), 1, List.of(), "/v1/organisaties/verenigingen/verenigingen/%s", "GET"),
            new MockRestMapping(List.of("v1", "organisaties", "verenigingen", "verenigingen", "patch"), List.of(), 1, List.of(), "/v1/organisaties/verenigingen/verenigingen/%s", "PATCH"),
            new MockRestMapping(List.of("v1", "organisaties", "verenigingen", "verenigingen", "stop", "post"), List.of(), 1, List.of(), "/v1/organisaties/verenigingen/verenigingen/%s/stop", "POST"),
            new MockRestMapping(List.of("v1", "organisaties", "verenigingen", "verenigingen", "historiek", "get"), List.of(), 1, List.of(), "/v1/organisaties/verenigingen/verenigingen/%s/historiek", "GET"),
            new MockRestMapping(List.of("v1", "organisaties", "verenigingen", "verenigingen", "feitelijkeverenigingen", "post"), List.of(), 0, List.of("naam"), "/v1/organisaties/verenigingen/verenigingen/feitelijkeverenigingen", "POST"),
            new MockRestMapping(List.of("v1", "organisaties", "verenigingen", "verenigingen", "kbo", "post"), List.of(), 0, List.of("kboNummer"), "/v1/organisaties/verenigingen/verenigingen/kbo", "POST"),
            new MockRestMapping(List.of("v1", "organisaties", "verenigingen", "verenigingen", "vzer", "post"), List.of(), 0, List.of("naam"), "/v1/organisaties/verenigingen/verenigingen/vzer", "POST"),
            new MockRestMapping(List.of("v1", "organisaties", "verenigingen", "verenigingen", "contactgegevens", "patch"), List.of(), 2, List.of(), "/v1/organisaties/verenigingen/verenigingen/%s/contactgegevens/%s", "PATCH"),
            new MockRestMapping(List.of("v1", "organisaties", "verenigingen", "verenigingen", "contactgegevens", "delete"), List.of(), 2, List.of(), "/v1/organisaties/verenigingen/verenigingen/%s/contactgegevens/%s", "DELETE"),
            new MockRestMapping(List.of("v1", "organisaties", "verenigingen", "verenigingen", "kbo", "patch"), List.of(), 1, List.of(), "/v1/organisaties/verenigingen/verenigingen/%s/kbo", "PATCH"),
            new MockRestMapping(List.of("v1", "organisaties", "verenigingen", "verenigingen", "kbo", "contactgegevens", "patch"), List.of(), 2, List.of(), "/v1/organisaties/verenigingen/verenigingen/%s/kbo/contactgegevens/%s", "PATCH"),
            new MockRestMapping(List.of("v1", "organisaties", "verenigingen", "verenigingen", "kbo", "locaties", "patch"), List.of(), 2, List.of(), "/v1/organisaties/verenigingen/verenigingen/%s/kbo/locaties/%s", "PATCH"),
            new MockRestMapping(List.of("v1", "organisaties", "verenigingen", "verenigingen", "lidmaatschappen", "post"), List.of(), 1, List.of(), "/v1/organisaties/verenigingen/verenigingen/%s/lidmaatschappen", "POST"),
            new MockRestMapping(List.of("v1", "organisaties", "verenigingen", "verenigingen", "lidmaatschappen", "patch"), List.of(), 2, List.of(), "/v1/organisaties/verenigingen/verenigingen/%s/lidmaatschappen/%s", "PATCH"),
            new MockRestMapping(List.of("v1", "organisaties", "verenigingen", "verenigingen", "lidmaatschappen", "delete"), List.of(), 2, List.of(), "/v1/organisaties/verenigingen/verenigingen/%s/lidmaatschappen/%s", "DELETE"),
            new MockRestMapping(List.of("v1", "organisaties", "verenigingen", "verenigingen", "locaties", "post"), List.of(), 1, List.of(), "/v1/organisaties/verenigingen/verenigingen/%s/locaties", "POST"),
            new MockRestMapping(List.of("v1", "organisaties", "verenigingen", "verenigingen", "locaties", "patch"), List.of(), 2, List.of(), "/v1/organisaties/verenigingen/verenigingen/%s/locaties/%s", "PATCH"),
            new MockRestMapping(List.of("v1", "organisaties", "verenigingen", "verenigingen", "locaties", "delete"), List.of(), 2, List.of(), "/v1/organisaties/verenigingen/verenigingen/%s/locaties/%s", "DELETE"),
            new MockRestMapping(List.of("v1", "organisaties", "verenigingen", "verenigingen", "subtype", "patch"), List.of(), 1, List.of(), "/v1/organisaties/verenigingen/verenigingen/%s/subtype", "PATCH"),
            new MockRestMapping(List.of("v1", "organisaties", "verenigingen", "verenigingen", "vertegenwoordigers", "post"), List.of(), 1, List.of(), "/v1/organisaties/verenigingen/verenigingen/%s/vertegenwoordigers", "POST"),
            new MockRestMapping(List.of("v1", "organisaties", "verenigingen", "verenigingen", "vertegenwoordigers", "patch"), List.of(), 2, List.of(), "/v1/organisaties/verenigingen/verenigingen/%s/vertegenwoordigers/%s", "PATCH"),
            new MockRestMapping(List.of("v1", "organisaties", "verenigingen", "verenigingen", "vertegenwoordigers", "delete"), List.of(), 2, List.of(), "/v1/organisaties/verenigingen/verenigingen/%s/vertegenwoordigers/%s", "DELETE"),

            // SocZek SocialeHuisvestingCIR
            new MockRestMapping(List.of("v1", "socZek", "socialeHuisvesting", "dossiers", "zoeken", "post"), List.of(), 0, List.of("dossierType", "dossierStatus", "dossierNummer"), "/v1/socZek/socialeHuisvesting/dossiers/zoeken", "POST"),
            new MockRestMapping(List.of("v1", "socZek", "socialeHuisvesting", "dossiers", "dossier", "dossierNummer", "get"), List.of(), 1, List.of(), "/v1/socZek/socialeHuisvesting/dossiers/dossier/%s", "GET"),
            new MockRestMapping(List.of("v1", "socZek", "socialeHuisvesting", "dossiers", "dossier", "dossierNummer", "put"), List.of(), 1, List.of(), "/v1/socZek/socialeHuisvesting/dossiers/dossier/%s", "PUT"),
            new MockRestMapping(List.of("v1", "socZek", "socialeHuisvesting", "dossiers", "bevestigDossierVerwerking", "post"), List.of(), 0, List.of("dossierNummer"), "/v1/socZek/socialeHuisvesting/dossiers/bevestigDossierVerwerking", "POST"),
            new MockRestMapping(List.of("v1", "socZek", "socialeHuisvesting", "dossiers", "bijlage", "post"), List.of(), 0, List.of("partijCode", "dossierNummer", "bijlageIdentificatie"), "/v1/socZek/socialeHuisvesting/dossiers/bijlage", "POST"),
            new MockRestMapping(List.of("v1", "socZek", "socialeHuisvesting", "dossiers", "dossierMetBijlages", "dossierNummer", "get"), List.of(), 1, List.of(), "/v1/socZek/socialeHuisvesting/dossiers/dossierMetBijlages/%s", "GET"),
            new MockRestMapping(List.of("v1", "socZek", "socialeHuisvesting", "dossiers", "partijInformatieWijzigingen", "post"), List.of(), 0, List.of("partijCode"), "/v1/socZek/socialeHuisvesting/dossiers/partijInformatieWijzigingen", "POST"),
            new MockRestMapping(List.of("v1", "socZek", "socialeHuisvesting", "dossiers", "verwerkDossierActie", "post"), List.of(), 0, List.of("partijCode", "dossierNummer", "actie"), "/v1/socZek/socialeHuisvesting/dossiers/verwerkDossierActie", "POST"),
            new MockRestMapping(List.of("v1", "socZek", "socialeHuisvesting", "dossiers", "wijzigDossierRangschikkingen", "post"), List.of(), 0, List.of("partijCode"), "/v1/socZek/socialeHuisvesting/dossiers/wijzigDossierRangschikkingen", "POST"),
            new MockRestMapping(List.of("v1", "socZek", "socialeHuisvesting", "dossiers", "zwarteLijstMeldingen", "post"), List.of(), 0, List.of("partijCode"), "/v1/socZek/socialeHuisvesting/dossiers/zwarteLijstMeldingen", "POST"),
            new MockRestMapping(List.of("v1", "socZek", "socialeHuisvesting", "dossiers", "zwarteLijstMeldingVoor", "post"), List.of(), 0, List.of("rijksregisternummer"), "/v1/socZek/socialeHuisvesting/dossiers/zwarteLijstMeldingVoor", "POST"),
            new MockRestMapping(List.of("v1", "socZek", "socialeHuisvesting", "gemeentes", "get"), List.of(), 0, List.of(), "/v1/socZek/socialeHuisvesting/gemeentes", "GET", true),
            new MockRestMapping(List.of("v1", "socZek", "socialeHuisvesting", "gemeentes", "wijkenVoorGemeente", "gemeenteNisCode", "get"), List.of(), 1, List.of(), "/v1/socZek/socialeHuisvesting/gemeentes/wijkenVoorGemeente/%s", "GET"),
            new MockRestMapping(List.of("v1", "socZek", "socialeHuisvesting", "gemeentes", "wijkenVoorGemeente", "gemeenteNisCode", "put"), List.of(), 1, List.of(), "/v1/socZek/socialeHuisvesting/gemeentes/wijkenVoorGemeente/%s", "PUT"),

            // SocZek SocialeHuisvestingCWR
            new MockRestMapping(List.of("v1", "socZek", "socialeHuisvesting", "woningen", "vmswWoningId", "get"), List.of(), 1, List.of(), "/v1/socZek/socialeHuisvesting/woningen/%s", "GET"),
            new MockRestMapping(List.of("v1", "socZek", "socialeHuisvesting", "woningen", "vmswWoningId", "patch"), List.of(), 1, List.of(), "/v1/socZek/socialeHuisvesting/woningen/%s", "PATCH"),
            new MockRestMapping(List.of("v1", "socZek", "socialeHuisvesting", "woningen", "get"), List.of("gemeenteNISCode", "woonmaatschappijWoningId", "gebouweenheidId"), 0, List.of(), "/v1/socZek/socialeHuisvesting/woningen", "GET"),
            new MockRestMapping(List.of("v1", "socZek", "socialeHuisvesting", "woningen", "post"), List.of(), 0, List.of("gebouweenheidId"), "/v1/socZek/socialeHuisvesting/woningen", "POST"),
            new MockRestMapping(List.of("v1", "socZek", "socialeHuisvesting", "woningen", "vmswWoningId", "woningkenmerkscores", "get"), List.of("woningKenmerkCategorie"), 1, List.of(), "/v1/socZek/socialeHuisvesting/woningen/%s/woningkenmerkscores", "GET"),
            new MockRestMapping(List.of("v1", "socZek", "socialeHuisvesting", "woningen", "vmswWoningId", "woningkenmerkscores", "patch"), List.of(), 1, List.of(), "/v1/socZek/socialeHuisvesting/woningen/%s/woningkenmerkscores", "PATCH"),
            new MockRestMapping(List.of("v1", "socZek", "socialeHuisvesting", "woningen", "vmswWoningId", "markthuurwaarden", "get"), List.of(), 1, List.of(), "/v1/socZek/socialeHuisvesting/woningen/%s/markthuurwaarden", "GET"),
            new MockRestMapping(List.of("v1", "socZek", "socialeHuisvesting", "woningen", "markthuurwaarden", "simulatie", "post"), List.of(), 0, List.of(), "/v1/socZek/socialeHuisvesting/woningen/markthuurwaarden/simulatie", "POST", true),
            new MockRestMapping(List.of("v1", "socZek", "socialeHuisvesting", "woningen", "vmswWoningId", "conditiescore", "get"), List.of(), 1, List.of(), "/v1/socZek/socialeHuisvesting/woningen/%s/conditiescore", "GET"),
            new MockRestMapping(List.of("v1", "socZek", "socialeHuisvesting", "woningen", "conditiescore", "simulatie", "post"), List.of(), 0, List.of(), "/v1/socZek/socialeHuisvesting/woningen/conditiescore/simulatie", "POST", true),
            new MockRestMapping(List.of("v1", "socZek", "socialeHuisvesting", "woningen", "vmswWoningId", "energiecorrectie", "get"), List.of(), 1, List.of(), "/v1/socZek/socialeHuisvesting/woningen/%s/energiecorrectie", "GET"),
            new MockRestMapping(List.of("v1", "socZek", "socialeHuisvesting", "woningen", "vmswWoningId", "epcwaarden", "get"), List.of(), 1, List.of(), "/v1/socZek/socialeHuisvesting/woningen/%s/epcwaarden", "GET"),
            new MockRestMapping(List.of("v1", "socZek", "socialeHuisvesting", "woningen", "vmswWoningId", "epcwaarden", "brondata", "documentId", "get"), List.of(), 2, List.of(), "/v1/socZek/socialeHuisvesting/woningen/%s/epcwaarden/brondata/%s", "GET"),
            new MockRestMapping(List.of("v1", "socZek", "socialeHuisvesting", "woningen", "vmswWoningId", "epcwaarden", "document", "documentId", "get"), List.of(), 2, List.of(), "/v1/socZek/socialeHuisvesting/woningen/%s/epcwaarden/document/%s", "GET"),
            new MockRestMapping(List.of("v1", "socZek", "socialeHuisvesting", "woningen", "vmswWoningId", "epcwaarden", "certificaat", "post"), List.of(), 1, List.of(), "/v1/socZek/socialeHuisvesting/woningen/%s/epcwaarden/certificaat", "POST"),
            new MockRestMapping(List.of("v1", "socZek", "socialeHuisvesting", "woningen", "vmswWoningId", "epcwaarden", "markeerReferentieversie", "patch"), List.of(), 1, List.of("epcVersieId"), "/v1/socZek/socialeHuisvesting/woningen/%s/epcwaarden/markeerReferentieversie", "PATCH"),
            new MockRestMapping(List.of("v1", "socZek", "socialeHuisvesting", "woningen", "vmswWoningId", "epcwaarden", "epcVersieId", "delete"), List.of(), 2, List.of(), "/v1/socZek/socialeHuisvesting/woningen/%s/epcwaarden/%s", "DELETE")

    );

    // url parameters should be marked with %s, as such they can be filled in with String.format
    // priority: when set, overrides the default calculated priority (lower number = higher precedence in WireMock)
    public MockRestMapping(List<String> folderPath, List<String> queryParameters, Integer urlParametersSize, List<String> requestBodyParameters, String url, String method) {
        this(folderPath, queryParameters, urlParametersSize, requestBodyParameters, url, method, null, false);
    }

    public MockRestMapping(List<String> folderPath, List<String> queryParameters, Integer urlParametersSize, List<String> requestBodyParameters, String url, String method, Integer priority) {
        this(folderPath, queryParameters, urlParametersSize, requestBodyParameters, url, method, priority, false);
    }

    public MockRestMapping(List<String> folderPath, List<String> queryParameters, Integer urlParametersSize, List<String> requestBodyParameters, String url, String method, boolean defaultOnly) {
        this(folderPath, queryParameters, urlParametersSize, requestBodyParameters, url, method, null, defaultOnly);
    }
}
