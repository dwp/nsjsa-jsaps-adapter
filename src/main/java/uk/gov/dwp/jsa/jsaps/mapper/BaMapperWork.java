package uk.gov.dwp.jsa.jsaps.mapper;

import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.dwp.jsa.adaptors.dto.jsaps.JsapsRequest;
import uk.gov.dwp.jsa.jsaps.model.ba.CurrentWork;
import uk.gov.dwp.jsa.jsaps.model.ba.Earnings;
import uk.gov.dwp.jsa.jsaps.model.ba.Employer;
import uk.gov.dwp.jsa.jsaps.model.ba.Jobseeker;
import uk.gov.dwp.jsa.jsaps.model.ba.JobseekerEmploymentDates;
import uk.gov.dwp.jsa.jsaps.model.ba.MandatoryTwoLineAddressType;
import uk.gov.dwp.jsa.jsaps.model.ba.PersonNumberNameType;
import uk.gov.dwp.jsa.jsaps.model.ba.PreviousWork;
import uk.gov.dwp.jsa.jsaps.model.ba.Review;
import uk.gov.dwp.jsa.jsaps.util.date.JsapsDateFormatter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Component
public class BaMapperWork extends Mapper {

    private static final String DISMISSAL = "dismissal";
    private static final String YOU_LEFT_VOLUNTARILY = "you left voluntarily";
    private static final String NUMBER_DAYS_WEEK_WORKED = "5";
    private static final String PERSON_NUMBER = "01";
    private static final String REVIEW_TYPE_JOB_SEEKER = "3";
    private static final int WEEKS_TO_ADD = 6;
    private static final String NSJSA_NOT_RELEVANT = "NSJSA Not relevant";
    private static final String ZERO_POUNDS = "0.00";
    private static final String NOT_VERIFIED = "NV";

    private JsapsDateFormatter dateFormatter;
    private BAMapperUtils baMapperUtils;
    private MoneyFormatter moneyFormatter;

    @Autowired
    public BaMapperWork(final JsapsDateFormatter dateFormatter,
                        final BAMapperUtils baMapperUtils,
                        final MoneyFormatter moneyFormatter) {
        this.dateFormatter = dateFormatter;
        this.baMapperUtils = baMapperUtils;
        this.moneyFormatter = moneyFormatter;
    }

    public void processCurrentWorkList(final JsapsRequest request, final Jobseeker jobseeker) {
        int index = 0;
        List<CurrentWork> currentWorkList = new ArrayList<>();
        if (request.getCircumstances().getCurrentWork() != null) {
            jobseeker.setCurrentWork(currentWorkList);
            while (index < request.getCircumstances().getCurrentWork().size()) {
                uk.gov.dwp.jsa.adaptors.dto.claim.circumstances.CurrentWork dwpCurrentWork =
                        request.getCircumstances().getCurrentWork().get(index);
                CurrentWork jsapsCurrentWork = new CurrentWork();
                createCurrentWorkBasicFields(dwpCurrentWork, jsapsCurrentWork);
                jsapsCurrentWork.setEmployer(createEmployer(dwpCurrentWork));
                jsapsCurrentWork.setReview(createCurrentWorkReview(request.getCircumstances().getDateOfClaim()));
                jsapsCurrentWork.setEarnings(createCurrentWorkEarnings(dwpCurrentWork));
                currentWorkList.add(jsapsCurrentWork);
                index++;
            }
        }
        jobseeker.setCurrentWork(currentWorkList);
    }


