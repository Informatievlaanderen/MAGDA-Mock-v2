package be.vlaanderen.vip.magda.magdamock.client.soap;

import be.vlaanderen.vip.magda.magdamock.utils.SoapResourceUtil;
import com.github.tomakehurst.wiremock.WireMockServer;

import java.io.IOException;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.matchingXPath;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

public class NotFoundStubHandler extends AbstractSoapStubHandler {

    public NotFoundStubHandler(WireMockServer wireMockServer, String soapTestPath) {
        super(wireMockServer, soapTestPath);
    }

    @Override
    public void register(String domain, String service, String version, String fileName) throws IOException {
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
                                        .withBody(SoapResourceUtil.readStubBody(soapTestPath, domain, service, version, "notfound"))
                        )
        );
    }
}
