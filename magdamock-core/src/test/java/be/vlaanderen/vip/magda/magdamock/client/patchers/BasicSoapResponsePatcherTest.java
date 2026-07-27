package be.vlaanderen.vip.magda.magdamock.client.patchers;

import be.vlaanderen.vip.magda.magdamock.utils.MagdaMockDocument;
import be.vlaanderen.vip.magda.magdamock.client.soap.ResponsePatcherTest;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class BasicSoapResponsePatcherTest extends ResponsePatcherTest {

    @Test
    void patchResponse_withGebruiker() throws Exception {
        String requestXml = """
            <Root>
            <Verzoek>
                <Context>
                    <Naam>GeefAanslagbiljetPersonenbelasting</Naam>
                    <Versie>02.00.0000</Versie>
                    <Bericht>
                        <Type>VRAAG</Type>
                        <Tijdstip>
                            <Datum>2026-07-20</Datum>
                            <Tijd>13:28:08.366</Tijd>
                        </Tijdstip>
                        <Afzender>
                            <Referte>REQ-123</Referte>
                            <Identificatie>SENDER-ID</Identificatie>
                            <Hoedanigheid>SENDER-ROLE</Hoedanigheid>
                            <Gebruiker>bart.peeters</Gebruiker>
                        </Afzender>
                    </Bericht>
                </Context>
                </Verzoek>
            </Root>
            """;

        String responseXml = """
            <Root>
                    <Repliek>
                        <Antwoord>
                            <Referte>
                            </Referte>
                        </Antwoord>
                    </Repliek>
            </Root>
            """;

        MagdaMockDocument request = MagdaMockDocument.fromString(requestXml);
        Document response = parseXml(responseXml);

        BasicSoapResponsePatcher soapResponse = new BasicSoapResponsePatcher(fixedClock, () -> FIXED_UUID);

        MagdaMockDocument result = soapResponse.patchResponse(request, response);

        assertBasicFields(result);
        assertEquals("bart.peeters", result.getValue("//Ontvanger/Gebruiker"));
    }

    @Test
    void patchResponse_withOutGebruiker() throws Exception {
        String requestXml = """
            <Root>
            <Verzoek>
                <Context>
                    <Naam>GeefAanslagbiljetPersonenbelasting</Naam>
                    <Versie>02.00.0000</Versie>
                    <Bericht>
                        <Type>VRAAG</Type>
                        <Tijdstip>
                            <Datum>2026-07-20</Datum>
                            <Tijd>13:28:08.366</Tijd>
                        </Tijdstip>
                        <Afzender>
                            <Referte>REQ-123</Referte>
                            <Identificatie>SENDER-ID</Identificatie>
                            <Hoedanigheid>SENDER-ROLE</Hoedanigheid>
                        </Afzender>
                    </Bericht>
                </Context>
                </Verzoek>
            </Root>
            """;

        String responseXml = """
            <Root>
                    <Repliek>
                        <Antwoord>
                            <Referte>
                            </Referte>
                        </Antwoord>
                    </Repliek>
            </Root>
            """;

        MagdaMockDocument request = MagdaMockDocument.fromString(requestXml);
        Document response = parseXml(responseXml);

        BasicSoapResponsePatcher soapResponse = new BasicSoapResponsePatcher(fixedClock, () -> FIXED_UUID);

        MagdaMockDocument result = soapResponse.patchResponse(request, response);

        assertBasicFields(result);
        assertNull(result.getValue("//Ontvanger/Gebruiker"));
    }
}
