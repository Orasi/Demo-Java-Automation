package com.orasi.bluesource.features.manageEmployees;

import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import ru.yandex.qatools.allure.annotations.Features;
import ru.yandex.qatools.allure.annotations.Parameter;
import ru.yandex.qatools.allure.annotations.Severity;
import ru.yandex.qatools.allure.annotations.Stories;
import ru.yandex.qatools.allure.annotations.Title;
import ru.yandex.qatools.allure.model.SeverityLevel;

import com.orasi.apps.bluesource.LoginPage;
import com.orasi.apps.bluesource.commons.TopNavigationBar;
import com.orasi.apps.bluesource.employeesPage.EmployeesPage;
import com.orasi.utils.Constants;
import com.orasi.utils.TestEnvironment;
import com.orasi.utils.TestReporter;
import com.orasi.utils.dataProviders.ExcelDataProvider;

public class ShowEmployeesPerPage  extends TestEnvironment {

    private String application = "Bluesource";

    @BeforeTest(groups = { "regression", "manageEmployees", "showEmployeesPerPage" })
    @Parameters({ "runLocation", "browserUnderTest", "browserVersion",
	    "operatingSystem", "environment" })
    public void setup(@Optional String runLocation, String browserUnderTest,
	    String browserVersion, String operatingSystem, String environment) {
	setApplicationUnderTest(application);
	setBrowserUnderTest(browserUnderTest);
	setBrowserVersion(browserVersion);
	setOperatingSystem(operatingSystem);
	setRunLocation(runLocation);
	setTestEnvironment(environment);
    }

    @AfterMethod(groups = { "regression", "manageEmployees", "showEmployeesPerPage" })
    public synchronized void closeSession(ITestResult result) {
	if(!result.isSuccess() || result.getMethod().getMethodName().equals("testShow20EmployeesPerPage"))
	endTest(testName);
    }

    /**
     * @throws Exception
     * @Summary: Adds a housekeeper to the schedule
     * @Precondition:NA
     * @Author: Jessica Marshall
     * @Version: 10/6/2014
     * @Return: N/A
     */
    @Features("Manage Employees")
    @Stories("I can select the number of Employees that is displayed")
    @Severity(SeverityLevel.MINOR)
    @Title("Manage Employees - Show 5 Employees Per Page")
    @Test(groups = { "regression", "manageEmployees", "showEmployeesPerPage" })
    public void testShow5EmployeesPerPage() {
	
	testName = new Object() {
	}.getClass().getEnclosingMethod().getName();

	testStart(testName);
	
	// Login
	LoginPage loginPage = new LoginPage(this);
	TestReporter.assertTrue(loginPage.pageLoaded(),"Verify login page is displayed");
	loginPage.login("COMPANY_ADMIN");

	// Verify user is logged in
	TopNavigationBar topNavigationBar = new TopNavigationBar(this);
	TestReporter.assertTrue(topNavigationBar.isLoggedIn(), "Validate the user logged in successfully");

	//Navigate to Employees Page
	topNavigationBar.clickEmployeesLink();
	EmployeesPage employeesPage = new EmployeesPage(this);
	TestReporter.assertTrue(employeesPage.pageLoaded(),"Verify Employees page is displayed");
	
	//Search for Employee
	employeesPage.setRowsPerPageDisplayed("5");
	TestReporter.assertTrue(employeesPage.validateRowsPerPageDisplayed("5"), "Validate 5 rows was displayed on the Employee Table");

    }
    
    @Features("Manage Employees")
    @Stories("I can select the number of Employees that is displayed")
    @Severity(SeverityLevel.MINOR)
    @Title("Manage Employees - Show 10 Employees Per Page")
    @Test(groups = { "regression", "manageEmployees", "showEmployeesPerPage" }, dependsOnMethods="testShow5EmployeesPerPage")
    public void testShow10EmployeesPerPage() {
	
	EmployeesPage employeesPage = new EmployeesPage(this);
	employeesPage.setRowsPerPageDisplayed("10");
	TestReporter.assertTrue(employeesPage.validateRowsPerPageDisplayed("10"), "Validate 10 rows was displayed on the Employee Table");

	// logout
	//topNavigationBar.clickLogout();
    }
    
    @Features("Manage Employees")
    @Stories("I can select the number of Employees that is displayed")
    @Severity(SeverityLevel.MINOR)
    @Title("Manage Employees - Show 15 Employees Per Page")
    @Test(groups = { "regression", "manageEmployees", "showEmployeesPerPage" }, dependsOnMethods="testShow10EmployeesPerPage")
    public void testShow15EmployeesPerPage() {
	
	EmployeesPage employeesPage = new EmployeesPage(this);
	employeesPage.setRowsPerPageDisplayed("15");
	TestReporter.assertTrue(employeesPage.validateRowsPerPageDisplayed("15"), "Validate 15 rows was displayed on the Employee Table");

	// logout
	//topNavigationBar.clickLogout();
    }
    
    @Features("Manage Employees")
    @Stories("I can select the number of Employees that is displayed")
    @Severity(SeverityLevel.MINOR)
    @Title("Manage Employees - Show 20 Employees Per Page")
    @Test(groups = { "regression", "manageEmployees", "showEmployeesPerPage" }, dependsOnMethods="testShow15EmployeesPerPage")
    public void testShow20EmployeesPerPage() {
	
	EmployeesPage employeesPage = new EmployeesPage(this);
	employeesPage.setRowsPerPageDisplayed("20");
	TestReporter.assertTrue(employeesPage.validateRowsPerPageDisplayed("20"), "Validate 20 rows was displayed on the Employee Table");

	// logout
	TopNavigationBar topNavigationBar = new TopNavigationBar(this);
	topNavigationBar.clickLogout();
    }
    
}