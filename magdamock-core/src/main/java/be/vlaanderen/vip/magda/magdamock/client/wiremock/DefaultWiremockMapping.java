package be.vlaanderen.vip.magda.magdamock.client.wiremock;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;

public class DefaultWiremockMapping {
    public static void addDefaultFallbackWiremockMapping(WireMockServer wireMockServer) {
        StubMapping stubMapping = StubMapping.buildFrom("""
                {
                    "priority": 100,
                    "request": {
                        "method": "ANY",
                        "urlPattern": ".*"
                    },
                    "response": {
                        "status": 666,
                        "jsonBody": { "status": "Error", "message": "No mapping defined for this endpoint" },
                        "headers": {
                            "Content-Type": "application/json"
                        }
                    }
                }
                """);
        wireMockServer.addStubMapping(stubMapping);
    }
}
