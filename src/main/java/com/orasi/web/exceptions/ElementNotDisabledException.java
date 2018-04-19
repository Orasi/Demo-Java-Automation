package com.orasi.web.exceptions;

import com.orasi.web.OrasiDriver;
import com.orasi.web.WebException;

public class ElementNotDisabledException extends WebException {
    private static final long serialVersionUID = 624614577584686540L;

    public ElementNotDisabledException(String message) {
        super(message);
    }

    public ElementNotDisabledException(String message, OrasiDriver driver) {
        super(message, driver);
    }
}
