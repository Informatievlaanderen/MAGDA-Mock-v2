package be.vlaanderen.vip.magda.magdamock.client.soap;

import com.github.tomakehurst.wiremock.extension.Parameters;
import com.github.tomakehurst.wiremock.http.ResponseDefinition;
import com.github.tomakehurst.wiremock.stubbing.ServeEvent;
import com.github.tomakehurst.wiremock.verification.LoggedRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Random;

import static be.vlaanderen.vip.magda.magdamock.client.soap.SoapStubRegistrar.VERSION_02_00;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RandomGenderFileTransformerTest {

    @TempDir
    Path tempDir;

    Path mannenDir;
    Path vrouwenDir;

    private final Parameters parameters = Parameters.from(
            Map.of(
                    "domain", "Persoon",
                    "service", "GeefPasfoto",
                    "version", VERSION_02_00
            )
    );

    private GenderFileTransformer transformer;

    @BeforeEach
    void setUp() {
        transformer = new GenderFileTransformer(tempDir, new Random(42));
        mannenDir = tempDir
                .resolve("Persoon")
                .resolve("GeefPasfoto")
                .resolve(VERSION_02_00)
                .resolve("mannen");

        vrouwenDir = tempDir
                .resolve("Persoon")
                .resolve("GeefPasfoto")
                .resolve(VERSION_02_00)
                .resolve("vrouwen");
    }

    @Test
    void shouldReturnRandomMaleFileWhenInszIsMale() throws Exception {

        Files.createDirectories(mannenDir);
        Files.createDirectories(vrouwenDir);

        Files.writeString(mannenDir.resolve("man1.xml"), "<response>man-1</response>");
        Files.writeString(mannenDir.resolve("man2.xml"), "<response>man-2</response>");
        Files.writeString(vrouwenDir.resolve("vrouw1.xml"), "<response>vrouw-1</response>");

        ServeEvent serveEvent = mockServeEvent(
                soapRequestWithInsz("85010112345"), // man
                parameters
        );

        ResponseDefinition response = transformer.transform(serveEvent);

        assertEquals(200, response.getStatus());
        String body = response.getBody();
        assertTrue(body.equals("<response>man-1</response>") || body.equals("<response>man-2</response>"));
    }

    @Test
    void shouldReturnFemaleFileWhenInszIsFemale() throws Exception {

        Files.createDirectories(mannenDir);
        Files.createDirectories(vrouwenDir);

        Files.writeString(mannenDir.resolve("man1.xml"), "<response>man-1</response>");
        Files.writeString(vrouwenDir.resolve("vrouw1.xml"), "<response>vrouw-1</response>");
        Files.writeString(vrouwenDir.resolve("vrouw2.xml"), "<response>vrouw-2</response>");

        ServeEvent serveEvent = mockServeEvent(
                soapRequestWithInsz("85010112445"), // => vrouw
                parameters
        );

        ResponseDefinition response = transformer.transform(serveEvent);

        assertEquals(200, response.getStatus());
        String body = response.getBody();
        assertTrue(body.equals("<response>vrouw-1</response>") || body.equals("<response>vrouw-2</response>"));
    }

    @Test
    void shouldReturn500WhenInszIsInvalid() {

        ServeEvent serveEvent = mockServeEvent(
                soapRequestWithInsz("ABC"),
                parameters
        );

        ResponseDefinition response = transformer.transform(serveEvent);

        assertEquals(500, response.getStatus());
        assertTrue(response.getBody().contains("INSZ must contain 11 digits"));
    }

    @Test
    void shouldReturn500WhenInszIsInvalid10Digits() {

        ServeEvent serveEvent = mockServeEvent(
                soapRequestWithInsz("1234567890"),
                parameters
        );

        ResponseDefinition response = transformer.transform(serveEvent);

        assertEquals(500, response.getStatus());
        assertTrue(response.getBody().contains("INSZ must contain 11 digits"));
    }

    @Test
    void shouldReturn500WhenNoXmlFilesExistInGenderDirectory() throws Exception {
        Files.createDirectories(
                tempDir.resolve("Persoon").resolve("GeefPasfoto").resolve(VERSION_02_00).resolve("mannen")
        );

        ServeEvent serveEvent = mockServeEvent(
                soapRequestWithInsz("85010112345"),
                parameters
        );

        ResponseDefinition response = transformer.transform(serveEvent);

        assertEquals(500, response.getStatus());
        assertTrue(response.getBody().contains("No XML files found in directory"));
    }

    private ServeEvent mockServeEvent(String requestBody, Parameters parameters) {
        LoggedRequest request = mock(LoggedRequest.class);
        when(request.getBodyAsString()).thenReturn(requestBody);

        ServeEvent serveEvent = mock(ServeEvent.class);
        when(serveEvent.getRequest()).thenReturn(request);
        when(serveEvent.getTransformerParameters()).thenReturn(parameters);

        return serveEvent;
    }

    private String soapRequestWithInsz(String insz) {
        return """
                <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/">
                  <soapenv:Body>
                    <ns2:Request xmlns:ns2="http://magdamock.com">
                      <INSZ>%s</INSZ>
                    </ns2:Request>
                  </soapenv:Body>
                </soapenv:Envelope>
                """.formatted(insz);
    }

}
