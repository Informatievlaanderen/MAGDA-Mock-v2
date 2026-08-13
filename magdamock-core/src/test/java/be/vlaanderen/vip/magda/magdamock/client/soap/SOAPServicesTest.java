package be.vlaanderen.vip.magda.magdamock.client.soap;

import be.vlaanderen.vip.magda.magdamock.client.MagdaMockConnection;
import be.vlaanderen.vip.magda.magdamock.client.handlers.MagdaMockSoapHandler;
import be.vlaanderen.vip.magda.magdamock.exceptions.MagdaMockSoapException;
import be.vlaanderen.vip.magda.magdamock.utils.MagdaMockDocument;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;


class SOAPServicesTest {

    private static String buildSoapRequest(String naam, String versie, List<RequestField> requestFields) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);

            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();

            Element envelope = document.createElementNS(
                    "http://schemas.xmlsoap.org/soap/envelope/",
                    "soapenv:Envelope"
            );
            document.appendChild(envelope);

            Element body = document.createElementNS(
                    "http://schemas.xmlsoap.org/soap/envelope/",
                    "soapenv:Body"
            );
            envelope.appendChild(body);

            Element request = document.createElement("Request");
            body.appendChild(request);

            appendPathValue(document, request, "//Verzoek/Context/Naam", naam);
            appendPathValue(document, request, "//Verzoek/Context/Versie", versie);

            appendPathValue(document, request, "Bericht/Afzender/Identificatie", "soap-services-test-identificatie");

            for (RequestField requestField : requestFields) {
                appendPathValue(document, request, requestField.path(), requestField.value());
            }

            return toXml(document);
        } catch (Exception e) {
            throw new IllegalStateException("Unable to build SOAP request", e);
        }
    }

    private static void appendPathValue(Document document, Element request, String path, String value) {
        String normalizedPath = normalizePath(path);
        String[] parts = normalizedPath.split("/");

        Element current = request;

        for (int i = 0; i < parts.length - 1; i++) {
            Element child = findDirectChild(current, parts[i]);
            if (child == null) {
                child = document.createElement(parts[i]);
                current.appendChild(child);
            }
            current = child;
        }

        Element leaf = document.createElement(parts[parts.length - 1]);
        current.appendChild(leaf);
        leaf.setTextContent(value);
    }

    private static String normalizePath(String path) {
        if (path.startsWith("//")) {
            return path.substring(2);
        }
        if (path.startsWith("/")) {
            return path.substring(1);
        }
        return path;
    }

    private static Element findDirectChild(Element parent, String tagName) {
        NodeList children = parent.getChildNodes();

        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            if (child.getNodeType() == Node.ELEMENT_NODE && tagName.equals(child.getNodeName())) {
                return (Element) child;
            }
        }

        return null;
    }

    private static String toXml(Document document) throws Exception {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        StringWriter writer = new StringWriter();
        transformer.transform(new DOMSource(document), new StreamResult(writer));
        return writer.toString();
    }

    record RequestField(String path, String value) {
    }

    @Nested
    class MappingsTest {
        MagdaMockConnection magdaMockConnection;

        static Stream<Arguments> allSoapServices(String filename) {
            ObjectMapper objectMapper = new ObjectMapper();

            try (var inputStream = SOAPServicesTest.class
                    .getClassLoader()
                    .getResourceAsStream(filename)) {

                if (inputStream == null) {
                    throw new IllegalStateException(String.format("Resource not found: %s", filename));
                }

                List<SoapServiceArgument> cases = objectMapper.readValue(
                        inputStream,
                        new TypeReference<List<SoapServiceArgument>>() {
                        }
                );

                return cases.stream()
                        .map(c -> arguments(
                                c.naam(),
                                c.versie(),
                                c.requestFields(),
                                c.xpathExpression(),
                                c.expectedValue()
                        ));
            } catch (IOException e) {
                throw new IllegalStateException("Unable to load SOAP service arguments from JSON", e);
            }
        }

        @BeforeEach
        void setUp() throws IOException, URISyntaxException {
            Path path = Paths.get(getClass()
                    .getClassLoader()
                    .getResource("soap")
                    .toURI());

            magdaMockConnection = MagdaMockConnection.create("", path.toAbsolutePath().toString(), "", false, false);
        }

        static Stream<Arguments> dossierServices() {
            return allSoapServices("soap/test-definitions/dossier-services.json");
        }

        @SneakyThrows
        @ParameterizedTest
        @MethodSource("dossierServices")
        void testDossierServices(
                String naam,
                String versie,
                List<RequestField> requestFields,
                String xpathExpression,
                String expectedValue
        ) {
            testService(naam, versie, requestFields, xpathExpression, expectedValue);
        }

        static Stream<Arguments> energieServices() {
            return allSoapServices("soap/test-definitions/energie-services.json");
        }

        @SneakyThrows
        @ParameterizedTest
        @MethodSource("energieServices")
        void testenergieServices(
                String naam,
                String versie,
                List<RequestField> requestFields,
                String xpathExpression,
                String expectedValue
        ) {
            testService(naam, versie, requestFields, xpathExpression, expectedValue);
        }

        static Stream<Arguments> gezinServices() {
            return allSoapServices("soap/test-definitions/gezin-services.json");
        }

        @SneakyThrows
        @ParameterizedTest
        @MethodSource("gezinServices")
        void testgezinServices(
                String naam,
                String versie,
                List<RequestField> requestFields,
                String xpathExpression,
                String expectedValue
        ) {
            testService(naam, versie, requestFields, xpathExpression, expectedValue);
        }

        static Stream<Arguments> inkomenServices() {
            return allSoapServices("soap/test-definitions/inkomen-services.json");
        }

        @SneakyThrows
        @ParameterizedTest
        @MethodSource("inkomenServices")
        void testinkomenServices(
                String naam,
                String versie,
                List<RequestField> requestFields,
                String xpathExpression,
                String expectedValue
        ) {
            testService(naam, versie, requestFields, xpathExpression, expectedValue);
        }

        static Stream<Arguments> kadasterServices() {
            return allSoapServices("soap/test-definitions/kadaster-services.json");
        }

        @SneakyThrows
        @ParameterizedTest
        @MethodSource("kadasterServices")
        void testkadasterServices(
                String naam,
                String versie,
                List<RequestField> requestFields,
                String xpathExpression,
                String expectedValue
        ) {
            testService(naam, versie, requestFields, xpathExpression, expectedValue);
        }

        static Stream<Arguments> ledServices() {
            return allSoapServices("soap/test-definitions/led-services.json");
        }

        @SneakyThrows
        @ParameterizedTest
        @MethodSource("ledServices")
        void testledServices(
                String naam,
                String versie,
                List<RequestField> requestFields,
                String xpathExpression,
                String expectedValue
        ) {
            testService(naam, versie, requestFields, xpathExpression, expectedValue);
        }

        static Stream<Arguments> ondernemingServices() {
            return allSoapServices("soap/test-definitions/onderneming-services.json");
        }

        @SneakyThrows
        @ParameterizedTest
        @MethodSource("ondernemingServices")
        void testondernemingServices(
                String naam,
                String versie,
                List<RequestField> requestFields,
                String xpathExpression,
                String expectedValue
        ) {
            testService(naam, versie, requestFields, xpathExpression, expectedValue);
        }

        static Stream<Arguments> onderwijsServices() {
            return allSoapServices("soap/test-definitions/onderwijs-services.json");
        }

        @SneakyThrows
        @ParameterizedTest
        @MethodSource("onderwijsServices")
        void testonderwijsServices(
                String naam,
                String versie,
                List<RequestField> requestFields,
                String xpathExpression,
                String expectedValue
        ) {
            testService(naam, versie, requestFields, xpathExpression, expectedValue);
        }

        static Stream<Arguments> persoonServices() {
            return allSoapServices("soap/test-definitions/persoon-services.json");
        }

        @SneakyThrows
        @ParameterizedTest
        @MethodSource("persoonServices")
        void testpersoonServices(
                String naam,
                String versie,
                List<RequestField> requestFields,
                String xpathExpression,
                String expectedValue
        ) {
            testService(naam, versie, requestFields, xpathExpression, expectedValue);
        }

        static Stream<Arguments> repertoriumServices() {
            return allSoapServices("soap/test-definitions/repertorium-services.json");
        }

        @SneakyThrows
        @ParameterizedTest
        @MethodSource("repertoriumServices")
        void testrepertoriumServices(
                String naam,
                String versie,
                List<RequestField> requestFields,
                String xpathExpression,
                String expectedValue
        ) {
            testService(naam, versie, requestFields, xpathExpression, expectedValue);
        }

        static Stream<Arguments> soceconServices() {
            return allSoapServices("soap/test-definitions/socecon-services.json");
        }

        @SneakyThrows
        @ParameterizedTest
        @MethodSource("soceconServices")
        void testsoceconServices(
                String naam,
                String versie,
                List<RequestField> requestFields,
                String xpathExpression,
                String expectedValue
        ) {
            testService(naam, versie, requestFields, xpathExpression, expectedValue);
        }

        static Stream<Arguments> soczekServices() {
            return allSoapServices("soap/test-definitions/soczek-services.json");
        }

        @SneakyThrows
        @ParameterizedTest
        @MethodSource("soczekServices")
        void testsoczekServices(
                String naam,
                String versie,
                List<RequestField> requestFields,
                String xpathExpression,
                String expectedValue
        ) {
            testService(naam, versie, requestFields, xpathExpression, expectedValue);
        }

        static Stream<Arguments> vlokServices() {
            return allSoapServices("soap/test-definitions/vlok-services.json");
        }

        @SneakyThrows
        @ParameterizedTest
        @MethodSource("vlokServices")
        void testvlokServices(
                String naam,
                String versie,
                List<RequestField> requestFields,
                String xpathExpression,
                String expectedValue
        ) {
            testService(naam, versie, requestFields, xpathExpression, expectedValue);
        }

        static Stream<Arguments> werkServices() {
            return allSoapServices("soap/test-definitions/werk-services.json");
        }

        @SneakyThrows
        @ParameterizedTest
        @MethodSource("werkServices")
        void testwerkServices(
                String naam,
                String versie,
                List<RequestField> requestFields,
                String xpathExpression,
                String expectedValue
        ) {
            testService(naam, versie, requestFields, xpathExpression, expectedValue);
        }

        void testService(
                String naam,
                String versie,
                List<RequestField> requestFields,
                String xpathExpression,
                String expectedValue
        ) throws XPathExpressionException {
            String requestBody = buildSoapRequest(naam, versie, requestFields);
            MagdaMockDocument magdaMockDocument = MagdaMockDocument.fromString(requestBody);
            MagdaMockSoapHandler.MockSoapResponse response = magdaMockConnection.sendSoapRequest(new MagdaMockSoapHandler.MockSoapRequest(magdaMockDocument.getXml()));
            Document document = response.document();

            XPath xpath = XPathFactory.newInstance().newXPath();
            String actualValue = xpath.evaluate(xpathExpression, document);

            assertEquals(expectedValue, actualValue);
        }

        record SoapServiceArgument(
                String naam,
                String versie,
                List<RequestField> requestFields,
                String xpathExpression,
                String expectedValue
        ) {
        }
    }

    @Nested
    class ExceptionTests {
        MagdaMockConnection magdaMockConnection;

        @BeforeEach
        void setUp() throws IOException, URISyntaxException {
            Path path = Paths.get(getClass()
                    .getClassLoader()
                    .getResource("soap")
                    .toURI());

            magdaMockConnection = MagdaMockConnection.create("", path.toAbsolutePath().toString(), "", false, false);
        }

        @Test
        public void whenInvalidServiceIsGiven_shouldThrowException() {
            String requestBody = buildSoapRequest("Unknown", "versie", List.of());
            MagdaMockDocument magdaMockDocument = MagdaMockDocument.fromString(requestBody);
            Assertions.assertThrows(MagdaMockSoapException.class, () -> magdaMockConnection.sendSoapRequest(new MagdaMockSoapHandler.MockSoapRequest(magdaMockDocument.getXml())));
        }
    }
}

