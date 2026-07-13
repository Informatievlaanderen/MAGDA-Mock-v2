package be.vlaanderen.vip.magda.magdamock.client.patchers;

import be.vlaanderen.vip.magda.magdamock.utils.MagdaDocument;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

@Slf4j
public class BasicSoapResponsePatcher implements SoapResponsePatcher {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");

    private final Clock clock;
    private final Supplier<UUID> uuidSupplier;

    public BasicSoapResponsePatcher() {
        this(Clock.systemDefaultZone(), UUID::randomUUID);
    }

    public BasicSoapResponsePatcher(Clock clock, Supplier<UUID> uuidSupplier) {
        this.clock = clock;
        this.uuidSupplier = uuidSupplier;
    }

    @Override
    public MagdaDocument patchResponse(MagdaDocument request, Document response) {
        MagdaDocument madgaDocumentResponse = new MagdaDocument(response);

        var senderReference = request.getValue("//Afzender/Referte");
        madgaDocumentResponse.setValue("//Ontvanger/Referte", senderReference);
        madgaDocumentResponse.setValue("//Antwoord/Referte", senderReference);
        madgaDocumentResponse.setValue("//Ontvanger/Identificatie", request.getValue("//Afzender/Identificatie"));
        madgaDocumentResponse.setValue("//Ontvanger/Hoedanigheid", request.getValue("//Afzender/Hoedanigheid"));

        Optional.ofNullable(request.getValue("//Afzender/Gebruiker"))
                .ifPresentOrElse(user -> madgaDocumentResponse.setValue("//Ontvanger/Gebruiker", user),
                        () -> madgaDocumentResponse.removeNode("//Ontvanger/Gebruiker"));

        LocalDateTime now = LocalDateTime.now(clock);
        madgaDocumentResponse.setValue("//Context/Bericht/Tijdstip/Datum", now.format(DATE_FORMAT));

        madgaDocumentResponse.setValue("//Context/Bericht/Tijdstip/Tijd", now.format(TIME_FORMAT));

        // Identificeert antwoord als komend van Magda Mock
        madgaDocumentResponse.setValue("//Afzender/Referte", uuidSupplier.get().toString());
        madgaDocumentResponse.setValue("//Afzender/Identificatie", "kb.vlaanderen.be/aiv/magda-mock-server");
        madgaDocumentResponse.setValue("//Afzender/Naam", "Magda Mock Server");

        return madgaDocumentResponse;
    }
}
