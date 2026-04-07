package be.vlaanderen.vip.magda.magdamock.spring.controller;


import be.vlaanderen.vip.magda.client.MagdaDocument;
import be.vlaanderen.vip.magda.client.connection.MagdaConnection;
import be.vlaanderen.vip.magda.exception.MagdaConnectionException;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
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

    private final MagdaConnection mockConnection;

    public MagdaMockController(MagdaConnection mockConnection) {
        this.mockConnection = mockConnection;
    }

    @PostMapping(value = {MAGDA_SOAP_02_00, "api/" + MAGDA_SOAP_02_00}, produces = {TEXT_XML_VALUE}, consumes = {APPLICATION_XML_VALUE, TEXT_XML_VALUE})
    public ResponseEntity<String> magdaSoap0200WebService(@RequestBody String request) throws MagdaConnectionException {
        return processMagdaMockRequest(request);
    }

    private ResponseEntity<String> processMagdaMockRequest(String request) throws MagdaConnectionException {
        //TODO: handle request parsing errors and return Magda Uitzondering error
        var requestDocument = MagdaDocument.fromString(request);

        var magdaResponse = mockConnection.sendDocument(requestDocument.getXml());
        if (magdaResponse != null) {
            return parseInputstream(MagdaDocument.fromDocument(magdaResponse));

        } else {
            return ResponseEntity.notFound().build();
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
        Pair<JsonNode, Integer> response = mockConnection.sendRestRequest(path, query, method, requestBody);
        return new ResponseEntity<>(Optional.ofNullable(response.getLeft()).map(Object::toString).orElse(""), HttpStatusCode.valueOf(response.getRight()));
    }
}
