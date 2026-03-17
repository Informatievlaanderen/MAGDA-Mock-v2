package be.vlaanderen.vip.magda.magdamock.client;

import be.vlaanderen.vip.magda.magdamock.config.WireMockData;
import be.vlaanderen.vip.magda.magdamock.utils.MockDataTemplateHelper;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.direct.DirectCallHttpServerFactory;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MagdaMockConnectionTest {

    @Test
    @SneakyThrows
    void whenTemplateReplacesOk_shouldReturnStatus200AndExpectedOutput() {
        MagdaMockConnection connection = MagdaMockConnection.create(createWireMockForTest());
        var response = connection.sendRestRequest("/template/ok", "", "GET", "", "Tue, 29 Oct 2024 16:56:32 GMT");
        assertEquals(200, response.getRight());
        assertEquals("\"2019-10-19\"", response.getLeft().get("test").toString());
    }


    @Test
    @SneakyThrows
    void whenTemplateReplacesNok_shouldReturnStatus500() {
        MagdaMockConnection connection = MagdaMockConnection.create(createWireMockForTest());
        var response = connection.sendRestRequest("/template/nok", "", "GET", "", "Tue, 29 Oct 2024 16:56:32 GMT");
        assertEquals(
                "{\"test\":\"{formatDate (dateMath (dateMath (parseDate request.headers.Date) '-10d') '-5y')}}\"}",
                response.getLeft().toString()
        );
    }


    private WireMockData createWireMockForTest() {
        DirectCallHttpServerFactory factory = new DirectCallHttpServerFactory();

        WireMockConfiguration config = WireMockConfiguration.wireMockConfig()
                .port(0)
                .httpServerFactory(factory)
                .globalTemplating(true)
                .templatingEnabled(true)
                .extensions(MockDataTemplateHelper.getTemplateHelperExtensions());

        WireMockServer wireMockServer = new WireMockServer(config);
        wireMockServer.stubFor(
                get(urlEqualTo("/template/ok"))
                        .willReturn(aResponse()
                                .withBody("""
                                        {"test": "{{formatDate (dateMath (dateMath (parseDate request.headers.Date) '-10d') '-5y')}}"}
                                        """
                                )
                        ));
        wireMockServer.stubFor(
                get(urlEqualTo("/template/nok"))
                        .willReturn(aResponse()
                                .withBody("""
                                        {"test": "{formatDate (dateMath (dateMath (parseDate request.headers.Date) '-10d') '-5y')}}"}
                                        """
                                )
                        ));

        return new WireMockData(wireMockServer, factory);
    }
}