    public void processPreviousWorkList(final JsapsRequest request, final Jobseeker jobseeker) {
        int index = 0;
        List<PreviousWork> previousWorkList = new ArrayList<>();
        jobseeker.setPreviousWork(previousWorkList);
        while (index < request.getCircumstances().getPreviousWork().size()) {

            uk.gov.dwp.jsa.adaptors.dto.claim.circumstances.PreviousWork dwpPreviousWork =
                    request.getCircumstances().getPreviousWork().get(index);
            PreviousWork jsapsPreviousWork = new PreviousWork();
            PersonNumberNameType personType = new PersonNumberNameType();
            personType.setPersonNumber(PERSON_NUMBER);
            jsapsPreviousWork.setPerson(personType);

            final JobseekerEmploymentDates jobSeekerEmploymentDates = new JobseekerEmploymentDates();
            jobSeekerEmploymentDates.setJobseekerEmploymentStartDate(
                    dateFormatter.format(dwpPreviousWork.getStartDate()));
            jobSeekerEmploymentDates.setJobseekerEmploymentEndDate(dateFormatter.format(dwpPreviousWork.getEndDate()));
            jsapsPreviousWork.setJobseekerEmploymentDates(jobSeekerEmploymentDates);

            jsapsPreviousWork.setSelfEmployed(
                    BooleanUtils.toString(dwpPreviousWork.isSelfEmployedOrDirector(), YES, NO));
            jsapsPreviousWork.setEmployer(createEmployer(dwpPreviousWork));
            String reason = dwpPreviousWork.getReasonEnded().toLowerCase(Locale.getDefault());
            jsapsPreviousWork.setLeftDueToDismissal(
                    BooleanUtils.toString(reason.equals(DISMISSAL), YES, NO));
            jsapsPreviousWork.setLeftVoluntarily(
                    BooleanUtils.toString(reason.equals(YOU_LEFT_VOLUNTARILY), YES, NO));
            jsapsPreviousWork.setJs84Issued(NO);
            jsapsPreviousWork.setIssueJs85(NO);
            jsapsPreviousWork.setIssueJs85Reminder(NO);
            jsapsPreviousWork.setNumberDaysWeeksWorked(NUMBER_DAYS_WEEK_WORKED);
            jsapsPreviousWork.setHowMuchOwed(ZERO_POUNDS);
            previousWorkList.add(jsapsPreviousWork);
            index++;
        }
    }

    private void createCurrentWorkBasicFields(
            final uk.gov.dwp.jsa.adaptors.dto.claim.circumstances.CurrentWork dwpCurrentWork,
            final CurrentWork jsapsCurrentWork) {

        PersonNumberNameType personType = new PersonNumberNameType();
        personType.setPersonNumber(PERSON_NUMBER);
        jsapsCurrentWork.setPerson(personType);
        jsapsCurrentWork.setSelfEmployed(BooleanUtils.toString(dwpCurrentWork.isSelfEmployedOrDirector(), YES, NO));
        jsapsCurrentWork.setTradeDisputeInvolvement(NO);
        jsapsCurrentWork.setContactEmployer(NO);
        jsapsCurrentWork.setEmployedAs(NSJSA_NOT_RELEVANT);
        jsapsCurrentWork.setUsualOccupationFlag(NO);
    }


    private Review createCurrentWorkReview(final LocalDate dateOfClaim) {
        Review review = new Review();
        review.setReviewType(REVIEW_TYPE_JOB_SEEKER);
        review.setCurrentWorkReviewDate(dateFormatter.format(dateOfClaim.plusWeeks(WEEKS_TO_ADD)));
        return review;
    }

    private Earnings createCurrentWorkEarnings(
            final uk.gov.dwp.jsa.adaptors.dto.claim.circumstances.CurrentWork dwpCurrentWork) {
        Earnings earnings = new Earnings();
        earnings.setNetPay(moneyFormatter.format(dwpCurrentWork.getNetPay()));
        earnings.setPaymentPeriod(baMapperUtils.translatePaymentFrequency(dwpCurrentWork.getPaymentFrequency()));
        earnings.setWeeklyHoursWorked(baMapperUtils.formatHoursPerWeek(dwpCurrentWork.getHoursPerWeek()));
        earnings.setPaidEmployment(BooleanUtils.toString(dwpCurrentWork.isPaid(), YES, NO));
        earnings.setVerified(NOT_VERIFIED);
        earnings.setExpensesReimbursed(NO);
        earnings.setPersonalOccupationPension(NO);
        return earnings;
    }

    private Employer createEmployer(final uk.gov.dwp.jsa.adaptors.dto.claim.circumstances.Work work) {
        Employer jsapsEmployer = new Employer();
        MandatoryTwoLineAddressType address = new MandatoryTwoLineAddressType();
        List<String> addressLines = new ArrayList<>();
        baMapperUtils.addIfNotBlank(addressLines, work.getEmployerAddress().getFirstLine());
        baMapperUtils.addIfNotBlank(addressLines, work.getEmployerAddress().getSecondLine());
        baMapperUtils.addIfNotBlank(addressLines, work.getEmployerAddress().getTown());
        baMapperUtils.addIfNotBlank(addressLines, work.getEmployerAddress().getCountry());
        address.setAddressline(addressLines);
        setIfNotNull(address::setPostcode, work.getEmployerAddress().getPostCode());
        jsapsEmployer.setAddress(address);
        setIfNotNull(jsapsEmployer::setPhoneNumber, work.getEmployerPhoneNumber());
        jsapsEmployer.setName(work.getEmployerName());
        return jsapsEmployer;
    }

}
