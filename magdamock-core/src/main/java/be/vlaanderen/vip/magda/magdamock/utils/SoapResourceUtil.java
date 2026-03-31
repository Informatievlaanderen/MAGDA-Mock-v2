package be.vlaanderen.vip.magda.magdamock.utils;

import be.vlaanderen.vip.magda.magdamock.client.soap.Domain;
import be.vlaanderen.vip.magda.magdamock.client.soap.Service;
import be.vlaanderen.vip.magda.magdamock.client.soap.Version;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public final class SoapResourceUtil {

    private SoapResourceUtil(){}

    public static List<Domain> loadDomainsFromPaths(List<String> paths) {
        Map<String, Map<String, Map<String, List<String>>>> structure = new TreeMap<>();

        for (String relativePath : paths) {
            String[] parts = relativePath.split("/");
            if (parts.length < 4) {
                continue;
            }

            String domain = parts[0];
            String service = parts[1];
            String version = parts[2];

            String fileName = String.join("/", Arrays.copyOfRange(parts, 3, parts.length));

            structure
                    .computeIfAbsent(domain, d -> new TreeMap<>())
                    .computeIfAbsent(service, s -> new TreeMap<>())
                    .computeIfAbsent(version, v -> new ArrayList<>())
                    .add(fileName);
        }
        return toDomainList(structure);
    }

    private static List<Domain> toDomainList(Map<String, Map<String, Map<String, List<String>>>> structure) {
        List<Domain> domains = new ArrayList<>();
        for (Map.Entry<String, Map<String, Map<String, List<String>>>> domainEntry : structure.entrySet()) {
            List<Service> services = new ArrayList<>();
            for (Map.Entry<String, Map<String, List<String>>> serviceEntry : domainEntry.getValue().entrySet()) {
                List<Version> versions = new ArrayList<>();
                for (Map.Entry<String, List<String>> versionEntry : serviceEntry.getValue().entrySet()) {
                    List<String> files = new ArrayList<>(versionEntry.getValue());
                    Collections.sort(files);
                    versions.add(new Version(versionEntry.getKey(), files));
                }
                services.add(new Service(serviceEntry.getKey(), versions));
            }
            domains.add(new Domain(domainEntry.getKey(), services));
        }
        return domains;
    }

    public static List<String> resolvePaths(String soapTestPath) throws IOException {

        if (StringUtils.isEmpty(soapTestPath)) {
            return List.of();
        }

        List<String> paths = new ArrayList<>();

        Path basePath = Paths.get(soapTestPath);

        if (!Files.exists(basePath)) {
            return paths;
        }

        try (var stream = Files.walk(basePath)) {
            stream
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".xml"))
                    .forEach(path -> {
                        Path relative = basePath.relativize(path);
                        paths.add(relative.toString().replace("\\", "/"));
                    });
        }
        return paths;
    }

    public static String readStubBody(String soapTestPath, String domain, String service, String version, String fileName) throws IOException {
        Path path = Path.of(soapTestPath, domain, service, version, fileName + ".xml");
        return Files.readString(path);
    }
}
