package by.nestegg.lending.exception;

public class TokenValidationException extends RuntimeException {

    public TokenValidationException() {
    }

    public TokenValidationException(String message) {
        super(message);
    }

}
