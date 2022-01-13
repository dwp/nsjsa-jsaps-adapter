package uk.gov.dwp.jsa.jsaps.adaptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.dwp.jsa.adaptors.dto.jsaps.JsapsRequest;
import uk.gov.dwp.jsa.jsaps.mapper.ESMapper;
import uk.gov.dwp.jsa.jsaps.model.es.JobseekerType;
import uk.gov.dwp.jsa.jsaps.model.es.Request;
import uk.gov.dwp.jsa.jsaps.model.es.RequestBody;
import uk.gov.dwp.jsa.jsaps.model.es.RequestHeaderType;
import uk.gov.dwp.jsa.jsaps.util.HttpEntityBuilder;

import static uk.gov.dwp.jsa.security.roles.Role.getStaffNumber;

@Component
public class ESRequestFactory {

    private static final String DEFAULT_OPERATOR = "12345678";

    private final ESMapper esMapper;

    @Autowired
    public ESRequestFactory(final ESMapper esMapper) {
        this.esMapper = esMapper;
    }

    public Request create(final JsapsRequest jsapsRequest) {
        final JobseekerType jobseekerType = esMapper.map(jsapsRequest);
        final String operator = getStaffNumber().orElse(DEFAULT_OPERATOR);
        final RequestHeaderType requestHeader =
                new RequestHeaderType()
                        .operator(operator)
                        .operatorGrade(HttpEntityBuilder.OPERATOR_GRADE)
                        .benefitNumber(HttpEntityBuilder.BENEFIT_NUMBER)
                        .targetOfficeId(jsapsRequest.getTargetOfficeId())
                        .homeOfficeId(jsapsRequest.getHomeOfficeId());
        final RequestBody requestBody = new RequestBody().addJobseekerItem(jobseekerType);
        final Request request = new Request().requestHeader(requestHeader).requestBody(requestBody);
        return request;
    }

}
