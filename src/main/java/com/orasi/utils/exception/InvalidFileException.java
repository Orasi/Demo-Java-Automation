package com.orasi.utils.exception;

import com.orasi.AutomationException;

public class InvalidFileException extends AutomationException {
    /**
     *
     */
    private static final long serialVersionUID = 1861535540217015795L;

    public InvalidFileException(String message) {
        super(message);
    }
}
