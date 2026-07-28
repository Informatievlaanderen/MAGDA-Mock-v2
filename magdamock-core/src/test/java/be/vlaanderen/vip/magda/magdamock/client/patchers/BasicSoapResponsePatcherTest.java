package be.vlaanderen.vip.magda.magdamock.client.patchers;

import be.vlaanderen.vip.magda.magdamock.utils.MagdaMockDocument;
import be.vlaanderen.vip.magda.magdamock.client.soap.ResponsePatcherTest;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

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

    @Test
    void patchResponse_updatesDateAndTimeUitzonderingen()  throws Exception {
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
<web:GeefDossiersResponse xmlns:web="http://webservice.geefdossiersdienst-02_00.dossier-02_00.vip.vlaanderen.be">
    <Repliek>
            <Context>
               <Naam>GeefDossiers</Naam>
               <Versie>02.00.0000</Versie>
               <Bericht>
                  <Type>ANTWOORD</Type>
                  <Tijdstip>
                     <Datum>2018-09-20</Datum>
                     <Tijd>10:08:51.187</Tijd>
                  </Tijdstip>
                  <Afzender>
                     <Identificatie>vip.vlaanderen.be</Identificatie>
                     <Naam>MagdaGateway</Naam>
                     <Referte>3500e729-553d-4cc9-bce4-5a32c3ccacb2</Referte>
                  </Afzender>
                  <Ontvanger>
                     <Identificatie>aiv.vlaanderen.be/dosis-test</Identificatie>
                     <Referte>7cd18cf1-7450-4fd7-8f98-cb87e65710a3</Referte>
                     <Hoedanigheid>1300</Hoedanigheid>
                  </Ontvanger>
               </Bericht>
            </Context>
            <Antwoorden>
               <Antwoord>
                  <Referte>7cd18cf1-7450-4fd7-8f98-cb87e65710a3</Referte>
                  <Uitzonderingen>
                     <Uitzondering>
                        <Identificatie>99986</Identificatie>
                        <Oorsprong>MAGDA</Oorsprong>
                        <Type>FOUT</Type>
                        <Tijdstip>
                           <Datum>2018-10-02</Datum>
                           <Tijd>16:36:02.659</Tijd>
                        </Tijdstip>
                        <Diagnose>Ongekende foutcode van de bron, contacteer Magda helpdesk</Diagnose>
                        <Annotaties>
                           <Annotatie>
                              <Naam>Bron</Naam>
                              <Waarde>VAPH</Waarde>
                           </Annotatie>
                        </Annotaties>
                     </Uitzondering>
                     <Uitzondering>
                        <Identificatie>60013</Identificatie>
                        <Oorsprong>MAGDA</Oorsprong>
                        <Type>FOUT</Type>
                        <Tijdstip>
                           <Datum>2018-10-02</Datum>
                           <Tijd>16:36:05.108</Tijd>
                        </Tijdstip>
                        <Diagnose>Fout in toegang naar de bron</Diagnose>
                        <Annotaties>
                           <Annotatie>
                              <Naam>Bron</Naam>
                              <Waarde>STL</Waarde>
                           </Annotatie>
                        </Annotaties>
                     </Uitzondering>
                  </Uitzonderingen>
               </Antwoord>
            </Antwoorden>
                              <Uitzonderingen>
                     <Uitzondering>
                        <Identificatie>99986</Identificatie>
                        <Oorsprong>MAGDA</Oorsprong>
                        <Type>FOUT</Type>
                        <Tijdstip>
                           <Datum>2018-10-02</Datum>
                           <Tijd>16:36:02.659</Tijd>
                        </Tijdstip>
                        <Diagnose>Ongekende foutcode van de bron, contacteer Magda helpdesk</Diagnose>
                        <Annotaties>
                           <Annotatie>
                              <Naam>Bron</Naam>
                              <Waarde>VAPH</Waarde>
                           </Annotatie>
                        </Annotaties>
                     </Uitzondering>
                     <Uitzondering>
                        <Identificatie>60013</Identificatie>
                        <Oorsprong>MAGDA</Oorsprong>
                        <Type>FOUT</Type>
                        <Tijdstip>
                           <Datum>2018-10-02</Datum>
                           <Tijd>16:36:05.108</Tijd>
                        </Tijdstip>
                        <Diagnose>Fout in toegang naar de bron</Diagnose>
                        <Annotaties>
                           <Annotatie>
                              <Naam>Bron</Naam>
                              <Waarde>STL</Waarde>
                           </Annotatie>
                        </Annotaties>
                     </Uitzondering>
                  </Uitzonderingen>
         </Repliek>
</web:GeefDossiersResponse>
        """;

        MagdaMockDocument request = MagdaMockDocument.fromString(requestXml);
        Document response = parseXml(responseXml);

        BasicSoapResponsePatcher soapResponse = new BasicSoapResponsePatcher(fixedClock, () -> FIXED_UUID);

        MagdaMockDocument result = soapResponse.patchResponse(request, response);

        assertBasicFields(result);

        NodeList xpath = result.xpath("//Uitzonderingen/Uitzondering/Tijdstip/Tijd");
        assertEquals(4, xpath.getLength());
        for (int i = 0; i < xpath.getLength(); i++) {
            assertEquals(TIME_STRING, xpath.item(i).getTextContent());
        }
        xpath = result.xpath("//Uitzonderingen/Uitzondering/Tijdstip/Datum");
        assertEquals(4, xpath.getLength());
        for (int i = 0; i < xpath.getLength(); i++) {
            assertEquals(DATE_STRING, xpath.item(i).getTextContent());
        }
    }
}
