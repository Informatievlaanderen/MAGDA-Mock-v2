package be.vlaanderen.vip.magda.magdamock.client.rest;

import java.util.List;

public record MockRestMapping(List<String> folderPath, List<String> queryParameters, Integer urlParametersSize, List<String> requestBodyParameters, String url, String method) {
    // url parameters should be marked with %s, as such they can be filled in with String.format
    public static final List<MockRestMapping> MAPPINGS = List.of(
            // Mobility
            new MockRestMapping(List.of("v1", "mobility", "registrations", "get", "vin"), List.of("vin"), 0, List.of(), "/v1/mobility/registrations", "GET"),
            new MockRestMapping(List.of("v1", "mobility", "registrations", "get", "plateNr"), List.of("plateNr"), 0, List.of(), "/v1/mobility/registrations", "GET"),

            // SocZek
            new MockRestMapping(List.of("v1", "socZek", "socialeHuisvesting", "dossiersZoeken", "post", "dossierStatus"), List.of(), 0, List.of("dossierStatus"), "/v1/socZek/socialeHuisvesting/dossiers/zoeken", "POST")
    );
}
