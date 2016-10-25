package com.webservicex.locationSearch.usZip;

import com.orasi.api.soapServices.core.SoapService;

public class USZip extends SoapService{

	public USZip() {
	    setServiceName("USZipSoap");
	    setServiceURL("http://www.webservicex.net/uszip.asmx?wsdl");
	}
}
