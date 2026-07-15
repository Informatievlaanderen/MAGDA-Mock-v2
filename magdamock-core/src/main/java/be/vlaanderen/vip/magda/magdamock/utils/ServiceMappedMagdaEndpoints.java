package be.vlaanderen.vip.magda.magdamock.utils;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * An implementation of MagdaEndpoints that maps service identifiers (service name/version pairs) to MagdaEndpoints.
 * This is the default implementation that the MagdaEndpoints builder will use.
 *
 * @see MagdaServiceIdentification
 * @see MagdaEndpoints
 */
public class ServiceMappedMagdaEndpoints implements MagdaEndpoints {

    private final Map<MagdaMockDocument.MagdaServiceIdentification, MagdaEndpoint> endpoints = new HashMap<>();

    public URI magdaUri(MagdaMockDocument.MagdaServiceIdentification serviceId) {
        return determineMagdaPath(serviceId).getUri();
    }

    private MagdaEndpoint determineMagdaPath(MagdaMockDocument.MagdaServiceIdentification dienst) {
        final var magdaEndpoint = endpoints.get(dienst);
        if(magdaEndpoint == null) {
            throw new IllegalArgumentException("No MagdaEndpoint configured for service '%s'. Add them in MagdaEndpoints.".formatted(dienst));
        }
        return magdaEndpoint;
    }

    public void addMapping(String dienstNaam, String dienstVersie, MagdaEndpoint magdaEndpoint) {
        endpoints.put(new MagdaMockDocument.MagdaServiceIdentification(dienstNaam, dienstVersie), magdaEndpoint);
    }
}
