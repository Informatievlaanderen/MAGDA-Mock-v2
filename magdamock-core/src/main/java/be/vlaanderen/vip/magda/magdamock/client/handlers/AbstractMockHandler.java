package be.vlaanderen.vip.magda.magdamock.client.handlers;

import be.vlaanderen.vip.magda.magdamock.config.WireMockData;
import be.vlaanderen.vip.magda.magdamock.utils.TimeoutUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.common.Urls;
import com.github.tomakehurst.wiremock.direct.DirectCallHttpServer;
import com.github.tomakehurst.wiremock.http.ContentTypeHeader;
import com.github.tomakehurst.wiremock.http.Cookie;
import com.github.tomakehurst.wiremock.http.FormParameter;
import com.github.tomakehurst.wiremock.http.HttpHeader;
import com.github.tomakehurst.wiremock.http.HttpHeaders;
import com.github.tomakehurst.wiremock.http.QueryParameter;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.http.RequestMethod;
import com.github.tomakehurst.wiremock.http.Response;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class AbstractMockHandler {
    protected final TimeoutUtil timeoutUtil;
    protected final WireMockServer wireMockServer;
    protected final DirectCallHttpServer internalWiremockHttpServer;
    protected final ObjectMapper mapper;

    public AbstractMockHandler(WireMockData wireMockData, TimeoutUtil timeoutUtil) {
        wireMockServer = wireMockData.wireMockServer();
        internalWiremockHttpServer = wireMockData.factory().getHttpServer();
        this.timeoutUtil = timeoutUtil;
        this.mapper = new ObjectMapper();
    }

    // As there need to be certain parameters filled in to avoid wiremock throwing nullpointers while templating, we create the request ourselves
    protected Request createInternalWiremockRequest(String url, String method, String requestBody, String dateHeader, String contentType) {
        if (dateHeader == null) {
            dateHeader = "";
        }
        HttpHeaders httpHeaders = new HttpHeaders(new HttpHeader("Date", dateHeader));
        return new Request() {
            @Override
            public String getUrl() {
                return Urls.getPathAndQuery(url);
            }

            @Override
            public String getAbsoluteUrl() {
                return url;
            }

            @Override
            public RequestMethod getMethod() {
                return RequestMethod.fromString(method);
            }

            @Override
            public String getScheme() {
                return "";
            }

            @Override
            public String getHost() {
                return "";
            }

            @Override
            public int getPort() {
                return wireMockServer.port();
            }

            @Override
            public String getClientIp() {
                return "";
            }

            @Override
            public String getHeader(String key) {
                List<String> values = header(key).getValues();
                if (values.isEmpty()) {
                    return "";
                }
                return values.getFirst();
            }

            @Override
            public HttpHeader header(String key) {
                return getHeaders().getHeader(key);
            }

            @Override
            public ContentTypeHeader contentTypeHeader() {
                return new ContentTypeHeader(contentType);
            }

            @Override
            public HttpHeaders getHeaders() {
                return httpHeaders;
            }

            @Override
            public boolean containsHeader(String key) {
                return !getHeader(key).isEmpty();
            }

            @Override
            public Set<String> getAllHeaderKeys() {
                return getHeaders().keys();
            }

            @Override
            public QueryParameter queryParameter(String key) {
                return null;
            }

            @Override
            public FormParameter formParameter(String key) {
                return null;
            }

            @Override
            public Map<String, FormParameter> formParameters() {
                return Map.of();
            }

            @Override
            public Map<String, Cookie> getCookies() {
                return Map.of();
            }

            @Override
            public byte[] getBody() {
                return requestBody.getBytes();
            }

            @Override
            public String getBodyAsString() {
                return "";
            }

            @Override
            public String getBodyAsBase64() {
                return "";
            }

            @Override
            public boolean isMultipart() {
                return false;
            }

            @Override
            public Collection<Part> getParts() {
                return List.of();
            }

            @Override
            public Part getPart(String name) {
                return null;
            }

            @Override
            public boolean isBrowserProxyRequest() {
                return false;
            }

            @Override
            public Optional<Request> getOriginalRequest() {
                return Optional.empty();
            }

            @Override
            public String getProtocol() {
                return "";
            }
        };
    }

    protected Response routeRequest(Request request) {
        return internalWiremockHttpServer.stubRequest(request);
    }
}
