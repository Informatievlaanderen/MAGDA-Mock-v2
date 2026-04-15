package be.vlaanderen.vip.magda.magdamock.client.soap;

import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.extension.Parameters;
import com.github.tomakehurst.wiremock.extension.ResponseDefinitionTransformerV2;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.http.ResponseDefinition;
import com.github.tomakehurst.wiremock.stubbing.ServeEvent;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Stream;

public class GenderFileTransformer implements ResponseDefinitionTransformerV2 {

    private final Path filesRoot;
    private final Random random;

    private final ConcurrentMap<String, List<Path>> cache = new ConcurrentHashMap<>();
    static final String NAME = "gender-file-transformer";

    public GenderFileTransformer(Path filesRoot) {
        this(filesRoot, new Random());
    }

    public GenderFileTransformer(Path filesRoot, Random random) {
        this.filesRoot = filesRoot;
        this.random = random;
    }

    @Override
    public void start() {
        try (Stream<Path> stream = Files.walk(filesRoot)) {
            stream.filter(Files::isDirectory)
                    .filter(path -> {
                        String name = path.getFileName().toString();
                        return "mannen".equals(name) || "vrouwen".equals(name);
                    })
                    .forEach(dir -> cache.put(dir.toString(), loadXmlFiles(dir)));
        } catch (IOException e) {
            throw new RuntimeException("Failed to preload XML directories under " + filesRoot, e);
        }
    }

    @Override
    public void stop() {
        cache.clear();
    }

    @Override
    public ResponseDefinition transform(ServeEvent serveEvent) {
        try {
            Request request = serveEvent.getRequest();
            Parameters parameters = serveEvent.getTransformerParameters();

            String domain = parameters.getString("domain");
            String service = parameters.getString("service");
            String version = parameters.getString("version");

            String insz = extractInsz(request.getBodyAsString());

            Path inszFile = filesRoot
                    .resolve(domain)
                    .resolve(service)
                    .resolve(version)
                    .resolve(insz + ".xml");

            String body;

            if (Files.exists(inszFile)) {
                body = Files.readString(inszFile, StandardCharsets.UTF_8);
            } else {
                String genderDir = isMale(insz) ? "mannen" : "vrouwen";

                Path dir = filesRoot
                        .resolve(domain)
                        .resolve(service)
                        .resolve(version)
                        .resolve(genderDir);

                List<Path> candidates = cache.computeIfAbsent(dir.toString(), key -> loadXmlFiles(dir));

                if (candidates.isEmpty()) {
                    throw new IllegalStateException("No XML files found in directory: " + dir);
                }

                Path selected = candidates.get(random.nextInt(candidates.size()));
                body = Files.readString(selected, StandardCharsets.UTF_8);

            }

            return new ResponseDefinitionBuilder()
                    .withStatus(200)
                    .withHeader("Content-Type", "text/xml; charset=utf-8")
                    .withBody(body)
                    .build();

        } catch (Exception e) {
            return new ResponseDefinitionBuilder()
                    .withStatus(500)
                    .withHeader("Content-Type", "text/plain; charset=utf-8")
                    .withBody("Fallback generation failed: " + e.getMessage())
                    .build();
        }
    }

    private List<Path> loadXmlFiles(Path dir) {
        if (!Files.isDirectory(dir)) {
            return List.of();
        }

        try (Stream<Path> stream = Files.list(dir)) {
            List<Path> result = stream
                    .filter(Files::isRegularFile)
                    .filter(path -> path.getFileName().toString().toLowerCase().endsWith(".xml"))
                    .sorted()
                    .toList();

            return new ArrayList<>(result);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read directory " + dir, e);
        }
    }

    private String extractInsz(String xml) throws ParserConfigurationException, XPathExpressionException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(false);

        Document document = factory.newDocumentBuilder()
                .parse(new InputSource(new StringReader(xml)));

        XPath xpath = XPathFactory.newInstance().newXPath();

        String insz = (String) xpath.evaluate(
                "//*[local-name()='INSZ']/text()",
                document,
                XPathConstants.STRING
        );

        if (insz == null || insz.isBlank()) {
            throw new IllegalArgumentException("INSZ missing");
        }

        insz = insz.replaceAll("[\\D]", "").trim();

        if (insz.length() != 11) {
            throw new IllegalArgumentException("INSZ must contain 11 digits");
        }

        return insz;
    }

    private boolean isMale(String insz) {
        return Optional.of(insz)
                .filter(StringUtils::isNumeric)
                .map(i -> Integer.parseInt(i.substring(6, 9)) % 2 == 1)
                .orElse(false);
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
