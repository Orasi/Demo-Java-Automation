package com.orasi.web.exceptions;

import com.orasi.web.OrasiDriver;
import com.orasi.web.WebException;

public class TextInElementNotPresentException extends WebException {
    private static final long serialVersionUID = 3407361723082329697L;

    public TextInElementNotPresentException(String message) {
        super(message);
    }

    public TextInElementNotPresentException(String message, OrasiDriver driver) {
        super(message, driver);
    }
}
