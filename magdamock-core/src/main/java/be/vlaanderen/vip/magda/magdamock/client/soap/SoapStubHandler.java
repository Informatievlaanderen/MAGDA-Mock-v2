package be.vlaanderen.vip.magda.magdamock.client.soap;

import java.io.IOException;

public interface SoapStubHandler {

    void register(
            String domain,
            String service,
            String version,
            String fileName
    ) throws IOException;
}
