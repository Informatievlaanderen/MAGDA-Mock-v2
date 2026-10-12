package be.vlaanderen.vip.magda.magdamock.client.patchers;

import be.vlaanderen.vip.magda.magdamock.utils.MagdaMockDocument;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.time.Clock;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.function.Supplier;

@Slf4j
public class GeefFunctiesResponsePatcher extends BasicSoapResponsePatcher {

    public GeefFunctiesResponsePatcher() {
        super();
    }

    public GeefFunctiesResponsePatcher(Clock clock, Supplier<UUID> uuidSupplier) {
        super(clock, uuidSupplier);
    }

    @Override
    public MagdaMockDocument patchResponse(MagdaMockDocument request, Document response) {
        MagdaMockDocument responseDocument = super.patchResponse(request, response);

        var aardFuncties = request.getValues("//AardFuncties/AardFunctie");
        responseDocument = filterAardFuncties(responseDocument, new HashSet<>(aardFuncties));
        var persoonOndernemingsnummer = request.getValue("//Persoon/Ondernemingsnummer");
        responseDocument = filterFunctiehouderVan(responseDocument, persoonOndernemingsnummer);
        var ondernemingsnummerFunctieIn = request.getValue("//Onderneming/Ondernemingsnummer[@Type='FunctieIn']");
        responseDocument = filterFunctiehouderVan(responseDocument, ondernemingsnummerFunctieIn);
        var ondernemingsnummerFunctieHouder = request.getValue("//Onderneming/Ondernemingsnummer[@Type='FunctieHouder']");
        responseDocument = filterOndernemingsnummer(responseDocument, ondernemingsnummerFunctieHouder);

        return responseDocument;
    }

    private MagdaMockDocument filterOndernemingsnummer(MagdaMockDocument responseDocument, String ondernemingsnummer) {
        if (ondernemingsnummer == null) {
            return responseDocument;
        }

        NodeList nodeList = responseDocument.xpath("//Functie/Ondernemingsnummer");
        for (int i=0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            String ondernemingsnummerValue = node.getTextContent();
            if (!ondernemingsnummer.equals(ondernemingsnummerValue)) {
                Node parent = node.getParentNode();
                responseDocument.removeNode(parent);
            }
        }

        return responseDocument;
    }
    private MagdaMockDocument filterFunctiehouderVan(MagdaMockDocument responseDocument, String ondernemingsnummer) {
        if (ondernemingsnummer == null) {
            return responseDocument;
        }

        NodeList nodeList = responseDocument.xpath("//Functie/FunctiehouderVan");
        for (int i=0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            String ondernemingsnummerValue = node.getTextContent();
            if (!ondernemingsnummer.equals(ondernemingsnummerValue)) {
                Node parent = node.getParentNode();
                responseDocument.removeNode(parent);
            }
        }

        return responseDocument;
    }

    private MagdaMockDocument filterAardFuncties(MagdaMockDocument responseDocument, Set<String> aardFuncties) {
        if (aardFuncties.isEmpty()) {
            return responseDocument;
        }

        NodeList nodeList = responseDocument.xpath("//AardFunctie/Code");
        for (int i=0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            String aardFunctie = node.getTextContent();
            if (!aardFuncties.contains(aardFunctie)) {
                Node parent = node.getParentNode().getParentNode();
                responseDocument.removeNode(parent);
            }
        }

        return responseDocument;
    }
}
