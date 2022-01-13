package uk.gov.dwp.jsa.jsaps.adaptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.dwp.jsa.adaptors.dto.jsaps.JsapsRequest;
import uk.gov.dwp.jsa.jsaps.model.es.Result;

import java.util.UUID;

@Service
public class JsapsServiceAdaptor implements JsapsAdaptor {
    private final BARequestPusher baRequestPusher;
    private final ESRequestPusher esRequestPusher;


    @Autowired
    public JsapsServiceAdaptor(final BARequestPusher baRequestPusher,
                               final ESRequestPusher esRequestPusher) {
        this.baRequestPusher = baRequestPusher;
        this.esRequestPusher = esRequestPusher;
    }

    @Override
    public Result pushESClaim(final UUID claimantId, final JsapsRequest jsapsRequest) {
        return esRequestPusher.push(claimantId, jsapsRequest);
    }


    @Override
    public uk.gov.dwp.jsa.jsaps.model.ba.Result pushBAClaim(final UUID claimantId, final JsapsRequest jsapsRequest) {
        return baRequestPusher.push(claimantId, jsapsRequest);
    }

}
