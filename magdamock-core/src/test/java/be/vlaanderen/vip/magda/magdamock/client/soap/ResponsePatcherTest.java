package be.vlaanderen.vip.magda.magdamock.client.soap;

import be.vlaanderen.vip.magda.magdamock.utils.MagdaDocument;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class ResponsePatcherTest {

    protected static final String UUID_STRING = "123e4567-e89b-12d3-a456-426614174000";
    protected static final UUID FIXED_UUID = UUID.fromString(UUID_STRING);
    protected static final String TIME_STRING = "14:15:16.789";
    protected static final String DATE_STRING = "2026-03-23";

    protected Clock fixedClock = Clock.fixed(
            Instant.parse("2026-03-23T14:15:16.789Z"),
            ZoneId.of("UTC")
    );

    protected Document parseXml(String xml) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        return factory.newDocumentBuilder().parse(new InputSource(new StringReader(xml)));
    }

    protected void assertBasicFields(MagdaDocument result) {
        assertEquals("REQ-123", result.getValue("//Ontvanger/Referte"));
        assertEquals("REQ-123", result.getValue("//Antwoord/Referte"));
        assertEquals("SENDER-ID", result.getValue("//Ontvanger/Identificatie"));
        assertEquals("SENDER-ROLE", result.getValue("//Ontvanger/Hoedanigheid"));

        assertEquals(DATE_STRING, result.getValue("//Context/Bericht/Tijdstip/Datum"));
        assertEquals(TIME_STRING, result.getValue("//Context/Bericht/Tijdstip/Tijd"));

        assertEquals(UUID_STRING, result.getValue("//Afzender/Referte"));
        assertEquals("kb.vlaanderen.be/aiv/magda-mock-server", result.getValue("//Afzender/Identificatie"));
        assertEquals("Magda Mock Server", result.getValue("//Afzender/Naam"));
    }
}
