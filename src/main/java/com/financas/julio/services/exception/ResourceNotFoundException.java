package com.financas.julio.services.exception;

public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException(Long id) {
        super("Resource Not Found with id: " + id);
    }
}
