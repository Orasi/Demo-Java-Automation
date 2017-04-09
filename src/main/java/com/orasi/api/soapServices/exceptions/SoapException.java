package com.orasi.api.soapServices.exceptions;

import com.orasi.api.WebServiceException;

public class SoapException extends WebServiceException{
	private static final long serialVersionUID = -8710980695994382082L;

	public SoapException(){
		super("SOAP Error:");
	}
	
	public SoapException(String message){
		super("SOAP Error: " +  message);
	}
	
	public SoapException(String message, Throwable cause){
		super("SOAP Error: " + message, cause);
	}
}
