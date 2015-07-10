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

public class ShowProjectsPerPage  extends TestEnvironment {

    private String application = "Bluesource";

    @BeforeTest(groups = { "regression", "manageProjects", "showProjectsPerPage" })
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

    @AfterMethod(groups = { "regression", "manageProjects", "showProjectsPerPage" })
    public synchronized void closeSession(ITestResult result) {
	if(!result.isSuccess() || result.getMethod().getMethodName().equals("testShow20ProjectsPerPage"))
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
    @Stories("I can select the number of Projects that is displayed")
    @Severity(SeverityLevel.MINOR)
    @Title("Manage Projects - Show 5 Projects Per Page")
    @Test(groups = { "regression", "manageProjects", "showProjectsPerPage" })
    public void testShow5ProjectsPerPage() {
	
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

	//Navigate to Projects Page
	topNavigationBar.clickProjectsLink();
	ProjectsPage projectsPage = new ProjectsPage(this);
	TestReporter.assertTrue(projectsPage.pageLoaded(),"Verify Projects page is displayed");
	
	//Search for Project
	projectsPage.clickInactiveButton();
	projectsPage.setRowsPerPageDisplayed("5");
	TestReporter.assertTrue(projectsPage.validateRowsPerPageDisplayed("5"), "Validate 5 rows was displayed on the Project Table");

	// logout
	//topNavigationBar.clickLogout();
    }
    
    @Features("Manage Projects")
    @Stories("I can select the number of Projects that is displayed")
    @Severity(SeverityLevel.MINOR)
    @Title("Manage Projects - Show 10 Projects Per Page")
    @Test(groups = { "regression", "manageProjects", "showProjectsPerPage" }, dependsOnMethods="testShow5ProjectsPerPage")
    public void testShow10ProjectsPerPage() {
	
	ProjectsPage projectsPage = new ProjectsPage(this);
	
	projectsPage.setRowsPerPageDisplayed("10");
	TestReporter.assertTrue(projectsPage.validateRowsPerPageDisplayed("10"), "Validate 10 rows was displayed on the Project Table");

	// logout
	//topNavigationBar.clickLogout();
    }
    
    @Features("Manage Projects")
    @Stories("I can select the number of Projects that is displayed")
    @Severity(SeverityLevel.MINOR)
    @Title("Manage Projects - Show 15 Projects Per Page")
    @Test(groups = { "regression", "manageProjects", "showProjectsPerPage" }, dependsOnMethods="testShow10ProjectsPerPage")
    public void testShow15ProjectsPerPage() {
	
	ProjectsPage projectsPage = new ProjectsPage(this);
	
	projectsPage.setRowsPerPageDisplayed("15");
	TestReporter.assertTrue(projectsPage.validateRowsPerPageDisplayed("15"), "Validate 15 rows was displayed on the Project Table");

	// logout
	//topNavigationBar.clickLogout();
    }
    
    @Features("Manage Projects")
    @Stories("I can select the number of Projects that is displayed")
    @Severity(SeverityLevel.MINOR)
    @Title("Manage Projects - Show 20 Projects Per Page")
    @Test(groups = { "regression", "manageProjects", "showProjectsPerPage" }, dependsOnMethods="testShow15ProjectsPerPage")
    public void testShow20ProjectsPerPage() {
	
	ProjectsPage projectsPage = new ProjectsPage(this);
	
	projectsPage.setRowsPerPageDisplayed("20");
	TestReporter.assertTrue(projectsPage.validateRowsPerPageDisplayed("20"), "Validate 20 rows was displayed on the Project Table");

	// logout
	TopNavigationBar topNavigationBar = new TopNavigationBar(this);
	topNavigationBar.clickLogout();
    }
    
}