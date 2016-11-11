package com.webservicex.measurementConverter.temperature.operations;

import java.io.File;

import com.orasi.utils.Constants;
import com.orasi.utils.XMLTools;
import com.webservicex.measurementConverter.temperature.ConvertTemp;

public class ChangeTemperatureUnit extends ConvertTemp{
	
	
	public ChangeTemperatureUnit(String scenario) {
		File xml = new File(this.getClass().getResource(Constants.XML_FILES + "/convertTemperatureSoap/convertTemp/convertTemp.xml").getPath());
		setRequestDocument(XMLTools.makeXMLDocument(xml));
		setServiceName("ConvertTemperatureSoap");
		//setRequestDocument(XMLTools.loadXML(buildRequestFromWSDL("ConvertTemp")));
		setRequestNodeValueByXPath(getTestScenario(Constants.EXCEL_SHEETS + "/tempConvert.xls", scenario));
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