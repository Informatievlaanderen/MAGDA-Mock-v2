package be.vlaanderen.vip.magda.magdamock.soap;

import be.vlaanderen.vip.magda.magdamock.config.MappingLists;
import be.vlaanderen.vip.magda.magdamock.config.MockSoapMapping;
import be.vlaanderen.vip.magda.magdamock.exceptions.MagdaMockSoapException;
import be.vlaanderen.vip.magda.magdamock.utils.MagdaMockDocument;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.XMLConstants;
import javax.xml.transform.dom.DOMSource;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.util.Map;
import java.util.Optional;

@Slf4j
public class SoapRequestValidatorImpl extends SoapBodyValidator {
    private final String xsdPath;

    public SoapRequestValidatorImpl(String xsdPath) {
        this.xsdPath = xsdPath;
    }

    private Validator getValidator(String naam, String versie) {
        try {
            var factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Optional<String> optionalPath = MappingLists.SOAP_MAPPINGS.stream().filter(mapping -> mapping.getService().equals(naam) && mapping.getVersion().equals(versie)).findFirst().map(MockSoapMapping::getXsdRequestPath);
            if (optionalPath.isEmpty()) {
                return null;
            }
            String path = optionalPath.get();
            String fullPath = String.format("%s/%s", xsdPath, path);
            log.info("Trying to load xml request validator from {}", fullPath);
            var schema = factory.newSchema(new File(fullPath));
            var validator = schema.newValidator();
            validator.setErrorHandler(new XsdErrorHandler());
            return validator;
        } catch (Exception e) {
            throw new MagdaMockSoapException(String.format("Unable to locate the request XSD schema for %s-%s .", naam, versie), "Server", e);
        }
    }

    @SneakyThrows
    public void validateXml(MagdaMockDocument magdaDocument) throws MagdaMockSoapException {
        try {
            String naam = magdaDocument.xpath("//Context/Naam").item(0).getTextContent();
            String versie = magdaDocument.xpath("//Context/Versie").item(0).getTextContent();
            Validator validator = getValidator(naam, versie);
            NodeList xpath = magdaDocument.xpath("//soapenv:Body/*");
            Document xml = nodelistToDocument(xpath);
            validator.validate(new DOMSource(xml));
        } catch (Exception e) {
            throw new MagdaMockSoapException(String.format("Request is not compliant with the associated XSD schema specification. Reason: %s", e.getMessage()), "Server", e);
        }
    }
}
