package be.vlaanderen.vip.magda.magdamock.client.patchers;

import be.vlaanderen.vip.magda.magdamock.client.soap.ResponsePatcherTest;
import be.vlaanderen.vip.magda.magdamock.utils.MagdaMockDocument;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class SimpleXpathPatcherTest extends ResponsePatcherTest {

    @Test
    void patchResponse_shouldPatchINSZ() throws Exception {
        String requestXml = """
                <Root>
                    <Afzender>
                        <Referte>REQ-123</Referte>
                        <Identificatie>SENDER-ID</Identificatie>
                        <Hoedanigheid>SENDER-ROLE</Hoedanigheid>
                    </Afzender>
                    <Criteria>
                        <INSZ>00000000000</INSZ>
                    </Criteria>
                </Root>
                """;

        String responseXml = """
                <Root>
                    <Ontvanger>
                        <Referte>OLD</Referte>
                        <Identificatie>OLD</Identificatie>
                        <Hoedanigheid>OLD</Hoedanigheid>
                        <Gebruiker>OLD</Gebruiker>
                    </Ontvanger>
                    <Antwoord>
                        <Referte>OLD</Referte>
                    </Antwoord>
                    <Context>
                        <Bericht>
                            <Tijdstip>
                                <Datum>OLD</Datum>
                                <Tijd>OLD</Tijd>
                            </Tijdstip>
                        </Bericht>
                    </Context>
                    <Afzender>
                        <Referte>OLD</Referte>
                        <Identificatie>OLD</Identificatie>
                        <Naam>OLD</Naam>
                    </Afzender>
                    <INSZ>00000000000</INSZ>
                    <SocialeStatuten>
                        <SociaalStatuut>
                            <Naam>STATUUT_A</Naam>
                            <Resultaat>
                                <Code>1</Code>
                                <Omschrijving>Van toepassing</Omschrijving>
                            </Resultaat>
                        </SociaalStatuut>
                        <SociaalStatuut>
                            <Naam>STATUUT_X</Naam>
                            <Resultaat>
                                <Code>1</Code>
                                <Omschrijving>Van toepassing</Omschrijving>
                            </Resultaat>
                        </SociaalStatuut>
                    </SocialeStatuten>
                </Root>
                """;

        MagdaMockDocument request = MagdaMockDocument.fromString(requestXml);
        Document response = parseXml(responseXml);

        SimpleXpathPatcher soapResponse =
                new SimpleXpathPatcher(fixedClock, () -> FIXED_UUID, "//Root/INSZ", "//Criteria/INSZ");

        MagdaMockDocument result = soapResponse.patchResponse(request, response);

        assertEquals("00000000000", result.getValue("//INSZ"));
    }

    @Test
    void patchResponse_shouldNotFailWhenINSZNotPresent() throws Exception {
        String requestXml = """
                <Root>
                    <Repliek>
                    <Context>
                    <Afzender>
                        <Referte>REQ-123</Referte>
                        <Identificatie>SENDER-ID</Identificatie>
                        <Hoedanigheid>SENDER-ROLE</Hoedanigheid>
                    </Afzender>
                    <Criteria>
                        <INSZ>00000000000</INSZ>
                    </Criteria>
                    </Context>
                    </Repliek>
                </Root>
                """;

        String responseXml = """
                <Root>
                    <Repliek>
                    <SocialeStatuten>
                        <SociaalStatuut>
                            <Naam>STATUUT_A</Naam>
                            <Resultaat>
                                <Code>1</Code>
                                <Omschrijving>Van toepassing</Omschrijving>
                            </Resultaat>
                        </SociaalStatuut>
                    </SocialeStatuten>
                    </Repliek>
                </Root>
                """;

        MagdaMockDocument request = MagdaMockDocument.fromString(requestXml);
        Document response = parseXml(responseXml);

        SimpleXpathPatcher soapResponse =
                new SimpleXpathPatcher(fixedClock, () -> FIXED_UUID, "//Root/INSZ", "//Criteria/INSZ");

        MagdaMockDocument result = soapResponse.patchResponse(request, response);
        assertNull(result.getValue("//Ontvanger/Gebruiker"));
        assertEquals(request.getValue("//Afzender/Referte"), result.getValue("//Ontvanger/Referte"));
    }
}
