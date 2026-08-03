package be.vlaanderen.vip.magda.magdamock.config.rest;

import lombok.Getter;

@Getter
public class RestPathParameter implements RestParameter {
    private ParameterType type;
    private String value;
    public RestPathParameter(String value) {
        type = ParameterType.PATH;
        this.value = value;
    }
    public static RestPathParameter of(String value) {
        return new RestPathParameter(value);
    }
}
