package be.vlaanderen.vip.magda.magdamock.config;

import be.vlaanderen.vip.magda.magdamock.client.soap.GenderFileTransformer;
import be.vlaanderen.vip.magda.magdamock.utils.MockDataTemplateHelper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.direct.DirectCallHttpServerFactory;

import java.nio.file.Path;

public class EmbeddedWireMockBuilder {

    public static WireMockData wireMockServer(String fileSource, String soapTestPath) {
        return wireMockServer(0, fileSource, soapTestPath);
    }

    public static WireMockData wireMockServer(Integer wiremockPort, String fileSource, String soapTestPath) {
        DirectCallHttpServerFactory factory = new DirectCallHttpServerFactory();
        WireMockConfiguration config = WireMockConfiguration.wireMockConfig()
                .port(wiremockPort)
                .httpServerFactory(factory)
                .globalTemplating(true)
                .extensions(MockDataTemplateHelper.getTemplateHelperExtensions(), new GenderFileTransformer(Path.of(soapTestPath)))
                .usingFilesUnderDirectory(fileSource);

        return new WireMockData(new WireMockServer(config), factory);
    }
}