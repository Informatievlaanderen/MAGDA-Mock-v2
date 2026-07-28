package be.vlaanderen.vip.magda.magdamock.soap;

import be.vlaanderen.vip.magda.magdamock.config.MockSoapMapping;
import be.vlaanderen.vip.magda.magdamock.exceptions.MagdaMockSoapException;
import be.vlaanderen.vip.magda.magdamock.utils.MagdaMockDocument;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import javax.xml.XMLConstants;
import javax.xml.transform.dom.DOMSource;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.util.Optional;

@Slf4j
public class SoapResponseValidatorImpl extends SoapBodyValidator {
    private final String xsdPath;

    public SoapResponseValidatorImpl(String xsdPath) {
        this.xsdPath = xsdPath;
    }

    private Validator getValidator(String naam, String versie) {
        try {
            var factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Optional<String> optionalPath = MockSoapMapping.MAPPINGS.stream().filter(mapping -> mapping.service().equals(naam) && mapping.version().equals(versie)).findFirst().map(MockSoapMapping::xsdResponsePath);
            if (optionalPath.isEmpty()) {
                return null;
            }
            String path = optionalPath.get();
            log.info("Trying to load xml response validator from {}", path);
            var schema = factory.newSchema(new File(String.format("%s/%s", xsdPath, path)));
            var validator = schema.newValidator();
            validator.setErrorHandler(new XsdErrorHandler());
            return validator;
        } catch (Exception e) {
            throw new MagdaMockSoapException(String.format("Unable to locate the response XSD schema for %s-%s .", naam, versie), "Server", e);
        }
    }

    @SneakyThrows
    public void validateXml(MagdaMockDocument magdaDocument) throws MagdaMockSoapException {
        try {
            String naam = magdaDocument.getValue("//Context/Naam");
            String versie = magdaDocument.getValue("//Context/Versie");
            Validator validator = getValidator(naam, versie);
            validator.validate(new DOMSource(magdaDocument.getXml()));
        } catch (Exception e) {
            throw new MagdaMockSoapException(String.format("Response is not compliant with the associated XSD schema specification. Reason: %s", e.getMessage()), "Server", e);
        }
    }
}
