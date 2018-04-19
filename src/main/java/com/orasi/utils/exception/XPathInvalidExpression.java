package com.orasi.utils.exception;

import com.orasi.AutomationException;

public class XPathInvalidExpression extends AutomationException {
    private static final long serialVersionUID = 3407361723082329697L;

    public XPathInvalidExpression(String message) {
        super("XPath expression [ " + message + " ] was invalid");
    }

    public XPathInvalidExpression(String message, Throwable cause) {
        super("XPath expression [ " + message + " ] was invalid", cause);
    }
}
