package be.vlaanderen.vip.magda.magdamock.filters;

import be.vlaanderen.vip.magda.magdamock.utils.MagdaMockDocument;
import be.vlaanderen.vip.magda.magdamock.soap.SoapValidationError;
import org.w3c.dom.Document;

public interface MagdaMockFilter {
    public Document filter(MagdaMockDocument request, Document response);
}
