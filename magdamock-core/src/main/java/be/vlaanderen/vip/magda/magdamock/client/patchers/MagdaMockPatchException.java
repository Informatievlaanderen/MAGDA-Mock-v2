package be.vlaanderen.vip.magda.magdamock.client.patchers;

public class MagdaMockPatchException extends RuntimeException {
    public MagdaMockPatchException(Throwable cause) {
        super(cause);
    }

    public MagdaMockPatchException(String message) {
        super(message);
    }
}
