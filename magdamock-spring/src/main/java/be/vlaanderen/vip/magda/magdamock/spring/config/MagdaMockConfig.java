package be.vlaanderen.vip.magda.magdamock.spring.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import be.vlaanderen.vip.magda.magdamock.client.MagdaMockConnection;

import java.io.IOException;
import java.util.Objects;

@Data
@Configuration
@ConfigurationProperties("magda.magdamock")
public class MagdaMockConfig {
    String soapTestPath;
    String restTestPath;
    String magdaXsdPath;
    Boolean magdaXsdRequestEnabled;
    Boolean magdaXsdResponseEnabled;
    String magdaOpenapiPath;
    Boolean magdaOpenapiRequestEnabled;
    Boolean magdaOpenapiResponseEnabled;
    Integer minimumTimeoutMillis;
    Integer maximumTimeoutMillis;
    Boolean enableTimeout;
    boolean logRequestBody;

    @Bean
    public MagdaMockConnection magdaMockConnection() throws IOException {
        if (Objects.requireNonNullElse(enableTimeout, false)) {
            if (minimumTimeoutMillis == null) {
                minimumTimeoutMillis = 0;
            }
            if (maximumTimeoutMillis == null) {
                maximumTimeoutMillis = Math.max(minimumTimeoutMillis, 5000);
            }
        } else {
            minimumTimeoutMillis = null;
            maximumTimeoutMillis = null;
        }
        return MagdaMockConnection.create(restTestPath, soapTestPath, Objects.requireNonNullElse(magdaXsdRequestEnabled, true), Objects.requireNonNullElse(magdaXsdResponseEnabled, true), magdaXsdPath, Objects.requireNonNullElse(magdaOpenapiRequestEnabled, true), Objects.requireNonNullElse(magdaOpenapiResponseEnabled, true), magdaOpenapiPath, minimumTimeoutMillis, maximumTimeoutMillis, logRequestBody);
    }
}
