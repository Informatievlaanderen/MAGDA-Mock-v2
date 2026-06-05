package be.vlaanderen.vip.magda.magdamock.client.rest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class RestDirectoryHandler {
    private static String FILENAME_ARG_SEPARATOR = "_";
    private static String FILENAME_DEFAULT = "default";
    private MockRestMapping mockRestMapping;
    private WireMockServer wireMockServer;
    private Path rootPath;
    private int defaultPriority;
    private int fallbackPriority;
    private ObjectMapper objectMapper;

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

    private Pair<String, Map<String, String>> getUrlAndQueryParameters(String filename) {
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
                queryParameters.put(mockRestMapping.queryParameters().get(i), filenameSplitParts.removeFirst());
            }
        }
        urlPath = urlPath.formatted(urlParameters);
        return Pair.of(urlPath, queryParameters);
    }

    private void addDefaultMapping(String responseContent, String filename) {
        filename = filename.replaceFirst("^" + FILENAME_DEFAULT, "");
        String urlPath = getUrlAndQueryParameters(filename).getLeft();
        String method = mockRestMapping.method();
        String wireMockStubbing = String.format("""
                {
                "priority": %s,
                "request":{
                    "method": "%s",
                    "urlPath": "%s"
                    },
                    "response": %s
                }
                }
                """, fallbackPriority, method, urlPath, responseContent);
        log.debug(wireMockStubbing);
        StubMapping stubMapping = StubMapping.buildFrom(wireMockStubbing);
        wireMockServer.addStubMapping(stubMapping);
    }

    private void addMapping(String responseContent, String filename) {
        Pair<String, Map<String, String>> urlAndQueryParameters = getUrlAndQueryParameters(filename);
        String method = mockRestMapping.method();
        String urlPath = getUrlAndQueryParameters(filename).getLeft();
        Map<String, String> queryParameters = urlAndQueryParameters.getRight();
        String queryParametersString = "";
        if (!queryParameters.isEmpty()) {
            queryParametersString = queryParameters.entrySet().stream().map(entry -> String.format("""
                    "%s": {"equalTo": "%s"}
                    """, entry.getKey(), entry.getValue())).collect(Collectors.joining(","));
            queryParametersString = String.format(",\"queryParameters\": {%s}", queryParametersString);
        }
        String wireMockStubbing = String.format("""
                {
                "priority": %s,
                "request":{
                    "method": "%s",
                    "urlPath": "%s"
                    %s
                    },
                    "response": %s
                }
                }
                """, defaultPriority-queryParameters.size(), method, urlPath, queryParametersString, responseContent);
        log.debug(wireMockStubbing);
        StubMapping stubMapping = StubMapping.buildFrom(wireMockStubbing);
        wireMockServer.addStubMapping(stubMapping);
    }
}
