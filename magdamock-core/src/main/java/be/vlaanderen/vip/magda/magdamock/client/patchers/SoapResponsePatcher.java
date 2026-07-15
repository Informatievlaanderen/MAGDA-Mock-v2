package be.vlaanderen.vip.magda.magdamock.client.patchers;

import be.vlaanderen.vip.magda.magdamock.utils.MagdaMockDocument;
import org.w3c.dom.Document;

public interface SoapResponsePatcher {

    MagdaMockDocument patchResponse(MagdaMockDocument request, Document response);

}
