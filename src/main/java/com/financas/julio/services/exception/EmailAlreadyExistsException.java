package com.financas.julio.services.exception;

public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException(String email) {
        super("E-mail already registered: " + email);
    }
}
