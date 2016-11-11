package com.webservicex.locationSearch.usZip.operations;

import java.io.File;

import com.orasi.utils.Constants;
import com.orasi.utils.XMLTools;
import com.webservicex.locationSearch.usZip.USZip;

public class GetInfoByZip extends USZip{
	public GetInfoByZip() {
		File xml = new File(this.getClass().getResource(Constants.XML_FILES + "/usZipSoap/getInfoByZIP/getInfoByZip.xml").getPath());
		setRequestDocument(XMLTools.makeXMLDocument(xml));
		
		//Generate a request from a project xml file
	    	setOperationName("GetInfoByZIP");
		//setRequestDocument(XMLTools.loadXML(buildRequestFromWSDL("GetInfoByZIP")));
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
