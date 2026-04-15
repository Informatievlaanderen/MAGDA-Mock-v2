package be.vlaanderen.vip.magda.magdamock.client.soap;

import be.vlaanderen.vip.magda.client.MagdaDocument;
import be.vlaanderen.vip.magda.magdamock.client.MagdaMockConnection;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
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

        MagdaMockConnection magdaMockConnection;

        @BeforeEach
        void setUp() throws IOException, URISyntaxException {
                Path path = Paths.get(getClass()
                        .getClassLoader()
                        .getResource("soap")
                        .toURI());

                magdaMockConnection = MagdaMockConnection.create("", path.toAbsolutePath().toString(), "");
        }

        @ParameterizedTest
        @MethodSource("allSoapServices")
        void testService(
                String naam,
                String versie,
                List<RequestField> requestFields,
                String xpathExpression,
                String expectedValue
        ) throws XPathExpressionException {

                String requestBody = buildSoapRequest(naam, versie, requestFields);
                MagdaDocument magdaDocument = MagdaDocument.fromString(requestBody);
                Document document = magdaMockConnection.sendDocument(magdaDocument.getXml());

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

        static Stream<Arguments> allSoapServices() {
                ObjectMapper objectMapper = new ObjectMapper();

                try (var inputStream = SOAPServicesTest.class
                        .getClassLoader()
                        .getResourceAsStream("soap/soap-services.json")) {

                        if (inputStream == null) {
                                throw new IllegalStateException("Resource not found: soap/soap-services.json");
                        }

                        List<SoapServiceArgument> cases = objectMapper.readValue(
                                inputStream,
                                new TypeReference<List<SoapServiceArgument>>() {}
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

                        appendPathValue(document, request, "Naam", naam);
                        appendPathValue(document, request, "Versie", versie);

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

                for (String part : parts) {
                        Element child = findDirectChild(current, part);
                        if (child == null) {
                                child = document.createElement(part);
                                current.appendChild(child);
                        }
                        current = child;
                }

                current.setTextContent(value);
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

        record RequestField(String path, String value) {}
}

