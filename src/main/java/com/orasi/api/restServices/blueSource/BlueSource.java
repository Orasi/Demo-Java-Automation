package com.orasi.api.restServices.blueSource;


import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.testng.Assert;

import com.orasi.api.restServices.blueSource.Paths;
import com.orasi.api.restServices.blueSource.employees.EmployeeDetails;
import com.orasi.api.restServices.blueSource.employees.Employees;
import com.orasi.api.restServices.blueSource.titles.Titles;
import com.orasi.api.restServices.core.RestService;

public class BlueSource extends RestService{
    final protected String authenticity_token = "DP+4EpvLCfTS+y4JwU7qXWlhobeGIaXx6Cm0IVzxaR4=";
    protected String sessionCookie = "_bluesource_session=Tml5Q3JvL0Jxc2hxZ2JQQm15Y1VoU2tlZkRtRXFib0ZFaGllNXUyL0hYRWpsMDh1TFpMSUlIaUxEUlVlam8rWW9YZUVxeDVWenlpVXdkY3Mybzc0UGswV0VDT0VXVGZIOHJtbUpQdW5kWm9jeTdQR3JBWWxiemJscE42ZWtKSkF0Q3RUNG1IQjlRTlhPbEZSTXZnUGY1UXhVZzZJeFIxVklRa095aTltWDB5Tko3TmlTZ0lzTlpURUpJcWxIaFoxMmI4dkJ3UWpVMklzS0FVZHNXRXk2aGJOZndiWHZPNEQ0UVZmdjNaQ0taclVMbUtOalErL09vYUFnY09EUmsraDdwenBEbjVrMmVYdWx6dmk3VnE3V1dHUW16aXpUSEFZNDh6MCttTnVLMzg9LS1ncFUyVnJGcTNiVFdCdkFsNjJiWnZ3PT0%3D--dda9c50791f3614568823d3c7c0f2c8448dbc624";
    private Employees employees = null;
    private Titles titles= null;
    public String getSessionCookie(){ 
	return sessionCookie;
    }
	
    protected void setSessionCookie(HttpContext httpContext){
	super.httpContext = httpContext;
    }
    
    public BlueSource(){
    }
    
    public BlueSource(String username){
	sendPost(Paths.saml_login, generateLoginParams(username));
	employees = new Employees(httpContext);
	titles = new Titles(httpContext);
    }
    
    public Employees employees(){
	return employees;
    }
    
    public Titles titles(){
	return titles;
    }
    
    @SuppressWarnings("unchecked")
    protected <T> T mapJSON(String data, Class clazz){
	T t= null;
	try {
	    t = (T) new RestService().mapJSONToObject(data, clazz);
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	return t;
    }
    
    @SuppressWarnings("unchecked")
    protected <T> T mapJSONTree(String data){
	T t= null;
	try {
	    t = (T) new RestService().mapJSONToTree(data);
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	return t;
    }
    
    protected String sendGet(String url){
	//send in the get request
	Header[] headers =  {
		    new BasicHeader("Connection", "keep-alive")
		    ,new BasicHeader("Content-type", "application/json")
		    ,new BasicHeader("Cache-Control", "max-age=0")
		};
	
	try {
	    sendGetRequest(url,headers);
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	
	//verify request comes back as 200 ok
	Assert.assertEquals(getStatusCode(), HttpStatus.SC_OK);
	
	return responseAsString;
    }
    
    protected String sendPatch(String url, List<NameValuePair> params){
	Header[] headers =  {
		    new BasicHeader("Content-type", "application/x-www-form-urlencoded")
		    ,new BasicHeader("Accept", "application/json,text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
		    ,new BasicHeader("Connection", "keep-alive")
		    ,new BasicHeader("Cache-Control", "max-age=255")
		    ,new BasicHeader("Cookie", getSessionCookie().replace("; path=/; HttpOnly", ""))
		};
	//send in the Post request
	try {
	    sendPatchRequest(url,headers, params);
	} catch (ClientProtocolException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	
	return responseAsString;
    }
    
    protected String sendPut(String url, List<NameValuePair> params){
   	Header[] headers =  {
   		    new BasicHeader("Content-type", "application/x-www-form-urlencoded")
   		    ,new BasicHeader("Accept", "application/json,text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
   		    ,new BasicHeader("Connection", "keep-alive")
   		    ,new BasicHeader("Cache-Control", "max-age=255")
   		    ,new BasicHeader("Cookie", getSessionCookie().replace("; path=/; HttpOnly", ""))
   		};
   	//send in the Post request
   	try {
   	    sendPutRequest(url,headers, params);
   	} catch (ClientProtocolException e) {
   	    // TODO Auto-generated catch block
   	    e.printStackTrace();
   	} catch (IOException e) {
   	    // TODO Auto-generated catch block
   	    e.printStackTrace();
   	}
   	//setSessionCookie(getResponseCookie());
   	//System.out.println(responseAsString);
   	
   	return responseAsString;
       }
    
    protected String sendPost(String url, List<NameValuePair> params){
   	//RestService restService = new RestService();
   	Header[] headers =  {
   		    new BasicHeader("Content-type", "application/x-www-form-urlencoded")
   		    ,new BasicHeader("Accept", "application/json,text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
   		    ,new BasicHeader("Connection", "keep-alive")
   		    ,new BasicHeader("Cache-Control", "max-age=255")
   		    ,new BasicHeader("Cookie", getSessionCookie().replace("; path=/; HttpOnly", ""))
   		};
   	//send in the Post request
   	try {
   	    sendPostRequest(url,headers, params);
   	} catch (ClientProtocolException e) {
   	    // TODO Auto-generated catch block
   	    e.printStackTrace();
   	} catch (IOException e) {
   	    // TODO Auto-generated catch block
   	    e.printStackTrace();
   	}
   	return responseAsString;
       }
    
    protected String sendDelete(String url, List<NameValuePair> params){
   	//RestService restService = new RestService();
   	Header[] headers =  {
   		    new BasicHeader("Content-type", "application/x-www-form-urlencoded")
   		    ,new BasicHeader("Accept", "application/json,text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
   		    ,new BasicHeader("Connection", "keep-alive")
   		    ,new BasicHeader("Cache-Control", "max-age=255")
   		    ,new BasicHeader("Cookie", getSessionCookie().replace("; path=/; HttpOnly", ""))
   		};
   	//send in the Post request
   	URI uri = null;
	try {
	    uri = new URI(url);
	} catch (URISyntaxException e1) {
	    // TODO Auto-generated catch block
	    e1.printStackTrace();
	}
	URIBuilder builder = new URIBuilder();
	builder.setScheme(uri.getScheme()).setHost(uri.getHost()).setPath(uri.getPath()).addParameters(params);
	try {
	   uri =builder.build();
	} catch (URISyntaxException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
   	try {
   	    sendDeleteRequest(uri,headers);
   	} catch (ClientProtocolException e) {
   	    // TODO Auto-generated catch block
   	    e.printStackTrace();
   	} catch (IOException e) {
   	    // TODO Auto-generated catch block
   	    e.printStackTrace();
   	}
   	return responseAsString;
       }

    private List<NameValuePair> generateLoginParams(String username){ 
	List<NameValuePair> params = new ArrayList<NameValuePair>();
	params.add(new BasicNameValuePair("authenticity_token", this.authenticity_token));
	params.add(new BasicNameValuePair("employee[username]", username));
	return params;
    }
}
