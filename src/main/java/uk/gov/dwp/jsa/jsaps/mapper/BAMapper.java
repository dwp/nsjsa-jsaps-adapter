package uk.gov.dwp.jsa.jsaps.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.dwp.jsa.adaptors.dto.jsaps.JsapsRequest;
import uk.gov.dwp.jsa.jsaps.model.ba.Jobseeker;
import uk.gov.dwp.jsa.jsaps.model.ba.LinkingDetails;
import uk.gov.dwp.jsa.jsaps.util.date.JsapsDateFormatter;

@Component
public class BAMapper extends Mapper {

    private JsapsDateFormatter dateFormatter;
    private BaMapperWork baMapperWork;
    private BaMapperPension baMapperPension;
    private BaMapperClaimAdmin baMapperClaimAdmin;
    private BaMapperTax baMapperTax;
    private BaMapperBankDetails baMapperBankDetails;

    @Autowired
    public BAMapper(final JsapsDateFormatter dateFormatter,
                    final BaMapperWork baMapperWork,
                    final BaMapperPension baMapperPension,
                    final BaMapperClaimAdmin baMapperClaimAdmin,
                    final BaMapperTax baMapperTax, final BaMapperBankDetails baMapperBankDetails) {
        this.dateFormatter = dateFormatter;
        this.baMapperWork = baMapperWork;
        this.baMapperPension = baMapperPension;
        this.baMapperClaimAdmin = baMapperClaimAdmin;
        this.baMapperTax = baMapperTax;
        this.baMapperBankDetails = baMapperBankDetails;
    }

    public Jobseeker map(final JsapsRequest request) {
        Jobseeker jobseeker = new Jobseeker();
        String nino = request.getClaimant().getNino();
        jobseeker.setNino(nino);
        jobseeker.setContributoryEffectiveDate(
                dateFormatter.format(request.getCircumstances().getDateOfClaim()));
        baMapperWork.processCurrentWorkList(request, jobseeker);
        baMapperWork.processPreviousWorkList(request, jobseeker);
        baMapperPension.processOcupationalPension(request, jobseeker);
        processLinkingDetails(jobseeker);
        baMapperClaimAdmin.processClaimAdmin(request, jobseeker);
        baMapperTax.processTax(jobseeker);
        baMapperBankDetails.processBank(request, jobseeker);
        return jobseeker;
    }

    private void processLinkingDetails(final Jobseeker jobseeker) {
        LinkingDetails linkingDetails = new LinkingDetails();
        linkingDetails.setAwardMixedCredit(NO);
        jobseeker.setLinkingDetails(linkingDetails);
    }

}
