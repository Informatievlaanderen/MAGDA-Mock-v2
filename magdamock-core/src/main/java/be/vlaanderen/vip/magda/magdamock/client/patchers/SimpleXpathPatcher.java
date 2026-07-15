package be.vlaanderen.vip.magda.magdamock.client.patchers;

import be.vlaanderen.vip.magda.magdamock.utils.MagdaMockDocument;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;

import java.time.Clock;
import java.util.UUID;
import java.util.function.Supplier;

@Slf4j
public class SimpleXpathPatcher extends BasicSoapResponsePatcher {
    private final String xpathDestination;
    private final String xpathSource;

    public SimpleXpathPatcher(String xpathDestination, String xpathSource) {
        super();
        this.xpathDestination = xpathDestination;
        this.xpathSource = xpathSource;
    }

    public SimpleXpathPatcher(Clock clock, Supplier<UUID> uuidSupplier, String xpathDestination, String xpathSource) {
        super(clock, uuidSupplier);
        this.xpathDestination = xpathDestination;
        this.xpathSource = xpathSource;
    }

    @Override
    public MagdaMockDocument patchResponse(MagdaMockDocument request, Document response) {
        MagdaMockDocument magdaMockDocument = super.patchResponse(request, response);
        magdaMockDocument.setValue(xpathDestination, request.getValue(xpathSource));

        return magdaMockDocument;
    }

}
