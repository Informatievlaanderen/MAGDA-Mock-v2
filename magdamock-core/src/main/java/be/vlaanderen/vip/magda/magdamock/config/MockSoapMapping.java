package be.vlaanderen.vip.magda.magdamock.config;

import lombok.Getter;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Getter
public class MockSoapMapping {
    public static final String SEPERATOR_FILE_NAME = "&";
    private final String domain;
    private final String service;
    private final String version;
    private final List<String> folderXpathKeys;
    private final List<String> fileXpathKeys;
    private final String xsdRequestPath;
    private final String xsdResponsePath;
    private final StubHandler stubHandler;
    private final MissingParameterConfiguration missingParameterConfiguration;

    public MockSoapMapping(String domain, String service, String version,
                           List<String> folderXpathKeys, List<String> fileXpathKeys, String xsdRequestPath, String xsdResponsePath,
                           StubHandler stubHandler, MissingParameterConfiguration missingParameterConfiguration) {
        this.domain = domain;
        this.service = service;
        this.version = version;
        this.folderXpathKeys = folderXpathKeys;
        this.fileXpathKeys = fileXpathKeys;
        this.xsdRequestPath = xsdRequestPath;
        this.xsdResponsePath = xsdResponsePath;
        this.stubHandler = stubHandler;
        this.missingParameterConfiguration = missingParameterConfiguration;
    }

    public static MockSoapMapping create(String domain, String service, String version,
                           List<String> keys, String separator, String xsdRequestPath, String xsdResponsePath) {
        List<String> folderXpathKeys = new ArrayList<>();
        List<String> fileXpathKeys = new ArrayList<>();
        if (SEPERATOR_FILE_NAME.equals(separator)) {
            fileXpathKeys = keys;
        } else {
            folderXpathKeys = keys;
        }
        return new MockSoapMapping(domain, service, version, folderXpathKeys, fileXpathKeys, xsdRequestPath, xsdResponsePath, StubHandler.FileSoap, MissingParameterConfiguration.EmptyString);
    }

    public String getId() {
        return String.format("%s.%s.%s", domain, service, version);
    }

    public Path getPath() {
        return Path.of(domain, service, version);
    }

    public enum StubHandler {
        FileSoap,
        SubDirSoap,
        GeefEpc,
        MultiFolder
    }
}