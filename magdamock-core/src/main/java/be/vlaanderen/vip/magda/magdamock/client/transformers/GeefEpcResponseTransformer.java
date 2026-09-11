package be.vlaanderen.vip.magda.magdamock.client.transformers;

import be.vlaanderen.vip.magda.magdamock.client.logging.LifecyclePhase;
import be.vlaanderen.vip.magda.magdamock.client.logging.SoapLogHelper;
import be.vlaanderen.vip.magda.magdamock.config.MappingLists;
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
import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
public class GeefEpcResponseTransformer implements ResponseDefinitionTransformerV2 {
    public static String NAME = "geefepc-response-transformer";
    private final Path filesRoot;

    @Override
    public ResponseDefinition transform(ServeEvent serveEvent) {
        try {
            SoapLogHelper.contextSetLifecyclePhase(LifecyclePhase.RESPONSE_ROUTING);
            Request request = serveEvent.getRequest();
            Parameters parameters = serveEvent.getTransformerParameters();

            String id = parameters.getString("mapping-id");

            MockSoapMapping mockSoapMapping = MappingLists.SOAP_MAPPINGS.stream().filter(mapping -> mapping.getId().equals(id)).findFirst().get();
            List<String> keys = mockSoapMapping.getKeys();
            MagdaMockDocument requestBody = MagdaMockDocument.fromString(request.getBodyAsString());
            log.debug("Fetching all parameters to find a mapping for {}", mockSoapMapping.getId());
            Map<String, String> xpathValues = keys.stream()
                    .map(k -> Pair.of(k, requestBody.getValue(k)))
                    .map(kv -> {
                        if (kv.getValue() == null) return Pair.of(kv.getKey(), "");
                        else return kv;
                    })
                    .map(kv ->
                            Pair.of(kv.getKey(), URLEncoder.encode(kv.getValue(), StandardCharsets.UTF_8)
                                    .replace("+", "%20"))
                    ).collect(Collectors.toMap(Pair::getKey, Pair::getValue));

            List<Path> fileOptions = new ArrayList<>();
            List<Path> defaultOptions = new ArrayList<>();
            Path mappingPath = mockSoapMapping.getPath();
            Path pathGebouwId = mappingPath.resolve("GebouwId");
            String gebouwId = xpathValues.getOrDefault(MappingLists.KEY_GEBOUW_ID, "");
            if (!gebouwId.isEmpty()) {
                fileOptions.addAll(determineFilenameOptionsForFlatfile(List.of(gebouwId), pathGebouwId));
                defaultOptions.add(pathGebouwId);
            }

            String gebouwEenheidId = xpathValues.getOrDefault(MappingLists.KEY_GEBOUWEENHEID_ID, "");
            if (!gebouwEenheidId.isEmpty()) {
                Path pathGebouweenheidId = mappingPath.resolve("GebouweenheidId");
                fileOptions.addAll(determineFilenameOptionsForFlatfile(List.of(gebouwEenheidId), pathGebouweenheidId));
                defaultOptions.add(pathGebouweenheidId);
            }

            String gemeenteNaam = xpathValues.getOrDefault(MappingLists.KEY_ADRES_GEMEENTE, "");
            if (!gemeenteNaam.isEmpty()) {
                Path pathAdres = mappingPath.resolve("Adres");
                Path pathAdresGemeenete = pathAdres.resolve(gemeenteNaam);
                fileOptions.addAll(determineFilenameOptionsForFlatfile(List.of(xpathValues.get(MappingLists.KEY_ADRES_STRAAT),xpathValues.get(MappingLists.KEY_ADRES_HUISNUMMER), xpathValues.get(MappingLists.KEY_ADRES_BUSNUMMER)), pathAdresGemeenete));
                defaultOptions.add(pathAdresGemeenete);
                defaultOptions.add(pathAdres);
            }

            String attestnummer = xpathValues.getOrDefault(MappingLists.KEY_ATTESTNUMMER, "");
            if (!attestnummer.isEmpty()) {
                Path pathAttestnummer = mappingPath.resolve("Attestnummer");
                fileOptions.addAll(determineFilenameOptionsForFlatfile(List.of(attestnummer), pathAttestnummer));
                defaultOptions.add(pathAttestnummer);
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