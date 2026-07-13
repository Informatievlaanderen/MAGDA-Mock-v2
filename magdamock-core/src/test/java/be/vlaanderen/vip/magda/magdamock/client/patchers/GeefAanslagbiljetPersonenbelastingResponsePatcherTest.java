package be.vlaanderen.vip.magda.magdamock.client.patchers;

import be.vlaanderen.vip.magda.magdamock.utils.MagdaDocument;
import be.vlaanderen.vip.magda.magdamock.client.soap.ResponsePatcherTest;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class GeefAanslagbiljetPersonenbelastingResponsePatcherTest extends ResponsePatcherTest {

    @Test
    void patchResponse_shouldPatchBaseFieldsAndCopyInkomensjaarAndPatchInkomensJaar() throws Exception {
        String requestXml = """
                <Root>
                    <Afzender>
                        <Referte>REQ-123</Referte>
                        <Identificatie>SENDER-ID</Identificatie>
                        <Hoedanigheid>SENDER-ROLE</Hoedanigheid>
                        <Gebruiker>bart.peeters</Gebruiker>
                    </Afzender>
                    <Vragen>
                        <Vraag>
                            <Inhoud>
                                <Criteria>
                                    <Inkomensjaar>2024</Inkomensjaar>
                                </Criteria>
                            </Inhoud>
                        </Vraag>
                    </Vragen>
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
                    <Antwoorden>
                        <Antwoord>
                            <Inhoud>
                                <AanslagbiljetPersonenbelasting>
                                    <Inkomensjaar>OLD_YEAR</Inkomensjaar>
                                </AanslagbiljetPersonenbelasting>
                            </Inhoud>
                        </Antwoord>
                    </Antwoorden>
                </Root>
                """;

        MagdaDocument request = MagdaDocument.fromString(requestXml);
        Document response = parseXml(responseXml);

        GeefAanslagbiljetPersonenbelastingResponsePatcher soapResponse =
                new GeefAanslagbiljetPersonenbelastingResponsePatcher(fixedClock, () -> FIXED_UUID);

        MagdaDocument result = soapResponse.patchResponse(request, response);

        assertBasicFields(result);
        assertEquals("bart.peeters", result.getValue("//Ontvanger/Gebruiker"));
        // specific behavior of GeefAanslagbiljetPersonenbelastingResponsePatcher
        assertEquals(
                "2024",
                result.getValue("//Antwoorden/Antwoord/Inhoud/AanslagbiljetPersonenbelasting/Inkomensjaar")
        );
    }

    @Test
    void patchResponse_shouldRemoveGebruikerWhenMissingInRequestAndPatchInkomensJaar() throws Exception {
        String requestXml = """
                <Root>
                    <Afzender>
                        <Referte>REQ-123</Referte>
                        <Identificatie>SENDER-ID</Identificatie>
                        <Hoedanigheid>SENDER-ROLE</Hoedanigheid>
                    </Afzender>
                    <Vragen>
                        <Vraag>
                            <Inhoud>
                                <Criteria>
                                    <Inkomensjaar>2022</Inkomensjaar>
                                </Criteria>
                            </Inhoud>
                        </Vraag>
                    </Vragen>
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
                    <Antwoorden>
                        <Antwoord>
                            <Inhoud>
                                <AanslagbiljetPersonenbelasting>
                                    <Inkomensjaar>OLD_YEAR</Inkomensjaar>
                                </AanslagbiljetPersonenbelasting>
                            </Inhoud>
                        </Antwoord>
                    </Antwoorden>
                </Root>
                """;

        MagdaDocument request = MagdaDocument.fromString(requestXml);
        Document response = parseXml(responseXml);

        GeefAanslagbiljetPersonenbelastingResponsePatcher soapResponse =
                new GeefAanslagbiljetPersonenbelastingResponsePatcher(fixedClock, () -> FIXED_UUID);

        MagdaDocument result = soapResponse.patchResponse(request, response);

        assertBasicFields(result);
        assertNull(result.getValue("//Ontvanger/Gebruiker"));
        assertEquals(
                "2022",
                result.getValue("//Antwoorden/Antwoord/Inhoud/AanslagbiljetPersonenbelasting/Inkomensjaar")
        );
    }

}
