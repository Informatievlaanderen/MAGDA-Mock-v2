package be.vlaanderen.vip.magda.magdamock.filters;

import be.vlaanderen.vip.magda.client.MagdaDocument;
import be.vlaanderen.vip.magda.magdamock.soap.SoapValidationError;
import org.w3c.dom.Document;

public interface MagdaMockFilter {
    public Document filter(MagdaDocument request, Document response);
}
