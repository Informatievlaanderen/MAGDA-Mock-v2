package be.vlaanderen.vip.magda.magdamock.config;

import lombok.Getter;

import java.nio.file.Path;
import java.util.List;

@Getter
public class MockSoapMapping {
    public static final String SEPERATOR_FILE_NAME = "&";
    private final String domain;
    private final String service;
    private final String version;
    private final List<String> keys;
    private final String separator;
    private final String xsdRequestPath;
    private final String xsdResponsePath;
    private final StubHandler stubHandler;
    private final MissingParameterConfiguration missingParameterConfiguration;

    public MockSoapMapping(String domain, String service, String version,
                           List<String> keys, String separator, String xsdRequestPath, String xsdResponsePath,
                           StubHandler stubHandler, MissingParameterConfiguration missingParameterConfiguration) {
        this.domain = domain;
        this.service = service;
        this.version = version;
        this.keys = keys;
        this.separator = separator;
        this.xsdRequestPath = xsdRequestPath;
        this.xsdResponsePath = xsdResponsePath;
        this.stubHandler = stubHandler;
        this.missingParameterConfiguration = missingParameterConfiguration;
    }

    public MockSoapMapping(String domain, String service, String version,
                           List<String> keys, String separator, String xsdRequestPath, String xsdResponsePath) {
        this(domain, service, version, keys, separator, xsdRequestPath, xsdResponsePath, switch (separator) {
            case SEPERATOR_FILE_NAME -> StubHandler.FileSoap;
            default -> StubHandler.SubDirSoap;
        }, MissingParameterConfiguration.EmptyString);
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