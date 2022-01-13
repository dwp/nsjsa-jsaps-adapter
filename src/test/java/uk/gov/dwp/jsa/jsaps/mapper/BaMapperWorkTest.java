package uk.gov.dwp.jsa.jsaps.mapper;

import org.junit.Before;
import org.junit.Test;
import uk.gov.dwp.jsa.adaptors.dto.jsaps.JsapsRequest;
import uk.gov.dwp.jsa.jsaps.model.ba.CurrentWork;
import uk.gov.dwp.jsa.jsaps.model.ba.Earnings;
import uk.gov.dwp.jsa.jsaps.model.ba.Employer;
import uk.gov.dwp.jsa.jsaps.model.ba.EmployerEmploymentDates;
import uk.gov.dwp.jsa.jsaps.model.ba.Jobseeker;
import uk.gov.dwp.jsa.jsaps.model.ba.JobseekerEmploymentDates;
import uk.gov.dwp.jsa.jsaps.model.ba.MandatoryTwoLineAddressType;
import uk.gov.dwp.jsa.jsaps.model.ba.PreviousWork;
import uk.gov.dwp.jsa.jsaps.model.ba.Review;
import uk.gov.dwp.jsa.jsaps.util.date.JsapsDateFormatter;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.iterableWithSize;
import static org.hamcrest.MatcherAssert.assertThat;

public class BaMapperWorkTest {



    public static final String HOURS_PER_WEEK = "12";
    private static final String YES = "Y";
    private static final String NO = "N";
    private static final String PERSON_NUMBER = "01";
    private static final String REVIEW_TYPE_JOB_SEEKER = "3";
    private static final String NUMBER_DAYS_WEEK_WORKED = "5";

    private JsapsRequestFactory jsapsRequestFactory = new JsapsRequestFactory();
    private JsapsDateFormatter jsapsDateFormatter;
    private BAMapperUtils baMapperUtils;
    private BaMapperWork classToBeTested;
    private Jobseeker jobSeeker;
    JsapsRequest jsapsRequest;


    @Before
    public void setup() {
        jobSeeker= new Jobseeker();
        jsapsDateFormatter = new JsapsDateFormatter();
        baMapperUtils = new BAMapperUtils();
        classToBeTested = new BaMapperWork(jsapsDateFormatter, baMapperUtils, new MoneyFormatter(baMapperUtils));
    }

    @Test
    public void givenRequestWhenProcessCurrentWorkListThenCurrentWorkListCreated(){
        givenRequest();
        whenProcessCurrentWork();
        thenCurrentWorkListCreated();
    }

    @Test
    public void givenRequestWhenProcessPreviousWorkListThenPreviousWorkListCreated(){
        givenRequest();
        whenProcessPreviousWorkList();
        thenPreviousWorkListCreated();
    }

    @Test
    public void givenRequestWithNoCurrentWorkWhenProcessCurrentWorkListThenCurrenWorkListEmpty(){
        givenRequestWithNoCurrentWork();
        whenProcessCurrentWork();
        thenCurrenWorkListEmpty();
    }


    private void givenRequestWithNoCurrentWork() {
        jsapsRequest = jsapsRequestFactory.createJsapsRequest();
        jsapsRequest.getCircumstances().setCurrentWork(null);
    }

    private void givenRequest(){
        jsapsRequest = jsapsRequestFactory.createJsapsRequest();
    }

    private void whenProcessCurrentWork(){
        classToBeTested.processCurrentWorkList(jsapsRequest,jobSeeker);
    }

    private void whenProcessPreviousWorkList(){
        classToBeTested.processPreviousWorkList(jsapsRequest,jobSeeker);
    }

    private void thenCurrentWorkListCreated() {
        final List<CurrentWork> currentWorkList = jobSeeker.getCurrentWork();
        assertThat(currentWorkList, iterableWithSize(1));
        CurrentWork jsapsCurrentWork = currentWorkList.get(0);
        assertThat(jsapsCurrentWork.getPerson().getPersonNumber(), is(PERSON_NUMBER));
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


    private void thenCurrenWorkListEmpty() {
        assert(jobSeeker.getCurrentWork().isEmpty());
    }


    private void thenPreviousWorkListCreated() {
        final List<PreviousWork> previousWorkList = jobSeeker.getPreviousWork();
        for(PreviousWork jsapsPreviousWork  : previousWorkList) {
            assertThat(jsapsPreviousWork.getPerson().getPersonNumber(), is(PERSON_NUMBER));
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

}
