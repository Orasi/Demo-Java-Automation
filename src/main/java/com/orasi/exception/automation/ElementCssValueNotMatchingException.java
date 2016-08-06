package com.orasi.exception.automation;

import com.orasi.exception.AutomationException;
import com.orasi.utils.OrasiDriver;

public class ElementCssValueNotMatchingException extends AutomationException{
	private static final long serialVersionUID = 3407361723082329697L;
	
	
	public ElementCssValueNotMatchingException(String message){
		super(message );
	}
	
	public ElementCssValueNotMatchingException(String message, OrasiDriver driver){
		super(message, driver );
	}
}
