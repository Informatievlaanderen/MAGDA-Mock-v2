package be.vlaanderen.vip.magda.magdamock.rest;

import com.github.erosb.kappa.core.validation.ValidationException;

import java.util.Collection;
import java.util.Map;

public class LenientRestValidator implements RestValidator {

    @Override
    public void validateRawRequest(String path, String query, String method, String requestBody, Map<String, Collection<String>> headers) {
        // NOTE: lenient validator does no checks
    }

    @Override
    public void validateResponseOnly(String path, String method, int statusCode, String resBody, Map<String, Collection<String>> resHeaders) {
        // NOTE: lenient validator does no checks
    }
}
