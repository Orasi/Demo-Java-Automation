package com.orasi.apps.bluesource.employeesPage;

import java.io.IOException;

import com.fasterxml.jackson.databind.JsonNode;
import com.orasi.api.restServices.core.RestService;
import com.orasi.utils.Randomness;

public class Employee {
    private String username; 
    private String firstName;
    private String lastName; 
    private String title; 
    private String role;
    private String manager;
    private String status;
    private String bridgeTime;
    private String location;
    private String startDate;
    private String cellPhone;
    private String officePhone;
    private String email;
    private String imName; 
    private String imClient;
    private String department;
    
    public String getUsername() {return username;}
    public String getFirstName() {return firstName.substring(0, 1).toUpperCase() + firstName.substring(1).toLowerCase();}
    public String getLastName() {return lastName.substring(0, 1).toUpperCase() + lastName.substring(1).toLowerCase();}
    public String getTitle() {return title;}
    public String getRole() {return role;}
    public String getManager() {return manager;}
    public String getStatus() {return status;}
    public String getBridgeTime() {return bridgeTime;}
    public String getLocation() {return location;}
    public String getStartDate() {return startDate;}
  //  public Date getStartDateAsDate() {return startDate;}    
    public String getCellPhone() {return cellPhone;}
    public String getOfficePhone() {return officePhone;}
    public String getEmail() {return email;}
    public String getImName() {return imName;}
    public String getImClient() {return imClient;}
    public String getDepartment() {return department;}
    
    public void setUsername(String username) {this.username = username;}
    public void setFirstName(String firstName) {this.firstName = firstName.substring(0, 1).toUpperCase() + firstName.substring(1).toLowerCase();}
    public void setLastName(String lastName) {this.lastName = lastName.substring(0, 1).toUpperCase() + lastName.substring(1).toLowerCase();}
    public void setTitle(String title) {this.title = title;}
    public void setRole(String role) {this.role = role;}
    public void setManager(String manager) {this.manager = manager;}
    public void setStatus(String status) {this.status = status;}
    public void setBridgeTime(String bridgeTime) {this.bridgeTime= bridgeTime;}
    public void setLocation(String location) {this.location = location;}
    public void setStartDate(String startDate) {this.startDate = startDate;}
    public void setCellPhone(String cellPhone) {this.cellPhone = cellPhone;}
    public void setOfficePhone(String officePhone) {this.officePhone = officePhone;}
    public void setEmail(String email) {this.email = email;}
    public void setImName(String imName) {this.imName = imName;}
    public void setImClient(String imClient) {this.imClient = imClient;}
    public void setDepartment(String department) {this.department = department;}
    
    public Employee (){
    	//Generate pretty name
    	RestService rest = new RestService();
    	String response = "";
    	JsonNode node = null;
    	try {
		response = 	rest.sendGetRequest("http://api.randomuser.me/?nat=us");
		node = rest.mapJSONToTree(response);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		setFirstName(node.findValue("first").textValue());
		setLastName(node.findValue("last").textValue());
		this.username = getFirstName() + "." + getLastName();
		this.title = "Contractor";
		this.role = "Base";
		this.manager = "Company Admin";
		this.status = "Contractor";
		this.bridgeTime = "1";
		this.location = "Remote";
		this.startDate = "2013-01-20";
		this.cellPhone = "(336) 358-1321";
		this.officePhone = "(336) 358-1321";
		this.email = getUsername() + "@random.com";
		this.imName = getEmail();
		this.imClient = "Skype";
		this.department = "Rural Testing";	
    }   
}
