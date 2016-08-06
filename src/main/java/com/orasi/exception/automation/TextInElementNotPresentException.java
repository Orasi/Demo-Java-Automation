package com.orasi.exception.automation;

import com.orasi.exception.AutomationException;
import com.orasi.utils.OrasiDriver;

public class TextInElementNotPresentException extends AutomationException{
	private static final long serialVersionUID = 3407361723082329697L;
	
	
	public TextInElementNotPresentException(String message){
		super(message );
	}
	
	public TextInElementNotPresentException(String message, OrasiDriver driver){
		super(message, driver );
	}
}
