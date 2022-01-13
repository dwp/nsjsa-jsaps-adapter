package uk.gov.dwp.jsa.jsaps.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.gov.dwp.jsa.adaptors.http.api.ApiError;
import uk.gov.dwp.jsa.adaptors.http.api.ApiSuccess;
import uk.gov.dwp.jsa.adaptors.http.api.ApiMultiErrorResponse;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class JsapsResponseBuilder<T> {

    private HttpStatus httpStatus;
    private List<ApiError> apiErrorData;
    private List<ApiSuccess<T>> apiSuccessData;

    public JsapsResponseBuilder<T> withStatus(final HttpStatus httpStatus) {
        Objects.requireNonNull(httpStatus);
        this.httpStatus = httpStatus;
        return this;
    }

    public JsapsResponseBuilder<T> withApiErrorData(final List<ApiError> apiErrorData) {
        Objects.requireNonNull(apiErrorData);
        this.apiErrorData = apiErrorData;
        return this;
    }


    public JsapsResponseBuilder<T> withSuccessData(final ApiSuccess<T> apiSuccess) {
        Objects.requireNonNull(apiSuccess);
        return withSuccessData(Collections.singletonList(apiSuccess));
    }

    public JsapsResponseBuilder<T> withSuccessData(final URI path, final T data) {
        Objects.requireNonNull(path);
        Objects.requireNonNull(data);
        return withSuccessData(Collections.singletonList(new ApiSuccess<>(path, data)));
    }

    public JsapsResponseBuilder<T> withSuccessData(final List<ApiSuccess<T>> apiSuccessData) {
        Objects.requireNonNull(apiSuccessData);
        this.apiSuccessData = apiSuccessData;
        return this;
    }

    public ResponseEntity<ApiMultiErrorResponse<T>> build() {
        Objects.requireNonNull(httpStatus);

        final ApiMultiErrorResponse<T> apiResponse = new ApiMultiErrorResponse<>();
        apiResponse.setError(apiErrorData);
        apiResponse.setSuccess(apiSuccessData);

        return ResponseEntity
                .status(httpStatus)
                .body(apiResponse);

    }
}
