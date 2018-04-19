package com.orasi.web.exceptions;

import com.orasi.web.OrasiDriver;
import com.orasi.web.WebException;

public class OptionNotInListboxException extends WebException {

    private static final long serialVersionUID = 4926417034544326093L;

    public OptionNotInListboxException(String message) {
        super(message);
    }

    public OptionNotInListboxException(String message, OrasiDriver driver) {
        super(message, driver);
    }
}
