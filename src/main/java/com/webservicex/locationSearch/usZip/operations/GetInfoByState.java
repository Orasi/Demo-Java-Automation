package com.webservicex.locationSearch.usZip.operations;

import com.orasi.utils.XMLTools;
import com.webservicex.locationSearch.usZip.USZip;

public class GetInfoByState extends USZip{
	public GetInfoByState() {
		
		//Generate a request from a project xml file
	    	setOperationName("GetInfoByState");
		setRequestDocument(XMLTools.loadXML(buildRequestFromWSDL("GetInfoByState")));
		removeComments() ;
		removeWhiteSpace();
	}	
	
	public void setState(String value){
	    setRequestNodeValueByXPath("/Envelope/Body/GetInfoByState/USState", value);
	}
	
	public int getNumberOfResults(){
	    return getNumberOfResponseNodesByXPath("/Envelope/Body/GetInfoByStateResponse/GetInfoByStateResult/NewDataSet/Table");
	}
}
