package be.vlaanderen.vip.magda.magdamock.spring.controller;


import be.vlaanderen.vip.magda.magdamock.utils.MagdaDocument;
import be.vlaanderen.vip.magda.exception.MagdaConnectionException;
import be.vlaanderen.vip.magda.magdamock.client.MagdaMockConnection;
import be.vlaanderen.vip.magda.magdamock.exceptions.MagdaMockSoapException;
import be.vlaanderen.vip.magda.magdamock.client.handlers.MagdaMockRestHandler;
import be.vlaanderen.vip.magda.magdamock.client.handlers.MagdaMockSoapHandler;
import be.vlaanderen.vip.magda.magdamock.client.logging.SoapLogHelper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;
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
import java.util.UUID;
import java.util.regex.Pattern;

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
    private final String correlationIdHeaderName = "x-correlation-id";


    public MagdaMockController(MagdaMockConnection mockConnection) {
        this.mockConnection = mockConnection;
    }

    @PostMapping(value = {SOAP_BASE_URL}, produces = {TEXT_XML_VALUE}, consumes = {APPLICATION_XML_VALUE, TEXT_XML_VALUE})
    public ResponseEntity<String> magdaSoap0200WebService(@RequestBody String request, HttpServletRequest incomingRequest) throws MagdaConnectionException {
        MDC.clear();
        SoapLogHelper.contextSetLifecyclePhase(SoapLogHelper.LifecyclePhase.NOT_SPECIFIED);
        Map<String, String> headers = new HashMap<>();
        for (Iterator<String> it = incomingRequest.getHeaderNames().asIterator(); it.hasNext(); ) {
            String headerName = it.next();
            headers.put(headerName.toLowerCase(), incomingRequest.getHeader(headerName));
        }
        String correlationId = headers.getOrDefault(correlationIdHeaderName, UUID.randomUUID().toString());
        SoapLogHelper.contextSetCorrelationId(correlationId);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(correlationIdHeaderName, correlationId);

        var response = processMagdaMockRequest(request, httpHeaders);
        MDC.clear();
        return response;
    }

    private ResponseEntity<String> processMagdaMockRequest(String request, HttpHeaders httpHeaders) throws MagdaConnectionException {
        //TODO: handle request parsing errors and return Magda Uitzondering error
        try {
            MagdaDocument requestDocument = parseDocument(request);
            var magdaResponse = mockConnection.sendSoapRequest(new MagdaMockSoapHandler.MockSoapRequest(requestDocument.getXml()));
            if (magdaResponse != null) {
                return parseInputstream(MagdaDocument.fromDocument(magdaResponse.document()), httpHeaders);

            } else {
                return ResponseEntity.notFound().headers(httpHeaders).build();
            }
        } catch (MagdaMockSoapException e) {
            return ResponseEntity.internalServerError().contentType(TEXT_XML).headers(httpHeaders).body(e.getDocument().toString());
        }
    }

    private MagdaDocument parseDocument(String request) throws MagdaMockSoapException {
        try {
            return MagdaDocument.fromString(request);
        } catch (Exception e) {
            throw new MagdaMockSoapException(String.format("Unable to parse SOAP request. Reason: %s", e.getCause().toString()), "Server", e);
        }
    }

    private ResponseEntity<String> parseInputstream(MagdaDocument magdaDocument, HttpHeaders httpHeaders) {
        if (magdaDocument != null) {
            return ResponseEntity.ok().contentType(TEXT_XML).headers(httpHeaders).body(magdaDocument.toString());
        } else {
            log.error("Could not find XML");

            // TODO: maak en return MAGDA Uitzondering antwoord
            return ResponseEntity.notFound().headers(httpHeaders).build();
        }
    }


    @RequestMapping(
            value = {REST_BASE_URL + "/**", "api/" + REST_BASE_URL + "/**"},
            method = {RequestMethod.DELETE, RequestMethod.GET, RequestMethod.PATCH, RequestMethod.POST, RequestMethod.PUT}
    )
    protected ResponseEntity<String> magdaRestEndpoint(@RequestBody(required = false) String requestBody, HttpServletRequest incomingRequest) throws MagdaConnectionException {
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
