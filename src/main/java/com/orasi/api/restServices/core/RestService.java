package com.orasi.api.restServices.core;

/**
 * Just playing around with some different ways of using rest services with Jackson 
 */
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.List;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.orasi.api.WebServiceException;
import com.orasi.api.restServices.core.Headers.HeaderType;
import com.orasi.utils.TestReporter;



public class RestService {	
	private HttpClient httpClient = null;
	
	//constructor
	public RestService() {
		TestReporter.logTrace("Initializing RestService");
		TestReporter.logTrace("Creating Http Client instance");
	    httpClient = HttpClientBuilder.create().build();
	}
	
	/**
	 * Sends a GET request to a URL
	 * 
	 * @param 	URL for the service you are testing
	 * @return 	response in string format
	 */
	public RestResponse sendGetRequest(String resource) {
	   	return sendGetRequest(resource, null, null);
	}
	
	/**
	 * Sends a GET request to a URL with predefined headers
	 * 
	 * @param 	resource for the service you are testing
	 * @param   HeaderType from {@link Headers.HeaderType}
	 * @return 	response in string format
	 */
	public RestResponse sendGetRequest(String resource, HeaderType type) {
		return sendGetRequest(resource, type, null);
	}

	/**
	 * Sends a GET request to a URL with predefined headers and set of URI parameters
	 * 
	 * @param 	resource for the service you are testing
	 * @param   type HeaderType Enum from {@link Headers.HeaderType}
	 * @param   params List of URL parameters to include
	 * @return 	response in string format
	 */
	public RestResponse sendGetRequest(String url, HeaderType type,  List<NameValuePair> params) {
		TestReporter.logTrace("Preparing to send GET request");
		TestReporter.logTrace("Creating Http GET instance with URL of ["+url+"]");
		HttpGet request = new HttpGet(url);
		
		try {
			if(params !=  null){
		    	String allParams= "";
		    	for (NameValuePair param : params){
		    		allParams += "[" +param.getName() + ": " + param.getValue()+"] ";
		    	}
				TestReporter.logDebug("Adding Parameters " + allParams);
				url = url+"?"+URLEncodedUtils.format(params, "utf-8");
				TestReporter.logDebug("URL with params: " + url);
				request.setURI(new URI(url));
		    }
			
		} catch (Exception e) {
			throw new WebServiceException(e.getMessage(),e);
		}

	    if(type != null) {
	    	request.setHeaders(Headers.createHeader(type));
	    }
		return sendRequest(request);
	}

	/**
	 * Sends a POST request to a URL with predefined headers and JSON
	 * 
	 * @param 	resource for the service you are testing
	 * @param   type HeaderType Enum from {@link Headers.HeaderType}
	 * @param   params List of URL parameters to include
	 * @return 	response in string format
	 */
	public RestResponse sendPostRequest(String url, HeaderType type, List<NameValuePair> params){
		return sendPostRequest(url, type, params, null);
	}
	
	/**
	 * Sends a POST request to a URL with predefined headers and JSON
	 * 
	 * @param 	resource for the service you are testing
	 * @param   type HeaderType Enum from {@link Headers.HeaderType}
	 * @param   json JSON as a string to send
	 * @return 	response in string format
	 */
	public RestResponse sendPostRequest(String url, HeaderType type, String body){
		return sendPostRequest(url, type, null, body);
	}

	public RestResponse sendPostRequest(String url, HttpEntity build) {
		TestReporter.logTrace("Preparing to send POST request");
		TestReporter.logTrace("Creating Http POST instance with URL of ["+url+"]");
		HttpPost httpPost = new HttpPost(url);
		httpPost.setEntity(build);
		return sendRequest(httpPost);
	}
	
