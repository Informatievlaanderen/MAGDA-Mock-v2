package be.vlaanderen.vip.magda.magdamock.config.rest;

import lombok.Getter;

@Getter
public class RestBodyParameter implements RestParameter {
    private ParameterType type;
    private String value;
    public RestBodyParameter(String value) {
        type = ParameterType.BODY;
        this.value = value;
    }
    public static RestBodyParameter of(String value) {
        return new RestBodyParameter(value);
    }
}
