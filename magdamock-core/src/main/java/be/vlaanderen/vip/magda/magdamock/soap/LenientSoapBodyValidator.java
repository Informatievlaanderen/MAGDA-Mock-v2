package be.vlaanderen.vip.magda.magdamock.soap;

import be.vlaanderen.vip.magda.magdamock.utils.MagdaDocument;
import org.w3c.dom.Document;

import java.util.Optional;

// doesn't check any bodies, allows everything
public class LenientSoapBodyValidator extends SoapBodyValidator {
    @Override
    public void validateXml(MagdaDocument magdaDocument) {
    }
}
