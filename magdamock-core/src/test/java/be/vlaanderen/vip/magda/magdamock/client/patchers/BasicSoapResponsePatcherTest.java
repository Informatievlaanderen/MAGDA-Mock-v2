package be.vlaanderen.vip.magda.magdamock.client.patchers;

import be.vlaanderen.vip.magda.client.MagdaDocument;
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
                <Afzender>
                    <Referte>REQ-123</Referte>
                    <Identificatie>SENDER-ID</Identificatie>
                    <Hoedanigheid>SENDER-ROLE</Hoedanigheid>
                    <Gebruiker>bart.peeters</Gebruiker>
                </Afzender>
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
            </Root>
            """;

        MagdaDocument request = MagdaDocument.fromString(requestXml);
        Document response = parseXml(responseXml);

        BasicSoapResponsePatcher soapResponse = new BasicSoapResponsePatcher(fixedClock, () -> FIXED_UUID);

        MagdaDocument result = soapResponse.patchResponse(request, response);

        assertBasicFields(result);
        assertEquals("bart.peeters", result.getValue("//Ontvanger/Gebruiker"));
    }

    @Test
    void patchResponse_withOutGebruiker() throws Exception {
        String requestXml = """
            <Root>
                <Afzender>
                    <Referte>REQ-123</Referte>
                    <Identificatie>SENDER-ID</Identificatie>
                    <Hoedanigheid>SENDER-ROLE</Hoedanigheid>
                </Afzender>
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
            </Root>
            """;

        MagdaDocument request = MagdaDocument.fromString(requestXml);
        Document response = parseXml(responseXml);

        BasicSoapResponsePatcher soapResponse = new BasicSoapResponsePatcher(fixedClock, () -> FIXED_UUID);

        MagdaDocument result = soapResponse.patchResponse(request, response);

        assertBasicFields(result);
        assertNull(result.getValue("//Ontvanger/Gebruiker"));
    }
}
