package com.orasi.web.exceptions;

import com.orasi.web.OrasiDriver;
import com.orasi.web.WebException;

public class ElementCssValueNotMatchingException extends WebException {
    private static final long serialVersionUID = 3407361723082329697L;

    public ElementCssValueNotMatchingException(String message) {
        super(message);
    }

    public ElementCssValueNotMatchingException(String message, OrasiDriver driver) {
        super(message, driver);
    }
}
