package com.orasi.exception.automation;

import com.orasi.exception.AutomationException;
import com.orasi.utils.OrasiDriver;

public class ElementNotEnabledException extends AutomationException{

    private static final long serialVersionUID = 6579447002670243452L;

    public ElementNotEnabledException(String message){
	super(message );
    }

    public ElementNotEnabledException(String message, OrasiDriver driver){
	super(message, driver );
    }
}
