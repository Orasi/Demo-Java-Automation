package com.orasi.bluesource.employeesPage;

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
import com.orasi.utils.OrasiDriver;
import com.orasi.utils.TestEnvironment;
import com.orasi.utils.TestReporter;
import com.orasi.utils.date.DateTimeConversion;

public class EmployeeSummaryPage {
	private OrasiDriver driver;
	
	//All the page elements
	
	@FindBy( xpath= "//div[contains(@class, 'panel-heading') and contains(@data-target,'#panel_body_2')]")	private Label lblGeneralInfo;	
	@FindBy(id = "panel_body_2") private Label lblGeneralInfoBody;
	@FindBy(xpath = "//div[@id='panel_body_2']/div/table")	private Webtable tabGeneralInfoTable;	
	@FindBy(xpath = "//h4[text()='General Info']/../button") private Button btnManageGeneralInfo;	
	@FindBy( xpath= "//div[contains(@class, 'panel-heading') and contains(@data-target,'#panel_body_3')]")	private Label lblProjectInfo;
	@FindBy(id = "panel_body_3") private Label lblProjectInfoBody;	
	@FindBy(xpath = "//h4[text()='Project Info']/../a" )private Button btnManageProjectInfo;	
	@FindBy( xpath= "//div[contains(@class, 'panel-heading') and contains(@data-target,'#panel_body_1')]")	private Label lblTimeOffInfo;
	@FindBy(id = "panel_body_1") private Label lblTimeOffInfoBody;	
	@FindBy(xpath = "//h4[text()='Time Off Info']/../a") private Button lnkManageTimeOff;	
	@FindBy(linkText = "Manage") private Link lnkViewTimeOff;
	
	
	/*
	 * General info rows
	 */
	private final int USERNAME = 1;
	private final int ROLE = 2;
	private final int TITLE = 3;
	private final int MANAGER = 4;
	private final int STATUS = 5;
	private final int LOCATION = 6;
	private final int START_DATE = 7;
	private final int TIME_WITH_ORASI = 8;
	private final int CELLPHONE = 9;
	private final int OFFICEPHONE = 10;
	private final int EMAIL = 11;
	private final int IM_USER = 12;
	private final int IM_CLIENT = 13;
	private final int DEPARTMENT = 14;
	
	// *********************
	// ** Build page area **
	// *********************
	public EmployeeSummaryPage(OrasiDriver driver){
		this.driver = driver;
		ElementFactory.initElements(driver, this);
	}
		
	public boolean pageLoaded(){
		return driver.page().pageLoaded(this.getClass(), lnkViewTimeOff); 	    
	}
	// *****************************************
	// ***Page Interactions ***
	// *****************************************

	public void ClickManageTimeOff(){
		lnkManageTimeOff.click();
	}
	
	public void ViewManageTimeOff(){
		lnkViewTimeOff.click();
	}
	
	public void viewGeneralInfo(){
		if(lblGeneralInfoBody.getAttribute("class").equals("panel-collapse collapse")) lblGeneralInfo.click();		
	}
	
	public void viewProjectsInfo(){
		if(lblProjectInfoBody.getAttribute("class").equals("panel-collapse collapse")) lblProjectInfo.click();
	}
	
	public void viewTimeOffInfo(){
		if(lblTimeOffInfoBody.getAttribute("class").equals("panel-collapse collapse")) lblTimeOffInfo.click();
	}
	
