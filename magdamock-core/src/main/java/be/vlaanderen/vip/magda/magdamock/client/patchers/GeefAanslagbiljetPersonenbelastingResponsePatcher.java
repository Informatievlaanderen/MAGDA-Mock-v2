package be.vlaanderen.vip.magda.magdamock.client.patchers;

import be.vlaanderen.vip.magda.magdamock.utils.MagdaMockDocument;
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
    public MagdaMockDocument patchResponse(MagdaMockDocument request, Document response) {
        MagdaMockDocument magdaMockDocument = super.patchResponse(request, response);

        magdaMockDocument.setValue("//Antwoorden/Antwoord/Inhoud/AanslagbiljetPersonenbelasting/Inkomensjaar",
                request.getValue("//Vragen/Vraag/Inhoud/Criteria/Inkomensjaar"));

        return magdaMockDocument;
    }
}
