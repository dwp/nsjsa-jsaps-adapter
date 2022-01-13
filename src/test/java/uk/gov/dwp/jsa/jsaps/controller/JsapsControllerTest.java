package uk.gov.dwp.jsa.jsaps.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.gov.dwp.jsa.adaptors.dto.claim.Claimant;
import uk.gov.dwp.jsa.adaptors.dto.claim.circumstances.Circumstances;
import uk.gov.dwp.jsa.adaptors.dto.jsaps.JsapsRequest;
import uk.gov.dwp.jsa.adaptors.http.api.ApiMultiErrorResponse;
import uk.gov.dwp.jsa.jsaps.service.JsapsService;
import uk.gov.dwp.jsa.jsaps.service.JsapsServiceResult;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.UUID;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class JsapsControllerTest {


    private static final UUID CLAIMANT_ID = UUID.fromString("d41948d0-02ec-4c25-98d5-ffa6dace1db4");
    private static final String ES_POST_URL = "/jsaps/v1/claim/d41948d0-02ec-4c25-98d5-ffa6dace1db4";

    private static final Claimant CLAIMANT = new Claimant();
    private static final Circumstances CIRCUMSTANCES = new Circumstances();
    private JsapsRequest employmentAndSupportRequest;
    private JsapsController classToBeTested;

    @Mock
    private HttpServletRequest httpServletRequest;

    @Mock
    private JsapsService jsapsService;

    private ResponseEntity<ApiMultiErrorResponse<String>> response;

    private JsapsServiceResult jsapsServiceResult;


    @Before
    public void setup() {
        when(httpServletRequest.getRequestURI()).thenReturn(ES_POST_URL);
        jsapsServiceResult = new JsapsServiceResult();
        jsapsServiceResult.setSuccess(true);
        jsapsServiceResult.setMessages(Arrays.asList("E1010: Bad Stuff On Screen 34",
                "E1011: Bad Stuff On Screen 35",
                "E1012: Bad Stuff On Screen 36"));
        employmentAndSupportRequest = new JsapsRequest();
        when(jsapsService.pushToJsaps(CLAIMANT_ID, employmentAndSupportRequest)).thenReturn(jsapsServiceResult);
        classToBeTested = new JsapsController(jsapsService);
    }


    @Test
    public void givenESRequestWhenPostForESThenReceive201() {
        givenESRequest();
        whenPostForES();
        thenReceive201();
    }

    private void givenESRequest() {
        employmentAndSupportRequest.setClaimant(CLAIMANT);
        employmentAndSupportRequest.setCircumstances(CIRCUMSTANCES);
    }

    private void whenPostForES() {
        response = classToBeTested.pushClaim(httpServletRequest, CLAIMANT_ID, employmentAndSupportRequest);
    }

    private void thenReceive201() {
        assertThat(response.getStatusCode(), is(HttpStatus.CREATED));
        assertThat(response.getBody().getSuccess().get(0).getData(), is("E1010: Bad Stuff On Screen 34"));
        assertThat(response.getBody().getSuccess().get(0).getPath().getPath(), is(ES_POST_URL));
    }

}
