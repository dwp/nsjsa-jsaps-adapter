package uk.gov.dwp.jsa.jsaps.adaptor;

import uk.gov.dwp.jsa.adaptors.dto.jsaps.JsapsRequest;

import java.util.UUID;

public interface JsapsAdaptor {
    uk.gov.dwp.jsa.jsaps.model.es.Result pushESClaim(final UUID claimantId, final JsapsRequest jsapsRequest);

    uk.gov.dwp.jsa.jsaps.model.ba.Result pushBAClaim(final UUID claimantId, final JsapsRequest jsapsRequest);
}
