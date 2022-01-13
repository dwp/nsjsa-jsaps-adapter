package uk.gov.dwp.jsa.jsaps.mapper;

import org.junit.Before;
import org.junit.Test;
import uk.gov.dwp.jsa.adaptors.dto.claim.circumstances.PensionDetail;
import uk.gov.dwp.jsa.adaptors.dto.jsaps.JsapsRequest;
import uk.gov.dwp.jsa.jsaps.model.ba.*;
import uk.gov.dwp.jsa.jsaps.util.date.DateSeed;
import uk.gov.dwp.jsa.jsaps.util.date.JsapsDateFormatter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.iterableWithSize;
import static org.junit.Assert.assertThat;

public class BAMapperTest {

    public static final String HOURS_PER_WEEK = "12";
    private static final String YES = "Y";
    private static final String NO = "N";
    private static final String PENSION_PERSON_NUMBER = "1";
    private static final String EMPLOYMENT_PERSON_NUMBER = "01";
    private static final String REVIEW_TYPE_JOB_SEEKER = "3";
    private static final int WEEKS_TO_ADD = 6;
    private static final String NUMBER_DAYS_WEEK_WORKED = "5";
    private static final String MAPPED_PAYMENT_FREQUENCY_MONTHLY  = "5";
    private static final String MAPPED_PAYMENT_FREQUENCY_QUARTERLY = "6";
    private static final String START_CLAIM_DATE = "240918";
    private static final String MAPPED_PAYMENT_FREQUENCY_WEEKLY = "2";


    private BAMapper classToBeTested;

    private JsapsRequest theRequest;
    private Jobseeker resultToCheck;


    private JsapsRequestFactory jsapsRequestFactory = new JsapsRequestFactory();
    private JsapsDateFormatter jsapsDateFormatter;
    private BAMapperUtils baMapperUtils;
    private MapperUtils mapperUtils;
    private BaMapperWork baMapperWork;
    private BaMapperPension baMapperPension;
    private BaMapperClaimAdmin baMapperClaimAdmin;
    private BaMapperTax baMapperTax;
    private BaMapperBankDetails baMapperBankDetails;

    @Before
    public void setup() {
        jsapsDateFormatter = new JsapsDateFormatter();
        baMapperUtils = new BAMapperUtils();
        mapperUtils = new MapperUtils();
        baMapperWork = new BaMapperWork(jsapsDateFormatter, baMapperUtils, new MoneyFormatter(baMapperUtils));
        baMapperPension = new BaMapperPension(
                jsapsDateFormatter,
                baMapperUtils,
                new PensionReviewDateFactory(new DateSeed()),
                new PensionStartDateFactory(),
                new MoneyFormatter(baMapperUtils));
        baMapperClaimAdmin = new BaMapperClaimAdmin(mapperUtils);
        baMapperTax = new BaMapperTax();
        baMapperBankDetails = new BaMapperBankDetails(jsapsDateFormatter);
        classToBeTested = new BAMapper(jsapsDateFormatter, baMapperWork,baMapperPension,baMapperClaimAdmin,baMapperTax, baMapperBankDetails);
    }


    @Test
    public void givenABackDatedRequestWhenMapThenJobSeekerTypeHasBackDatedDetails() {
        givenARequest();
        whenMap();
        thenJobSeekerIsCreated();
    }


    @Test
    public void givenSeveralPensionsWhenMapThenThePensionsAreMappedCorrectly(){
        givenARequestWithPernsionsTwoDeferredTwoCurrent();
        whenMap();
        thenThePensionsAreMappedCorrectly();
    }

    private void givenARequest() {
        theRequest = jsapsRequestFactory.createJsapsRequest();
    }


