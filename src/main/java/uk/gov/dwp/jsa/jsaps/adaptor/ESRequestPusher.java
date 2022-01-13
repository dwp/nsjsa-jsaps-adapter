package uk.gov.dwp.jsa.jsaps.adaptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import uk.gov.dwp.jsa.adaptors.dto.jsaps.JsapsRequest;
import uk.gov.dwp.jsa.jsaps.client.JsapsClient;
import uk.gov.dwp.jsa.jsaps.model.es.Request;
import uk.gov.dwp.jsa.jsaps.model.es.Response;
import uk.gov.dwp.jsa.jsaps.model.es.Result;

import java.util.UUID;

import static uk.gov.dwp.jsa.security.roles.Role.getStaffNumber;

@Component
public class ESRequestPusher {
    private static final String DEFAULT_OPERATOR = "12345678";

    private final JsapsClient client;
    private final ESRequestFactory esRequestFactory;
    private final String esPath;

    @Autowired
    public ESRequestPusher(final JsapsClient client,
                           final ESRequestFactory esRequestFactory,
                           @Value("${jsaps.api.path.es.post}") final String pEsPath) {
        this.client = client;
        this.esRequestFactory = esRequestFactory;
        this.esPath = pEsPath;
    }

    public Result push(final UUID claimantId, final JsapsRequest jsapsRequest) {
        final String operator = getStaffNumber().orElse(DEFAULT_OPERATOR);
        final Request request = esRequestFactory.create(jsapsRequest);
        final Response result = client.postES(esPath, claimantId,
                operator,
                jsapsRequest.getHomeOfficeId(),
                jsapsRequest.getTargetOfficeId(),
                request,
                Response.class);
        return result.getResponseHeader().getResult();
    }


}
