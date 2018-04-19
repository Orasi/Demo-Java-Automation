package com.orasi.web.exceptions;

import com.orasi.web.WebException;

public class NullDriverException extends WebException {
    private static final long serialVersionUID = 3407361723082329697L;

    public NullDriverException() {
        super("Driver was null");
    }
}