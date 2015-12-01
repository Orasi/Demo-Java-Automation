package com.orasi.apps.bluesource.projectsPage;

import org.openqa.selenium.support.FindBy;

import ru.yandex.qatools.allure.annotations.Step;

import com.orasi.api.restServices.blueSource.BlueSource;
import com.orasi.api.restServices.blueSource.employees.EmployeeDetails;
import com.orasi.core.by.angular.FindByNG;
import com.orasi.core.interfaces.Button;
import com.orasi.core.interfaces.Label;
import com.orasi.core.interfaces.Link;
import com.orasi.core.interfaces.Webtable;
import com.orasi.core.interfaces.impl.internal.ElementFactory;
import com.orasi.utils.TestEnvironment;
import com.orasi.utils.TestReporter;
import com.orasi.utils.date.DateTimeConversion;

public class ProjectSummaryPage {
	private TestEnvironment te;
	
	//All the page elements
	@FindBy(xpath = "//table")
	private Webtable tabProjectInfoTable;
	
	@FindBy(xpath = "//h4[text()='Project Info']/../button")
	private Button btnManageProjectInfo;
	
	
	/*
	 * General info rows
	 */
	private final int CLIENT_PARTNER = 1;
	private final int TEAM_LEADS = 2;
	private final int START_DATE = 3;
	private final int PROJECTED_END_DATE = 4;
	private final int STATUS = 5;
	
	// *********************
	// ** Build page area **
	// *********************
	public ProjectSummaryPage(TestEnvironment te){
		this.te = te;
		ElementFactory.initElements(te.getDriver(), this);
	}
		
	public boolean pageLoaded(){
		return te.pageLoaded(this.getClass(), tabProjectInfoTable); 	    
	}
	// *****************************************
	// ***Page Interactions ***
	// *****************************************


	@Step("Then the Projects Information is correct")
	public boolean validateProjectInfo(Project project){

		if (!project.getClientPartner().equalsIgnoreCase(tabProjectInfoTable.getCellData( CLIENT_PARTNER, 2))) {TestReporter.logFailure("Client P name did not match"); return false;}
		if (!project.getStartDate().equalsIgnoreCase(tabProjectInfoTable.getCellData( START_DATE, 2))) {TestReporter.logFailure("Start Date did not match"); return false;}
		if (!project.getEndDate().equalsIgnoreCase(tabProjectInfoTable.getCellData( PROJECTED_END_DATE, 2))) {TestReporter.logFailure("Projected End Date did not match"); return false;}
		if (!project.getStatus().equalsIgnoreCase(tabProjectInfoTable.getCellData( STATUS, 2))) {TestReporter.logFailure("Status did not match"); return false;}
			
		for(String teamLead : project.getTeamLeads()){
		    if (!tabProjectInfoTable.getCellData(TEAM_LEADS, 2).toLowerCase().contains(teamLead.toLowerCase())) {TestReporter.logFailure("Team Lead did not match: " + teamLead); return false;}
		}
		return true;
	}
	
	@Step("When I click Manage General Info")
	public void clickManageProjectInfo(){
	    btnManageProjectInfo.click();
	}
}
