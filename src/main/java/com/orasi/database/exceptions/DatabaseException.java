package com.orasi.database.exceptions;

public class DatabaseException extends RuntimeException {
    private static final long serialVersionUID = -2738970905210023091L;

    public DatabaseException() {
        super();
    }

    public DatabaseException(String message) {
        super(message);
    }

    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }

}