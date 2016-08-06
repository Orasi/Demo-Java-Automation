package com.orasi.exception.automation;

import com.orasi.exception.AutomationException;
import com.orasi.utils.OrasiDriver;

public class ElementNotDisabledException extends AutomationException{
    private static final long serialVersionUID = 624614577584686540L;

    public ElementNotDisabledException(String message){
	super(message );
    }

    public ElementNotDisabledException(String message, OrasiDriver driver){
	super(message, driver );
    }
}
