package be.vlaanderen.vip.magda.magdamock.client.rest;

import java.util.List;

public record MockRestMapping(List<String> folderPath, List<String> queryParameters, Integer urlParametersSize, String url, String method) {
    // url parameters should be marked with %s, as such they can be filled in with String.format
    public static final List<MockRestMapping> MAPPINGS = List.of(
            new MockRestMapping(List.of("v1", "mobility", "registrations", "get", "vin"), List.of("vin"), 0, "/v1/mobility/registrations", "GET"),
            new MockRestMapping(List.of("v1", "mobility", "registrations", "get", "plateNr"), List.of("plateNr"), 0, "/v1/mobility/registrations", "GET")
    );
}
