package be.vlaanderen.vip.magda.magdamock.client.patchers;

import be.vlaanderen.vip.magda.magdamock.utils.MagdaDocument;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;

import java.time.Clock;
import java.util.UUID;
import java.util.function.Supplier;

@Slf4j
public class GeefSociaalStatuutResponsePatcher extends BasicSoapResponsePatcher {

    public GeefSociaalStatuutResponsePatcher() {
        super();
    }

    public GeefSociaalStatuutResponsePatcher(Clock clock, Supplier<UUID> uuidSupplier) {
        super(clock, uuidSupplier);
    }

    @Override
    public MagdaDocument patchResponse(MagdaDocument request, Document response) {
        MagdaDocument magdaDocument = super.patchResponse(request, response);
        magdaDocument.setValue("//INSZ", request.getValue("//INSZ"));

        var socialStatuteNamesRequest = request.getValues("//SociaalStatuut/Naam");
        var socialStatuteNamesResponse = magdaDocument.getValues("//SociaalStatuut/Naam");

        socialStatuteNamesResponse.stream().filter(x -> !socialStatuteNamesRequest.contains(x)).forEach(x -> magdaDocument.removeNode("//SociaalStatuut[Naam[text()='" + x + "']]"));
        socialStatuteNamesRequest.stream().filter(x -> !socialStatuteNamesResponse.contains(x)).forEach(x -> writeNotAppliedSocialStatute(magdaDocument, x));
        return magdaDocument;
    }

    private void writeNotAppliedSocialStatute(MagdaDocument response, String socialStatuteName) {
        /*
            <SociaalStatuut>
                <Naam>SOCIAL_STATUTE_NAME</Naam>
                <Resultaat>
                    <Code>0</Code>
                    <Omschrijving>Niet van toepassing</Omschrijving>
                </Resultaat>
            </SociaalStatuut>
         */
        var socialeStatuten = response.xpath("//SocialeStatuten");
        if(socialeStatuten.getLength() > 0) {
            var doc = response.getXml();
            var sociaalStatuutNode = doc.createElement("SociaalStatuut");
            //Include name
            var nameNode = doc.createElement("Naam");
            nameNode.appendChild(doc.createTextNode(socialStatuteName));
            sociaalStatuutNode.appendChild(nameNode);
            //Include result
            var resultNode = doc.createElement("Resultaat");
            var resultCodeNode = doc.createElement("Code");
            resultCodeNode.appendChild(doc.createTextNode("0"));
            resultNode.appendChild(resultCodeNode);
            var resultDescriptionNode = doc.createElement("Omschrijving");
            resultDescriptionNode.appendChild(doc.createTextNode("Niet van toepassing"));
            resultNode.appendChild(resultDescriptionNode);
            sociaalStatuutNode.appendChild(resultNode);
            //Add to collection
            socialeStatuten.item(0).appendChild(sociaalStatuutNode);
        }
    }
}
