package uk.gov.dwp.jsa.jsaps.mapper;

import org.junit.Before;
import org.junit.Test;
import uk.gov.dwp.jsa.adaptors.dto.jsaps.JsapsRequest;
import uk.gov.dwp.jsa.jsaps.model.ba.ClaimAdmin;
import uk.gov.dwp.jsa.jsaps.model.ba.Flags;
import uk.gov.dwp.jsa.jsaps.model.ba.Jobseeker;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class BaMapperClaimAdminTest {


    private static final String NOT_APPLICABLE = "X";
    private static final String YES = "Y";
    private static final String NO = "N";

    private JsapsRequest jsapsRequest;
    private JsapsRequestFactory jsapsRequestFactory = new JsapsRequestFactory();
    private MapperUtils mapperUtils;
    private BaMapperClaimAdmin classToBeTested;
    private Jobseeker jobSeeker;

    @Before
    public void setup() {
        jobSeeker = new Jobseeker();
        mapperUtils = new MapperUtils();
        classToBeTested = new BaMapperClaimAdmin(mapperUtils);
    }

    @Test
    public void givenRequestWhenProcessClaimAdminThenClaimAdminCreated() {
        givenRequest();
        whenProcessClaimAdmin();
        thenClaimAdminCreated();
    }



    private void givenRequest() {
        jsapsRequest = jsapsRequestFactory.createJsapsRequest();
    }


    private void whenProcessClaimAdmin() {
        classToBeTested.processClaimAdmin(jsapsRequest, jobSeeker);
    }

    private void thenClaimAdminCreated() {
        final ClaimAdmin claimAdmin = jobSeeker.getClaimAdmin();
        assertThat(claimAdmin.getClaimType(),is("ORD"));
        assertThat(claimAdmin.getClaimFileType(),is(NO));
        assertThat(claimAdmin.getBenefitWeekEndingDay(),is("WED"));
        assertThat(claimAdmin.getAttendingCycle(),is("P"));
        assertThat(claimAdmin.getStatisticalSymbol(),is("E"));
        assertThat(claimAdmin.getOccupation().getSought(),is("9243"));
        assertThat(claimAdmin.getOccupation().getUsual(),is("9243"));
        assertThat(claimAdmin.getJobseekerChildcareDetails().getLrInterest(),is("1"));
        assertThat(claimAdmin.getJobseekerChildcareDetails().getNonResidentParent(),is(NO));
        assertThat(claimAdmin.getCsaNewRules(),is(NOT_APPLICABLE));
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
}
