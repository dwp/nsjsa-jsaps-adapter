package uk.gov.dwp.jsa.jsaps.exception;

public class BadRequestException extends RuntimeException {

    public BadRequestException(final String message) {
        super(message);
    }

    public  BadRequestException() {
    }
}
