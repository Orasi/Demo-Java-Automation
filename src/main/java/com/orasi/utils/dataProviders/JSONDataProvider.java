package com.orasi.utils.dataProviders;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONDataProvider {
    public static Object[][] compileJSON(String header, String text){
		
	JSONArray jArray = new JSONArray();
	JSONObject jObject = new JSONObject();
	String JSONType = "";
	
	//Determine if dealing with a JSON object or array 
	try {
		jArray =  new JSONArray(text);
		JSONType = "ARRAY";
	} catch (JSONException e) {
		try {
			jObject = new JSONObject(text);
			JSONType = "OBJECT";
			
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	//Handle JSON based on if it is an array or object
	String[][] array = null;
	if (JSONType.equals("OBJECT")){
		array = new String[2][jObject.names().length()];
		//Push column headers to array
		for (int columns = 0 ; columns < array[0].length ; columns++){
			try {
				array[0][columns] = jObject.names().get(columns).toString();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//push data to array
		for (int rows = 0 ; rows <  array[0].length ; rows++){
			try {
				array[1][rows] = jObject.get(array[0][rows]).toString();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}else if(JSONType.equals("ARRAY")){
	    	//Pull out column headers
		try {
			array = new String[jArray.length()+1][jArray.getJSONObject(0).names().length()];
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//Push column headers to array
		for (int columns = 0 ; columns < array[0].length ; columns++){
			try {
				array[0][columns] = jArray.getJSONObject(0).names().get(columns).toString();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//Push data to array
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
	return array;
    }
}
