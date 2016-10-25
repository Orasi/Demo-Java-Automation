package com.orasi.api.restServices.core;

import java.io.IOException;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.orasi.api.WebServiceException;
import com.orasi.exception.AutomationException;
import com.orasi.utils.TestReporter;

public class RestResponse {
	private ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	private HttpUriRequest originalRequest = null;
	private HttpResponse response = null;
	private int statusCode = 0;
	private String responseFormat = "";
	private String responseAsString = "";
	private String serviceURL = "";
	
	public RestResponse(HttpResponse httpResponse){
		TestReporter.logTrace("Creating RestResponse based in HTTPResponse");
		response  = httpResponse;
		statusCode = response.getStatusLine().getStatusCode();
		responseFormat = ContentType.getOrDefault(response.getEntity()).getMimeType().replace("application/", "");
		try {
			responseAsString = EntityUtils.toString(response.getEntity());

			TestReporter.logDebug("Response Status returned [" + httpResponse.getStatusLine() +"]");
			TestReporter.logDebug("Response returned: " +responseAsString);
		} catch (ParseException | IOException e) {
			throw new AutomationException(e.getMessage(), e);
		}
	}
	
	public int getStatusCode(){ return statusCode; }	
	public String getResponseFormat(){ return responseFormat; }
	public String getResponse(){ return responseAsString; }
	public Header[] getHeaders(){ return response.getAllHeaders();}
	public String getServiceURL(){ return serviceURL; }
	public void setServiceURL(String url){ this.serviceURL = url; }

	
	/**
	 * Uses the class instance of the responeAsString to map to object
	 * @param clazz
	 * @return
	 * @throws IOException
	 */
	public <T> T mapJSONToObject(Class<T> clazz)  {
		return mapJSONToObject(responseAsString, clazz);
		
	}
	
	/**
	 * Can pass in any json as a string and map to object
	 * @param clazz
	 * @return
	 * @throws IOException
	 */
	public <T> T mapJSONToObject(String stringResponse, Class<T> clazz)  {
		T map = null;
		try {
			map =  mapper.readValue(stringResponse, clazz);
		} catch (JsonParseException e) {
			throw new WebServiceException("Failed to parse JSON", e);
		} catch (JsonMappingException e) {
			throw new WebServiceException("Failed to Map JSON", e);
		} catch (IOException e) {
			throw new WebServiceException("Failed to output JSON", e);
		}
		return map;
	}
	
	/**
	 * Can pass in any json as a string and maps to tree
	 * @param clazz
	 * @return
	 * @throws IOException
	 */
	public JsonNode mapJSONToTree(String stringResponse) {
				
		try {
			return mapper.readTree(stringResponse);
		} catch (IOException e) {
			throw new AutomationException("Failed to read response:" + stringResponse, e);
		}
	}
	
	/**
	 * Uses the class instance of the responeAsString to map to tree
	 * @param clazz
	 * @return
	 * @throws IOException
	 */
	public JsonNode mapJSONToTree() {
		return mapJSONToTree(responseAsString);
	}
}
