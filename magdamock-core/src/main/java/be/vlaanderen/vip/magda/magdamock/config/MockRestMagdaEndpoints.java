package be.vlaanderen.vip.magda.magdamock.config;

import be.vlaanderen.vip.magda.magdamock.utils.MagdaEndpoints;
import be.vlaanderen.vip.magda.magdamock.utils.MagdaMockDocument;
import lombok.SneakyThrows;
import org.apache.hc.core5.net.URIBuilder;

import java.net.URI;

public record MockRestMagdaEndpoints(URI uri) implements MagdaEndpoints {
    @SneakyThrows
    @Override
    public URI magdaUri(MagdaMockDocument.MagdaServiceIdentification serviceId) {
        if (serviceId.equals(new MagdaMockDocument.MagdaServiceIdentification("REST /v1/mobility/registrations", "00.01"))) {
            return new URIBuilder(uri).appendPath("/v1/mobility/registrations").build();
        } else if (serviceId.equals(new MagdaMockDocument.MagdaServiceIdentification("REST /v1/socZek/handicap/volledigeDossiers", "00.01"))) {
            return new URIBuilder(uri).appendPath("/v1/socZek/handicap/volledigeDossiers").build();
        }
        return uri;
    }
}