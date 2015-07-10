package com.orasi.apps.bluesource.commons;


import org.openqa.selenium.support.FindBy;

import ru.yandex.qatools.allure.annotations.Step;

import com.orasi.core.interfaces.Link;
import com.orasi.core.interfaces.impl.internal.ElementFactory;
import com.orasi.utils.TestEnvironment;

public class TopNavigationBar {
    	private TestEnvironment te = null;
	
	//All the page elements:
	@FindBy(linkText = "Logout")
	private Link lnkLogout;
	
	@FindBy(xpath = "//a[text() = 'Directory']")
	private Link lnkDirectory;
	
	@FindBy(xpath = "//a[text() = 'Projects']")
	private Link lnkProjects;
	
	@FindBy(xpath = "//a[text() = 'Employees']")
	private Link lnkEmployees;
	
	@FindBy(xpath = "//a[text() = 'Admin ']")
	private Link lnkAdminDrop;
	
	@FindBy(css = "a[href = '/admin/departments']")
	private Link lnkDept;
	
	@FindBy(css = "a[href = '/admin/titles']")
	private Link lnkTitle;
	

	// *********************
	// ** Build page area **
	// *********************
	public TopNavigationBar(TestEnvironment te){
	    this.te = te;
	    ElementFactory.initElements(te.getDriver(), this);
	}
	
	public boolean pageLoaded(){
	    return te.pageLoaded().isElementLoaded(this.getClass(), lnkLogout); 
	}
	// *****************************************
	// ***Page Interactions ***
	// *****************************************

	@Step("And I click the \"Admin Link\"")
	public void clickAdminLink(){
		lnkAdminDrop.click();
	}
	
	@Step("And I navigate to the \"Departments Page\"")
	public void clickDepartmentsLink(){
		lnkDept.click();
	}
	
	@Step("And I navigate to the \"Titles Page\"")
	public void clickTitlesLink(){
		lnkTitle.click();
	}
	
	@Step("And I navigate to the \"Directory Page\"")
	public void clickDirectoryLink(){
		lnkDirectory.click();
	}
	
	@Step("And I navigate to the \"Projects Page\"")
	public void clickProjectsLink(){
		lnkProjects.click();
	}
	
	@Step("And I navigate to the \"Employees Page\"")
	public void clickEmployeesLink(){
		lnkEmployees.click();
	}
	//Verify logout link is displayed
	@Step("And I log in successfully")
	public boolean isLoggedIn(){
		return lnkLogout.isDisplayed();
	}
	
	
	//Click logout
	//@Step("And I Log Out")
	public void clickLogout(){
		lnkLogout.click();
	}
}
