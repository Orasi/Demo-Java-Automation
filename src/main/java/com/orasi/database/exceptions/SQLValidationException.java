package com.orasi.database.exceptions;

public class SQLValidationException extends RuntimeException {
    private static final long serialVersionUID = -2738970905210023091L;

    public SQLValidationException() {
        super();
    }

    public SQLValidationException(String message) {
        super(message);
    }

    public SQLValidationException(String message, String sql) {
        super(message + ". SQL: " + sql);
    }

}