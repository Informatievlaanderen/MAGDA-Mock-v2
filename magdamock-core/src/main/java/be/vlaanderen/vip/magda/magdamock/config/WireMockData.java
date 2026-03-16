package be.vlaanderen.vip.magda.magdamock.config;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.direct.DirectCallHttpServerFactory;

public record WireMockData(WireMockServer wireMockServer, DirectCallHttpServerFactory factory) {
}
