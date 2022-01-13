package uk.gov.dwp.jsa.jsaps.mapper;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.dwp.jsa.adaptors.dto.jsaps.JsapsRequest;
import uk.gov.dwp.jsa.jsaps.model.es.ClaimDetails;
import uk.gov.dwp.jsa.jsaps.model.es.JobseekerType;
import uk.gov.dwp.jsa.jsaps.model.es.PersonalDetails;
import uk.gov.dwp.jsa.jsaps.util.date.JsapsDateFormatter;

import java.time.LocalDate;

@Component
public class ESMapper extends Mapper {
    private static final String RECEIVING_UNIVERSAL_CREDIT_EVENT_SUBTYPE = "19";
    private static final String NOT_RECEIVING_UNIVERSAL_CREDIT_EVENT_SUBTYPE = "01";
    private static final String EVENT_TYPE = "01";
    private JsapsDateFormatter jsapsDateFormatter;
    private MapperUtils mapperUtils;

    @Autowired
    public ESMapper(final JsapsDateFormatter jsapsDateFormatter,
                    final MapperUtils mapperUtils) {
        this.jsapsDateFormatter = jsapsDateFormatter;
        this.mapperUtils = mapperUtils;
    }

    public JobseekerType map(final JsapsRequest request) {
        String nino = request.getClaimant().getNino();
        LocalDate dateOfClaim = request.getCircumstances().getDateOfClaim();
        PersonalDetails personalDetails = createPersonalDetails(nino, dateOfClaim);
        ClaimDetails claimDetails = createClaimDetails(request, nino, dateOfClaim);
        JobseekerType jobseekerType = new JobseekerType();
        jobseekerType.setEsActionCompletedDate(jsapsDateFormatter.format(LocalDate.now()));
        jobseekerType.setPersonalDetails(personalDetails);
        jobseekerType.setClaimDetails(claimDetails);
        return jobseekerType;
    }

    private ClaimDetails createClaimDetails(final JsapsRequest request, final String nino,
                                            final LocalDate dateOfClaim) {
        ClaimDetails claimDetails = new ClaimDetails();
        claimDetails.setBackdatedClaim(NO);
        claimDetails.setDateOfClaim(jsapsDateFormatter.format(dateOfClaim));
        claimDetails.setFailedAllWork(NO);
        claimDetails.setJsaOnlineClaim(BooleanUtils.toString(!request.getAgentMode(), YES, NO));
        claimDetails.setBwe(mapperUtils.calculateDayOfWeekBasedOnNino(nino));
        claimDetails.setEsjNumber(StringUtils.substring(request.getHomeOfficeId(), 1));
        claimDetails.setPaymentCycle(mapperUtils.calculateCycleBasedOnNino(nino));
        claimDetails.setPostalSigningApplicable(NO);
        claimDetails.setEventType(EVENT_TYPE);
        if (BooleanUtils.toBoolean(request.getReceivingUniversalCredit())) {
            claimDetails.setEventSubtype(RECEIVING_UNIVERSAL_CREDIT_EVENT_SUBTYPE);
        } else {
            claimDetails.setEventSubtype(NOT_RECEIVING_UNIVERSAL_CREDIT_EVENT_SUBTYPE);
        }
        return claimDetails;
    }


    private PersonalDetails createPersonalDetails(final String nino,
                                                  final LocalDate dateOfClaim) {
        PersonalDetails personalDetails = new PersonalDetails();
        personalDetails.setJobseekerNino(nino);
        personalDetails.setCustomerFlag(YES);
        personalDetails.setClericalInterest(NO);
        personalDetails.setNominatedPayee(YES);
        personalDetails.setAddressEffectiveDate(jsapsDateFormatter.format(dateOfClaim));
        return personalDetails;
    }


}
