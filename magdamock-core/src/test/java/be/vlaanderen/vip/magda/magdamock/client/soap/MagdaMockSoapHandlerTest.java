package be.vlaanderen.vip.magda.magdamock.client.soap;

import be.vlaanderen.vip.magda.magdamock.client.handlers.MagdaMockSoapHandler;
import be.vlaanderen.vip.magda.magdamock.config.WireMockData;
import be.vlaanderen.vip.magda.magdamock.soap.LenientSoapBodyValidator;
import be.vlaanderen.vip.magda.magdamock.utils.MagdaMockDocument;
import be.vlaanderen.vip.magda.magdamock.utils.NoopTimeoutUtil;
import be.vlaanderen.vip.magda.magdamock.utils.TimeoutUtil;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.direct.DirectCallHttpServer;
import com.github.tomakehurst.wiremock.direct.DirectCallHttpServerFactory;
import com.github.tomakehurst.wiremock.http.HttpHeaders;
import com.github.tomakehurst.wiremock.http.Response;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MagdaMockSoapHandlerTest {
    private WireMockServer wiremockserver;
    private DirectCallHttpServerFactory directCallHttpServerFactory;
    private DirectCallHttpServer directCallHttpServer;

    private MagdaMockSoapHandler magdaMockSoapHandler;

    private MagdaMockSoapHandlerTest() {
        directCallHttpServerFactory = mock(DirectCallHttpServerFactory.class);
        directCallHttpServer = mock(DirectCallHttpServer.class);
        when(directCallHttpServerFactory.getHttpServer()).thenReturn(directCallHttpServer);

        wiremockserver = mock(WireMockServer.class);

        magdaMockSoapHandler = new MagdaMockSoapHandler(
                new WireMockData(wiremockserver, directCallHttpServerFactory),
                new NoopTimeoutUtil(),
                new LenientSoapBodyValidator(),
                new LenientSoapBodyValidator(),
                false
        );
    }

    @Test
    public void whenSoapEnvelopeIsAlreadyPresentInMockData_noAdditionalSoapEnvelopeIsAdded(){
        Response response = mock(Response.class);
        when(response.getBodyAsString()).thenReturn(
                """
                        <soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/" xmlns:web="http://webservice.geefpasfotodienst-02_00.persoon-02_00.vip.vlaanderen.be">
<web:GeefKindVoordelenResponse
        xmlns:web="http://webservice.geefkindvoordelendienst-02_00.gezin-02_00.vip.vlaanderen.be">
    <Repliek>
        <Context>
            <Naam>GeefKindVoordelen</Naam>
            <Versie>02.00.0000</Versie>
            <Bericht>
                <Type>ANTWOORD</Type>
                <Tijdstip>
                    <Datum>2020-10-21</Datum>
                    <Tijd>11:39:58.708</Tijd>
                </Tijdstip>
                <Afzender>
                    <Identificatie>vip.vlaanderen.be</Identificatie>
                    <Naam>MagdaGateway</Naam>
                    <Referte>eded3540-c9b9-4753-a451-2801dc131e5d</Referte>
                </Afzender>
                <Ontvanger>
                    <Identificatie>wvg.vlaanderen.be/kg/groeipakket</Identificatie>
                    <Referte>A_QT_700502_113958</Referte>
                    <Hoedanigheid>301</Hoedanigheid>
                </Ontvanger>
            </Bericht>
        </Context>
        <Antwoorden>
            <Antwoord>
                <Referte>A_QT_700502_113958</Referte>
                <Inhoud>
                    <KindVoordelen>
                        <KindVoordeel>
                            <Bron>VLAANDEREN</Bron>
                            <INSZ>01620345635</INSZ>
                            <KindVoordeelFonds>
                                <Ondernemingsnummer>0687467902</Ondernemingsnummer>
                            </KindVoordeelFonds>
                            <Begunstigden>
                                <Begunstigde>
                                    <INSZ>87620345765</INSZ>
                                    <Betalingsperiodes>
                                        <Betalingsperiode>
                                            <Periode>
                                                <BeginMaand>2020-10</BeginMaand>
                                            </Periode>
                                            <Categorie>
                                                <Code>BASIS</Code>
                                                <Omschrijving>Basisbedrag</Omschrijving>
                                            </Categorie>
                                        </Betalingsperiode>
                                    </Betalingsperiodes>
                                </Begunstigde>
                                <Begunstigde>
                                    <INSZ>84700763452</INSZ>
                                    <Betalingsperiodes>
                                        <Betalingsperiode>
                                            <Periode>
                                                <BeginMaand>2020-10</BeginMaand>
                                            </Periode>
                                            <Categorie>
                                                <Code>BASIS</Code>
                                                <Omschrijving>Basisbedrag</Omschrijving>
                                            </Categorie>
                                        </Betalingsperiode>
                                    </Betalingsperiodes>
                                </Begunstigde>
                            </Begunstigden>
                            <Kinderen>
                                <Kind>
                                    <INSZ>01620345635</INSZ>
                                    <Betalingsperiodes>
                                        <Betalingsperiode>
                                            <Periode>
                                                <BeginMaand>2020-10</BeginMaand>
                                            </Periode>
                                            <Categorie>
                                                <Code>BASIS</Code>
                                                <Omschrijving>Basisbedrag</Omschrijving>
                                            </Categorie>
                                            <Begunstigden>
                                                <Begunstigde>84700763452</Begunstigde>
                                                <Begunstigde>87620345765</Begunstigde>
                                            </Begunstigden>
                                        </Betalingsperiode>
                                    </Betalingsperiodes>
                                </Kind>
                            </Kinderen>
                        </KindVoordeel>
                    </KindVoordelen>
                </Inhoud>
            </Antwoord>
        </Antwoorden>
    </Repliek>
</web:GeefKindVoordelenResponse>
                        </soap:Envelope>
                    """
        );
        when(response.getStatus()).thenReturn(200);
        when(response.getHeaders()).thenReturn(HttpHeaders.noHeaders());
        when(directCallHttpServer.stubRequest(any())).thenReturn(response);

        var responseDocument = magdaMockSoapHandler.sendSoapRequest(
                new MagdaMockSoapHandler.MockSoapRequest(
                        MagdaMockDocument.fromString(
                                """
                                    <Verzoek>
                                        <Context>
                                            <Naam>GeefPasfoto</Naam>
                                            <Versie>02.00.0000</Versie>
                                            <Bericht>
                                                <Type>VRAAG</Type>
                                                <Tijdstip>
                                                    <Datum>{{datum}}</Datum>
                                                    <Tijd>{{tijdstip}}</Tijd>
                                                </Tijdstip>
                                                <Afzender>
                                                    <Identificatie>{{magda_connection_uri}}</Identificatie>
                                                    <Referte>{{referte}}</Referte>
                                                    <Hoedanigheid>{{magda_connection_hdc}}</Hoedanigheid>
                                                </Afzender>
                                            </Bericht>
                                            <Annotaties>
                                                <Annotatie>
                                                    <Naam/>
                                                    <Waarde/>
                                                </Annotatie>
                                            </Annotaties>
                                        </Context>
                                    </Verzoek>
                                    """
                        ).getXml()
                )
        );

        Document bodyDocument = responseDocument.document();
        assertEquals(1, bodyDocument.getElementsByTagNameNS("http://schemas.xmlsoap.org/soap/envelope/", "Envelope").getLength());
        assertEquals("http://schemas.xmlsoap.org/soap/envelope/",bodyDocument.lookupNamespaceURI(bodyDocument.getDocumentElement().getPrefix()));
        assertEquals("Envelope", bodyDocument.getDocumentElement().getLocalName());
    }

    @Test
    public void whenSoapEnvelopeNotPresentInMockdata_SurroundingSoapEnvelopeIsAdded(){
        Response response = mock(Response.class);
        when(response.getBodyAsString()).thenReturn(
                """
                        <soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/" xmlns:web="http://webservice.geefpasfotodienst-02_00.persoon-02_00.vip.vlaanderen.be">
<web:GeefKindVoordelenResponse
        xmlns:web="http://webservice.geefkindvoordelendienst-02_00.gezin-02_00.vip.vlaanderen.be">
    <Repliek>
        <Context>
            <Naam>GeefKindVoordelen</Naam>
            <Versie>02.00.0000</Versie>
            <Bericht>
                <Type>ANTWOORD</Type>
                <Tijdstip>
                    <Datum>2020-10-21</Datum>
                    <Tijd>11:39:58.708</Tijd>
                </Tijdstip>
                <Afzender>
                    <Identificatie>vip.vlaanderen.be</Identificatie>
                    <Naam>MagdaGateway</Naam>
                    <Referte>eded3540-c9b9-4753-a451-2801dc131e5d</Referte>
                </Afzender>
                <Ontvanger>
                    <Identificatie>wvg.vlaanderen.be/kg/groeipakket</Identificatie>
                    <Referte>A_QT_700502_113958</Referte>
                    <Hoedanigheid>301</Hoedanigheid>
                </Ontvanger>
            </Bericht>
        </Context>
        <Antwoorden>
            <Antwoord>
                <Referte>A_QT_700502_113958</Referte>
                <Inhoud>
                    <KindVoordelen>
                        <KindVoordeel>
                            <Bron>VLAANDEREN</Bron>
                            <INSZ>01620345635</INSZ>
                            <KindVoordeelFonds>
                                <Ondernemingsnummer>0687467902</Ondernemingsnummer>
                            </KindVoordeelFonds>
                            <Begunstigden>
                                <Begunstigde>
                                    <INSZ>87620345765</INSZ>
                                    <Betalingsperiodes>
                                        <Betalingsperiode>
                                            <Periode>
                                                <BeginMaand>2020-10</BeginMaand>
                                            </Periode>
                                            <Categorie>
                                                <Code>BASIS</Code>
                                                <Omschrijving>Basisbedrag</Omschrijving>
                                            </Categorie>
                                        </Betalingsperiode>
                                    </Betalingsperiodes>
                                </Begunstigde>
                                <Begunstigde>
                                    <INSZ>84700763452</INSZ>
                                    <Betalingsperiodes>
                                        <Betalingsperiode>
                                            <Periode>
                                                <BeginMaand>2020-10</BeginMaand>
                                            </Periode>
                                            <Categorie>
                                                <Code>BASIS</Code>
                                                <Omschrijving>Basisbedrag</Omschrijving>
                                            </Categorie>
                                        </Betalingsperiode>
                                    </Betalingsperiodes>
                                </Begunstigde>
                            </Begunstigden>
                            <Kinderen>
                                <Kind>
                                    <INSZ>01620345635</INSZ>
                                    <Betalingsperiodes>
                                        <Betalingsperiode>
                                            <Periode>
                                                <BeginMaand>2020-10</BeginMaand>
                                            </Periode>
                                            <Categorie>
                                                <Code>BASIS</Code>
                                                <Omschrijving>Basisbedrag</Omschrijving>
                                            </Categorie>
                                            <Begunstigden>
                                                <Begunstigde>84700763452</Begunstigde>
                                                <Begunstigde>87620345765</Begunstigde>
                                            </Begunstigden>
                                        </Betalingsperiode>
                                    </Betalingsperiodes>
                                </Kind>
                            </Kinderen>
                        </KindVoordeel>
                    </KindVoordelen>
                </Inhoud>
            </Antwoord>
        </Antwoorden>
    </Repliek>
</web:GeefKindVoordelenResponse>
                        </soap:Envelope>
                    """
        );
        when(response.getStatus()).thenReturn(200);
        when(response.getHeaders()).thenReturn(HttpHeaders.noHeaders());
        when(directCallHttpServer.stubRequest(any())).thenReturn(response);

        var responseDocument = magdaMockSoapHandler.sendSoapRequest(
                new MagdaMockSoapHandler.MockSoapRequest(
                        MagdaMockDocument.fromString(
                                """
                                    <Verzoek>
                                        <Context>
                                            <Naam>GeefPasfoto</Naam>
                                            <Versie>02.00.0000</Versie>
                                            <Bericht>
                                                <Type>VRAAG</Type>
                                                <Tijdstip>
                                                    <Datum>{{datum}}</Datum>
                                                    <Tijd>{{tijdstip}}</Tijd>
                                                </Tijdstip>
                                                <Afzender>
                                                    <Identificatie>{{magda_connection_uri}}</Identificatie>
                                                    <Referte>{{referte}}</Referte>
                                                    <Hoedanigheid>{{magda_connection_hdc}}</Hoedanigheid>
                                                </Afzender>
                                            </Bericht>
                                            <Annotaties>
                                                <Annotatie>
                                                    <Naam/>
                                                    <Waarde/>
                                                </Annotatie>
                                            </Annotaties>
                                        </Context>
                                    </Verzoek>
                                    """
                        ).getXml()
                )
        );

        Document bodyDocument = responseDocument.document();
        assertEquals(1, bodyDocument.getElementsByTagNameNS("http://schemas.xmlsoap.org/soap/envelope/", "Envelope").getLength());
        assertEquals("http://schemas.xmlsoap.org/soap/envelope/",bodyDocument.lookupNamespaceURI(bodyDocument.getDocumentElement().getPrefix()));
        assertEquals("Envelope", bodyDocument.getDocumentElement().getLocalName());
    }
}
