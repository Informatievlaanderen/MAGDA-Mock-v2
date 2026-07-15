package be.vlaanderen.vip.magda.magdamock.spring.controller;


import be.vlaanderen.vip.magda.magdamock.utils.MagdaMockDocument;
import be.vlaanderen.vip.magda.magdamock.client.MagdaMockConnection;
import be.vlaanderen.vip.magda.magdamock.client.handlers.MagdaMockRestHandler;
import be.vlaanderen.vip.magda.magdamock.client.handlers.MagdaMockSoapHandler;
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.TEXT_XML;
import static org.springframework.util.MimeTypeUtils.APPLICATION_XML_VALUE;
import static org.springframework.util.MimeTypeUtils.TEXT_XML_VALUE;

@RestController
@Slf4j
public class MagdaMockController {
    // Gemeenschappelijk endpoint voor alle soap
    private static final String SOAP_BASE_URL = "/soap";
    // Gemeenschappelijk endpoint voor alle rest
    private static final String REST_BASE_URL = "/rest";

    private final MagdaMockConnection mockConnection;

    public MagdaMockController(MagdaMockConnection mockConnection) {
        this.mockConnection = mockConnection;
    }

    @PostMapping(value = {SOAP_BASE_URL}, produces = {TEXT_XML_VALUE}, consumes = {APPLICATION_XML_VALUE, TEXT_XML_VALUE})
    public ResponseEntity<String> magdaSoap0200WebService(@RequestBody String request) {
        return processMagdaMockRequest(request);
    }

    private ResponseEntity<String> processMagdaMockRequest(String request) {
        //TODO: handle request parsing errors and return Magda Uitzondering error
        try {
            MagdaMockDocument requestDocument = parseDocument(request);
            var magdaResponse = mockConnection.sendSoapRequest(new MagdaMockSoapHandler.MockSoapRequest(requestDocument.getXml()));
            if (magdaResponse != null) {
                return parseInputstream(MagdaMockDocument.fromDocument(magdaResponse.document()));

            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (SoapValidationError e) {
            return ResponseEntity.internalServerError().contentType(TEXT_XML).body(e.getExceptionBody().toString());
        }
    }

    private MagdaMockDocument parseDocument(String request) throws SoapValidationError {
        try {
            return MagdaMockDocument.fromString(request);
        } catch (Exception e) {
            throw new SoapValidationError(MagdaMockDocument.fromString("""
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

    private ResponseEntity<String> parseInputstream(MagdaMockDocument magdaMockDocument) {
        if (magdaMockDocument != null) {
            return ResponseEntity.ok().contentType(TEXT_XML).body(magdaMockDocument.toString());
        } else {
            log.error("Could not find XML");

            // TODO: maak en return MAGDA Uitzondering antwoord
            return ResponseEntity.notFound().build();
        }
    }


    @RequestMapping(
            value = {REST_BASE_URL + "/**", "api/" + REST_BASE_URL + "/**"},
            method = {RequestMethod.DELETE, RequestMethod.GET, RequestMethod.PATCH, RequestMethod.POST, RequestMethod.PUT}
    )
    protected ResponseEntity<String> magdaRestEndpoint(@RequestBody(required = false) String requestBody, HttpServletRequest incomingRequest) {
        requestBody = requestBody == null ? "" : requestBody;
        String method = incomingRequest.getMethod();
        List<String> splittedRequestUri = new ArrayList<>(Arrays.stream(incomingRequest.getRequestURI().split(Pattern.quote(REST_BASE_URL))).toList());
        String query = incomingRequest.getQueryString();
        splittedRequestUri.remove(0);
        String path = String.join(REST_BASE_URL, splittedRequestUri);
        Map<String, String> headers = new HashMap<>();
        for (Iterator<String> it = incomingRequest.getHeaderNames().asIterator(); it.hasNext(); ) {
            String headerName = it.next();
            headers.put(headerName.toLowerCase(), incomingRequest.getHeader(headerName));
        }
        var response = mockConnection.sendRestRequest(new MagdaMockRestHandler.MockRestRequest(path, query, method, requestBody, headers));
        return new ResponseEntity<>(Optional.ofNullable(response.body()).map(String::new).map(Object::toString).orElse(""), CollectionUtils.toMultiValueMap(response.headers()), HttpStatusCode.valueOf(response.status()));
    }
}
