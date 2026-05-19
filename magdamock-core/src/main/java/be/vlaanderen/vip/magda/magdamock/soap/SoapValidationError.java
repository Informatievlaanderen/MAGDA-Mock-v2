package be.vlaanderen.vip.magda.magdamock.soap;

import be.vlaanderen.vip.magda.client.MagdaDocument;
import lombok.Getter;
import org.w3c.dom.Document;

public class SoapValidationError extends RuntimeException {
    @Getter
    private final MagdaDocument exceptionBody;

    public SoapValidationError(MagdaDocument exceptionBody) {
        this.exceptionBody = exceptionBody;
    }
}
