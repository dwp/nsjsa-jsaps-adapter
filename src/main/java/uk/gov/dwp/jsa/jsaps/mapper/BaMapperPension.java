package uk.gov.dwp.jsa.jsaps.mapper;

import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.dwp.jsa.adaptors.dto.claim.circumstances.PensionDetail;
import uk.gov.dwp.jsa.adaptors.dto.jsaps.JsapsRequest;
import uk.gov.dwp.jsa.jsaps.model.ba.Jobseeker;
import uk.gov.dwp.jsa.jsaps.model.ba.OccPenPersonNumberNameType;
import uk.gov.dwp.jsa.jsaps.model.ba.OccupationalPension;
import uk.gov.dwp.jsa.jsaps.model.ba.PensionDetails;
import uk.gov.dwp.jsa.jsaps.util.date.JsapsDateFormatter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class BaMapperPension extends Mapper {

    private static final String PENSION_REFERENCE = "NSJSA N/A";
    private static final String PERSON_NUMBER = "1";
    private static final String ZERO_POUNDS = "0.00";
    private static final String PENSIONS_VERIFICATION = "V";

    private static final String WEEKLY_PXP = "2";

    private final JsapsDateFormatter dateFormatter;
    private final BAMapperUtils baMapperUtils;
    private final PensionReviewDateFactory pensionReviewDateFactory;
    private final PensionStartDateFactory pensionStartDateFactory;
    private final MoneyFormatter moneyFormatter;

    @Autowired
    public BaMapperPension(final JsapsDateFormatter dateFormatter,
                           final BAMapperUtils baMapperUtils,
                           final PensionReviewDateFactory pensionReviewDateFactory,
                           final PensionStartDateFactory pensionStartDateFactory,
                           final MoneyFormatter moneyFormatter) {
        this.dateFormatter = dateFormatter;
        this.baMapperUtils = baMapperUtils;
        this.pensionReviewDateFactory = pensionReviewDateFactory;
        this.pensionStartDateFactory = pensionStartDateFactory;
        this.moneyFormatter = moneyFormatter;
    }


    public void processOcupationalPension(final JsapsRequest request, final Jobseeker jobseeker) {
        List<OccupationalPension> ocupationalPensionList = new ArrayList<>();
        LocalDate dateOfClaim = request.getCircumstances().getDateOfClaim();
        request.getCircumstances().getPensions().getDeferred().stream()
                .forEach(p -> ocupationalPensionList.add(mapOccupationalPension(dateOfClaim, p, TypePension.DEFERRED)));
        request.getCircumstances().getPensions().getCurrent().stream()
                .forEach(p -> ocupationalPensionList.add(mapOccupationalPension(dateOfClaim, p, TypePension.CURRENT)));
        jobseeker.setOccupationalPension(ocupationalPensionList);
    }

    private OccupationalPension mapOccupationalPension(final LocalDate dateOfClaim, final PensionDetail pensionDetail,
                                                       final TypePension typePension) {
        OccupationalPension occupationalPension = createOccupationalPension(pensionDetail, dateOfClaim, typePension);
        List<PensionDetails> jsapsPensionDetailsListSizeOne = new ArrayList<>();
        jsapsPensionDetailsListSizeOne.add(mapPensionDetail(pensionDetail, typePension, dateOfClaim));
        occupationalPension.setPensionDetails(jsapsPensionDetailsListSizeOne);
        return occupationalPension;
    }


    private OccupationalPension createOccupationalPension(final PensionDetail pensionDetail,
                                                          final LocalDate dateOfClaim,
                                                          final TypePension typePension) {
        OccupationalPension occupationalPension = new OccupationalPension();
        OccPenPersonNumberNameType person = new OccPenPersonNumberNameType();
        person.setPersonNumber(PERSON_NUMBER);
        occupationalPension.setPerson(person);
        occupationalPension.setPensionReference(PENSION_REFERENCE);
        occupationalPension.setReviewDate(
                dateFormatter.format(pensionReviewDateFactory.create(pensionDetail, dateOfClaim, typePension)));
        return occupationalPension;
    }

    private PensionDetails mapPensionDetail(final PensionDetail dwpPension,
                                            final TypePension typePension,
                                            final LocalDate dateOfClaim) {
        PensionDetails jsapsPensionDetail = new PensionDetails();

        jsapsPensionDetail.setPensionStartDate(
                dateFormatter.format(pensionStartDateFactory.create(dwpPension, dateOfClaim, typePension)));
        jsapsPensionDetail.setGrossAmount(moneyFormatter.format(
                baMapperUtils.toWeeklyAmount(dwpPension.getGrossPay(), dwpPension.getPaymentFrequency())
        ));
        jsapsPensionDetail.setPensionNettAmount(ZERO_POUNDS);
        jsapsPensionDetail.setFrequency(WEEKLY_PXP);
        jsapsPensionDetail.setVerification(PENSIONS_VERIFICATION);
        jsapsPensionDetail.setReceivedFlag(BooleanUtils.toString(TypePension.CURRENT == typePension, YES, NO));
        return jsapsPensionDetail;
    }


}
