package com.orasi.bluesource.features.manageProjects;

import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.orasi.bluesource.LoginPage;
import com.orasi.bluesource.commons.TopNavigationBar;
import com.orasi.bluesource.projectsPage.ProjectsPage;
import com.orasi.utils.OrasiDriver;
import com.orasi.utils.TestEnvironment;
import com.orasi.utils.TestReporter;

import ru.yandex.qatools.allure.annotations.Features;
import ru.yandex.qatools.allure.annotations.Severity;
import ru.yandex.qatools.allure.annotations.Stories;
import ru.yandex.qatools.allure.annotations.Title;
import ru.yandex.qatools.allure.model.SeverityLevel;

public class ShowProjectsPerPage  extends TestEnvironment {

    private OrasiDriver driver = null;
    @BeforeClass(alwaysRun=true)
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

    @AfterClass(alwaysRun=true)
    public synchronized void closeSession(ITestContext test) {
	endTest(testName, test);
    }    

    @Features("Manage Projects")
    @Stories("I can select the number of Projects that is displayed")
    @Severity(SeverityLevel.MINOR)
    @Title("Manage Projects - Show 5 Projects Per Page")
    @Test(groups = { "regression", "manageProjects", "showProjectsPerPage" })
    public void testShow5ProjectsPerPage() {
	
	testName = new Object() {
	}.getClass().getEnclosingMethod().getName();

	testStart(testName);
	driver = getDriver();
	// Login
	LoginPage loginPage = new LoginPage(driver);
	TestReporter.assertTrue(loginPage.pageLoaded(),"Verify login page is displayed");
	loginPage.login("COMPANY_ADMIN");

	// Verify user is logged in
	TopNavigationBar topNavigationBar = new TopNavigationBar(driver);
	TestReporter.assertTrue(topNavigationBar.isLoggedIn(), "Validate the user logged in successfully");

	//Navigate to Projects Page
	topNavigationBar.clickProjectsLink();
	ProjectsPage projectsPage = new ProjectsPage(driver);
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
	
	ProjectsPage projectsPage = new ProjectsPage(driver);
	
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
	
	ProjectsPage projectsPage = new ProjectsPage(driver);
	
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
	
	ProjectsPage projectsPage = new ProjectsPage(driver);
	
	projectsPage.setRowsPerPageDisplayed("20");
	TestReporter.assertTrue(projectsPage.validateRowsPerPageDisplayed("20"), "Validate 20 rows was displayed on the Project Table");

	// logout
	TopNavigationBar topNavigationBar = new TopNavigationBar(driver);
	topNavigationBar.clickLogout();
    }
    
}