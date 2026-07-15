package be.vlaanderen.vip.magda.magdamock.client.patchers;

import be.vlaanderen.vip.magda.magdamock.utils.MagdaMockDocument;
import be.vlaanderen.vip.magda.magdamock.client.soap.ResponsePatcherTest;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GeefSociaalStatuutResponsePatcherTest extends ResponsePatcherTest {

    @Test
    void patchResponse_shouldPatchBaseFieldsCopyInszRemoveExtraStatutesAndAddMissingOnes() throws Exception {
        String requestXml = """
                <Root>
                    <Afzender>
                        <Referte>REQ-123</Referte>
                        <Identificatie>SENDER-ID</Identificatie>
                        <Hoedanigheid>SENDER-ROLE</Hoedanigheid>
                        <Gebruiker>bart.peeters</Gebruiker>
                    </Afzender>
                    <INSZ>12345678901</INSZ>
                    <SocialeStatuten>
                        <SociaalStatuut>
                            <Naam>STATUUT_A</Naam>
                        </SociaalStatuut>
                        <SociaalStatuut>
                            <Naam>STATUUT_B</Naam>
                        </SociaalStatuut>
                    </SocialeStatuten>
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

        GeefSociaalStatuutResponsePatcher soapResponse =
                new GeefSociaalStatuutResponsePatcher(fixedClock, () -> FIXED_UUID);

        MagdaMockDocument result = soapResponse.patchResponse(request, response);

        assertEquals("bart.peeters", result.getValue("//Ontvanger/Gebruiker"));
        assertBasicFields(result);

        assertEquals("12345678901", result.getValue("//INSZ"));

        assertNull(result.getValue("//SociaalStatuut[Naam='STATUUT_X']/Naam"));

        assertEquals("STATUUT_A", result.getValue("//SociaalStatuut[Naam='STATUUT_A']/Naam"));
        assertEquals("1", result.getValue("//SociaalStatuut[Naam='STATUUT_A']/Resultaat/Code"));
        assertEquals("Van toepassing", result.getValue("//SociaalStatuut[Naam='STATUUT_A']/Resultaat/Omschrijving"));

        assertEquals("STATUUT_B", result.getValue("//SociaalStatuut[Naam='STATUUT_B']/Naam"));
        assertEquals("0", result.getValue("//SociaalStatuut[Naam='STATUUT_B']/Resultaat/Code"));
        assertEquals("Niet van toepassing", result.getValue("//SociaalStatuut[Naam='STATUUT_B']/Resultaat/Omschrijving"));

        List<String> finalNames = result.getValues("//SociaalStatuut/Naam");
        assertEquals(2, finalNames.size());
        assertTrue(finalNames.contains("STATUUT_A"));
        assertTrue(finalNames.contains("STATUUT_B"));
    }

    @Test
    void patchResponse_shouldRemoveGebruikerWhenMissingInRequest() throws Exception {
        String requestXml = """
                <Root>
                    <Afzender>
                        <Referte>REQ-123</Referte>
                        <Identificatie>SENDER-ID</Identificatie>
                        <Hoedanigheid>SENDER-ROLE</Hoedanigheid>
                    </Afzender>
                    <INSZ>12345678901</INSZ>
                    <SocialeStatuten>
                        <SociaalStatuut>
                            <Naam>STATUUT_A</Naam>
                        </SociaalStatuut>
                    </SocialeStatuten>
                </Root>
                """;

        String responseXml = """
                <Root>
                    <Ontvanger>
                        <Referte>OLD</Referte>
                        <Identificatie>OLD</Identificatie>
                        <Hoedanigheid>OLD</Hoedanigheid>
                        <Gebruiker>TO_BE_REMOVED</Gebruiker>
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
                    </SocialeStatuten>
                </Root>
                """;

        MagdaMockDocument request = MagdaMockDocument.fromString(requestXml);
        Document response = parseXml(responseXml);

        GeefSociaalStatuutResponsePatcher soapResponse =
                new GeefSociaalStatuutResponsePatcher(fixedClock, () -> FIXED_UUID);

        MagdaMockDocument result = soapResponse.patchResponse(request, response);

        assertBasicFields(result);
        assertNull(result.getValue("//Ontvanger/Gebruiker"));
        assertEquals("12345678901", result.getValue("//INSZ"));
        assertEquals("STATUUT_A", result.getValue("//SociaalStatuut[Naam='STATUUT_A']/Naam"));
    }
}
