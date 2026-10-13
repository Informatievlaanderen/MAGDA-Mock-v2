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
    String KEY_ADRES_POSTCODE = "//Criteria/Adres/Postcode";

    List<MockSoapMapping> SOAP_MAPPINGS = List.of(
            // Dossier
            MockSoapMapping.create("Dossier", "GeefDossiers", VERSION_02_00, List.of(KEY_INSZ), SEPERATOR_FILE_NAME, "Dossier.GeefDossiersDienst-02.00/WebService/GeefDossiers.xsd", "Dossier.GeefDossiersDienst-02.00/WebService/GeefDossiersResponse.xsd"),

            // Energie
            new MockSoapMapping("Energie", "GeefEpc", VERSION_02_01, List.of(
                    KEY_GEBOUW_ID,
                    KEY_GEBOUWEENHEID_ID,
                    KEY_ADRES_GEMEENTE,
                    KEY_ADRES_POSTCODE,
                    KEY_ADRES_STRAAT,
                    KEY_ADRES_HUISNUMMER,
                    KEY_ADRES_BUSNUMMER,
                    KEY_ATTESTNUMMER), List.of(), "Energie.GeefEpcDienst-02.01/WebService/GeefEpc.xsd", "Energie.GeefEpcDienst-02.01/WebService/GeefEpcResponse.xsd", MockSoapMapping.StubHandler.GeefEpc, MissingParameterConfiguration.EmptyString),

            // Gezin
            MockSoapMapping.create("Gezin", "GeefKindVoordelen", VERSION_02_00, List.of(KEY_INSZ), SEPERATOR_DIRECTORY, "Gezin.GeefKindVoordelenDienst-02.00/WebService/GeefKindVoordelen.xsd", "Gezin.GeefKindVoordelenDienst-02.00/WebService/GeefKindVoordelenResponse.xsd"),
            MockSoapMapping.create("Gezin", "GeefKindVoordelen", VERSION_02_01, List.of(KEY_INSZ), SEPERATOR_DIRECTORY, "Gezin.GeefKindVoordelenDienst-02.01/WebService/GeefKindVoordelen.xsd", "Gezin.GeefKindVoordelenDienst-02.01/WebService/GeefKindVoordelenResponse.xsd"),
            MockSoapMapping.create("Gezin", "GeefZorgtoeslag", VERSION_02_00, List.of(KEY_INSZ), SEPERATOR_DIRECTORY, "Gezin.GeefZorgtoeslagDienst-02.00/WebService/GeefZorgtoeslag.xsd", "Gezin.GeefZorgtoeslagDienst-02.00/WebService/GeefZorgtoeslagResponse.xsd"),

            // Inkomen
            new MockSoapMapping("Inkomen", "GeefAanslagbiljetPersonenbelasting", VERSION_02_00, List.of(), List.of(KEY_INSZ, "//Criteria/Inkomensjaar"), "Inkomen.GeefAanslagbiljetPersonenbelastingDienst-02.00/WebService/GeefAanslagbiljetPersonenbelasting.xsd", "Inkomen.GeefAanslagbiljetPersonenbelastingDienst-02.00/WebService/GeefAanslagbiljetPersonenbelastingResponse.xsd", MockSoapMapping.StubHandler.FileSoap, MissingParameterConfiguration.Wildcard),

            // Kadaster
            new MultiFolderMockSoapMapping(
                    "Kadaster", "GeefCadNetTransacties", VERSION_01_00, List.of("//Inhoud/Identificator/INSZ", "//Inhoud/JaarAkte", "//Inhoud/Identificator/KBO", "//Inhoud/JaarAkte", "//Inhoud/Identificator/PersIdf", "//Inhoud/JaarAkte", "//Inhoud/Identificator/Local", "//Inhoud/JaarAkte"), "Kadaster.GeefCadNetTransactiesDienst-01.00/WebService/GeefCadNetTransacties.xsd", "Kadaster.GeefCadNetTransactiesDienst-01.00/WebService/GeefCadNetTransactiesResponse.xsd",
                    List.of("INSZ", "KBO", "PersIdf", "Local"), List.of(2, 2, 2, 2)
            ),
            new MultiFolderMockSoapMapping(
                    "Kadaster", "GeefEigendomstoestanden", VERSION_02_00, List.of(
                    KEY_EIGENDOMID,
                    "//Identificatie/KadastraleAfdeling",
                    "//Identificatie/Sectie",
                    "//Identificatie/Grondnummer",
                    "//Identificatie/Bisnummer",
                    "//Identificatie/Cijferexponent",
                    "//Identificatie/Letterexponent",
                    "//Identificatie/Partitie"
            ), "Kadaster.GeefEigendomstoestandenDienst-02.00/WebService/GeefEigendomstoestanden.xsd", "Kadaster.GeefEigendomstoestandenDienst-02.00/WebService/GeefEigendomstoestandenResponse.xsd",
                    List.of("EigendomId", "PatKey"), List.of(1, 7)
            ),
            new MultiFolderMockSoapMapping(
                    "Kadaster", "GeefHistoriekEigendomstoestand", VERSION_03_00, List.of(
                    "//Criteria/EigendomstoestandId",
                    "//Criteria/KadastraleInschrijving/KadastraleAfdeling",
                    "//Criteria/KadastraleInschrijving/Artikelnummer",
                    "//Criteria/KadastraleInschrijving/Volgnummer"
            ), "Kadaster.GeefHistoriekEigendomstoestandDienst-03.00/WebService/GeefHistoriekEigendomstoestand.xsd", "Kadaster.GeefHistoriekEigendomstoestandDienst-03.00/WebService/GeefHistoriekEigendomstoestandResponse.xsd",
                    List.of("EigendomstoestandId", "KadastraleInschrijving"), List.of(1, 3)
            ),
            MockSoapMapping.create("Kadaster", "GeefHistoriekMutatiedossier", VERSION_03_00, List.of("//Criteria/Dossiernummer"), SEPERATOR_DIRECTORY, "Kadaster.GeefHistoriekMutatiedossierDienst-03.00/WebService/GeefHistoriekMutatiedossier.xsd", "Kadaster.GeefHistoriekMutatiedossierDienst-03.00/WebService/GeefHistoriekMutatiedossierResponse.xsd"),
            MockSoapMapping.create("Kadaster", "GeefKadastraleAfdelingenOpKBO", VERSION_01_00, List.of(KEY_ONDERNEMINGSNUMMER), SEPERATOR_DIRECTORY, "Kadaster.GeefKadastraleAfdelingenOpKBODienst-01.00/WebService/GeefKadastraleAfdelingenOpKBO.xsd", "Kadaster.GeefKadastraleAfdelingenOpKBODienst-01.00/WebService/GeefKadastraleAfdelingenOpKBOResponse.xsd"),
            new MultiFolderMockSoapMapping("Kadaster", "GeefTransacties", VERSION_03_00, List.of(
                    "//Criteria/INSZ", "//Criteria/Ondernemingsnummer"
            ), "Kadaster.GeefTransactiesDienst-03.00/WebService/GeefTransacties.xsd", "Kadaster.GeefTransactiesDienst-03.00/WebService/GeefTransactiesResponse.xsd",
                    List.of("INSZ", "Ondernemingsnummer"), List.of(1,1)
            ),
            new MultiFolderMockSoapMapping("Kadaster", "ZoekEigendomstoestanden", VERSION_02_00, List.of(
                    "//Criteria/INSZ", "//Criteria/Ondernemingsnummer"
            ), "Kadaster.ZoekEigendomstoestandenDienst-02.00/WebService/ZoekEigendomstoestanden.xsd", "Kadaster.ZoekEigendomstoestandenDienst-02.00/WebService/ZoekEigendomstoestandenResponse.xsd",
                    List.of("INSZ", "Ondernemingsnummer"), List.of(1,1)
            ),
            MockSoapMapping.create("Kadaster", "ZoekPerceel", VERSION_02_00, List.of(
                    "//Identificatie/KadastraleAfdeling",
                    "//Identificatie/Sectie",
                    "//Identificatie/Grondnummer",
                    "//Identificatie/Bisnummer",
                    "//Identificatie/Cijferexponent",
                    "//Identificatie/Letterexponent",
                    "//Identificatie/Partitie",
                    "//Actief"
            ), SEPERATOR_FILE_NAME, "Kadaster.ZoekPerceelDienst-02.00/WebService/ZoekPerceel.xsd", "Kadaster.ZoekPerceelDienst-02.00/WebService/ZoekPerceelResponse.xsd"),
            MockSoapMapping.create("Kadaster", "ZoekVerkoopprijzen", VERSION_03_00, List.of(
                    "//Criteria/Provincie",
                    "//Criteria/AdministratieveGemeentes/AdministratieveGemeente[1]",
                    "//Criteria/TypesInschrijving/TypeInschrijving[1]",
                    "//Criteria/CodesKadastraleAardVolgensAkte/CodeKadastraleAardVolgensAkte[1]"), SEPERATOR_FILE_NAME, "Kadaster.ZoekVerkoopprijzenDienst-03.00/WebService/ZoekVerkoopprijzen.xsd", "Kadaster.ZoekVerkoopprijzenDienst-03.00/WebService/ZoekVerkoopprijzenResponse.xsd"),

            // LED
            MockSoapMapping.create("LED", "AnnuleerBewijs", VERSION_02_00, List.of(KEY_INSZ), SEPERATOR_DIRECTORY, "LED.AnnuleerBewijsDienst-02.00/WebService/AnnuleerBewijs.xsd", "LED.AnnuleerBewijsDienst-02.00/WebService/AnnuleerBewijsResponse.xsd"),
            MockSoapMapping.create("LED", "GeefBewijs", VERSION_02_00, List.of(KEY_INSZ), SEPERATOR_DIRECTORY, "LED.GeefBewijsDienst-02.00/WebService/GeefBewijs.xsd", "LED.GeefBewijsDienst-02.00/WebService/GeefBewijsResponse.xsd"),
            MockSoapMapping.create("LED", "RegistreerBewijs", VERSION_02_00, List.of(KEY_INSZ), SEPERATOR_DIRECTORY, "LED.RegistreerBewijsDienst-02.00/WebService/RegistreerBewijs.xsd", "LED.RegistreerBewijsDienst-02.00/WebService/RegistreerBewijsResponse.xsd"),
            MockSoapMapping.create("LED", "RegistreerMutatieBewijs", VERSION_02_00, List.of(KEY_INSZ), SEPERATOR_DIRECTORY, "LED.RegistreerMutatieBewijsDienst-02.00/WebService/RegistreerMutatieBewijs.xsd", "LED.RegistreerMutatieBewijsDienst-02.00/WebService/RegistreerMutatieBewijsResponse.xsd"),

            // Onderneming
            MockSoapMapping.create("Onderneming", "GeefAdressenLocaties", VERSION_02_00, List.of(KEY_ONDERNEMINGSNUMMER), SEPERATOR_DIRECTORY, "OndGeo.GeefAdressenLocatiesDienst-02.00/WebService/GeefAdressenLocaties.xsd", "OndGeo.GeefAdressenLocatiesDienst-02.00/WebService/GeefAdressenLocatiesResponse.xsd"),
            MockSoapMapping.create("Onderneming", "GeefBeschikbareJaarrekeningen", VERSION_02_00, List.of(KEY_ONDERNEMINGSNUMMER), SEPERATOR_DIRECTORY, "Onderneming.GeefBeschikbareJaarrekeningenDienst-02.00/WebService/GeefBeschikbareJaarrekeningen.xsd", "Onderneming.GeefBeschikbareJaarrekeningenDienst-02.00/WebService/GeefBeschikbareJaarrekeningenResponse.xsd"),
            MockSoapMapping.create("Onderneming", "GeefDeelnemingen", VERSION_02_00, List.of(KEY_ONDERNEMINGSNUMMER), SEPERATOR_DIRECTORY, "Onderneming.GeefDeelnemingenDienst-02.00/WebService/GeefDeelnemingen.xsd", "Onderneming.GeefDeelnemingenDienst-02.00/WebService/GeefDeelnemingenResponse.xsd"),
            MockSoapMapping.create("Onderneming", "GeefFiscaleInhoudingsplicht", VERSION_02_01, List.of(KEY_ONDERNEMINGSNUMMER), SEPERATOR_DIRECTORY, "Onderneming.GeefFiscaleInhoudingsplichtDienst-02.01/WebService/GeefFiscaleInhoudingsplicht.xsd", "Onderneming.GeefFiscaleInhoudingsplichtDienst-02.01/WebService/GeefFiscaleInhoudingsplichtResponse.xsd"),
            MockSoapMapping.create("Onderneming", "GeefFiscaleSchuld", VERSION_02_00, List.of(KEY_ONDERNEMINGSNUMMER), SEPERATOR_DIRECTORY, "Onderneming.GeefFiscaleSchuldDienst-02.00/WebService/GeefFiscaleSchuld.xsd", "Onderneming.GeefFiscaleSchuldDienst-02.00/WebService/GeefFiscaleSchuldResponse.xsd"),
            new MultiFolderMockSoapMapping("Onderneming", "GeefFuncties", VERSION_02_00,
                    List.of(KEY_INSZ, "//Onderneming/Ondernemingsnummer[@Type='FunctieIn'] | //Onderneming/Ondernemingsnummer[@Type='FunctieHouder'][not(//Onderneming/Ondernemingsnummer[@Type='FunctieIn'])]"), "Onderneming.GeefFunctiesDienst-02.00/WebService/GeefFuncties.xsd", "Onderneming.GeefFunctiesDienst-02.00/WebService/GeefFunctiesResponse.xsd",
                    List.of("Persoon", "Onderneming"), List.of(1,1)),
            MockSoapMapping.create("Onderneming", "GeefJaarrekeningen", VERSION_02_00, List.of(KEY_ONDERNEMINGSNUMMER, KEY_BOEKJAAR), SEPERATOR_DIRECTORY, "Onderneming.GeefJaarrekeningenDienst-02.00/WebService/GeefJaarrekeningen.xsd", "Onderneming.GeefJaarrekeningenDienst-02.00/WebService/GeefJaarrekeningenResponse.xsd"),
            MockSoapMapping.create("Onderneming", "GeefOnderneming", VERSION_02_00, List.of(KEY_ONDERNEMINGSNUMMER), SEPERATOR_DIRECTORY, "Onderneming.GeefOndernemingDienst-02.00/WebService/GeefOnderneming.xsd", "Onderneming.GeefOndernemingDienst-02.00/WebService/GeefOndernemingResponse.xsd"),
            MockSoapMapping.create("Onderneming", "GeefOndernemingSignalen", VERSION_02_00, List.of(KEY_ONDERNEMINGSNUMMER), SEPERATOR_DIRECTORY, "Onderneming.GeefOndernemingSignalenDienst-02.00/WebService/GeefOndernemingSignalen.xsd", "Onderneming.GeefOndernemingSignalenDienst-02.00/WebService/GeefOndernemingSignalenResponse.xsd"),
            MockSoapMapping.create("Onderneming", "GeefOndernemingVKBO", VERSION_02_00, List.of(KEY_ONDERNEMINGSNUMMER), SEPERATOR_DIRECTORY, "Onderneming.GeefOndernemingVKBODienst-02.00/WebService/GeefOndernemingVKBO.xsd", "Onderneming.GeefOndernemingVKBODienst-02.00/WebService/GeefOndernemingVKBOResponse.xsd"),
            MockSoapMapping.create("Onderneming", "GeefPCenTW", VERSION_02_00, List.of(KEY_ONDERNEMINGSNUMMER), SEPERATOR_DIRECTORY, "Onderneming.GeefPCenTWDienst-02.00/WebService/GeefPCenTW.xsd", "Onderneming.GeefPCenTWDienst-02.00/WebService/GeefPCenTWResponse.xsd"),
            MockSoapMapping.create("Onderneming", "GeefSocialeSchuld", VERSION_02_00, List.of(KEY_ONDERNEMINGSNUMMER), SEPERATOR_DIRECTORY, "Onderneming.GeefSocialeSchuldDienst-02.00/WebService/GeefSocialeSchuld.xsd", "Onderneming.GeefSocialeSchuldDienst-02.00/WebService/GeefSocialeSchuldResponse.xsd"),
            MockSoapMapping.create("Onderneming", "GeefTewerkstelling", VERSION_02_00, List.of(KEY_ONDERNEMINGSNUMMER), SEPERATOR_DIRECTORY, "Onderneming.GeefTewerkstellingDienst-02.00/WebService/GeefTewerkstelling.xsd", "Onderneming.GeefTewerkstellingDienst-02.00/WebService/GeefTewerkstellingResponse.xsd"),
            MockSoapMapping.create("Onderneming", "ZoekOnderneming", VERSION_02_00, List.of("//Criteria/Naam", "//Criteria/Adres/GemeenteNISCode", "//Criteria/Adres/GemeentePostCode", "//Criteria/Adres/StraatCode", "//Criteria/Adres/Huisnummer"), SEPERATOR_FILE_NAME, "Onderneming.ZoekOndernemingDienst-02.00/WebService/ZoekOnderneming.xsd", "Onderneming.ZoekOndernemingDienst-02.00/WebService/ZoekOndernemingResponse.xsd"),

            // Onderwijs
            MockSoapMapping.create("Onderwijs", "GeefHistoriekInschrijving", VERSION_02_01, List.of(KEY_INSZ), SEPERATOR_DIRECTORY, "Onderwijs.GeefHistoriekInschrijvingDienst-02.01/WebService/GeefHistoriekInschrijving.xsd", "Onderwijs.GeefHistoriekInschrijvingDienst-02.01/WebService/GeefHistoriekInschrijvingResponse.xsd"),

            // Persoon
            MockSoapMapping.create("Persoon", "WijzigKSZPersoon", VERSION_02_02, List.of("//WijzigKSZPersoon/INSZ"), SEPERATOR_DIRECTORY, "Persoon.WijzigKSZPersoonDienst-02.02/WebService/WijzigKSZPersoon.xsd", "Persoon.WijzigKSZPersoonDienst-02.02/WebService/WijzigKSZPersoonResponse.xsd"),
            MockSoapMapping.create("Persoon", "GeefAttest", VERSION_02_00, List.of(KEY_INSZ, "//Criteria/Attest/Naam", "//Criteria/Attest/Taal"), SEPERATOR_FILE_NAME, "Persoon.GeefAttestDienst-02.00/WebService/GeefAttest.xsd", "Persoon.GeefAttestDienst-02.00/WebService/GeefAttestResponse.xsd"),
            MockSoapMapping.create("Persoon", "GeefGezinssamenstelling", VERSION_02_00, List.of(KEY_INSZ), SEPERATOR_DIRECTORY, "Persoon.GeefGezinssamenstellingDienst-02.00/WebService/GeefGezinssamenstelling.xsd", "Persoon.GeefGezinssamenstellingDienst-02.00/WebService/GeefGezinssamenstellingResponse.xsd"),
            MockSoapMapping.create("Persoon", "GeefGezinssamenstelling", VERSION_02_02, List.of(KEY_INSZ), SEPERATOR_DIRECTORY, "Persoon.GeefGezinssamenstellingDienst-02.02/WebService/GeefGezinssamenstelling.xsd", "Persoon.GeefGezinssamenstellingDienst-02.02/WebService/GeefGezinssamenstellingResponse.xsd"),
            MockSoapMapping.create("Persoon", "GeefHistoriekGezinssamenstelling", VERSION_02_02, List.of(KEY_INSZ), SEPERATOR_DIRECTORY, "Persoon.GeefHistoriekGezinssamenstellingDienst-02.02/WebService/GeefHistoriekGezinssamenstelling.xsd", "Persoon.GeefHistoriekGezinssamenstellingDienst-02.02/WebService/GeefHistoriekGezinssamenstellingResponse.xsd"),
            MockSoapMapping.create("Persoon", "GeefHistoriekPersoon", VERSION_02_00, List.of(KEY_INSZ), SEPERATOR_DIRECTORY, "Persoon.GeefHistoriekPersoonDienst-02.00/WebService/GeefHistoriekPersoon.xsd", "Persoon.GeefHistoriekPersoonDienst-02.00/WebService/GeefHistoriekPersoonResponse.xsd"),
            MockSoapMapping.create("Persoon", "GeefHistoriekPersoon", VERSION_02_02, List.of("//Inhoud/Bron", KEY_INSZ), SEPERATOR_DIRECTORY, "Persoon.GeefHistoriekPersoonDienst-02.02/WebService/GeefHistoriekPersoon.xsd", "Persoon.GeefHistoriekPersoonDienst-02.02/WebService/GeefHistoriekPersoonResponse.xsd"),
            MockSoapMapping.create("Persoon", "GeefPasfoto", VERSION_02_00, List.of(KEY_INSZ), SEPERATOR_DIRECTORY, "Persoon.GeefPasfotoDienst-02.00/WebService/GeefPasfoto.xsd", "Persoon.GeefPasfotoDienst-02.00/WebService/GeefPasfotoResponse.xsd"),
            MockSoapMapping.create("Persoon", "GeefPersoon", VERSION_02_02, List.of(
                    "//Inhoud/Bron",
                    KEY_INSZ
            ), SEPERATOR_DIRECTORY, "Persoon.GeefPersoonDienst-02.02/WebService/GeefPersoon.xsd", "Persoon.GeefPersoonDienst-02.02/WebService/GeefPersoonResponse.xsd"),
            MockSoapMapping.create("Persoon", "GeefPersoonMutatiesNotificaties", VERSION_02_00, List.of("//Inhoud/Ontvangstreferte"), SEPERATOR_DIRECTORY, "Persoon.GeefPersoonMutatiesNotificatiesDienst-02.00/WebService/GeefPersoonMutatiesNotificaties.xsd", "Persoon.GeefPersoonMutatiesNotificatiesDienst-02.00/WebService/GeefPersoonMutatiesNotificatiesResponse.xsd"),
            MockSoapMapping.create("Persoon", "RaadpleegLeerkredietsaldo", VERSION_01_00, List.of("//Inhoud/Ontvangstreferte"), SEPERATOR_DIRECTORY, "Persoon.RaadpleegLeerkredietsaldoDienst-01.02/WebService/RaadpleegLeerkredietsaldo.xsd", "Persoon.RaadpleegLeerkredietsaldoDienst-01.02/WebService/RaadpleegLeerkredietsaldoResponse.xsd"),
            MockSoapMapping.create("Persoon", "ZoekPersoonOpAdres", VERSION_02_02, List.of(
                    "//Inhoud/Bron",
                    "//Criteria/Adres/PostCode",
                    "//Criteria/Adres/Straatcode",
                    "//Criteria/Adres/Huisnummer",
                    "//Criteria/EnkelReferentiepersoon"), SEPERATOR_FILE_NAME, "Persoon.ZoekPersoonOpAdresDienst-02.02/WebService/ZoekPersoonOpAdres.xsd", "Persoon.ZoekPersoonOpAdresDienst-02.02/WebService/ZoekPersoonOpAdresResponse.xsd"),
            new MockSoapMapping("Persoon", "ZoekPersoonOpNaam", VERSION_02_02,
                    List.of("//Inhoud/Bron","//Criteria/Geboorte/Datum"),
                    List.of("//Criteria/Naam/Achternaam", "//Criteria/Naam/Voornamen/Voornaam[1]", "//Criteria/Naam/Voornamen/Voornaam[2]", "//Criteria/Naam/Voornamen/Voornaam[3]"),
                    "Persoon.ZoekPersoonOpNaamDienst-02.02/WebService/ZoekPersoonOpNaam.xsd", "Persoon.ZoekPersoonOpNaamDienst-02.02/WebService/ZoekPersoonOpNaamResponse.xsd", MockSoapMapping.StubHandler.FileSoap, MissingParameterConfiguration.EmptyString),
            MockSoapMapping.create("Persoon", "CreeerBis", VERSION_02_02, List.of("//Persoon/Naam/Achternaam", "//Persoon/Naam/Voornamen/Voornaam"), SEPERATOR_FILE_NAME, "Persoon.CreeerBisDienst-02.02/WebService/CreeerBis.xsd", "Persoon.CreeerBisDienst-02.02/WebService/CreeerBisResponse.xsd"),

            // Repertorium
            MockSoapMapping.create("Repertorium", "RegistreerInschrijving", VERSION_02_00, List.of(KEY_INSZ), SEPERATOR_DIRECTORY, "Repertorium.RegistreerInschrijvingDienst-02.00/WebService/RegistreerInschrijving.xsd", "Repertorium.RegistreerInschrijvingDienst-02.00/WebService/RegistreerInschrijvingResponse.xsd"),
            MockSoapMapping.create("Repertorium", "RegistreerInschrijving", VERSION_02_01, List.of("//Subject/Type", "//Subject/Sleutel"), SEPERATOR_DIRECTORY, "Repertorium.RegistreerInschrijvingDienst-02.01/WebService/RegistreerInschrijving.xsd", "Repertorium.RegistreerInschrijvingDienst-02.01/WebService/RegistreerInschrijvingResponse.xsd"),
            MockSoapMapping.create("Repertorium", "RegistreerUitschrijving", VERSION_02_00, List.of(KEY_INSZ), SEPERATOR_DIRECTORY, "Repertorium.RegistreerUitschrijvingDienst-02.00/WebService/RegistreerUitschrijving.xsd", "Repertorium.RegistreerUitschrijvingDienst-02.00/WebService/RegistreerUitschrijvingResponse.xsd"),

            // SocEcon
            MockSoapMapping.create("SocEcon", "GeefStatusRechtOndersteuningen", VERSION_02_00, List.of(KEY_INSZ), SEPERATOR_DIRECTORY, "SocEcon.GeefStatusRechtOndersteuningenDienst-02.00/WebService/GeefStatusRechtOndersteuningen.xsd", "SocEcon.GeefStatusRechtOndersteuningenDienst-02.00/WebService/GeefStatusRechtOndersteuningenResponse.xsd"),

            // SocZek
            MockSoapMapping.create("SocZek", "GeefArbeidsongeschiktheid", VERSION_01_00, List.of(KEY_SSIN, "//yearQuarter"), SEPERATOR_FILE_NAME, "SocZek.GeefArbeidsongeschiktheidDienst-01.00/WebService/GeefArbeidsongeschiktheid.xsd", "SocZek.GeefArbeidsongeschiktheidDienst-01.00/WebService/GeefArbeidsongeschiktheidResponse.xsd"),
            MockSoapMapping.create("SocZek", "GeefAttestWerkloosheid", VERSION_01_00, List.of(KEY_INSS), SEPERATOR_DIRECTORY, "SocZek.GeefAttestWerkloosheidDienst-01.00/WebService/GeefAttestWerkloosheid.xsd", "SocZek.GeefAttestWerkloosheidDienst-01.00/WebService/GeefAttestWerkloosheidResponse.xsd"),
            MockSoapMapping.create("SocZek", "GeefBetalingenHandicap", VERSION_03_00, List.of(KEY_SSIN), SEPERATOR_DIRECTORY, "SocZek.GeefBetalingenHandicapDienst-03.00/WebService/GeefBetalingenHandicap.xsd", "SocZek.GeefBetalingenHandicapDienst-03.00/WebService/GeefBetalingenHandicapResponse.xsd"),
            MockSoapMapping.create("SocZek", "GeefDossierHandicap", VERSION_03_00, List.of("local-name(//Criteria/child::*[1])", KEY_SSIN), SEPERATOR_DIRECTORY, "SocZek.GeefDossierHandicapDienst-03.00/WebService/GeefDossierHandicap.xsd", "SocZek.GeefDossierHandicapDienst-03.00/WebService/GeefDossierHandicapResponse.xsd"),
            // Criteria/child::*[1]/name() -> tag name of the first child node in element Criteria -> ConsultFilesByDateCriteria | ConsultFilesByPeriodCriteria
            MockSoapMapping.create("SocZek", "GeefInschrijvingWerkzoekende", VERSION_01_00, List.of(KEY_SSIN), SEPERATOR_DIRECTORY, "SocZek.GeefInschrijvingWerkzoekendeDienst-01.00/WebService/GeefInschrijvingWerkzoekende.xsd", "SocZek.GeefInschrijvingWerkzoekendeDienst-01.00/WebService/GeefInschrijvingWerkzoekendeResponse.xsd"),
            MockSoapMapping.create("SocZek", "GeefLeefloonbedragen", VERSION_02_00, List.of(KEY_INSZ, "//Criteria/Jaar"), SEPERATOR_FILE_NAME, "SocZek.GeefLeefloonbedragenDienst-02.00/WebService/GeefLeefloonbedragen.xsd", "SocZek.GeefLeefloonbedragenDienst-02.00/WebService/GeefLeefloonbedragenResponse.xsd"),
            MockSoapMapping.create("SocZek", "GeefLeefloonperiodes", VERSION_02_00, List.of(KEY_INSZ), SEPERATOR_DIRECTORY, "SocZek.GeefLeefloonperiodesDienst-02.00/WebService/GeefLeefloonperiodes.xsd", "SocZek.GeefLeefloonperiodesDienst-02.00/WebService/GeefLeefloonperiodesResponse.xsd"),
            MockSoapMapping.create("SocZek", "GeefPensioen", VERSION_02_00, List.of(KEY_INSZ, "//Criteria/Pijler"), SEPERATOR_FILE_NAME, "SocZek.GeefPensioenDienst-02.00/WebService/GeefPensioen.xsd", "SocZek.GeefPensioenDienst-02.00/WebService/GeefPensioenResponse.xsd"),
            MockSoapMapping.create("SocZek", "GeefPensioenrechten", VERSION_02_00, List.of(KEY_INSZ, "//Criteria/Pijler"), SEPERATOR_FILE_NAME, "SocZek.GeefPensioenrechtenDienst-02.00/WebService/GeefPensioenrechten.xsd", "SocZek.GeefPensioenrechtenDienst-02.00/WebService/GeefPensioenrechtenResponse.xsd"),
            MockSoapMapping.create("SocZek", "GeefStatuutRVV", VERSION_02_00, List.of(KEY_INSZ), SEPERATOR_DIRECTORY, "SocZek.GeefStatuutRVVDienst-02.00/WebService/GeefStatuutRVV.xsd", "SocZek.GeefStatuutRVVDienst-02.00/WebService/GeefStatuutRVVResponse.xsd"),
            MockSoapMapping.create("SocZek", "GeefSociaalStatuut", VERSION_03_00, List.of(KEY_INSZ), SEPERATOR_DIRECTORY, "SocZek.GeefSociaalStatuutDienst-03.00/WebService/GeefSociaalStatuut.xsd", "SocZek.GeefSociaalStatuutDienst-03.00/WebService/GeefSociaalStatuutResponse.xsd"),
            MockSoapMapping.create("SocZek", "GeefVervangingsinkomenUitWerkloosheid", VERSION_02_01, List.of(KEY_INSZ), SEPERATOR_DIRECTORY, "SocZek.GeefVervangingsinkomenUitWerkloosheidDienst-02.01/WebService/GeefVervangingsinkomenUitWerkloosheid.xsd", "SocZek.GeefVervangingsinkomenUitWerkloosheidDienst-02.01/WebService/GeefVervangingsinkomenUitWerkloosheidResponse.xsd"),
            MockSoapMapping.create("SocZek", "GeefVolledigDossierHandicap", VERSION_03_00, List.of(KEY_RRNR), SEPERATOR_DIRECTORY, "SocZek.GeefVolledigDossierHandicapDienst-03.00/WebService/GeefVolledigDossierHandicap.xsd", "SocZek.GeefVolledigDossierHandicapDienst-03.00/WebService/GeefVolledigDossierHandicapResponse.xsd"),
            MockSoapMapping.create("SocZek", "GeefWerkzoekende", VERSION_02_00, List.of(KEY_INSZ), SEPERATOR_DIRECTORY, "SocZek.GeefWerkzoekendeDienst-02.00/WebService/GeefWerkzoekende.xsd", "SocZek.GeefWerkzoekendeDienst-02.00/WebService/GeefWerkzoekendeResponse.xsd"),

            // Werk
            MockSoapMapping.create("Werk", "GeefLoopbaanARZA", VERSION_02_01, List.of(KEY_INSZ), SEPERATOR_DIRECTORY, "Werk.GeefLoopbaanARZADienst-02.01/WebService/GeefLoopbaanARZA.xsd", "Werk.GeefLoopbaanARZADienst-02.01/WebService/GeefLoopbaanARZAResponse.xsd"),
            MockSoapMapping.create("Werk", "GeefLoopbaanonderbrekingen", VERSION_02_00, List.of(KEY_INSZ), SEPERATOR_DIRECTORY, "Werk.GeefLoopbaanonderbrekingenDienst-02.00/WebService/GeefLoopbaanonderbrekingen.xsd", "Werk.GeefLoopbaanonderbrekingenDienst-02.00/WebService/GeefLoopbaanonderbrekingenResponse.xsd"),
            MockSoapMapping.create("Werk", "GeefWerkrelaties", VERSION_02_00, List.of(KEY_INSZ, KEY_ONDERNEMINGSNUMMER, "//Criteria/Relatie/Werkgever/RSZNummer"), SEPERATOR_FILE_NAME, "Werk.GeefWerkrelatiesDienst-02.00/WebService/GeefWerkrelaties.xsd", "Werk.GeefWerkrelatiesDienst-02.00/WebService/GeefWerkrelatiesResponse.xsd"),
            MockSoapMapping.create("Werk", "GeefDmfaVoorWerknemer", VERSION_03_00, List.of(KEY_INSZ, "//Volgende"), SEPERATOR_FILE_NAME, "Werk.GeefDmfaVoorWerknemerDienst-03.00/WebService/GeefDmfaVoorWerknemer.xsd", "Werk.GeefDmfaVoorWerknemerDienst-03.00/WebService/GeefDmfaVoorWerknemerResponse.xsd"),
            MockSoapMapping.create("Werk", "GeefBijdrageARZA", VERSION_01_00, List.of(KEY_SSIN), SEPERATOR_DIRECTORY, "Werk.GeefBijdrageARZADienst-01.00/WebService/GeefBijdrageARZA.xsd", "Werk.GeefBijdrageARZADienst-01.00/WebService/GeefBijdrageARZAResponse.xsd"),

            //Vlok
            MockSoapMapping.create("Vlok", "BewaarWoningKwaliteit", VERSION_02_00, List.of("//Criteria/NISCode", "local-name(//Criteria/NISCode/following-sibling::*[1])"), SEPERATOR_FILE_NAME, "Vlok.BewaarWoningKwaliteitDienst-02.00/WebService/BewaarWoningKwaliteit.xsd", "Vlok.BewaarWoningKwaliteitDienst-02.00/WebService/BewaarWoningKwaliteitResponse.xsd"),
            // following-sibling::*[1]/name() -> tag name of the element after NISCode
            MockSoapMapping.create("Vlok", "BewaarWoningKwaliteitBijlage", VERSION_02_00, List.of("//Criteria/NISCode"), SEPERATOR_FILE_NAME, "Vlok.BewaarWoningKwaliteitBijlageDienst-02.00/WebService/BewaarWoningKwaliteitBijlage.xsd", "Vlok.BewaarWoningKwaliteitBijlageDienst-02.00/WebService/BewaarWoningKwaliteitBijlageResponse.xsd"),
            MockSoapMapping.create("Vlok", "GeefWoningKwaliteit", VERSION_02_00, List.of("//NISCode", "//Criteria/Type", "//Criteria/Referte"), SEPERATOR_FILE_NAME, "Vlok.GeefWoningKwaliteitDienst-02.00/WebService/GeefWoningKwaliteit.xsd", "Vlok.GeefWoningKwaliteitDienst-02.00/WebService/GeefWoningKwaliteitResponse.xsd"),
            MockSoapMapping.create("Vlok", "GeefWoningKwaliteitBijlage", VERSION_02_00, List.of("//NISCode", "//Criteria/Bijlagen/Bijlage[1]/Type", "//Criteria/Bijlagen/Bijlage[1]/Referte"), SEPERATOR_FILE_NAME, "Vlok.GeefWoningKwaliteitBijlageDienst-02.00/WebService/GeefWoningKwaliteitBijlage.xsd", "Vlok.GeefWoningKwaliteitBijlageDienst-02.00/WebService/GeefWoningKwaliteitBijlageResponse.xsd"),
            MockSoapMapping.create("Vlok", "ZoekWoningKwaliteit", VERSION_02_00, List.of("//NISCode", "//Zoekterm"), SEPERATOR_FILE_NAME, "Vlok.ZoekWoningKwaliteitDienst-02.00/WebService/ZoekWoningKwaliteit.xsd", "Vlok.ZoekWoningKwaliteitDienst-02.00/WebService/ZoekWoningKwaliteitResponse.xsd")
    );

}
