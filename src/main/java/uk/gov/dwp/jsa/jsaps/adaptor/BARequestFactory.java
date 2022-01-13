package uk.gov.dwp.jsa.jsaps.adaptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.dwp.jsa.adaptors.dto.jsaps.JsapsRequest;
import uk.gov.dwp.jsa.jsaps.mapper.BAMapper;
import uk.gov.dwp.jsa.jsaps.model.ba.Jobseeker;
import uk.gov.dwp.jsa.jsaps.model.ba.Request;
import uk.gov.dwp.jsa.jsaps.model.ba.RequestBody;
import uk.gov.dwp.jsa.jsaps.model.ba.RequestHeaderType;
import uk.gov.dwp.jsa.jsaps.util.HttpEntityBuilder;

import static uk.gov.dwp.jsa.security.roles.Role.getStaffNumber;

@Component
public class BARequestFactory {

    private static final String DEFAULT_OPERATOR = "12345678";

    private final BAMapper baMapper;

    @Autowired
    public BARequestFactory(final BAMapper baMapper) {
        this.baMapper = baMapper;
    }

    public Request create(final JsapsRequest jsapsRequest) {
        final Jobseeker jobseeker = baMapper.map(jsapsRequest);
        final String operator = getStaffNumber().orElse(DEFAULT_OPERATOR);
        final RequestBody requestBody = new RequestBody().addJobseekerItem(jobseeker);
        final RequestHeaderType requestHeader = new RequestHeaderType()
                .operator(operator)
                .operatorGrade(HttpEntityBuilder.OPERATOR_GRADE)
                .benefitNumber(HttpEntityBuilder.BENEFIT_NUMBER)
                .targetOfficeId(jsapsRequest.getTargetOfficeId())
                .homeOfficeId(jsapsRequest.getHomeOfficeId());
        final Request request = new Request().requestBody(requestBody).requestHeader(requestHeader);
        return request;
    }

}
