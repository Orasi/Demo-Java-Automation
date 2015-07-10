package com.orasi.bluesource.features.manageProjects;

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
import com.orasi.apps.bluesource.projectsPage.ProjectsPage;
import com.orasi.utils.Constants;
import com.orasi.utils.TestEnvironment;
import com.orasi.utils.TestReporter;
import com.orasi.utils.dataProviders.ExcelDataProvider;

public class SearchForProject  extends TestEnvironment {

    private String application = "Bluesource";
    
    @DataProvider(name = "dataScenario")
    public Object[][] scenarios() {
	return new ExcelDataProvider(Constants.BLUESOURCE_DATAPROVIDER_PATH
		+ "ManageProjects.xlsx", "SearchForProjects").getTestData();
    }

    @BeforeTest(groups = { "regression", "manageProject", "searchProject" })
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

    @AfterMethod(groups = { "regression", "manageProjects", "searchProject" })
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
    @Features("Manage Projects")
    @Stories("I can search for an Project on the Projects Page using any criteria")
    @Severity(SeverityLevel.CRITICAL)
    @Title("Manage Projects - Search For Project")
    @Test(dataProvider = "dataScenario", groups = { "regression", "manageProjects", "searchProject" })
    public void testSearchProject(@Parameter String testScenario, @Parameter String role, @Parameter String searchText, @Parameter String column) {
	
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
	topNavigationBar.clickProjectsLink();
	ProjectsPage projectsPage = new ProjectsPage(this);
	TestReporter.assertTrue(projectsPage.pageLoaded(),"Verify Projects page is displayed");
	
	//Search for Employee
	projectsPage.clickInactiveButton();
	projectsPage.enterSearchText(searchText);
	TestReporter.assertTrue(projectsPage.validateTextInTable(searchText, column), "Validate " + searchText + " was found under the " + column + " column");

	// logout
	topNavigationBar.clickLogout();
    }
    
}