package uk.gov.dwp.jsa.jsaps.util.date;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class JsapsDateFormatter {

    private static final String DATE_FORMAT = "ddMMyy";

    public String format(final LocalDate date) {
        if (date == null) {
            return null;
        }
        return DateTimeFormatter.ofPattern(DATE_FORMAT).format(date);
    }


}
