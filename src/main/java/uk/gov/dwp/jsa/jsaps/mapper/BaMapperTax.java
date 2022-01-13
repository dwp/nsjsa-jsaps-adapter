package uk.gov.dwp.jsa.jsaps.mapper;

import org.springframework.stereotype.Component;
import uk.gov.dwp.jsa.jsaps.model.ba.Jobseeker;
import uk.gov.dwp.jsa.jsaps.model.ba.P45;
import uk.gov.dwp.jsa.jsaps.model.ba.Tax;

@Component
public class BaMapperTax extends Mapper {


    public void processTax(final Jobseeker jobseeker) {
        Tax tax = new Tax();
        tax.setP187Completed(NO);
        P45 p45 = new P45();
        p45.setExpected(YES);
        p45.setSupplied(NO);
        tax.setP45(p45);
        tax.setContinuingEmployment(NO);
        jobseeker.setTax(tax);
    }
}