    private void givenARequestWithPernsionsTwoDeferredTwoCurrent() {
        theRequest = jsapsRequestFactory.createJsapsRequest();
        List<PensionDetail> temp = new ArrayList<>();
        PensionDetail deferredPension = theRequest.getCircumstances().getPensions().getDeferred().get(0);
        temp.addAll(theRequest.getCircumstances().getPensions().getDeferred());
        temp.add(deferredPension);
        theRequest.getCircumstances().getPensions().setDeferred(temp);
        temp = new ArrayList<>();
        PensionDetail currentPension = theRequest.getCircumstances().getPensions().getCurrent().get(0);
        temp.addAll(theRequest.getCircumstances().getPensions().getCurrent());
        temp.add(currentPension);
        theRequest.getCircumstances().getPensions().setCurrent(temp);
    }

    private void whenMap() {
        resultToCheck = classToBeTested.map(theRequest);
    }


    private void thenJobSeekerIsCreated() {
        List<CurrentWork> currentWorkList = resultToCheck.getCurrentWork();
        assertCurrentWork(currentWorkList);
        List<PreviousWork> previousWorkList = resultToCheck.getPreviousWork();
        assertThat(previousWorkList, iterableWithSize(1));
        assertPreviousWork(previousWorkList);
        assertOcupationalPension(resultToCheck.getOccupationalPension());
        assertThat(resultToCheck.getLinkingDetails().getAwardMixedCredit(),is(NO));
        assertClaimAdmin(resultToCheck.getClaimAdmin());
        assertTaxes(resultToCheck.getTax());
        assertBankOrBuildingSocietyDetails(resultToCheck.getMop());
    }

    private void thenThePensionsAreMappedCorrectly(){
        List<OccupationalPension> occupationalPensionList = resultToCheck.getOccupationalPension();
        //It is a one element list
        assertThat(occupationalPensionList.size(),is(4));
        OccupationalPension occupationalPension = occupationalPensionList.get(0);
        assertThat(occupationalPension.getPerson().getPersonNumber(),is(PENSION_PERSON_NUMBER));
        assertThat(occupationalPension.getPensionReference(),is("NSJSA N/A"));
        assertThat(occupationalPension.getPensionDetails().size(),is(1));
        //This test relies on the order of processing of the data, first one deferred, one current, one future.
        PensionDetails deferredPensionDetails0 = occupationalPensionList.get(0).getPensionDetails().get(0);
        assertPension(deferredPensionDetails0,START_CLAIM_DATE, "54.92",
                MAPPED_PAYMENT_FREQUENCY_WEEKLY,  "N");
        PensionDetails deferredPensionDetails1 = occupationalPensionList.get(1).getPensionDetails().get(0);
        assertPension(deferredPensionDetails1,START_CLAIM_DATE, "54.92",
                MAPPED_PAYMENT_FREQUENCY_WEEKLY,  "N");
        PensionDetails currentPensionDetails1 = occupationalPensionList.get(2).getPensionDetails().get(0);
        assertPension(currentPensionDetails1,START_CLAIM_DATE, "54.92",
                MAPPED_PAYMENT_FREQUENCY_WEEKLY,  "Y");
        PensionDetails currentPensionDetails2 = occupationalPensionList.get(3).getPensionDetails().get(0);
        assertPension(currentPensionDetails2,START_CLAIM_DATE, "54.92",
                MAPPED_PAYMENT_FREQUENCY_WEEKLY,  "Y");
    }


    private void assertOcupationalPension(final List<OccupationalPension> occupationalPensionList) {
        //It is a one element list
        assertThat(occupationalPensionList.size(),is(2));
        OccupationalPension occupationalPension = occupationalPensionList.get(0);
        assertThat(occupationalPension.getPerson().getPersonNumber(),is(PENSION_PERSON_NUMBER));
        assertThat(occupationalPension.getPensionReference(),is("NSJSA N/A"));
        //This test relies on the order of processing of the data, first one deferred, one current, one future.
        PensionDetails deferredPensionDetails = occupationalPensionList.get(0).getPensionDetails().get(0);
        assertThat(occupationalPensionList.get(0).getPensionDetails().size(),is(1));
        assertPension(deferredPensionDetails, START_CLAIM_DATE, "54.92",
                MAPPED_PAYMENT_FREQUENCY_WEEKLY,  "N");
        PensionDetails currentPensionDetails = occupationalPensionList.get(1).getPensionDetails().get(0);
        assertThat(occupationalPensionList.get(1).getPensionDetails().size(),is(1));
        assertPension(currentPensionDetails,START_CLAIM_DATE, "54.92",
                MAPPED_PAYMENT_FREQUENCY_WEEKLY,  "Y");
    }