	/**
	 * Sends a POST request to a URL with predefined headers and set of URI parameters and JSON
	 * 
	 * @param 	resource for the service you are testing
	 * @param   type HeaderType Enum from {@link Headers.HeaderType}
	 * @param   params List of URL parameters to include
	 * @param   json JSON as a string to send
	 * @return 	response in string format
	 */
	public RestResponse sendPostRequest(String url, HeaderType type, List<NameValuePair> params, String json){
		TestReporter.logTrace("Preparing to send POST request");
		TestReporter.logTrace("Creating Http POST instance with URL of ["+url+"]");
		HttpPost httpPost = new HttpPost(url);
		
		if(type != null)httpPost.setHeaders(Headers.createHeader(type));		
		if(params !=  null)	httpPost = (HttpPost) addParams(httpPost, params);
		if(json !=  null) httpPost = (HttpPost) addJson(httpPost, json);
		
		return sendRequest(httpPost);
	}

	/**
	 * Sends a PUT request to a URL with predefined headers and JSON
	 * 
	 * @param 	resource for the service you are testing
	 * @param   type HeaderType Enum from {@link Headers.HeaderType}
	 * @param   json JSON as a string to send
	 * @return 	response in string format
	 */
	public RestResponse sendPutRequest(String resource, HeaderType type, String json) {
		return sendPutRequest(resource, type, null, json);
	}
	
	/**
	 * Sends a PUT request to a URL with predefined headers and set of URI parameters
	 * 
	 * @param 	resource for the service you are testing
	 * @param   type HeaderType Enum from {@link Headers.HeaderType}
	 * @param   params List of URL parameters to include
	 * @return 	response in string format
	 */
	public RestResponse sendPutRequest(String url,HeaderType type, List<NameValuePair> params) {
		return sendPutRequest(url, type , params, null);
	}

	/**
	 * Sends a PUT request to a URL with predefined headers and set of URI parameters and JSON
	 * 
	 * @param 	resource for the service you are testing
	 * @param   type HeaderType Enum from {@link Headers.HeaderType}
	 * @param   params List of URL parameters to include
	 * @param   json JSON as a string to send
	 * @return 	response in string format
	 */
	public RestResponse sendPutRequest(String url, HeaderType type, List<NameValuePair> params, String json){
		TestReporter.logTrace("Preparing to send PUT request");
		TestReporter.logTrace("Creating Http PUT instance with URL of ["+url+"]");
		HttpPut httpPut = new HttpPut(url);
		
		if(type != null)httpPut.setHeaders(Headers.createHeader(type));		
		if(params !=  null)	httpPut = (HttpPut) addParams(httpPut, params);
		if(json !=  null) httpPut = (HttpPut) addJson(httpPut, json);
		
		return sendRequest(httpPut);
	}
	
	/**
	 * Sends a PATCH request to a URL with predefined headers and set of URI parameters
	 * 
	 * @param 	resource for the service you are testing
	 * @param   type HeaderType Enum from {@link Headers.HeaderType}
	 * @param   params List of URL parameters to include
	 * @return 	response in string format
	 */
	public RestResponse sendPatchRequest(String url, HeaderType type, List<NameValuePair> params){
	    return sendPatchRequest(url, type, params, null);
	}

	/**
	 * Sends a PATCH request to a URL with predefined headers and JSON
	 * 
	 * @param 	resource for the service you are testing
	 * @param   type HeaderType Enum from {@link Headers.HeaderType}
	 * @param   json JSON as a string to send
	 * @return 	response in string format
	 */
	public RestResponse sendPatchRequest(String url, HeaderType type, String json){
	    return sendPatchRequest(url,type, null, json);
	}

	/**
	 * Sends a PATCH request to a URL with predefined headers and set of URI parameters and JSON
	 * 
	 * @param 	resource for the service you are testing
	 * @param   type HeaderType Enum from {@link Headers.HeaderType}
	 * @param   params List of URL parameters to include
	 * @param   json JSON as a string to send
	 * @return 	response in string format
	 */
	public RestResponse sendPatchRequest(String url,HeaderType type,  List<NameValuePair> params, String json){
		TestReporter.logTrace("Preparing to send PATCH request");
		TestReporter.logTrace("Creating Http PATCH instance with URL of ["+url+"]");
		HttpPatch httpPatch = new HttpPatch(url);
		
		if(type != null)httpPatch.setHeaders(Headers.createHeader(type));		
		if(params !=  null)	httpPatch = (HttpPatch) addParams(httpPatch, params);
		if(json !=  null) httpPatch = (HttpPatch) addJson(httpPatch, json);
		
		return sendRequest(httpPatch);
	}
	/**
	 * Sends a delete request.  Depends on the service if a response is returned.
	 * If no response is returned, will return null	 * 
	 * 
	 * @param 	url
	 * @return 	response in string format or null
	 */
	
