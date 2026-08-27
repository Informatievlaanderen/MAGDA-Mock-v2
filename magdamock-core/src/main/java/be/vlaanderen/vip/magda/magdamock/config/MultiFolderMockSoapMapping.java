package be.vlaanderen.vip.magda.magdamock.config;

import lombok.Getter;

import java.util.List;

@Getter
public class MultiFolderMockSoapMapping extends MockSoapMapping {
    private List<String> subfolders;
    private List<Integer> subfolderNumberOfElements;

    public MultiFolderMockSoapMapping(String domain, String service, String version,
                                      List<String> keys, String separator, String xsdRequestPath, String xsdResponsePath,
                                      List<String> subfolders, List<Integer> subfolderNumberOfElements) {
        super(domain, service, version, keys, separator, xsdRequestPath, xsdResponsePath, StubHandler.MultiFolder, MissingParameterConfiguration.EmptyString);
        this.subfolders = subfolders;
        this.subfolderNumberOfElements = subfolderNumberOfElements;
    }
}