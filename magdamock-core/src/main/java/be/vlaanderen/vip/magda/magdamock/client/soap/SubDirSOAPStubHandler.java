package be.vlaanderen.vip.magda.magdamock.client.soap;

import be.vlaanderen.vip.magda.magdamock.utils.SoapResourceUtil;
import com.github.tomakehurst.wiremock.WireMockServer;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.matchingXPath;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

@Slf4j
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
        boolean isDefaultFile = fileName.contains("default");
        if (isDefaultFile) {
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

        if (keys.size() > values.size() && !isDefaultFile) {
            log.info("Unable to create SOAP wiremock stubbing for {} {} {} {}", domain, service, version, fileName);
            return;
        }

        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            if (!isDefaultFile) {
                String value = values.get(i);
                if (!value.isEmpty()) {
                    String xpathExpression;
                    if (key.endsWith("/name()")) {
                        String nodeKey = key.substring(0, key.length() - "/name()".length());
                        xpathExpression = nodeKey + "[local-name()='" + value + "']";
                    } else {
                        xpathExpression = key + "[normalize-space()='" + value + "']";
                    }
                    mappingBuilder = mappingBuilder.withRequestBody(matchingXPath(xpathExpression));
                }
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
                .map(part -> URLDecoder.decode(part, StandardCharsets.UTF_8))
                .toList();
    }

    private boolean isFileOnly(String fileName) {
        return !fileName.contains(separator);
    }
}
