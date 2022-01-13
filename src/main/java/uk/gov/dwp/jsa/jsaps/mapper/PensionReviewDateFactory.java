package uk.gov.dwp.jsa.jsaps.mapper;

import org.springframework.stereotype.Component;
import uk.gov.dwp.jsa.adaptors.dto.claim.circumstances.PensionDetail;
import uk.gov.dwp.jsa.jsaps.util.date.DateSeed;

import java.time.LocalDate;

@Component
public class PensionReviewDateFactory {

    private static final int DAYS_30 = 30;
    private static final int DAYS_60 = 60;

    private DateSeed dateSeed;

    public PensionReviewDateFactory(final DateSeed dateSeed) {

        this.dateSeed = dateSeed;
    }

    public LocalDate create(
            final PensionDetail pensionDetail,
            final LocalDate dateOfClaim,
            final TypePension typePension) {

        if (TypePension.CURRENT.equals(typePension)
                || TypePension.DEFERRED.equals(typePension)) {
            return dateSeed.now().plusDays(DAYS_30);
        }

        if (null == pensionDetail.getStartDate()) {
            return dateOfClaim.plusDays(DAYS_60);

        } else if (pensionDetail.getStartDate().getMonth().equals(dateOfClaim.getMonth())) {
            return dateSeed.now().plusDays(DAYS_30);
        } else {
            return pensionDetail.getStartDate().withDayOfMonth(1).plusDays(DAYS_30);
        }
    }


}
