package com.webservicex.genericBarCode;

import com.orasi.api.soapServices.SoapService;

public class GenericBarCode extends SoapService{

	public GenericBarCode() {
	    setServiceName("GenerateBarCode");
	    setServiceURL("http://www.webservicex.net/genericbarcode.asmx?WSDL");
	}
}
