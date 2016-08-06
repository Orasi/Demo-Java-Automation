package com.orasi.exception.automation;

import com.orasi.exception.AutomationException;

public class PageInitialization extends AutomationException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3407361723082329697L;

	public PageInitialization(){
		super();		
	}
	
	public PageInitialization(String message){
		super( message);
	}
	
	public PageInitialization(String message, Throwable cause){
		super(message, cause);
	}
}
