package uk.gov.dwp.jsa.jsaps.util;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.HttpEntity;
import uk.gov.dwp.jsa.jsaps.exception.BadRequestException;

import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(JUnitParamsRunner.class)
public class HttpEntityBuilderTest {

    private static final String SESSION_ID = "d41948d0-02ec-4c25-98d5-ffa6dace1db4";
    private static final String OPERATOR = "12345678";
    private static final String HOME_OFFICE_ID = "123456";
    private static final String TARGET_OFFICE_ID = "123456";
    private static final String OPERATOR_GRADE = "01";
    private static final String BENEFIT_NUMBER = "62";


    @Test
    @Parameters({
            "12345678,123456,123456, true",
            "1234567,123456,123456, false",
            "12345678,12345,123456, false",
            "12345678,123456,12345, false",
            ",123456,123456, false",
            "12345678,,123456, false",
            "12345678,123456,, false"})
    public void testBuild(String operator, final String homeOfficeId, final String targetOfficeId, final Boolean httpEntityCreated ){
        HttpEntityBuilder builder = new HttpEntityBuilder(SESSION_ID, operator, homeOfficeId, targetOfficeId);
        HttpEntity result = null;
        try {
            result =  builder.build();
        }catch (BadRequestException e){
            result = null;
        }
        assertThat( result != null, is(httpEntityCreated));
    }


    @Test
    public void givenAllThreeValuesRightWhenBuildThenHeadersAreSet(){
        HttpEntityBuilder builder = new HttpEntityBuilder(SESSION_ID, OPERATOR, HOME_OFFICE_ID, TARGET_OFFICE_ID);
        HttpEntity result = builder.build();
        assertThat( result.getHeaders().get("operator").get(0), is(OPERATOR));
        assertThat( result.getHeaders().get("homeOfficeId").get(0), is(HOME_OFFICE_ID));
        assertThat( result.getHeaders().get("targetOfficeId").get(0), is(TARGET_OFFICE_ID));
        assertThat( result.getHeaders().get("benefitNumber").get(0), is(BENEFIT_NUMBER));
        assertThat( result.getHeaders().get("operatorGrade").get(0), is(OPERATOR_GRADE));
        assertThat( result.getHeaders().get("sessionId").get(0), is(SESSION_ID));
        assertThat( result.getHeaders().get("correlationId").get(0), is(notNullValue()));
    }
}
