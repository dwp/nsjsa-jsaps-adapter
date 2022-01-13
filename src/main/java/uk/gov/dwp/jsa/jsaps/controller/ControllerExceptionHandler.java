package uk.gov.dwp.jsa.jsaps.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import uk.gov.dwp.jsa.adaptors.http.api.ApiError;
import uk.gov.dwp.jsa.adaptors.http.api.ApiMultiErrorResponse;
import uk.gov.dwp.jsa.jsaps.exception.BadRequestException;
import uk.gov.dwp.jsa.jsaps.exception.PxPException;
import uk.gov.dwp.jsa.jsaps.service.JsapsResponseBuilder;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class ControllerExceptionHandler {

    private static final String DEFAULT_ERROR_CODE = "default-error-code";
    private static final Logger LOGGER = LoggerFactory.getLogger(ControllerExceptionHandler.class);

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public @ResponseBody
    ResponseEntity<ApiMultiErrorResponse<Object>> handleBadRequest(final BadRequestException bre) {
        LOGGER.error("Problem PUSHING to JSAPS received status {}, with message {}",
                HttpStatus.BAD_REQUEST,
                bre);
        return buildErrorResponse(
                HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(PxPException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public @ResponseBody
    ResponseEntity<ApiMultiErrorResponse<Object>> handlePxPException(
            final Exception ex,
            final HttpServletRequest request) {
        //FindBugs BC_UNCONFIRMED_CAST
        assert ex instanceof PxPException : ex.getClass();
        PxPException pxpException = (PxPException) ex;
        ResponseEntity<ApiMultiErrorResponse<Object>> responseEntity
                = buildErrorResponseForPxP(pxpException.getMessages());
        logPxPException(request, pxpException);
        return responseEntity;
    }

    private <T> ResponseEntity<ApiMultiErrorResponse<T>> buildErrorResponse(final HttpStatus status) {

        return new JsapsResponseBuilder<T>()
                .withStatus(status)
                .withApiErrorData(Arrays.asList(new ApiError(DEFAULT_ERROR_CODE, status.getReasonPhrase()))
                )
                .build();
    }

    private <T> ResponseEntity<ApiMultiErrorResponse<T>> buildErrorResponseForPxP(
            final List<String> listErrorMessages) {
        List<ApiError> errorMessages = listErrorMessages
                .parallelStream()
                .map(m -> new ApiError(DEFAULT_ERROR_CODE, m))
                .collect(Collectors.toList());
        return new JsapsResponseBuilder<T>()
                .withStatus(HttpStatus.BAD_REQUEST)
                .withApiErrorData(errorMessages)
                .build();
    }

    private void logPxPException(final HttpServletRequest request, final PxPException pxpException) {
        StringBuilder builder = new StringBuilder();
        pxpException.getMessages().stream().forEachOrdered(message -> builder.append(message + " -- "));
        LOGGER.error("Problem PUSHING to JSAPS received messages {}", builder);
    }

}
