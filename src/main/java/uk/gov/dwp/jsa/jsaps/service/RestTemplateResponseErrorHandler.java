package uk.gov.dwp.jsa.jsaps.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;
import uk.gov.dwp.jsa.jsaps.exception.BadRequestException;
import uk.gov.dwp.jsa.jsaps.exception.PxPException;
import uk.gov.dwp.jsa.jsaps.model.es.ErrorResponse;
import uk.gov.dwp.jsa.jsaps.util.ObjectUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpStatus.Series.CLIENT_ERROR;
import static org.springframework.http.HttpStatus.Series.SERVER_ERROR;

public class RestTemplateResponseErrorHandler implements ResponseErrorHandler {




    @Override
    public boolean hasError(final ClientHttpResponse httpResponse) throws IOException {
        HttpStatus.Series series = httpResponse.getStatusCode().series();
        return (series == CLIENT_ERROR || series == SERVER_ERROR);
    }

    @Override
    public void handleError(final ClientHttpResponse httpResponse) throws IOException {
        HttpStatus.Series series = httpResponse.getStatusCode().series();
        if (series == HttpStatus.Series.CLIENT_ERROR) {
            ErrorResponse errorResponse = mapToObject(httpResponse);
            List<String> exceptionParameter = new ArrayList<>();
            ObjectUtils.resolve(() ->
                    errorResponse
                            .getResponseHeader()
                            .getResult()
                            .getSystemErrors().getSystemError())
                    .ifPresent(m -> m.stream()
                            .forEach(message -> exceptionParameter.add(message)));
            ObjectUtils.resolve(() ->
                    errorResponse
                            .getResponseHeader()
                            .getResult()
                            .getMessages().getMessage())
                    .ifPresent(m -> m.stream()
                            .forEach(message -> exceptionParameter.add(
                                    message.getValue() + " on screen " + message.getScreen())));

            throw new PxPException(exceptionParameter);
        } else if (series == HttpStatus.Series.SERVER_ERROR && httpResponse.getStatusCode().is5xxServerError()) {
            throw new BadRequestException();
        }
    }


    private ErrorResponse mapToObject(final ClientHttpResponse response) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(response.getBody(), ErrorResponse.class);
    }

}

