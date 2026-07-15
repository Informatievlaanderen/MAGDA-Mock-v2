package be.vlaanderen.vip.magda.magdamock.filters;

import be.vlaanderen.vip.magda.magdamock.utils.MagdaMockDocument;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;

public class EmptyElementsFilter implements MagdaMockFilter {

    private static final String XSLT = """
        <xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
          <xsl:template match="*[not(@*) and not(*) and not(normalize-space())]"/>
          
          <xsl:template match="node()|@*">
            <xsl:copy>
              <xsl:apply-templates select="node()|@*"/>
            </xsl:copy>
          </xsl:template>
        </xsl:stylesheet>
        """;

    public Document filter(MagdaMockDocument request, Document response) {
        if (response == null) {
            return null;
        }
        try {
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer(new StreamSource(new StringReader(XSLT)));

            DOMResult outputTarget = new DOMResult();

            transformer.transform(new DOMSource(response), outputTarget);
            return (Document) outputTarget.getNode();
        } catch (TransformerException e) {
            return response;
        }
    }
}
