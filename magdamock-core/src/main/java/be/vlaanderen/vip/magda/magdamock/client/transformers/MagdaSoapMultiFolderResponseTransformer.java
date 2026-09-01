package be.vlaanderen.vip.magda.magdamock.client.transformers;

import be.vlaanderen.vip.magda.magdamock.client.logging.LifecyclePhase;
import be.vlaanderen.vip.magda.magdamock.client.logging.SoapLogHelper;
import be.vlaanderen.vip.magda.magdamock.config.MappingLists;
import be.vlaanderen.vip.magda.magdamock.config.MultiFolderMockSoapMapping;
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
public class MagdaSoapMultiFolderResponseTransformer implements ResponseDefinitionTransformerV2 {
    public static String NAME = "magda-soap-multi-folder-response-transformer";
    private final Path filesRoot;

    @Override
    public ResponseDefinition transform(ServeEvent serveEvent) {
        try {
            SoapLogHelper.contextSetLifecyclePhase(LifecyclePhase.RESPONSE_ROUTING);
            Request request = serveEvent.getRequest();
            Parameters parameters = serveEvent.getTransformerParameters();

            String id = parameters.getString("mapping-id");

            MultiFolderMockSoapMapping mockSoapMapping = (MultiFolderMockSoapMapping) MappingLists.SOAP_MAPPINGS.stream().filter(mapping -> mapping.getId().equals(id)).findFirst().get();
            List<String> keys = mockSoapMapping.getKeys();
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
            List<List<String>> xpathValuesPerSubMapping = new ArrayList<>();
            int index = 0;
            for (Integer amount : mockSoapMapping.getSubfolderNumberOfElements()) {
                xpathValuesPerSubMapping.add(xpathValues.subList(index, index+amount));
                index += amount;
            }

            List<Path> fileOptions = new ArrayList<>();
            List<Path> defaultOptions = new ArrayList<>();
            Path mappingPath = mockSoapMapping.getPath();
            index = 0;
            for (String subfolder : mockSoapMapping.getSubfolders()) {
                Path subfolderPath = mappingPath.resolve(subfolder);
                List<String> valuesXpath = xpathValuesPerSubMapping.get(index++);
                if (valuesXpath.stream().anyMatch(value -> !value.isEmpty())) {
                    fileOptions.addAll(determineFilenameOptionsForFlatfile(valuesXpath, subfolderPath));
                    defaultOptions.add(subfolderPath);
                }
            }

            defaultOptions.add(mappingPath);
            fileOptions.addAll(defaultOptions.stream().map(f -> filesRoot.resolve(f).resolve("default.xml")).toList());

            boolean stop = false;
            int i = 0;
            Path responseFile = null;
            while (!stop && i < fileOptions.size()) {
                Path fileToCheck = fileOptions.get(i);
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
                    .withHeader("X-MagdaMock-Content-Location", responseFile.toFile().getAbsolutePath())
                    .withBody(Files.readString(responseFile))
                    .build();
        } catch (IOException e) {
            return new ResponseDefinitionBuilder()
                    .withStatus(404)
                    .withHeader("Content-Type", "text/plain; charset=utf-8")
                    .build();
        }
    }

    private List<Path> determineFilenameOptionsForFlatfile(List<String> xpathValues, Path subPath) {
        List<String> fileNames = new ArrayList<>();
        List<String> filenameParts = new ArrayList<>(xpathValues);
        fileNames.add(String.join("&", filenameParts));
        while (!filenameParts.isEmpty() && filenameParts.getLast().isBlank()) {
            filenameParts.removeLast();
            fileNames.add(String.join("&", filenameParts));
        }
        fileNames.removeIf(String::isBlank);
        return fileNames.stream().map(
                fileName -> filesRoot
                        .resolve(subPath)
                        .resolve(fileName + ".xml")
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