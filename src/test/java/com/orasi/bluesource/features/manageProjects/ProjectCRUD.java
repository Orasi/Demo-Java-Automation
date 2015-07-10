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

public class ProjectCRUD  extends TestEnvironment {

    private String application = "Bluesource";
    private Project project= new Project();
    
    @DataProvider(name = "dataScenario")
    public Object[][] scenarios() {
	return new ExcelDataProvider(Constants.BLUESOURCE_DATAPROVIDER_PATH
		+ "ManageEmployees.xlsx", "AddEmployee").getTestData();
    }

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

    @AfterMethod(groups = { "regression",  "manageProjects", "projectsCRUD" })
    public void closeSession(ITestResult result) {
	if(!result.isSuccess() || result.getMethod().getMethodName().equals("testDeactivateProject"))
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
    @Stories("I can create a new Project")
    @Severity(SeverityLevel.BLOCKER)
    @Title("Manage Projects - Create Project")
    @Test(dataProvider = "dataScenario", groups = { "regression", "manageProjects", "projectsCRUD" })
    public void testAddProject(@Parameter String testScenario, @Parameter String role) {
	
	setTestName(new Object() {}.getClass().getEnclosingMethod().getName());

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

	// Open New Employee Modal 
	projectsPage.clickAddProjectButton();
	ManageProjectModal newProject = new ManageProjectModal(this);	
	TestReporter.assertTrue(newProject.pageLoaded(),"Verify New Project Popup Modal is displayed");
	
	//Enter Employee Info
	newProject.addProject(project);
	TestReporter.assertTrue(projectsPage.isSuccessMsgDisplayed(), "Verify Success message appears after creating new employee");
	
	//Validate new Employee created and visible
	projectsPage.enterSearchText(project.getProjectName());
	TestReporter.assertTrue(projectsPage.validateProjectFoundInTable(project.getProjectName()), "Verify Project " + project.getProjectName() + " appeared in the Employee Table");
    }    
    
  
    @Features("Manage Projects")
    @Stories("I can see an Project's Info after creating project")
    @Severity(SeverityLevel.NORMAL)
    @Title("Manage Projects - Verify Project Details Information")
    @Test( groups = { "regression", "manageProjects", "projectsCRUD" },
    	  dependsOnMethods = {"testAddProject"})
    public void testViewProjectInfo() {
	ProjectsPage projectsPage = new ProjectsPage(this);
	projectsPage.selectProjectName(project.getProjectName());
	
	ProjectSummaryPage summary = new ProjectSummaryPage(this);
	TestReporter.assertTrue(summary.pageLoaded(),"Verify Project's Summary page is displayed");
	TestReporter.assertTrue(summary.validateProjectInfo(project), "Verify displayed Project's Information is correct");
	
    }
 
     
    @Features("Manage Projects")
    @Stories("I can Modify an Project's Info and view changes")
    @Severity(SeverityLevel.MINOR)
    @Title("Manage Projects - Modify Project")
    @Test(groups = { "regression", "manageProjects", "projectsCRUD" },
    	  dependsOnMethods = {"testViewProjectInfo"})
    public void testModifyProjectInfo() {
	ProjectSummaryPage summary = new ProjectSummaryPage(this);
	summary.clickManageProjectInfo();;
	
	ManageProjectModal modifyProject = new ManageProjectModal(this);
	TestReporter.assertTrue(modifyProject.pageLoaded(),"Verify Manage Project Popup Modal is displayed");
	
	project.removeTeamLead("Kristi Collins");
	project.setEndDate("2017-07-09");
	
	modifyProject.modifyProject(project);
	TestReporter.assertTrue(summary.validateProjectInfo(project), "Verify Project's Information is correct");
    }
    
    
    @Features("Manage Projects")
    @Stories("I can mark an Project as Inactive")
    @Severity(SeverityLevel.MINOR)
    @Title("Manage Projects - Mark Project Inactive")
    @Test(groups = { "regression", "manageProjects", "projectsCRUD" },
    	  dependsOnMethods = {"testModifyProjectInfo"})
    public void testDeactivateProject() {
	ProjectSummaryPage summary = new ProjectSummaryPage(this);
	summary.clickManageProjectInfo();;
	
	ManageProjectModal modifyProject = new ManageProjectModal(this);
	TestReporter.assertTrue(modifyProject.pageLoaded(),"Verify Manage Project Popup Modal is displayed");
	
	
	project.setStatus("Inactive");
	modifyProject.modifyProject(project);
	TestReporter.assertTrue(summary.validateProjectInfo(project), "Verify Project's General Information is correct");
	
	TopNavigationBar topNavigationBar = new TopNavigationBar(this);
	topNavigationBar.clickProjectsLink();
	
	ProjectsPage projectsPage = new ProjectsPage(this);
	TestReporter.assertTrue(projectsPage.pageLoaded(),"Verify Projects page is displayed");
	projectsPage.enterSearchText(project.getProjectName());
	TestReporter.assertTrue(projectsPage.validateNoRowsFound(),"Verify Projects Table does not have Project as active");
	topNavigationBar.clickLogout();
    }
}
