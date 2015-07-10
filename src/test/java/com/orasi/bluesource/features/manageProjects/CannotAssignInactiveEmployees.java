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
import com.orasi.apps.bluesource.projectsPage.ManageProjectModal;
import com.orasi.apps.bluesource.projectsPage.Project;
import com.orasi.apps.bluesource.projectsPage.ProjectSummaryPage;
import com.orasi.apps.bluesource.projectsPage.ProjectsPage;
import com.orasi.utils.Constants;
import com.orasi.utils.TestEnvironment;
import com.orasi.utils.TestReporter;
import com.orasi.utils.dataProviders.ExcelDataProvider;

public class CannotAssignInactiveEmployees  extends TestEnvironment {

    private String application = "Bluesource";
    private String activeEmployee = "Linley Love";
    private String inactiveEmployee = "Inactive Employee";


    @BeforeTest(groups = { "regression",  "manageProjects", "projectsCRUD" })
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

    @AfterMethod(groups = { "regression",  "manageProjects", "CannotAssignInactiveEmployees" })
    public void closeSession(ITestResult result) {
	endTest(testName);
    }

    /**
     * @Summary: Creates an Employee
     * @Precondition:NA
     * @Author: Jessica Marshall
     * @Version: 10/6/2014
     * @Return: N/A
     */
    @Features("Manage Projects")
    @Stories("Ensure inactive Employees are not selectable for projects")
    @Severity(SeverityLevel.MINOR)
    @Title("Manage Projects - Cannot Assign Inactive Employees as Client Partner")
    @Test(groups = { "regression", "manageProjects", "CannotAssignInactiveEmployees" })
    public void testCannotAssignInactiveEmployeesAsClientPartner() {
	
	setTestName(new Object() {}.getClass().getEnclosingMethod().getName());

	testStart(testName);
	
	// Login
	LoginPage loginPage = new LoginPage(this);
	TestReporter.assertTrue(loginPage.pageLoaded(),"Verify login page is displayed");
	loginPage.login("COMPANY_ADMIN");

	// Verify user is logged in
	TopNavigationBar topNavigationBar = new TopNavigationBar(this);
	TestReporter.assertTrue(topNavigationBar.isLoggedIn(), "Validate the user logged in successfully");

	//Navigate to Employees Page
	topNavigationBar.clickProjectsLink();
	ProjectsPage projectsPage = new ProjectsPage(this);
	TestReporter.assertTrue(projectsPage.pageLoaded(),"Verify Projects page is displayed");

	// Open New Project Modal 
	projectsPage.clickAddProjectButton();
	ManageProjectModal newProject = new ManageProjectModal(this);	
	TestReporter.assertTrue(newProject.pageLoaded(),"Verify New Project Popup Modal is displayed");
	TestReporter.assertTrue(newProject.validateClientPartnerIsAvailible(activeEmployee), "Validate " + activeEmployee + " is selectable as a Client Partner");
	TestReporter.assertTrue(newProject.validateClientPartnerIsNotAvailible(inactiveEmployee), "Validate " + inactiveEmployee + " is not selectable as a Client Partner");
	
    }    
    
    @Features("Manage Projects")
    @Stories("Ensure inactive Employees are not selectable for projects")
    @Severity(SeverityLevel.MINOR)
    @Title("Manage Projects - Cannot Assign Inactive Employees as Test Lead")
    @Test(groups = { "regression", "manageProjects", "CannotAssignInactiveEmployees" })
    public void testCannotAssignInactiveEmployeesAsTestLead() {
	setTestName(new Object() {}.getClass().getEnclosingMethod().getName());

	testStart(testName);
	
	// Login
	LoginPage loginPage = new LoginPage(this);
	TestReporter.assertTrue(loginPage.pageLoaded(),"Verify login page is displayed");
	loginPage.login("COMPANY_ADMIN");

	// Verify user is logged in
	TopNavigationBar topNavigationBar = new TopNavigationBar(this);
	TestReporter.assertTrue(topNavigationBar.isLoggedIn(), "Validate the user logged in successfully");

	//Navigate to Employees Page
	topNavigationBar.clickProjectsLink();
	ProjectsPage projectsPage = new ProjectsPage(this);
	TestReporter.assertTrue(projectsPage.pageLoaded(),"Verify Projects page is displayed");

	// Open New Project Modal 
	projectsPage.clickAddProjectButton();
	ManageProjectModal newProject = new ManageProjectModal(this);	
	TestReporter.assertTrue(newProject.pageLoaded(),"Verify New Project Popup Modal is displayed");
	TestReporter.assertTrue(newProject.validateTeamLeadIsAvailible(activeEmployee), "Validate " + activeEmployee + " is selectable as a Team Lead");
	TestReporter.assertTrue(newProject.validateTeamLeadIsNotAvailible(inactiveEmployee), "Validate " + inactiveEmployee + " is not selectable as a Team Lead");
    }    
}
