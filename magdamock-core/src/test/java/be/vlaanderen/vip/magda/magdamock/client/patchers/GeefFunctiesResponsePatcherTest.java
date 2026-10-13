package be.vlaanderen.vip.magda.magdamock.client.patchers;

import be.vlaanderen.vip.magda.magdamock.client.soap.ResponsePatcherTest;
import be.vlaanderen.vip.magda.magdamock.utils.MagdaMockDocument;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GeefFunctiesResponsePatcherTest extends ResponsePatcherTest {

    @Test
    void patchResponse_shouldFilterFunctiesBasedOnINSZ() throws Exception {
        String requestXml = """
                <Root>
                    <Context>
                    <Bericht>
                                            <Tijdstip>
                            <Datum>2026-07-20</Datum>
                            <Tijd>13:28:08.366</Tijd>
                        </Tijdstip>
                    </Bericht>
                
                    <Afzender>
                        <Referte>REQ-123</Referte>
                        <Identificatie>SENDER-ID</Identificatie>
                        <Hoedanigheid>SENDER-ROLE</Hoedanigheid>
                        <Gebruiker>bart.peeters</Gebruiker>
                    </Afzender>
                    </Context>
                    <Criteria>
                      <Persoon>
                          <INSZ>79650415395</INSZ>
                      </Persoon>
                    </Criteria>
                </Root>
                """;

        String responseXml = """
                                           <web:GeefFunctiesResponse xmlns:web="http://webservice.geeffunctiesdienst-02_00.onderneming-02_00.vip.vlaanderen.be">
                                           	<Repliek>
                                           		<Context>
                                           			<Naam>GeefFuncties</Naam>
                                           			<Versie>02.00.0000</Versie>
                                           			<Bericht>
                                           				<Type>ANTWOORD</Type>
                                           				<Tijdstip>
                                           					<Datum>0001-01-01</Datum>
                                           					<Tijd>00:00:00.000</Tijd>
                                           				</Tijdstip>
                                           				<Afzender>
                                           					<Identificatie>?</Identificatie>
                                           					<Naam>?</Naam>
                                           					<Referte>?</Referte>
                                           				</Afzender>
                                           				<Ontvanger>
                                           					<Identificatie>?</Identificatie>
                                           					<Referte>?</Referte>
                                           					<Hoedanigheid>?</Hoedanigheid>
                                           				</Ontvanger>
                                                       </Bericht>
                                                   </Context>
                                                   <Antwoorden>
                                                       <Antwoord>
                                                           <Referte>?</Referte>
                                           				<Inhoud>
                                           					<Functies>
                                           						<Functie DatumModificatie="2018-03-05">
                                           							<AuthentiekeBron>
                                           								<Code>03</Code>
                                           								<Omschrijving>KBO</Omschrijving>
                                           							</AuthentiekeBron>
                                           							<FunctiehouderVan>1999990243</FunctiehouderVan>
                                           							<AanduidingPersoonOnderneming>
                                           								<Code>1</Code>
                                           								<Omschrijving>Vertegenwoordiging door natuurlijke persoon</Omschrijving>
                                           							</AanduidingPersoonOnderneming>
                                           							<INSZ>79650415395</INSZ>
                                           							<VolgNummer>001</VolgNummer>
                                           							<AardFunctie>
                                           								<Code>10002</Code>
                                           								<Omschrijving>Bestuurder</Omschrijving>
                                           							</AardFunctie>
                                           							<Periode>
                                           								<Begin>2018-02-23</Begin>
                                           								<Einde>9999-12-31</Einde>
                                           							</Periode>
                                           							<AanduidingVrijstelling>
                                           								<Code>999</Code>
                                           								<Omschrijving>geen vrijstelling mogelijk</Omschrijving>
                                           							</AanduidingVrijstelling>
                                           						</Functie>
                                           						<Functie DatumModificatie="2021-09-06">
                                           							<AuthentiekeBron>
                                           								<Code>03</Code>
                                           								<Omschrijving>KBO</Omschrijving>
                                           							</AuthentiekeBron>
                                           							<FunctiehouderVan>1999990441</FunctiehouderVan>
                                           							<AanduidingPersoonOnderneming>
                                           								<Code>1</Code>
                                           								<Omschrijving>Vertegenwoordiging door natuurlijke persoon</Omschrijving>
                                           							</AanduidingPersoonOnderneming>
                                           							<INSZ>79650415395</INSZ>
                                           							<VolgNummer>001</VolgNummer>
                                           							<AardFunctie>
                                           								<Code>10002</Code>
                                           								<Omschrijving>Bestuurder</Omschrijving>
                                           							</AardFunctie>
                                           							<Periode>
                                           								<Begin>2021-09-06</Begin>
                                           								<Einde>9999-12-31</Einde>
                                           							</Periode>
                                           							<AanduidingVrijstelling>
                                           								<Code>999</Code>
                                           								<Omschrijving>geen vrijstelling mogelijk</Omschrijving>
                                           							</AanduidingVrijstelling>
                                           						</Functie>
                                           						<Functie DatumModificatie="2022-10-13">
                                           							<AuthentiekeBron>
                                           								<Code>03</Code>
                                           								<Omschrijving>KBO</Omschrijving>
                                           							</AuthentiekeBron>
                                           							<FunctiehouderVan>1999990441</FunctiehouderVan>
                                           							<AanduidingPersoonOnderneming>
                                           								<Code>1</Code>
                                           								<Omschrijving>Vertegenwoordiging door natuurlijke persoon</Omschrijving>
                                           							</AanduidingPersoonOnderneming>
                                           							<INSZ>79650415395</INSZ>
                                           							<VolgNummer>002</VolgNummer>
                                           							<AardFunctie>
                                           								<Code>10004</Code>
                                           								<Omschrijving>Persoon belast met dagelijks bestuur</Omschrijving>
                                           							</AardFunctie>
                                           							<Periode>
                                           								<Begin>2022-10-13</Begin>
                                           								<Einde>9999-12-31</Einde>
                                           							</Periode>
                                           							<AanduidingVrijstelling>
                                           								<Code>999</Code>
                                           								<Omschrijving>geen vrijstelling mogelijk</Omschrijving>
                                           							</AanduidingVrijstelling>
                                           						</Functie>
                                           						<Functie DatumModificatie="2014-08-12">
                                           							<AuthentiekeBron>
                                           								<Code>03</Code>
                                           								<Omschrijving>KBO</Omschrijving>
                                           							</AuthentiekeBron>
                                           							<FunctiehouderVan>1999990540</FunctiehouderVan>
                                           							<AanduidingPersoonOnderneming>
                                           								<Code>1</Code>
                                           								<Omschrijving>Vertegenwoordiging door natuurlijke persoon</Omschrijving>
                                           							</AanduidingPersoonOnderneming>
                                           							<VolgNummer>001</VolgNummer>
                                           							<INSZ>79650415395</INSZ>
                                           							<AardFunctie>
                                           								<Code>20017</Code>
                                           								<Omschrijving>Fotograaf</Omschrijving>
                                           							</AardFunctie>
                                           							<Periode>
                                           								<Begin>2014-08-12</Begin>
                                           								<Einde>9999-12-31</Einde>
                                           							</Periode>
                                           							<AanduidingVrijstelling>
                                           								<Code>999</Code>
                                           								<Omschrijving>geen vrijstelling mogelijk</Omschrijving>
                                           							</AanduidingVrijstelling>
                                           						</Functie>
                                           					</Functies>
                                           				</Inhoud>
                                           			</Antwoord>
                                           		</Antwoorden>
                                           	</Repliek>
                                           </web:GeefFunctiesResponse>
                """;

        MagdaMockDocument request = MagdaMockDocument.fromString(requestXml);
        Document response = parseXml(responseXml);

        GeefFunctiesResponsePatcher soapResponse =
                new GeefFunctiesResponsePatcher(fixedClock, () -> FIXED_UUID);

        MagdaMockDocument result = soapResponse.patchResponse(request, response);

        List<String> values = result.getValues("//AardFunctie/Code");
        assertEquals(4, values.size());
        assertEquals(List.of("10002", "10002", "10004", "20017"), values);

    }

    @Test
    void patchResponse_shouldFilterFunctiesBasedOnINSZAndOndernemingsnummer() throws Exception {
        String requestXml = """
                <Root>
                    <Context>
                    <Bericht>
                                            <Tijdstip>
                            <Datum>2026-07-20</Datum>
                            <Tijd>13:28:08.366</Tijd>
                        </Tijdstip>
                    </Bericht>
                
                    <Afzender>
                        <Referte>REQ-123</Referte>
                        <Identificatie>SENDER-ID</Identificatie>
                        <Hoedanigheid>SENDER-ROLE</Hoedanigheid>
                        <Gebruiker>bart.peeters</Gebruiker>
                    </Afzender>
                    </Context>
                    <Criteria>
                      <Persoon>
                            <INSZ>79650415395</INSZ>
                            <Ondernemingsnummer>1999990441</Ondernemingsnummer>
                      </Persoon>
                    </Criteria>
                </Root>
                """;

        String responseXml = """
                                           <web:GeefFunctiesResponse xmlns:web="http://webservice.geeffunctiesdienst-02_00.onderneming-02_00.vip.vlaanderen.be">
                                           	<Repliek>
                                           		<Context>
                                           			<Naam>GeefFuncties</Naam>
                                           			<Versie>02.00.0000</Versie>
                                           			<Bericht>
                                           				<Type>ANTWOORD</Type>
                                           				<Tijdstip>
                                           					<Datum>0001-01-01</Datum>
                                           					<Tijd>00:00:00.000</Tijd>
                                           				</Tijdstip>
                                           				<Afzender>
                                           					<Identificatie>?</Identificatie>
                                           					<Naam>?</Naam>
                                           					<Referte>?</Referte>
                                           				</Afzender>
                                           				<Ontvanger>
                                           					<Identificatie>?</Identificatie>
                                           					<Referte>?</Referte>
                                           					<Hoedanigheid>?</Hoedanigheid>
                                           				</Ontvanger>
                                                       </Bericht>
                                                   </Context>
                                                   <Antwoorden>
                                                       <Antwoord>
                                                           <Referte>?</Referte>
                                           				<Inhoud>
                                           					<Functies>
                                           						<Functie DatumModificatie="2018-03-05">
                                           							<AuthentiekeBron>
                                           								<Code>03</Code>
                                           								<Omschrijving>KBO</Omschrijving>
                                           							</AuthentiekeBron>
                                           							<FunctiehouderVan>1999990243</FunctiehouderVan>
                                           							<AanduidingPersoonOnderneming>
                                           								<Code>1</Code>
                                           								<Omschrijving>Vertegenwoordiging door natuurlijke persoon</Omschrijving>
                                           							</AanduidingPersoonOnderneming>
                                           							<INSZ>79650415395</INSZ>
                                           							<VolgNummer>001</VolgNummer>
                                           							<AardFunctie>
                                           								<Code>10002</Code>
                                           								<Omschrijving>Bestuurder</Omschrijving>
                                           							</AardFunctie>
                                           							<Periode>
                                           								<Begin>2018-02-23</Begin>
                                           								<Einde>9999-12-31</Einde>
                                           							</Periode>
                                           							<AanduidingVrijstelling>
                                           								<Code>999</Code>
                                           								<Omschrijving>geen vrijstelling mogelijk</Omschrijving>
                                           							</AanduidingVrijstelling>
                                           						</Functie>
                                           						<Functie DatumModificatie="2021-09-06">
                                           							<AuthentiekeBron>
                                           								<Code>03</Code>
                                           								<Omschrijving>KBO</Omschrijving>
                                           							</AuthentiekeBron>
                                           							<FunctiehouderVan>1999990441</FunctiehouderVan>
                                           							<AanduidingPersoonOnderneming>
                                           								<Code>1</Code>
                                           								<Omschrijving>Vertegenwoordiging door natuurlijke persoon</Omschrijving>
                                           							</AanduidingPersoonOnderneming>
                                           							<INSZ>79650415395</INSZ>
                                           							<VolgNummer>001</VolgNummer>
                                           							<AardFunctie>
                                           								<Code>10002</Code>
                                           								<Omschrijving>Bestuurder</Omschrijving>
                                           							</AardFunctie>
                                           							<Periode>
                                           								<Begin>2021-09-06</Begin>
                                           								<Einde>9999-12-31</Einde>
                                           							</Periode>
                                           							<AanduidingVrijstelling>
                                           								<Code>999</Code>
                                           								<Omschrijving>geen vrijstelling mogelijk</Omschrijving>
                                           							</AanduidingVrijstelling>
                                           						</Functie>
                                           						<Functie DatumModificatie="2022-10-13">
                                           							<AuthentiekeBron>
                                           								<Code>03</Code>
                                           								<Omschrijving>KBO</Omschrijving>
                                           							</AuthentiekeBron>
                                           							<FunctiehouderVan>1999990441</FunctiehouderVan>
                                           							<AanduidingPersoonOnderneming>
                                           								<Code>1</Code>
                                           								<Omschrijving>Vertegenwoordiging door natuurlijke persoon</Omschrijving>
                                           							</AanduidingPersoonOnderneming>
                                           							<INSZ>79650415395</INSZ>
                                           							<VolgNummer>002</VolgNummer>
                                           							<AardFunctie>
                                           								<Code>10004</Code>
                                           								<Omschrijving>Persoon belast met dagelijks bestuur</Omschrijving>
                                           							</AardFunctie>
                                           							<Periode>
                                           								<Begin>2022-10-13</Begin>
                                           								<Einde>9999-12-31</Einde>
                                           							</Periode>
                                           							<AanduidingVrijstelling>
                                           								<Code>999</Code>
                                           								<Omschrijving>geen vrijstelling mogelijk</Omschrijving>
                                           							</AanduidingVrijstelling>
                                           						</Functie>
                                           						<Functie DatumModificatie="2014-08-12">
                                           							<AuthentiekeBron>
                                           								<Code>03</Code>
                                           								<Omschrijving>KBO</Omschrijving>
                                           							</AuthentiekeBron>
                                           							<FunctiehouderVan>1999990540</FunctiehouderVan>
                                           							<AanduidingPersoonOnderneming>
                                           								<Code>1</Code>
                                           								<Omschrijving>Vertegenwoordiging door natuurlijke persoon</Omschrijving>
                                           							</AanduidingPersoonOnderneming>
                                           							<VolgNummer>001</VolgNummer>
                                           							<INSZ>79650415395</INSZ>
                                           							<AardFunctie>
                                           								<Code>20017</Code>
                                           								<Omschrijving>Fotograaf</Omschrijving>
                                           							</AardFunctie>
                                           							<Periode>
                                           								<Begin>2014-08-12</Begin>
                                           								<Einde>9999-12-31</Einde>
                                           							</Periode>
                                           							<AanduidingVrijstelling>
                                           								<Code>999</Code>
                                           								<Omschrijving>geen vrijstelling mogelijk</Omschrijving>
                                           							</AanduidingVrijstelling>
                                           						</Functie>
                                           					</Functies>
                                           				</Inhoud>
                                           			</Antwoord>
                                           		</Antwoorden>
                                           	</Repliek>
                                           </web:GeefFunctiesResponse>
                """;

        MagdaMockDocument request = MagdaMockDocument.fromString(requestXml);
        Document response = parseXml(responseXml);

        GeefFunctiesResponsePatcher soapResponse =
                new GeefFunctiesResponsePatcher(fixedClock, () -> FIXED_UUID);

        MagdaMockDocument result = soapResponse.patchResponse(request, response);

        List<String> values = result.getValues("//AardFunctie/Code");
        assertEquals(2, values.size());
        assertEquals(List.of("10002", "10004"), values);
    }

    @Test
    void patchResponse_shouldFilterFunctiesBasedOnINSZAndOndernemingsnummerAndAardFuncties() throws Exception {
        String requestXml = """
                <Root>
                    <Context>
                    <Bericht>
                                            <Tijdstip>
                            <Datum>2026-07-20</Datum>
                            <Tijd>13:28:08.366</Tijd>
                        </Tijdstip>
                    </Bericht>
                
                    <Afzender>
                        <Referte>REQ-123</Referte>
                        <Identificatie>SENDER-ID</Identificatie>
                        <Hoedanigheid>SENDER-ROLE</Hoedanigheid>
                        <Gebruiker>bart.peeters</Gebruiker>
                    </Afzender>
                    </Context>
                    <Criteria>
                      <Persoon>
                            <INSZ>79650415395</INSZ>
                            <Ondernemingsnummer>1999990441</Ondernemingsnummer>
                          <AardFuncties>
                              <AardFunctie>10004</AardFunctie>
                          </AardFuncties>
                      </Persoon>
                    </Criteria>
                </Root>
                """;

        String responseXml = """
                                           <web:GeefFunctiesResponse xmlns:web="http://webservice.geeffunctiesdienst-02_00.onderneming-02_00.vip.vlaanderen.be">
                                           	<Repliek>
                                           		<Context>
                                           			<Naam>GeefFuncties</Naam>
                                           			<Versie>02.00.0000</Versie>
                                           			<Bericht>
                                           				<Type>ANTWOORD</Type>
                                           				<Tijdstip>
                                           					<Datum>0001-01-01</Datum>
                                           					<Tijd>00:00:00.000</Tijd>
                                           				</Tijdstip>
                                           				<Afzender>
                                           					<Identificatie>?</Identificatie>
                                           					<Naam>?</Naam>
                                           					<Referte>?</Referte>
                                           				</Afzender>
                                           				<Ontvanger>
                                           					<Identificatie>?</Identificatie>
                                           					<Referte>?</Referte>
                                           					<Hoedanigheid>?</Hoedanigheid>
                                           				</Ontvanger>
                                                       </Bericht>
                                                   </Context>
                                                   <Antwoorden>
                                                       <Antwoord>
                                                           <Referte>?</Referte>
                                           				<Inhoud>
                                           					<Functies>
                                           						<Functie DatumModificatie="2018-03-05">
                                           							<AuthentiekeBron>
                                           								<Code>03</Code>
                                           								<Omschrijving>KBO</Omschrijving>
                                           							</AuthentiekeBron>
                                           							<FunctiehouderVan>1999990243</FunctiehouderVan>
                                           							<AanduidingPersoonOnderneming>
                                           								<Code>1</Code>
                                           								<Omschrijving>Vertegenwoordiging door natuurlijke persoon</Omschrijving>
                                           							</AanduidingPersoonOnderneming>
                                           							<INSZ>79650415395</INSZ>
                                           							<VolgNummer>001</VolgNummer>
                                           							<AardFunctie>
                                           								<Code>10002</Code>
                                           								<Omschrijving>Bestuurder</Omschrijving>
                                           							</AardFunctie>
                                           							<Periode>
                                           								<Begin>2018-02-23</Begin>
                                           								<Einde>9999-12-31</Einde>
                                           							</Periode>
                                           							<AanduidingVrijstelling>
                                           								<Code>999</Code>
                                           								<Omschrijving>geen vrijstelling mogelijk</Omschrijving>
                                           							</AanduidingVrijstelling>
                                           						</Functie>
                                           						<Functie DatumModificatie="2021-09-06">
                                           							<AuthentiekeBron>
                                           								<Code>03</Code>
                                           								<Omschrijving>KBO</Omschrijving>
                                           							</AuthentiekeBron>
                                           							<FunctiehouderVan>1999990441</FunctiehouderVan>
                                           							<AanduidingPersoonOnderneming>
                                           								<Code>1</Code>
                                           								<Omschrijving>Vertegenwoordiging door natuurlijke persoon</Omschrijving>
                                           							</AanduidingPersoonOnderneming>
                                           							<INSZ>79650415395</INSZ>
                                           							<VolgNummer>001</VolgNummer>
                                           							<AardFunctie>
                                           								<Code>10002</Code>
                                           								<Omschrijving>Bestuurder</Omschrijving>
                                           							</AardFunctie>
                                           							<Periode>
                                           								<Begin>2021-09-06</Begin>
                                           								<Einde>9999-12-31</Einde>
                                           							</Periode>
                                           							<AanduidingVrijstelling>
                                           								<Code>999</Code>
                                           								<Omschrijving>geen vrijstelling mogelijk</Omschrijving>
                                           							</AanduidingVrijstelling>
                                           						</Functie>
                                           						<Functie DatumModificatie="2022-10-13">
                                           							<AuthentiekeBron>
                                           								<Code>03</Code>
                                           								<Omschrijving>KBO</Omschrijving>
                                           							</AuthentiekeBron>
                                           							<FunctiehouderVan>1999990441</FunctiehouderVan>
                                           							<AanduidingPersoonOnderneming>
                                           								<Code>1</Code>
                                           								<Omschrijving>Vertegenwoordiging door natuurlijke persoon</Omschrijving>
                                           							</AanduidingPersoonOnderneming>
                                           							<INSZ>79650415395</INSZ>
                                           							<VolgNummer>002</VolgNummer>
                                           							<AardFunctie>
                                           								<Code>10004</Code>
                                           								<Omschrijving>Persoon belast met dagelijks bestuur</Omschrijving>
                                           							</AardFunctie>
                                           							<Periode>
                                           								<Begin>2022-10-13</Begin>
                                           								<Einde>9999-12-31</Einde>
                                           							</Periode>
                                           							<AanduidingVrijstelling>
                                           								<Code>999</Code>
                                           								<Omschrijving>geen vrijstelling mogelijk</Omschrijving>
                                           							</AanduidingVrijstelling>
                                           						</Functie>
                                           						<Functie DatumModificatie="2014-08-12">
                                           							<AuthentiekeBron>
                                           								<Code>03</Code>
                                           								<Omschrijving>KBO</Omschrijving>
                                           							</AuthentiekeBron>
                                           							<FunctiehouderVan>1999990540</FunctiehouderVan>
                                           							<AanduidingPersoonOnderneming>
                                           								<Code>1</Code>
                                           								<Omschrijving>Vertegenwoordiging door natuurlijke persoon</Omschrijving>
                                           							</AanduidingPersoonOnderneming>
                                           							<VolgNummer>001</VolgNummer>
                                           							<INSZ>79650415395</INSZ>
                                           							<AardFunctie>
                                           								<Code>20017</Code>
                                           								<Omschrijving>Fotograaf</Omschrijving>
                                           							</AardFunctie>
                                           							<Periode>
                                           								<Begin>2014-08-12</Begin>
                                           								<Einde>9999-12-31</Einde>
                                           							</Periode>
                                           							<AanduidingVrijstelling>
                                           								<Code>999</Code>
                                           								<Omschrijving>geen vrijstelling mogelijk</Omschrijving>
                                           							</AanduidingVrijstelling>
                                           						</Functie>
                                           					</Functies>
                                           				</Inhoud>
                                           			</Antwoord>
                                           		</Antwoorden>
                                           	</Repliek>
                                           </web:GeefFunctiesResponse>
                """;

        MagdaMockDocument request = MagdaMockDocument.fromString(requestXml);
        Document response = parseXml(responseXml);

        GeefFunctiesResponsePatcher soapResponse =
                new GeefFunctiesResponsePatcher(fixedClock, () -> FIXED_UUID);

        MagdaMockDocument result = soapResponse.patchResponse(request, response);

        List<String> values = result.getValues("//AardFunctie/Code");
        assertEquals(1, values.size());
        assertEquals(List.of("10004"), values);
    }

    @Test
    void patchResponse_shouldFilterFunctiesBasedOnINSZAndAardFuncties() throws Exception {
        String requestXml = """
                <Root>
                    <Context>
                    <Bericht>
                                            <Tijdstip>
                            <Datum>2026-07-20</Datum>
                            <Tijd>13:28:08.366</Tijd>
                        </Tijdstip>
                    </Bericht>
                
                    <Afzender>
                        <Referte>REQ-123</Referte>
                        <Identificatie>SENDER-ID</Identificatie>
                        <Hoedanigheid>SENDER-ROLE</Hoedanigheid>
                        <Gebruiker>bart.peeters</Gebruiker>
                    </Afzender>
                    </Context>
                    <Criteria>
                      <Persoon>
                            <INSZ>79650415395</INSZ>
                          <AardFuncties>
                              <AardFunctie>10004</AardFunctie>
                              <AardFunctie>20017</AardFunctie>
                          </AardFuncties>
                      </Persoon>
                    </Criteria>
                </Root>
                """;

        String responseXml = """
                                           <web:GeefFunctiesResponse xmlns:web="http://webservice.geeffunctiesdienst-02_00.onderneming-02_00.vip.vlaanderen.be">
                                           	<Repliek>
                                           		<Context>
                                           			<Naam>GeefFuncties</Naam>
                                           			<Versie>02.00.0000</Versie>
                                           			<Bericht>
                                           				<Type>ANTWOORD</Type>
                                           				<Tijdstip>
                                           					<Datum>0001-01-01</Datum>
                                           					<Tijd>00:00:00.000</Tijd>
                                           				</Tijdstip>
                                           				<Afzender>
                                           					<Identificatie>?</Identificatie>
                                           					<Naam>?</Naam>
                                           					<Referte>?</Referte>
                                           				</Afzender>
                                           				<Ontvanger>
                                           					<Identificatie>?</Identificatie>
                                           					<Referte>?</Referte>
                                           					<Hoedanigheid>?</Hoedanigheid>
                                           				</Ontvanger>
                                                       </Bericht>
                                                   </Context>
                                                   <Antwoorden>
                                                       <Antwoord>
                                                           <Referte>?</Referte>
                                           				<Inhoud>
                                           					<Functies>
                                           						<Functie DatumModificatie="2018-03-05">
                                           							<AuthentiekeBron>
                                           								<Code>03</Code>
                                           								<Omschrijving>KBO</Omschrijving>
                                           							</AuthentiekeBron>
                                           							<FunctiehouderVan>1999990243</FunctiehouderVan>
                                           							<AanduidingPersoonOnderneming>
                                           								<Code>1</Code>
                                           								<Omschrijving>Vertegenwoordiging door natuurlijke persoon</Omschrijving>
                                           							</AanduidingPersoonOnderneming>
                                           							<INSZ>79650415395</INSZ>
                                           							<VolgNummer>001</VolgNummer>
                                           							<AardFunctie>
                                           								<Code>10002</Code>
                                           								<Omschrijving>Bestuurder</Omschrijving>
                                           							</AardFunctie>
                                           							<Periode>
                                           								<Begin>2018-02-23</Begin>
                                           								<Einde>9999-12-31</Einde>
                                           							</Periode>
                                           							<AanduidingVrijstelling>
                                           								<Code>999</Code>
                                           								<Omschrijving>geen vrijstelling mogelijk</Omschrijving>
                                           							</AanduidingVrijstelling>
                                           						</Functie>
                                           						<Functie DatumModificatie="2021-09-06">
                                           							<AuthentiekeBron>
                                           								<Code>03</Code>
                                           								<Omschrijving>KBO</Omschrijving>
                                           							</AuthentiekeBron>
                                           							<FunctiehouderVan>1999990441</FunctiehouderVan>
                                           							<AanduidingPersoonOnderneming>
                                           								<Code>1</Code>
                                           								<Omschrijving>Vertegenwoordiging door natuurlijke persoon</Omschrijving>
                                           							</AanduidingPersoonOnderneming>
                                           							<INSZ>79650415395</INSZ>
                                           							<VolgNummer>001</VolgNummer>
                                           							<AardFunctie>
                                           								<Code>10002</Code>
                                           								<Omschrijving>Bestuurder</Omschrijving>
                                           							</AardFunctie>
                                           							<Periode>
                                           								<Begin>2021-09-06</Begin>
                                           								<Einde>9999-12-31</Einde>
                                           							</Periode>
                                           							<AanduidingVrijstelling>
                                           								<Code>999</Code>
                                           								<Omschrijving>geen vrijstelling mogelijk</Omschrijving>
                                           							</AanduidingVrijstelling>
                                           						</Functie>
                                           						<Functie DatumModificatie="2022-10-13">
                                           							<AuthentiekeBron>
                                           								<Code>03</Code>
                                           								<Omschrijving>KBO</Omschrijving>
                                           							</AuthentiekeBron>
                                           							<FunctiehouderVan>1999990441</FunctiehouderVan>
                                           							<AanduidingPersoonOnderneming>
                                           								<Code>1</Code>
                                           								<Omschrijving>Vertegenwoordiging door natuurlijke persoon</Omschrijving>
                                           							</AanduidingPersoonOnderneming>
                                           							<INSZ>79650415395</INSZ>
                                           							<VolgNummer>002</VolgNummer>
                                           							<AardFunctie>
                                           								<Code>10004</Code>
                                           								<Omschrijving>Persoon belast met dagelijks bestuur</Omschrijving>
                                           							</AardFunctie>
                                           							<Periode>
                                           								<Begin>2022-10-13</Begin>
                                           								<Einde>9999-12-31</Einde>
                                           							</Periode>
                                           							<AanduidingVrijstelling>
                                           								<Code>999</Code>
                                           								<Omschrijving>geen vrijstelling mogelijk</Omschrijving>
                                           							</AanduidingVrijstelling>
                                           						</Functie>
                                           						<Functie DatumModificatie="2014-08-12">
                                           							<AuthentiekeBron>
                                           								<Code>03</Code>
                                           								<Omschrijving>KBO</Omschrijving>
                                           							</AuthentiekeBron>
                                           							<FunctiehouderVan>1999990540</FunctiehouderVan>
                                           							<AanduidingPersoonOnderneming>
                                           								<Code>1</Code>
                                           								<Omschrijving>Vertegenwoordiging door natuurlijke persoon</Omschrijving>
                                           							</AanduidingPersoonOnderneming>
                                           							<VolgNummer>001</VolgNummer>
                                           							<INSZ>79650415395</INSZ>
                                           							<AardFunctie>
                                           								<Code>20017</Code>
                                           								<Omschrijving>Fotograaf</Omschrijving>
                                           							</AardFunctie>
                                           							<Periode>
                                           								<Begin>2014-08-12</Begin>
                                           								<Einde>9999-12-31</Einde>
                                           							</Periode>
                                           							<AanduidingVrijstelling>
                                           								<Code>999</Code>
                                           								<Omschrijving>geen vrijstelling mogelijk</Omschrijving>
                                           							</AanduidingVrijstelling>
                                           						</Functie>
                                           					</Functies>
                                           				</Inhoud>
                                           			</Antwoord>
                                           		</Antwoorden>
                                           	</Repliek>
                                           </web:GeefFunctiesResponse>
                """;

        MagdaMockDocument request = MagdaMockDocument.fromString(requestXml);
        Document response = parseXml(responseXml);

        GeefFunctiesResponsePatcher soapResponse =
                new GeefFunctiesResponsePatcher(fixedClock, () -> FIXED_UUID);

        MagdaMockDocument result = soapResponse.patchResponse(request, response);

        List<String> values = result.getValues("//AardFunctie/Code");
        assertEquals(2, values.size());
        assertEquals(List.of("10004", "20017"), values);
    }

    @Test
    void patchResponse_shouldFilterFunctiesBasedOnOndernemingsnummerFunctieIn() throws Exception {
        String requestXml = """
                <Root>
                    <Context>
                    <Bericht>
                                            <Tijdstip>
                            <Datum>2026-07-20</Datum>
                            <Tijd>13:28:08.366</Tijd>
                        </Tijdstip>
                    </Bericht>
                
                    <Afzender>
                        <Referte>REQ-123</Referte>
                        <Identificatie>SENDER-ID</Identificatie>
                        <Hoedanigheid>SENDER-ROLE</Hoedanigheid>
                        <Gebruiker>bart.peeters</Gebruiker>
                    </Afzender>
                    </Context>
                    <Criteria>
                      <Onderneming>
                          <Ondernemingsnummer Type="FunctieIn">1999990639</Ondernemingsnummer>
                      </Onderneming>
                    </Criteria>
                </Root>
                """;

        String responseXml = """
                <?xml version="1.0" encoding="UTF-8" standalone="no"?>
                <web:GeefFunctiesResponse xmlns:web="http://webservice.geeffunctiesdienst-02_00.onderneming-02_00.vip.vlaanderen.be">
                	<Repliek>
                		<Context>
                			<Naam>GeefFuncties</Naam>
                			<Versie>02.00.0000</Versie>
                			<Bericht>
                				<Type>ANTWOORD</Type>
                				<Tijdstip>
                					<Datum>0001-01-01</Datum>
                					<Tijd>00:00:00.000</Tijd>
                				</Tijdstip>
                				<Afzender>
                					<Identificatie>?</Identificatie>
                					<Naam>?</Naam>
                					<Referte>?</Referte>
                				</Afzender>
                				<Ontvanger>
                					<Identificatie>?</Identificatie>
                					<Referte>?</Referte>
                					<Hoedanigheid>?</Hoedanigheid>
                				</Ontvanger>
                            </Bericht>
                        </Context>
                        <Antwoorden>
                            <Antwoord>
                                <Referte>?</Referte>
                				<Inhoud>
                					<Functies>
                						<Functie DatumModificatie="2025-01-10">
                							<AuthentiekeBron>
                								<Code>03</Code>
                								<Omschrijving>KBO</Omschrijving>
                							</AuthentiekeBron>
                							<FunctiehouderVan>1999990639</FunctiehouderVan>
                							<AanduidingPersoonOnderneming>
                								<Code>2</Code>
                								<Omschrijving>Vertegenwoordiging door onderneming</Omschrijving>
                							</AanduidingPersoonOnderneming>
                							<Ondernemingsnummer>1999990138</Ondernemingsnummer>
                							<VolgNummer>001</VolgNummer>
                							<AardFunctie>
                								<Code>10002</Code>
                								<Omschrijving>Bestuurder</Omschrijving>
                							</AardFunctie>
                							<Periode>
                								<Begin>2025-01-10</Begin>
                								<Einde>9999-12-31</Einde>
                							</Periode>
                							<AanduidingVrijstelling>
                								<Code>999</Code>
                								<Omschrijving>geen vrijstelling mogelijk</Omschrijving>
                							</AanduidingVrijstelling>
                						</Functie>
                						<Functie DatumModificatie="2025-01-10">
                							<AuthentiekeBron>
                								<Code>03</Code>
                								<Omschrijving>KBO</Omschrijving>
                							</AuthentiekeBron>
                							<FunctiehouderVan>1999990639</FunctiehouderVan>
                							<AanduidingPersoonOnderneming>
                								<Code>2</Code>
                								<Omschrijving>Vertegenwoordiging door onderneming</Omschrijving>
                							</AanduidingPersoonOnderneming>
                							<Ondernemingsnummer>1999990342</Ondernemingsnummer>
                							<VolgNummer>002</VolgNummer>
                							<AardFunctie>
                								<Code>10002</Code>
                								<Omschrijving>Bestuurder</Omschrijving>
                							</AardFunctie>
                							<Periode>
                								<Begin>2025-01-10</Begin>
                								<Einde>9999-12-31</Einde>
                							</Periode>
                							<AanduidingVrijstelling>
                								<Code>999</Code>
                								<Omschrijving>geen vrijstelling mogelijk</Omschrijving>
                							</AanduidingVrijstelling>
                						</Functie>
                						<Functie DatumModificatie="2025-01-10">
                							<AuthentiekeBron>
                								<Code>03</Code>
                								<Omschrijving>KBO</Omschrijving>
                							</AuthentiekeBron>
                							<FunctiehouderVan>1999990639</FunctiehouderVan>
                							<AanduidingPersoonOnderneming>
                								<Code>2</Code>
                								<Omschrijving>Vertegenwoordiging door onderneming</Omschrijving>
                							</AanduidingPersoonOnderneming>
                							<Ondernemingsnummer>1999990441</Ondernemingsnummer>
                							<VolgNummer>003</VolgNummer>
                							<AardFunctie>
                								<Code>10002</Code>
                								<Omschrijving>Bestuurder</Omschrijving>
                							</AardFunctie>
                							<Periode>
                								<Begin>2025-01-10</Begin>
                								<Einde>9999-12-31</Einde>
                							</Periode>
                							<AanduidingVrijstelling>
                								<Code>999</Code>
                								<Omschrijving>geen vrijstelling mogelijk</Omschrijving>
                							</AanduidingVrijstelling>
                						</Functie>
                						<Functie DatumModificatie="2025-01-10">
                							<AuthentiekeBron>
                								<Code>03</Code>
                								<Omschrijving>KBO</Omschrijving>
                							</AuthentiekeBron>
                							<FunctiehouderVan>1999990639</FunctiehouderVan>
                							<AanduidingPersoonOnderneming>
                								<Code>3</Code>
                								<Omschrijving>Vertegenwoordiging door natuurlijke persoon en onderneming</Omschrijving>
                							</AanduidingPersoonOnderneming>
                							<INSZ>69681374910</INSZ>
                							<Ondernemingsnummer>1999990138</Ondernemingsnummer>
                							<VolgNummer>001</VolgNummer>
                							<AardFunctie>
                								<Code>10003</Code>
                								<Omschrijving>Vaste vertegenwoordiger</Omschrijving>
                							</AardFunctie>
                							<Periode>
                								<Begin>2025-01-10</Begin>
                								<Einde>9999-12-31</Einde>
                							</Periode>
                							<AanduidingVrijstelling>
                								<Code>999</Code>
                								<Omschrijving>geen vrijstelling mogelijk</Omschrijving>
                							</AanduidingVrijstelling>
                						</Functie>
                						<Functie DatumModificatie="2025-01-10">
                							<AuthentiekeBron>
                								<Code>03</Code>
                								<Omschrijving>KBO</Omschrijving>
                							</AuthentiekeBron>
                							<FunctiehouderVan>1999990639</FunctiehouderVan>
                							<AanduidingPersoonOnderneming>
                								<Code>3</Code>
                								<Omschrijving>Vertegenwoordiging door natuurlijke persoon en onderneming</Omschrijving>
                							</AanduidingPersoonOnderneming>
                							<INSZ>91712585131</INSZ>
                							<Ondernemingsnummer>1999990441</Ondernemingsnummer>
                							<VolgNummer>002</VolgNummer>
                							<AardFunctie>
                								<Code>10003</Code>
                								<Omschrijving>Vaste vertegenwoordiger</Omschrijving>
                							</AardFunctie>
                							<Periode>
                								<Begin>2025-01-10</Begin>
                								<Einde>9999-12-31</Einde>
                							</Periode>
                							<AanduidingVrijstelling>
                								<Code>999</Code>
                								<Omschrijving>geen vrijstelling mogelijk</Omschrijving>
                							</AanduidingVrijstelling>
                						</Functie>
                						<Functie DatumModificatie="2025-01-10">
                							<AuthentiekeBron>
                								<Code>03</Code>
                								<Omschrijving>KBO</Omschrijving>
                							</AuthentiekeBron>
                							<FunctiehouderVan>1999990639</FunctiehouderVan>
                							<AanduidingPersoonOnderneming>
                								<Code>3</Code>
                								<Omschrijving>Vertegenwoordiging door natuurlijke persoon en onderneming</Omschrijving>
                							</AanduidingPersoonOnderneming>
                							<INSZ>86692115747</INSZ>
                							<Ondernemingsnummer>1999990342</Ondernemingsnummer>
                							<VolgNummer>003</VolgNummer>
                							<AardFunctie>
                								<Code>10003</Code>
                								<Omschrijving>Vaste vertegenwoordiger</Omschrijving>
                							</AardFunctie>
                							<Periode>
                								<Begin>2025-01-10</Begin>
                								<Einde>9999-12-31</Einde>
                							</Periode>
                							<AanduidingVrijstelling>
                								<Code>999</Code>
                								<Omschrijving>geen vrijstelling mogelijk</Omschrijving>
                							</AanduidingVrijstelling>
                						</Functie>						
                						<Functie DatumModificatie="2026-03-15">
                							<AuthentiekeBron>
                								<Code>03</Code>
                								<Omschrijving>KBO</Omschrijving>
                							</AuthentiekeBron>
                							<FunctiehouderVan>1999990738</FunctiehouderVan>
                							<AanduidingPersoonOnderneming>
                								<Code>2</Code>
                								<Omschrijving>Vertegenwoordiging door onderneming</Omschrijving>
                							</AanduidingPersoonOnderneming>
                							<Ondernemingsnummer>1999990639</Ondernemingsnummer>
                							<VolgNummer>001</VolgNummer>
                							<AardFunctie>
                								<Code>10002</Code>
                								<Omschrijving>Bestuurder</Omschrijving>
                							</AardFunctie>
                							<Periode>
                								<Begin>2026-03-15</Begin>
                								<Einde>9999-12-31</Einde>
                							</Periode>
                							<AanduidingVrijstelling>
                								<Code>999</Code>
                								<Omschrijving>geen vrijstelling mogelijk</Omschrijving>
                							</AanduidingVrijstelling>
                						</Functie>
                					</Functies>
                				</Inhoud>
                			</Antwoord>
                		</Antwoorden>
                	</Repliek>
                </web:GeefFunctiesResponse>
                """;

        MagdaMockDocument request = MagdaMockDocument.fromString(requestXml);
        Document response = parseXml(responseXml);

        GeefFunctiesResponsePatcher soapResponse =
                new GeefFunctiesResponsePatcher(fixedClock, () -> FIXED_UUID);

        MagdaMockDocument result = soapResponse.patchResponse(request, response);

        List<String> values = result.getValues("//AardFunctie/Code");
        assertEquals(6, values.size());
        assertEquals(List.of("10002", "10002", "10002", "10003", "10003", "10003"), values);
    }


    @Test
    void patchResponse_shouldFilterFunctiesBasedOnOndernemingsnummerFunctieHouder() throws Exception {
        String requestXml = """
                <Root>
                    <Context>
                    <Bericht>
                                            <Tijdstip>
                            <Datum>2026-07-20</Datum>
                            <Tijd>13:28:08.366</Tijd>
                        </Tijdstip>
                    </Bericht>
                
                    <Afzender>
                        <Referte>REQ-123</Referte>
                        <Identificatie>SENDER-ID</Identificatie>
                        <Hoedanigheid>SENDER-ROLE</Hoedanigheid>
                        <Gebruiker>bart.peeters</Gebruiker>
                    </Afzender>
                    </Context>
                    <Criteria>
                      <Onderneming>
                          <Ondernemingsnummer Type="FunctieHouder">1999990639</Ondernemingsnummer>
                      </Onderneming>
                    </Criteria>
                </Root>
                """;

        String responseXml = """
                <?xml version="1.0" encoding="UTF-8" standalone="no"?>
                <web:GeefFunctiesResponse xmlns:web="http://webservice.geeffunctiesdienst-02_00.onderneming-02_00.vip.vlaanderen.be">
                	<Repliek>
                		<Context>
                			<Naam>GeefFuncties</Naam>
                			<Versie>02.00.0000</Versie>
                			<Bericht>
                				<Type>ANTWOORD</Type>
                				<Tijdstip>
                					<Datum>0001-01-01</Datum>
                					<Tijd>00:00:00.000</Tijd>
                				</Tijdstip>
                				<Afzender>
                					<Identificatie>?</Identificatie>
                					<Naam>?</Naam>
                					<Referte>?</Referte>
                				</Afzender>
                				<Ontvanger>
                					<Identificatie>?</Identificatie>
                					<Referte>?</Referte>
                					<Hoedanigheid>?</Hoedanigheid>
                				</Ontvanger>
                            </Bericht>
                        </Context>
                        <Antwoorden>
                            <Antwoord>
                                <Referte>?</Referte>
                				<Inhoud>
                					<Functies>
                						<Functie DatumModificatie="2025-01-10">
                							<AuthentiekeBron>
                								<Code>03</Code>
                								<Omschrijving>KBO</Omschrijving>
                							</AuthentiekeBron>
                							<FunctiehouderVan>1999990639</FunctiehouderVan>
                							<AanduidingPersoonOnderneming>
                								<Code>2</Code>
                								<Omschrijving>Vertegenwoordiging door onderneming</Omschrijving>
                							</AanduidingPersoonOnderneming>
                							<Ondernemingsnummer>1999990138</Ondernemingsnummer>
                							<VolgNummer>001</VolgNummer>
                							<AardFunctie>
                								<Code>10002</Code>
                								<Omschrijving>Bestuurder</Omschrijving>
                							</AardFunctie>
                							<Periode>
                								<Begin>2025-01-10</Begin>
                								<Einde>9999-12-31</Einde>
                							</Periode>
                							<AanduidingVrijstelling>
                								<Code>999</Code>
                								<Omschrijving>geen vrijstelling mogelijk</Omschrijving>
                							</AanduidingVrijstelling>
                						</Functie>
                						<Functie DatumModificatie="2025-01-10">
                							<AuthentiekeBron>
                								<Code>03</Code>
                								<Omschrijving>KBO</Omschrijving>
                							</AuthentiekeBron>
                							<FunctiehouderVan>1999990639</FunctiehouderVan>
                							<AanduidingPersoonOnderneming>
                								<Code>2</Code>
                								<Omschrijving>Vertegenwoordiging door onderneming</Omschrijving>
                							</AanduidingPersoonOnderneming>
                							<Ondernemingsnummer>1999990342</Ondernemingsnummer>
                							<VolgNummer>002</VolgNummer>
                							<AardFunctie>
                								<Code>10002</Code>
                								<Omschrijving>Bestuurder</Omschrijving>
                							</AardFunctie>
                							<Periode>
                								<Begin>2025-01-10</Begin>
                								<Einde>9999-12-31</Einde>
                							</Periode>
                							<AanduidingVrijstelling>
                								<Code>999</Code>
                								<Omschrijving>geen vrijstelling mogelijk</Omschrijving>
                							</AanduidingVrijstelling>
                						</Functie>
                						<Functie DatumModificatie="2025-01-10">
                							<AuthentiekeBron>
                								<Code>03</Code>
                								<Omschrijving>KBO</Omschrijving>
                							</AuthentiekeBron>
                							<FunctiehouderVan>1999990639</FunctiehouderVan>
                							<AanduidingPersoonOnderneming>
                								<Code>2</Code>
                								<Omschrijving>Vertegenwoordiging door onderneming</Omschrijving>
                							</AanduidingPersoonOnderneming>
                							<Ondernemingsnummer>1999990441</Ondernemingsnummer>
                							<VolgNummer>003</VolgNummer>
                							<AardFunctie>
                								<Code>10002</Code>
                								<Omschrijving>Bestuurder</Omschrijving>
                							</AardFunctie>
                							<Periode>
                								<Begin>2025-01-10</Begin>
                								<Einde>9999-12-31</Einde>
                							</Periode>
                							<AanduidingVrijstelling>
                								<Code>999</Code>
                								<Omschrijving>geen vrijstelling mogelijk</Omschrijving>
                							</AanduidingVrijstelling>
                						</Functie>
                						<Functie DatumModificatie="2025-01-10">
                							<AuthentiekeBron>
                								<Code>03</Code>
                								<Omschrijving>KBO</Omschrijving>
                							</AuthentiekeBron>
                							<FunctiehouderVan>1999990639</FunctiehouderVan>
                							<AanduidingPersoonOnderneming>
                								<Code>3</Code>
                								<Omschrijving>Vertegenwoordiging door natuurlijke persoon en onderneming</Omschrijving>
                							</AanduidingPersoonOnderneming>
                							<INSZ>69681374910</INSZ>
                							<Ondernemingsnummer>1999990138</Ondernemingsnummer>
                							<VolgNummer>001</VolgNummer>
                							<AardFunctie>
                								<Code>10003</Code>
                								<Omschrijving>Vaste vertegenwoordiger</Omschrijving>
                							</AardFunctie>
                							<Periode>
                								<Begin>2025-01-10</Begin>
                								<Einde>9999-12-31</Einde>
                							</Periode>
                							<AanduidingVrijstelling>
                								<Code>999</Code>
                								<Omschrijving>geen vrijstelling mogelijk</Omschrijving>
                							</AanduidingVrijstelling>
                						</Functie>
                						<Functie DatumModificatie="2025-01-10">
                							<AuthentiekeBron>
                								<Code>03</Code>
                								<Omschrijving>KBO</Omschrijving>
                							</AuthentiekeBron>
                							<FunctiehouderVan>1999990639</FunctiehouderVan>
                							<AanduidingPersoonOnderneming>
                								<Code>3</Code>
                								<Omschrijving>Vertegenwoordiging door natuurlijke persoon en onderneming</Omschrijving>
                							</AanduidingPersoonOnderneming>
                							<INSZ>91712585131</INSZ>
                							<Ondernemingsnummer>1999990441</Ondernemingsnummer>
                							<VolgNummer>002</VolgNummer>
                							<AardFunctie>
                								<Code>10003</Code>
                								<Omschrijving>Vaste vertegenwoordiger</Omschrijving>
                							</AardFunctie>
                							<Periode>
                								<Begin>2025-01-10</Begin>
                								<Einde>9999-12-31</Einde>
                							</Periode>
                							<AanduidingVrijstelling>
                								<Code>999</Code>
                								<Omschrijving>geen vrijstelling mogelijk</Omschrijving>
                							</AanduidingVrijstelling>
                						</Functie>
                						<Functie DatumModificatie="2025-01-10">
                							<AuthentiekeBron>
                								<Code>03</Code>
                								<Omschrijving>KBO</Omschrijving>
                							</AuthentiekeBron>
                							<FunctiehouderVan>1999990639</FunctiehouderVan>
                							<AanduidingPersoonOnderneming>
                								<Code>3</Code>
                								<Omschrijving>Vertegenwoordiging door natuurlijke persoon en onderneming</Omschrijving>
                							</AanduidingPersoonOnderneming>
                							<INSZ>86692115747</INSZ>
                							<Ondernemingsnummer>1999990342</Ondernemingsnummer>
                							<VolgNummer>003</VolgNummer>
                							<AardFunctie>
                								<Code>10003</Code>
                								<Omschrijving>Vaste vertegenwoordiger</Omschrijving>
                							</AardFunctie>
                							<Periode>
                								<Begin>2025-01-10</Begin>
                								<Einde>9999-12-31</Einde>
                							</Periode>
                							<AanduidingVrijstelling>
                								<Code>999</Code>
                								<Omschrijving>geen vrijstelling mogelijk</Omschrijving>
                							</AanduidingVrijstelling>
                						</Functie>						
                						<Functie DatumModificatie="2026-03-15">
                							<AuthentiekeBron>
                								<Code>03</Code>
                								<Omschrijving>KBO</Omschrijving>
                							</AuthentiekeBron>
                							<FunctiehouderVan>1999990738</FunctiehouderVan>
                							<AanduidingPersoonOnderneming>
                								<Code>2</Code>
                								<Omschrijving>Vertegenwoordiging door onderneming</Omschrijving>
                							</AanduidingPersoonOnderneming>
                							<Ondernemingsnummer>1999990639</Ondernemingsnummer>
                							<VolgNummer>001</VolgNummer>
                							<AardFunctie>
                								<Code>10002</Code>
                								<Omschrijving>Bestuurder</Omschrijving>
                							</AardFunctie>
                							<Periode>
                								<Begin>2026-03-15</Begin>
                								<Einde>9999-12-31</Einde>
                							</Periode>
                							<AanduidingVrijstelling>
                								<Code>999</Code>
                								<Omschrijving>geen vrijstelling mogelijk</Omschrijving>
                							</AanduidingVrijstelling>
                						</Functie>
                					</Functies>
                				</Inhoud>
                			</Antwoord>
                		</Antwoorden>
                	</Repliek>
                </web:GeefFunctiesResponse>
                """;

        MagdaMockDocument request = MagdaMockDocument.fromString(requestXml);
        Document response = parseXml(responseXml);

        GeefFunctiesResponsePatcher soapResponse =
                new GeefFunctiesResponsePatcher(fixedClock, () -> FIXED_UUID);

        MagdaMockDocument result = soapResponse.patchResponse(request, response);

        List<String> values = result.getValues("//AardFunctie/Code");
        assertEquals(1, values.size());
        assertEquals(List.of("10002"), values);
    }

    @Test
    void patchResponse_shouldFilterFunctiesBasedOnOndernemingsnummerFunctieInAndFunctieHouderAndAardFuncties() throws Exception {
        String requestXml = """
                <Root>
                    <Context>
                    <Bericht>
                                            <Tijdstip>
                            <Datum>2026-07-20</Datum>
                            <Tijd>13:28:08.366</Tijd>
                        </Tijdstip>
                    </Bericht>
                
                    <Afzender>
                        <Referte>REQ-123</Referte>
                        <Identificatie>SENDER-ID</Identificatie>
                        <Hoedanigheid>SENDER-ROLE</Hoedanigheid>
                        <Gebruiker>bart.peeters</Gebruiker>
                    </Afzender>
                    </Context>
                    <Criteria>
                      <Onderneming>
                          <Ondernemingsnummer Type="FunctieIn">1999990639</Ondernemingsnummer>
                          <Ondernemingsnummer Type="FunctieHouder">1999990441</Ondernemingsnummer>
                          <AardFuncties>
                              <AardFunctie>10002</AardFunctie>
                          </AardFuncties>
                      </Onderneming>
                    </Criteria>
                </Root>
                """;

        String responseXml = """
                <?xml version="1.0" encoding="UTF-8" standalone="no"?>
                <web:GeefFunctiesResponse xmlns:web="http://webservice.geeffunctiesdienst-02_00.onderneming-02_00.vip.vlaanderen.be">
                	<Repliek>
                		<Context>
                			<Naam>GeefFuncties</Naam>
                			<Versie>02.00.0000</Versie>
                			<Bericht>
                				<Type>ANTWOORD</Type>
                				<Tijdstip>
                					<Datum>0001-01-01</Datum>
                					<Tijd>00:00:00.000</Tijd>
                				</Tijdstip>
                				<Afzender>
                					<Identificatie>?</Identificatie>
                					<Naam>?</Naam>
                					<Referte>?</Referte>
                				</Afzender>
                				<Ontvanger>
                					<Identificatie>?</Identificatie>
                					<Referte>?</Referte>
                					<Hoedanigheid>?</Hoedanigheid>
                				</Ontvanger>
                            </Bericht>
                        </Context>
                        <Antwoorden>
                            <Antwoord>
                                <Referte>?</Referte>
                				<Inhoud>
                					<Functies>
                						<Functie DatumModificatie="2025-01-10">
                							<AuthentiekeBron>
                								<Code>03</Code>
                								<Omschrijving>KBO</Omschrijving>
                							</AuthentiekeBron>
                							<FunctiehouderVan>1999990639</FunctiehouderVan>
                							<AanduidingPersoonOnderneming>
                								<Code>2</Code>
                								<Omschrijving>Vertegenwoordiging door onderneming</Omschrijving>
                							</AanduidingPersoonOnderneming>
                							<Ondernemingsnummer>1999990138</Ondernemingsnummer>
                							<VolgNummer>001</VolgNummer>
                							<AardFunctie>
                								<Code>10002</Code>
                								<Omschrijving>Bestuurder</Omschrijving>
                							</AardFunctie>
                							<Periode>
                								<Begin>2025-01-10</Begin>
                								<Einde>9999-12-31</Einde>
                							</Periode>
                							<AanduidingVrijstelling>
                								<Code>999</Code>
                								<Omschrijving>geen vrijstelling mogelijk</Omschrijving>
                							</AanduidingVrijstelling>
                						</Functie>
                						<Functie DatumModificatie="2025-01-10">
                							<AuthentiekeBron>
                								<Code>03</Code>
                								<Omschrijving>KBO</Omschrijving>
                							</AuthentiekeBron>
                							<FunctiehouderVan>1999990639</FunctiehouderVan>
                							<AanduidingPersoonOnderneming>
                								<Code>2</Code>
                								<Omschrijving>Vertegenwoordiging door onderneming</Omschrijving>
                							</AanduidingPersoonOnderneming>
                							<Ondernemingsnummer>1999990342</Ondernemingsnummer>
                							<VolgNummer>002</VolgNummer>
                							<AardFunctie>
                								<Code>10002</Code>
                								<Omschrijving>Bestuurder</Omschrijving>
                							</AardFunctie>
                							<Periode>
                								<Begin>2025-01-10</Begin>
                								<Einde>9999-12-31</Einde>
                							</Periode>
                							<AanduidingVrijstelling>
                								<Code>999</Code>
                								<Omschrijving>geen vrijstelling mogelijk</Omschrijving>
                							</AanduidingVrijstelling>
                						</Functie>
                						<Functie DatumModificatie="2025-01-10">
                							<AuthentiekeBron>
                								<Code>03</Code>
                								<Omschrijving>KBO</Omschrijving>
                							</AuthentiekeBron>
                							<FunctiehouderVan>1999990639</FunctiehouderVan>
                							<AanduidingPersoonOnderneming>
                								<Code>2</Code>
                								<Omschrijving>Vertegenwoordiging door onderneming</Omschrijving>
                							</AanduidingPersoonOnderneming>
                							<Ondernemingsnummer>1999990441</Ondernemingsnummer>
                							<VolgNummer>003</VolgNummer>
                							<AardFunctie>
                								<Code>10002</Code>
                								<Omschrijving>Bestuurder</Omschrijving>
                							</AardFunctie>
                							<Periode>
                								<Begin>2025-01-10</Begin>
                								<Einde>9999-12-31</Einde>
                							</Periode>
                							<AanduidingVrijstelling>
                								<Code>999</Code>
                								<Omschrijving>geen vrijstelling mogelijk</Omschrijving>
                							</AanduidingVrijstelling>
                						</Functie>
                						<Functie DatumModificatie="2025-01-10">
                							<AuthentiekeBron>
                								<Code>03</Code>
                								<Omschrijving>KBO</Omschrijving>
                							</AuthentiekeBron>
                							<FunctiehouderVan>1999990639</FunctiehouderVan>
                							<AanduidingPersoonOnderneming>
                								<Code>3</Code>
                								<Omschrijving>Vertegenwoordiging door natuurlijke persoon en onderneming</Omschrijving>
                							</AanduidingPersoonOnderneming>
                							<INSZ>69681374910</INSZ>
                							<Ondernemingsnummer>1999990138</Ondernemingsnummer>
                							<VolgNummer>001</VolgNummer>
                							<AardFunctie>
                								<Code>10003</Code>
                								<Omschrijving>Vaste vertegenwoordiger</Omschrijving>
                							</AardFunctie>
                							<Periode>
                								<Begin>2025-01-10</Begin>
                								<Einde>9999-12-31</Einde>
                							</Periode>
                							<AanduidingVrijstelling>
                								<Code>999</Code>
                								<Omschrijving>geen vrijstelling mogelijk</Omschrijving>
                							</AanduidingVrijstelling>
                						</Functie>
                						<Functie DatumModificatie="2025-01-10">
                							<AuthentiekeBron>
                								<Code>03</Code>
                								<Omschrijving>KBO</Omschrijving>
                							</AuthentiekeBron>
                							<FunctiehouderVan>1999990639</FunctiehouderVan>
                							<AanduidingPersoonOnderneming>
                								<Code>3</Code>
                								<Omschrijving>Vertegenwoordiging door natuurlijke persoon en onderneming</Omschrijving>
                							</AanduidingPersoonOnderneming>
                							<INSZ>91712585131</INSZ>
                							<Ondernemingsnummer>1999990441</Ondernemingsnummer>
                							<VolgNummer>002</VolgNummer>
                							<AardFunctie>
                								<Code>10003</Code>
                								<Omschrijving>Vaste vertegenwoordiger</Omschrijving>
                							</AardFunctie>
                							<Periode>
                								<Begin>2025-01-10</Begin>
                								<Einde>9999-12-31</Einde>
                							</Periode>
                							<AanduidingVrijstelling>
                								<Code>999</Code>
                								<Omschrijving>geen vrijstelling mogelijk</Omschrijving>
                							</AanduidingVrijstelling>
                						</Functie>
                						<Functie DatumModificatie="2025-01-10">
                							<AuthentiekeBron>
                								<Code>03</Code>
                								<Omschrijving>KBO</Omschrijving>
                							</AuthentiekeBron>
                							<FunctiehouderVan>1999990639</FunctiehouderVan>
                							<AanduidingPersoonOnderneming>
                								<Code>3</Code>
                								<Omschrijving>Vertegenwoordiging door natuurlijke persoon en onderneming</Omschrijving>
                							</AanduidingPersoonOnderneming>
                							<INSZ>86692115747</INSZ>
                							<Ondernemingsnummer>1999990342</Ondernemingsnummer>
                							<VolgNummer>003</VolgNummer>
                							<AardFunctie>
                								<Code>10003</Code>
                								<Omschrijving>Vaste vertegenwoordiger</Omschrijving>
                							</AardFunctie>
                							<Periode>
                								<Begin>2025-01-10</Begin>
                								<Einde>9999-12-31</Einde>
                							</Periode>
                							<AanduidingVrijstelling>
                								<Code>999</Code>
                								<Omschrijving>geen vrijstelling mogelijk</Omschrijving>
                							</AanduidingVrijstelling>
                						</Functie>						
                						<Functie DatumModificatie="2026-03-15">
                							<AuthentiekeBron>
                								<Code>03</Code>
                								<Omschrijving>KBO</Omschrijving>
                							</AuthentiekeBron>
                							<FunctiehouderVan>1999990738</FunctiehouderVan>
                							<AanduidingPersoonOnderneming>
                								<Code>2</Code>
                								<Omschrijving>Vertegenwoordiging door onderneming</Omschrijving>
                							</AanduidingPersoonOnderneming>
                							<Ondernemingsnummer>1999990639</Ondernemingsnummer>
                							<VolgNummer>001</VolgNummer>
                							<AardFunctie>
                								<Code>10002</Code>
                								<Omschrijving>Bestuurder</Omschrijving>
                							</AardFunctie>
                							<Periode>
                								<Begin>2026-03-15</Begin>
                								<Einde>9999-12-31</Einde>
                							</Periode>
                							<AanduidingVrijstelling>
                								<Code>999</Code>
                								<Omschrijving>geen vrijstelling mogelijk</Omschrijving>
                							</AanduidingVrijstelling>
                						</Functie>
                					</Functies>
                				</Inhoud>
                			</Antwoord>
                		</Antwoorden>
                	</Repliek>
                </web:GeefFunctiesResponse>
                """;

        MagdaMockDocument request = MagdaMockDocument.fromString(requestXml);
        Document response = parseXml(responseXml);

        GeefFunctiesResponsePatcher soapResponse =
                new GeefFunctiesResponsePatcher(fixedClock, () -> FIXED_UUID);

        MagdaMockDocument result = soapResponse.patchResponse(request, response);

        List<String> values = result.getValues("//AardFunctie/Code");
        assertEquals(1, values.size());
        assertEquals(List.of("10002"), values);
    }
}
