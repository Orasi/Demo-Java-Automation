package com.orasi.web.exceptions;

import com.orasi.web.OrasiDriver;
import com.orasi.web.WebException;

public class ElementNotHiddenException extends WebException {

    private static final long serialVersionUID = 1865273000586352087L;

    public ElementNotHiddenException(String message) {
        super(message);
    }

    public ElementNotHiddenException(String message, OrasiDriver driver) {
        super(message, driver);
    }
}
