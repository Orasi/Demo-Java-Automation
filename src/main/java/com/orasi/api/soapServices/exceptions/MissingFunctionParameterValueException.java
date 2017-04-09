package com.orasi.api.soapServices.exceptions;

import com.orasi.api.WebServiceException;

public class MissingFunctionParameterValueException extends WebServiceException{
	private static final long serialVersionUID = -8710980695994382082L;

	public MissingFunctionParameterValueException(String message){
		super(message);
	}
	
	public MissingFunctionParameterValueException(String message, Throwable cause){
		super(message, cause);
	}
}
