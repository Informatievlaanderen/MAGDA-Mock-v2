package be.vlaanderen.vip.magda.magdamock.client.rest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class RestDirectoryHandler {
    private static String FILENAME_ARG_SEPARATOR = "&";
    private static String FILENAME_DEFAULT = "default";
    private MockRestMapping mockRestMapping;
    private WireMockServer wireMockServer;
    private Path rootPath;
    private int defaultPriority;
    private int fallbackPriority;
    private ObjectMapper objectMapper;

    private record RestMappingDTO(String url, Map<String, String> queryParameters, Map<String, String> requestBodyParameters){}

    public RestDirectoryHandler(MockRestMapping mockRestMapping, WireMockServer wireMockServer, Path rootPath) {
        this.mockRestMapping = mockRestMapping;
        this.wireMockServer = wireMockServer;
        this.rootPath = rootPath;
        this.defaultPriority = 50;
        this.fallbackPriority = 100;
        objectMapper = new ObjectMapper();
    }

    public void addAllStubs() {
        Path path = rootPath;
        for (String pathPart : mockRestMapping.folderPath()) {
            path = path.resolve(pathPart);
        }
        log.info("Adding stubs for path: {}", path);

        File[] files = new File(path.toUri()).listFiles();
        if (files == null) {
            return;
        }
        for (File file : files) {
            try {
                String name = file.getName();
                name = FilenameUtils.removeExtension(name);
                log.info("Adding stub for file: {}", name);
                JsonNode jsonNode = objectMapper.readTree(file);
                String response = jsonNode.get("response").toString();
                if (name.startsWith(FILENAME_DEFAULT)) {
                    addDefaultMapping(response, name);
                } else {
                    addMapping(response, name);
                }
            } catch (Exception e) {
                log.error("Failed to add stubs for path: {}", file.getPath(), e);
            }
        }
    }

    private RestMappingDTO getUrlAndQueryParameters(String filename) {
        String urlPath = mockRestMapping.url();
        List<String> filenameSplitParts = new ArrayList<>(Arrays.stream(filename.split(FILENAME_ARG_SEPARATOR)).toList());
        Object[] urlParameters = new String[mockRestMapping.urlParametersSize()];
        for (int i = 0; i < mockRestMapping.urlParametersSize(); i++) {
            String s = filenameSplitParts.isEmpty() ? "" : filenameSplitParts.removeFirst();
            urlParameters[i] = s;
        }
        Map<String, String> queryParameters = new HashMap<>();
        for (int i = 0; i < mockRestMapping.queryParameters().size(); i++) {
            if (!filenameSplitParts.isEmpty()) {
                String value = URLDecoder.decode(filenameSplitParts.removeFirst(), StandardCharsets.UTF_8);
                if (!value.isBlank())
                    queryParameters.put(mockRestMapping.queryParameters().get(i), value);
            }
        }
        Map<String, String> bodyParameters = new HashMap<>();
        for (int i = 0; i < mockRestMapping.requestBodyParameters().size(); i++) {
            if (!filenameSplitParts.isEmpty()) {
                String value = filenameSplitParts.removeFirst();
                if (!value.isBlank())
                    bodyParameters.put(mockRestMapping.requestBodyParameters().get(i), value);
            }
        }
        urlPath = urlPath.formatted(urlParameters);
        return new RestMappingDTO(urlPath, queryParameters, bodyParameters);
    }

    private void addDefaultMapping(String responseContent, String filename) {
        filename = filename.replaceFirst("^" + FILENAME_DEFAULT, "");
        String urlPath = getUrlAndQueryParameters(filename).url();
        String method = mockRestMapping.method();
        boolean hasUrlPattern = urlPath.contains("%s") || mockRestMapping.urlParametersSize() > 0;
        String urlPattern = mockRestMapping.url().replaceAll("%s", "[^/]+");
        String urlField = hasUrlPattern
                ? String.format("\"urlPathPattern\": \"%s\"", urlPattern)
                : String.format("\"urlPath\": \"%s\"", urlPath);
        int priority = mockRestMapping.priority() != null
                ? mockRestMapping.priority() + (fallbackPriority - defaultPriority)
                : fallbackPriority;
        String wireMockStubbing = String.format("""
                {
                "priority": %s,
                "request":{
                    "method": "%s",
                    %s
                    },
                    "response": %s
                }
                """, priority, method, urlField, responseContent);
        log.debug(wireMockStubbing);
        StubMapping stubMapping = StubMapping.buildFrom(wireMockStubbing);
        wireMockServer.addStubMapping(stubMapping);
    }

    private void addMapping(String responseContent, String filename) {
        RestMappingDTO urlAndQueryParameters = getUrlAndQueryParameters(filename);
        String method = mockRestMapping.method();
        String urlPath = urlAndQueryParameters.url();
        Map<String, String> queryParameters = urlAndQueryParameters.queryParameters();
        String queryParametersString = "";
        if (!queryParameters.isEmpty()) {
            queryParametersString = queryParameters.entrySet().stream().map(entry -> String.format("""
                    "%s": {"equalTo": "%s"}
                    """, entry.getKey(), entry.getValue())).collect(Collectors.joining(","));
            queryParametersString = String.format(",\"queryParameters\": {%s}", queryParametersString);
        }
        String bodyPatterns = "";
        Map<String, String> bodyParameters = urlAndQueryParameters.requestBodyParameters();
        if (!bodyParameters.isEmpty()) {
            bodyPatterns = bodyParameters.entrySet().stream().map(entry -> String.format("{\"matchesJsonPath\": \"$[?(@.%s == '%s')]\"}", entry.getKey(), entry.getValue())).collect(Collectors.joining(","));
            bodyPatterns = String.format(",\"bodyPatterns\": [%s]", bodyPatterns);
        }
        int priority = mockRestMapping.priority() != null
                ? mockRestMapping.priority()- queryParameters.size() - bodyParameters.size()
                : defaultPriority - queryParameters.size() - bodyParameters.size();
        String wireMockStubbing = String.format("""
                {
                "priority": %s,
                "request":{
                    "method": "%s",
                    "urlPath": "%s"
                    %s
                    %s
                    },
                    "response": %s
                }
                """, priority, method, urlPath, queryParametersString, bodyPatterns, responseContent);
        log.debug(wireMockStubbing);
        StubMapping stubMapping = StubMapping.buildFrom(wireMockStubbing);
        wireMockServer.addStubMapping(stubMapping);
    }
}