	public RestResponse sendDeleteRequest(String url,HeaderType type){
		TestReporter.logTrace("Preparing to send PATCH request");
		TestReporter.logTrace("Creating Http PATCH instance with URL of ["+url+"]");
		HttpDelete httpDelete = new HttpDelete(url);
		
		if(type != null){
	    	httpDelete.setHeaders(Headers.createHeader(type));
	    }
		
		return sendRequest(httpDelete);
	}
	
	public RestResponse sendDeleteRequest(String url ){
		return sendDeleteRequest(url, null); 
	}
	/**
	 * Sends an options request.  Options should give what the acceptable methods are for
	 * the service (GET, HEAD, PUT, POST, etc).  There should be some sort of an ALLOW 
	 * header that will give you the allowed methods.  May or may not be a body to the response, 
	 * depending on the service.  
	 * 
	 * This method will return all the headers and the test should parse through and find the header 
	 * it needs, that will give the allowed methods, as the naming convention will be different for each service.  
	 * 
	 * @param 	URL		for the service
	 * @return 	returns an array of headers
	 * @throws 	ClientProtocolException
	 * @throws 	IOException
	 */
	public Header[] sendOptionsRequest(String url ) {
		HttpOptions httpOptions=new HttpOptions(url);
		return sendRequest(httpOptions).getHeaders();
	}

	public static String getJsonFromObject(Object request){
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(request);
		} catch (JsonProcessingException e) {
			throw new WebServiceException("Failed to convert object to json");
		}
	}
	
	public RestResponse sendRequest(HttpUriRequest request){
		RestResponse response = null;
		try {
			TestReporter.logTrace("Sending request");
			response = new RestResponse(httpClient.execute(request));
		} catch (IOException e) {
			e.printStackTrace();
		}
		TestReporter.logTrace("Setting URI used on RestResponse");		
		response.setServiceURL(request.getURI().toString());
		TestReporter.logTrace("Returning RestResponse to calling method");
		return response;
	}
	
	private HttpUriRequest addParams(HttpUriRequest request, List<NameValuePair> params){
		String allParams= "";
    	for (NameValuePair param : params){
    		allParams += "[" +param.getName() + ": " + param.getValue()+"] ";
    	}
		TestReporter.logDebug("Adding Parameters " + allParams);
			try {
				if(request instanceof HttpPatch) ((HttpPatch) request).setEntity(new UrlEncodedFormEntity(params));
				else if(request instanceof HttpPut) ((HttpPut) request).setEntity(new UrlEncodedFormEntity(params));
				else if(request instanceof HttpPost) ((HttpPost) request).setEntity(new UrlEncodedFormEntity(params));
			} catch (UnsupportedEncodingException e) {
				throw new WebServiceException("Failed to add parameters to REST request", e.getCause());
			}
		return request;
	}
	

	private HttpUriRequest addJson(HttpUriRequest request, String json){
		TestReporter.logDebug("Adding json [" + json + "]");
			try {
				if(request instanceof HttpPatch) ((HttpPatch) request).setEntity( new ByteArrayEntity(json.getBytes("UTF-8")));
				else if(request instanceof HttpPut) ((HttpPut) request).setEntity( new ByteArrayEntity(json.getBytes("UTF-8")));
				else if(request instanceof HttpPost) ((HttpPost) request).setEntity( new ByteArrayEntity(json.getBytes("UTF-8")));
			} catch (UnsupportedEncodingException e) {
				throw new WebServiceException("Failed to add JSON to REST request", e.getCause());
			}
		return request;
	}

}