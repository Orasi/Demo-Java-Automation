package com.orasi.apps.bluesource.departmentsPage;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import ru.yandex.qatools.allure.annotations.Step;

import com.orasi.core.interfaces.Element;
import com.orasi.core.interfaces.Label;
import com.orasi.core.interfaces.Link;
import com.orasi.core.interfaces.impl.ElementImpl;
import com.orasi.core.interfaces.impl.internal.ElementFactory;
import com.orasi.utils.AlertHandler;
import com.orasi.utils.Constants;
import com.orasi.utils.OrasiDriver;
import com.orasi.utils.TestEnvironment;
public class ListingDepartmentsPage {
	
    	private OrasiDriver  driver = null;
	
	//All the page elements
	@FindBy(linkText = "Add Department") private Link lnkAddDept;	
	@FindBy(xpath = "//h1[text() = 'Departments']")	private Label lblTitle;
	@FindBy(css = ".alert-success.alert-dismissable") private Label lblSuccessMsg;	
	private By addSubDepartment = By.cssSelector("a");
	private By editIcon = By.cssSelector("div > a:nth-child(1)");
	private By deleteIcon = By.cssSelector("div > a:nth-child(2)");
	
	private final int hierarchySeperator = 20;
	
	// *********************
	// ** Build page area **
	// *********************
	public ListingDepartmentsPage(OrasiDriver driver){
		this.driver = driver;
		ElementFactory.initElements(driver, this);
	}
	
	public boolean pageLoaded(){
	    return driver.pageLoaded(this.getClass(), lnkAddDept);
	}
	

	public boolean pageLoaded(OrasiDriver driver){
	    ElementFactory.initElements(driver, this);
	    return driver.pageLoaded(this.getClass(), lnkAddDept);
	}
	//Methods
	
	//click add dept link
	@Step("And I click the \"New Department\" link")
	public void clickAddDeptLink(){
		lnkAddDept.click();
	}
	
	@Step("And I click the \"Add Subdepartment\" link for Department \"{0}\"")
	public void clickAddSubDepartment(String parentDepartment){
	    getDepartmentElement(parentDepartment).findElement(addSubDepartment).click();
	}
	
	public boolean isTitleHeaderDisplayed(){
		return lblTitle.isDisplayed();
	}
	
	//return if the success message is displayed
	@Step("Then an alert should appear for conformation")
	public boolean isSuccessMsgDisplayed(){
		return lblSuccessMsg.isDisplayed();
	}
	
	//search page for a dept, return if displayed
	@Step("And the department \"{0}\" should be found on the Titles table")
	public boolean searchTableByDept(String departmentName){
	    if (getDepartmentElement(departmentName) != null) return true;
	    return false;
	}
	
	@Step("And I click the \"Edit\" icon on the row for department \"{0}\"")
	public void clickModifyDepartment(String departmentName){
	    getDepartmentElement(departmentName).findElement(editIcon).click();
	}
	
	@Step("And I can delete the department from the table")
	public void deleteDepartment(String departmentName){
	    getDepartmentElement(departmentName).findElement(deleteIcon).click();
	    AlertHandler.handleAllAlerts(driver, 1);
	}
	
	
	public boolean isSubdepartment( String departmentName, String parentDepartmentName){
	    return isSubdepartment(departmentName, parentDepartmentName, true);
	}
	
	@Step("Then the department \"{0}\" is a subdepartment of \"{1}\"")
	public boolean isSubdepartment( String departmentName, String parentDepartmentName, boolean isDirect){
	    int positionOffset = 20;
	    Element department = null;
	    Element parentDepartment = getDepartmentElement(parentDepartmentName);
	    
	    int parentDepartmentPosition = Integer.valueOf(parentDepartment.getCssValue("margin-left").replace("px",""));
	    int departmentPosition = 0;
	    
	    boolean hasChildren = true;
	    boolean isSubDepartment = false;
	    int currentTimeout = driver.getElementTimeout();
	    driver.setElementTimeout(0);
	    department = parentDepartment;
	    while (hasChildren){
		try{
		    department = new ElementImpl(department.findElement(By.xpath("./following-sibling::li")));
		    if(department.getText().replace("Add Subdepartment","").trim().equals(departmentName)) {			
			departmentPosition = Integer.valueOf(department.getCssValue("margin-left").replace("px",""));
			if(isDirect){
			    if(departmentPosition - parentDepartmentPosition == positionOffset)isSubDepartment = true;
			}else{
			    if(departmentPosition - parentDepartmentPosition >= positionOffset)isSubDepartment = true;
			}
		    }
		}catch( NoSuchElementException nse){
		    hasChildren = false;
		}
	    }
	    driver.setElementTimeout(currentTimeout);
	    
	    return isSubDepartment;
	}
	
	private Element getDepartmentElement(String departmentName){
	  //Get all the rows in the table by CSS
	  List<WebElement> elementList = driver.findElements(By.cssSelector(".list-group-item"));
	  for(WebElement element:elementList){
	      if(element.getText().replace("Add Subdepartment","").trim().equals(departmentName)) return new ElementImpl(element,driver);
	  }
	  return null;
	}
}
