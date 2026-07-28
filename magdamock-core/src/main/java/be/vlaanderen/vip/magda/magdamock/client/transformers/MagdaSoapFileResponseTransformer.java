package be.vlaanderen.vip.magda.magdamock.client.transformers;

import be.vlaanderen.vip.magda.magdamock.client.logging.LifecyclePhase;
import be.vlaanderen.vip.magda.magdamock.client.logging.SoapLogHelper;
import be.vlaanderen.vip.magda.magdamock.config.MockSoapMapping;
import be.vlaanderen.vip.magda.magdamock.utils.MagdaMockDocument;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.extension.Parameters;
import com.github.tomakehurst.wiremock.extension.ResponseDefinitionTransformerV2;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.http.ResponseDefinition;
import com.github.tomakehurst.wiremock.stubbing.ServeEvent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@AllArgsConstructor
public class MagdaSoapFileResponseTransformer implements ResponseDefinitionTransformerV2 {
    public static String NAME = "magda-soap-response-transformer";
    private final Path filesRoot;

    @Override
    public ResponseDefinition transform(ServeEvent serveEvent) {
        try {
            SoapLogHelper.contextSetLifecyclePhase(LifecyclePhase.RESPONSE_MAPPING);
            Request request = serveEvent.getRequest();
            Parameters parameters = serveEvent.getTransformerParameters();

            String domain = parameters.getString("domain");
            String service = parameters.getString("service");
            String version = parameters.getString("version");

            MockSoapMapping mockSoapMapping = MockSoapMapping.MAPPINGS.stream().filter(mapping -> mapping.service().equals(service) && mapping.version().equals(version)).findFirst().get();
            List<String> keys = mockSoapMapping.keys();
            MagdaMockDocument requestBody = MagdaMockDocument.fromString(request.getBodyAsString());
            log.debug("Fetching all parameters to find a mapping for {}", mockSoapMapping.getId());
            List<String> xpathValues = keys.stream()
                    .map(requestBody::getValue)
                    .map(s -> {
                        if (s == null) return "";
                        else return s;
                    })
                    .map(s ->
                            URLEncoder.encode(s, StandardCharsets.UTF_8)
                                    .replace("+", "%20")
                    )
                    .toList();

            //
            List<String> fileNames;
            if (mockSoapMapping.stubHandler().equals(MockSoapMapping.StubHandler.FileSoap)) {
                fileNames = determineFilenameOptionsForFlatfile(xpathValues);
            } else {
                fileNames = determineFilenameOptionsForSubdir(xpathValues);
            }

            boolean stop = false;
            int i = 0;
            Path responseFile = null;
            while (!stop && i < fileNames.size()) {
                String fileNameToCheck = fileNames.get(i) + ".xml";
                Path fileToCheck = filesRoot
                        .resolve(domain)
                        .resolve(service)
                        .resolve(version)
                        .resolve(fileNameToCheck);
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

            return new ResponseDefinitionBuilder()
                    .withStatus(200)
                    .withHeader("Content-Type", "text/xml; charset=utf-8")
                    .withBody(Files.readString(responseFile))
                    .build();
        } catch (IOException e) {
            return new ResponseDefinitionBuilder()
                    .withStatus(404)
                    .withHeader("Content-Type", "text/plain; charset=utf-8")
                    .build();
        }
    }

    private List<String> determineFilenameOptionsForFlatfile(List<String> xpathValues) {
        List<String> fileNames = new ArrayList<>();
        int i = 1 << xpathValues.size();
        while (i-- > 0) {
            List<String> filenameParts = new ArrayList<>();
            for (int j = 0; j < xpathValues.size(); j++) {
                int index = xpathValues.size() - j - 1;
                boolean isSet = (i & (1 << index)) != 0;
                if (isSet) {
                    filenameParts.add(xpathValues.get(j));
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
        fileNames.add("default");
        return fileNames;
    }


    private List<String> determineFilenameOptionsForSubdir(List<String> xpathValues) {
        List<String> fileNames = new ArrayList<>();
        xpathValues = new ArrayList<>(xpathValues);
        // option 1: full path 'exists' (has all parts not '')
        // option 2-x: partial path, + default.xml
        boolean partRemoved = false;
        while (xpathValues.contains("")) {
            xpathValues.removeLast();
            partRemoved = true;
        }
        if (!partRemoved) {
            fileNames.add(String.join("/", xpathValues));
            xpathValues.removeLast();
        }
        while (!xpathValues.isEmpty()) {
            String path = String.join("/", xpathValues);
            path += "/default";
            fileNames.add(path);
            xpathValues.removeLast();
        }
        fileNames.add("default");

        return fileNames;
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