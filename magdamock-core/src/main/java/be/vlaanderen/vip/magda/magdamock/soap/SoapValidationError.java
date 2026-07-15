package be.vlaanderen.vip.magda.magdamock.soap;

import be.vlaanderen.vip.magda.magdamock.utils.MagdaMockDocument;
import lombok.Getter;
import org.w3c.dom.Document;

public class SoapValidationError extends RuntimeException {
    @Getter
    private final MagdaMockDocument exceptionBody;

    public SoapValidationError(MagdaMockDocument exceptionBody) {
        this.exceptionBody = exceptionBody;
    }
}
