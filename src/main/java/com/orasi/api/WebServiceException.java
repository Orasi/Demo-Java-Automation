package com.orasi.api;

public class WebServiceException  extends RuntimeException{
	private static final long serialVersionUID = -8710980695994382082L;

	public WebServiceException(){
		super();
	}
	
	public WebServiceException(String message){
		super( message);
	}
	

	
	public WebServiceException(String message, Throwable cause){
		super(message, cause);
	}
	
}

