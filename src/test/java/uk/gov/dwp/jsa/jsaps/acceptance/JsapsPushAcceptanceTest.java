package uk.gov.dwp.jsa.jsaps.acceptance;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.extension.responsetemplating.ResponseTemplateTransformer;
import com.github.tomakehurst.wiremock.junit.WireMockClassRule;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import uk.gov.dwp.jsa.adaptors.dto.jsaps.JsapsRequest;
import uk.gov.dwp.jsa.jsaps.Application;
import uk.gov.dwp.jsa.jsaps.mapper.JsapsRequestFactory;
import uk.gov.dwp.jsa.jsaps.model.es.Response;
import uk.gov.dwp.jsa.jsaps.model.es.ResponseHeaderType;
import uk.gov.dwp.jsa.jsaps.model.es.Result;
import uk.gov.dwp.jsa.jsaps.util.WiremockUtils;
import uk.gov.dwp.jsa.security.WithMockUser;
import uk.gov.dwp.jsa.security.roles.Role;

import java.util.Arrays;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.containing;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static io.netty.handler.codec.http.HttpHeaders.Values.APPLICATION_JSON;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@ContextConfiguration(classes = {Application.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("local_test")
public class JsapsPushAcceptanceTest {

    private static final String OPERATOR_GRADE = "01";
    private static final String BENEFIT_NUMBER = "62";

    private static final String HOME_OFFICE_ID = "207621";
    private static final String TARGET_OFFICE_ID = "123456";


    private static final String URL_POST_ES = "/jsaps/v1/claim/d41948d0-02ec-4c25-98d5-ffa6dace1db4";
    private static final int STUB_LOWER_PRIORITY = 2;
    private static final String JWT_OPERATOR_HARDCODE = "12345678";
    @ClassRule
    public static WireMockClassRule wireMockRule
            = new WireMockClassRule(
            options()
                    .port(WiremockUtils.WIREMOCK_PORT)
                    .extensions(new ResponseTemplateTransformer(true)));
    @Rule
    public WireMockClassRule wiremockServer = wireMockRule;
    @Rule
    public WireMockClassRule cis;
    private JsapsRequestFactory jsapsRequestFactory = new JsapsRequestFactory();
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    private JsapsRequest employmentAndSupportRequest;


    @Before
    public void setup() throws JsonProcessingException {
        employmentAndSupportRequest = jsapsRequestFactory.createJsapsRequest();
        stubWiremockPxP_ES_Response();
        stubWiremockPxP_BA_Response();
    }

    @WithMockUser(role = Role.CCA, staffNumber = "12345678")
    @Test
    public void givenJsapsRequestWhenPostThenReceive201() throws Exception {
        whenPostThenReceive201();
    }


    @WithMockUser(role = Role.CCA)
    @Test
    public void givenJsapsRequestMissingClaimantWhenPostThenReceive400() throws Exception {
        givenJsapsRequestMissingClaimant();
        whenPostThenReceive400();
    }

    @WithMockUser(role = Role.CCA)
    @Test
    public void givenJsapsRequestMissingCircumstancesWhenPostThenReceive400() throws Exception {
        givenJsapsRequestMissingCircumstances();
        whenPostThenReceive400();
    }

    @WithMockUser(role = Role.CCA)
    @Test
    public void givenJsapsRequestMissingAgentModeWhenPostThenReceive400() throws Exception {
        givenJsapsRequestMissingAgentMode();
        whenPostThenReceive400();
    }

    @WithMockUser(role = Role.CCA)
    @Test
    public void givenJsapsRequestMissinBankDetailsWhenPostThenReceive400() throws Exception {
        givenJsapsRequestMissingAgentMode();
        whenPostThenReceive400();
    }

    @WithMockUser(role = Role.CCA, staffNumber = "")
    @Test
    public void givenJsapsRequestHasAnyKindOfErrorWhenPostThenReceive400() throws Exception {
        //StaffNumber is empty
        whenPostThenReceive400();
    }

    private void givenJsapsRequestMissingAgentMode() {
        employmentAndSupportRequest.setAgentMode(null);
    }


    private void givenJsapsRequestMissingClaimant() {
        employmentAndSupportRequest.setClaimant(null);
    }

    private void givenJsapsRequestMissingCircumstances() {
        employmentAndSupportRequest.setCircumstances(null);
    }


    private void stubWiremockPxP_ES_Response() throws JsonProcessingException {
        Response response = createResponse_201();
        wiremockServer.stubFor(post(urlPathMatching("/pxp/jsaps_es")).atPriority(STUB_LOWER_PRIORITY)
                .withHeader("operator", containing(JWT_OPERATOR_HARDCODE))
                .withHeader("homeOfficeId", containing(HOME_OFFICE_ID))
                .withHeader("targetOfficeId", containing(TARGET_OFFICE_ID))
                .withHeader("benefitNumber",containing( BENEFIT_NUMBER))
                .withHeader( "operatorGrade", containing(OPERATOR_GRADE))
                .willReturn(aResponse()
                        .withStatus(201)
                        .withHeader("Content-Type", APPLICATION_JSON)
                        .withBody(toJson(response))));
    }

    private Response createResponse_201() {
        Response response = new Response();
        ResponseHeaderType responseHeader = new ResponseHeaderType();
        Result responseHeaderResult = new Result();
        responseHeaderResult.setCompletionIndicator("true");
        responseHeaderResult.setMessages(createListMessages_es());
        responseHeader.setResult(responseHeaderResult);
        response.setResponseHeader(responseHeader);
        response.setResponseBody("LALALA");
        return response;
    }


    private Response createResponse_400() {
        Response response = new Response();
        ResponseHeaderType responseHeader = new ResponseHeaderType();
        Result responseHeaderResult = new Result();
        responseHeaderResult.setSystemErrors(createListSystemErrors_es());
        responseHeaderResult.setMessages(createListMessages_es());
        responseHeaderResult.setCompletionIndicator("false");
        responseHeader.setResult(responseHeaderResult);
        response.setResponseHeader(responseHeader);
        response.setResponseBody("LALALA");
        return response;
    }


    private void stubWiremockPxP_BA_Response() throws JsonProcessingException {
        Response response = createResponse_201();
        wiremockServer.stubFor(post(urlPathMatching("/pxp/jsaps_ba")).atPriority(STUB_LOWER_PRIORITY)
                .withHeader("operator", containing(JWT_OPERATOR_HARDCODE))
                .withHeader("homeOfficeId", containing(HOME_OFFICE_ID))
                .withHeader("targetOfficeId", containing(TARGET_OFFICE_ID))
                .withHeader("benefitNumber",containing( BENEFIT_NUMBER))
                .withHeader( "operatorGrade", containing(OPERATOR_GRADE))
                .willReturn(aResponse()
                        .withStatus(201)
                        .withHeader("Content-Type", APPLICATION_JSON)
                        .withBody(toJson(response))));
    }



    private void whenPostThenReceive400() throws Exception {
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post(URL_POST_ES)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(employmentAndSupportRequest)))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
    }

    private void whenPostThenReceive201() throws Exception {
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post(URL_POST_ES)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(employmentAndSupportRequest)))
                .andExpect(content().string(containsString(URL_POST_ES)))
                .andExpect(status().is(HttpStatus.CREATED.value()));
    }

    private void whenPostThenReceive400WithMessages() throws Exception {
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post(URL_POST_ES)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(employmentAndSupportRequest)))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                .andExpect(content().string("{\"error\":[{\"code\":\"default-error-code\",\"message\":\"da SystemError\"},{\"code\":\"default-error-code\",\"message\":\"daValue jsaps_es\"}]}"))
        ;
    }


    private <T> String toJson(T objectToConverted) throws JsonProcessingException {
        return mapper.writeValueAsString(objectToConverted);
    }


    private uk.gov.dwp.jsa.jsaps.model.es.SystemErrors createListSystemErrors_es(){
        uk.gov.dwp.jsa.jsaps.model.es.SystemErrors systemErrors_es = new uk.gov.dwp.jsa.jsaps.model.es.SystemErrors();
//        SystemeErrors
        systemErrors_es.setSystemError(Arrays.asList("da SystemError"));
        return systemErrors_es;
    }

    private uk.gov.dwp.jsa.jsaps.model.es.Messages createListMessages_es(){
        uk.gov.dwp.jsa.jsaps.model.es.Messages messages_es = new uk.gov.dwp.jsa.jsaps.model.es.Messages();
        uk.gov.dwp.jsa.jsaps.model.es.Message message = new uk.gov.dwp.jsa.jsaps.model.es.Message();
        message.setScreen("daScreen");
        message.setValue("daValue jsaps_es");
        messages_es.setMessage(Arrays.asList(message));
        return messages_es;
    }
}
