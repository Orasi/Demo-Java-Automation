package com.orasi.exception.automation;

import com.orasi.exception.AutomationException;
import com.orasi.utils.OrasiDriver;

public class OptionNotInListboxException extends AutomationException{
	
    private static final long serialVersionUID = 4926417034544326093L;

	public OptionNotInListboxException(String message){
		super(message );
	}
	
	public OptionNotInListboxException(String message, OrasiDriver driver){
		super(message, driver );
	}
}
