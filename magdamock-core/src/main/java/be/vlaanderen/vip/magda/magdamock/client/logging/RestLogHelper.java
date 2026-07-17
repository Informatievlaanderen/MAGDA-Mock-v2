package be.vlaanderen.vip.magda.magdamock.client.logging;

import be.vlaanderen.vip.magda.magdamock.utils.MagdaMockDocument;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

@Slf4j
public class RestLogHelper {

    public static void contextSetRestServiceNameVersion(String method, String path) {
        try {
            MDC.put("ServiceName", String.format("%s %s", method, path));
        } catch (Exception ex) {
            log.info("Could not get Rest Service Name Version", ex);
        }
    }

    public static void contextSetLifecyclePhase(LifecyclePhase lifecyclePhase) {
        MDC.put("LifecyclePhase", lifecyclePhase.name());
    }

    public static void contextSetCorrelationId(String correlationId) {
        MDC.put("CorrelationId", correlationId);
    }

}
