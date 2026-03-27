package be.vlaanderen.vip.magda.magdamock.utils;

import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;
import com.github.tomakehurst.wiremock.extension.TemplateHelperProviderExtension;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MockDataTemplateHelper {

    public static TemplateHelperProviderExtension getTemplateHelperExtensions() {
        return new TemplateHelperProviderExtension() {
            @Override
            public String getName() {
                return "custom-helpers";
            }

            @Override
            public Map<String, Helper<?>> provideTemplateHelpers() {
                return Map.of("parseDate", new DateParseHelper(),
                        "formatDate", new DateFormatter(),
                        "dateMath", new DateMathHelper(),
                        "startOfQuarter", new StartQuarterHelper(),
                        "endOfQuarter", new EndQuarterHelper());
            }
        };
    }


    private static ChronoUnit parseUnit(String u) {
        return switch (u.toLowerCase()) {
            case "y" -> ChronoUnit.YEARS;
            case "m" -> ChronoUnit.MONTHS;
            case "h" -> ChronoUnit.HOURS;
            case "s" -> ChronoUnit.SECONDS;
            default -> ChronoUnit.DAYS;
        };
    }

    private static class DateParseHelper implements Helper<Object> {
        @Override
        public Object apply(Object toParse, Options options) {
            LocalDate date;
            try {
                date = LocalDate.parse(toParse.toString(), DateTimeFormatter.RFC_1123_DATE_TIME);
            } catch (Exception ex) {
                date = LocalDate.now();
            }
            return date;
        }
    }

    private static class DateFormatter implements Helper<LocalDate> {
        @Override
        public Object apply(LocalDate date, Options options) {
            String format = options.param(0, "yyyy-MM-dd");
            return DateTimeFormatter.ofPattern(format).format(date);
        }
    }

    private static class DateMathHelper implements Helper<LocalDate> {
        @Override
        public Object apply(LocalDate date, Options options) {
            String addition = options.param(0);
            Pattern dateTimeDiffPattern = Pattern.compile("([-+][0-9]*)?([a-z]+)", Pattern.CASE_INSENSITIVE);
            Matcher matcher = dateTimeDiffPattern.matcher(addition);
            if (!matcher.find())
                return date;
            String chronoUnit = matcher.group(2).toLowerCase(Locale.ROOT);
            int amountToAdd = Integer.parseInt(matcher.group(1));
            return date.plus(amountToAdd, parseUnit(chronoUnit));
        }
    }

    private static class StartQuarterHelper implements Helper<LocalDate> {
        @Override
        public Object apply(LocalDate date, Options options) {
            LocalDate newTime = date.minusDays(0);
            int month = Math.floorDivExact(newTime.getMonthValue() - 1, 3) * 3 + 1;
            int day = 1;
            newTime = newTime.withMonth(month);
            newTime = newTime.withDayOfMonth(day);
            return newTime;
        }
    }

    private static class EndQuarterHelper implements Helper<LocalDate> {
        @Override
        public Object apply(LocalDate date, Options options) {
            LocalDate newTime = date.plusMonths(3);
            int month = Math.floorDivExact(newTime.getMonthValue() - 1, 3) * 3 + 1;
            int day = 1;
            newTime = newTime.withMonth(month);
            newTime = newTime.withDayOfMonth(day);
            newTime = newTime.minusDays(1);
            return newTime;
        }
    }
}