	//@Step("Then the Employees General Information is correct")
	public boolean validateAPIGeneralInfo(Employee employee){
		viewGeneralInfo();
		BlueSource blueSource = new BlueSource("Company.admin");
		String url = driver.getCurrentUrl();
		int startTrim = url.lastIndexOf('/') + 1;
		int employeeID = Integer.parseInt(url.substring(startTrim, url.length()));
		EmployeeDetails apiEmployee = blueSource.employees().getEmployeeDetails(employeeID);
		
		if (!employee.getUsername().equalsIgnoreCase(apiEmployee.getUsername())) {TestReporter.logFailure("User name in API did not match"); return false;}
		if (!employee.getRole().equalsIgnoreCase(apiEmployee.getRole())) {TestReporter.logFailure("Role in API did not match"); return false;}
		if (!employee.getStatus().equalsIgnoreCase(apiEmployee.getStatus())) {TestReporter.logFailure("Status in API did not match"); return false;}
		if (!employee.getLocation().equalsIgnoreCase(apiEmployee.getLocation())) {TestReporter.logFailure("Location in API did not match"); return false;}
		if (!employee.getStartDate().equals(apiEmployee.getStart_date())) {TestReporter.logFailure("Start Date in API did not match"); return false;}
		if (!employee.getCellPhone().equalsIgnoreCase(apiEmployee.getCell_phone())) {TestReporter.logFailure("Cell Phone in API did not match"); return false;}
		if (!employee.getOfficePhone().equalsIgnoreCase(apiEmployee.getOffice_phone())) {TestReporter.logFailure("Office Phone in API did not match"); return false;}
		if (!employee.getEmail().equalsIgnoreCase(apiEmployee.getEmail())) {TestReporter.logFailure("Email in API did not match"); return false;}
		if (!employee.getImName().equalsIgnoreCase(apiEmployee.getIm_name())) {TestReporter.logFailure("IM Username in API did not match"); return false;}
		if (!employee.getImClient().equalsIgnoreCase(apiEmployee.getIm_client())) {TestReporter.logFailure("IM Client in API did not match"); return false;}
		/*
		 *  This items are returned as ID's 
		 *if (!employee.getDepartment().equalsIgnoreCase(tabGeneralInfoTable.getCellData(te, DEPARTMENT, 2))) {TestReporter.logFailure("Department did not match"); return false;}
		 *if (!employee.getTitle().equalsIgnoreCase(tabGeneralInfoTable.getCellData(te, TITLE, 2))) {TestReporter.logFailure("Title did not match"); return false;}
		 *if (!employee.getManager().equalsIgnoreCase(tabGeneralInfoTable.getCellData(te, MANAGER, 2))) {TestReporter.logFailure("Manager did not match"); return false;}
		*/				
		return true;
	}
	
	@Step("Then the Employees General Information is correct")
	public boolean validateGeneralInfo(Employee employee){
		viewGeneralInfo();
		
		String convertedStartDate = DateTimeConversion.convert(employee.getStartDate(), "yyyy-MM-dd", "MMMM dd, yyyy");

		if (!employee.getUsername().equalsIgnoreCase(tabGeneralInfoTable.getCellData( USERNAME, 2))) {TestReporter.logFailure("User name did not match"); return false;}
		if (!employee.getRole().equalsIgnoreCase(tabGeneralInfoTable.getCellData(ROLE, 2))) {TestReporter.logFailure("Role did not match"); return false;}
		if (!employee.getTitle().equalsIgnoreCase(tabGeneralInfoTable.getCellData( TITLE, 2))) {TestReporter.logFailure("Title did not match"); return false;}
		if (!employee.getManager().equalsIgnoreCase(tabGeneralInfoTable.getCellData( MANAGER, 2))) {TestReporter.logFailure("Manager did not match"); return false;}
		if (!employee.getStatus().equalsIgnoreCase(tabGeneralInfoTable.getCellData( STATUS, 2))) {TestReporter.logFailure("Status did not match"); return false;}
		if (!employee.getLocation().equalsIgnoreCase(tabGeneralInfoTable.getCellData( LOCATION, 2))) {TestReporter.logFailure("Location did not match"); return false;}
		if (!convertedStartDate.equals(tabGeneralInfoTable.getCellData( START_DATE, 2))) {TestReporter.logFailure("Start Date did not match"); return false;}
		//if (!employee.getStartDate().equalsIgnoreCase(tabGeneralInfoTable.getCellData( TIME_WITH_ORASI, 2))) {TestReporter.logFailure("Time With Orasi did not match"); return false;}
		if (!employee.getCellPhone().equalsIgnoreCase(tabGeneralInfoTable.getCellData(CELLPHONE, 2))) {TestReporter.logFailure("Cell Phone did not match"); return false;}
		if (!employee.getOfficePhone().equalsIgnoreCase(tabGeneralInfoTable.getCellData( OFFICEPHONE, 2))) {TestReporter.logFailure("Office Phone did not match"); return false;}
		if (!employee.getEmail().equalsIgnoreCase(tabGeneralInfoTable.getCellData( EMAIL, 2))) {TestReporter.logFailure("Email did not match"); return false;}
		if (!employee.getImName().equalsIgnoreCase(tabGeneralInfoTable.getCellData( IM_USER, 2))) {TestReporter.logFailure("IM Username did not match"); return false;}
		if (!employee.getImClient().equalsIgnoreCase(tabGeneralInfoTable.getCellData( IM_CLIENT, 2))) {TestReporter.logFailure("IM Client did not match"); return false;}
		if (!employee.getDepartment().equalsIgnoreCase(tabGeneralInfoTable.getCellData( DEPARTMENT, 2))) {TestReporter.logFailure("Department did not match"); return false;}
				
		return true;
	}
	
	@Step("When I click Manage General Info")
	public void clickManageGeneralInfo(){
	    btnManageGeneralInfo.jsClick();
	}
}
