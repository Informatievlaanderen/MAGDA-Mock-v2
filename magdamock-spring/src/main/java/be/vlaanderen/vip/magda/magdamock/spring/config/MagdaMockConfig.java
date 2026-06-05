package be.vlaanderen.vip.magda.magdamock.spring.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import be.vlaanderen.vip.magda.magdamock.client.MagdaMockConnection;

import java.io.IOException;

@Data
@Configuration
@ConfigurationProperties("magda.magdamock")
public class MagdaMockConfig {
    String soapTestPath;
    String restTestPath;
    String magdaXsdPath;
    Integer minimumTimeoutMillis;
    Integer maximumTimeoutMillis;
    boolean enableTimeout;

    @Bean
    public MagdaMockConnection magdaMockConnection() throws IOException {
        if (enableTimeout) {
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
        return MagdaMockConnection.create(restTestPath, soapTestPath, magdaXsdPath, minimumTimeoutMillis, maximumTimeoutMillis);
    }
}
