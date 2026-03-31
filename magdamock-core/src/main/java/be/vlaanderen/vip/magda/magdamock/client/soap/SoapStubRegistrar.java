package be.vlaanderen.vip.magda.magdamock.client.soap;

import com.github.tomakehurst.wiremock.WireMockServer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SoapStubRegistrar {

    private final SoapStubHandler inszSoapStubHandler;
    private final SoapStubHandler notFoundStubHandler;
    private final SoapStubHandler ondernemingStubHandler;
    private final Map<String, SoapStubHandler> soapStubHandlerMap = new HashMap<>();

    public SoapStubRegistrar(WireMockServer wireMockServer, String soapTestPath) {
        this.inszSoapStubHandler = new InszSoapStubHandler(wireMockServer, soapTestPath);
        this.notFoundStubHandler = new NotFoundStubHandler(wireMockServer, soapTestPath);
        this.ondernemingStubHandler = new OndernemingsStubHandler(wireMockServer, soapTestPath);
        soapStubHandlerMap.put("GeefPasfoto", new GeefPasfotoStubHandler(wireMockServer, soapTestPath));
    }

    public void registerDomain(Domain domain) {
        domain.services().forEach(service ->
                service.versions().forEach(version ->
                        version.files().forEach(file -> {
                            try {
                                SoapStubHandler soapStubHandler = determeSoapStubHandler(service.name(), file);
                                if (soapStubHandler != null) {
                                    soapStubHandler.register(
                                            domain.name(),
                                            service.name(),
                                            version.name(),
                                            file
                                    );
                                }
                            } catch (IOException e) {
                                throw new IllegalStateException("SOAP file can not be registered", e);
                            }
                        })
                )
        );
    }

    private SoapStubHandler determeSoapStubHandler(String service, String file) {
        String fileName = file.replace(".xml", "");
        if (isInsz(fileName)) {
            return inszSoapStubHandler;
        } else if (isOndernemingsNummer(fileName)) {
            return ondernemingStubHandler;
        } else if ("notfound".equals(fileName)) {
            return notFoundStubHandler;
        }
        return soapStubHandlerMap.get(service);
    }

    private boolean isInsz(String input) {
        return input != null && input.matches("\\d{11}");
    }

    private boolean isOndernemingsNummer(String input) {
        return input != null && input.matches("\\d{10}");
    }

}
