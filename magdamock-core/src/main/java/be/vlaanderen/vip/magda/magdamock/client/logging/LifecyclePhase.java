package be.vlaanderen.vip.magda.magdamock.client.logging;

public enum LifecyclePhase {
    NOT_SPECIFIED,
    REST_SETUP,
    SOAP_SETUP,
    REQUEST_VALIDATION,
    REQUEST_PRE_PROCESSING,
    RESPONSE_ROUTING,
    RESPONSE_TEMPLATING,
    RESPONSE_POST_PROCESSING,
    RESPONSE_VALIDATION
}
