package uk.gov.dwp.jsa.jsaps.mapper;

import org.junit.Before;
import org.junit.Test;
import uk.gov.dwp.jsa.adaptors.dto.jsaps.JsapsRequest;
import uk.gov.dwp.jsa.jsaps.model.ba.Jobseeker;
import uk.gov.dwp.jsa.jsaps.model.ba.Tax;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class BaMapperTaxTest {

    private static final String YES = "Y";
    private static final String NO = "N";

    private BaMapperTax classToBeTested;
    private JsapsRequestFactory jsapsRequestFactory = new JsapsRequestFactory();
    private Jobseeker jobSeeker;

    @Before
    public void setup() {
        jobSeeker = new Jobseeker();
        classToBeTested =  new BaMapperTax();
    }

    @Test
    public void processTax() {
    }

    @Test
    public void givenJobSeekerToSetWithTaxDetailsWhenProcessPensionThenOcupationalPensionListCreated() {
        givenJobSeekerToSetWithTaxDetails();
        whenProcessPension();
        thenThePensionsAreMappedCorrectly();
    }

    private void givenJobSeekerToSetWithTaxDetails() {
        //We don't use any data coming from NSJSA
        jobSeeker = new Jobseeker();
    }

    private void whenProcessPension() {
        classToBeTested.processTax(jobSeeker);
    }

    private void thenThePensionsAreMappedCorrectly() {
        Tax tax = jobSeeker.getTax();
        assertThat(tax.getP187Completed(),is(NO));
        assertThat(tax.getP45().getExpected(),is(YES));
        assertThat(tax.getP45().getSupplied(),is(NO));
        assertThat(tax.getContinuingEmployment(),is(NO));
    }


}
