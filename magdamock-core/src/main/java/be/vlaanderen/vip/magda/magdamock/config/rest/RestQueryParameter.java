package be.vlaanderen.vip.magda.magdamock.config.rest;

import lombok.Getter;

@Getter
public class RestQueryParameter implements RestParameter {
    private ParameterType type;
    private String value;

    public RestQueryParameter(String value) {
        type = ParameterType.QUERY;
        this.value = value;
    }

    public static RestQueryParameter of(String value) {
        return new RestQueryParameter(value);
    }
}
