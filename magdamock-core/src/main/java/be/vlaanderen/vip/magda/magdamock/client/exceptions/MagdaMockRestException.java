package be.vlaanderen.vip.magda.magdamock.client.exceptions;

/**
 * An exception to be thrown by MagdaMock in case MagdaMock really fails to handle a response,
 * rather than simulating a backend failing to handle a response.
 */
public class MagdaMockRestException extends RuntimeException {

    public MagdaMockRestException(String message, Throwable cause) {
        super(message, cause);
    }

    public MagdaMockRestException(String message) {
        super(message);
    }
}