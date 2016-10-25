package com.orasi.api.restServices.core;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.orasi.utils.Randomness;
import com.orasi.utils.TestReporter;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;


public class Headers {
	//private Header[] headers ;
	/**
	 * Used in coordination with RestService.createHeader() <br/>
	 * Enums: <br/>
	 * BASIC_CONVO:<br/> 
	 * 			Content-type: application/json;charset=utf-8 <br/>
	 * 			Accept: application/json <br/>
	 * 			username: test116.user <br/>
	 *     		messageId: Random Alphanumic string <br/>
	 *     		Connection: keep-alive <br/>
	 *     		ConversationId:  Random Alphanumic string  <br/>
	 *     		requestedTimestamp: Current timestamp<br/><br/>
	 * REST :<br/>
	 * 			Authorization: BEARER and generated token<br/>    
	 *
	 */
	public static enum HeaderType {
	  	AUTH,
	    BASIC_CONVO,
	    JSON;
	}
	/*  private Header[] getHeaders(){
		  return headers;
	  }*/
	  
	  /**
	   * Automatically populates headers based on predefined options from RestService.HeaderType
	   * @param type Uses options from RestService.HeaderType enum
	   */
	  public static Header[]  createHeader(HeaderType type)  {
		  Header[] headers = null;
	        switch(type) {		    
		        case AUTH:		        	
        			TestReporter.logDebug("Creating headers for [AUTH]");

		        	headers= new Header[] {
		    	   		     new BasicHeader("Content-type", "application/x-www-form-urlencoded")};
		        	break;
		        case BASIC_CONVO:
        			TestReporter.logDebug("Creating headers for [BASIC_CONVO]");
		        	headers = new Header[] {
		    	   		     new BasicHeader("Content-type", "application/json;charset=utf-8")
		    	   		    ,new BasicHeader("Accept", "application/json")
		    	   		    ,new BasicHeader("username", "test.user")
		    	   		    ,new BasicHeader("messageId", Randomness.generateMessageId())
		    	   		    ,new BasicHeader("Connection", "keep-alive")
		    	   		    ,new BasicHeader("requestedTimestamp", Randomness.generateCurrentXMLDatetime() + ".000-04:00")
		    	   		};
		        	break;   
		        case JSON:
	        			TestReporter.logDebug("Creating headers for [JSON]");
			        	headers = new Header[] {
			    	   		     new BasicHeader("Content-type", "application/json")
			    	   		    
			    	   		};
			        	break;
	            default:
	                break;
	        }
	        
	        //Logging headers
	        String allHeaders = "";
	    	for (Header header : headers){
	    		allHeaders += "[" +header.getName() + ": " + header.getValue()+"] ";
	    	}
			TestReporter.logDebug("Headers added " + allHeaders);
			
			return headers;
	    }
}