package be.vlaanderen.vip.magda.magdamock.client.soap;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SoapStubRegistrarTest {

    private WireMockServer wireMockServer;

    @TempDir
    Path tempDir;

    @AfterEach
    void tearDown() {
        if (wireMockServer != null) {
            wireMockServer.stop();
        }
    }

    @Test
    void registerDomain_shouldRegisterInszStub() throws Exception {
        wireMockServer = new WireMockServer(options().dynamicPort());
        wireMockServer.start();

        String soapTestPath = tempDir.resolve("soap").toString();

        String domainName = "person";
        String serviceName = "GeefPersoon";
        String versionName = "02.00";
        String insz = "12345678901";

        writeStubFile(
                tempDir,
                soapTestPath,
                domainName,
                serviceName,
                versionName,
                insz + ".xml",
                "<response>persoon gevonden</response>"
        );

        Domain domain = new Domain(
                domainName,
                List.of(new Service(
                        serviceName,
                        List.of(new Version(
                                versionName,
                                List.of(insz + ".xml")
                        ))
                ))
        );

        SoapStubRegistrar registrar = new SoapStubRegistrar(wireMockServer, soapTestPath);

        registrar.registerDomain(domain);

        String requestBody = """
                <Envelope>
                  <Body>
                    <Naam>%s</Naam>
                    <Versie>%s</Versie>
                    <INSZ>%s</INSZ>
                  </Body>
                </Envelope>
                """.formatted(serviceName, versionName, insz);

        var response = HttpClient.newHttpClient().send(
                HttpRequest.newBuilder()
                        .uri(URI.create("http://localhost:" + wireMockServer.port() + "/soap"))
                        .header("Content-Type", "text/xml; charset=utf-8")
                        .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                        .build(),
                HttpResponse.BodyHandlers.ofString()
        );

        assertEquals(200, response.statusCode());
        assertEquals("<response>persoon gevonden</response>", response.body());
        assertTrue(response.headers()
                .firstValue("Content-Type")
                .orElse("")
                .contains("text/xml"));
    }

    @Test
    void registerDomain_shouldRegisterNotFoundStub() throws Exception {
        wireMockServer = new WireMockServer(options().dynamicPort());
        wireMockServer.start();

        String soapTestPath = tempDir.resolve("soap").toString();

        String domainName = "person";
        String serviceName = "GeefPersoon";
        String versionName = "02.00";

        writeStubFile(
                tempDir,
                soapTestPath,
                domainName,
                serviceName,
                versionName,
                "notfound.xml",
                "<response>niet gevonden</response>"
        );

        Domain domain = new Domain(
                domainName,
                List.of(new Service(
                        serviceName,
                        List.of(new Version(
                                versionName,
                                List.of("notfound.xml")
                        ))
                ))
        );

        SoapStubRegistrar registrar = new SoapStubRegistrar(wireMockServer, soapTestPath);

        registrar.registerDomain(domain);

        String requestBody = """
                <Envelope>
                  <Body>
                    <Naam>%s</Naam>
                    <Versie>%s</Versie>
                    <INSZ>00000000000</INSZ>
                  </Body>
                </Envelope>
                """.formatted(serviceName, versionName);

        var response = HttpClient.newHttpClient().send(
                java.net.http.HttpRequest.newBuilder()
                        .uri(URI.create("http://localhost:" + wireMockServer.port() + "/soap"))
                        .header("Content-Type", "text/xml; charset=utf-8")
                        .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                        .build(),
                HttpResponse.BodyHandlers.ofString()
        );

        assertEquals(200, response.statusCode());
        assertEquals("<response>niet gevonden</response>", response.body());
    }

    @Test
    void registerDomain_shouldThrowIllegalStateExceptionWhenStubFileDoesNotExist() {
        wireMockServer = new WireMockServer(options().dynamicPort());
        wireMockServer.start();

        String soapTestPath = tempDir.resolve("soap").toString();

        String domainName = "person";
        String serviceName = "GeefPersoon";
        String versionName = "02.00";
        String insz = "12345678901";

        Domain domain = new Domain(
                domainName,
                List.of(new Service(
                        serviceName,
                        List.of(new Version(
                                versionName,
                                List.of(insz + ".xml")
                        ))
                ))
        );

        SoapStubRegistrar registrar = new SoapStubRegistrar(wireMockServer, soapTestPath);

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> registrar.registerDomain(domain)
        );

        assertTrue(exception.getMessage().contains("SOAP file can not be registered"));
        assertNotNull(exception.getCause());
        assertInstanceOf(IOException.class, exception.getCause());
    }

    @Test
    void registerDomain_shouldRegisterOndernemingStub() throws Exception {
        wireMockServer = new WireMockServer(options().dynamicPort());
        wireMockServer.start();

        String soapTestPath = tempDir.resolve("soap").toString();

        String domainName = "enterprise";
        String serviceName = "GeefOnderneming";
        String versionName = "02.00";
        String ondernemingsNummer = "1234567890";

        writeStubFile(
                tempDir,
                soapTestPath,
                domainName,
                serviceName,
                versionName,
                ondernemingsNummer + ".xml",
                "<response>onderneming gevonden</response>"
        );

        Domain domain = new Domain(
                domainName,
                List.of(new Service(
                        serviceName,
                        List.of(new Version(
                                versionName,
                                List.of(ondernemingsNummer + ".xml")
                        ))
                ))
        );

        SoapStubRegistrar registrar = new SoapStubRegistrar(wireMockServer, soapTestPath);

        registrar.registerDomain(domain);

        String requestBody = """
            <Envelope>
              <Body>
                <Naam>%s</Naam>
                <Versie>%s</Versie>
                <Ondernemingsnummer>%s</Ondernemingsnummer>
              </Body>
            </Envelope>
            """.formatted(serviceName, versionName, ondernemingsNummer);

        var response = HttpClient.newHttpClient().send(
                HttpRequest.newBuilder()
                        .uri(URI.create("http://localhost:" + wireMockServer.port() + "/soap"))
                        .header("Content-Type", "text/xml; charset=utf-8")
                        .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                        .build(),
                HttpResponse.BodyHandlers.ofString()
        );

        assertEquals(200, response.statusCode());
        assertEquals("<response>onderneming gevonden</response>", response.body());
        assertTrue(response.headers()
                .firstValue("Content-Type")
                .orElse("")
                .contains("text/xml"));
    }

    private static void writeStubFile(
            Path root,
            String soapTestPath,
            String domain,
            String service,
            String version,
            String fileName,
            String content
    ) throws IOException {
        Path file = root.resolve(Path.of(soapTestPath, domain, service, version, fileName));
        Files.createDirectories(file.getParent());
        Files.writeString(file, content);
    }

}
