package com.orasi.web.exceptions;

import com.orasi.web.OrasiDriver;
import com.orasi.web.WebException;

public class ElementNotVisibleException extends WebException {
    private static final long serialVersionUID = 7724792038612608062L;

    public ElementNotVisibleException(String message) {
        super(message);
    }

    public ElementNotVisibleException(String message, OrasiDriver driver) {
        super(message, driver);
    }
}
