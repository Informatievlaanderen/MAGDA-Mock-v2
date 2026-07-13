package be.vlaanderen.vip.magda.magdamock.client.logging;

import be.vlaanderen.vip.magda.magdamock.utils.MagdaDocument;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

@Slf4j
public class SoapLogHelper {

    public static void contextSetSoapServiceNameVersion(MagdaDocument magdaDocument) {
        try {
            MagdaDocument.MagdaServiceIdentification magdaServiceIdentification = magdaDocument.getServiceIdentification();
            MDC.put("ServiceName", magdaServiceIdentification.getServiceNaam());
        } catch (Exception ex) {
            log.info("Could not get Soap Service Name Version", ex);
        }
    }

    public static void contextSetLifecyclePhase(LifecyclePhase lifecyclePhase) {
        MDC.put("LifecyclePhase", lifecyclePhase.name());
    }

    public static void contextSetReference(MagdaDocument magdaDocument) {
        try {
            String referte = magdaDocument.xpath("//Context/Bericht/Afzender/Referte").item(0).getTextContent();
            MDC.put("Reference", referte);
        } catch (Exception ex) {
            log.info("Could not get Soap Reference Value", ex);
        }
    }

    public static void contextSetCorrelationId(String correlationId) {
        MDC.put("CorrelationId", correlationId);
    }

    public enum LifecyclePhase {
        NOT_SPECIFIED,
        REQUEST_VALIDATION,
        REQUEST_PRE_PROCESSING,
        RESPONSE_MAPPING,
        RESPONSE_DYNAMIC_FUNCTIONS,
        RESPONSE_POST_PROCESSING,
        RESPONSE_VALIDATION
    }
}
