package be.vlaanderen.vip.magda.magdamock.spring.config;

import be.vlaanderen.vip.magda.client.connection.MagdaConnection;
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
    String magdaMockTestPath;
    String soapTestPath;

    @Bean
    public MagdaConnection magdaMockConnection() throws IOException {
        return MagdaMockConnection.create(magdaMockTestPath, soapTestPath);
    }
}
