package com.orasi.exception.automation;

import com.orasi.exception.AutomationException;
import com.orasi.utils.OrasiDriver;

public class ElementAttributeValueNotMatchingException extends AutomationException{
	private static final long serialVersionUID = 3407361723082329697L;
	
	
	public ElementAttributeValueNotMatchingException(String message){
		super(message );
	}
	
	public ElementAttributeValueNotMatchingException(String message, OrasiDriver driver){
		super(message, driver );
	}
}
