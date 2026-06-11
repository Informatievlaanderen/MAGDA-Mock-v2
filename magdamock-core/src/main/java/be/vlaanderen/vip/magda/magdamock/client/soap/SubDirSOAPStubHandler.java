package be.vlaanderen.vip.magda.magdamock.client.soap;

import be.vlaanderen.vip.magda.magdamock.utils.SoapResourceUtil;
import com.github.tomakehurst.wiremock.WireMockServer;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.matchingXPath;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

public class SubDirSOAPStubHandler extends AbstractSoapStubHandler {

    private final List<String> keys;
    private final String separator;

    public SubDirSOAPStubHandler(WireMockServer wireMockServer, String soapTestPath, List<String> keys) {
        this(wireMockServer, soapTestPath, keys, "/");
    }

    public SubDirSOAPStubHandler(WireMockServer wireMockServer, String soapTestPath, List<String> keys, String separator) {
        super(wireMockServer, soapTestPath);
        this.keys = keys;
        this.separator = separator;
    }

    @Override
    public void register(String domain, String service, String version, String fileName) throws IOException {

        int priority = 10;
        if (fileName.contains("notfound") || fileName.contains("success")) {
            priority = 20;
        }

        var mappingBuilder = post(urlEqualTo("/soap"))
                .atPriority(priority)
                .withRequestBody(matchingXPath(
                        "//*[local-name()='Naam' and normalize-space()='" + service + "']"
                ))
                .withRequestBody(matchingXPath(
                        "//*[local-name()='Versie' and normalize-space()='" + version + "']"
                ));


        List<String> values;

        if (isFileOnly(fileName)) {
            values = List.of(fileName.replace(".xml", ""));
        } else {
            values = getValues(fileName);
        }

        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = values.get(i);
            if (!value.contains("notfound") && !value.contains("success")) {
                mappingBuilder = mappingBuilder.withRequestBody(
                        matchingXPath(key + "[normalize-space()='" + value + "']")
                );
            } else {
                break;
            }
        }

        wireMockServer.stubFor(
                mappingBuilder.willReturn(
                        aResponse()
                                .withStatus(200)
                                .withHeader("Content-Type", "text/xml; charset=utf-8")
                                .withBody(
                                        SoapResourceUtil.readStubBody(
                                                soapTestPath,
                                                domain,
                                                service,
                                                version,
                                                fileName.replace(".xml", "")
                                        )
                                )
                )
        );
    }

    private List<String> getValues(String fileName) {
        String[] parts = fileName.replaceFirst("\\.xml$", "").split(separator);

        return Arrays.stream(parts)
                .toList();
    }

    private boolean isFileOnly(String fileName) {
        return !fileName.contains(separator);
    }
}
