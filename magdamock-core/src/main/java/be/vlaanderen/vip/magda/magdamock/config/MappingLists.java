package be.vlaanderen.vip.magda.magdamock.config;

import java.util.List;

public interface MappingLists {
    String SEPERATOR_DIRECTORY = "/";
    String SEPERATOR_FILE_NAME = "&";

    String VERSION_01_00 = "01.00.0000";
    String VERSION_02_00 = "02.00.0000";
    String VERSION_02_01 = "02.01.0000";
    String VERSION_02_02 = "02.02.0000";
    String VERSION_03_00 = "03.00.0000";
    String KEY_INSZ = "//INSZ";
    String KEY_RRNR = "//rrnr";
    String KEY_SSIN = "//ssin";
    String KEY_INSS = "//INSS";
    String KEY_ONDERNEMINGSNUMMER = "//Ondernemingsnummer";
    String KEY_BOEKJAAR = "//Boekjaar";
    String KEY_EIGENDOMID = "//EigendomId";
    String KEY_EIGENDOMSTOESTANDID = "//EigendomstoestandId";
    String KEY_DOSSIERNUMMER = "//Dossiernummer";
    String KEY_KADASTRALE_AFDELING = "//KadastraleAfdeling";
    String KEY_SECTIE = "//Sectie";
    String KEY_GRONDNUMMER = "//Grondnummer";
    String KEY_GEBOUW_ID = "//Criteria/GebouwId";
    String KEY_GEBOUWEENHEID_ID = "//Criteria/GebouweenheidId";
    String KEY_ADRES_GEMEENTE = "//Criteria/Adres/Gemeente";
    String KEY_ADRES_STRAAT = "//Criteria/Adres/Straat";
    String KEY_ADRES_HUISNUMMER = "//Criteria/Adres/Huisnummer";
    String KEY_ADRES_BUSNUMMER = "//Criteria/Adres/Busnummer";
    String KEY_ATTESTNUMMER = "//Criteria/Attestnummer";

