package be.vlaanderen.vip.magda.magdamock.soap;

import be.vlaanderen.vip.magda.client.MagdaDocument;
import lombok.SneakyThrows;
import org.w3c.dom.Document;

import java.util.Optional;

public interface SoapBodyValidator {
    @SneakyThrows
    Optional<Document> validateXml(MagdaDocument magdaDocument);
}
