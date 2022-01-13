package uk.gov.dwp.jsa.jsaps.mapper;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import uk.gov.dwp.jsa.adaptors.dto.claim.circumstances.PensionDetail;
import uk.gov.dwp.jsa.adaptors.dto.jsaps.JsapsRequest;
import uk.gov.dwp.jsa.jsaps.model.ba.Jobseeker;
import uk.gov.dwp.jsa.jsaps.model.ba.OccupationalPension;
import uk.gov.dwp.jsa.jsaps.model.ba.PensionDetails;
import uk.gov.dwp.jsa.jsaps.util.date.JsapsDateFormatter;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static uk.gov.dwp.jsa.jsaps.mapper.JsapsRequestFactory.DATE_OF_CLAIM;

public class BaMapperPensionTest {


    private static final String PERSON_NUMBER = "1";
    private static final  String MAPPED_PAYMENT_FREQUENCY_MONTHLY  = "5";
    private static final  String MAPPED_PAYMENT_FREQUENCY_WEEKLY = "2";
    private static final String START_CLAIM_DATE_TEXT = "240918";
    private static final LocalDate START_CLAIM_DATE = LocalDate.of(2018, 9, 24);
    private static final String REVIEW_DATE_TEXT = "240419";
    private static final LocalDate REVIEW_DATE = LocalDate.of(2019, 4, 24);

    @Mock
    private PensionReviewDateFactory pensionReviewDateFactory;
    @Mock
    private PensionStartDateFactory pensionStartDateFactory;

    private JsapsRequest jsapsRequest;
    private JsapsRequestFactory jsapsRequestFactory = new JsapsRequestFactory();
    private JsapsDateFormatter jsapsDateFormatter;
    private BAMapperUtils baMapperUtils;
    private BaMapperPension classToBeTested;
    private Jobseeker jobSeeker;

    @Before
    public void setup() {
        initMocks(this);
        jobSeeker = new Jobseeker();
        jsapsDateFormatter = new JsapsDateFormatter();
        baMapperUtils = new BAMapperUtils();
        classToBeTested = new BaMapperPension(
                jsapsDateFormatter,
                baMapperUtils,
                pensionReviewDateFactory,
                pensionStartDateFactory,
                new MoneyFormatter(baMapperUtils));
        when(pensionStartDateFactory.create(any(PensionDetail.class), eq(DATE_OF_CLAIM), any(TypePension.class))).thenReturn(START_CLAIM_DATE);
        when(pensionReviewDateFactory.create(any(PensionDetail.class), eq(DATE_OF_CLAIM), any(TypePension.class))).thenReturn(REVIEW_DATE);
    }

    @Test
    public void givenRequestWhenProcessPensionThenOcupationalPensionListCreated() {
        givenRequest();
        whenProcessPension();
        thenThePensionsAreMappedCorrectly();
    }

    private void givenRequest() {
        jsapsRequest = jsapsRequestFactory.createJsapsRequest();
    }


    private void whenProcessPension() {
        classToBeTested.processOcupationalPension(jsapsRequest, jobSeeker);
    }


    private void thenThePensionsAreMappedCorrectly() {
        List<OccupationalPension> occupationalPensionList = jobSeeker.getOccupationalPension();
        assertThat(occupationalPensionList.size(), is(2));
        OccupationalPension occupationalPension = occupationalPensionList.get(0);
        assertThat(occupationalPension.getPerson().getPersonNumber(), is(PERSON_NUMBER));
        assertThat(occupationalPension.getPensionReference(), is("NSJSA N/A"));
        assertThat(occupationalPension.getPensionDetails().size(), is(1));
        //This test relies on the order of processing of the data, first one deferred, one current, one future.
        PensionDetails deferredPensionDetails = occupationalPensionList.get(0).getPensionDetails().get(0);
        assertPension(deferredPensionDetails, START_CLAIM_DATE_TEXT, "54.92",
                MAPPED_PAYMENT_FREQUENCY_WEEKLY, "N");
        assertThat(occupationalPensionList.get(0).getReviewDate(),is(REVIEW_DATE_TEXT));
        PensionDetails currentPensionDetails = occupationalPensionList.get(1).getPensionDetails().get(0);
        assertPension(currentPensionDetails, START_CLAIM_DATE_TEXT, "54.92",
                MAPPED_PAYMENT_FREQUENCY_WEEKLY, "Y");
        assertThat(occupationalPensionList.get(1).getReviewDate(),is(REVIEW_DATE_TEXT));
    }


    private void assertPension(PensionDetails pensionDetails, final String startDate,
                               final String amount, final String paymentFrequency, final String received) {
        assertThat(pensionDetails.getPensionStartDate(), is(startDate));
        assertThat(pensionDetails.getFrequency(), is(paymentFrequency));
        assertThat(pensionDetails.getGrossAmount(), is(amount));
        assertThat(pensionDetails.getPensionNettAmount(), is("0.00"));
        assertThat(pensionDetails.getReceivedFlag(), is(received));
        assertThat(pensionDetails.getVerification(), is("V"));
    }




}
