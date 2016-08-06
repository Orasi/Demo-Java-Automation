package com.orasi.bluesource.features.manageProjects;

import org.testng.ITestContext;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.orasi.apps.bluesource.LoginPage;
import com.orasi.apps.bluesource.commons.TopNavigationBar;
import com.orasi.apps.bluesource.projectsPage.ManageProjectModal;
import com.orasi.apps.bluesource.projectsPage.Project;
import com.orasi.apps.bluesource.projectsPage.ProjectSummaryPage;
import com.orasi.apps.bluesource.projectsPage.ProjectsPage;
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

public class ProjectCRUD  extends TestEnvironment {

    private Project project= new Project();
    private OrasiDriver driver = null;
    
    @BeforeTest(alwaysRun=true)
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

    @AfterTest(alwaysRun=true)
    public void closeSession(ITestContext test) {
	endTest(testName, test);
    }    

    @DataProvider(name = "dataScenario")
    public Object[][] scenarios() {
	return new ExcelDataProvider(Constants.BLUESOURCE_DATAPROVIDER_PATH + "ManageEmployees.xlsx", "AddEmployee").getTestData();
    }

    @Features("Manage Projects")
    @Stories("I can create a new Project")
    @Severity(SeverityLevel.BLOCKER)
    @Title("Manage Projects - Create Project")
    @Test(dataProvider = "dataScenario", groups = { "regression", "manageProjects", "projectsCRUD" , "qaOnly"})
    public void testAddProject(@Parameter String testScenario, @Parameter String role) {
	
	setTestName(new Object() {}.getClass().getEnclosingMethod().getName());

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
	topNavigationBar.clickProjectsLink();
	ProjectsPage projectsPage = new ProjectsPage(driver);
	TestReporter.assertTrue(projectsPage.pageLoaded(),"Verify Projects page is displayed");

	// Open New Employee Modal 
	projectsPage.clickAddProjectButton();
	ManageProjectModal newProject = new ManageProjectModal(driver);	
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
    @Test( groups = { "regression", "manageProjects", "projectsCRUD" , "qaOnly"},
    	  dependsOnMethods = {"testAddProject"})
    public void testViewProjectInfo() {
	ProjectsPage projectsPage = new ProjectsPage(driver);
	projectsPage.selectProjectName(project.getProjectName());
	
	ProjectSummaryPage summary = new ProjectSummaryPage(driver);
	TestReporter.assertTrue(summary.pageLoaded(),"Verify Project's Summary page is displayed");
	TestReporter.assertTrue(summary.validateProjectInfo(project), "Verify displayed Project's Information is correct");
	
    }
 
     
    @Features("Manage Projects")
    @Stories("I can Modify an Project's Info and view changes")
    @Severity(SeverityLevel.MINOR)
    @Title("Manage Projects - Modify Project")
    @Test(groups = { "regression", "manageProjects", "projectsCRUD", "qaOnly" },
    	  dependsOnMethods = {"testViewProjectInfo"})
    public void testModifyProjectInfo() {
	ProjectSummaryPage summary = new ProjectSummaryPage(driver);
	summary.clickManageProjectInfo();;
	
	ManageProjectModal modifyProject = new ManageProjectModal(driver);
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
    @Test(groups = { "regression", "manageProjects", "projectsCRUD", "qaOnly" },
    	  dependsOnMethods = {"testModifyProjectInfo"})
    public void testDeactivateProject() {
	ProjectSummaryPage summary = new ProjectSummaryPage(driver);
	summary.clickManageProjectInfo();;
	
	ManageProjectModal modifyProject = new ManageProjectModal(driver);
	TestReporter.assertTrue(modifyProject.pageLoaded(),"Verify Manage Project Popup Modal is displayed");
	
	
	project.setStatus("Inactive");
	modifyProject.modifyProject(project);
	TestReporter.assertTrue(summary.validateProjectInfo(project), "Verify Project's General Information is correct");
	
	TopNavigationBar topNavigationBar = new TopNavigationBar(driver);
	topNavigationBar.clickProjectsLink();
	
	ProjectsPage projectsPage = new ProjectsPage(driver);
	TestReporter.assertTrue(projectsPage.pageLoaded(),"Verify Projects page is displayed");
	projectsPage.enterSearchText(project.getProjectName());
	TestReporter.assertTrue(projectsPage.validateNoRowsFound(),"Verify Projects Table does not have Project as active");
	topNavigationBar.clickLogout();
    }
}
