package be.vlaanderen.vip.magda.magdamock.client.soap;

import be.vlaanderen.vip.magda.client.MagdaServiceIdentification;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static be.vlaanderen.vip.magda.magdamock.client.soap.SoapStubRegistrar.VERSION_01_00;
import static be.vlaanderen.vip.magda.magdamock.client.soap.SoapStubRegistrar.VERSION_02_00;
import static be.vlaanderen.vip.magda.magdamock.client.soap.SoapStubRegistrar.VERSION_02_01;
import static be.vlaanderen.vip.magda.magdamock.client.soap.SoapStubRegistrar.VERSION_02_02;
import static be.vlaanderen.vip.magda.magdamock.client.soap.SoapStubRegistrar.VERSION_03_00;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SoapStubRegistrarTest {

    @Test
    void all_shouldContainAllRegisteredServicesAndVersions() {
        List<SoapStubRegistrar.SoapStubDefinition> definitions = SoapStubRegistrar.SoapStubDefinitions.allDefinitions();

        Set<String> actual = definitions.stream()
                .map(definition -> definition.service() + "|" + definition.version())
                .collect(Collectors.toSet());

        // Dossier
        assertContains(actual, "GeefDossiers", VERSION_02_00);

        // Gezin
        assertContains(actual, "GeefKindVoordelen", VERSION_02_00);

        // Inkomen
        assertContains(actual, "GeefAanslagbiljetPersonenbelasting", VERSION_02_00);

        // Kadaster
        assertContains(actual, "GeefCadNetTransacties", VERSION_01_00);
        assertContains(actual, "GeefEigendomstoestanden", VERSION_02_00);
        assertContains(actual, "GeefHistoriekEigendomstoestand", VERSION_03_00);
        assertContains(actual, "GeefHistoriekMutatiedossier", VERSION_03_00);
        assertContains(actual, "GeefKadastraleAfdelingenOpKBO", VERSION_01_00);
        assertContains(actual, "GeefTransacties", VERSION_03_00);
        assertContains(actual, "ZoekEigendomstoestanden", VERSION_02_00);
        assertContains(actual, "ZoekPerceel", VERSION_02_00);
        assertContains(actual, "ZoekVerkoopprijzen", VERSION_03_00);

        // LED
        assertContains(actual, "AnnuleerBewijs", VERSION_02_00);
        assertContains(actual, "GeefBewijs", VERSION_02_00);
        assertContains(actual, "RegistreerBewijs", VERSION_02_00);
        assertContains(actual, "RegistreerMutatieBewijs", VERSION_02_00);

        // Onderneming
        assertContains(actual, "GeefAdressenLocaties", VERSION_02_00);
        assertContains(actual, "GeefBeschikbareJaarrekeningen", VERSION_02_00);
        assertContains(actual, "GeefDeelnemingen", VERSION_02_00);
        assertContains(actual, "GeefFiscaleInhoudingsplicht", VERSION_02_01);
        assertContains(actual, "GeefFiscaleSchuld", VERSION_02_00);
        assertContains(actual, "GeefFuncties", VERSION_02_00);
        assertContains(actual, "GeefJaarrekeningen", VERSION_02_00);
        assertContains(actual, "GeefOnderneming", VERSION_02_00);
        assertContains(actual, "GeefOndernemingSignalen", VERSION_02_00);
        assertContains(actual, "GeefOndernemingVKBO", VERSION_02_00);
        assertContains(actual, "GeefPCenTW", VERSION_02_00);
        assertContains(actual, "GeefSocialeSchuld", VERSION_02_00);
        assertContains(actual, "GeefTewerkstelling", VERSION_02_00);
        assertContains(actual, "ZoekOnderneming", VERSION_02_00);

        // Onderwijs
        assertContains(actual, "GeefHistoriekInschrijving", VERSION_02_01);

        // Persoon
        assertContains(actual, "GeefAttest", VERSION_02_00);
        assertContains(actual, "GeefGezinssamenstelling", VERSION_02_00);
        assertContains(actual, "GeefGezinssamenstelling", VERSION_02_02);
        assertContains(actual, "GeefHistoriekGezinssamenstelling", VERSION_02_02);
        assertContains(actual, "GeefHistoriekPersoon", VERSION_02_00);
        assertContains(actual, "GeefHistoriekPersoon", VERSION_02_02);
        assertContains(actual, "GeefPasfoto", VERSION_02_00);
        assertContains(actual, "GeefPersoon", VERSION_02_02);
        assertContains(actual, "GeefPersoonMutatiesNotificaties", VERSION_02_00);
        assertContains(actual, "RaadpleegLeerkredietsaldo", VERSION_01_00);
        assertContains(actual, "ZoekPersoonOpAdres", VERSION_02_02);
        assertContains(actual, "ZoekPersoonOpNaam", VERSION_02_02);

        // Repertorium
        assertContains(actual, "RegistreerInschrijving", VERSION_02_00);
        assertContains(actual, "RegistreerInschrijving", VERSION_02_01);
        assertContains(actual, "RegistreerUitschrijving", VERSION_02_00);

        // SocEcon
        assertContains(actual, "GeefStatusRechtOndersteuningen", VERSION_02_00);

        // SocSec
        assertContains(actual, "GeefBetalingenHandicap", VERSION_03_00);
        assertContains(actual, "GeefDossierHandicap", VERSION_03_00);
        assertContains(actual, "GeefLeefloonbedragen", VERSION_02_00);
        assertContains(actual, "GeefSociaalStatuut", VERSION_03_00);
        assertContains(actual, "GeefVolledigDossierHandicap", VERSION_03_00);

        // Vastgoed
        assertContains(actual, "GeefEpc", VERSION_02_01);

        // Werk
        assertContains(actual, "GeefLoopbaanARZA", VERSION_02_01);
        assertContains(actual, "GeefLoopbaanonderbrekingen", VERSION_02_00);
        assertContains(actual, "GeefWerkrelaties", VERSION_02_00);
        assertContains(actual, "GeefDmfaVoorWerknemer", VERSION_03_00);

        //Vlok
        assertContains(actual, "GeefWoningKwaliteit", VERSION_02_00);
        assertContains(actual, "ZoekWoningKwaliteit", VERSION_02_00);
        assertContains(actual, "GeefWoningKwaliteitBijlage", VERSION_02_00);

    }

    @Test
    void createHandler_shouldCreateSubDirHandler() {
        var definition = SoapStubRegistrar.SoapStubDefinitions.allDefinitions().stream()
                .filter(d -> d.service().equals("GeefPersoon") && d.version().equals(VERSION_02_02))
                .findFirst()
                .orElseThrow();

        SoapStubHandler handler = definition.createHandler(mock(WireMockServer.class), "soap");

        assertInstanceOf(SubDirSOAPStubHandler.class, handler);
    }

    @Test
    void createHandler_shouldCreatePasfotoHandler() {
        var definition = SoapStubRegistrar.SoapStubDefinitions.allDefinitions().stream()
                .filter(d -> d.service().equals("GeefPasfoto") && d.version().equals(VERSION_02_00))
                .findFirst()
                .orElseThrow();

        SoapStubHandler handler = definition.createHandler(mock(WireMockServer.class), "soap");

        assertInstanceOf(GeefPasfotoStubHandler.class, handler);
    }

    @Test
    void registerDomain_shouldRegisterAllFiles() throws IOException {
        SoapStubHandler handler = mock(SoapStubHandler.class);

        SoapStubRegistrar registrar = new SoapStubRegistrar(
                Map.of(new MagdaServiceIdentification("GeefPersoon", VERSION_02_02), handler)
        );

        Version version = mock(Version.class);
        when(version.name()).thenReturn(VERSION_02_02);
        when(version.files()).thenReturn(List.of("a.xml", "b.xml"));

        Service service = mock(Service.class);
        when(service.name()).thenReturn("GeefPersoon");
        when(service.versions()).thenReturn(List.of(version));

        Domain domain = mock(Domain.class);
        when(domain.name()).thenReturn("Persoon");
        when(domain.services()).thenReturn(List.of(service));

        registrar.registerDomain(domain);

        verify(handler).register("Persoon", "GeefPersoon", VERSION_02_02, "a.xml");
        verify(handler).register("Persoon", "GeefPersoon", VERSION_02_02, "b.xml");
    }

    private void assertContains(Set<String> actual, String service, String version) {
        assertTrue(
                actual.contains(service + "|" + version),
                () -> "Missing registration for " + service + " / " + version
        );
    }

}
