package be.vlaanderen.vip.magda.magdamock.client.exceptions;

import be.vlaanderen.vip.magda.client.MagdaDocument;
import be.vlaanderen.vip.magda.magdamock.filters.EmptyElementsFilter;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * An exception to be thrown by MagdaMock in case MagdaMock really fails to handle a response,
 * rather than simulating a backend failing to handle a response.
 */
@Slf4j
public class MagdaMockSoapException extends RuntimeException {

    @Getter
    private final MagdaDocument document;

    public MagdaMockSoapException(String faultString, String faultCode, String detail, Throwable cause) {
        super(faultString, cause);
        MagdaDocument document = MagdaDocument.fromString(String.format("""
                                                <SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/">
                                                <SOAP-ENV:Header/>
                                                <SOAP-ENV:Body>
                                                    <ns0:Fault xmlns:ns0="http://schemas.xmlsoap.org/soap/envelope/">
                                                        <faultcode>%s</faultcode>
                                                        <faultstring>%s</faultstring>
                                                        <detail>
                                                            <message>%s</message>
                                                        </detail>
                                                    </ns0:Fault>
                                                </SOAP-ENV:Body>
                                            </SOAP-ENV:Envelope>
                
                """, faultCode, faultString, detail));
        log.error(faultString, cause);
        this.document = MagdaDocument.fromDocument(EmptyElementsFilter.getInstance().filter(null,EmptyElementsFilter.getInstance().filter(null, document.getXml())));
    }
}