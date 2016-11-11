package com.webservicex.measurementConverter.frequency.operations;

import java.io.File;

import com.orasi.utils.Constants;
import com.orasi.utils.XMLTools;
import com.webservicex.measurementConverter.frequency.Frequency;

public class ChangeFrequencyUnit extends Frequency{
	
	public ChangeFrequencyUnit(String scenario) {
		File xml = new File(this.getClass().getResource(Constants.XML_FILES + "/frequencyUnitSoap/changeFrequencyUnit/changeFrequencyUnit.xml").getPath());
		setRequestDocument(XMLTools.makeXMLDocument(xml));
		setOperationName("ChangeFrequencyUnit");
		//setRequestDocument(XMLTools.loadXML(buildRequestFromWSDL("ChangeFrequencyUnit")));
		setRequestNodeValueByXPath(getTestScenario(Constants.EXCEL_SHEETS + "/freqConvert.xls", scenario));
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
