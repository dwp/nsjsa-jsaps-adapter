package uk.gov.dwp.jsa.jsaps.mapper;

import uk.gov.dwp.jsa.adaptors.dto.claim.circumstances.PensionDetail;

import java.time.LocalDate;

public class PensionDetailBuilder {

    private static final LocalDate START_DATE = null;

    private LocalDate startDate = START_DATE;

    public PensionDetail create() {
        PensionDetail pensionDetail = new PensionDetail();
        pensionDetail.setStartDate(startDate);
        return pensionDetail;
    }

    public PensionDetailBuilder withStartDate(LocalDate startDate) {
        this.startDate = startDate;
        return this;
    }
}
