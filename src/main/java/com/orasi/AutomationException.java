package com.orasi;

public class AutomationException extends RuntimeException {

    private static final long serialVersionUID = -8710980695994382082L;

    public AutomationException() {
        super("Automation Error");
    }

    public AutomationException(String message) {
        super("Automation Error: " + message);
    }

    public AutomationException(String message, Throwable cause) {
        super("Automation Error: " + message, cause);
    }

}
