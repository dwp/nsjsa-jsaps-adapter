package uk.gov.dwp.jsa.jsaps.util.date;

import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DateSeed {
    public LocalDate now() {
        return LocalDate.now();
    }
}

