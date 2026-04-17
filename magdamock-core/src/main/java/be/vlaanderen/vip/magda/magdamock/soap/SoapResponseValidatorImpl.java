package be.vlaanderen.vip.magda.magdamock.soap;

import be.vlaanderen.vip.magda.client.MagdaDocument;
import lombok.SneakyThrows;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.IntStream;

public class SoapResponseValidatorImpl extends SoapBodyValidator {
    private final Map<String, String> XML_FOLDERS_AND_XSDS = data(
            "GeefDossiers/02.00.0000", "Dossier.GeefDossiersDienst-02.00/WebService/GeefDossiersResponse.xsd",

            "GeefKindVoordelen/02.00.0000", "Gezin.GeefKindVoordelenDienst-02.00/WebService/GeefKindVoordelenResponse.xsd",

            "GeefAanslagbiljetPersonenbelasting/02.00.0000", "Inkomen.GeefAanslagbiljetPersonenbelastingDienst-02.00/WebService/GeefAanslagbiljetPersonenbelastingResponse.xsd",

            "ZoekEigendomstoestanden/02.00.0000", "Kadaster.ZoekEigendomstoestandenDienst-02.00/WebService/ZoekEigendomstoestandenResponse.xsd",

            "GeefBewijs/02.00.0000", "LED.GeefBewijsDienst-02.00/WebService/GeefBewijsResponse.xsd",
            "RegistreerBewijs/02.00.0000", "LED.RegistreerBewijsDienst-02.00/WebService/RegistreerBewijsResponse.xsd",

            "GeefFuncties/02.00.0000", "Onderneming.GeefFunctiesDienst-02.00/WebService/GeefFunctiesResponse.xsd",
            "GeefJaarrekeningen/02.00.0000", "Onderneming.GeefJaarrekeningenDienst-02.00/WebService/GeefJaarrekeningenResponse.xsd",
            "GeefOnderneming/02.00.0000", "Onderneming.GeefOndernemingDienst-02.00/WebService/GeefOndernemingResponse.xsd",
            "GeefOndernemingVKBO/02.00.0000", "Onderneming.GeefOndernemingVKBODienst-02.00/WebService/GeefOndernemingVKBOResponse.xsd",

            "GeefHistoriekInschrijving/02.01.0000", "Onderwijs.GeefHistoriekInschrijvingDienst-02.01/WebService/GeefHistoriekInschrijvingResponse.xsd",

            "GeefAttest/02.00.0000", "Persoon.GeefAttestDienst-02.00/WebService/GeefAttestResponse.xsd",
            "GeefGezinssamenstelling/02.00.0000", "Persoon.GeefGezinssamenstellingDienst-02.00/WebService/GeefGezinssamenstellingResponse.xsd",
            "GeefGezinssamenstelling/02.02.0000", "Persoon.GeefGezinssamenstellingDienst-02.02/WebService/GeefGezinssamenstellingResponse.xsd",
            "GeefHistoriekGezinssamenstelling/02.02.0000", "Persoon.GeefHistoriekGezinssamenstellingDienst-02.02/WebService/GeefHistoriekGezinssamenstellingResponse.xsd",
            "GeefHistoriekPersoon/02.00.0000", "Persoon.GeefHistoriekPersoonDienst-02.00/WebService/GeefHistoriekPersoonResponse.xsd",
            "GeefHistoriekPersoon/02.02.0000", "Persoon.GeefHistoriekPersoonDienst-02.02/WebService/GeefHistoriekPersoonResponse.xsd",
            "GeefPasfoto/02.00.0000", "Persoon.GeefPasfotoDienst-02.00/WebService/GeefPasfotoResponse.xsd",
            "GeefPersoon/02.02.0000", "Persoon.GeefPersoonDienst-02.02/WebService/GeefPersoonResponse.xsd",
            "ZoekPersoonOpAdres/02.02.0000", "Persoon.ZoekPersoonOpAdresDienst-02.02/WebService/ZoekPersoonOpAdresResponse.xsd",

            "RegistreerInschrijving/02.00.0000", "Repertorium.RegistreerInschrijvingDienst-02.00/WebService/RegistreerInschrijvingResponse.xsd",
            "RegistreerInschrijving/02.01.0000", "Repertorium.RegistreerInschrijvingDienst-02.01/WebService/RegistreerInschrijvingResponse.xsd",
            "RegistreerUitschrijving/02.00.0000", "Repertorium.RegistreerUitschrijvingDienst-02.00/WebService/RegistreerUitschrijvingResponse.xsd",

            "GeefStatusRechtOndersteuningen/02.00.0000", "SocEcon.GeefStatusRechtOndersteuningenDienst-02.00/WebService/GeefStatusRechtOndersteuningenResponse.xsd",

            "GeefBetalingenHandicap/03.00.0000", "SocZek.GeefBetalingenHandicapDienst-03.00/WebService/GeefBetalingenHandicapResponse.xsd",
            "GeefDossierHandicap/03.00.0000", "SocZek.GeefDossierHandicapDienst-03.00/WebService/GeefDossierHandicapResponse.xsd",
            "GeefLeefloonbedragen/02.00.0000", "SocZek.GeefLeefloonbedragenDienst-02.00/WebService/GeefLeefloonbedragenResponse.xsd",
            "GeefSociaalStatuut/03.00.0000", "SocZek.GeefSociaalStatuutDienst-03.00/WebService/GeefSociaalStatuutResponse.xsd",
            "GeefVolledigDossierHandicap/03.00.0000", "SocZek.GeefVolledigDossierHandicapDienst-03.00/WebService/GeefVolledigDossierHandicapResponse.xsd",

            "GeefEpc/02.01.0000", "Energie.GeefEpcDienst-02.01/WebService/GeefEpcResponse.xsd",

            "GeefLoopbaanARZA/02.01.0000", "Werk.GeefLoopbaanARZADienst-02.01/WebService/GeefLoopbaanARZAResponse.xsd",
            "GeefDmfaVoorWerknemer/03.00.0000", "Werk.GeefDmfaVoorWerknemer-03.00/WebService/GeefDmfaVoorWerknemerResponse.xsd",
            "GeefLoopbaanonderbrekingen/02.00.0000", "Werk.GeefLoopbaanonderbrekingenDienst-02.00/WebService/GeefLoopbaanonderbrekingenResponse.xsd",
            "GeefWerkrelaties/02.00.0000", "Werk.GeefWerkrelatiesDienst-02.00/WebService/GeefWerkrelatiesResponse.xsd");
    private final String xsdPath;

