package be.vlaanderen.vip.magda.magdamock.client.soap;

import com.github.tomakehurst.wiremock.WireMockServer;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j

public class GeefPasfotoStubHandler extends AbstractSoapStubHandler {

    public GeefPasfotoStubHandler(WireMockServer wireMockServer, String soapTestPath) {
        super(wireMockServer, soapTestPath);
    }

    @Override
    public void register(String domain, String service, String version, String fileName) throws IOException {
      log.warn("Not yet imeplemented");
    }


}
