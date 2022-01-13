package uk.gov.dwp.jsa.jsaps.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.dwp.jsa.adaptors.dto.jsaps.JsapsRequest;
import uk.gov.dwp.jsa.adaptors.http.api.ApiMultiErrorResponse;
import uk.gov.dwp.jsa.adaptors.http.api.ApiSuccess;
import uk.gov.dwp.jsa.jsaps.service.JsapsResponseBuilder;
import uk.gov.dwp.jsa.jsaps.service.JsapsService;
import uk.gov.dwp.jsa.jsaps.service.JsapsServiceResult;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/jsaps/{version}")
public class JsapsController {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsapsController.class);

    private JsapsService jsapsService;


    @Autowired
    public JsapsController(final JsapsService jsapsService) {
        this.jsapsService = jsapsService;
    }

    @PreAuthorize("hasAnyAuthority('WC', 'CCM')")
    @PostMapping("/claim/{claimantId}")
    public ResponseEntity<ApiMultiErrorResponse<String>> pushClaim(
            final HttpServletRequest request,
            @PathVariable final UUID claimantId,
            @RequestBody @Validated final JsapsRequest jsapsRequest) {
        LOGGER.debug("pushClaim: request ClaimantId: {} ", claimantId);
        ResponseEntity<ApiMultiErrorResponse<String>> response;
        JsapsServiceResult jsapsServiceResult = jsapsService.pushToJsaps(claimantId, jsapsRequest);
        if (jsapsServiceResult.getSuccess()) {
            List<ApiSuccess<String>> apiSucessData = new ArrayList<>();
            for (String message : jsapsServiceResult.getMessages()) {
                apiSucessData.add(new ApiSuccess<>(URI.create(request.getRequestURI()), message));
            }
            response = buildSuccessfulResponse(
                    request.getRequestURI(),
                    apiSucessData,
                    HttpStatus.CREATED);
        } else {
            LOGGER.warn("pushClaim: Error during push to jsaps, request ClaimantId: {} ", claimantId);
            for (String message : jsapsServiceResult.getMessages()) {
                LOGGER.warn(message);
            }
            response = buildFailResponse();
        }
        return response;
    }


    private <T> ResponseEntity<ApiMultiErrorResponse<T>> buildSuccessfulResponse(
            final String path,
            final List<ApiSuccess<T>> successData,
            final HttpStatus status) {
        return new JsapsResponseBuilder<T>()
                .withStatus(status)
                .withSuccessData(successData)
                .build();
    }

    private <T> ResponseEntity<ApiMultiErrorResponse<T>> buildFailResponse() {
        return new JsapsResponseBuilder<T>()
                .withStatus(HttpStatus.BAD_REQUEST)
                .build();
    }
}
