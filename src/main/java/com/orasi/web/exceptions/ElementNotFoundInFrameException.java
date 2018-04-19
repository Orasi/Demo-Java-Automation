package com.orasi.web.exceptions;

import com.orasi.web.OrasiDriver;
import com.orasi.web.WebException;

public class ElementNotFoundInFrameException extends WebException {
    private static final long serialVersionUID = 624614577584686540L;

    public ElementNotFoundInFrameException(String message) {
        super(message);
    }

    public ElementNotFoundInFrameException(String message, OrasiDriver driver) {
        super(message, driver);
    }
}
