package com.webservicex.measurementConverter.temperature;
import com.orasi.api.soapServices.core.SoapService;

public class ConvertTemp extends SoapService{
	
	public ConvertTemp() {
		setServiceName("ConvertTemperatureSoap");
		setServiceURL("http://www.webservicex.net/ConvertTemperature.asmx?WSDL");
	}

}