package com.webservicex.locationSearch.usZip.operations;

import com.orasi.utils.XMLTools;
import com.webservicex.locationSearch.usZip.USZip;

public class GetInfoByCity extends USZip{
	public GetInfoByCity() {
		
		//Generate a request from a project xml file
	    	setOperationName("GetInfoByCity");
		setRequestDocument(XMLTools.loadXML(buildRequestFromWSDL("GetInfoByCity")));
		removeComments() ;
		removeWhiteSpace();
	}	
	
	public void setCity(String value){
	    setRequestNodeValueByXPath("/Envelope/Body/GetInfoByCity/USCity", value);
	}
	
	public int getNumberOfResults(){
	    return getNumberOfResponseNodesByXPath("/Envelope/Body/GetInfoByCityResponse/GetInfoByCityResult/NewDataSet/Table");
	}
}
