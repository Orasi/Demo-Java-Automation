package com.orasi.exception.automation;

import com.orasi.exception.AutomationException;

public class ListboxOptionNotFoundException extends AutomationException {
	/**
     * 
     */
    private static final long serialVersionUID = -1985117339792858897L;

	public ListboxOptionNotFoundException(){
		super();		
	}
	
	public ListboxOptionNotFoundException(String message){
		super(message);
	}
	
	public ListboxOptionNotFoundException(String message, Throwable cause){
		super(message, cause);
	}
}
