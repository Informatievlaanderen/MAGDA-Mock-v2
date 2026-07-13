package be.vlaanderen.vip.magda.magdamock.client.patchers;

import be.vlaanderen.vip.magda.magdamock.utils.MagdaDocument;
import be.vlaanderen.vip.magda.client.MagdaServiceIdentification;
import org.w3c.dom.Document;

import java.util.HashMap;
import java.util.Map;

public class SoapResponsePatcherImpl implements SoapResponsePatcher {

    private final Map<MagdaDocument.MagdaServiceIdentification, SoapResponsePatcher> soapResponsePatcherMap;
    private final SoapResponsePatcher baseSoapResponsePatcher;

    private static final String VERSION_02_00 = "02.00.0000";
    private static final String VERSION_03_00 = "03.00.0000";

    public SoapResponsePatcherImpl() {
        this(createDefaultPatcherMap(), new BasicSoapResponsePatcher());
    }

    public SoapResponsePatcherImpl(Map<MagdaDocument.MagdaServiceIdentification, SoapResponsePatcher> soapResponsePatcherMap,
                            SoapResponsePatcher baseSoapResponsePatcher) {
        this.soapResponsePatcherMap = soapResponsePatcherMap;
        this.baseSoapResponsePatcher = baseSoapResponsePatcher;
    }

    private static Map<MagdaDocument.MagdaServiceIdentification, SoapResponsePatcher> createDefaultPatcherMap() {
        Map<MagdaDocument.MagdaServiceIdentification, SoapResponsePatcher> patcherMap = new HashMap<>();
        patcherMap.put(
                new MagdaDocument.MagdaServiceIdentification("GeefAanslagbiljetPersonenbelasting", VERSION_02_00),
                new GeefAanslagbiljetPersonenbelastingResponsePatcher()
        );
        patcherMap.put(
                new MagdaDocument.MagdaServiceIdentification("GeefSociaalStatuut", VERSION_03_00),
                new GeefSociaalStatuutResponsePatcher()
        );
        patcherMap.put(
                new MagdaDocument.MagdaServiceIdentification("GeefPasfoto", VERSION_02_00),
                new SimpleXpathPatcher("//Inhoud/Pasfoto/INSZ", "//Criteria/INSZ")
        );
        return patcherMap;
    }

    @Override
    public MagdaDocument patchResponse(MagdaDocument request, Document response) {
        SoapResponsePatcher soapResponsePatcher = soapResponsePatcherMap.get(request.getServiceIdentification());
        if (soapResponsePatcher != null) {
            return soapResponsePatcher.patchResponse(request, response);
        }
        return baseSoapResponsePatcher.patchResponse(request, response);
    }
}
