package be.vlaanderen.vip.magda.magdamock.client.soap;

import be.vlaanderen.vip.magda.magdamock.config.MockSoapMapping;
import be.vlaanderen.vip.magda.magdamock.utils.MagdaMockDocument;
import com.github.tomakehurst.wiremock.WireMockServer;

import java.io.IOException;
import java.util.Map;

public class SoapStubRegistrar {

    public static final String VERSION_01_00 = "01.00.0000";
    public static final String VERSION_02_00 = "02.00.0000";
    public static final String VERSION_02_01 = "02.01.0000";
    public static final String VERSION_02_02 = "02.02.0000";
    public static final String VERSION_03_00 = "03.00.0000";

    public static final String KEY_GEBOUWID = "//GebouwId";

    private final Map<MagdaMockDocument.MagdaServiceIdentification, SoapStubHandler> soapStubHandlerMap;

    public SoapStubRegistrar(Map<MagdaMockDocument.MagdaServiceIdentification, SoapStubHandler> soapStubHandlerMap) {
        this.soapStubHandlerMap = soapStubHandlerMap;
    }

    public SoapStubRegistrar(WireMockServer wireMockServer, String soapTestPath) {
        this.soapStubHandlerMap = MockSoapMapping.createHandlersMap(wireMockServer, soapTestPath);
    }

    // TODO: refactor?
    public void registerDomain(Domain domain) {
        domain.services().forEach(service ->
                service.versions().forEach(version ->
                        version.files().forEach(file -> registerFile(domain, service, version, file))
                )
        );
    }

    private void registerFile(Domain domain, Service service, Version version, String file) {
        try {
            SoapStubHandler soapStubHandler = determineSoapStubHandler(service.name(), version.name());
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
    }

    private SoapStubHandler determineSoapStubHandler(String service, String version) {
        return soapStubHandlerMap.get(new MagdaMockDocument.MagdaServiceIdentification(service, version));
    }
}
