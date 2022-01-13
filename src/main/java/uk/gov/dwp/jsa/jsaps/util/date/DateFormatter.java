package uk.gov.dwp.jsa.jsaps.util.date;

import java.time.LocalDate;

public interface DateFormatter {

    String format(final LocalDate date);

    LocalDate parse(final String date);
}
