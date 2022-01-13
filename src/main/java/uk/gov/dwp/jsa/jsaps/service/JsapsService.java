package uk.gov.dwp.jsa.jsaps.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.dwp.jsa.adaptors.dto.jsaps.JsapsRequest;
import uk.gov.dwp.jsa.jsaps.adaptor.JsapsServiceAdaptor;
import uk.gov.dwp.jsa.jsaps.model.es.Message;
import uk.gov.dwp.jsa.jsaps.model.es.Messages;
import uk.gov.dwp.jsa.jsaps.model.es.Result;
import uk.gov.dwp.jsa.jsaps.model.es.SystemErrors;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class JsapsService {
    private static final Logger LOGGER = LoggerFactory.getLogger(JsapsService.class);
    private JsapsServiceAdaptor jsapsServiceAdaptor;
    private ESPushValidator esPushValidator;

    @Autowired
    public JsapsService(
            final JsapsServiceAdaptor jsapsServiceAdaptor,
            final ESPushValidator esPushValidator) {
        this.jsapsServiceAdaptor = jsapsServiceAdaptor;
        this.esPushValidator = esPushValidator;
    }

    public JsapsServiceResult pushToJsaps(final UUID claimantId, final JsapsRequest request) {
        JsapsServiceResult objectToReturn = new JsapsServiceResult();
        LOGGER.warn("Jsaps request for claimantID {}", claimantId);
        Result resultES = jsapsServiceAdaptor.pushESClaim(claimantId, request);
        List<String> messagesAsString = new ArrayList<>();
        Messages messages = resultES.getMessages();

        if (messages != null && messages.getMessage() != null) {
            for (Message m : messages.getMessage()) {
                messagesAsString.add(m.getValue() + " on screen "
                        + m.getScreen() + " for claimant: " + claimantId);
            }
        } else {
            messagesAsString.add("No ES messages for claimant: " + claimantId);
        }

        if (esPushValidator.isSuccessful(resultES)) {
            uk.gov.dwp.jsa.jsaps.model.ba.Result resultBA = jsapsServiceAdaptor.pushBAClaim(claimantId, request);
            uk.gov.dwp.jsa.jsaps.model.ba.Messages baMessages = resultBA.getMessages();
            if (baMessages != null && baMessages.getMessage() != null) {
                for (uk.gov.dwp.jsa.jsaps.model.ba.Message m : baMessages.getMessage()) {
                    messagesAsString.add(m.getValue() + " on screen "
                            + m.getScreen() + " for claimant: " + claimantId);
                }
            } else {
                messagesAsString.add("No BA messages for claimant: " + claimantId);
            }

            uk.gov.dwp.jsa.jsaps.model.ba.SystemErrors systemErrorsBa = resultBA.getSystemErrors();
            if (systemErrorsBa != null) {
                for (String s : systemErrorsBa.getSystemError()) {
                    messagesAsString.add("System Error on BA Push: " + s + " for claimantId " + claimantId);
                }
            }
            objectToReturn.setSuccess(true);
        } else {
          LOGGER.warn("ES Push unsuccessful for Claimant id: " + claimantId);
          SystemErrors systemErrorsEs = resultES.getSystemErrors();
          if (systemErrorsEs != null) {
            for (String s : systemErrorsEs.getSystemError()) {
                messagesAsString.add("System Error on ES Push: " + s + " for claimantId " + claimantId);
            }
          }
          objectToReturn.setSuccess(false);
        }
        objectToReturn.setMessages(messagesAsString);
        return objectToReturn;
    }
}
