package be.vlaanderen.vip.magda.magdamock.config;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.direct.DirectCallHttpServerFactory;

public class EmbeddedWireMockBuilder {

    public static WireMockData wireMockServer(String fileSource) {
        return wireMockServer(0, fileSource);
    }

    public static WireMockData wireMockServer(Integer wiremockPort, String fileSource) {
        DirectCallHttpServerFactory factory = new DirectCallHttpServerFactory();

        WireMockConfiguration config = WireMockConfiguration.wireMockConfig()
                .port(wiremockPort)
                .httpServerFactory(factory)
                .usingFilesUnderDirectory(fileSource);

        return new WireMockData(new WireMockServer(config), factory);
    }
}