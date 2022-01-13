package uk.gov.dwp.jsa.jsaps.mapper;

import org.springframework.stereotype.Component;
import uk.gov.dwp.jsa.adaptors.dto.claim.circumstances.PensionDetail;

import java.time.LocalDate;

@Component
public class PensionStartDateFactory {
    private static final int DAYS_30 = 30;

    public LocalDate create(
            final PensionDetail pensionDetail,
            final LocalDate dateOfClaim,
            final TypePension typePension) {

        if (TypePension.CURRENT.equals(typePension)
                || TypePension.DEFERRED.equals(typePension)) {
            return dateOfClaim;
        }

        if (null == pensionDetail.getStartDate()) {
            return dateOfClaim.plusDays(DAYS_30);

        } else if (pensionDetail.getStartDate().getMonth().equals(dateOfClaim.getMonth())) {
            return dateOfClaim;
        } else {
            return pensionDetail.getStartDate().withDayOfMonth(1);
        }
    }


}
