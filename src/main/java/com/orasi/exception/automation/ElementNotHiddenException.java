package com.orasi.exception.automation;

import com.orasi.exception.AutomationException;
import com.orasi.utils.OrasiDriver;

public class ElementNotHiddenException extends AutomationException{

    private static final long serialVersionUID = 1865273000586352087L;

    public ElementNotHiddenException(String message){
	super(message );
    }

    public ElementNotHiddenException(String message, OrasiDriver driver){
	super(message, driver );
    }
}
