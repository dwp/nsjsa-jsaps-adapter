package uk.gov.dwp.jsa.jsaps.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import uk.gov.dwp.jsa.adaptors.ServicesProperties;
import uk.gov.dwp.jsa.jsaps.model.ba.Messages;
import uk.gov.dwp.jsa.jsaps.model.es.Request;
import uk.gov.dwp.jsa.jsaps.model.es.Response;
import uk.gov.dwp.jsa.jsaps.util.HttpEntityBuilder;
import uk.gov.dwp.jsa.jsaps.util.ObjectUtils;

import java.util.UUID;

@Component
public class JsapsClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(JsapsClient.class);

    private static final String ES_PREFIX = "ES-";
    private static final String BA_PREFIX = "BA-";
    private final ServicesProperties servicesProperties;
    private final RestTemplate restTemplate;

    @Autowired
    public JsapsClient(final ServicesProperties servicesProperties, final RestTemplate restTemplate) {
        this.servicesProperties = servicesProperties;
        this.restTemplate = restTemplate;
    }

    public Response postES(final String url,
                           final UUID claimantId, final String operator,
                           final String homeOfficeId,
                           final String targetOfficeId,
                           final Request request,
                           final Class responseType) {
        LOGGER.warn("postES: ClaimantId: {}", claimantId);
        HttpEntity httpEntityWithHeaders = createHttpEntityWithHeaders(ES_PREFIX + claimantId, operator,
                homeOfficeId,
                targetOfficeId,
                request);

        ResponseEntity<Response> exchange = restTemplate.exchange(
                getUrl(url),
                HttpMethod.POST,
                httpEntityWithHeaders,
                responseType);
        ObjectUtils.resolve(() ->
                exchange.getBody().getResponseHeader().getResult().getMessages()).ifPresent(this::logMessagesFromES);
        return exchange.getBody();
    }

    public uk.gov.dwp.jsa.jsaps.model.ba.Response postBA(final String url,
                                                         final UUID claimantId, final String operator,
                                                         final String homeOfficeId,
                                                         final String targetOfficeId,
                                                         final uk.gov.dwp.jsa.jsaps.model.ba.Request request,
                                                         final Class responseType) {
        LOGGER.warn("postBA: ClaimantId: {}", claimantId);
        HttpEntity httpEntityWithHeaders = createHttpEntityWithHeaders(BA_PREFIX + claimantId, operator,
                homeOfficeId,
                targetOfficeId,
                request);

        ResponseEntity<uk.gov.dwp.jsa.jsaps.model.ba.Response> exchange = restTemplate.exchange(
                getUrl(url),
                HttpMethod.POST,
                httpEntityWithHeaders,
                responseType);
        ObjectUtils.resolve(() ->
                exchange.getBody().getResponseHeader().getResult().getMessages()).ifPresent(this::logMessagesFromBA);
        return exchange.getBody();
    }

    private void logMessagesFromBA(final Messages messages) {
        StringBuilder builder = new StringBuilder();
        if (messages != null && messages.getMessage() != null) {
            messages.getMessage().stream().forEachOrdered(message -> builder.append(
                    message.getValue() + " on screen " + message.getScreen() + " --"));
            LOGGER.warn("Messages received from BA: {}", builder);
        }
    }

    private void logMessagesFromES(final uk.gov.dwp.jsa.jsaps.model.es.Messages messages) {
        StringBuilder builder = new StringBuilder();
        if (messages != null && messages.getMessage() != null) {
            messages.getMessage().stream().forEachOrdered(message -> builder.append(
                    message.getValue() + " on screen " + message.getScreen() + " -- "));
            LOGGER.warn("Messages received from ES: {}", builder);
        }
    }

    private String getUrl(final String urlTemplate) {
        return servicesProperties.getJsapsServer() + urlTemplate;
    }

    private HttpEntity createHttpEntityWithHeaders(final String sessionId, final String operator,
                                                   final String homeOfficeId,
                                                   final String targetOfficeId,
                                                   final Object request) {
        return new HttpEntityBuilder(sessionId, operator, homeOfficeId, targetOfficeId).withRequest(request)
                .build();
    }

}
