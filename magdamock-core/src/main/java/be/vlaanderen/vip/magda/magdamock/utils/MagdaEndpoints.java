package be.vlaanderen.vip.magda.magdamock.utils;

import java.net.URI;

/**
 * An interface that relates service identifiers (service name/version pairs) to the URI of an endpoint that handles requests to the according service.
 *
 * @see MagdaMockDocument.MagdaServiceIdentification
 */
public interface MagdaEndpoints {
    URI magdaUri(MagdaMockDocument.MagdaServiceIdentification serviceId);

    static Builder builder() {
        return new Builder();
    }

    class Builder {

        private final ServiceMappedMagdaEndpoints serviceMappedMagdaEndpoints;

        public Builder() {
            this.serviceMappedMagdaEndpoints = new ServiceMappedMagdaEndpoints();
        }

        public Builder addMapping(String dienstNaam, String dienstVersion, MagdaEndpoint magdaEndpoint) {
            serviceMappedMagdaEndpoints.addMapping(dienstNaam, dienstVersion, magdaEndpoint);
            return this;
        }

        public MagdaEndpoints build() {
            return serviceMappedMagdaEndpoints;
        }
    }
}