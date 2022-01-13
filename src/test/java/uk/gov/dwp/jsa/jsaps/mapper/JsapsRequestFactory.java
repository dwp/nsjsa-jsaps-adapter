package uk.gov.dwp.jsa.jsaps.mapper;


import uk.gov.dwp.jsa.adaptors.dto.claim.Address;
import uk.gov.dwp.jsa.adaptors.dto.claim.BankDetails;
import uk.gov.dwp.jsa.adaptors.dto.claim.Claimant;
import uk.gov.dwp.jsa.adaptors.dto.claim.ContactDetails;
import uk.gov.dwp.jsa.adaptors.dto.claim.Name;
import uk.gov.dwp.jsa.adaptors.dto.claim.circumstances.AskedForAdvice;
import uk.gov.dwp.jsa.adaptors.dto.claim.circumstances.AvailableForInterview;
import uk.gov.dwp.jsa.adaptors.dto.claim.circumstances.BackDating;
import uk.gov.dwp.jsa.adaptors.dto.claim.circumstances.Circumstances;
import uk.gov.dwp.jsa.adaptors.dto.claim.circumstances.CurrentWork;
import uk.gov.dwp.jsa.adaptors.dto.claim.circumstances.Education;
import uk.gov.dwp.jsa.adaptors.dto.claim.circumstances.JuryService;
import uk.gov.dwp.jsa.adaptors.dto.claim.circumstances.NonWorkingIllness;
import uk.gov.dwp.jsa.adaptors.dto.claim.circumstances.OtherBenefit;
import uk.gov.dwp.jsa.adaptors.dto.claim.circumstances.PensionDetail;
import uk.gov.dwp.jsa.adaptors.dto.claim.circumstances.Pensions;
import uk.gov.dwp.jsa.adaptors.dto.claim.circumstances.PreviousWork;
import uk.gov.dwp.jsa.adaptors.dto.claim.circumstances.TravelledOutsideUk;
import uk.gov.dwp.jsa.adaptors.dto.jsaps.JsapsRequest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.UUID;


public class JsapsRequestFactory {



    public static final String HOME_OFFICE_ID = "207621";
    private static final String TARGET_OFFICE_ID = "123456";
    private static final String ESJ_NUMBER = "07621";
    private static final String BUILDING_SOCIETY_ACCOUNT_NUMBER ="14575881";

    private static final String NINO = "QQ123456D";
    private static final String FIRST_NAME = "John";
    private static final String LAST_NAME = "Doe";
    private static final LocalDate DATE_OF_BIRTH = LocalDate.of(1980, 11, 19);
    private static final String POST_CODE = "WS11 5DF";
    private static final String LINE_1 = "1, Poundlove Road";
    private static final String EMAIL_ADDRESS = "testemail@gmail.com";
    private static final String TITLE = "MR";
    public static final String MOBILE_NUMBER = "07756 789789";
    private static final String ACCOUNT_HOLDER = "Mr John Doe";
    public static final String COUNTRY = "England";
    private static final String LINE_2 = "Sandwell";
    public static final LocalDate CLAIM_START_DATE = LocalDate.of(2018, 2, 25);
    public static final LocalDate DATE_OF_CLAIM = LocalDate.of(2018, 9, 24);
    public static final LocalDate JURY_START = LocalDate.of(2018, 2, 15);
    public static final LocalDate JURY_FINISH = LocalDate.of(2018, 3, 24);
    private static final String COURSE_NAME = "City & Guilds Photography";
    private static final String COURSE_PLACE_NAME = "Walsall College";
    private static final int COURSE_HOURS_PER_WEEK = 18;
    private static final LocalDate COURSE_STATE_DATE = LocalDate.of(2016, 6, 9);
    private static final LocalDate COURSE_END_DATE = LocalDate.of(2016, 12, 12);
    public static final String REFERENCE_NUMBER_PARAM = "1234";
    private static final String TOWN = "West Midlands";
    private static final LocalDate PENSION_START_DATE = LocalDate.of(2018, 3, 25);
    private static final LocalDate PENSION_FUTURE_START_DATE = LocalDate.of(2019, 3, 25);
    private static final  String PROVIDER_NAME = "Tesco";
    private static final  BigDecimal PENSION_AMOUNT  = new BigDecimal(238);
    private static final  BigDecimal FUTURE_PENSION_AMOUNT  = new BigDecimal(425.5);
    private static final  String PAYMENT_FREQUENCY  = "MONTHLY";
    private static final  String PAYMENT_FREQUENCY_QUARTERLY = "QUARTERLY";
    private static final  Boolean  HAS_PERIOD_INCREASE  = true;
    private static final   String NEXT_INCREASE_MONTH  = "DECEMBER";
    // TODO - needs removing once we stop testing against a static HTML file
    private static final LocalDate DATE_OF_PRINT = LocalDate.of(2018, 10, 12);

    // TODO - temporarily put this in
    private static final Locale LOCALE = Locale.getDefault();
    public static final String EMPLOYER_PHONE_NUMBER = "0247 999 9999";
    public static final BigDecimal WAGE = new BigDecimal("230");
    public static final int HOURS_PER_WEEK = 12;
    private static final UUID ID = UUID.randomUUID();
    private static final UUID CLAIMANT_ID = UUID.randomUUID();
    private static final String LOCALE_EN = "en";

    public static final boolean HAS_MORE_THAN_MAXIMUM_CURRENT_JOBS = false;
    public static final boolean HAS_MORE_THAN_MAXIMUM_PREVIOUS_JOBS = false;
    private static final boolean HAS_EXTRA_PENSIONS = false;


    private BankDetails bankDetails;

    private String buildingSocietyReference;
    private String SORT_CODE = "00 11 22";
    private String ACCOUNT_NUMBER = "01234567";

