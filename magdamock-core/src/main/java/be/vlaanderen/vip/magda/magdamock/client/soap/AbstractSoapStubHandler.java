package be.vlaanderen.vip.magda.magdamock.client.soap;

import com.github.tomakehurst.wiremock.WireMockServer;

public abstract class AbstractSoapStubHandler implements SoapStubHandler {

    protected final WireMockServer wireMockServer;
    protected final String soapTestPath;

    protected AbstractSoapStubHandler(WireMockServer wireMockServer,
                                      String soapTestPath) {

        this.wireMockServer = wireMockServer;
        this.soapTestPath = soapTestPath;
    }

}
