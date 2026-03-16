package be.vlaanderen.vip.magda.magdamock.spring.config;

import be.vlaanderen.vip.magda.client.connection.MagdaConnection;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import be.vlaanderen.vip.magda.magdamock.client.MagdaMockConnection;

@Data
@Configuration
@ConfigurationProperties("magda.magdamock")
public class MagdaMockConfig {
    String magdaMockTestPath;

    @Bean
    public MagdaConnection magdaMockConnection() {
        return MagdaMockConnection.create(magdaMockTestPath);
    }
}
