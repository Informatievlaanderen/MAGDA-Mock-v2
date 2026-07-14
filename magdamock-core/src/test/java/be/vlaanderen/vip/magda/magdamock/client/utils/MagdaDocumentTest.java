package be.vlaanderen.vip.magda.magdamock.client.utils;

import be.vlaanderen.vip.magda.magdamock.exceptions.MagdaMockSoapException;
import be.vlaanderen.vip.magda.magdamock.utils.MagdaDocument;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;

public class MagdaDocumentTest {

    @SneakyThrows
    @Test
    public void whenParseFakeMagdaRequest_shouldHaveCorrectServiceIdentification() {
        String input = """
                <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:web="http://magda.vlaanderen.be/persoon/soap/geefpersoon/v02_02">
                    <soapenv:Header/>
                    <soapenv:Body>
                        <web:GeefPersoon>
                            <Verzoek>
                                <Context>
                                    <Naam>Mock</Naam>
                                    <Versie>01.02.1234</Versie>
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
                                </Context>
                                <Vragen>
                                    <Vraag>
                                        <Referte>{{referte}}</Referte>
                                        <Inhoud>
                                            <Criteria>
                                                <INSZ>{{insz}}</INSZ>
                                            </Criteria>
                                            <Bron>KSZ</Bron>
                                            <Taal>nl</Taal>
                                        </Inhoud>
                                    </Vraag>
                                </Vragen>
                            </Verzoek>
                        </web:GeefPersoon>
                    </soapenv:Body>
                </soapenv:Envelope>
                """;
        MagdaDocument document = MagdaDocument.fromString(input);
        Assertions.assertNotNull(document);
        var identification = document.getServiceIdentification();
        Assertions.assertNotNull(identification);
        Assertions.assertEquals("Mock", identification.name());
        Assertions.assertEquals("01.02.1234", identification.version());
        Assertions.assertEquals("Mock-01.02.1234", identification.getServiceNaam());
    }

    @Test
    public void whenInputIsInvalid_shouldThrowException() {
        String input = """
                <soapenv:Envelope>
        """;
        Assertions.assertThrows(MagdaMockSoapException.class, () -> MagdaDocument.fromString(input));
    }
}
