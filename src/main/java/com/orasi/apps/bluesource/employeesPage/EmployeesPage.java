package com.orasi.apps.bluesource.employeesPage;

import java.util.List;

import org.openqa.selenium.support.FindBy;

import ru.yandex.qatools.allure.annotations.Step;

import com.orasi.apps.bluesource.commons.BluesourceTables;
import com.orasi.apps.bluesource.commons.SortOrder;
import com.orasi.core.interfaces.Button;
import com.orasi.core.interfaces.Checkbox;
import com.orasi.core.interfaces.Element;
import com.orasi.core.interfaces.Label;
import com.orasi.core.interfaces.Textbox;
import com.orasi.core.interfaces.Webtable;
import com.orasi.core.interfaces.impl.internal.ElementFactory;
import com.orasi.utils.OrasiDriver;

public class EmployeesPage {
	
	private OrasiDriver driver;
	
	//All the page elements
	@FindBy(id= "filter_btn")	
	private Button btnAll;

	@FindBy(xpath="//a[text()='All']")
	private Label lblAll;

	@FindBy(xpath="//a[text()='Direct']")
	private Label lblDirect;
	
	@FindBy(xpath= "//input[@id='preference_show_inactives'][@type='checkbox']")	
	private Checkbox chkShowInactive;
	
	@FindBy(xpath= "//button[@data-target='#modal_1' and text()='Add']")	
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
	public EmployeesPage(OrasiDriver driver){
		this.driver = driver;
		ElementFactory.initElements(driver, this);
	}
	public EmployeesPage(){}
	
	public boolean pageLoaded(){
	    return driver.page().pageLoaded(this.getClass(), txtSearch); 	    
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
	    loadingModal.syncHidden(10);
	    txtSearch.syncVisible();
	    txtSearch.set(text);    
	}
	
	@Step("Then Employees with the value \"{0}\" in the \"{1}\" column are displayed")
	public boolean validateTextInTable(String text, String column){
	    BluesourceTables table = new BluesourceTables(driver);
	    String columnName = EmployeesTableColumns.valueOf(column).toString();	    
	    return table.validateTextInTable(text, columnName);
	}
	
	@Step("When I sort the \"{0}\" column in \"{1}\" order")	
	public void sortColumn(String column, String order){
	    BluesourceTables table = new BluesourceTables(driver);
	    String columnName = EmployeesTableColumns.valueOf(column).toString();
	    table.sortColumn(columnName, SortOrder.valueOf(order));	
	}
	
	@Step("Then the \"{0}\" column is displayed in \"{1}\" order")
	public boolean validateSortColumn(String column, String order){
	    BluesourceTables table = new BluesourceTables(driver);	    
	    String columnName = EmployeesTableColumns.valueOf(column).toString();
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
	
	public int getTotalDisplayedEmployees(){
	    loadingModal.syncHidden();
	    lblTotalEmployeeLabel.syncVisible();
	    String total = lblTotalEmployeeLabel.getText();
	    return Integer.parseInt(total.substring(total.indexOf("of")+3, total.length()));
	}
	
	@Step("When I click the All Button on the Employees Page")
	public void clickAllButton(){
	    loadingModal.syncHidden();
	    btnAll.click();
	    driver.page().pageLoaded();
	}
	
	@Step("When I click the Add Button on the Employees Page")
	public void clickAddEmployeeButton(){
	    loadingModal.syncHidden(10);
	    btnAdd.jsClick();
	    driver.page().pageLoaded();
	}

	@Step("When I click the Show All Label on the Employees Page")
	public void clickShowAll(){
	    loadingModal.syncHidden();
	    btnAll.click();
	    lblAll.syncVisible();
	    lblAll.click();
	    driver.page().pageLoaded();
	} 

	@Step("When I click the Show Direct Label on the Employees Page")
	public void clickShowDirect(){
	    loadingModal.syncHidden();
	    btnAll.click();
	    lblDirect.syncVisible();
	    lblDirect.click();
	    driver.page().pageLoaded();
	} 
	
	@Step("When I check the Show Inactive Checkbox on the Employees Page")
	public void checkInactiveCheckbox(){
	    loadingModal.syncHidden();
	    btnAll.click();
	    chkShowInactive.syncVisible();
	    chkShowInactive.check();
	    driver.page().pageLoaded();
	} 

	@Step("When I check the Show Inactive Checkbox on the Employees Page")
	public void uncheckInactiveCheckbox(){
	    loadingModal.syncHidden();
	    btnAll.click();
	    chkShowInactive.syncVisible();
	    chkShowInactive.uncheck();
	    driver.page().pageLoaded();
	} 
	@Step("Then the Employees table should update the employees displayed")
	public boolean validateEmployeeTableResultsUpdated(int previousCount){
	    return (previousCount != getTotalDisplayedEmployees());
	}	
	
	@Step("Then the Employee will display no rows found")
	public boolean validateNoRowsFound(){
	    return (tabEmployeeTable.getRowCount() == 1);
	}
	
	public boolean validateLastnameFoundInTable(String username){
	    return validateTextInTable(username.substring(0, 1).toUpperCase() + username.substring(1).toLowerCase(), EmployeesTableColumns.LASTNAME.name());
	}
	
	@Step("When I click the \"{0}\" Name link")
	public void selectEmployeeName(String name){
		BluesourceTables table = new BluesourceTables(driver);
		table.selectFieldLink(name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase());
	}
}
