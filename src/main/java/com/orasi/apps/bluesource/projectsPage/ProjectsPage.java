package com.orasi.apps.bluesource.projectsPage;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;

import ru.yandex.qatools.allure.annotations.Step;

import com.orasi.apps.bluesource.commons.BluesourceTables;
import com.orasi.apps.bluesource.commons.SortOrder;
import com.orasi.core.interfaces.Button;
import com.orasi.core.interfaces.Element;
import com.orasi.core.interfaces.Label;
import com.orasi.core.interfaces.Textbox;
import com.orasi.core.interfaces.Webtable;
import com.orasi.core.interfaces.impl.internal.ElementFactory;
import com.orasi.utils.OrasiDriver;
import com.orasi.utils.TestEnvironment;
import com.orasi.utils.TestReporter;

public class ProjectsPage {
	
	private OrasiDriver driver;
	
	//All the page elements
	@FindBy(css= "#all-content > div.header-btn-section > div > div:nth-child(1) > label") private Button btnShowInactive;	
	@FindBy(xpath= "//button[text()='Add']")	private Button btnAdd;	
	@FindBy(xpath = "//input[@id='search-bar']") private Textbox txtSearch;	
	@FindBy(css = "a.ng-binding") private Element tableCell;	
	@FindBy(css = ".alert-success.alert-dismissable") private Label lblSuccessMsg;	
	@FindBy(className = "table") private Webtable tabProjectTable;	
	@FindBy(id = "loading-section")	private Element loadingModal;	
	@FindBy(css = "#resource-content > div:nth-child(2) > p") private Label lblTotalProjectLabel;
	
	// *********************
	// ** Build page area **
	// *********************
	public ProjectsPage(OrasiDriver driver){
		this.driver = driver;
		ElementFactory.initElements(driver, this);
	}
	public ProjectsPage(){}
	
	public boolean pageLoaded(){
	    return driver.pageLoaded(this.getClass(), txtSearch); 	    
	}
	
	// *****************************************
	// ***Page Interactions ***
	// *****************************************
	
	@Step("Then a success message is displayed")
	public boolean isSuccessMsgDisplayed(){
	    return lblSuccessMsg.isDisplayed();
	}
	
	public String getSuccessMsgText(){
	    return lblSuccessMsg.getText();
	}
	
	@Step("When I search for \"{0}\" on the Projects Page")
	public void enterSearchText(String text){
	    loadingModal.syncHidden();
	    txtSearch.syncVisible();
	    txtSearch.safeSet(text);
	}
	
	@Step("Then Employees with the value \"{0}\" in the \"{1}\" column are displayed")
	public boolean validateTextInTable(String text, String column){
	    BluesourceTables table = new BluesourceTables(driver);
	    String columnName = ProjectsTableColumns.valueOf(column).toString();	    
	    return table.validateTextInTable(text, columnName);
	}
	
	@Step("When I sort the \"{0}\" column in \"{1}\" order")	
	public void sortColumn(String column, String order){
	    BluesourceTables table = new BluesourceTables(driver);
	    String columnName = ProjectsTableColumns.valueOf(column).toString();
	    table.sortColumn(columnName, SortOrder.valueOf(order));	
	}
	
	@Step("Then the \"{0}\" column is displayed in \"{1}\" order")
	public boolean validateSortColumn(String column, String order){
	    BluesourceTables table = new BluesourceTables(driver);	    
	    String columnName = ProjectsTableColumns.valueOf(column).toString();
	    //if((column.equals("CLIENTPARTNER")) && order.equals("ASCENDING")) order = "DESCENDING";
	    if((column.equals("CLIENTPARTNER")) && order.equals("DESCENDING")) order = "ASCENDING";
	    return table.validateSortColumn(columnName, SortOrder.valueOf(order));	
	}
	
	@Step("When I set the number of rows to be \"{0}\"")
	public void setRowsPerPageDisplayed(String numberOfRows){
	    BluesourceTables table = new BluesourceTables(driver);
	    table.setRowsPerPageDisplayed(numberOfRows);
	}
	
	@Step("Then the number of rows displayed should be \"{0}\"")
	public boolean validateRowsPerPageDisplayed(String numberOfRows){
	    BluesourceTables table = new BluesourceTables(driver);
	    return table.validateRowsPerPageDisplayed(numberOfRows);
	}
	
	public int getTotalDisplayedProjects(){
	    loadingModal.syncHidden();
	    lblTotalProjectLabel.syncVisible();
	    String total = lblTotalProjectLabel.getText();
	    return Integer.parseInt(total.substring(total.indexOf("of")+3, total.length()));
	}

	@Step("When I click the Add Button on the Employees Page")
	public void clickAddProjectButton(){
	    loadingModal.syncHidden();
	    btnAdd.jsClick();
	    driver.pageLoaded();
	}
	
	@Step("When I click the Show Inactive Button on the Projects Page")
	public void clickInactiveButton(){
	    loadingModal.syncHidden();
	    btnShowInactive.jsClick();
	    driver.pageLoaded();
	} 
	
	@Step("Then the Projects table should update the projects displayed")
	public boolean validateProjectsTableResultsUpdated(int previousCount){
	    return (previousCount != getTotalDisplayedProjects());
	}	
	
	@Step("Then the Employee will display no rows found")
	public boolean validateNoRowsFound(){
	    return (tabProjectTable.getRowCount() == 1);
	}
	
	public boolean validateProjectFoundInTable(String projectName){
	    return validateTextInTable(projectName.substring(0, 1).toUpperCase() + projectName.substring(1).toLowerCase(), ProjectsTableColumns.PROJECTNAME.name());
	}
	
	@Step("When I click the \"{0}\" Name link")
	public void selectProjectName(String name){
		BluesourceTables table = new BluesourceTables(driver);
		table.selectFieldLink(name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase());
	}
}