    List<MockSoapMapping> SOAP_MAPPINGS = List.of(
            // Dossier
            new MockSoapMapping("Dossier", "GeefDossiers", VERSION_02_00, List.of(KEY_INSZ), SEPERATOR_FILE_NAME, "Dossier.GeefDossiersDienst-02.00/WebService/GeefDossiers.xsd", "Dossier.GeefDossiersDienst-02.00/WebService/GeefDossiersResponse.xsd"),

            // Energie
            new MockSoapMapping("Energie", "GeefEpc", VERSION_02_01, List.of(
                    KEY_GEBOUW_ID,
                    KEY_GEBOUWEENHEID_ID,
                    KEY_ADRES_GEMEENTE,
                    KEY_ADRES_STRAAT,
                    KEY_ADRES_HUISNUMMER,
                    KEY_ADRES_BUSNUMMER,
                    KEY_ATTESTNUMMER), SEPERATOR_DIRECTORY, "Energie.GeefEpcDienst-02.01/WebService/GeefEpc.xsd", "Energie.GeefEpcDienst-02.01/WebService/GeefEpcResponse.xsd", MockSoapMapping.StubHandler.GeefEpc, MissingParameterConfiguration.EmptyString),

            // Gezin
            new MockSoapMapping("Gezin", "GeefKindVoordelen", VERSION_02_00, List.of(KEY_INSZ), SEPERATOR_DIRECTORY, "Gezin.GeefKindVoordelenDienst-02.00/WebService/GeefKindVoordelen.xsd", "Gezin.GeefKindVoordelenDienst-02.00/WebService/GeefKindVoordelenResponse.xsd"),
            new MockSoapMapping("Gezin", "GeefKindVoordelen", VERSION_02_01, List.of(KEY_INSZ), SEPERATOR_DIRECTORY, "Gezin.GeefKindVoordelenDienst-02.01/WebService/GeefKindVoordelen.xsd", "Gezin.GeefKindVoordelenDienst-02.01/WebService/GeefKindVoordelenResponse.xsd"),
            new MockSoapMapping("Gezin", "GeefZorgtoeslag", VERSION_02_00, List.of(KEY_INSZ), SEPERATOR_DIRECTORY, "Gezin.GeefZorgtoeslagDienst-02.00/WebService/GeefZorgtoeslag.xsd", "Gezin.GeefZorgtoeslagDienst-02.00/WebService/GeefZorgtoeslagResponse.xsd"),

            // Inkomen
            new MockSoapMapping("Inkomen", "GeefAanslagbiljetPersonenbelasting", VERSION_02_00, List.of(KEY_INSZ, "//Criteria/Inkomensjaar"), SEPERATOR_FILE_NAME, "Inkomen.GeefAanslagbiljetPersonenbelastingDienst-02.00/WebService/GeefAanslagbiljetPersonenbelasting.xsd", "Inkomen.GeefAanslagbiljetPersonenbelastingDienst-02.00/WebService/GeefAanslagbiljetPersonenbelastingResponse.xsd", MockSoapMapping.StubHandler.FileSoap, MissingParameterConfiguration.Wildcard),

            // Kadaster
            new MultiFolderMockSoapMapping(
                    "Kadaster", "GeefCadNetTransacties", VERSION_01_00, List.of("//Inhoud/Identificator/INSZ", "//Inhoud/JaarAkte", "//Inhoud/Identificator/KBO", "//Inhoud/JaarAkte", "//Inhoud/Identificator/PersIdf", "//Inhoud/JaarAkte", "//Inhoud/Identificator/Local", "//Inhoud/JaarAkte"), SEPERATOR_DIRECTORY, "Kadaster.GeefCadNetTransactiesDienst-01.00/WebService/GeefCadNetTransacties.xsd", "Kadaster.GeefCadNetTransactiesDienst-01.00/WebService/GeefCadNetTransactiesResponse.xsd",
                    List.of("INSZ", "KBO", "PersIdf", "Local"), List.of(2, 2, 2, 2)
            ),
            new MockSoapMapping("Kadaster", "GeefEigendomstoestanden", VERSION_02_00, List.of(KEY_EIGENDOMID), SEPERATOR_DIRECTORY, "Kadaster.GeefEigendomstoestandenDienst-02.00/WebService/GeefEigendomstoestanden.xsd", "Kadaster.GeefEigendomstoestandenDienst-02.00/WebService/GeefEigendomstoestandenResponse.xsd"),
            new MockSoapMapping("Kadaster", "GeefHistoriekEigendomstoestand", VERSION_03_00, List.of(KEY_EIGENDOMSTOESTANDID), SEPERATOR_DIRECTORY, "Kadaster.GeefHistoriekEigendomstoestandDienst-03.00/WebService/GeefHistoriekEigendomstoestand.xsd", "Kadaster.GeefHistoriekEigendomstoestandDienst-03.00/WebService/GeefHistoriekEigendomstoestandResponse.xsd"),
            new MockSoapMapping("Kadaster", "GeefHistoriekMutatiedossier", VERSION_03_00, List.of(KEY_DOSSIERNUMMER), SEPERATOR_DIRECTORY, "Kadaster.GeefHistoriekMutatiedossierDienst-03.00/WebService/GeefHistoriekMutatiedossier.xsd", "Kadaster.GeefHistoriekMutatiedossierDienst-03.00/WebService/GeefHistoriekMutatiedossierResponse.xsd"),
            new MockSoapMapping("Kadaster", "GeefKadastraleAfdelingenOpKBO", VERSION_01_00, List.of(KEY_ONDERNEMINGSNUMMER), SEPERATOR_DIRECTORY, "Kadaster.GeefKadastraleAfdelingenOpKBODienst-01.00/WebService/GeefKadastraleAfdelingenOpKBO.xsd", "Kadaster.GeefKadastraleAfdelingenOpKBODienst-01.00/WebService/GeefKadastraleAfdelingenOpKBOResponse.xsd"),
            new MockSoapMapping("Kadaster", "GeefTransacties", VERSION_03_00, List.of(KEY_INSZ), SEPERATOR_DIRECTORY, "Kadaster.GeefTransactiesDienst-03.00/WebService/GeefTransacties.xsd", "Kadaster.GeefTransactiesDienst-03.00/WebService/GeefTransactiesResponse.xsd"),
            new MockSoapMapping("Kadaster", "ZoekEigendomstoestanden", VERSION_02_00, List.of(KEY_INSZ), SEPERATOR_DIRECTORY, "Kadaster.ZoekEigendomstoestandenDienst-02.00/WebService/ZoekEigendomstoestanden.xsd", "Kadaster.ZoekEigendomstoestandenDienst-02.00/WebService/ZoekEigendomstoestandenResponse.xsd"),
            new MockSoapMapping("Kadaster", "ZoekPerceel", VERSION_02_00, List.of(KEY_KADASTRALE_AFDELING, KEY_SECTIE, KEY_GRONDNUMMER), SEPERATOR_DIRECTORY, "Kadaster.ZoekPerceelDienst-02.00/WebService/ZoekPerceel.xsd", "Kadaster.ZoekPerceelDienst-02.00/WebService/ZoekPerceelResponse.xsd"),
            new MockSoapMapping("Kadaster", "ZoekVerkoopprijzen", VERSION_03_00, List.of(
                    "//Criteria/Provincie",
                    "//Criteria/AdministratieveGemeentes/AdministratieveGemeente",
                    "//Criteria/TypesInschrijving/TypeInschrijving",
                    "//Criteria/CodesKadastraleAardVolgensAkte/CodeKadastraleAardVolgensAkte"), SEPERATOR_DIRECTORY, "Kadaster.ZoekVerkoopprijzenDienst-03.00/WebService/ZoekVerkoopprijzen.xsd", "Kadaster.ZoekVerkoopprijzenDienst-03.00/WebService/ZoekVerkoopprijzenResponse.xsd"),

            // LED
            new MockSoapMapping("LED", "AnnuleerBewijs", VERSION_02_00, List.of(KEY_INSZ), SEPERATOR_DIRECTORY, "LED.AnnuleerBewijsDienst-02.00/WebService/AnnuleerBewijs.xsd", "LED.AnnuleerBewijsDienst-02.00/WebService/AnnuleerBewijsResponse.xsd"),
            new MockSoapMapping("LED", "GeefBewijs", VERSION_02_00, List.of(KEY_INSZ), SEPERATOR_DIRECTORY, "LED.GeefBewijsDienst-02.00/WebService/GeefBewijs.xsd", "LED.GeefBewijsDienst-02.00/WebService/GeefBewijsResponse.xsd"),
            new MockSoapMapping("LED", "RegistreerBewijs", VERSION_02_00, List.of(KEY_INSZ), SEPERATOR_DIRECTORY, "LED.RegistreerBewijsDienst-02.00/WebService/RegistreerBewijs.xsd", "LED.RegistreerBewijsDienst-02.00/WebService/RegistreerBewijsResponse.xsd"),
            new MockSoapMapping("LED", "RegistreerMutatieBewijs", VERSION_02_00, List.of(KEY_INSZ), SEPERATOR_DIRECTORY, "LED.RegistreerMutatieBewijsDienst-02.00/WebService/RegistreerMutatieBewijs.xsd", "LED.RegistreerMutatieBewijsDienst-02.00/WebService/RegistreerMutatieBewijsResponse.xsd"),

            // Onderneming
            new MockSoapMapping("Onderneming", "GeefAdressenLocaties", VERSION_02_00, List.of(KEY_ONDERNEMINGSNUMMER), SEPERATOR_DIRECTORY, "OndGeo.GeefAdressenLocatiesDienst-02.00/WebService/GeefAdressenLocaties.xsd", "OndGeo.GeefAdressenLocatiesDienst-02.00/WebService/GeefAdressenLocatiesResponse.xsd"),
            new MockSoapMapping("Onderneming", "GeefBeschikbareJaarrekeningen", VERSION_02_00, List.of(KEY_ONDERNEMINGSNUMMER), SEPERATOR_DIRECTORY, "Onderneming.GeefBeschikbareJaarrekeningenDienst-02.00/WebService/GeefBeschikbareJaarrekeningen.xsd", "Onderneming.GeefBeschikbareJaarrekeningenDienst-02.00/WebService/GeefBeschikbareJaarrekeningenResponse.xsd"),
            new MockSoapMapping("Onderneming", "GeefDeelnemingen", VERSION_02_00, List.of(KEY_ONDERNEMINGSNUMMER), SEPERATOR_DIRECTORY, "Onderneming.GeefDeelnemingenDienst-02.00/WebService/GeefDeelnemingen.xsd", "Onderneming.GeefDeelnemingenDienst-02.00/WebService/GeefDeelnemingenResponse.xsd"),
            new MockSoapMapping("Onderneming", "GeefFiscaleInhoudingsplicht", VERSION_02_01, List.of(KEY_ONDERNEMINGSNUMMER), SEPERATOR_DIRECTORY, "Onderneming.GeefFiscaleInhoudingsplichtDienst-02.01/WebService/GeefFiscaleInhoudingsplicht.xsd", "Onderneming.GeefFiscaleInhoudingsplichtDienst-02.01/WebService/GeefFiscaleInhoudingsplichtResponse.xsd"),
            new MockSoapMapping("Onderneming", "GeefFiscaleSchuld", VERSION_02_00, List.of(KEY_ONDERNEMINGSNUMMER), SEPERATOR_DIRECTORY, "Onderneming.GeefFiscaleSchuldDienst-02.00/WebService/GeefFiscaleSchuld.xsd", "Onderneming.GeefFiscaleSchuldDienst-02.00/WebService/GeefFiscaleSchuldResponse.xsd"),
            new MockSoapMapping("Onderneming", "GeefFuncties", VERSION_02_00, List.of(KEY_INSZ), SEPERATOR_DIRECTORY, "Onderneming.GeefFunctiesDienst-02.00/WebService/GeefFuncties.xsd", "Onderneming.GeefFunctiesDienst-02.00/WebService/GeefFunctiesResponse.xsd"),
            new MockSoapMapping("Onderneming", "GeefJaarrekeningen", VERSION_02_00, List.of(KEY_ONDERNEMINGSNUMMER, KEY_BOEKJAAR), SEPERATOR_DIRECTORY, "Onderneming.GeefJaarrekeningenDienst-02.00/WebService/GeefJaarrekeningen.xsd", "Onderneming.GeefJaarrekeningenDienst-02.00/WebService/GeefJaarrekeningenResponse.xsd"),
            new MockSoapMapping("Onderneming", "GeefOnderneming", VERSION_02_00, List.of(KEY_ONDERNEMINGSNUMMER), SEPERATOR_DIRECTORY, "Onderneming.GeefOndernemingDienst-02.00/WebService/GeefOnderneming.xsd", "Onderneming.GeefOndernemingDienst-02.00/WebService/GeefOndernemingResponse.xsd"),
            new MockSoapMapping("Onderneming", "GeefOndernemingSignalen", VERSION_02_00, List.of(KEY_ONDERNEMINGSNUMMER), SEPERATOR_DIRECTORY, "Onderneming.GeefOndernemingSignalenDienst-02.00/WebService/GeefOndernemingSignalen.xsd", "Onderneming.GeefOndernemingSignalenDienst-02.00/WebService/GeefOndernemingSignalenResponse.xsd"),
            new MockSoapMapping("Onderneming", "GeefOndernemingVKBO", VERSION_02_00, List.of(KEY_ONDERNEMINGSNUMMER), SEPERATOR_DIRECTORY, "Onderneming.GeefOndernemingVKBODienst-02.00/WebService/GeefOndernemingVKBO.xsd", "Onderneming.GeefOndernemingVKBODienst-02.00/WebService/GeefOndernemingVKBOResponse.xsd"),
            new MockSoapMapping("Onderneming", "GeefPCenTW", VERSION_02_00, List.of(KEY_ONDERNEMINGSNUMMER), SEPERATOR_DIRECTORY, "Onderneming.GeefPCenTWDienst-02.00/WebService/GeefPCenTW.xsd", "Onderneming.GeefPCenTWDienst-02.00/WebService/GeefPCenTWResponse.xsd"),
            new MockSoapMapping("Onderneming", "GeefSocialeSchuld", VERSION_02_00, List.of(KEY_ONDERNEMINGSNUMMER), SEPERATOR_DIRECTORY, "Onderneming.GeefSocialeSchuldDienst-02.00/WebService/GeefSocialeSchuld.xsd", "Onderneming.GeefSocialeSchuldDienst-02.00/WebService/GeefSocialeSchuldResponse.xsd"),
            new MockSoapMapping("Onderneming", "GeefTewerkstelling", VERSION_02_00, List.of(KEY_ONDERNEMINGSNUMMER), SEPERATOR_DIRECTORY, "Onderneming.GeefTewerkstellingDienst-02.00/WebService/GeefTewerkstelling.xsd", "Onderneming.GeefTewerkstellingDienst-02.00/WebService/GeefTewerkstellingResponse.xsd"),
            new MockSoapMapping("Onderneming", "ZoekOnderneming", VERSION_02_00, List.of("//Criteria/Naam", "//Criteria/Adres/GemeenteNISCode"), SEPERATOR_FILE_NAME, "Onderneming.ZoekOndernemingDienst-02.00/WebService/ZoekOnderneming.xsd", "Onderneming.ZoekOndernemingDienst-02.00/WebService/ZoekOndernemingResponse.xsd"),

            // Onderwijs
            new MockSoapMapping("Onderwijs", "GeefHistoriekInschrijving", VERSION_02_01, List.of(KEY_INSZ), SEPERATOR_DIRECTORY, "Onderwijs.GeefHistoriekInschrijvingDienst-02.01/WebService/GeefHistoriekInschrijving.xsd", "Onderwijs.GeefHistoriekInschrijvingDienst-02.01/WebService/GeefHistoriekInschrijvingResponse.xsd"),

            // Persoon
            new MockSoapMapping("Persoon", "WijzigKSZPersoon", VERSION_02_02, List.of("//WijzigKSZPersoon/INSZ"), SEPERATOR_DIRECTORY, "Persoon.WijzigKSZPersoonDienst-02.02/WebService/WijzigKSZPersoon.xsd", "Persoon.WijzigKSZPersoonDienst-02.02/WebService/WijzigKSZPersoonResponse.xsd"),
            new MockSoapMapping("Persoon", "GeefAttest", VERSION_02_00, List.of(KEY_INSZ), SEPERATOR_DIRECTORY, "Persoon.GeefAttestDienst-02.00/WebService/GeefAttest.xsd", "Persoon.GeefAttestDienst-02.00/WebService/GeefAttestResponse.xsd"),
            new MockSoapMapping("Persoon", "GeefGezinssamenstelling", VERSION_02_00, List.of(KEY_INSZ), SEPERATOR_DIRECTORY, "Persoon.GeefGezinssamenstellingDienst-02.00/WebService/GeefGezinssamenstelling.xsd", "Persoon.GeefGezinssamenstellingDienst-02.00/WebService/GeefGezinssamenstellingResponse.xsd"),
            new MockSoapMapping("Persoon", "GeefGezinssamenstelling", VERSION_02_02, List.of(KEY_INSZ), SEPERATOR_DIRECTORY, "Persoon.GeefGezinssamenstellingDienst-02.02/WebService/GeefGezinssamenstelling.xsd", "Persoon.GeefGezinssamenstellingDienst-02.02/WebService/GeefGezinssamenstellingResponse.xsd"),
            new MockSoapMapping("Persoon", "GeefHistoriekGezinssamenstelling", VERSION_02_02, List.of(KEY_INSZ), SEPERATOR_DIRECTORY, "Persoon.GeefHistoriekGezinssamenstellingDienst-02.02/WebService/GeefHistoriekGezinssamenstelling.xsd", "Persoon.GeefHistoriekGezinssamenstellingDienst-02.02/WebService/GeefHistoriekGezinssamenstellingResponse.xsd"),
            new MockSoapMapping("Persoon", "GeefHistoriekPersoon", VERSION_02_00, List.of(KEY_INSZ), SEPERATOR_DIRECTORY, "Persoon.GeefHistoriekPersoonDienst-02.00/WebService/GeefHistoriekPersoon.xsd", "Persoon.GeefHistoriekPersoonDienst-02.00/WebService/GeefHistoriekPersoonResponse.xsd"),
            new MockSoapMapping("Persoon", "GeefHistoriekPersoon", VERSION_02_02, List.of(KEY_INSZ), SEPERATOR_DIRECTORY, "Persoon.GeefHistoriekPersoonDienst-02.02/WebService/GeefHistoriekPersoon.xsd", "Persoon.GeefHistoriekPersoonDienst-02.02/WebService/GeefHistoriekPersoonResponse.xsd"),
            new MockSoapMapping("Persoon", "GeefPasfoto", VERSION_02_00, List.of(KEY_INSZ), SEPERATOR_DIRECTORY, "Persoon.GeefPasfotoDienst-02.00/WebService/GeefPasfoto.xsd", "Persoon.GeefPasfotoDienst-02.00/WebService/GeefPasfotoResponse.xsd"),
            new MockSoapMapping("Persoon", "GeefPersoon", VERSION_02_02, List.of(
                    "//Inhoud/Bron",
                    KEY_INSZ
            ), SEPERATOR_DIRECTORY, "Persoon.GeefPersoonDienst-02.02/WebService/GeefPersoon.xsd", "Persoon.GeefPersoonDienst-02.02/WebService/GeefPersoonResponse.xsd"),
            new MockSoapMapping("Persoon", "GeefPersoonMutatiesNotificaties", VERSION_02_00, List.of("//Inhoud/Ontvangstreferte"), SEPERATOR_DIRECTORY, "Persoon.GeefPersoonMutatiesNotificatiesDienst-02.00/WebService/GeefPersoonMutatiesNotificaties.xsd", "Persoon.GeefPersoonMutatiesNotificatiesDienst-02.00/WebService/GeefPersoonMutatiesNotificatiesResponse.xsd"),
            new MockSoapMapping("Persoon", "RaadpleegLeerkredietsaldo", VERSION_01_00, List.of("//Inhoud/Ontvangstreferte"), SEPERATOR_DIRECTORY, "Persoon.RaadpleegLeerkredietsaldoDienst-01.02/WebService/RaadpleegLeerkredietsaldo.xsd", "Persoon.RaadpleegLeerkredietsaldoDienst-01.02/WebService/RaadpleegLeerkredietsaldoResponse.xsd"),
            new MockSoapMapping("Persoon", "ZoekPersoonOpAdres", VERSION_02_02, List.of(
                    "//Inhoud/Bron",
                    "//Criteria/Adres/PostCode",
                    "//Criteria/Adres/Straatcode",
                    "//Criteria/Adres/Huisnummer",
                    "//Criteria/EnkelReferentiepersoon"), SEPERATOR_FILE_NAME, "Persoon.ZoekPersoonOpAdresDienst-02.02/WebService/ZoekPersoonOpAdres.xsd", "Persoon.ZoekPersoonOpAdresDienst-02.02/WebService/ZoekPersoonOpAdresResponse.xsd"),
            new MockSoapMapping("Persoon", "ZoekPersoonOpNaam", VERSION_02_02, List.of(
                    "//Inhoud/Bron",
                    "//Criteria/Naam/Achternaam",
                    "//Criteria/Geboorte/Datum"), SEPERATOR_DIRECTORY, "Persoon.ZoekPersoonOpNaamDienst-02.02/WebService/ZoekPersoonOpNaam.xsd", "Persoon.ZoekPersoonOpNaamDienst-02.02/WebService/ZoekPersoonOpNaamResponse.xsd"),
            new MockSoapMapping("Persoon", "CreeerBis", VERSION_02_02, List.of("//Persoon/Naam/Achternaam", "//Persoon/Naam/Voornamen/Voornaam"), SEPERATOR_FILE_NAME, "Persoon.CreeerBisDienst-02.02/WebService/CreeerBis.xsd", "Persoon.CreeerBisDienst-02.02/WebService/CreeerBisResponse.xsd"),

            // Repertorium
            new MockSoapMapping("Repertorium", "RegistreerInschrijving", VERSION_02_00, List.of(KEY_INSZ), SEPERATOR_DIRECTORY, "Repertorium.RegistreerInschrijvingDienst-02.00/WebService/RegistreerInschrijving.xsd", "Repertorium.RegistreerInschrijvingDienst-02.00/WebService/RegistreerInschrijvingResponse.xsd"),
            new MockSoapMapping("Repertorium", "RegistreerInschrijving", VERSION_02_01, List.of("//Subject/Type", "//Subject/Sleutel"), SEPERATOR_DIRECTORY, "Repertorium.RegistreerInschrijvingDienst-02.01/WebService/RegistreerInschrijving.xsd", "Repertorium.RegistreerInschrijvingDienst-02.01/WebService/RegistreerInschrijvingResponse.xsd"),
            new MockSoapMapping("Repertorium", "RegistreerUitschrijving", VERSION_02_00, List.of(KEY_INSZ), SEPERATOR_DIRECTORY, "Repertorium.RegistreerUitschrijvingDienst-02.00/WebService/RegistreerUitschrijving.xsd", "Repertorium.RegistreerUitschrijvingDienst-02.00/WebService/RegistreerUitschrijvingResponse.xsd"),

            // SocEcon
            new MockSoapMapping("SocEcon", "GeefStatusRechtOndersteuningen", VERSION_02_00, List.of(KEY_INSZ), SEPERATOR_DIRECTORY, "SocEcon.GeefStatusRechtOndersteuningenDienst-02.00/WebService/GeefStatusRechtOndersteuningen.xsd", "SocEcon.GeefStatusRechtOndersteuningenDienst-02.00/WebService/GeefStatusRechtOndersteuningenResponse.xsd"),

            // SocZek
            new MockSoapMapping("SocZek", "GeefArbeidsongeschiktheid", VERSION_01_00, List.of(KEY_SSIN, "//yearQuarter"), SEPERATOR_FILE_NAME, "SocZek.GeefArbeidsongeschiktheidDienst-01.00/WebService/GeefArbeidsongeschiktheid.xsd", "SocZek.GeefArbeidsongeschiktheidDienst-01.00/WebService/GeefArbeidsongeschiktheidResponse.xsd"),
            new MockSoapMapping("SocZek", "GeefAttestWerkloosheid", VERSION_01_00, List.of(KEY_INSS), SEPERATOR_DIRECTORY, "SocZek.GeefAttestWerkloosheidDienst-01.00/WebService/GeefAttestWerkloosheid.xsd", "SocZek.GeefAttestWerkloosheidDienst-01.00/WebService/GeefAttestWerkloosheidResponse.xsd"),
            new MockSoapMapping("SocZek", "GeefBetalingenHandicap", VERSION_03_00, List.of(KEY_SSIN), SEPERATOR_DIRECTORY, "SocZek.GeefBetalingenHandicapDienst-03.00/WebService/GeefBetalingenHandicap.xsd", "SocZek.GeefBetalingenHandicapDienst-03.00/WebService/GeefBetalingenHandicapResponse.xsd"),
            new MockSoapMapping("SocZek", "GeefDossierHandicap", VERSION_03_00, List.of("local-name(//Criteria/child::*[1])", KEY_SSIN), SEPERATOR_DIRECTORY, "SocZek.GeefDossierHandicapDienst-03.00/WebService/GeefDossierHandicap.xsd", "SocZek.GeefDossierHandicapDienst-03.00/WebService/GeefDossierHandicapResponse.xsd"),
            // Criteria/child::*[1]/name() -> tag name of the first child node in element Criteria -> ConsultFilesByDateCriteria | ConsultFilesByPeriodCriteria
            new MockSoapMapping("SocZek", "GeefInschrijvingWerkzoekende", VERSION_01_00, List.of(KEY_SSIN), SEPERATOR_DIRECTORY, "SocZek.GeefInschrijvingWerkzoekendeDienst-01.00/WebService/GeefInschrijvingWerkzoekende.xsd", "SocZek.GeefInschrijvingWerkzoekendeDienst-01.00/WebService/GeefInschrijvingWerkzoekendeResponse.xsd"),
            new MockSoapMapping("SocZek", "GeefLeefloonbedragen", VERSION_02_00, List.of(KEY_INSZ), SEPERATOR_DIRECTORY, "SocZek.GeefLeefloonbedragenDienst-02.00/WebService/GeefLeefloonbedragen.xsd", "SocZek.GeefLeefloonbedragenDienst-02.00/WebService/GeefLeefloonbedragenResponse.xsd"),
            new MockSoapMapping("SocZek", "GeefLeefloonperiodes", VERSION_02_00, List.of(KEY_INSZ), SEPERATOR_DIRECTORY, "SocZek.GeefLeefloonperiodesDienst-02.00/WebService/GeefLeefloonperiodes.xsd", "SocZek.GeefLeefloonperiodesDienst-02.00/WebService/GeefLeefloonperiodesResponse.xsd"),
            new MockSoapMapping("SocZek", "GeefPensioen", VERSION_02_00, List.of(KEY_INSZ, "//Criteria/Pijler"), SEPERATOR_FILE_NAME, "SocZek.GeefPensioenDienst-02.00/WebService/GeefPensioen.xsd", "SocZek.GeefPensioenDienst-02.00/WebService/GeefPensioenResponse.xsd"),
            new MockSoapMapping("SocZek", "GeefPensioenrechten", VERSION_02_00, List.of(KEY_INSZ, "//Criteria/Pijler"), SEPERATOR_FILE_NAME, "SocZek.GeefPensioenrechtenDienst-02.00/WebService/GeefPensioenrechten.xsd", "SocZek.GeefPensioenrechtenDienst-02.00/WebService/GeefPensioenrechtenResponse.xsd"),
            new MockSoapMapping("SocZek", "GeefStatuutRVV", VERSION_02_00, List.of(KEY_INSZ), SEPERATOR_DIRECTORY, "SocZek.GeefStatuutRVVDienst-02.00/WebService/GeefStatuutRVV.xsd", "SocZek.GeefStatuutRVVDienst-02.00/WebService/GeefStatuutRVVResponse.xsd"),
            new MockSoapMapping("SocZek", "GeefSociaalStatuut", VERSION_03_00, List.of(KEY_INSZ), SEPERATOR_DIRECTORY, "SocZek.GeefSociaalStatuutDienst-03.00/WebService/GeefSociaalStatuut.xsd", "SocZek.GeefSociaalStatuutDienst-03.00/WebService/GeefSociaalStatuutResponse.xsd"),
            new MockSoapMapping("SocZek", "GeefVervangingsinkomenUitWerkloosheid", VERSION_02_01, List.of(KEY_INSZ), SEPERATOR_DIRECTORY, "SocZek.GeefVervangingsinkomenUitWerkloosheidDienst-02.01/WebService/GeefVervangingsinkomenUitWerkloosheid.xsd", "SocZek.GeefVervangingsinkomenUitWerkloosheidDienst-02.01/WebService/GeefVervangingsinkomenUitWerkloosheidResponse.xsd"),
            new MockSoapMapping("SocZek", "GeefVolledigDossierHandicap", VERSION_03_00, List.of(KEY_RRNR), SEPERATOR_DIRECTORY, "SocZek.GeefVolledigDossierHandicapDienst-03.00/WebService/GeefVolledigDossierHandicap.xsd", "SocZek.GeefVolledigDossierHandicapDienst-03.00/WebService/GeefVolledigDossierHandicapResponse.xsd"),
            new MockSoapMapping("SocZek", "GeefWerkzoekende", VERSION_02_00, List.of(KEY_INSZ), SEPERATOR_DIRECTORY, "SocZek.GeefWerkzoekendeDienst-02.00/WebService/GeefWerkzoekende.xsd", "SocZek.GeefWerkzoekendeDienst-02.00/WebService/GeefWerkzoekendeResponse.xsd"),

            // Werk
            new MockSoapMapping("Werk", "GeefLoopbaanARZA", VERSION_02_01, List.of(KEY_INSZ), SEPERATOR_DIRECTORY, "Werk.GeefLoopbaanARZADienst-02.01/WebService/GeefLoopbaanARZA.xsd", "Werk.GeefLoopbaanARZADienst-02.01/WebService/GeefLoopbaanARZAResponse.xsd"),
            new MockSoapMapping("Werk", "GeefLoopbaanonderbrekingen", VERSION_02_00, List.of(KEY_INSZ), SEPERATOR_DIRECTORY, "Werk.GeefLoopbaanonderbrekingenDienst-02.00/WebService/GeefLoopbaanonderbrekingen.xsd", "Werk.GeefLoopbaanonderbrekingenDienst-02.00/WebService/GeefLoopbaanonderbrekingenResponse.xsd"),
            new MockSoapMapping("Werk", "GeefWerkrelaties", VERSION_02_00, List.of(KEY_INSZ, KEY_ONDERNEMINGSNUMMER, "//Criteria/Relatie/Werkgever/RSZNummer"), SEPERATOR_FILE_NAME, "Werk.GeefWerkrelatiesDienst-02.00/WebService/GeefWerkrelaties.xsd", "Werk.GeefWerkrelatiesDienst-02.00/WebService/GeefWerkrelatiesResponse.xsd"),
            new MockSoapMapping("Werk", "GeefDmfaVoorWerknemer", VERSION_03_00, List.of(KEY_INSZ, "//Volgende"), SEPERATOR_FILE_NAME, "Werk.GeefDmfaVoorWerknemerDienst-03.00/WebService/GeefDmfaVoorWerknemer.xsd", "Werk.GeefDmfaVoorWerknemerDienst-03.00/WebService/GeefDmfaVoorWerknemerResponse.xsd"),
            new MockSoapMapping("Werk", "GeefBijdrageARZA", VERSION_01_00, List.of(KEY_SSIN), SEPERATOR_DIRECTORY, "Werk.GeefBijdrageARZADienst-01.00/WebService/GeefBijdrageARZA.xsd", "Werk.GeefBijdrageARZADienst-01.00/WebService/GeefBijdrageARZAResponse.xsd"),

            //Vlok
            new MockSoapMapping("Vlok", "BewaarWoningKwaliteit", VERSION_02_00, List.of("//Criteria/NISCode", "local-name(//Criteria/NISCode/following-sibling::*[1])"), SEPERATOR_FILE_NAME, "Vlok.BewaarWoningKwaliteitDienst-02.00/WebService/BewaarWoningKwaliteit.xsd", "Vlok.BewaarWoningKwaliteitDienst-02.00/WebService/BewaarWoningKwaliteitResponse.xsd"),
            // following-sibling::*[1]/name() -> tag name of the element after NISCode
            new MockSoapMapping("Vlok", "BewaarWoningKwaliteitBijlage", VERSION_02_00, List.of("//Criteria/NISCode"), SEPERATOR_FILE_NAME, "Vlok.BewaarWoningKwaliteitBijlageDienst-02.00/WebService/BewaarWoningKwaliteitBijlage.xsd", "Vlok.BewaarWoningKwaliteitBijlageDienst-02.00/WebService/BewaarWoningKwaliteitBijlageResponse.xsd"),
            new MockSoapMapping("Vlok", "GeefWoningKwaliteit", VERSION_02_00, List.of("//NISCode", "//Criteria/Type", "//Criteria/Referte"), SEPERATOR_FILE_NAME, "Vlok.GeefWoningKwaliteitDienst-02.00/WebService/GeefWoningKwaliteit.xsd", "Vlok.GeefWoningKwaliteitDienst-02.00/WebService/GeefWoningKwaliteitResponse.xsd"),
            new MockSoapMapping("Vlok", "GeefWoningKwaliteitBijlage", VERSION_02_00, List.of("//NISCode", "//Criteria/Bijlagen/Bijlage[1]/Type", "//Criteria/Bijlagen/Bijlage[1]/Referte"), SEPERATOR_FILE_NAME, "Vlok.GeefWoningKwaliteitBijlageDienst-02.00/WebService/GeefWoningKwaliteitBijlage.xsd", "Vlok.GeefWoningKwaliteitBijlageDienst-02.00/WebService/GeefWoningKwaliteitBijlageResponse.xsd"),
            new MockSoapMapping("Vlok", "ZoekWoningKwaliteit", VERSION_02_00, List.of("//NISCode", "//Zoekterm"), SEPERATOR_FILE_NAME, "Vlok.ZoekWoningKwaliteitDienst-02.00/WebService/ZoekWoningKwaliteit.xsd", "Vlok.ZoekWoningKwaliteitDienst-02.00/WebService/ZoekWoningKwaliteitResponse.xsd")
    );

}
