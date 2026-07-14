package be.vlaanderen.vip.magda.magdamock.spring.controller;

import be.vlaanderen.vip.magda.magdamock.client.MagdaMockConnection;
import jakarta.servlet.http.HttpServletRequest;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;

import static org.mockito.Mockito.*;

@SpringBootTest
public class MagdaMockControllerTest {
    @Test
    @SneakyThrows
    public void soapHandlerWhenUnableToParseDocument_shouldReturn500() {
        MagdaMockConnection magdaMockConnection = mock(MagdaMockConnection.class);
        HttpServletRequest httpRequest =  mock(HttpServletRequest.class);
        when(httpRequest.getHeaderNames()).thenReturn(Collections.enumeration(Collections.emptySet()));

        MagdaMockController magdaMockController = new MagdaMockController(magdaMockConnection);
        var response = magdaMockController.magdaSoap0200WebService("", httpRequest);
        Assertions.assertEquals(500, response.getStatusCode().value());
    }
}
