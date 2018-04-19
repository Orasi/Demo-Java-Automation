package com.orasi.web.exceptions;

import com.orasi.web.OrasiDriver;
import com.orasi.web.WebException;

public class SelectElementNotFoundException extends WebException {
    private static final long serialVersionUID = 3407361723082329697L;

    public SelectElementNotFoundException(String message) {
        super(message);
    }

    public SelectElementNotFoundException(String message, OrasiDriver driver) {
        super(message, driver);
    }
}
