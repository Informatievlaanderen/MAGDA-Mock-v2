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

public class SoapRequestValidatorImpl extends SoapBodyValidator {
    private final Map<String, String> XML_FOLDERS_AND_XSDS = data(
            "GeefDossiers/02.00.0000", "Dossier.GeefDossiersDienst-02.00/WebService/GeefDossiers.xsd",

            "GeefKindVoordelen/02.00.0000", "Gezin.GeefKindVoordelenDienst-02.00/WebService/GeefKindVoordelen.xsd",

            "GeefAanslagbiljetPersonenbelasting/02.00.0000", "Inkomen.GeefAanslagbiljetPersonenbelastingDienst-02.00/WebService/GeefAanslagbiljetPersonenbelasting.xsd",

            "ZoekEigendomstoestanden/02.00.0000", "Kadaster.ZoekEigendomstoestandenDienst-02.00/WebService/ZoekEigendomstoestanden.xsd",

            "GeefBewijs/02.00.0000", "LED.GeefBewijsDienst-02.00/WebService/GeefBewijs.xsd",
            "RegistreerBewijs/02.00.0000", "LED.RegistreerBewijsDienst-02.00/WebService/RegistreerBewijs.xsd",

            "GeefFuncties/02.00.0000", "Onderneming.GeefFunctiesDienst-02.00/WebService/GeefFuncties.xsd",
            "GeefJaarrekeningen/02.00.0000", "Onderneming.GeefJaarrekeningenDienst-02.00/WebService/GeefJaarrekeningen.xsd",
            "GeefOnderneming/02.00.0000", "Onderneming.GeefOndernemingDienst-02.00/WebService/GeefOnderneming.xsd",
            "GeefOndernemingVKBO/02.00.0000", "Onderneming.GeefOndernemingVKBODienst-02.00/WebService/GeefOndernemingVKBO.xsd",

            "GeefHistoriekInschrijving/02.01.0000", "Onderwijs.GeefHistoriekInschrijvingDienst-02.01/WebService/GeefHistoriekInschrijving.xsd",

            "GeefAttest/02.00.0000", "Persoon.GeefAttestDienst-02.00/WebService/GeefAttest.xsd",
            "GeefGezinssamenstelling/02.00.0000", "Persoon.GeefGezinssamenstellingDienst-02.00/WebService/GeefGezinssamenstelling.xsd",
            "GeefGezinssamenstelling/02.02.0000", "Persoon.GeefGezinssamenstellingDienst-02.02/WebService/GeefGezinssamenstelling.xsd",
            "GeefHistoriekGezinssamenstelling/02.02.0000", "Persoon.GeefHistoriekGezinssamenstellingDienst-02.02/WebService/GeefHistoriekGezinssamenstelling.xsd",
            "GeefHistoriekPersoon/02.00.0000", "Persoon.GeefHistoriekPersoonDienst-02.00/WebService/GeefHistoriekPersoon.xsd",
            "GeefHistoriekPersoon/02.02.0000", "Persoon.GeefHistoriekPersoonDienst-02.02/WebService/GeefHistoriekPersoon.xsd",
            "GeefPasfoto/02.00.0000", "Persoon.GeefPasfotoDienst-02.00/WebService/GeefPasfoto.xsd",
            "GeefPersoon/02.02.0000", "Persoon.GeefPersoonDienst-02.02/WebService/GeefPersoon.xsd",
            "ZoekPersoonOpAdres/02.02.0000", "Persoon.ZoekPersoonOpAdresDienst-02.02/WebService/ZoekPersoonOpAdres.xsd",

            "RegistreerInschrijving/02.00.0000", "Repertorium.RegistreerInschrijvingDienst-02.00/WebService/RegistreerInschrijving.xsd",
            "RegistreerInschrijving/02.01.0000", "Repertorium.RegistreerInschrijvingDienst-02.01/WebService/RegistreerInschrijving.xsd",
            "RegistreerUitschrijving/02.00.0000", "Repertorium.RegistreerUitschrijvingDienst-02.00/WebService/RegistreerUitschrijving.xsd",

            "GeefStatusRechtOndersteuningen/02.00.0000", "SocEcon.GeefStatusRechtOndersteuningenDienst-02.00/WebService/GeefStatusRechtOndersteuningen.xsd",

            "GeefBetalingenHandicap/03.00.0000", "SocZek.GeefBetalingenHandicapDienst-03.00/WebService/GeefBetalingenHandicap.xsd",
            "GeefDossierHandicap/03.00.0000", "SocZek.GeefDossierHandicapDienst-03.00/WebService/GeefDossierHandicap.xsd",
            "GeefLeefloonbedragen/02.00.0000", "SocZek.GeefLeefloonbedragenDienst-02.00/WebService/GeefLeefloonbedragen.xsd",
            "GeefSociaalStatuut/03.00.0000", "SocZek.GeefSociaalStatuutDienst-03.00/WebService/GeefSociaalStatuut.xsd",
            "GeefVolledigDossierHandicap/03.00.0000", "SocZek.GeefVolledigDossierHandicapDienst-03.00/WebService/GeefVolledigDossierHandicap.xsd",

            "GeefEpc/02.01.0000", "Energie.GeefEpcDienst-02.01/WebService/GeefEpc.xsd",

            "GeefLoopbaanARZA/02.01.0000", "Werk.GeefLoopbaanARZADienst-02.01/WebService/GeefLoopbaanARZA.xsd",
            "GeefDmfaVoorWerknemer/03.00.0000", "Werk.GeefDmfaVoorWerknemer-03.00/WebService/GeefDmfaVoorWerknemer.xsd",
            "GeefLoopbaanonderbrekingen/02.00.0000", "Werk.GeefLoopbaanonderbrekingenDienst-02.00/WebService/GeefLoopbaanonderbrekingen.xsd",
            "GeefWerkrelaties/02.00.0000", "Werk.GeefWerkrelatiesDienst-02.00/WebService/GeefWerkrelaties.xsd");
    private final String xsdPath;

    public SoapRequestValidatorImpl(String xsdPath) {
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
            NodeList xpath = magdaDocument.xpath("//soapenv:Body/*");
            Document xml = nodelistToDocument(xpath);
            validator.validate(new DOMSource(xml));
        } catch (Exception e) {
            return Optional.of(
                    MagdaDocument.fromString(
                            String.format(
                            """
                            <Error>
                                <Type>
                                    Invalid Request
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
