package com.orasi.exception.automation;

import com.orasi.exception.AutomationException;

public class NoKeyFoundException extends AutomationException {
    /**
     * 
     */
    private static final long serialVersionUID = 1861535540217015795L;

    public NoKeyFoundException(String message) {
	super(message);
    }
}