    private void assertCurrentWork(final List<CurrentWork> currentWorkList) {
        assertThat(currentWorkList, iterableWithSize(1));
        CurrentWork jsapsCurrentWork = currentWorkList.get(0);
        assertThat(jsapsCurrentWork.getPerson().getPersonNumber(), is(EMPLOYMENT_PERSON_NUMBER));
        assertThat(jsapsCurrentWork.getSelfEmployed(), is(YES));
        assertThat(jsapsCurrentWork.getTradeDisputeInvolvement(), is(NO));
        assertThat(jsapsCurrentWork.getContactEmployer(), is(NO));
        assertThat(jsapsCurrentWork.getEmployedAs(), is("NSJSA Not relevant"));
        assertThat(jsapsCurrentWork.getUsualOccupationFlag(), is(NO));
        List<String> addressLines = Arrays.asList("5-7 Hornsey St",
                "Hornsey,","London");
        assertEmployer(jsapsCurrentWork.getEmployer(),"N7 8GA", addressLines,  "Tescos","0247 999 9999");
        Review review = jsapsCurrentWork.getReview();
        assertThat(review.getReviewType(), is(REVIEW_TYPE_JOB_SEEKER));
        assertThat(review.getCurrentWorkReviewDate(), is("051118"));
        Earnings earnings = jsapsCurrentWork.getEarnings();
        assertThat(earnings.getNetPay(), is("230.00"));
        assertThat(earnings.getPaymentPeriod(), is("5"));
        assertThat(earnings.getWeeklyHoursWorked(), is(HOURS_PER_WEEK));
        assertThat(earnings.getPaidEmployment(), is(YES));
        assertThat(earnings.getVerified(), is("NV"));
        assertThat(earnings.getExpensesReimbursed(), is(NO));
        assertThat(earnings.getPersonalOccupationPension(), is(NO));
    }



    private void assertPreviousWork(final List<PreviousWork> previousWorkList) {
        for(PreviousWork jsapsPreviousWork  : previousWorkList) {
            assertThat(jsapsPreviousWork.getPerson().getPersonNumber(), is(EMPLOYMENT_PERSON_NUMBER));
            JobseekerEmploymentDates jobseekerEmploymentDates = jsapsPreviousWork.getJobseekerEmploymentDates();
            assertThat(jobseekerEmploymentDates.getJobseekerEmploymentStartDate(), is("220718"));
            assertThat(jobseekerEmploymentDates.getJobseekerEmploymentEndDate(), is("150818"));
            assertThat(jsapsPreviousWork.getSelfEmployed(), is(NO));
            List<String> addressLines = Arrays.asList("ABC Firm House", "Sometown,", "London");
            assertEmployer(jsapsPreviousWork.getEmployer(), "N1 2AA", addressLines, "ABC Firm", "0131 999 9999");
            assertThat(jsapsPreviousWork.getLeftDueToDismissal(), is(NO));
            assertThat(jsapsPreviousWork.getLeftVoluntarily(), is(NO));
            assertThat(jsapsPreviousWork.getJs84Issued(), is(NO));
            assertThat(jsapsPreviousWork.getIssueJs85(), is(NO));
            assertThat(jsapsPreviousWork.getIssueJs85Reminder(), is(NO));
            assertThat(jsapsPreviousWork.getNumberDaysWeeksWorked(), is(NUMBER_DAYS_WEEK_WORKED));
            assertThat(jsapsPreviousWork.getHowMuchOwed(), is("0.00"));
        }
    }



