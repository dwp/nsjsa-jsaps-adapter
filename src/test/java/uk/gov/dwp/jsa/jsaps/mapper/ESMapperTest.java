package uk.gov.dwp.jsa.jsaps.mapper;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import uk.gov.dwp.jsa.adaptors.dto.jsaps.JsapsRequest;
import uk.gov.dwp.jsa.jsaps.model.es.ClaimDetails;
import uk.gov.dwp.jsa.jsaps.model.es.JobseekerType;
import uk.gov.dwp.jsa.jsaps.model.es.PersonalDetails;
import uk.gov.dwp.jsa.jsaps.util.date.JsapsDateFormatter;

import java.time.LocalDate;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static uk.gov.dwp.jsa.jsaps.mapper.JsapsRequestFactory.*;

public class ESMapperTest {

    private static final String RECEIVING_UNIVERSAL_CREDIT_EVENT_SUBTYPE = "19";
    private static final String EVENT_TYPE = "01";
    private static final String YES = "Y";
    private static final String NO = "N";
    private ESMapper classToBeTested;

    private JsapsRequest theRequest;
    private JobseekerType resultToCheck;


    private JsapsRequestFactory jsapsRequestFactory= new JsapsRequestFactory();
    private JsapsDateFormatter jsapsDateFormatter;

    @Before
    public void setup(){
        jsapsDateFormatter = new JsapsDateFormatter();
        MapperUtils mapperUtils = new MapperUtils();
        classToBeTested = new ESMapper(jsapsDateFormatter,mapperUtils);
    }

    @Test
    public void givenARequestWithoutWhenMapThenJobSeekerTypeIsCreated() {
        givenARequestWithoutBackDatedClaim();
        whenMap();
        thenJobSeekerTypeIsCreatedWithoutBackDatedClaimDetails();
    }


    @Test
    public void givenABackDatedRequestWhenMapThenJobSeekerTypeHasBackDatedDetails() {
        givenARequest();
        whenMap();
        thenJobSeekerTypeIsCreated();

    }


    private void givenARequest(){
        theRequest = jsapsRequestFactory.createJsapsRequest();
    }


    private void givenARequestWithoutBackDatedClaim(){
        theRequest = jsapsRequestFactory.createJsapsRequest();
       theRequest.getCircumstances().setDateOfClaim( theRequest.getCircumstances().getClaimStartDate());
    }

    private void whenMap(){
        resultToCheck = classToBeTested.map(theRequest);
    }


    private void thenJobSeekerTypeIsCreated(){
        PersonalDetails personalDetails = resultToCheck.getPersonalDetails();

        assertThat(personalDetails.getJobseekerNino(), is(theRequest.getClaimant().getNino()));
        assertThat(personalDetails.getCustomerFlag(),is(YES));
        assertThat(personalDetails.getClericalInterest(),is(NO));
        assertThat(personalDetails.getNominatedPayee(),is(YES));
        //TODO Find out this value TR0732 date of contact
        //Value: Initial date of contact (see TR0732)
        assertThat(personalDetails.getAddressEffectiveDate(),is("240918"));
        ClaimDetails claimDetails = resultToCheck.getClaimDetails();
        assertThat(claimDetails.getDateOfClaim(),is("240918"));
        assertThat(claimDetails.getFailedAllWork(),is(NO));
        assertThat(claimDetails.getBwe(),is("WED"));
        assertThat(claimDetails.getPaymentCycle(),is("P"));
        assertThat(claimDetails.getPostalSigningApplicable(),is(NO));
        assertThat(claimDetails.getBackdatedClaim(),is(NO));
        assertThat(claimDetails.getEventType(),is(EVENT_TYPE));
        assertThat(claimDetails.getEventSubtype(),is(RECEIVING_UNIVERSAL_CREDIT_EVENT_SUBTYPE));
        assertThat(resultToCheck.getEsActionCompletedDate(),is(jsapsDateFormatter.format(LocalDate.now())));
        assertThat(claimDetails.getEsjNumber(),is(StringUtils.substring(HOME_OFFICE_ID, 1)));
    }

    private void thenJobSeekerTypeIsCreatedWithoutBackDatedClaimDetails() {
        ClaimDetails claimDetails = resultToCheck.getClaimDetails();
        assertThat(claimDetails.getBackdatedClaim(),is(NO));
        assertThat(claimDetails.getBackdatedClaimDetails(),is(nullValue()));
    }



}
