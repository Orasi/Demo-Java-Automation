package com.orasi.bluesource.features.manageEmployees;

import org.testng.ITestContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.orasi.apps.bluesource.LoginPage;
import com.orasi.apps.bluesource.commons.TopNavigationBar;
import com.orasi.apps.bluesource.employeesPage.EmployeesPage;
import com.orasi.utils.Constants;
import com.orasi.utils.OrasiDriver;
import com.orasi.utils.TestEnvironment;
import com.orasi.utils.TestReporter;
import com.orasi.utils.dataProviders.ExcelDataProvider;

import ru.yandex.qatools.allure.annotations.Features;
import ru.yandex.qatools.allure.annotations.Parameter;
import ru.yandex.qatools.allure.annotations.Severity;
import ru.yandex.qatools.allure.annotations.Stories;
import ru.yandex.qatools.allure.annotations.Title;
import ru.yandex.qatools.allure.model.SeverityLevel;

public class FilterEmployees  extends TestEnvironment {

    private OrasiDriver driver = null;
    @DataProvider(name = "dataScenario")
    public Object[][] scenarios() {
	return new ExcelDataProvider(Constants.BLUESOURCE_DATAPROVIDER_PATH + "ManageEmployees.xlsx", "FilterEmployees").getTestData();
    }
    
    @BeforeTest( alwaysRun=true)
    @Parameters({ "runLocation", "browserUnderTest", "browserVersion", "operatingSystem", "environment" })
    public void setup(String runLocation, String browserUnderTest, String browserVersion, String operatingSystem, String environment) {
	setApplicationUnderTest("Bluesource");
	setBrowserUnderTest(browserUnderTest);
	setBrowserVersion(browserVersion);
	setOperatingSystem(operatingSystem);
	setRunLocation(runLocation);
	setTestEnvironment(environment);
	setThreadDriver(true);
    }

    @AfterMethod(  alwaysRun=true)
    public void closeSession(ITestContext test) {
	endTest(testName, test, driver);
    }    

    @Features("Manage Employees")
    @Stories("I can filter on the Employee Page for Employees that I supervise")
    @Severity(SeverityLevel.MINOR)
    @Title("Manage Employees - Filter on Direct Employees")
    @Test(dataProvider = "dataScenario", groups = { "regression", "manageEmployees", "filterEmployees" })
    public void testFilterDirectEmployee(@Parameter String testScenario, @Parameter String role) {
	
	testName = new Object() {
	}.getClass().getEnclosingMethod().getName();

	testStart(testName);
	driver = getDriver();
	// Login
	LoginPage loginPage = new LoginPage(driver);
	TestReporter.assertTrue(loginPage.pageLoaded(),"Verify login page is displayed");
	loginPage.login(role);

	// Verify user is logged in
	TopNavigationBar topNavigationBar = new TopNavigationBar(driver);
	TestReporter.assertTrue(topNavigationBar.isLoggedIn(), "Validate the user logged in successfully");

	//Navigate to Employees Page
	topNavigationBar.clickEmployeesLink();
	EmployeesPage employeesPage = new EmployeesPage(driver);
	TestReporter.assertTrue(employeesPage.pageLoaded(),"Verify Employees page is displayed");
	
	//Record the current amount of employees displayed and click Direct
	int currentAmount = employeesPage.getTotalDisplayedEmployees();
	employeesPage.clickShowDirect();
	TestReporter.assertTrue(employeesPage.validateEmployeeTableResultsUpdated(currentAmount), "Verify Employee table updated successfully");
	
	// logout
	topNavigationBar.clickLogout();
    }
    
    @Features("Manage Employees")
    @Stories("I can filter on the Employee Page for Employees that are labelled inactive")
    @Severity(SeverityLevel.MINOR)
    @Title("Manage Employees - Filter Inactive Employees")
    @Test(dataProvider = "dataScenario", groups = { "regression", "manageEmployees", "filterEmployees" })
    public void testFilterInactiveEmployee(@Parameter String testScenario, @Parameter String role) {
	
	testName = new Object() {
	}.getClass().getEnclosingMethod().getName();

	testStart(testName);
	
	// Login
	LoginPage loginPage = new LoginPage(driver);
	TestReporter.assertTrue(loginPage.pageLoaded(),"Verify login page is displayed");
	loginPage.login(role);

	// Verify user is logged in
	TopNavigationBar topNavigationBar = new TopNavigationBar(driver);
	TestReporter.assertTrue(topNavigationBar.isLoggedIn(), "Validate the user logged in successfully");

	//Navigate to Employees Page
	topNavigationBar.clickEmployeesLink();
	EmployeesPage employeesPage = new EmployeesPage(driver);
	TestReporter.assertTrue(employeesPage.pageLoaded(),"Verify Employees page is displayed");
	
	//Record the current amount of employees displayed and click Direct
	employeesPage.checkInactiveCheckbox();
	int currentAmount = employeesPage.getTotalDisplayedEmployees();
	employeesPage.uncheckInactiveCheckbox();
	TestReporter.assertTrue(employeesPage.validateEmployeeTableResultsUpdated(currentAmount), "Verify Employee table updated successfully");
	
	// logout
	topNavigationBar.clickLogout();
    }
    
    @Features("Manage Employees")
    @Stories("I can filter back to all Employees after filtering Direct Employees")
    @Severity(SeverityLevel.MINOR)
    @Title("Manage Employees - Show All Employees")
    @Test(dataProvider = "dataScenario", groups = { "regression", "manageEmployees", "filterEmployees" })
    public void testFilterShowAllEmployee(@Parameter String testScenario, @Parameter String role) {
	
	testName = new Object() {
	}.getClass().getEnclosingMethod().getName();

	testStart(testName);
	
	// Login
	LoginPage loginPage = new LoginPage(driver);
	TestReporter.assertTrue(loginPage.pageLoaded(),"Verify login page is displayed");
	loginPage.login(role);

	// Verify user is logged in
	TopNavigationBar topNavigationBar = new TopNavigationBar(driver);
	TestReporter.assertTrue(topNavigationBar.isLoggedIn(), "Validate the user logged in successfully");

	//Navigate to Employees Page
	topNavigationBar.clickEmployeesLink();
	EmployeesPage employeesPage = new EmployeesPage(driver);
	TestReporter.assertTrue(employeesPage.pageLoaded(),"Verify Employees page is displayed");
	
	//Record the current amount of employees displayed and click Direct
	
	employeesPage.clickShowDirect();
	int currentAmount = employeesPage.getTotalDisplayedEmployees();
	employeesPage.clickShowAll();
	TestReporter.assertTrue(employeesPage.validateEmployeeTableResultsUpdated(currentAmount), "Verify Employee table updated successfully");
	
	// logout
	topNavigationBar.clickLogout();
    }
}