package be.vlaanderen.vip.magda.magdamock.client.soap;

import be.vlaanderen.vip.magda.client.MagdaDocument;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;

import java.time.Clock;
import java.util.UUID;
import java.util.function.Supplier;

@Slf4j
public class GeefAanslagbiljetPersonenbelastingResponsePatcher extends BasicSoapResponsePatcher {

    public GeefAanslagbiljetPersonenbelastingResponsePatcher() {
        super();
    }

    public GeefAanslagbiljetPersonenbelastingResponsePatcher(Clock clock, Supplier<UUID> uuidSupplier) {
        super(clock, uuidSupplier);
    }

    @Override
    public MagdaDocument patchResponse(MagdaDocument request, Document response) {
        MagdaDocument magdaDocument = super.patchResponse(request, response);

        magdaDocument.setValue("//Antwoorden/Antwoord/Inhoud/AanslagbiljetPersonenbelasting/Inkomensjaar",
                request.getValue("//Vragen/Vraag/Inhoud/Criteria/Inkomensjaar"));

        return magdaDocument;
    }
}
