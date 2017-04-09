package com.orasi.api.soapServices.exceptions;

import com.orasi.api.WebServiceException;

public class HeaderNotFoundException extends WebServiceException{
	private static final long serialVersionUID = -8710980695994382082L;

	public HeaderNotFoundException(String message){
		super(message);
	}
	
	public HeaderNotFoundException(String message, Throwable cause){
		super(message, cause);
	}
}
