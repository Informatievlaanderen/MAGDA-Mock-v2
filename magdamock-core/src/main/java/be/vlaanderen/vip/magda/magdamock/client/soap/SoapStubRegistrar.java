package be.vlaanderen.vip.magda.magdamock.client.soap;

import be.vlaanderen.vip.magda.magdamock.utils.MagdaMockDocument;
import com.github.tomakehurst.wiremock.WireMockServer;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SoapStubRegistrar {

    public static final String VERSION_01_00 = "01.00.0000";
    public static final String VERSION_02_00 = "02.00.0000";
    public static final String VERSION_02_01 = "02.01.0000";
    public static final String VERSION_02_02 = "02.02.0000";
    public static final String VERSION_03_00 = "03.00.0000";

    public static final String KEY_INSZ = "//INSZ";
    public static final String KEY_ONDERNEMINGSNUMMER = "//Ondernemingsnummer";
    public static final String KEY_BOEKJAAR = "//Boekjaar";
    public static final String KEY_RRNR = "//rrnr";
    public static final String KEY_SSIN = "//ssin";
    public static final String KEY_EIGENDOMID = "//EigendomId";
    public static final String KEY_EIGENDOMSTOESTANDID = "//EigendomstoestandId";
    public static final String KEY_DOSSIERNUMMER = "//Dossiernummer";
    public static final String KEY_KADASTRALE_AFDELING = "//KadastraleAfdeling";
    public static final String KEY_SECTIE = "//Sectie";
    public static final String KEY_GRONDNUMMER = "//Grondnummer";
    public static final String KEY_GEBOUWID = "//GebouwId";

    private final Map<MagdaMockDocument.MagdaServiceIdentification, SoapStubHandler> soapStubHandlerMap;

    public SoapStubRegistrar(Map<MagdaMockDocument.MagdaServiceIdentification, SoapStubHandler> soapStubHandlerMap) {
        this.soapStubHandlerMap = soapStubHandlerMap;
    }

    public SoapStubRegistrar(WireMockServer wireMockServer, String soapTestPath) {
        this.soapStubHandlerMap = createHandlers(wireMockServer, soapTestPath);
    }

    public void registerDomain(Domain domain) {
        domain.services().forEach(service ->
                service.versions().forEach(version ->
                        version.files().forEach(file -> registerFile(domain, service, version, file))
                )
        );
    }

    private void registerFile(Domain domain, Service service, Version version, String file) {
        try {
            SoapStubHandler soapStubHandler = determineSoapStubHandler(service.name(), version.name());
            if (soapStubHandler != null) {
                soapStubHandler.register(
                        domain.name(),
                        service.name(),
                        version.name(),
                        file
                );
            }
        } catch (IOException e) {
            throw new IllegalStateException("SOAP file can not be registered", e);
        }
    }

    private SoapStubHandler determineSoapStubHandler(String service, String version) {
        return soapStubHandlerMap.get(new MagdaMockDocument.MagdaServiceIdentification(service, version));
    }

    private static Map<MagdaMockDocument.MagdaServiceIdentification, SoapStubHandler> createHandlers(
            WireMockServer wireMockServer,
            String soapTestPath
    ) {
        return SoapStubDefinitions.allDefinitions().stream()
                .collect(Collectors.toMap(
                        definition -> new MagdaMockDocument.MagdaServiceIdentification(definition.service(), definition.version()),
                        definition -> definition.createHandler(wireMockServer, soapTestPath)
                ));
    }

    static class SoapStubDefinitions {

        private SoapStubDefinitions() {
        }

        static List<SoapStubDefinition> allDefinitions() {
            return List.of(
                    // Dossier
                    subDir("GeefDossiers", VERSION_02_00, KEY_INSZ),

                    // Gezin
                    subDir("GeefKindVoordelen", VERSION_02_00, KEY_INSZ),

                    // Inkomen
                    flatFile("GeefAanslagbiljetPersonenbelasting", VERSION_02_00, KEY_INSZ, "//Criteria/Inkomensjaar"),

                    // Kadaster
                    subDir("GeefCadNetTransacties", VERSION_01_00, KEY_INSZ),
                    subDir("GeefEigendomstoestanden", VERSION_02_00, KEY_EIGENDOMID),
                    subDir("GeefHistoriekEigendomstoestand", VERSION_03_00, KEY_EIGENDOMSTOESTANDID),
                    subDir("GeefHistoriekMutatiedossier", VERSION_03_00, KEY_DOSSIERNUMMER),
                    subDir("GeefKadastraleAfdelingenOpKBO", VERSION_01_00, KEY_ONDERNEMINGSNUMMER),
                    subDir("GeefTransacties", VERSION_03_00, KEY_INSZ),
                    subDir("ZoekEigendomstoestanden", VERSION_02_00, KEY_INSZ),
                    subDir("ZoekPerceel", VERSION_02_00, KEY_KADASTRALE_AFDELING, KEY_SECTIE, KEY_GRONDNUMMER),
                    subDir("ZoekVerkoopprijzen", VERSION_03_00,
                            "//Criteria/Provincie",
                            "//Criteria/AdministratieveGemeentes/AdministratieveGemeente",
                            "//Criteria/TypesInschrijving/TypeInschrijving",
                            "//Criteria/CodesKadastraleAardVolgensAkte/CodeKadastraleAardVolgensAkte"),

                    // LED
                    subDir("AnnuleerBewijs", VERSION_02_00, KEY_INSZ),
                    subDir("GeefBewijs", VERSION_02_00, KEY_INSZ),
                    subDir("RegistreerBewijs", VERSION_02_00, KEY_INSZ),
                    subDir("RegistreerMutatieBewijs", VERSION_02_00, KEY_INSZ),

                    // Onderneming
                    subDir("GeefAdressenLocaties", VERSION_02_00, KEY_ONDERNEMINGSNUMMER),
                    subDir("GeefBeschikbareJaarrekeningen", VERSION_02_00, KEY_ONDERNEMINGSNUMMER),
                    subDir("GeefDeelnemingen", VERSION_02_00, KEY_ONDERNEMINGSNUMMER),
                    subDir("GeefFiscaleInhoudingsplicht", VERSION_02_01, KEY_ONDERNEMINGSNUMMER),
                    subDir("GeefFiscaleSchuld", VERSION_02_00, KEY_ONDERNEMINGSNUMMER),
                    subDir("GeefFuncties", VERSION_02_00, KEY_INSZ),
                    subDir("GeefJaarrekeningen", VERSION_02_00, KEY_ONDERNEMINGSNUMMER, KEY_BOEKJAAR),
                    subDir("GeefOnderneming", VERSION_02_00, KEY_ONDERNEMINGSNUMMER),
                    subDir("GeefOndernemingSignalen", VERSION_02_00, KEY_ONDERNEMINGSNUMMER),
                    subDir("GeefOndernemingVKBO", VERSION_02_00, KEY_ONDERNEMINGSNUMMER),
                    subDir("GeefPCenTW", VERSION_02_00, KEY_ONDERNEMINGSNUMMER),
                    subDir("GeefSocialeSchuld", VERSION_02_00, KEY_ONDERNEMINGSNUMMER),
                    subDir("GeefTewerkstelling", VERSION_02_00, KEY_ONDERNEMINGSNUMMER),
                    flatFile("ZoekOnderneming", VERSION_02_00, "//Criteria/Naam", "//Criteria/Adres/GemeenteNISCode"),

                    // Onderwijs
                    subDir("GeefHistoriekInschrijving", VERSION_02_01, KEY_INSZ),

                    // Persoon
                    subDir("GeefAttest", VERSION_02_00, KEY_INSZ),
                    subDir("GeefGezinssamenstelling", VERSION_02_00, KEY_INSZ),
                    subDir("GeefGezinssamenstelling", VERSION_02_02, KEY_INSZ),
                    subDir("GeefHistoriekGezinssamenstelling", VERSION_02_02, KEY_INSZ),
                    subDir("GeefHistoriekPersoon", VERSION_02_00, KEY_INSZ),
                    subDir("GeefHistoriekPersoon", VERSION_02_02, KEY_INSZ),
                    subDir("GeefPasfoto", VERSION_02_00, KEY_INSZ),
                    subDir("GeefPersoon", VERSION_02_02, KEY_INSZ),
                    subDir("GeefPersoonMutatiesNotificaties", VERSION_02_00, "//Inhoud/Ontvangstreferte"),
                    subDir("RaadpleegLeerkredietsaldo", VERSION_01_00, "//Inhoud/Ontvangstreferte"),
                    flatFile("ZoekPersoonOpAdres", VERSION_02_02,
                            "//Inhoud/Bron",
                            "//Criteria/Adres/PostCode",
                            "//Criteria/Adres/Straatcode",
                            "//Criteria/Adres/Huisnummer",
                            "//Criteria/EnkelReferentiepersoon"),
                    subDir("ZoekPersoonOpNaam", VERSION_02_02,
                            "//Inhoud/Bron",
                            "//Criteria/Naam/Achternaam",
                            "//Criteria/Geboorte/Datum"),

                    // Repertorium
                    subDir("RegistreerInschrijving", VERSION_02_00, KEY_INSZ),
                    subDir("RegistreerInschrijving", VERSION_02_01, "//Subject/Type", "//Subject/Sleutel"),
                    subDir("RegistreerUitschrijving", VERSION_02_00, KEY_INSZ),

                    // SocEcon
                    subDir("GeefStatusRechtOndersteuningen", VERSION_02_00, KEY_INSZ),

                    // SocSec
                    subDir("GeefBetalingenHandicap", VERSION_03_00, KEY_SSIN),
                    subDir("GeefDossierHandicap", VERSION_03_00, KEY_SSIN),
                    subDir("GeefLeefloonbedragen", VERSION_02_00, KEY_INSZ),
                    subDir("GeefSociaalStatuut", VERSION_03_00, KEY_INSZ),
                    subDir("GeefVolledigDossierHandicap", VERSION_03_00, KEY_RRNR),

                    // Vastgoed
                    subDir("GeefEpc", VERSION_02_01,
                            "//Criteria/Adres/Postcode",
                            "//Criteria/Adres/Straat",
                            "//Criteria/Adres/Huisnummer"),

                    // Werk
                    subDir("GeefLoopbaanARZA", VERSION_02_01, KEY_INSZ),
                    subDir("GeefLoopbaanonderbrekingen", VERSION_02_00, KEY_INSZ),
                    subDir("GeefWerkrelaties", VERSION_02_00, KEY_INSZ),
                    subDir("GeefDmfaVoorWerknemer", VERSION_03_00, KEY_INSZ),

                    //Vlok
                    flatFile("GeefWoningKwaliteit", VERSION_02_00, "//NISCode", "//Type", "//Referte"),
                    flatFile("ZoekWoningKwaliteit", VERSION_02_00, "//NISCode", "//Zoekterm"),
                    flatFile("GeefWoningKwaliteitBijlage", VERSION_02_00, "//NISCode", "//Type", "//Referte"),
                    flatFile("BewaarWoningKwaliteit", VERSION_02_00, "//Criteria/NISCode", "//Criteria/NISCode/following-sibling::*[1]/name()"),
                    // following-sibling::*[1]/name() -> tag name of the element after NISCode
                    flatFile("BewaarWoningKwaliteitBijlage", VERSION_02_00, "//Criteria/NISCode")

            );
        }

        private static SoapStubDefinition subDir(String service, String version, String... keys) {
            return new SoapStubDefinition(
                    service,
                    version,
                    (wireMockServer, soapTestPath) ->
                            new SubDirSOAPStubHandler(wireMockServer, soapTestPath, List.of(keys))
            );
        }

        private static SoapStubDefinition flatFile(String service, String version, String... keys) {
            return new SoapStubDefinition(
                    service,
                    version,
                    (wireMockServer, soapTestPath) ->
                            new SubDirSOAPStubHandler(wireMockServer, soapTestPath, List.of(keys), "&")
            );
        }

        private static SoapStubDefinition pasfoto(String service, String version) {
            return new SoapStubDefinition(
                    service,
                    version,
                    GeefPasfotoStubHandler::new
            );
        }
    }

    record SoapStubDefinition(
            String service,
            String version,
            SoapStubHandlerFactory factory
    ) {
        SoapStubHandler createHandler(WireMockServer wireMockServer, String soapTestPath) {
            return factory.create(wireMockServer, soapTestPath);
        }
    }

    @FunctionalInterface
    interface SoapStubHandlerFactory {
        SoapStubHandler create(WireMockServer wireMockServer, String soapTestPath);
    }

}
