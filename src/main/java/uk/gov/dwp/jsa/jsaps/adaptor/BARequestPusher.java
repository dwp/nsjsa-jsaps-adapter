package uk.gov.dwp.jsa.jsaps.adaptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import uk.gov.dwp.jsa.adaptors.dto.jsaps.JsapsRequest;
import uk.gov.dwp.jsa.jsaps.client.JsapsClient;
import uk.gov.dwp.jsa.jsaps.model.ba.Request;
import uk.gov.dwp.jsa.jsaps.model.ba.Response;
import uk.gov.dwp.jsa.jsaps.model.ba.Result;

import java.util.UUID;

import static uk.gov.dwp.jsa.security.roles.Role.getStaffNumber;

@Component
public class BARequestPusher {
    private static final String DEFAULT_OPERATOR = "12345678";

    private final JsapsClient client;
    private final BARequestFactory baRequestFactory;
    private final String baPath;

    @Autowired
    public BARequestPusher(final JsapsClient client,
                           final BARequestFactory baRequestFactory,
                           @Value("${jsaps.api.path.ba.post}") final String pBaPath) {
        this.client = client;
        this.baRequestFactory = baRequestFactory;
        this.baPath = pBaPath;
    }

    public Result push(final UUID claimantId, final JsapsRequest jsapsRequest) {
        final String operator = getStaffNumber().orElse(DEFAULT_OPERATOR);
        final Request request = baRequestFactory.create(jsapsRequest);
        final Response result = client.postBA(
                baPath,
                claimantId,
                operator,
                jsapsRequest.getHomeOfficeId(),
                jsapsRequest.getTargetOfficeId(),
                request,
                Response.class);
        return result.getResponseHeader().getResult();
    }

}
