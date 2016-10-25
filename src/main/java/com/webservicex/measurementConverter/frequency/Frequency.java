package com.webservicex.measurementConverter.frequency;

import com.orasi.api.soapServices.core.SoapService;

public class Frequency extends SoapService {
	public Frequency() {
		setServiceName("FrequencyUnitSoap");
		setServiceURL("http://www.webservicex.net/convertFrequency.asmx?WSDL");
	}
}
