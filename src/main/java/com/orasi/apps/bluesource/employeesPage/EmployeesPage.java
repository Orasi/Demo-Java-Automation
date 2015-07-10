package com.orasi.apps.bluesource.employeesPage;

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
import com.orasi.utils.TestEnvironment;
import com.orasi.utils.TestReporter;

public class EmployeesPage {
	
	private TestEnvironment te;
	
	//All the page elements
	@FindBy(css= "#all-content > div.header-btn-section > div > div:nth-child(1) > label:nth-child(1)")	
	private Button btnAll;

	@FindBy(css= "#all-content > div.header-btn-section > div > div:nth-child(1) > label:nth-child(2)")	
	private Button btnDirect;

	@FindBy(css= "#all-content > div.header-btn-section > div > div:nth-child(2) > label")	
	private Button btnShowInactive;
	
	@FindBy(css= "#all-content > div.header-btn-section > div > div:nth-child(4) > button")	
	private Button btnAdd;
	
	@FindBy(css = "input[id = 'search-bar']")
	private Textbox txtSearch;
		
	@FindBy(css = ".alert-success.alert-dismissable")
	private Label lblSuccessMsg;
	
	@FindBy(className = "table")
	private Webtable tabEmployeeTable;
	
	@FindBy(id = "loading-section")
	private Element loadingModal;
	
	@FindBy(css = "#resource-content > div:nth-child(2) > p")
	private Label lblTotalEmployeeLabel;
	
	// *********************
	// ** Build page area **
	// *********************
	public EmployeesPage(TestEnvironment te){
		this.te = te;
		ElementFactory.initElements(te.getDriver(), this);
	}
	public EmployeesPage(){}
	
	public boolean pageLoaded(){
	    return te.pageLoaded().isElementLoaded(this.getClass(), txtSearch); 	    
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
	
	@Step("When I search for \"{0}\" on the Employees Page")
	public void enterSearchText(String text){
	    loadingModal.syncHidden(te.getDriver());
	    txtSearch.syncVisible(te.getDriver());
	    txtSearch.safeSet(text);    
	}
	
	@Step("Then Employees with the value \"{0}\" in the \"{1}\" column are displayed")
	public boolean validateTextInTable(String text, String column){
	    BluesourceTables table = new BluesourceTables(te);
	    String columnName = EmployeesTableColumns.valueOf(column).toString();	    
	    return table.validateTextInTable(text, columnName);
	}
	
	@Step("When I sort the \"{0}\" column in \"{1}\" order")	
	public void sortColumn(String column, String order){
	    BluesourceTables table = new BluesourceTables(te);
	    String columnName = EmployeesTableColumns.valueOf(column).toString();
	    table.sortColumn(columnName, SortOrder.valueOf(order));	
	}
	
	@Step("Then the \"{0}\" column is displayed in \"{1}\" order")
	public boolean validateSortColumn(String column, String order){
	    BluesourceTables table = new BluesourceTables(te);	    
	    String columnName = EmployeesTableColumns.valueOf(column).toString();
	    return table.validateSortColumn(columnName, SortOrder.valueOf(order));	
	}
	
	@Step("When I set the number of rows to be \"{0}\"")
	public void setRowsPerPageDisplayed(String numberOfRows){
	    BluesourceTables table = new BluesourceTables(te);
	    table.setRowsPerPageDisplayed(numberOfRows);
	}
	
	@Step("Then the number of rows displayed should be \"{0}\"")
	public boolean validateRowsPerPageDisplayed(String numberOfRows){
	    BluesourceTables table = new BluesourceTables(te);
	    return table.validateRowsPerPageDisplayed(numberOfRows);
	}
	
	public int getTotalDisplayedEmployees(){
	    loadingModal.syncHidden(te.getDriver());
	    lblTotalEmployeeLabel.syncVisible(te.getDriver());
	    String total = lblTotalEmployeeLabel.getText();
	    return Integer.parseInt(total.substring(total.indexOf("of")+3, total.length()));
	}
	
	@Step("When I click the All Button on the Employees Page")
	public void clickAllButton(){
	    loadingModal.syncHidden(te.getDriver());
	    btnAll.click();
	    te.pageLoaded().isDomComplete();
	}
	
	@Step("When I click the Add Button on the Employees Page")
	public void clickAddEmployeeButton(){
	    loadingModal.syncHidden(te.getDriver());
	    btnAdd.click();
	    te.pageLoaded().isDomComplete();
	}
	
	@Step("When I click the Direct Button on the Employees Page")
	public void clickDirectButton(){
	    loadingModal.syncHidden(te.getDriver());
	    btnDirect.click();
	    te.pageLoaded().isDomComplete();
	}
	
	@Step("When I click the Show Inactive Button on the Employees Page")
	public void clickInactiveButton(){
	    loadingModal.syncHidden(te.getDriver());
	    btnShowInactive.click();
	    te.pageLoaded().isDomComplete();
	} 
	
	@Step("Then the Employees table should update the employees displayed")
	public boolean validateEmployeeTableResultsUpdated(int previousCount){
	    return (previousCount != getTotalDisplayedEmployees());
	}	
	
	@Step("Then the Employee will display no rows found")
	public boolean validateNoRowsFound(){
	    return (tabEmployeeTable.getRowCount(te) == 1);
	}
	
	public boolean validateLastnameFoundInTable(String username){
	    return validateTextInTable(username.substring(0, 1).toUpperCase() + username.substring(1).toLowerCase(), EmployeesTableColumns.LASTNAME.name());
	}
	
	@Step("When I click the \"{0}\" Name link")
	public void selectEmployeeName(String name){
		BluesourceTables table = new BluesourceTables(te);
		table.selectFieldLink(name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase());
	}
	
	//search the employee results table by first & last name & click on it
	public boolean searchTableByFirstAndLastName(String firstName, String lastName){
		WebDriverWait wait = new WebDriverWait(te.getDriver(), 30);
		
		//enter the name to search by
		//driver.findElement(By.cssSelector("input[id = 'search-bar']")).sendKeys(firstName + " " + lastName);
		txtSearch.safeSet(firstName + " " + lastName);
		
		//wait for page to load/refresh
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("a.ng-binding")));
		
		//Get all the elements by CSS in the table that are the individual cells
		List<WebElement> elementList = te.getDriver().findElements(By.cssSelector("a.ng-binding"));
		for (int i = 0; i < elementList.size(); i++){
			
			//if it's the last element then just stop as it won't match
			if (i == elementList.size() - 1){
				
				break;
			}
			if (elementList.get(i).getText().trim().equals(firstName)){
				TestReporter.log("First name was found in table of employees");
				
				//If it matches, then see if the following element matches the last name
				if (elementList.get(i+1).getText().trim().equals(lastName)){
				    TestReporter.log("Last name was found in table of employees");
					//click on the element 
					elementList.get(i).click();
					
					//return true
					TestReporter.log("The employee was found in the table of employees");
					return true;
				}
			}
		}
		
		//element not found that matches first and last name
		Reporter.log("The employee was not found in search results" + firstName + " " + lastName );
		return false;
		
	}
	
	//This method just selects the first employee in the employee table - so you don't need to know
	//who the employee is
	public void selectFirstEmployee(){
		
		//Get all the table elements
		List<WebElement> elementList = te.getDriver().findElements(By.cssSelector("a.ng-binding"));
		
		//click on the first one
		elementList.get(0).click();
	}
}
