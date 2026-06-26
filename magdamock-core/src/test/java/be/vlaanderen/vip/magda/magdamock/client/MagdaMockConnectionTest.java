package be.vlaanderen.vip.magda.magdamock.client;

import be.vlaanderen.vip.magda.client.MagdaDocument;
import be.vlaanderen.vip.magda.magdamock.config.WireMockData;
import be.vlaanderen.vip.magda.magdamock.soap.LenientSoapBodyValidator;
import be.vlaanderen.vip.magda.magdamock.utils.MockDataTemplateHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.direct.DirectCallHttpServerFactory;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.containing;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.matchingXPath;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class MagdaMockConnectionTest {

    @Test
    @SneakyThrows
    void whenTemplateReplacesOk_shouldReturnStatus200AndExpectedOutput() {
        MagdaMockConnection connection = MagdaMockConnection.create(createWireMockForTest(), new LenientSoapBodyValidator(), new LenientSoapBodyValidator());
        var response = connection.sendRestRequest("/template/ok", "", "GET", "", "Tue, 29 Oct 2024 16:56:32 GMT", "");
        assertEquals(200, response.status());
        assertEquals("\"2019-10-19\"", new ObjectMapper().readTree(response.body()).get("test").toString());
        assertNotNull(response.headers().get("x-correlation-id"));
    }


    @Test
    @SneakyThrows
    void whenTemplateReplacesNok_shouldReturnStatus500() {
        MagdaMockConnection connection = MagdaMockConnection.create(createWireMockForTest(), new LenientSoapBodyValidator(), new LenientSoapBodyValidator());
        var response = connection.sendRestRequest("/template/nok", "", "GET", "", "Tue, 29 Oct 2024 16:56:32 GMT", "");
        assertEquals(
                "{\"test\":\"{formatDate (dateMath (dateMath (parseDate request.headers.Date) '-10d') '-5y')}}\"}",
                new ObjectMapper().readTree(response.body()).toString()
        );
        assertNotNull(response.headers().get("x-correlation-id"));
    }

    @Test
    @SneakyThrows
    void whenDocumentFound_shouldReturnDocument() {
        MagdaMockConnection connection = MagdaMockConnection.create(createWireMockForTest(), new LenientSoapBodyValidator(), new LenientSoapBodyValidator());
        var response = connection.sendDocument(
                MagdaDocument.fromString("""
                        <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:web="http://magda.vlaanderen.be/persoon/soap/geefpersoon/v02_02">
                            <soapenv:Header/>
                            <soapenv:Body>
                                <web:GeefPersoon>
                                    <Verzoek>
                                        <Context>
                                            <Naam>GeefPersoon</Naam>
                                            <Versie>02.02.0000</Versie>
                                            <Bericht>
                                                <Type>VRAAG</Type>
                                                <Tijdstip>
                                                    <Datum>2022-02-02</Datum>
                                                </Tijdstip>
                                            </Bericht>
                                        </Context>
                                        <Vragen>
                                            <Vraag>
                                                <Referte>482d403a-22aa-11f1-a0f2-04cf4b22694c</Referte>
                                                <Inhoud>
                                                    <Criteria>
                                                        <INSZ>00631499723</INSZ>
                                                    </Criteria>
                                                    <Bron>RR</Bron>
                                                    <Taal>nl</Taal>
                                                </Inhoud>
                                            </Vraag>
                                        </Vragen>
                                    </Verzoek>
                                </web:GeefPersoon>
                            </soapenv:Body>
                        </soapenv:Envelope>
                        """).getXml()
        );
        assertNotNull(response);
    }
    @Test
    @SneakyThrows
    void whenSenderIdIsMissing_shouldReturnMagdaError13001() {
        MagdaMockConnection connection = MagdaMockConnection.create(createWireMockForTest(), new LenientSoapBodyValidator(), new LenientSoapBodyValidator());
        var response = connection.sendDocument(
                MagdaDocument.fromString("""
                    <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:web="http://magda.vlaanderen.be/persoon/soap/geefpersoon/v02_02">
                        <soapenv:Header/>
                        <soapenv:Body>
                            <web:GeefPersoon>
                                <Verzoek>
                                    <Context>
                                        <Naam>GeefPersoon</Naam>
                                        <Versie>02.02.0000</Versie>
                                        <Bericht>
                                            <Type>VRAAG</Type>
                                            <Tijdstip>
                                                <Datum>2022-02-02</Datum>
                                            </Tijdstip>
                                            <Afzender>
                                                <Identificatie></Identificatie>
                                                <Referte></Referte>
                                                <Hoedanigheid></Hoedanigheid>
                                            </Afzender>
                                        </Bericht>
                                    </Context>
                                    <Vragen>
                                        <Vraag>
                                            <Referte>482d403a-22aa-11f1-a0f2-04cf4b22694c</Referte>
                                            <Inhoud>
                                                <Criteria>
                                                    <INSZ>00631499723</INSZ>
                                                </Criteria>
                                                <Bron>RR</Bron>
                                                <Taal>nl</Taal>
                                            </Inhoud>
                                        </Vraag>
                                    </Vragen>
                                </Verzoek>
                            </web:GeefPersoon>
                        </soapenv:Body>
                    </soapenv:Envelope>
                    """).getXml()
        );
        assertNotNull(response);
        MagdaDocument magdaDocument = MagdaDocument.fromDocument(response);
        assertEquals("13001", magdaDocument.getValue("//Uitzonderingen/Uitzondering/Identificatie"));
    }

    @Test
    @SneakyThrows
    void whenTemplateDocumentFound_shouldReturnDocumentWithTemplateFilledIn() {
        MagdaMockConnection connection = MagdaMockConnection.create(createWireMockForTest(), new LenientSoapBodyValidator(), new LenientSoapBodyValidator());
        var response = connection.sendDocument(
                MagdaDocument.fromString("""
                        <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:web="http://magda.vlaanderen.be/persoon/soap/geefpersoon/v02_02">
                            <soapenv:Header/>
                            <soapenv:Body>
                                <web:GeefPersoon>
                                    <Verzoek>
                                        <Context>
                                            <Naam>GeefPersoon</Naam>
                                            <Versie>02.02.0000</Versie>
                                            <Bericht>
                                                <Type>VRAAG</Type>
                                                <Tijdstip>
                                                    <Datum>2022-02-02</Datum>
                                                </Tijdstip>
                                            </Bericht>
                                        </Context>
                                        <Vragen>
                                            <Vraag>
                                                <Referte>482d403a-22aa-11f1-a0f2-04cf4b22694c</Referte>
                                                <Inhoud>
                                                    <Criteria>
                                                        <INSZ>template</INSZ>
                                                    </Criteria>
                                                    <Bron>RR</Bron>
                                                    <Taal>nl</Taal>
                                                </Inhoud>
                                            </Vraag>
                                        </Vragen>
                                    </Verzoek>
                                </web:GeefPersoon>
                            </soapenv:Body>
                        </soapenv:Envelope>
                        """).getXml()
        );
        assertNotNull(response);
        MagdaDocument doc = MagdaDocument.fromDocument(response);
        String date = doc.getValue("//Context/Bericht/Tijdstip/Datum");
        LocalDate today = LocalDate.now();
        String formattedDate = today.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        assertEquals(formattedDate, date);
    }

    @Test
    @SneakyThrows
    void whenDocumentNotFound_shouldReturnNull() {
        MagdaMockConnection connection = MagdaMockConnection.create(createWireMockForTest(), new LenientSoapBodyValidator(), new LenientSoapBodyValidator());
        var response = connection.sendDocument(
                MagdaDocument.fromString("""
                        <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:web="http://magda.vlaanderen.be/persoon/soap/geefpersoon/v02_02">
                            <soapenv:Header/>
                            <soapenv:Body>
                                <web:GeefPersoon>
                                    <Verzoek>
                                        <Context>
                                            <Naam>GeefPersoon</Naam>
                                            <Versie>02.02.0000</Versie>
                                            <Bericht>
                                                <Type>VRAAG</Type>
                                                <Tijdstip>
                                                    <Datum>2022-02-02</Datum>
                                                </Tijdstip>
                                            </Bericht>
                                        </Context>
                                        <Vragen>
                                            <Vraag>
                                                <Referte>482d403a-22aa-11f1-a0f2-04cf4b22694c</Referte>
                                                <Inhoud>
                                                    <Criteria>
                                                        <INSZ>NOT_FOUND</INSZ>
                                                    </Criteria>
                                                    <Bron>RR</Bron>
                                                    <Taal>nl</Taal>
                                                </Inhoud>
                                            </Vraag>
                                        </Vragen>
                                    </Verzoek>
                                </web:GeefPersoon>
                            </soapenv:Body>
                        </soapenv:Envelope>
                        """).getXml()
        );
        assertNull(response);
    }

    @Test
    void whenRequestIsInvalid_shouldReturn400(){
        MagdaMockConnection connection = MagdaMockConnection.create(createWireMockForTest(), new LenientSoapBodyValidator(), new LenientSoapBodyValidator());
        var response = connection.sendRestRequest("/invalidResponse", "", "GET", """
                {"test": invalid json}
                """, "Tue, 29 Oct 2024 16:56:32 GMT", "");
        assertEquals(400, response.status());
        assertNotNull(response.headers().get("x-correlation-id"));
    }

    private WireMockData createWireMockForTest() {
        DirectCallHttpServerFactory factory = new DirectCallHttpServerFactory();

        WireMockConfiguration config = WireMockConfiguration.wireMockConfig()
                .port(0)
                .httpServerFactory(factory)
                .globalTemplating(true)
                .templatingEnabled(true)
                .extensions(MockDataTemplateHelper.getTemplateHelperExtensions());

        WireMockServer wireMockServer = new WireMockServer(config);
        wireMockServer.stubFor(
                get(urlEqualTo("/template/ok"))
                        .willReturn(aResponse()
                                .withBody("""
                                        {"test": "{{formatDate (dateMath (dateMath (parseDate request.headers.Date) '-10d') '-5y')}}"}
                                        """
                                )
                        ));
        wireMockServer.stubFor(
                get(urlEqualTo("/template/nok"))
                        .willReturn(aResponse()
                                .withBody("""
                                        {"test": "{formatDate (dateMath (dateMath (parseDate request.headers.Date) '-10d') '-5y')}}"}
                                        """
                                )
                        ));

        wireMockServer.stubFor(
                get(urlEqualTo("/invalidResponse"))
                        .willReturn(aResponse()
                                .withBody("invalid json"
                                )
                        ));

        wireMockServer.stubFor(
                post(urlEqualTo("/soap"))
                        .withRequestBody(matchingXPath("//Vraag/Inhoud/Criteria/INSZ/text()", containing("00631499723")))
                        .willReturn(
                                aResponse()
                                        .withStatus(200)
                                        .withBody("""
                                                <?xml version="1.0" encoding="utf-8"?>
                                                <web:GeefPersoonResponse xmlns:web="http://magda.vlaanderen.be/persoon/soap/geefpersoon/v02_02">
                                                  <Repliek>
                                                    <Context>
                                                      <Naam>GeefPersoon</Naam>
                                                      <Versie>02.02.0000</Versie>
                                                      <Bericht>
                                                        <Type>ANTWOORD</Type>
                                                        <Tijdstip>
                                                          <Datum>2021-07-14</Datum>
                                                          <Tijd>11:44:48.763</Tijd>
                                                        </Tijdstip>
                                                        <Afzender>
                                                          <Identificatie>vip.vlaanderen.be</Identificatie>
                                                          <Naam>MagdaGateway</Naam>
                                                          <Referte>3dfcbdf3-91fe-43cf-acf7-9e885fae67b1</Referte>
                                                        </Afzender>
                                                        <Ontvanger>
                                                          <Identificatie>kb.vlaanderen.be/aiv/burgerloket-wwoom-aip</Identificatie>
                                                          <Referte>9e751ab2-cfad-4591-8670-be57417f59bd</Referte>
                                                          <Hoedanigheid>1300</Hoedanigheid>
                                                        </Ontvanger>
                                                      </Bericht>
                                                    </Context>
                                                    <Antwoorden>
                                                      <Antwoord>
                                                        <Referte>71f7d90a-93f7-4cba-a1c8-56e580a48b73</Referte>
                                                        <Inhoud>
                                                          <Persoon Bron="RR" DatumCreatie="2000-03-14" DatumModificatie="2021-10-01">
                                                            <INSZ>00631499723</INSZ>
                                                            <Naam DatumBegin="2000-03-14">
                                                              <Achternamen>
                                                                <Achternaam>Konincks</Achternaam>
                                                              </Achternamen>
                                                              <Voornamen>
                                                                <Voornaam>Hugo</Voornaam>
                                                              </Voornamen>
                                                            </Naam>
                                                            <Geslacht>
                                                              <Code>1</Code>
                                                              <Omschrijving>Mannelijk</Omschrijving>
                                                            </Geslacht>
                                                            <Beheerder DatumBegin="2000-03-14">
                                                              <Plaats>
                                                                <Gemeente>
                                                                  <NISCode>73083</NISCode>
                                                                  <Naam>Tongeren</Naam>
                                                                </Gemeente>
                                                                <Land>
                                                                  <NISCode>150</NISCode>
                                                                  <ISOCode>BEL</ISOCode>
                                                                  <Naam>BELGIE</Naam>
                                                                </Land>
                                                              </Plaats>
                                                              <Fusie>
                                                                <Code>0</Code>
                                                                <Omschrijving>NEEN</Omschrijving>
                                                              </Fusie>
                                                              <Taalregime>
                                                                <Code>2</Code>
                                                                <Omschrijving>Nederlands</Omschrijving>
                                                              </Taalregime>
                                                            </Beheerder>
                                                            <Register DatumBegin="2000-03-14">
                                                              <Code>RR</Code>
                                                              <Omschrijving Taal="nl">Rijksregister</Omschrijving>
                                                            </Register>
                                                            <Nationaliteiten>
                                                              <Nationaliteit DatumBegin="2000-03-14">
                                                                <Code>150</Code>
                                                                <Omschrijving>Belg</Omschrijving>
                                                              </Nationaliteit>
                                                            </Nationaliteiten>
                                                            <Geboorte DatumBegin="2000-03-14">
                                                              <Datum>2000-03-14</Datum>
                                                            </Geboorte>
                                                            <Adressen>
                                                              <Hoofdverblijfplaats DatumBegin="2021-10-01">
                                                                <Straatnaam>
                                                                  <String>Wijngaardstraat</String>
                                                                  <Taal>nl</Taal>
                                                                </Straatnaam>
                                                                <Huisnummer>42</Huisnummer>
                                                                <Gemeentenaam>
                                                                  <String>Tongeren</String>
                                                                  <Taal>nl</Taal>
                                                                </Gemeentenaam>
                                                                <Land>
                                                                  <String>BELGIE</String>
                                                                  <Taal>nl</Taal>
                                                                </Land>
                                                                <Postcode>9000</Postcode>
                                                                <NISCodeLand>150</NISCodeLand>
                                                                <ISOCodeLand>BEL</ISOCodeLand>
                                                                <NISCodeGemeente>73083</NISCodeGemeente>
                                                              </Hoofdverblijfplaats>
                                                            </Adressen>
                                                            <Identiteitsbewijzen>
                                                              <Identiteitsbewijs DatumBegin="2021-03-01">
                                                                <Type>
                                                                  <Code>0000</Code>
                                                                  <Omschrijving Taal="nl">identiteitsbewijs van Belg</Omschrijving>
                                                                </Type>
                                                                <Nummer>699745854126</Nummer>
                                                              </Identiteitsbewijs>
                                                            </Identiteitsbewijzen>
                                                          </Persoon>
                                                        </Inhoud>
                                                      </Antwoord>
                                                    </Antwoorden>
                                                  </Repliek>
                                                </web:GeefPersoonResponse>
                                                """)
                        )
        );
        wireMockServer.stubFor(
                post(urlEqualTo("/soap"))
                        .withRequestBody(matchingXPath("//Vraag/Inhoud/Criteria/INSZ/text()", containing("template")))
                        .willReturn(
                                aResponse()
                                        .withStatus(200)
                                        .withBody("""
                                                <?xml version="1.0" encoding="utf-8"?>
                                                <web:GeefPersoonResponse xmlns:web="http://magda.vlaanderen.be/persoon/soap/geefpersoon/v02_02">
                                                  <Repliek>
                                                    <Context>
                                                      <Naam>GeefPersoon</Naam>
                                                      <Versie>02.02.0000</Versie>
                                                      <Bericht>
                                                        <Type>ANTWOORD</Type>
                                                        <Tijdstip>
                                                          <Datum>{{formatDate (dateMath (dateMath (parseDate request.headers.Date) '-10d') '-5y')}}</Datum>
                                                          <Tijd>11:44:48.763</Tijd>
                                                        </Tijdstip>
                                                        <Afzender>
                                                          <Identificatie>vip.vlaanderen.be</Identificatie>
                                                          <Naam>MagdaGateway</Naam>
                                                          <Referte>3dfcbdf3-91fe-43cf-acf7-9e885fae67b1</Referte>
                                                        </Afzender>
                                                        <Ontvanger>
                                                          <Identificatie>kb.vlaanderen.be/aiv/burgerloket-wwoom-aip</Identificatie>
                                                          <Referte>9e751ab2-cfad-4591-8670-be57417f59bd</Referte>
                                                          <Hoedanigheid>1300</Hoedanigheid>
                                                        </Ontvanger>
                                                      </Bericht>
                                                    </Context>
                                                    <Antwoorden>
                                                      <Antwoord>
                                                        <Referte>71f7d90a-93f7-4cba-a1c8-56e580a48b73</Referte>
                                                        <Inhoud>
                                                          <Persoon Bron="RR" DatumCreatie="2000-03-14" DatumModificatie="2021-10-01">
                                                            <INSZ>00631499723</INSZ>
                                                            <Naam DatumBegin="2000-03-14">
                                                              <Achternamen>
                                                                <Achternaam>Konincks</Achternaam>
                                                              </Achternamen>
                                                              <Voornamen>
                                                                <Voornaam>Hugo</Voornaam>
                                                              </Voornamen>
                                                            </Naam>
                                                            <Geslacht>
                                                              <Code>1</Code>
                                                              <Omschrijving>Mannelijk</Omschrijving>
                                                            </Geslacht>
                                                            <Beheerder DatumBegin="2000-03-14">
                                                              <Plaats>
                                                                <Gemeente>
                                                                  <NISCode>73083</NISCode>
                                                                  <Naam>Tongeren</Naam>
                                                                </Gemeente>
                                                                <Land>
                                                                  <NISCode>150</NISCode>
                                                                  <ISOCode>BEL</ISOCode>
                                                                  <Naam>BELGIE</Naam>
                                                                </Land>
                                                              </Plaats>
                                                              <Fusie>
                                                                <Code>0</Code>
                                                                <Omschrijving>NEEN</Omschrijving>
                                                              </Fusie>
                                                              <Taalregime>
                                                                <Code>2</Code>
                                                                <Omschrijving>Nederlands</Omschrijving>
                                                              </Taalregime>
                                                            </Beheerder>
                                                            <Register DatumBegin="2000-03-14">
                                                              <Code>RR</Code>
                                                              <Omschrijving Taal="nl">Rijksregister</Omschrijving>
                                                            </Register>
                                                            <Nationaliteiten>
                                                              <Nationaliteit DatumBegin="2000-03-14">
                                                                <Code>150</Code>
                                                                <Omschrijving>Belg</Omschrijving>
                                                              </Nationaliteit>
                                                            </Nationaliteiten>
                                                            <Geboorte DatumBegin="2000-03-14">
                                                              <Datum>2000-03-14</Datum>
                                                            </Geboorte>
                                                            <Adressen>
                                                              <Hoofdverblijfplaats DatumBegin="2021-10-01">
                                                                <Straatnaam>
                                                                  <String>Wijngaardstraat</String>
                                                                  <Taal>nl</Taal>
                                                                </Straatnaam>
                                                                <Huisnummer>42</Huisnummer>
                                                                <Gemeentenaam>
                                                                  <String>Tongeren</String>
                                                                  <Taal>nl</Taal>
                                                                </Gemeentenaam>
                                                                <Land>
                                                                  <String>BELGIE</String>
                                                                  <Taal>nl</Taal>
                                                                </Land>
                                                                <Postcode>9000</Postcode>
                                                                <NISCodeLand>150</NISCodeLand>
                                                                <ISOCodeLand>BEL</ISOCodeLand>
                                                                <NISCodeGemeente>73083</NISCodeGemeente>
                                                              </Hoofdverblijfplaats>
                                                            </Adressen>
                                                            <Identiteitsbewijzen>
                                                              <Identiteitsbewijs DatumBegin="2021-03-01">
                                                                <Type>
                                                                  <Code>0000</Code>
                                                                  <Omschrijving Taal="nl">identiteitsbewijs van Belg</Omschrijving>
                                                                </Type>
                                                                <Nummer>699745854126</Nummer>
                                                              </Identiteitsbewijs>
                                                            </Identiteitsbewijzen>
                                                          </Persoon>
                                                        </Inhoud>
                                                      </Antwoord>
                                                    </Antwoorden>
                                                  </Repliek>
                                                </web:GeefPersoonResponse>
                                                """)
                        )
        );

        return new WireMockData(wireMockServer, factory);
    }
}
