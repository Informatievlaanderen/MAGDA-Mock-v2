package be.vlaanderen.vip.magda.magdamock.client.logging;

import be.vlaanderen.vip.magda.magdamock.utils.MagdaMockDocument;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

@Slf4j
public class SoapLogHelper {

    public static void contextSetSoapServiceNameVersion(MagdaMockDocument magdaMockDocument) {
        try {
            MagdaMockDocument.MagdaServiceIdentification magdaServiceIdentification = magdaMockDocument.getServiceIdentification();
            MDC.put("ServiceName", magdaServiceIdentification.getServiceNaam());
        } catch (Exception ex) {
            log.info("Could not get Soap Service Name Version", ex);
        }
    }

    public static void contextSetLifecyclePhase(LifecyclePhase lifecyclePhase) {
        MDC.put("LifecyclePhase", lifecyclePhase.name());
    }

    public static void contextSetReference(MagdaMockDocument magdaMockDocument) {
        try {
            String referte = magdaMockDocument.xpath("//Context/Bericht/Afzender/Referte").item(0).getTextContent();
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
        SOAP_SETUP,
        REQUEST_VALIDATION,
        REQUEST_PRE_PROCESSING,
        RESPONSE_MAPPING,
        RESPONSE_DYNAMIC_FUNCTIONS,
        RESPONSE_POST_PROCESSING,
        RESPONSE_VALIDATION
    }
}
