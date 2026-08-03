package be.vlaanderen.vip.magda.magdamock.config.rest;

import lombok.Getter;

@Getter
public class RestHeaderParameter implements RestParameter {
    private ParameterType type;
    private String value;
    public RestHeaderParameter(String value) {
        type = ParameterType.HEADER;
        this.value = value;
    }
    public static RestHeaderParameter of(String value) {
        return new RestHeaderParameter(value);
    }
}
