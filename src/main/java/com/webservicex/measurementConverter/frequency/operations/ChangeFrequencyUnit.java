package com.webservicex.measurementConverter.frequency.operations;

import com.orasi.utils.XMLTools;
import com.webservicex.measurementConverter.frequency.Frequency;

public class ChangeFrequencyUnit extends Frequency{
	
	public ChangeFrequencyUnit(String scenario) {
		setOperationName("ChangeFrequencyUnit");
		setRequestDocument(XMLTools.loadXML(buildRequestFromWSDL("ChangeFrequencyUnit")));
		setRequestNodeValueByXPath(getTestScenario("/excelsheets/freqConvert.xls", scenario));
		removeComments();
		removeWhiteSpace();
	}
	
	public void setFreqValue(String value){
		setRequestNodeValueByXPath("/Envelope/Body/ChangeFrequencyUnit/FrequencyValue", value);
	}
	
	public String getResult() {
	    return getResponseNodeValueByXPath("/Envelope/Body/ChangeFrequencyUnitResponse/ChangeFrequencyUnitResult");
	}

}
