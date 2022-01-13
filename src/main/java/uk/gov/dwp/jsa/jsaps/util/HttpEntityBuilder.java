package uk.gov.dwp.jsa.jsaps.util;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import uk.gov.dwp.jsa.jsaps.exception.BadRequestException;

import java.util.Collections;
import java.util.UUID;

public class HttpEntityBuilder {

    public static final String OPERATOR_GRADE = "01";
    public static final String BENEFIT_NUMBER = "62";

    private static final int OPERATOR_LENGTH = 8;
    private static final int HOMEOFFICE_LENGTH = 6;
    private static final int TARGETOFFICE_LENGTH = 6;

    private String sessionId;
    private String operator;
    private String homeOfficeId;
    private String targetOfficeId;
    private Object request;


    public HttpEntityBuilder(final String sessionId, final String operator, final String homeOfficeId,
                             final String targetOfficeId) {
        this.sessionId = sessionId;
        this.operator = operator;
        this.homeOfficeId = homeOfficeId;
        this.targetOfficeId = targetOfficeId;
    }

    public HttpEntityBuilder withRequest(final Object request) {
        this.request = request;
        return this;
    }

    public HttpEntity build() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        if (operator == null || operator.length() != OPERATOR_LENGTH) {
            throw new BadRequestException("Error on header.operator, value should have length 8");
        }
        if (homeOfficeId == null || homeOfficeId.length() != HOMEOFFICE_LENGTH) {
            throw new BadRequestException("Error on header.homeOffice, value should have length 6");
        }
        if (targetOfficeId == null || targetOfficeId.length() != TARGETOFFICE_LENGTH) {
            throw new BadRequestException("Error on header.targetOffice, value should have length 6");
        }
        headers.set("sessionId", sessionId);
        headers.set("operator", operator);
        headers.set("homeOfficeId", homeOfficeId);
        headers.set("targetOfficeId", targetOfficeId);
        headers.set("benefitNumber", BENEFIT_NUMBER);
        headers.set("operatorGrade", OPERATOR_GRADE);
        headers.set("correlationId", UUID.randomUUID().toString());


        return new HttpEntity<>(request, headers);
    }
}
