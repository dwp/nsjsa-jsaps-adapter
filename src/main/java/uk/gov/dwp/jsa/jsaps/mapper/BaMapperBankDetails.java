package uk.gov.dwp.jsa.jsaps.mapper;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import uk.gov.dwp.jsa.adaptors.dto.jsaps.JsapsRequest;
import uk.gov.dwp.jsa.jsaps.model.ba.ActdetailsType;
import uk.gov.dwp.jsa.jsaps.model.ba.BankDetails;
import uk.gov.dwp.jsa.jsaps.model.ba.BuildingSocietyDetails;
import uk.gov.dwp.jsa.jsaps.model.ba.Jobseeker;
import uk.gov.dwp.jsa.jsaps.model.ba.Mop;
import uk.gov.dwp.jsa.jsaps.service.JsapsBuildingSocietyService;
import uk.gov.dwp.jsa.jsaps.util.date.JsapsDateFormatter;

import java.time.LocalDate;

@Component
public class BaMapperBankDetails extends Mapper {

    private static final int BANK_HOLDER_CHARACTER_LIMIT = 18;

    @Autowired
    private JsapsBuildingSocietyService jsapsBuildingSocietyService;

    private JsapsDateFormatter dateFormatter;

    @Autowired
    public BaMapperBankDetails(final JsapsDateFormatter dateFormatter) {
        this.dateFormatter = dateFormatter;
    }

    public void processBank(final JsapsRequest request, final Jobseeker jobseeker) {
        Mop mop = new Mop();
        mop.setPersonalIssueFlag(NO);
        ActdetailsType currentActDetails = new ActdetailsType();
        if (request.getBankDetails() != null) {
            if (ObjectUtils.isEmpty(request.getBankDetails().getReference())) {
                BankDetails bankDetails = new BankDetails();
                bankDetails.setEffectiveDate(dateFormatter.format(LocalDate.now()));
                bankDetails.setAccountName(
                        StringUtils.left(request.getBankDetails().getAccountHolder(), BANK_HOLDER_CHARACTER_LIMIT));
                bankDetails.setSortCode(request.getBankDetails().getSortCode());
                bankDetails.setAccountNumber(request.getBankDetails().getAccountNumber());
                currentActDetails.setBankDetails(bankDetails);
            } else {
                BuildingSocietyDetails buildingSocietyDetails = new BuildingSocietyDetails();
                buildingSocietyDetails.setEffectiveDate(dateFormatter.format(LocalDate.now()));
                buildingSocietyDetails.setAccountName(request.getBankDetails().getAccountHolder());
                String buildingSocietyCode = jsapsBuildingSocietyService.getBuildingSocietyCode(request);
                buildingSocietyDetails.setBuildingSocietyCode(buildingSocietyCode);
                buildingSocietyDetails.setAccountNumber(request.getBankDetails().getReference());
                currentActDetails.setBuildingSocietyDetails(buildingSocietyDetails);
            }
            mop.setCurrentActDetails(currentActDetails);
            jobseeker.setMop(mop);
        }
    }
}