    public Statement create() {
        final Address homeAddress =
                new AddressBuilder()
                        .withCountry(COUNTRY)
                        .withFirstLine(LINE_1)
                        .withSecondLine(LINE_2)
                        .withTown(TOWN)
                        .withPostCode(POST_CODE)
                        .build();
        final Address postalAddress =
                new AddressBuilder()
                        .withCountry(COUNTRY)
                        .withFirstLine(LINE_1)
                        .withTown(TOWN)
                        .withSecondLine(LINE_2)
                        .withPostCode(POST_CODE)
                        .build();

        bankDetails = new BankDetails(
                ID,
                CLAIMANT_ID,
                ACCOUNT_HOLDER,
                SORT_CODE,
                ACCOUNT_NUMBER,
                buildingSocietyReference);
        Claimant claimant = new ClaimantBuilder()
                .withNino(NINO)
                .withName(new Name(TITLE, FIRST_NAME, LAST_NAME))
                .withDOB(DATE_OF_BIRTH)
                .withAddress(homeAddress)
                .withPostalAddress(postalAddress)
                .withContactDetails(new ContactDetails(MOBILE_NUMBER, EMAIL_ADDRESS, true, true))
                .build();

        final Education education = new Education(
                COURSE_NAME,
                COURSE_HOURS_PER_WEEK,
                COURSE_STATE_DATE,
                COURSE_END_DATE,
                COURSE_PLACE_NAME);

        JuryService juryService = new JuryService(JURY_START, JURY_FINISH);
        OtherBenefit otherBenefit = new OtherBenefit("ESA, Carers allowance");

        final Address currentWorkAddress =
                new AddressBuilder()
                        .withFirstLine("5-7 Hornsey St")
                        .withSecondLine("Hornsey,")
                        .withTown("London")
                        .withPostCode("N7 8GA")
                        .build();

        final List<CurrentWork> currentWork = Arrays.asList(
                new CurrentWork(
                        "Tescos",
                        true, EMPLOYER_PHONE_NUMBER, currentWorkAddress,
                        false, true, WAGE, PAYMENT_FREQUENCY,
                        false, false, HOURS_PER_WEEK));

        final Address previousWorkAddress =
                new AddressBuilder()
                        .withFirstLine("ABC Firm House")
                        .withSecondLine("Sometown,")
                        .withTown("London")
                        .withPostCode("N1 2AA")
                        .build();

        final List<PreviousWork> previousWork = Arrays.asList(
                new PreviousWork("ABC Firm",
                                false,
                                "0131 999 9999",
                                previousWorkAddress,
                        LocalDate.of(2018, 7, 22),
                        LocalDate.of(2018, 8, 15),
                        "OTHER",
                        "I was bullied out of the job by my manager",
                        true));



        final BackDating backDating = new BackDating(
                true,
                true,
                "Couldn't apply sooner",
                false,
                true,
                new AskedForAdvice("the advice", true),
                new NonWorkingIllness(true, LocalDate.now(), LocalDate.now()),
                new TravelledOutsideUk(true, LocalDate.now(), LocalDate.now())
        );

        Circumstances circumstances = new Circumstances(
                ID,
                CLAIMANT_ID,
                CLAIM_START_DATE,
                DATE_OF_CLAIM,
                true,
                false,
                currentWork,
                previousWork,
                education, juryService,
                new AvailableForInterview(),
                otherBenefit,
                createPensions(),
                HAS_MORE_THAN_MAXIMUM_CURRENT_JOBS,
                HAS_MORE_THAN_MAXIMUM_PREVIOUS_JOBS,
                LOCALE_EN,
                backDating,
                false);

        return new Statement(DATE_OF_PRINT,
                             claimant,
                             circumstances,
                             bankDetails);
    }

    private static Pensions createPensions() {
        Pensions pensions = new Pensions();
        pensions.setDeferred(createPension());
        pensions.setCurrent(createPension());
        pensions.setHasExtraPensions(HAS_EXTRA_PENSIONS);
        return pensions;
    }

    private static List<PensionDetail> createPension() {
        final Address pensionProviderAddress =
                new AddressBuilder()
                        .withFirstLine("2 Sample Lane")
                        .withTown("Sampletown")
                        .withPostCode("SA4 7SH")
                        .build();
        List<PensionDetail> pensionDetailList = Arrays.asList(new PensionFactory()
                .create(PENSION_START_DATE, PROVIDER_NAME, PENSION_AMOUNT,
                        PAYMENT_FREQUENCY, HAS_PERIOD_INCREASE, NEXT_INCREASE_MONTH, pensionProviderAddress));
        return pensionDetailList;
    }

    public JsapsRequestFactory withBuildingSociety(){
        buildingSocietyReference = "22222";
        return this;
    }

    public JsapsRequestFactory withBuildingSocietyMappingReference() {
        buildingSocietyReference = "74574915";
        return this;
    }

    public JsapsRequestFactory withBuildingSocietySortCode() {
        SORT_CODE = "20 27 33";
        return this;
    }

    public JsapsRequestFactory withBuildingSocietyAccountNumber() {
        ACCOUNT_NUMBER = "74574915";
        return this;
    }


    public JsapsRequest createJsapsRequest() {
        Statement  statement = this.create();
        JsapsRequest request = new JsapsRequest();
        request.setCircumstances(statement.getCircumstances());
        request.setClaimant(statement.getClaimant());
        request.setBankDetails(statement.getBankDetails());
        request.setAgentMode(true);
        request.setReceivingUniversalCredit(true);
        request.setHomeOfficeId(HOME_OFFICE_ID);
        request.setTargetOfficeId(TARGET_OFFICE_ID);
        request.setEsjNumber(ESJ_NUMBER);
        return request;
    }

}
