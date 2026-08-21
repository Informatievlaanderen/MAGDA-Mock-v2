package be.vlaanderen.vip.magda.magdamock.exceptions;

import be.vlaanderen.vip.magda.magdamock.utils.MagdaMockDocument;
import be.vlaanderen.vip.magda.magdamock.filters.EmptyElementsFilter;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.StringEscapeUtils;

/**
 * An exception to be thrown by MagdaMock in case MagdaMock really fails to handle a response,
 * rather than simulating a backend failing to handle a response.
 */
@Slf4j
public class MagdaMockSoapException extends RuntimeException {

    @Getter
    private final String document;

    public MagdaMockSoapException(String faultString, String faultCode, Throwable cause) {
        super(faultString, cause);

        String document = String.format("""
                                                <SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/">
                                                <SOAP-ENV:Header/>
                                                <SOAP-ENV:Body>
                                                    <ns0:Fault xmlns:ns0="http://schemas.xmlsoap.org/soap/envelope/">
                                                        <faultcode>%s</faultcode>
                                                        <faultstring>%s</faultstring>
                                                    </ns0:Fault>
                                                </SOAP-ENV:Body>
                                            </SOAP-ENV:Envelope>

                """, escapeXml(faultCode), escapeXml(faultString));
        log.error(faultString, cause);
        this.document = document;
    }

    public MagdaMockSoapException(String faultString, String faultCode, String detail, Throwable cause) {
        super(faultString, cause);
        String document = String.format("""
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

                """, escapeXml(faultCode), escapeXml(faultString), escapeXml(detail));
        log.error("{} Reason: {}", faultString, detail, cause);
        this.document = document;
    }

    private static String escapeXml(String value) {
        return value == null ? "" : StringEscapeUtils.escapeXml10(value);
    }
}