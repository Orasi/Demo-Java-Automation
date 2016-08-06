package com.orasi.bluesource.projectsPage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.orasi.utils.Randomness;

public class Project {
    private String projectName;
    private String clientPartner; 
    private ArrayList<String> teamLeads = new ArrayList<String>(); 
    private ArrayList<String> teamLeadsToRemove = new ArrayList<String>();
    private String status;
    private String startDate; 
    private String endDate;
    
    public String getProjectName() {
        return projectName.substring(0, 1).toUpperCase() + projectName.substring(1).toLowerCase();
    }
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
    public String getClientPartner() {
        return clientPartner;
    }
    public void setClientPartner(String clientPartner) {
        this.clientPartner = clientPartner;
    }
    public ArrayList<String> getTeamLeads() {
        return teamLeads;
    }
   
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getStartDate() {
        return startDate;
    }
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
    public String getEndDate() {
        return endDate;
    }
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void removeTeamLead(String teamLead){
	this.teamLeads.remove(teamLeads.indexOf(teamLead));
	this.teamLeadsToRemove.add(teamLead);
    }
    
    public ArrayList<String> getTeamLeadsToRemove(){
	return teamLeadsToRemove;
    }

    public void addTeamLead(String teamLead){
	this.teamLeads.add(teamLead);
    }
    
    public Project (){
	this.projectName = Randomness.randomAlphaNumeric(8) + " project";
	this.clientPartner = "Virginia Vestal";
	this.teamLeads.add("Kristi Collins");
	this.teamLeads.add("Jim Azar");
	this.status = "Active";
	this.startDate = "2015-07-09";
	this.endDate =  "2018-07-09";
	
    }   
}
