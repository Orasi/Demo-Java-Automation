package com.webservicex.locationSearch.usZip.operations;

import com.orasi.utils.XMLTools;
import com.webservicex.locationSearch.usZip.USZip;

public class GetInfoByZip extends USZip{
	public GetInfoByZip() {
		
		//Generate a request from a project xml file
	    	setOperationName("GetInfoByZIP");
		setRequestDocument(XMLTools.loadXML(buildRequestFromWSDL("GetInfoByZIP")));
		removeComments() ;
		removeWhiteSpace();
	}	
	
	public void setZip(String value){
	    setRequestNodeValueByXPath("/Envelope/Body/GetInfoByZIP/USZip", value);
	}
	
	public int getNumberOfResults(){
	    return getNumberOfResponseNodesByXPath("/Envelope/Body/GetInfoByZIPResponse/GetInfoByZIPResult/NewDataSet/Table");
	}
}
