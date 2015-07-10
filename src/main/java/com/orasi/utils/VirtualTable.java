package com.orasi.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.orasi.utils.database.Recordset;

public class VirtualTable {
	private enum BaseURL{
		DEV{ public String toString() {return "";}},
		TEST{ public String toString() {return "";}},
		PROD{ public String toString() {return "";}}
	}
	
	private enum Rows{
		GET_ALL_ROWS{ public String toString() {return "/public/gdo/row/{tableName}";}},
		GET_ROW{ public String toString() {return "/public/gdo/row/{tableName}/{firstRowNum}/{displayNumber}/{searchColumnName}/{searchColumnValue}";}}		
	}
	
	private final String baseURL = BaseURL.DEV.toString();

	private String sendGetRequest(String strUrl, String responseFormat){

		StringBuilder rawResponse = new StringBuilder();

		URL urlRequest = null;

		try {
			urlRequest = new URL(strUrl);
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		HttpURLConnection conn = null;
		try {
			conn = (HttpURLConnection) urlRequest
					.openConnection();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		conn.setDoOutput(true);
		conn.setDoInput(true);
		conn.setRequestProperty("Accept", "application/" + responseFormat);
		try {
			conn.setRequestMethod("GET");
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		InputStream stream = null;
		try {
			stream = conn.getInputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BufferedReader bufferReader = new BufferedReader(new InputStreamReader(
				stream));

		String buffer = "";
		try {
			while ((buffer = bufferReader.readLine()) != null) {
				rawResponse.append(buffer);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return rawResponse.toString();
	}

	public String getAllRows(String tableName) {
		String url = baseURL + Rows.GET_ALL_ROWS;
		String response = "";

		url = url.replace("{tableName}", tableName);
		response = sendGetRequest(url, "json");

		return response;
	}
	
	public String getAllTestRows(String tableName) {
		String url = baseURL + Rows.GET_ALL_ROWS;
		String response = "";

		url = url.replace("{tableName}", tableName);
		response = sendGetRequest(url, "xml");

		return response;
	}
	
	public String getRow(String tableName, String searchColumnName, String searchColumnValue) {
		String url = baseURL + Rows.GET_ROW;
		String response = "";
		url = url.replace("{tableName}", tableName);
		url = url.replace("{firstRowNum}", "0");
		url = url.replace("{displayNumber}", "1");
		url = url.replace("{searchColumnName}", searchColumnName);
		url = url.replace("{searchColumnValue}", searchColumnValue);
		url = url.replace(" ", "%20");
		url = url.replace("'", "%27");
		response = sendGetRequest(url, "json");
		
		return response;
	}
	
	
	public String getRows(String tableName, String searchColumnName, String searchColumnValue) {
		String url = baseURL + Rows.GET_ROW;
		String response = "";
		url = url.replace("{tableName}", tableName);
		url = url.replace("{firstRowNum}", "0");
		url = url.replace("{displayNumber}", "10000");
		url = url.replace("{searchColumnName}", searchColumnName);
		url = url.replace("{searchColumnValue}", searchColumnValue);
		url = url.replace(" ", "%20");
		url = url.replace("'", "%27");
		response = sendGetRequest(url, "json");
		
		return response;
	}
	
	public static Recordset compileJSON(String header, String text){
		JSONObject jHeader;
		try {
			jHeader= new JSONObject(text).getJSONObject(header + "List");
		} catch (JSONException e) {
			throw new RuntimeException("Failed to compile JSON. " + e.getMessage());
		}
		
		try {
			String error = jHeader.getJSONObject(header).get("FROM_CONTROLLER").toString();
			throw new RuntimeException("Error returned from server: " + error);
		} catch (JSONException e) {
			/*
			 * Do nothing. No errors returned from server
			 */
		}
		
		JSONArray jArray = new JSONArray();
		JSONObject jObject = new JSONObject();
		String JSONType = "";
		
		try {
			jArray =  jHeader.getJSONArray(header);
			JSONType = "ARRAY";
		} catch (JSONException e) {
			try {
				jObject = jHeader.getJSONObject(header);
				JSONType = "OBJECT";
				/*Iterator x = jObj.getJSONObject(header + "List").getJSONObject(header).keys();
				while (x.hasNext()){
					String key = (String) x.next();
					jArray.put( jObj.getJSONObject(header + "List").getJSONObject(header).get(key));
				}*/
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		String[][] array = null;
		if (JSONType.equals("OBJECT")){
			jObject.remove("ID");
			array = new String[2][jObject.names().length()];
			for (int columns = 0 ; columns < array[0].length ; columns++){
				try {
					array[0][columns] = jObject.names().get(columns).toString();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			for (int rows = 0 ; rows <  array[0].length ; rows++){
				try {
					array[1][rows] = jObject.get(array[0][rows]).toString();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}else if(JSONType.equals("ARRAY")){
			try {
				jArray.getJSONObject(0).remove("ID");
				array = new String[jArray.length()+1][jArray.getJSONObject(0).names().length()];
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			for (int columns = 0 ; columns < array[0].length ; columns++){
				try {
					array[0][columns] = jArray.getJSONObject(0).names().get(columns).toString();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			for(int arrays = 0 ; arrays < jArray.length() ; arrays++){							
				for (int rows = 0 ; rows <  array[0].length ; rows++){
					try {
						array[arrays+1][rows] = jArray.getJSONObject(arrays).get(array[0][rows]).toString();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		return new Recordset(array);
	}
	
	public static Recordset compileXML(String header, String xml){
		 DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		  DocumentBuilder builder = null;
		try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  InputSource is = new InputSource( new StringReader( xml) );
		  Document doc = null;
		try {
			doc = builder.parse( is );
		} catch (SAXException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		  XPathFactory xFactory = XPathFactory.newInstance();
		  XPath xpath = xFactory.newXPath();
		  XPathExpression expr = null;
		try {
			expr = xpath.compile("/" + header + "List/"+header);
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		  Object result = null;
		try {
			result = expr.evaluate(doc, XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}			
		
		  NodeList nodes = (NodeList) result;
		  String[][] array =  new String[nodes.getLength()+1][nodes.item(0).getChildNodes().getLength()-1];
		  for (int columns = 0 ; columns < array[0].length  ; columns++){				
				array[0][columns] = nodes.item(0).getChildNodes().item(columns+1).getNodeName();				
			}
		  
		  for(int arrays = 0 ; arrays < nodes.getLength() ; arrays++){							
				for (int rows = 0 ; rows <  array[0].length ; rows++){
					array[arrays+1][rows] = nodes.item(arrays).getChildNodes().item(rows+1).getTextContent();
				}
			}
		  return new Recordset(array);
	}
}
