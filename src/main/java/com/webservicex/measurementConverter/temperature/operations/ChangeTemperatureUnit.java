package com.webservicex.measurementConverter.temperature.operations;

import com.orasi.utils.XMLTools;
import com.webservicex.measurementConverter.temperature.ConvertTemp;

public class ChangeTemperatureUnit extends ConvertTemp{
	
	
	public ChangeTemperatureUnit(String scenario) {
		setServiceName("ConvertTemperatureSoap");
		setRequestDocument(XMLTools.loadXML(buildRequestFromWSDL("ConvertTemp")));
		setRequestNodeValueByXPath(getTestScenario("/excelsheets/tempConvert.xls", scenario));
		removeComments();
		removeWhiteSpace();
	}
	
	public void setTemperature(String value){
		setRequestNodeValueByXPath("/Envelope/Body/ConvertTemp/Temperature", value); 

	}
	
	public String getResult() {
		return getResponseNodeValueByXPath("/Envelope/Body/ConvertTempResponse/ConvertTempResult");
	}

}