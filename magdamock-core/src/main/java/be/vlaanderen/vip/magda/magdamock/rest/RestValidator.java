package be.vlaanderen.vip.magda.magdamock.rest;

import com.github.erosb.kappa.core.validation.ValidationException;

import java.util.Collection;
import java.util.Map;

public interface RestValidator {
    void validateRawRequest(
            String path,
            String query,
            String method,
            String requestBody,
            Map<String, Collection<String>> headers) throws ValidationException;

    void validateResponseOnly(
            String path,
            String method,
            int statusCode,
            String resBody,
            Map<String, Collection<String>> resHeaders) throws ValidationException;
}
