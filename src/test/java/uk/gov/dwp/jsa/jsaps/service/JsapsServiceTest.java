package uk.gov.dwp.jsa.jsaps.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.dwp.jsa.adaptors.dto.jsaps.JsapsRequest;
import uk.gov.dwp.jsa.jsaps.adaptor.JsapsServiceAdaptor;
import uk.gov.dwp.jsa.jsaps.model.ba.Messages;
import uk.gov.dwp.jsa.jsaps.model.ba.SystemErrors;

import java.util.Arrays;
import java.util.UUID;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class JsapsServiceTest {

    private JsapsService classToBeTested;

    private JsapsRequest jsapsRequest;

    private uk.gov.dwp.jsa.jsaps.model.ba.Result resultBA;

    private uk.gov.dwp.jsa.jsaps.model.ba.SystemErrors systemErrorsBa;

    private uk.gov.dwp.jsa.jsaps.model.es.Result resultES;

    private uk.gov.dwp.jsa.jsaps.model.es.SystemErrors systemErrorsEs;
    private static final UUID CLAIMANT_ID = UUID.randomUUID();

    @Mock
    private JsapsServiceAdaptor jsapsServiceAdaptor;
    @Mock
    private ESPushValidator esPushValidator;

    @Before
    public void setup(){
        resultBA = new uk.gov.dwp.jsa.jsaps.model.ba.Result();
        resultBA.setCompletionIndicator("True");
        resultBA.setMessages(createListMessages_ba());
        resultES = new uk.gov.dwp.jsa.jsaps.model.es.Result();
        resultES.setCompletionIndicator("True");
        resultES.setMessages(createListMessages_es());
        jsapsRequest = new JsapsRequest();
        classToBeTested = new JsapsService(jsapsServiceAdaptor, esPushValidator);
        systemErrorsBa = new SystemErrors();
        systemErrorsBa.addSystemErrorItem("This is a BA System Error");
        systemErrorsEs = new uk.gov.dwp.jsa.jsaps.model.es.SystemErrors();
        systemErrorsEs.addSystemErrorItem("This is an ES System Error");

    }

    @Test
    public void givenESPushFailed_ThenWeDontDoBAPush() {
        when(esPushValidator.isSuccessful(resultES)).thenReturn(false);
        when(jsapsServiceAdaptor.pushESClaim(CLAIMANT_ID, jsapsRequest)).thenReturn(resultES);
        resultES.setSystemErrors(systemErrorsEs);
        resultES.setMessages(null);
        JsapsServiceResult result = classToBeTested.pushToJsaps(CLAIMANT_ID, jsapsRequest);
        verify(jsapsServiceAdaptor).pushESClaim(CLAIMANT_ID, jsapsRequest);
        verify(jsapsServiceAdaptor, never()).pushBAClaim(any(UUID.class), any(JsapsRequest.class));
        assertTrue(result.getMessages().stream().anyMatch(message ->
                message.equals("System Error on ES Push: This is an ES System Error for claimantId " + CLAIMANT_ID)));
    }

    @Test
    public void givenBAPushFailed_ThenWeCheckSystemErrors() {
        when(esPushValidator.isSuccessful(resultES)).thenReturn(true);
        when(jsapsServiceAdaptor.pushESClaim(CLAIMANT_ID, jsapsRequest)).thenReturn(resultES);
        when(jsapsServiceAdaptor.pushBAClaim(CLAIMANT_ID, jsapsRequest)).thenReturn(resultBA);
        resultBA.setSystemErrors(systemErrorsBa);
        resultBA.setMessages(null);
        JsapsServiceResult result = classToBeTested.pushToJsaps(CLAIMANT_ID, jsapsRequest);
        verify(jsapsServiceAdaptor).pushESClaim(CLAIMANT_ID, jsapsRequest);
        verify(jsapsServiceAdaptor).pushBAClaim(CLAIMANT_ID, jsapsRequest);
        assertTrue(result.getMessages().stream().anyMatch(message ->
                message.equals("System Error on BA Push: This is a BA System Error for claimantId " + CLAIMANT_ID)));
    }

    @Test
    public void givenRequestWhenCallPushToJSapsThenAdaptorIsCalled(){
        when(esPushValidator.isSuccessful(resultES)).thenReturn(true);
        when(jsapsServiceAdaptor.pushESClaim(CLAIMANT_ID, jsapsRequest)).thenReturn(resultES);
        when(jsapsServiceAdaptor.pushBAClaim(CLAIMANT_ID, jsapsRequest)).thenReturn(resultBA);
        JsapsServiceResult result = classToBeTested.pushToJsaps(CLAIMANT_ID, jsapsRequest);
        verify(jsapsServiceAdaptor).pushESClaim(CLAIMANT_ID, jsapsRequest);
        verify(jsapsServiceAdaptor).pushBAClaim(CLAIMANT_ID, jsapsRequest);
        assertThat(result.getSuccess(),is(true));
        assertThat(result.getMessages().size(),is(2));
        assertThat(result.getMessages().get(0),is("daValue jsaps_es on screen daScreen for claimant: " + CLAIMANT_ID));
        assertThat(result.getMessages().get(1),is("daValue jsaps_ba on screen daScreen for claimant: " + CLAIMANT_ID));
    }

    private uk.gov.dwp.jsa.jsaps.model.es.Messages createListMessages_es(){
        uk.gov.dwp.jsa.jsaps.model.es.Messages messages_es = new uk.gov.dwp.jsa.jsaps.model.es.Messages();
        uk.gov.dwp.jsa.jsaps.model.es.Message message = new uk.gov.dwp.jsa.jsaps.model.es.Message();
        message.setScreen("daScreen");
        message.setValue("daValue jsaps_es");
        messages_es.setMessage(Arrays.asList(message));
        return messages_es;
    }


    private uk.gov.dwp.jsa.jsaps.model.ba.Messages createListMessages_ba(){
        uk.gov.dwp.jsa.jsaps.model.ba.Messages messages_ba = new uk.gov.dwp.jsa.jsaps.model.ba.Messages();
        uk.gov.dwp.jsa.jsaps.model.ba.Message message = new uk.gov.dwp.jsa.jsaps.model.ba.Message();
        message.setScreen("daScreen");
        message.setValue("daValue jsaps_ba");
        messages_ba.setMessage(Arrays.asList(message));
        return messages_ba;
    }
}
