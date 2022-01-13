package uk.gov.dwp.jsa.jsaps.mapper;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import uk.gov.dwp.jsa.adaptors.dto.jsaps.JsapsRequest;
import uk.gov.dwp.jsa.jsaps.model.ba.ActdetailsType;
import uk.gov.dwp.jsa.jsaps.model.ba.Jobseeker;
import uk.gov.dwp.jsa.jsaps.model.ba.Mop;
import uk.gov.dwp.jsa.jsaps.util.date.JsapsDateFormatter;

import java.time.LocalDate;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BaMapperBankDetailsTest {

    private static final String NO = "N";
    private static final String AA = "AA";
    private static final String BUILDING_SOCIETY_SORT_CODE_VALUE = "TC";
    private static final String BUILDING_SOCIETY_ACCOUNT_CODE_VALUE = "MM";
    final String ACCOUNT_HOLDER = "Mr John Doe";
    final String SORT_CODE = "00 11 22";
    final String ACCOUNT_NUMBER = "22222";
    final String BUILDING_SOCIETY_ACCOUNT_NUMBER = "74574915";

    private JsapsRequest jsapsRequest;
    private JsapsRequestFactory jsapsRequestFactory = new JsapsRequestFactory();
    private JsapsDateFormatter jsapsDateFormatter;
    private BAMapperUtils baMapperUtils;
    @Autowired
    private BaMapperBankDetails classToBeTested;
    private Jobseeker jobSeeker;

    @Before
    public void setup() {
        jobSeeker = new Jobseeker();
        jsapsDateFormatter = new JsapsDateFormatter();
        baMapperUtils = new BAMapperUtils();
    }

    @Test
    public void givenRequestWithBankDetailsWhenProcessBankDetailsThenTheBankDetailssAreMappedCorrectly() {
        givenRequestWithBankDetails();
        whenProcessBankDetails();
        thenTheBankDetailsAreMappedCorrectly();
    }

    @Test
    public void givenRequestWithBuildingSocietyWhenProcessBankDetailsThenTheBuildingSocietyDetailsAreMappedCorrectly() {
        givenRequestWithBuildingSociety();
        whenProcessBankDetails();
        thenTheBuildingSocietyDetailsAreMappedCorrectly();
    }

    @Test
    public void givenRequestWithBuildingSocietyCorrectCodeIsSuppliedForSortCode() {
        givenRequestWithBuildingSociety();
        givenRequestWithBuildingSocietySortCode();
        whenProcessBankDetails();
        thenBuildingSocietySortCodeDetailsAreMappedCorrectly();
    }

    @Test
    public void givenRequestWithBuildingSocietyCorrectCodeIsSuppliedForAccountNumber() {
        givenRequestWithBuildingSocietyMapping();
        givenRequestWithBuildingSocietyAccountNumber();
        whenProcessBankDetails();
        thenBuildingSocietyAccountNumberCodeDetailsAreMappedCorrectly();
    }

    private void givenRequestWithBankDetails() {
        jsapsRequest = jsapsRequestFactory.createJsapsRequest();
        jsapsRequest.getBankDetails().setAccountHolder("Mr John Smithroweshfdfhjfhfdsjbssssssaqwdqdccfgfvs more than eighty one characters");

    }

    private void givenRequestWithBuildingSociety() {
        jsapsRequest = jsapsRequestFactory.withBuildingSociety().createJsapsRequest();
    }

    private void givenRequestWithBuildingSocietyMapping() {
        jsapsRequest = jsapsRequestFactory.withBuildingSocietyMappingReference().createJsapsRequest();
    }

    private void givenRequestWithBuildingSocietySortCode() {
        jsapsRequest = jsapsRequestFactory.withBuildingSocietySortCode().createJsapsRequest();
    }

    private void givenRequestWithBuildingSocietyAccountNumber() {
        jsapsRequest = jsapsRequestFactory.withBuildingSocietyAccountNumber().createJsapsRequest();
    }

    private void whenProcessBankDetails() {
        classToBeTested.processBank(jsapsRequest, jobSeeker);
    }

    private void thenTheBankDetailsAreMappedCorrectly() {
        final Mop mop = jobSeeker.getMop();

        assertThat(mop.getPersonalIssueFlag(), is(NO));
        ActdetailsType currentActDetails = mop.getCurrentActDetails();
        assertThat(currentActDetails.getBankDetails().getEffectiveDate(),is(jsapsDateFormatter.format(LocalDate.now())));
        assertThat(currentActDetails.getBankDetails().getAccountName(),is("Mr John Smithrowes"));
        assertThat(currentActDetails.getBankDetails().getSortCode(),is(SORT_CODE));
        assertThat(currentActDetails.getBankDetails().getAccountNumber(),is("01234567"));
    }



    private void thenTheBuildingSocietyDetailsAreMappedCorrectly() {
        final Mop mop = jobSeeker.getMop();
        assertThat(mop.getPersonalIssueFlag(), is(NO));
        ActdetailsType currentActDetails = mop.getCurrentActDetails();
        assertThat(currentActDetails.getBuildingSocietyDetails().getEffectiveDate(),is(jsapsDateFormatter.format(LocalDate.now())));
        assertThat(currentActDetails.getBuildingSocietyDetails().getAccountName(),is(ACCOUNT_HOLDER));
        assertThat(currentActDetails.getBuildingSocietyDetails().getAccountNumber(),is(ACCOUNT_NUMBER));
        assertThat(currentActDetails.getBuildingSocietyDetails().getBuildingSocietyCode(),is(AA));
    }

    private void thenBuildingSocietySortCodeDetailsAreMappedCorrectly() {
        final Mop mop = jobSeeker.getMop();
        assertThat(mop.getPersonalIssueFlag(), is(NO));
        ActdetailsType currentActDetails = mop.getCurrentActDetails();
        assertThat(currentActDetails.getBuildingSocietyDetails().getEffectiveDate(),is(jsapsDateFormatter.format(LocalDate.now())));
        assertThat(currentActDetails.getBuildingSocietyDetails().getAccountName(),is(ACCOUNT_HOLDER));
        assertThat(currentActDetails.getBuildingSocietyDetails().getAccountNumber(),is(ACCOUNT_NUMBER));
        assertThat(currentActDetails.getBuildingSocietyDetails().getBuildingSocietyCode(),is(BUILDING_SOCIETY_SORT_CODE_VALUE));
    }

    private void thenBuildingSocietyAccountNumberCodeDetailsAreMappedCorrectly() {
        final Mop mop = jobSeeker.getMop();
        assertThat(mop.getPersonalIssueFlag(), is(NO));
        ActdetailsType currentActDetails = mop.getCurrentActDetails();
        assertThat(currentActDetails.getBuildingSocietyDetails().getEffectiveDate(),is(jsapsDateFormatter.format(LocalDate.now())));
        assertThat(currentActDetails.getBuildingSocietyDetails().getAccountName(),is(ACCOUNT_HOLDER));
        assertThat(currentActDetails.getBuildingSocietyDetails().getAccountNumber(),is(BUILDING_SOCIETY_ACCOUNT_NUMBER));
        assertThat(currentActDetails.getBuildingSocietyDetails().getBuildingSocietyCode(),is(BUILDING_SOCIETY_ACCOUNT_CODE_VALUE));
    }

}
