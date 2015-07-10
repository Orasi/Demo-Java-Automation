package com.orasi.api.restServices.blueSource.employees;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.orasi.utils.Randomness;
/*
 * Sample
 * 
 * {
       "id": 937,
       "username": "hMPWfYQe.X9tqC13Z",
       "first_name": "blah",
       "last_name": "x9tqc13z",
       "created_at": "2015-07-01T16:31:16.603-04:00",
       "updated_at": "2015-07-01T16:31:17.784-04:00",
       "manager_id": 323,
       "role": "Base",
       "start_date": "2013-01-20",
       "level": null,
       "cell_phone": "(336) 358-1321",
       "im_name": "hMPWfYQe.X9tqC13Z@random.com",
       "im_client": "Skype",
       "status": "Contractor",
       "email": "hMPWfYQe.X9tqC13Z@random.com",
       "office_phone": "(336) 358-1321",
       "location": "Remote",
       "department_id": 3182,
       "title_id": 2,
       "sys_admin": null,
       "bridge_time": 1,
       "vacation_time_left": null,
       "floating_time_left": null,
       "sick_time_left": null
    }
 */
public class EmployeeDetails{

    private int id;
    private String username;
    private String first_name;
    private String last_name;
    private String created_at;
    private String updated_at;
    private int manager_id;
    private String role;
    private String start_date;
    private String level;
    private String cell_phone;
    private String im_name;
    private String im_client;
    private String status;
    private String email;
    private String office_phone;
    private String location;
    private int department_id;
    private int title_id;
    private String  sys_admin;
    private String bridge_time;
    private int vacation_time_left;
    private int floating_time_left;
    private int sick_time_left;
    
    public int getId() {
        return id;
    }
    public String getUsername() {
        return username;
    }
    public String getFirst_name() {
        return first_name;
    }
    public String getLast_name() {
        return last_name;
    }
    public String getCreated_at() {
        return created_at;
    }
    public String getUpdated_at() {
        return updated_at;
    }
    public int getManager_id() {
        return manager_id;
    }
    public String getRole() {
        return role;
    }
    public String getStart_date() {
        return start_date;
    }
    public String getLevel() {
        return level;
    }
    public String getCell_phone() {
        return cell_phone;
    }
    public String getIm_name() {
        return im_name;
    }
    public String getIm_client() {
        return im_client;
    }
    public String getStatus() {
        return status;
    }
    public String getEmail() {
        return email;
    }
    public String getOffice_phone() {
        return office_phone;
    }
    public String getLocation() {
        return location;
    }
    public int getDepartment_id() {
        return department_id;
    }
    public int getTitle_id() {
        return title_id;
    }
    public String getSys_admin() {
        return sys_admin;
    }
    public String getBridge_time() {
        return bridge_time;
    }
    public int getVacation_time_left() {
        return vacation_time_left;
    }
    public int getFloating_time_left() {
        return floating_time_left;
    }
    public int getSick_time_left() {
        return sick_time_left;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }
    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }
    public void setManager_id(int manager_id) {
        this.manager_id = manager_id;
    }
    public void setRole(String role) {
        this.role = role;
    }
    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }
    public void setLevel(String level) {
        this.level = level;
    }
    public void setCell_phone(String cell_phone) {
        this.cell_phone = cell_phone;
    }
    public void setIm_name(String im_name) {
        this.im_name = im_name;
    }
    public void setIm_client(String im_client) {
        this.im_client = im_client;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setOffice_phone(String office_phone) {
        this.office_phone = office_phone;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public void setDepartment_id(int department_id) {
        this.department_id = department_id;
    }
    public void setTitle_id(int title_id) {
        this.title_id = title_id;
    }
    public void setSys_admin(String sys_admin) {
        this.sys_admin = sys_admin;
    }
    public void setBridge_time(String bridge_time) {
        this.bridge_time = bridge_time;
    }
    public void setVacation_time_left(int vacation_time_left) {
        this.vacation_time_left = vacation_time_left;
    }
    public void setFloating_time_left(int floating_time_left) {
        this.floating_time_left = floating_time_left;
    }
    public void setSick_time_left(int sick_time_left) {
        this.sick_time_left = sick_time_left;
    }
    
    public List<NameValuePair> generateURIParameters(String authenticity_token){ 
	return generateURIParameters(authenticity_token, null);
    }
    
    public List<NameValuePair> generateURIParameters(String authenticity_token, String method){ 
	List<NameValuePair> params = new ArrayList<NameValuePair>();
 	params.add(new BasicNameValuePair("authenticity_token", authenticity_token));
 	params.add(new BasicNameValuePair("_method", method));
 	params.add(new BasicNameValuePair("employee[username]", username));
 	params.add(new BasicNameValuePair("employee[first_name]", first_name));
 	params.add(new BasicNameValuePair("employee[last_name]", last_name));
 	params.add(new BasicNameValuePair("employee[title_id]", String.valueOf(title_id)));
 	params.add(new BasicNameValuePair("employee[role]", role));
 	params.add(new BasicNameValuePair("employee[manager_id]",String.valueOf(manager_id)));
 	params.add(new BasicNameValuePair("employee[status]", status));
 	params.add(new BasicNameValuePair("employee[bridge_time]", bridge_time));
 	params.add(new BasicNameValuePair("employee[location]", location));
 	params.add(new BasicNameValuePair("employee[start_date]", start_date));
 	params.add(new BasicNameValuePair("employee[cell_phone]", cell_phone));
 	params.add(new BasicNameValuePair("employee[office_phone]", office_phone));
 	params.add(new BasicNameValuePair("employee[email]", email));
 	params.add(new BasicNameValuePair("employee[im_name]", im_name));
 	params.add(new BasicNameValuePair("employee[im_client]", im_client));
 	params.add(new BasicNameValuePair("employee[department_id][]", String.valueOf(department_id)));
 	return params;
     }
    
    public void generateData(){
	this.first_name = Randomness.randomAlphaNumeric(8);
	this.last_name = Randomness.randomAlphaNumeric(8);
	this.username = getFirst_name() + "." + getLast_name();
	this.title_id= 3;
	this.role = "Base";
	this.manager_id= 3;
	this.status = "Contractor";
	this.bridge_time = "1";
	this.location = "Remote";
	this.start_date= "2013-01-20";
	this.cell_phone= "(336) 358-1321";
	this.office_phone = "(336) 358-1321";
	this.email = getUsername() + "@random.com";
	this.im_name = getEmail();
	this.im_client = "Skype";
	this.department_id = 1;	
    }
}
