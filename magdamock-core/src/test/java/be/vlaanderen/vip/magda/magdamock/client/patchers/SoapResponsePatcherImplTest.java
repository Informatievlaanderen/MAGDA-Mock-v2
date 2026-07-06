package be.vlaanderen.vip.magda.magdamock.client.patchers;

import be.vlaanderen.vip.magda.client.MagdaDocument;
import be.vlaanderen.vip.magda.client.MagdaServiceIdentification;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;

import java.util.HashMap;
import java.util.Map;

import static be.vlaanderen.vip.magda.magdamock.client.soap.SoapStubRegistrar.VERSION_01_00;
import static be.vlaanderen.vip.magda.magdamock.client.soap.SoapStubRegistrar.VERSION_02_00;
import static be.vlaanderen.vip.magda.magdamock.client.soap.SoapStubRegistrar.VERSION_03_00;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class SoapResponsePatcherImplTest {

    @Test
    void shouldDelegateToSpecificPatcherWhenServiceIdentificationMatches() {
        SoapResponsePatcher geefSociaalStatuutPatcher = mock(SoapResponsePatcher.class);
        SoapResponsePatcher geefAanslagbiljetPersonenbelastingPatcher = mock(SoapResponsePatcher.class);
        SoapResponsePatcher basePatcher = mock(SoapResponsePatcher.class);

        MagdaServiceIdentification serviceIdentification =
                new MagdaServiceIdentification("GeefSociaalStatuut", VERSION_03_00);

        MagdaServiceIdentification serviceIdentification2 =
                new MagdaServiceIdentification("GeefAanslagbiljetPersonenbelasting", VERSION_02_00);

        Map<MagdaServiceIdentification, SoapResponsePatcher> patcherMap = new HashMap<>();
        patcherMap.put(serviceIdentification, geefSociaalStatuutPatcher);
        patcherMap.put(serviceIdentification2, geefAanslagbiljetPersonenbelastingPatcher);

        SoapResponsePatcherImpl soapResponsePatcher =
                new SoapResponsePatcherImpl(patcherMap, basePatcher);

        MagdaDocument request = mock(MagdaDocument.class);
        Document response = mock(Document.class);
        MagdaDocument expected = mock(MagdaDocument.class);

        when(request.getServiceIdentification()).thenReturn(serviceIdentification);
        when(geefSociaalStatuutPatcher.patchResponse(request, response)).thenReturn(expected);

        MagdaDocument result = soapResponsePatcher.patchResponse(request, response);

        assertSame(expected, result);
        verify(geefSociaalStatuutPatcher).patchResponse(request, response);
        verifyNoInteractions(basePatcher);
    }

    @Test
    void shouldDelegateToGeefAanslagbiljetPersonenbelastingPatcher() {
        SoapResponsePatcher geefAanslagbiljetPersonenbelastingPatcher = mock(SoapResponsePatcher.class);
        SoapResponsePatcher geefSociaalStatuutPatcher = mock(SoapResponsePatcher.class);
        SoapResponsePatcher basePatcher = mock(SoapResponsePatcher.class);

        MagdaServiceIdentification serviceIdentification =
                new MagdaServiceIdentification("GeefAanslagbiljetPersonenbelasting", VERSION_02_00);

        MagdaServiceIdentification serviceIdentification2 =
                new MagdaServiceIdentification("GeefSociaalStatuut", VERSION_03_00);

        Map<MagdaServiceIdentification, SoapResponsePatcher> patcherMap = new HashMap<>();
        patcherMap.put(serviceIdentification, geefAanslagbiljetPersonenbelastingPatcher);
        patcherMap.put(serviceIdentification2, geefSociaalStatuutPatcher);

        SoapResponsePatcherImpl soapResponsePatcher =
                new SoapResponsePatcherImpl(patcherMap, basePatcher);

        MagdaDocument request = mock(MagdaDocument.class);
        Document response = mock(Document.class);
        MagdaDocument expected = mock(MagdaDocument.class);

        when(request.getServiceIdentification()).thenReturn(serviceIdentification);
        when(geefAanslagbiljetPersonenbelastingPatcher.patchResponse(request, response)).thenReturn(expected);

        MagdaDocument result = soapResponsePatcher.patchResponse(request, response);

        assertSame(expected, result);
        verify(geefAanslagbiljetPersonenbelastingPatcher).patchResponse(request, response);
        verifyNoInteractions(basePatcher);
    }

    @Test
    void shouldDelegateToBasePatcherWhenNoSpecificPatcherIsFound() {

        MagdaServiceIdentification serviceIdentification =
                new MagdaServiceIdentification("GeefAanslagbiljetPersonenbelasting", VERSION_02_00);

        MagdaServiceIdentification serviceIdentification2 =
                new MagdaServiceIdentification("GeefSociaalStatuut", VERSION_03_00);

        SoapResponsePatcher geefAanslagbiljetPersonenbelastingPatcher = mock(SoapResponsePatcher.class);
        SoapResponsePatcher geefSociaalStatuutPatcher = mock(SoapResponsePatcher.class);

        Map<MagdaServiceIdentification, SoapResponsePatcher> patcherMap = new HashMap<>();
        patcherMap.put(serviceIdentification, geefAanslagbiljetPersonenbelastingPatcher);
        patcherMap.put(serviceIdentification2, geefSociaalStatuutPatcher);

        SoapResponsePatcher basePatcher = mock(SoapResponsePatcher.class);

        SoapResponsePatcherImpl soapResponsePatcher =
                new SoapResponsePatcherImpl(new HashMap<>(), basePatcher);

        MagdaDocument request = mock(MagdaDocument.class);
        Document response = mock(Document.class);
        MagdaDocument expected = mock(MagdaDocument.class);

        when(request.getServiceIdentification())
                .thenReturn(new MagdaServiceIdentification("UnknownService", VERSION_01_00));
        when(basePatcher.patchResponse(request, response)).thenReturn(expected);

        MagdaDocument result = soapResponsePatcher.patchResponse(request, response);

        assertSame(expected, result);
        verify(basePatcher).patchResponse(request, response);
    }

}
