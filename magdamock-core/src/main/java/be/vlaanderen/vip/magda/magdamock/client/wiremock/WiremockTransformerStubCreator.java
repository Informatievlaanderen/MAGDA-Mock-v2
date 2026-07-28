package be.vlaanderen.vip.magda.magdamock.client.wiremock;

import be.vlaanderen.vip.magda.magdamock.client.transformers.MagdaRestFileResponseTransformer;
import be.vlaanderen.vip.magda.magdamock.client.transformers.MagdaSoapFileResponseTransformer;
import be.vlaanderen.vip.magda.magdamock.config.MockRestMapping;
import be.vlaanderen.vip.magda.magdamock.config.MockSoapMapping;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.matching.UrlPattern;
import lombok.extern.slf4j.Slf4j;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.matchingXPath;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.request;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

@Slf4j
public class WiremockTransformerStubCreator {
    public static void addRestTransformerStub(WireMockServer wireMockServer, MockRestMapping mockRestMapping) {
        log.info("Adding generic rest stub for {} {} at priority {}", mockRestMapping.getId(), mockRestMapping.toPath(), mockRestMapping.priority());
        wireMockServer.stubFor(
                request(mockRestMapping.method(), UrlPattern.fromOneOf(null, null, null, null, mockRestMapping.url()))
                        .atPriority(mockRestMapping.priority())
                        .willReturn(
                                aResponse()
                                        .withTransformers(MagdaRestFileResponseTransformer.NAME)
                                        .withTransformerParameter("mapping-id", mockRestMapping.getId())
                        )
        );
        // TODO: add option for defaultOnly mock mappings
    }

    public static void addSoapSubdirTransformerStub(WireMockServer wireMockServer, MockSoapMapping mockSoapMapping) {
        String domain = mockSoapMapping.domain();
        String service = mockSoapMapping.service();
        String version = mockSoapMapping.version();
        log.info("Adding generic sub dir stub for {} {} {}", domain, service, version);
        wireMockServer.stubFor(
                post(urlEqualTo("/soap"))
                        .withRequestBody(matchingXPath(
                                "//*[local-name()='Naam' and normalize-space()='" + service + "']"
                        ))
                        .withRequestBody(matchingXPath(
                                "//*[local-name()='Versie' and normalize-space()='" + version + "']"
                        ))
                        .willReturn(
                                aResponse()
                                        .withStatus(200)
                                        .withTransformers(MagdaSoapFileResponseTransformer.NAME)
                                        .withTransformerParameter("domain", domain)
                                        .withTransformerParameter("service", service)
                                        .withTransformerParameter("version", version)
                        )
        );
    }
    public static void addSoapFileTransformerStub(WireMockServer wireMockServer, MockSoapMapping mockSoapMapping) {
        String domain = mockSoapMapping.domain();
        String service = mockSoapMapping.service();
        String version = mockSoapMapping.version();
        log.info("Adding generic file stub for {} {} {}", domain, service, version);
        wireMockServer.stubFor(
                post(urlEqualTo("/soap"))
                        .withRequestBody(matchingXPath(
                                "//*[local-name()='Naam' and normalize-space()='" + service + "']"
                        ))
                        .withRequestBody(matchingXPath(
                                "//*[local-name()='Versie' and normalize-space()='" + version + "']"
                        ))
                        .willReturn(
                                aResponse()
                                        .withStatus(200)
                                        .withTransformers(MagdaSoapFileResponseTransformer.NAME)
                                        .withTransformerParameter("domain", domain)
                                        .withTransformerParameter("service", service)
                                        .withTransformerParameter("version", version)
                        )
        );
    }
}