    public SoapResponseValidatorImpl(String xsdPath) {
        this.xsdPath = xsdPath;
    }

    private Validator getValidator(String naam, String versie) {
        try {
            var factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            String path = XML_FOLDERS_AND_XSDS.get(String.format("%s/%s", naam, versie));
            var schema = factory.newSchema(new File(String.format("%s/%s", xsdPath, path)));
            var validator = schema.newValidator();
            validator.setErrorHandler(new XsdErrorHandler());
            return validator;
        } catch (Exception e) {
            return null;
        }
    }

    @SneakyThrows
    public Optional<Document> validateXml(MagdaDocument magdaDocument) {
        String naam = magdaDocument.xpath("//Context/Naam").item(0).getTextContent();
        String versie = magdaDocument.xpath("//Context/Versie").item(0).getTextContent();
        try {
            Validator validator = getValidator(naam, versie);
            validator.validate(new DOMSource(magdaDocument.getXml()));
        } catch (Exception e) {
            return Optional.of(
                    MagdaDocument.fromString(
                            String.format(
                            """
                            <Error>
                                <Type>
                                    Invalid Response body
                                </Type>
                                <ErrorMessage>
                                    %s
                                </ErrorMessage>
                            </Error>
                            """,
                                    e.getMessage()
                            )
                    ).getXml()
            );
        }
        return Optional.empty();
    }
}
