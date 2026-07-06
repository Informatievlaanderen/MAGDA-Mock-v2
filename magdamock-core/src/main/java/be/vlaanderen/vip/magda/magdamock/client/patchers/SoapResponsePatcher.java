package be.vlaanderen.vip.magda.magdamock.client.patchers;

import be.vlaanderen.vip.magda.client.MagdaDocument;
import org.w3c.dom.Document;

public interface SoapResponsePatcher {

    MagdaDocument patchResponse(MagdaDocument request, Document response);

}
