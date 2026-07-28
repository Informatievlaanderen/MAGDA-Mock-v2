package be.vlaanderen.vip.magda.magdamock.soap;

import be.vlaanderen.vip.magda.magdamock.utils.MagdaMockDocument;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

public abstract class SoapBodyValidator {
    protected static Map<String, String> data(String... data) {
        return IntStream.iterate(0, i -> i < data.length, i -> i + 2)
                .collect(HashMap::new,
                        (m, i) -> m.put(data[i], data[i + 1]),
                        HashMap::putAll);
    }

    public abstract void validateXml(MagdaMockDocument magdaMockDocument) throws SoapValidationError;

    protected Document nodelistToDocument(NodeList nodeList) throws ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document newDocument = builder.newDocument();
        Node node = nodeList.item(0);
        Node importedNode = newDocument.importNode(node, true);
        newDocument.appendChild(importedNode);
        return newDocument;
    }

    public static class XsdErrorHandler implements ErrorHandler {

        @Override
        public void warning(SAXParseException exception) {
        }

        @Override
        public void error(SAXParseException exception) throws SAXException {
            handleMessage(exception);
        }

        @Override
        public void fatalError(SAXParseException exception) throws SAXException {
            handleMessage(exception);
        }

        private void handleMessage(SAXParseException e) throws SAXException {
            throw new SAXException("Error occurred on line %s: %s".formatted(e.getLineNumber(), e.getMessage()));
        }
    }
}
