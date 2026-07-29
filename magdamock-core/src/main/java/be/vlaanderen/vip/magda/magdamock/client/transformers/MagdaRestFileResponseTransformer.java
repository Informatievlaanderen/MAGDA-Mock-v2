package be.vlaanderen.vip.magda.magdamock.client.transformers;

import be.vlaanderen.vip.magda.magdamock.client.logging.LifecyclePhase;
import be.vlaanderen.vip.magda.magdamock.client.logging.RestLogHelper;
import be.vlaanderen.vip.magda.magdamock.config.MockRestMapping;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.extension.Parameters;
import com.github.tomakehurst.wiremock.extension.ResponseDefinitionTransformerV2;
import com.github.tomakehurst.wiremock.http.FormParameter;
import com.github.tomakehurst.wiremock.http.QueryParameter;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.http.ResponseDefinition;
import com.github.tomakehurst.wiremock.stubbing.ServeEvent;
import com.jayway.jsonpath.JsonPath;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Slf4j
@AllArgsConstructor
public class MagdaRestFileResponseTransformer implements ResponseDefinitionTransformerV2 {
    public static String NAME = "magda-rest-file-response-transformer";
    private final Path filesRoot;

    @Override
    public ResponseDefinition transform(ServeEvent serveEvent) {
        try {
            RestLogHelper.contextSetLifecyclePhase(LifecyclePhase.RESPONSE_MAPPING);
            Request request = serveEvent.getRequest();
            Parameters parameters = serveEvent.getTransformerParameters();

            String id = parameters.getString("mapping-id");

            List<MockRestMapping> mockRestMappingList = MockRestMapping.MAPPINGS.stream().filter(
                    mapping -> mapping.getId().equals(id)
            ).toList();

            log.debug("Fetching all parameters to find a mapping for {}", id);
            List<Path> fileNameOptions = new ArrayList<>();
            for (MockRestMapping mockRestMapping : mockRestMappingList) {
                List<String> fileParts = getUrlAndQueryParameters(request, mockRestMapping);

                fileNameOptions.addAll(determineFilenameOptions(fileParts, mockRestMapping));
            }

            boolean stop = false;
            int i = 0;
            Path responseFile = null;
            while (!stop && i < fileNameOptions.size()) {
                Path fileToCheck = fileNameOptions.get(i);
                if (Files.exists(fileToCheck)) {
                    responseFile = fileToCheck;
                    stop = true;
                }
                log.debug("Trying to if file {} exists", fileToCheck);
                i++;
            }

            if (responseFile == null) {
                log.debug("Didn't find any matching file");
                return new ResponseDefinitionBuilder().withStatus(404).build();
            }
            log.debug("Found best matching file: {}", responseFile.toFile().getAbsolutePath());

            JsonNode response = new ObjectMapper().readTree(responseFile.toFile()).get("response");
            ResponseDefinitionBuilder responseDefinitionBuilder = new ResponseDefinitionBuilder();

            int status = response.get("status").asInt();
            responseDefinitionBuilder = responseDefinitionBuilder.withStatus(status);

            if (response.has("body")) {
                byte[] body = response.get("body").binaryValue();
                responseDefinitionBuilder = responseDefinitionBuilder.withBody(body);
            } else if (response.has("jsonBody")) {
                JsonNode body = response.get("jsonBody");
                responseDefinitionBuilder = responseDefinitionBuilder.withJsonBody(body);
            }
            if (response.has("headers")) {
                JsonNode headers = response.get("headers");
                for (Iterator<String> it = headers.fieldNames(); it.hasNext(); ) {
                    String header = it.next();
                    responseDefinitionBuilder.withHeader(header, headers.get(header).asText());
                }
            }
            return responseDefinitionBuilder
                    .build();
        } catch (Exception e) {
            log.error("Exception occurred while trying to construct a REST response", e);
            return new ResponseDefinitionBuilder()
                    .withStatus(666)
                    .withHeader("Content-Type", "text/plain; charset=utf-8")
                    .build();
        }
    }

    private List<String> getUrlAndQueryParameters(Request request, MockRestMapping mockRestMapping) {
        List<String> fileParts = new ArrayList<>();

        // url params
        for (String key : mockRestMapping.urlParameters()) {
            fileParts.add(request.getPathParameters().get(key));
        }

        // query params
        for (String key : mockRestMapping.queryParameters()) {
            QueryParameter queryParameter = request.queryParameter(key);
            if (queryParameter.isPresent()) {
                fileParts.add(queryParameter.firstValue());
            } else {
                fileParts.add("");
            }
        }

        // body params
        for (String key : mockRestMapping.requestBodyParameters()) {
            FormParameter formParameter = request.formParameter(key);
            if (formParameter.isPresent()) {
                fileParts.add(formParameter.firstValue());
            } else {
                try {
                    JsonNode j = new ObjectMapper().readTree(request.getBody());
                    if (!key.startsWith("/")) {
                        key = "/" + key;
                    }
                    fileParts.add(j.at(key).asText());
                } catch (Exception e) {
                    log.error("Error occured while extracting body parameter", e);
                    fileParts.add("");
                }
            }
        }

        return fileParts.stream()
                .map(s ->
                        URLEncoder.encode(s, StandardCharsets.UTF_8)
                                .replace("+", "%20")
                )
                .toList();
    }

    private List<Path> determineFilenameOptions(List<String> fileParts, MockRestMapping mockRestMapping) {
        List<String> fileNames = new ArrayList<>();
        if (!mockRestMapping.defaultOnly()) {
            int i = 1 << fileParts.size();
            while (i-- > 0) {
                List<String> filenameParts = new ArrayList<>();
                for (int j = 0; j < fileParts.size(); j++) {
                    int index = fileParts.size() - j - 1;
                    boolean isSet = (i & (1 << index)) != 0;
                    if (isSet) {
                        filenameParts.add(fileParts.get(j));
                    } else {
                        filenameParts.add("");
                    }
                }
                fileNames.add(String.join("&", filenameParts));

                while (!filenameParts.isEmpty() && filenameParts.getLast().isBlank()) {
                    filenameParts.removeLast();
                    fileNames.add(String.join("&", filenameParts));
                }
            }
            fileNames.removeIf(String::isBlank);
        }
        fileNames.add("default");

        return fileNames.stream().map(
                fileName -> filesRoot
                        .resolve(mockRestMapping.toPath())
                        .resolve(fileName + ".json")
        ).toList();
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public boolean applyGlobally() {
        return false;
    }
}