package be.vlaanderen.vip.magda.magdamock.client.rest;

import java.util.List;

public record MockRestMapping(List<String> folderPath, List<String> queryParameters, Integer urlParametersSize, List<String> requestBodyParameters, String url, String method, Integer priority, boolean defaultOnly) {
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

    public static final List<MockRestMapping> MAPPINGS = List.of(
            // Mobility
            new MockRestMapping(List.of("v1", "mobility", "registrations", "get", "plateNr"), List.of("plateNr"), 0, List.of(), "/v1/mobility/registrations", "GET", 40),
            new MockRestMapping(List.of("v1", "mobility", "registrations", "get", "plateUID"), List.of("plateUID"), 0, List.of(), "/v1/mobility/registrations", "GET", 41),
            new MockRestMapping(List.of("v1", "mobility", "registrations", "get", "vin"), List.of("vin", "unifier"), 0, List.of(), "/v1/mobility/registrations", "GET", 42),
            new MockRestMapping(List.of("v1", "mobility", "registrations", "get", "nationalNr"), List.of("nationalNr"), 0, List.of(), "/v1/mobility/registrations", "GET", 43),
            new MockRestMapping(List.of("v1", "mobility", "registrations", "get", "companyNr"), List.of("companyNr"), 0, List.of(), "/v1/mobility/registrations", "GET", 44),
            new MockRestMapping(List.of("v1", "mobility", "registrations", "get"), List.of(), 0, List.of(), "/v1/mobility/registrations", "GET", 30, true),

            // SocZek
            new MockRestMapping(List.of("v1", "socZek", "socialeHuisvesting", "dossiers", "dossier", "get"), List.of(), 1, List.of(), "/v1/socZek/socialeHuisvesting/dossiers/dossier/%s", "GET"),

            new MockRestMapping(List.of("v1", "socZek", "socialeHuisvesting", "dossiers", "zoeken", "post"), List.of(), 0, List.of("dossierType", "dossierStatus", "dossierNummer"), "/v1/socZek/socialeHuisvesting/dossiers/zoeken", "POST"),

            new MockRestMapping(List.of("v1", "socZek", "socialeHuisvesting", "dossiers", "dossier", "put"), List.of(), 1, List.of(), "/v1/socZek/socialeHuisvesting/dossiers/dossier/%s", "PUT"),

            new MockRestMapping(List.of("v1", "socZek", "socialeHuisvesting", "dossiers", "bijlage", "post"), List.of(), 0, List.of("partijCode", "dossierNummer", "bijlageIdentificatie"), "/v1/socZek/socialeHuisvesting/dossiers/bijlage", "POST"),

            new MockRestMapping(List.of("v1", "socZek", "socialeHuisvesting", "dossiers", "bevestigDossierVerwerking", "post"), List.of(), 0, List.of("partijCode", "dossierNummer", "versieNummer"), "/v1/socZek/socialeHuisvesting/dossiers/bevestigDossierVerwerking", "POST"),

            new MockRestMapping(List.of("v1", "socZek", "socialeHuisvesting", "dossiers", "wijzigDossierRangschikkingen", "post"), List.of(), 0, List.of("partijCode"), "/v1/socZek/socialeHuisvesting/dossiers/wijzigDossierRangschikkingen", "POST"),

            new MockRestMapping(List.of("v1", "socZek", "socialeHuisvesting", "dossiers", "partijInformatieWijzigingen", "post"), List.of(), 0, List.of("partijCode"), "/v1/socZek/socialeHuisvesting/dossiers/partijInformatieWijzigingen", "POST"),

            new MockRestMapping(List.of("v1", "socZek", "socialeHuisvesting", "dossiers", "verwerkDossierActie", "post"), List.of(), 0, List.of("partijCode", "dossierNummer", "actie"), "/v1/socZek/socialeHuisvesting/dossiers/verwerkDossierActie", "POST"),

            new MockRestMapping(List.of("v1", "socZek", "socialeHuisvesting", "dossiers", "zwarteLijstMeldingen", "post"), List.of(), 0, List.of("partijCode"), "/v1/socZek/socialeHuisvesting/dossiers/zwarteLijstMeldingen", "POST"),

            new MockRestMapping(List.of("v1", "socZek", "socialeHuisvesting", "dossiers", "dossierMetBijlages", "get"), List.of(), 1, List.of(), "/v1/socZek/socialeHuisvesting/dossiers/dossierMetBijlages/%s", "GET"),

            new MockRestMapping(List.of("v1", "socZek", "socialeHuisvesting", "dossiers", "zwarteLijstMeldingVoor", "post"), List.of(), 0, List.of("rijksregisternummer"), "/v1/socZek/socialeHuisvesting/dossiers/zwarteLijstMeldingVoor", "POST"),

            new MockRestMapping(List.of("v1", "socZek", "socialeHuisvesting", "gemeentes", "get"), List.of(), 0, List.of(), "/v1/socZek/socialeHuisvesting/gemeentes", "GET"),

            new MockRestMapping(List.of("v1", "socZek", "socialeHuisvesting", "gemeentes", "wijkenVoorGemeente", "get"), List.of(), 1, List.of(), "/v1/socZek/socialeHuisvesting/gemeentes/wijkenVoorGemeente/%s", "GET"),

            new MockRestMapping(List.of("v1", "socZek", "socialeHuisvesting", "gemeentes", "wijkenVoorGemeente", "put"), List.of(), 1, List.of(), "/v1/socZek/socialeHuisvesting/gemeentes/wijkenVoorGemeente/%s", "PUT")





    );
}
