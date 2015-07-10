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

public class SortEmployeeTableColumns  extends TestEnvironment {

    private String application = "Bluesource";
    
    @DataProvider(name = "dataScenario")
    public Object[][] scenarios() {
	return new ExcelDataProvider(Constants.BLUESOURCE_DATAPROVIDER_PATH
		+ "ManageEmployees.xlsx", "SortEmployeeTableColumns").getTestData();
    }

    @BeforeTest(groups = { "regression", "manageEmployees", "sortEmployeeTableColumns" })
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

    @AfterMethod(groups = { "regression", "manageEmployees", "sortEmployeeTableColumns" })
    public synchronized void closeSession(ITestResult test) {
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
    @Stories("I can sort on a column an Employee on the Employee Page")
    @Title("Manage Employees - Sort Employee Table Columns")
    @Severity(SeverityLevel.TRIVIAL)
    @Test(dataProvider = "dataScenario", groups = { "regression", "manageEmployees", "sortEmployeeTableColumns" })
    public void testSortColumns(@Parameter String testScenario, @Parameter String role, @Parameter String column, @Parameter String order) {
	
	testName = new Object() {
	}.getClass().getEnclosingMethod().getName();

	testStart(testName);
	
	// Login
	LoginPage loginPage = new LoginPage(this);
	TestReporter.assertTrue(loginPage.pageLoaded(),"Verify login page is displayed");
	loginPage.login(role);

	// Verify user is logged in
	TopNavigationBar topNavigationBar = new TopNavigationBar(this);
	TestReporter.assertTrue(topNavigationBar.isLoggedIn(), "Validate the user logged in successfully");

	//Navigate to Employees Page
	topNavigationBar.clickEmployeesLink();
	EmployeesPage employeesPage = new EmployeesPage(this);
	TestReporter.assertTrue(employeesPage.pageLoaded(),"Verify Employees page is displayed");
	
	//Search for Employee
	employeesPage.sortColumn(column, order);
	TestReporter.assertTrue(employeesPage.validateSortColumn(column, order), "Validate column " + column + " was found sorted in " + order + " order");

	// logout
	topNavigationBar.clickLogout();
    }
    
}