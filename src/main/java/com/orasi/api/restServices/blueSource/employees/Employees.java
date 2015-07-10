package com.orasi.api.restServices.blueSource.employees;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.protocol.HttpContext;

import com.fasterxml.jackson.databind.JsonNode;
import com.orasi.api.restServices.blueSource.BlueSource;
import com.orasi.api.restServices.blueSource.Paths;

public class Employees extends BlueSource{
       
    public Employees(HttpContext httpContext){
	setSessionCookie(httpContext);
    }
    public int getNumberOfEmployees(){
	String url = Paths.employees + ".json";		
   	JsonNode allEmployees = mapJSONTree(sendGet(url));   	
   	return allEmployees.size();
   	
    }
    
    public EmployeeDetails getEmployeeDetails(int id){
 	String url = Paths.employee.replace(":id", String.valueOf(id)) + ".json";
 	return mapJSON(sendGet(url), EmployeeDetails.class);
     }
     
     public List<EmployeeDetails> getAllEmployees(){
   	String url = Paths.employees + ".json";
   	List<EmployeeDetails> employeeCollection = new ArrayList<EmployeeDetails>();
   		
   	JsonNode allEmployees = null;
   	String response = sendGet(url);
   	 allEmployees = mapJSONTree(response);
   	
   	
   	Iterator<JsonNode> nodeIterator = allEmployees.iterator();
   	EmployeeDetails employee = null;
	while (nodeIterator.hasNext()) {

	   JsonNode data = nodeIterator.next();
	   employee = mapJSON(data.toString(), EmployeeDetails.class);
	   employeeCollection.add(employee);
	}
	
	return employeeCollection;
     }
     
     public EmployeeDetails createEmployee(EmployeeDetails employee){
	 String url = Paths.employees + ".json";
	 List<NameValuePair> params = employee.generateURIParameters(authenticity_token);
	 sendPost(url,  params);
	 employee.setId(getNumberOfEmployees());
	 return employee;
     }
     
     public void updateEmployeeDetails(EmployeeDetails employee){
	 String url = Paths.employee.replace(":id", String.valueOf(employee.getId())) + ".json";
	 List<NameValuePair> params = employee.generateURIParameters(authenticity_token);
	 sendPatch(url, params);
     }
}
