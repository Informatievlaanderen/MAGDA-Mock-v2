package be.vlaanderen.vip.magda.magdamock.client.filters;

import be.vlaanderen.vip.magda.magdamock.utils.MagdaDocument;
import be.vlaanderen.vip.magda.magdamock.filters.EmptyElementsFilter;
import be.vlaanderen.vip.magda.magdamock.filters.MagdaMockFilter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;

public class EmptyElementsFilterTests {
    @Test
    public void testEmptyElementsFilter() {
        MagdaMockFilter magdaMockFilter = new EmptyElementsFilter();
        Document document = MagdaDocument.fromString("""
                <root>
                    <Element1>filled in</Element1>
                    <Element2 attribute="filled in"></Element2>
                    <Element3></Element3>
                    <Element4/>
                </root>
                """).getXml();
        document = magdaMockFilter.filter(null, document);
        MagdaDocument magdaDocument = MagdaDocument.fromDocument(document);
        Assertions.assertEquals(1, document.getElementsByTagName("Element1").getLength());
        Assertions.assertEquals(1, document.getElementsByTagName("Element2").getLength());
        Assertions.assertEquals(0, document.getElementsByTagName("Element3").getLength());
        Assertions.assertEquals(0, document.getElementsByTagName("Element4").getLength());
    }
}
