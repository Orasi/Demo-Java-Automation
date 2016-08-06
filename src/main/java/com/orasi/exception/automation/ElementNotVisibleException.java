package com.orasi.exception.automation;

import com.orasi.exception.AutomationException;
import com.orasi.utils.OrasiDriver;

public class ElementNotVisibleException extends AutomationException{
    private static final long serialVersionUID = 7724792038612608062L;

    public ElementNotVisibleException(String message){
	super(message );
    }

    public ElementNotVisibleException(String message, OrasiDriver driver){
	super(message, driver );
    }
}
