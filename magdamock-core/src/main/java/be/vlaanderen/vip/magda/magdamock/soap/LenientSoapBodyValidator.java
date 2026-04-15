package be.vlaanderen.vip.magda.magdamock.soap;

import be.vlaanderen.vip.magda.client.MagdaDocument;
import org.w3c.dom.Document;

import java.util.Optional;

// doesn't check any bodies, allows everything
public class LenientSoapBodyValidator implements SoapBodyValidator {
    @Override
    public Optional<Document> validateXml(MagdaDocument magdaDocument) {
        return Optional.empty();
    }
}
