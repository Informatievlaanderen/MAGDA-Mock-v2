package be.vlaanderen.vip.magda.magdamock.filters;

import be.vlaanderen.vip.magda.client.MagdaDocument;
import org.w3c.dom.Document;

import javax.xml.transform.Transformer;
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
    private static EmptyElementsFilter INSTANCE;

    public static EmptyElementsFilter getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new EmptyElementsFilter();
        }
        return INSTANCE;
    }

    public Document filter(MagdaDocument request, Document response) {
        if (response == null) {
            return null;
        }
        try {
            Document returnValue = response;
            boolean stop = false;
            int counter = 0;
            while(!stop && ++counter < 10) {
                counter++;
                TransformerFactory factory = TransformerFactory.newInstance();
                Transformer transformer = factory.newTransformer(new StreamSource(new StringReader(XSLT)));

                DOMResult outputTarget = new DOMResult();

                transformer.transform(new DOMSource(returnValue), outputTarget);
                Document temporaryValue = (Document) outputTarget.getNode();
                stop = MagdaDocument.fromDocument(temporaryValue).toString().equals(MagdaDocument.fromDocument(returnValue).toString());
                returnValue = temporaryValue;
            }
            return returnValue;
        } catch (TransformerException e) {
            return response;
        }
    }
}
