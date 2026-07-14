package be.vlaanderen.vip.magda.magdamock.client.utils;

import be.vlaanderen.vip.magda.magdamock.client.soap.Domain;
import be.vlaanderen.vip.magda.magdamock.client.soap.Service;
import be.vlaanderen.vip.magda.magdamock.client.soap.Version;
import be.vlaanderen.vip.magda.magdamock.utils.SoapResourceUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SoapResourceUtilTest {

    @Test
    void loadDomainsFromPaths_persoonDomainStructure() {
        List<String> paths = List.of(
                "Persoon/GeefPersoon/02.00.0000/1234567890.xml",
                "Persoon/GeefPersoon/02.00.0000/1234567891.xml",
                "Persoon/GeefAttest/02.00.0000/1234567892.xml",
                "Persoon/GeefAttest/02.00.0000/1234567893.xml"
        );

        List<Domain> domains = SoapResourceUtil.loadDomainsFromPaths(paths);

        assertEquals(1, domains.size());

        Domain domain = domains.get(0);
        assertEquals("Persoon", domain.name());
        assertEquals(2, domain.services().size());

        Service geefAttest = domain.services().stream()
                .filter(service -> service.name().equals("GeefAttest"))
                .findFirst()
                .orElseThrow();

        assertEquals(1, geefAttest.versions().size());

        Version attestVersion = geefAttest.versions().get(0);
        assertEquals("02.00.0000", attestVersion.name());
        assertEquals(
                List.of("1234567892.xml", "1234567893.xml"),
                attestVersion.files()
        );

        Service geefPersoon = domain.services().stream()
                .filter(service -> service.name().equals("GeefPersoon"))
                .findFirst()
                .orElseThrow();

        assertEquals(1, geefPersoon.versions().size());

        Version persoonVersion = geefPersoon.versions().get(0);
        assertEquals("02.00.0000", persoonVersion.name());
        assertEquals(
                List.of("1234567890.xml", "1234567891.xml"),
                persoonVersion.files()
        );
    }

    @Test
    void resolvePaths_persoonStructure(@TempDir Path tempDir) throws IOException {
        Path base = tempDir.resolve("soap");

        Path geefPersoonPath = base.resolve("Persoon/GeefPersoon/02.00.0000");
        Files.createDirectories(geefPersoonPath);
        Files.createFile(geefPersoonPath.resolve("1234567890.xml"));
        Files.createFile(geefPersoonPath.resolve("1234567891.xml"));

        Path geefAttestPath = base.resolve("Persoon/GeefAttest/02.00.0000");
        Files.createDirectories(geefAttestPath);
        Files.createFile(geefAttestPath.resolve("1234567892.xml"));
        Files.createFile(geefAttestPath.resolve("1234567893.xml"));
        Files.createFile(geefAttestPath.resolve("readme.txt"));

        List<String> result = SoapResourceUtil.resolvePaths(base.toString());

        assertEquals(4, result.size());
        assertTrue(result.contains("Persoon/GeefPersoon/02.00.0000/1234567890.xml"));
        assertTrue(result.contains("Persoon/GeefPersoon/02.00.0000/1234567891.xml"));
        assertTrue(result.contains("Persoon/GeefAttest/02.00.0000/1234567892.xml"));
        assertTrue(result.contains("Persoon/GeefAttest/02.00.0000/1234567893.xml"));
        assertFalse(result.contains("Persoon/GeefAttest/02.00.0000/readme.txt"));
    }

}
