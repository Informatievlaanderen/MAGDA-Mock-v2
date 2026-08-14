package be.vlaanderen.vip.magda.magdamock.client.patchers;

import be.vlaanderen.vip.magda.magdamock.utils.MagdaMockDocument;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    public MagdaMockDocument patchResponse(MagdaMockDocument request, Document response) {
        try {
            MagdaMockDocument madgaDocumentResponse = constructContext(request, response);

            madgaDocumentResponse.setValue("//Antwoord/Referte", uuidSupplier.get().toString());

            LocalDateTime now = LocalDateTime.now(clock);
            madgaDocumentResponse.setValue("//Uitzonderingen/Uitzondering/Tijdstip/Datum", now.format(DATE_FORMAT));
            madgaDocumentResponse.setValue("//Uitzonderingen/Uitzondering/Tijdstip/Tijd", now.format(TIME_FORMAT));

            madgaDocumentResponse.setValue("//Context/Bericht/Tijdstip/Datum", now.format(DATE_FORMAT));
            madgaDocumentResponse.setValue("//Context/Bericht/Tijdstip/Tijd", now.format(TIME_FORMAT));

            madgaDocumentResponse.setValue("//Context/Bericht/Type", "ANTWOORD");

            return madgaDocumentResponse;
        } catch (Exception e) {
            log.error("Exception while patching SOAP response", e);
            return new MagdaMockDocument(response);
        }
    }

    public MagdaMockDocument constructContext(MagdaMockDocument request, Document response) {
        try {
            Document requestDoc = request.getXml();
            NodeList contextNodes = requestDoc.getElementsByTagName("Context");
            NodeList oldContextNodes = response.getElementsByTagName("Context");
            for (int i = 0; i < oldContextNodes.getLength(); i++) {
                Node oldContext = oldContextNodes.item(i);
                oldContext.getParentNode().removeChild(oldContext);
            }

            if (contextNodes.getLength() != 0) {
                Node requestContext = contextNodes.item(0);
                Node importedContext = response.importNode(requestContext, true);

                NodeList repliekNodes = response.getElementsByTagName("Repliek");
                Node repliekElement = repliekNodes.item(0);
                repliekElement.insertBefore(importedContext, repliekElement.getFirstChild());

                NodeList afzenderNodes = ((Element) importedContext).getElementsByTagName("Afzender");
                if (afzenderNodes.getLength() > 0) {
                    NodeList oldOntvangerNodes = response.getElementsByTagName("Ontvanger");
                    for (int i = 0; i < oldOntvangerNodes.getLength(); i++) {
                        Node toRemoveNode = oldOntvangerNodes.item(i);
                        toRemoveNode.getParentNode().removeChild(toRemoveNode);
                    }


                    // Move all children from afzender to ontvanger
                    Node oldAfzender = afzenderNodes.item(0);
                    Node ontvanger = response.createElement("Ontvanger");
                    while (oldAfzender.hasChildNodes()) {
                        ontvanger.appendChild(oldAfzender.getFirstChild());
                    }
                    oldAfzender.getParentNode().replaceChild(ontvanger, oldAfzender);

                    // Afzender identificatie voor Magda Mock
                    Node newAfzender = response.createElement("Afzender");
                    Element identificatie = response.createElement("Identificatie");
                    identificatie.setTextContent("kb.vlaanderen.be/aiv/magda-mock-server");
                    newAfzender.appendChild(identificatie);
                    Element naam = response.createElement("Naam");
                    naam.setTextContent("Magda Mock Server");
                    newAfzender.appendChild(naam);
                    Node referte = response.createElement("Referte");
                    referte.setTextContent(uuidSupplier.get().toString());
                    newAfzender.appendChild(referte);

                    ontvanger.getParentNode().insertBefore(newAfzender, ontvanger);
                }
            }
            return new MagdaMockDocument(response);
        } catch (Exception e) {
            log.error("Exception while constructing context in SOAP response", e);
            return new MagdaMockDocument(response);
        }
    }
}
