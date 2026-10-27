package be.vlaanderen.vip.magda.magdamock.client.patchers;

import be.vlaanderen.vip.magda.magdamock.client.soap.ResponsePatcherTest;
import be.vlaanderen.vip.magda.magdamock.utils.MagdaMockDocument;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GeefBetalingenHandicapResponsePatcherTest extends ResponsePatcherTest {

    @Test
    void patchResponse_shouldFilterPaymentsBasedOnSBeginAndEndDates() throws Exception {
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
                    <Vraag>
                    	<Referte>28c2cfc1-6015-45f5-818f-48bc8ccaa1fc</Referte>
                        <Criteria>
                            <ConsultPaymentsCriteria>
                                <ssin>83660634135</ssin>
                                <handicapAuthenticSources>
                                    <DGPH>true</DGPH>
                                    <VSB>false</VSB>
                                    <IrisCare>false</IrisCare>
                                    <NicCin>false</NicCin>
                                    <MDG>false</MDG>
                                </handicapAuthenticSources>
                                <period>
                                    <beginDate>2023-07-01</beginDate>
                                    <endDate>2023-12-31</endDate>
                                </period>
                            </ConsultPaymentsCriteria>
                        </Criteria>
                    </Vraag>
                </Root>
                """;

        String responseXml = """
<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<web:GeefBetalingenHandicapResponse xmlns:web="http://magda.vlaanderen.be/soczek/soap/geefbetalingenhandicap/v03_00">
    <Repliek>
        <Context>
            <Naam>GeefBetalingenHandicap</Naam>
            <Versie>03.00.0000</Versie>
            <Bericht>
                <Type>ANTWOORD</Type>
                <Tijdstip>
                    <Datum>?</Datum>
                    <Tijd>?</Tijd>
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
                    <ConsultPaymentsResponse>
                        <status>
                            <value>DATA_FOUND</value>
                            <code>MSG00000</code>
                        </status>
                        <results>
                            <dgphResult>
                                <status>
                                    <value>DATA_FOUND</value>
                                    <code>MSG00000</code>
                                </status>
                                <payments>
                                    <payment>
                                        <yearMonth>2023-01</yearMonth>
                                        <amount>1047.48</amount>
                                        <cancelledPayment>0</cancelledPayment>
                                    </payment>
                                    <payment>
                                        <yearMonth>2023-02</yearMonth>
                                        <amount>1046.44</amount>
                                        <cancelledPayment>0</cancelledPayment>
                                    </payment>
                                    <payment>
                                        <yearMonth>2023-03</yearMonth>
                                        <amount>1041.58</amount>
                                        <cancelledPayment>0</cancelledPayment>
                                    </payment>
                                    <payment>
                                        <yearMonth>2023-04</yearMonth>
                                        <amount>1047.48</amount>
                                        <cancelledPayment>0</cancelledPayment>
                                    </payment>
                                    <payment>
                                        <yearMonth>2023-05</yearMonth>
                                        <amount>1046.44</amount>
                                        <cancelledPayment>0</cancelledPayment>
                                    </payment>
                                    <payment>
                                        <yearMonth>2023-06</yearMonth>
                                        <amount>1041.58</amount>
                                        <cancelledPayment>0</cancelledPayment>
                                    </payment>
                                    <payment>
                                        <yearMonth>2023-07</yearMonth>
                                        <amount>1047.48</amount>
                                        <cancelledPayment>0</cancelledPayment>
                                    </payment>
                                    <payment>
                                        <yearMonth>2023-08</yearMonth>
                                        <amount>1046.44</amount>
                                        <cancelledPayment>0</cancelledPayment>
                                    </payment>
                                    <payment>
                                        <yearMonth>2023-09</yearMonth>
                                        <amount>1041.58</amount>
                                        <cancelledPayment>0</cancelledPayment>
                                    </payment>
                                    <payment>
                                        <yearMonth>2023-10</yearMonth>
                                        <amount>1047.48</amount>
                                        <cancelledPayment>0</cancelledPayment>
                                    </payment>
                                    <payment>
                                        <yearMonth>2023-11</yearMonth>
                                        <amount>1046.44</amount>
                                        <cancelledPayment>0</cancelledPayment>
                                    </payment>
                                    <payment>
                                        <yearMonth>2023-12</yearMonth>
                                        <amount>1041.58</amount>
                                        <cancelledPayment>0</cancelledPayment>
                                    </payment>
                                </payments>
                            </dgphResult>
                        </results>
                    </ConsultPaymentsResponse>
                </Inhoud>
            </Antwoord>
        </Antwoorden>
    </Repliek>
</web:GeefBetalingenHandicapResponse>
                """;

        MagdaMockDocument request = MagdaMockDocument.fromString(requestXml);
        Document response = parseXml(responseXml);

        GeefBetalingenHandicapResponsePatcher soapResponse =
                new GeefBetalingenHandicapResponsePatcher(fixedClock, () -> FIXED_UUID);

        MagdaMockDocument result = soapResponse.patchResponse(request, response);

        List<String> values = result.getValues("//payments/payment/yearMonth");
        assertEquals(6, values.size());
        assertEquals(List.of("2023-07", "2023-08", "2023-09", "2023-10", "2023-11", "2023-12"), values);
    }


    @Test
    void patchResponse_shouldNotFilterPaymentsWhenDatePeriodIsBroaderThanPaymentsInResponseFile() throws Exception {
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
                    <Vraag>
                    	<Referte>28c2cfc1-6015-45f5-818f-48bc8ccaa1fc</Referte>
                        <Criteria>
                            <ConsultPaymentsCriteria>
                                <ssin>83660634135</ssin>
                                <handicapAuthenticSources>
                                    <DGPH>true</DGPH>
                                    <VSB>false</VSB>
                                    <IrisCare>false</IrisCare>
                                    <NicCin>false</NicCin>
                                    <MDG>false</MDG>
                                </handicapAuthenticSources>
                                <period>
                                    <beginDate>2020-07-01</beginDate>
                                    <endDate>2030-12-31</endDate>
                                </period>
                            </ConsultPaymentsCriteria>
                        </Criteria>
                    </Vraag>
                </Root>
                """;

        String responseXml = """
<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<web:GeefBetalingenHandicapResponse xmlns:web="http://magda.vlaanderen.be/soczek/soap/geefbetalingenhandicap/v03_00">
    <Repliek>
        <Context>
            <Naam>GeefBetalingenHandicap</Naam>
            <Versie>03.00.0000</Versie>
            <Bericht>
                <Type>ANTWOORD</Type>
                <Tijdstip>
                    <Datum>?</Datum>
                    <Tijd>?</Tijd>
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
                    <ConsultPaymentsResponse>
                        <status>
                            <value>DATA_FOUND</value>
                            <code>MSG00000</code>
                        </status>
                        <results>
                            <dgphResult>
                                <status>
                                    <value>DATA_FOUND</value>
                                    <code>MSG00000</code>
                                </status>
                                <payments>
                                    <payment>
                                        <yearMonth>2023-01</yearMonth>
                                        <amount>1047.48</amount>
                                        <cancelledPayment>0</cancelledPayment>
                                    </payment>
                                    <payment>
                                        <yearMonth>2023-02</yearMonth>
                                        <amount>1046.44</amount>
                                        <cancelledPayment>0</cancelledPayment>
                                    </payment>
                                    <payment>
                                        <yearMonth>2023-03</yearMonth>
                                        <amount>1041.58</amount>
                                        <cancelledPayment>0</cancelledPayment>
                                    </payment>
                                    <payment>
                                        <yearMonth>2023-04</yearMonth>
                                        <amount>1047.48</amount>
                                        <cancelledPayment>0</cancelledPayment>
                                    </payment>
                                    <payment>
                                        <yearMonth>2023-05</yearMonth>
                                        <amount>1046.44</amount>
                                        <cancelledPayment>0</cancelledPayment>
                                    </payment>
                                    <payment>
                                        <yearMonth>2023-06</yearMonth>
                                        <amount>1041.58</amount>
                                        <cancelledPayment>0</cancelledPayment>
                                    </payment>
                                    <payment>
                                        <yearMonth>2023-07</yearMonth>
                                        <amount>1047.48</amount>
                                        <cancelledPayment>0</cancelledPayment>
                                    </payment>
                                    <payment>
                                        <yearMonth>2023-08</yearMonth>
                                        <amount>1046.44</amount>
                                        <cancelledPayment>0</cancelledPayment>
                                    </payment>
                                    <payment>
                                        <yearMonth>2023-09</yearMonth>
                                        <amount>1041.58</amount>
                                        <cancelledPayment>0</cancelledPayment>
                                    </payment>
                                    <payment>
                                        <yearMonth>2023-10</yearMonth>
                                        <amount>1047.48</amount>
                                        <cancelledPayment>0</cancelledPayment>
                                    </payment>
                                    <payment>
                                        <yearMonth>2023-11</yearMonth>
                                        <amount>1046.44</amount>
                                        <cancelledPayment>0</cancelledPayment>
                                    </payment>
                                    <payment>
                                        <yearMonth>2023-12</yearMonth>
                                        <amount>1041.58</amount>
                                        <cancelledPayment>0</cancelledPayment>
                                    </payment>
                                </payments>
                            </dgphResult>
                        </results>
                    </ConsultPaymentsResponse>
                </Inhoud>
            </Antwoord>
        </Antwoorden>
    </Repliek>
</web:GeefBetalingenHandicapResponse>
                """;

        MagdaMockDocument request = MagdaMockDocument.fromString(requestXml);
        Document response = parseXml(responseXml);

        GeefBetalingenHandicapResponsePatcher soapResponse =
                new GeefBetalingenHandicapResponsePatcher(fixedClock, () -> FIXED_UUID);

        MagdaMockDocument result = soapResponse.patchResponse(request, response);

        List<String> values = result.getValues("//payments/payment/yearMonth");
        assertEquals(12, values.size());
        assertEquals(List.of("2023-01", "2023-02", "2023-03", "2023-04", "2023-05", "2023-06", "2023-07", "2023-08", "2023-09", "2023-10", "2023-11", "2023-12"), values);
    }

    @Test
    void patchResponse_noPaymentsInPeriodChangesStatus() throws Exception {
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
                    <Vraag>
                    	<Referte>28c2cfc1-6015-45f5-818f-48bc8ccaa1fc</Referte>
                        <Criteria>
                            <ConsultPaymentsCriteria>
                                <ssin>83660634135</ssin>
                                <handicapAuthenticSources>
                                    <DGPH>true</DGPH>
                                    <VSB>false</VSB>
                                    <IrisCare>false</IrisCare>
                                    <NicCin>false</NicCin>
                                    <MDG>false</MDG>
                                </handicapAuthenticSources>
                                <period>
                                    <beginDate>2020-07-01</beginDate>
                                    <endDate>2020-12-31</endDate>
                                </period>
                            </ConsultPaymentsCriteria>
                        </Criteria>
                    </Vraag>
                </Root>
                """;

        String responseXml = """
<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<web:GeefBetalingenHandicapResponse xmlns:web="http://magda.vlaanderen.be/soczek/soap/geefbetalingenhandicap/v03_00">
    <Repliek>
        <Context>
            <Naam>GeefBetalingenHandicap</Naam>
            <Versie>03.00.0000</Versie>
            <Bericht>
                <Type>ANTWOORD</Type>
                <Tijdstip>
                    <Datum>?</Datum>
                    <Tijd>?</Tijd>
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
                    <ConsultPaymentsResponse>
                        <status>
                            <value>DATA_FOUND</value>
                            <code>MSG00000</code>
                        </status>
                        <results>
                            <dgphResult>
                                <status>
                                    <value>DATA_FOUND</value>
                                    <code>MSG00000</code>
                                </status>
                                <payments>
                                    <payment>
                                        <yearMonth>2023-01</yearMonth>
                                        <amount>1047.48</amount>
                                        <cancelledPayment>0</cancelledPayment>
                                    </payment>
                                    <payment>
                                        <yearMonth>2023-02</yearMonth>
                                        <amount>1046.44</amount>
                                        <cancelledPayment>0</cancelledPayment>
                                    </payment>
                                    <payment>
                                        <yearMonth>2023-03</yearMonth>
                                        <amount>1041.58</amount>
                                        <cancelledPayment>0</cancelledPayment>
                                    </payment>
                                    <payment>
                                        <yearMonth>2023-04</yearMonth>
                                        <amount>1047.48</amount>
                                        <cancelledPayment>0</cancelledPayment>
                                    </payment>
                                    <payment>
                                        <yearMonth>2023-05</yearMonth>
                                        <amount>1046.44</amount>
                                        <cancelledPayment>0</cancelledPayment>
                                    </payment>
                                    <payment>
                                        <yearMonth>2023-06</yearMonth>
                                        <amount>1041.58</amount>
                                        <cancelledPayment>0</cancelledPayment>
                                    </payment>
                                    <payment>
                                        <yearMonth>2023-07</yearMonth>
                                        <amount>1047.48</amount>
                                        <cancelledPayment>0</cancelledPayment>
                                    </payment>
                                    <payment>
                                        <yearMonth>2023-08</yearMonth>
                                        <amount>1046.44</amount>
                                        <cancelledPayment>0</cancelledPayment>
                                    </payment>
                                    <payment>
                                        <yearMonth>2023-09</yearMonth>
                                        <amount>1041.58</amount>
                                        <cancelledPayment>0</cancelledPayment>
                                    </payment>
                                    <payment>
                                        <yearMonth>2023-10</yearMonth>
                                        <amount>1047.48</amount>
                                        <cancelledPayment>0</cancelledPayment>
                                    </payment>
                                    <payment>
                                        <yearMonth>2023-11</yearMonth>
                                        <amount>1046.44</amount>
                                        <cancelledPayment>0</cancelledPayment>
                                    </payment>
                                    <payment>
                                        <yearMonth>2023-12</yearMonth>
                                        <amount>1041.58</amount>
                                        <cancelledPayment>0</cancelledPayment>
                                    </payment>
                                </payments>
                            </dgphResult>
                        </results>
                    </ConsultPaymentsResponse>
                </Inhoud>
            </Antwoord>
        </Antwoorden>
    </Repliek>
</web:GeefBetalingenHandicapResponse>
                """;

        MagdaMockDocument request = MagdaMockDocument.fromString(requestXml);
        Document response = parseXml(responseXml);

        GeefBetalingenHandicapResponsePatcher soapResponse =
                new GeefBetalingenHandicapResponsePatcher(fixedClock, () -> FIXED_UUID);

        MagdaMockDocument result = soapResponse.patchResponse(request, response);

        List<String> values = result.getValues("//ConsultPaymentsResponse/status/value");
        assertEquals(1, values.size());
        assertEquals(List.of("NO_DATA_FOUND"), values);
    }
}
