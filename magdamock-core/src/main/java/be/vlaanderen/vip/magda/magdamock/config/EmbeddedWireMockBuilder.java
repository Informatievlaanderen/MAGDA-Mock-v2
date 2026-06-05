package be.vlaanderen.vip.magda.magdamock.config;

import be.vlaanderen.vip.magda.magdamock.client.soap.GenderFileTransformer;
import be.vlaanderen.vip.magda.magdamock.utils.MockDataTemplateHelper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.direct.DirectCallHttpServerFactory;

import java.nio.file.Path;

public class EmbeddedWireMockBuilder {

    private Integer wiremockPort;
    private String soapTestPath;
    private String fileSource;

    public EmbeddedWireMockBuilder() {}

    public EmbeddedWireMockBuilder wiremockPort(Integer wiremockPort) {
        this.wiremockPort = wiremockPort;
        return this;
    }

    public EmbeddedWireMockBuilder soapTestPath(String soapTestPath) {
        this.soapTestPath = soapTestPath;
        return this;
    }

    public EmbeddedWireMockBuilder fileSource(String fileSource) {
        this.fileSource = fileSource;
        return this;
    }

    public WireMockData build() {
        if (wiremockPort == null) {
            wiremockPort = 0;
        }
        DirectCallHttpServerFactory factory = new DirectCallHttpServerFactory();
        WireMockConfiguration config = WireMockConfiguration.wireMockConfig()
                .port(wiremockPort)
                .httpServerFactory(factory)
                .globalTemplating(true)
                .extensions(MockDataTemplateHelper.getTemplateHelperExtensions(), new GenderFileTransformer(Path.of(soapTestPath)));
        if (fileSource != null) {
            config = config
                    .usingFilesUnderDirectory(fileSource);
        }

        return new WireMockData(new WireMockServer(config), factory);
    }
}