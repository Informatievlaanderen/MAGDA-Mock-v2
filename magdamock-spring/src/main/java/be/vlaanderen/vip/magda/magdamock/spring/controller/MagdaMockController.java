package be.vlaanderen.vip.magda.magdamock.spring.controller;


import be.vlaanderen.vip.magda.client.MagdaDocument;
import be.vlaanderen.vip.magda.exception.MagdaConnectionException;
import be.vlaanderen.vip.magda.magdamock.client.MagdaMockConnection;
import be.vlaanderen.vip.magda.magdamock.soap.SoapValidationError;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.TEXT_XML;
import static org.springframework.util.MimeTypeUtils.APPLICATION_XML_VALUE;
import static org.springframework.util.MimeTypeUtils.TEXT_XML_VALUE;

@RestController
@Slf4j
public class MagdaMockController {
    // Gemeenschappelijk endpoint voor alle MAGDA SOAP 2.0 webservices
    private static final String MAGDA_SOAP_02_00 = "Magda-02.00/soap/WebService";
    // Gemeenschappelijk endpoint voor alle MAGDA REST webservices
    private static final String MAGDA_REST_V1 = "Magda-v1/rest";

    private final MagdaMockConnection mockConnection;

    public MagdaMockController(MagdaMockConnection mockConnection) {
        this.mockConnection = mockConnection;
    }

    @PostMapping(value = {MAGDA_SOAP_02_00, "api/" + MAGDA_SOAP_02_00}, produces = {TEXT_XML_VALUE}, consumes = {APPLICATION_XML_VALUE, TEXT_XML_VALUE})
    public ResponseEntity<String> magdaSoap0200WebService(@RequestBody String request) throws MagdaConnectionException {
        return processMagdaMockRequest(request);
    }

    private ResponseEntity<String> processMagdaMockRequest(String request) throws MagdaConnectionException {
        //TODO: handle request parsing errors and return Magda Uitzondering error
        try {
            MagdaDocument requestDocument = parseDocument(request);
            var magdaResponse = mockConnection.sendDocument(requestDocument.getXml());
            if (magdaResponse != null) {
                return parseInputstream(MagdaDocument.fromDocument(magdaResponse));

            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (SoapValidationError e) {
            return ResponseEntity.internalServerError().contentType(TEXT_XML).body(e.getExceptionBody().toString());
        }
    }

    private MagdaDocument parseDocument(String request) throws SoapValidationError {
        try {
            return MagdaDocument.fromString(request);
        } catch (Exception e) {
            throw new SoapValidationError(MagdaDocument.fromString("""
                    <SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/">
                        <SOAP-ENV:Header/>
                        <SOAP-ENV:Body>
                            <ns0:Fault xmlns:ns0="http://schemas.xmlsoap.org/soap/envelope/">
                                <faultcode>soap:Client</faultcode>
                                <faultstring>Problems creating SAAJ object model</faultstring>
                            </ns0:Fault>
                        </SOAP-ENV:Body>
                    </SOAP-ENV:Envelope>
                    """
            ));
        }
    }

    private ResponseEntity<String> parseInputstream(MagdaDocument magdaDocument) {
        if (magdaDocument != null) {
            return ResponseEntity.ok().contentType(TEXT_XML).body(magdaDocument.toString());
        } else {
            log.error("Could not find XML");

            // TODO: maak en return MAGDA Uitzondering antwoord
            return ResponseEntity.notFound().build();
        }
    }


    @RequestMapping(
            value = {MAGDA_REST_V1 + "/**", "api/" + MAGDA_REST_V1 + "/**"},
            produces = {APPLICATION_JSON_VALUE}, consumes = {APPLICATION_JSON_VALUE},
            method = {RequestMethod.DELETE, RequestMethod.GET, RequestMethod.PATCH, RequestMethod.POST, RequestMethod.PUT}
    )
    protected ResponseEntity<String> magdaRestEndpoint(@RequestBody(required = false) String requestBody, HttpServletRequest incomingRequest) throws MagdaConnectionException {
        requestBody = requestBody == null ? "" : requestBody;
        String method = incomingRequest.getMethod();
        List<String> splittedRequestUri = new ArrayList<>(Arrays.stream(incomingRequest.getRequestURI().split(Pattern.quote(MAGDA_REST_V1))).toList());
        String query = incomingRequest.getQueryString();
        splittedRequestUri.remove(0);
        String path = String.join(MAGDA_REST_V1, splittedRequestUri);
        String dateHeaderName = "date", correlationIdHeaderName = "x-correlation-id";
        for (Iterator<String> it = incomingRequest.getHeaderNames().asIterator(); it.hasNext(); ) {
            String headerName = it.next();
            if (headerName.equalsIgnoreCase(dateHeaderName)) {
                dateHeaderName = headerName;
            }
            if (headerName.equalsIgnoreCase(correlationIdHeaderName)) {
                correlationIdHeaderName = headerName;
            }
        }
        var response = mockConnection.sendRestRequest(path, query, method, requestBody, incomingRequest.getHeader(dateHeaderName), incomingRequest.getHeader(correlationIdHeaderName));
        return new ResponseEntity<>(Optional.ofNullable(response.body()).map(Object::toString).orElse(""), CollectionUtils.toMultiValueMap(response.headers()), HttpStatusCode.valueOf(response.status()));
    }
}
