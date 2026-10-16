package be.vlaanderen.vip.magda.magdamock.client.patchers;

import be.vlaanderen.vip.magda.magdamock.utils.MagdaMockDocument;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Clock;
import java.util.Date;
import java.util.UUID;
import java.util.function.Supplier;

@Slf4j
public class GeefBetalingenHandicapResponsePatcher extends BasicSoapResponsePatcher {

    public GeefBetalingenHandicapResponsePatcher() {
        super();
    }

    public GeefBetalingenHandicapResponsePatcher(Clock clock, Supplier<UUID> uuidSupplier) {
        super(clock, uuidSupplier);
    }

    @Override
    public MagdaMockDocument patchResponse(MagdaMockDocument request, Document response) {
        MagdaMockDocument magdaMockDocument = super.patchResponse(request, response);
        try {
            SimpleDateFormat yearMonthFormat = new SimpleDateFormat("yyyy-MM");

            var beginDateString = request.getValue("//Criteria/ConsultPaymentsCriteria/period/beginDate");
            var beginDate = yearMonthFormat.parse(beginDateString);
            var endDateString = request.getValue("//Criteria/ConsultPaymentsCriteria/period/endDate");
            var endDate = yearMonthFormat.parse(endDateString);
            var paymentsResponse = magdaMockDocument.getValues("//payments/payment/yearMonth");

            paymentsResponse.stream().filter(x -> {
                try {
                    Date parsedDate = yearMonthFormat.parse(x);
                    return parsedDate.before(beginDate) || parsedDate.after(endDate);
                } catch (ParseException e) {
                    log.error("Unable to parse date in payment yearMonth", e);
                    return false;
                }
            }).forEach(x -> magdaMockDocument.removeNode("//payments/payment[yearMonth[text()='" + x + "']]"));
        } catch (ParseException | NullPointerException e) {
            log.error("Unable to parse dates, no filtering will be applied", e);
        }
        var paymentsCount = magdaMockDocument.getValues("//payments/payment/yearMonth").size();
        if (paymentsCount < 1) {
            magdaMockDocument.setValue("//status/value", "NO_DATA_FOUND");
            magdaMockDocument.setValue("//status/code", "MSG00100");
        }
        return magdaMockDocument;
    }
}
