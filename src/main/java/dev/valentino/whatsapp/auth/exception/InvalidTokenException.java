package dev.valentino.whatsapp.auth.exception;

import javax.security.sasl.AuthenticationException;

public class InvalidTokenException extends AuthenticationException {

    public InvalidTokenException() {
        super("Invalid token");
    }
}
