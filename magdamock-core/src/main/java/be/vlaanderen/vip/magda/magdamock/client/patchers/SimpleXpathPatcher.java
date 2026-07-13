package be.vlaanderen.vip.magda.magdamock.client.patchers;

import be.vlaanderen.vip.magda.magdamock.utils.MagdaDocument;
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
    public MagdaDocument patchResponse(MagdaDocument request, Document response) {
        MagdaDocument magdaDocument = super.patchResponse(request, response);
        magdaDocument.setValue(xpathDestination, request.getValue(xpathSource));

        return magdaDocument;
    }

}
