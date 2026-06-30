package be.vlaanderen.vip.magda.magdamock.client.soap;

import be.vlaanderen.vip.magda.magdamock.utils.SoapResourceUtil;
import com.github.tomakehurst.wiremock.WireMockServer;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.matchingXPath;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

@Slf4j

public class GeefPasfotoStubHandler extends AbstractSoapStubHandler {

    public GeefPasfotoStubHandler(WireMockServer wireMockServer, String soapTestPath) {
        super(wireMockServer, soapTestPath);
    }

    @Override
    public void register(String domain, String service, String version, String fileName) throws IOException {
        wireMockServer.stubFor(
                post(urlEqualTo("/soap"))
                        .atPriority(15)
                        .withRequestBody(matchingXPath(
                                "//*[local-name()='Naam' and normalize-space()='" + service + "']"
                        ))
                        .withRequestBody(matchingXPath(
                                "//*[local-name()='Versie' and normalize-space()='" + version + "']"
                        ))
                        .withRequestBody(matchingXPath(
                                "//*[local-name()='INSZ' and normalize-space()]"
                        ))
                        .willReturn(
                                aResponse()
                                        .withStatus(200)
                                        .withTransformers(GenderFileTransformer.NAME)
                                        .withTransformerParameter("domain", domain)
                                        .withTransformerParameter("service", service)
                                        .withTransformerParameter("version", version)
                        )
        );

        wireMockServer.stubFor(
                post(urlEqualTo("/soap"))
                        .atPriority(20)
                        .withRequestBody(matchingXPath(
                                "//*[local-name()='Naam' and normalize-space()='" + service + "']"
                        ))
                        .withRequestBody(matchingXPath(
                                "//*[local-name()='Versie' and normalize-space()='" + version + "']"
                        ))
                        .willReturn(
                                aResponse()
                                        .withStatus(200)
                                        .withHeader("Content-Type", "text/xml; charset=utf-8")
                                        .withBody(
                                                SoapResourceUtil.readStubBody(
                                                        soapTestPath,
                                                        domain,
                                                        service,
                                                        version,
                                                        "default"
                                                )
                                        )
                        )
        );
    }
}
