package uk.gov.dwp.jsa.jsaps.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.dwp.jsa.adaptors.dto.jsaps.JsapsRequest;
import uk.gov.dwp.jsa.jsaps.model.ba.ChildcareType;
import uk.gov.dwp.jsa.jsaps.model.ba.ClaimAdmin;
import uk.gov.dwp.jsa.jsaps.model.ba.Flags;
import uk.gov.dwp.jsa.jsaps.model.ba.Jobseeker;
import uk.gov.dwp.jsa.jsaps.model.ba.Occupation;

@Component
public class BaMapperClaimAdmin extends Mapper {

    private static final String CLAIM_TYPE = "ORD";
    private static final String STATISTICAL_SYMBOL = "E";
    private static final String CONSTANT_FOR_SOUGHT_OR_USUAL = "9243";
    private static final String LR_INTEREST = "1";
    private static final String NOT_APPLICABLE = "X";
    private MapperUtils mapperUtils;

    @Autowired
    public BaMapperClaimAdmin(final MapperUtils mapperUtils) {
        this.mapperUtils = mapperUtils;
    }

    public void processClaimAdmin(final JsapsRequest request, final Jobseeker jobseeker) {

        ClaimAdmin claimAdmin = new ClaimAdmin();
        claimAdmin.setClaimType(CLAIM_TYPE);
        claimAdmin.setClaimFileType(NO);
        String nino = request.getClaimant().getNino();
        claimAdmin.setBenefitWeekEndingDay(mapperUtils.calculateDayOfWeekBasedOnNino(nino));
        claimAdmin.setAttendingCycle(mapperUtils.calculateCycleBasedOnNino(nino));
        claimAdmin.setStatisticalSymbol(STATISTICAL_SYMBOL);
        claimAdmin.setCsaNewRules(NOT_APPLICABLE);

        Occupation ocupation = new Occupation();
        ocupation.setSought(CONSTANT_FOR_SOUGHT_OR_USUAL);
        ocupation.setUsual(CONSTANT_FOR_SOUGHT_OR_USUAL);
        claimAdmin.setOccupation(ocupation);

        ChildcareType childrenCareDetails = new ChildcareType();
        childrenCareDetails.setLrInterest(LR_INTEREST);
        childrenCareDetails.setNonResidentParent(NO);
        claimAdmin.setJobseekerChildcareDetails(childrenCareDetails);

        claimAdmin.setCmIndicator(NO);
        Flags flags = new Flags();
        flags.setJsagSigned(YES);
        flags.setAppealsInterest(NO);
        flags.setPostalSigningApplicable(NO);
        flags.setSplitPayee(NO);
        flags.setAdvanceAllowed(YES);
        flags.setCompensationRecoveryGroup(NO);
        flags.setLboClaim(NO);
        flags.setSocialFundInterest(NO);
        flags.setLegalAidInterest(NO);
        flags.setOverpaymentInterest(NO);
        flags.setReferToClaimDocs(NO);
        flags.setDirectPaymentInterest(NO);
        flags.setAsylumSeeker(NO);
        flags.setReferToAnotherClaimDocs(NO);
        flags.setInhibitNextPayment(NO);
        flags.setRequestBarcodeLabel(NO);
        claimAdmin.setFlags(flags);

        jobseeker.setClaimAdmin(claimAdmin);
    }

}