    private void assertClaimAdmin(final ClaimAdmin claimAdmin) {
        assertThat(claimAdmin.getClaimType(),is("ORD"));
        assertThat(claimAdmin.getClaimFileType(),is(NO));
        assertThat(claimAdmin.getBenefitWeekEndingDay(),is("WED"));
        assertThat(claimAdmin.getAttendingCycle(),is("P"));
        assertThat(claimAdmin.getStatisticalSymbol(),is("E"));
        assertThat(claimAdmin.getOccupation().getSought(),is("9243"));
        assertThat(claimAdmin.getOccupation().getUsual(),is("9243"));
        assertThat(claimAdmin.getJobseekerChildcareDetails().getLrInterest(),is("1"));
        assertThat(claimAdmin.getJobseekerChildcareDetails().getNonResidentParent(),is(NO));
        assertThat(claimAdmin.getCmIndicator(),is(NO));
        Flags flag = claimAdmin.getFlags();
        assertThat(flag.getJsagSigned(),is(YES));
        assertThat(flag.getAppealsInterest(),is(NO));
        assertThat(flag.getPostalSigningApplicable(),is(NO));
        assertThat(flag.getSplitPayee(),is(NO));
        assertThat(flag.getAdvanceAllowed(),is(YES));
        assertThat(flag.getCompensationRecoveryGroup(),is(NO));
        assertThat(flag.getLboClaim(),is(NO));
        assertThat(flag.getSocialFundInterest(),is(NO));
        assertThat(flag.getLegalAidInterest(),is(NO));
        assertThat(flag.getOverpaymentInterest(),is(NO));
        assertThat(flag.getReferToClaimDocs(),is(NO));
        assertThat(flag.getDirectPaymentInterest(),is(NO));
        assertThat(flag.getAsylumSeeker(),is(NO));
        assertThat(flag.getReferToAnotherClaimDocs(),is(NO));
        assertThat(flag.getInhibitNextPayment(),is(NO));
        assertThat(flag.getRequestBarcodeLabel(),is(NO));
    }


    private void assertTaxes(final Tax tax) {
        assertThat(tax.getP187Completed(),is(NO));
        assertThat(tax.getP45().getExpected(),is(YES));
        assertThat(tax.getP45().getSupplied(),is(NO));
        assertThat(tax.getContinuingEmployment(),is(NO));
    }

    private void assertBankOrBuildingSocietyDetails(final Mop mop) {
         final String ACCOUNT_HOLDER = "Mr John Doe";
        final String SORT_CODE = "00 11 22";
        final String ACCOUNT_NUMBER = "01234567";
        assertThat(mop.getPersonalIssueFlag(), is(NO));
        ActdetailsType currentActDetails = mop.getCurrentActDetails();
        assertThat(currentActDetails.getBankDetails().getEffectiveDate(),is(jsapsDateFormatter.format(LocalDate.now())));
        assertThat(currentActDetails.getBankDetails().getAccountName(),is(ACCOUNT_HOLDER));
        assertThat(currentActDetails.getBankDetails().getSortCode(),is(SORT_CODE));
        assertThat(currentActDetails.getBankDetails().getAccountNumber(),is(ACCOUNT_NUMBER));
    }

    private void assertEmployer(final Employer employer, final String postCode, final List<String> addressLines, final String name, final String phoneNumber) {
        MandatoryTwoLineAddressType address = employer.getAddress();
        assertThat(address.getAddressline().size(), is(addressLines.size()));
        int i = 0;
        while (i < addressLines.size() ){
            assertThat(address.getAddressline().get(i), is(addressLines.get(i)));
            i++;
        }
        assertThat(address.getPostcode(), is(postCode));
        assertThat(employer.getName(), is(name));
        assertThat(employer.getPhoneNumber(), is(phoneNumber));
    }

    private void assertPension(PensionDetails pensionDetails,final String startDate,
                               final String amount,  final String paymentFrequency, final String received){
        assertThat(pensionDetails.getPensionStartDate(),is(startDate));
        assertThat(pensionDetails.getFrequency(),is(paymentFrequency));
        assertThat(pensionDetails.getGrossAmount(),is(amount));
        assertThat(pensionDetails.getPensionNettAmount(),is("0.00"));
        assertThat(pensionDetails.getReceivedFlag(),is(received));
        assertThat(pensionDetails.getVerification(),is("V"));
    }
}
