package com.orasi.utils.exception;

import com.orasi.AutomationException;

public class XMLTransformException extends AutomationException {
    private static final long serialVersionUID = 3407361723082329697L;

    public XMLTransformException(String message) {
        super(message);
    }

    public XMLTransformException(String message, Throwable cause) {
        super(message, cause);
    }
}
