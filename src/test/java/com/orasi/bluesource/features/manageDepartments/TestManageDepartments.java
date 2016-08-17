package com.orasi.bluesource.features.manageDepartments;

import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.orasi.bluesource.LoginPage;
import com.orasi.bluesource.commons.TopNavigationBar;
import com.orasi.bluesource.departmentsPage.ListingDepartmentsPage;
import com.orasi.bluesource.departmentsPage.ManageDepartmentPage;
import com.orasi.utils.Constants;
import com.orasi.utils.OrasiDriver;
import com.orasi.utils.Randomness;
import com.orasi.utils.TestEnvironment;
import com.orasi.utils.TestReporter;
import com.orasi.utils.dataProviders.ExcelDataProvider;

import ru.yandex.qatools.allure.annotations.Features;
import ru.yandex.qatools.allure.annotations.Parameter;
import ru.yandex.qatools.allure.annotations.Severity;
import ru.yandex.qatools.allure.annotations.Stories;
import ru.yandex.qatools.allure.annotations.Title;
import ru.yandex.qatools.allure.model.SeverityLevel;

public class TestManageDepartments  extends TestEnvironment {

    private String departmentName = "";
    private String subDepartment = "";
    private String mainDepartment = "Rural Testing";
    private OrasiDriver driver = null;
    @DataProvider(name = "dataScenario")
    public Object[][] scenarios() {
	return new ExcelDataProvider(Constants.BLUESOURCE_DATAPROVIDER_PATH + "TestAddNewDept.xlsx", "TestAddNewDept").getTestData();
    }
    
    @BeforeClass( alwaysRun=true)
    @Parameters({ "runLocation", "browserUnderTest", "browserVersion", "operatingSystem", "environment" })
    public void setup(String runLocation, String browserUnderTest, String browserVersion, String operatingSystem, String environment) {
	setApplicationUnderTest("Bluesource");
	setBrowserUnderTest(browserUnderTest);
	setBrowserVersion(browserVersion);
	setOperatingSystem(operatingSystem);
	setRunLocation(runLocation);
	setTestEnvironment(environment);
	//setThreadDriver(true);
    }

    @AfterClass( alwaysRun=true)
    public void closeSession(ITestContext test) {
	endTest(testName, test);
    }    

    @Features("Manage Departments")
    @Stories("As an admin, I should be able to create a new department")
    @Severity(SeverityLevel.BLOCKER)
    @Title("ManageDepartments - Create Department")
    @Test(dataProvider = "dataScenario", groups = { "demo", "regression","manageDepartments", "qaOnly" })
    public void testAddDepartment(@Parameter String testScenario, @Parameter String role,
	    @Parameter String newDept) {
	
    	
	   	setTestName("Manage Departments_" + getBrowserUnderTest() + "_" + getOperatingSystem());
		
	   	//Create a unique department name
		departmentName = newDept;
		if (departmentName.equalsIgnoreCase("RANDOM")){
			departmentName = Randomness.randomAlphaNumeric(11);
		}
	
		TestReporter.logScenario("This test creates a new department, verifies the department was created successfully,"
				+ " then deletes the department & verifies the department was deleted successfully");
		
		//Launch browser & create driver
		testStart(testName);
		driver = getDriver();
		
		// Login
		TestReporter.logStep("Login to Bluesource");
		LoginPage loginPage = new LoginPage(driver);
		TestReporter.assertTrue(loginPage.pageLoaded(),"Verify login page is displayed");
		loginPage.login(role);
	
		// Verify user is logged in
		TestReporter.logStep("Verify successful login");
		TopNavigationBar topNavigationBar = new TopNavigationBar(driver);
		TestReporter.assertTrue(topNavigationBar.isLoggedIn(), "Validate the user logged in successfully");
	
		// Navigate to the dept page
		TestReporter.logStep("Navigate to Departments page");
		topNavigationBar.clickAdminLink();
		topNavigationBar.clickDepartmentsLink();
	
		// Verify navigated to the dept page
		ListingDepartmentsPage deptPage = new ListingDepartmentsPage(driver);
		TestReporter.assertTrue(deptPage.pageLoaded(),"Verify list of departments page is displayed");
		
		// Add a new dept
		TestReporter.logStep("Add a new department: [" + departmentName + "]");
		deptPage.clickAddDeptLink();
		ManageDepartmentPage manageDepartmentPage = new ManageDepartmentPage(driver);
		manageDepartmentPage.createDepartment(departmentName);
	
		TestReporter.logStep("Verify successful department creation");
		// Verify the dept is added
		TestReporter.assertTrue(deptPage.isSuccessMsgDisplayed(), "Validate success message appears");
	
		// Verify the dept is displayed on the dept results table
		TestReporter.logStep("Verify new department displays in the list of departments");
		TestReporter.assertTrue(deptPage.searchTableByDept(departmentName), "Validate new department exists in table");
	
    }
    
    @Features("Manage Departments")
    @Stories("As an admin, I should be able to delete a department")
    @Severity(SeverityLevel.NORMAL)
    @Title("ManageDepartments - Delete Department")
    @Test(groups = { "demo", "regression","manageDepartments", "qaOnly" },dependsOnMethods= "testAddDepartment")
    public void testDeleteDepartment() {
	
		ListingDepartmentsPage deptPage = new ListingDepartmentsPage(driver);
		TestReporter.assertTrue(deptPage.pageLoaded(),"Verify list of departments page is displayed");
		
		// Delete the new dept
		TestReporter.logStep("Delete new department: [" + departmentName + "]");
		deptPage.deleteDepartment(departmentName, getBrowserUnderTest());
	
		// Verify the dept is deleted
		TestReporter.logStep("Verify new department is deleted");
		ListingDepartmentsPage refreshedPage = new ListingDepartmentsPage(driver);
		TestReporter.assertTrue(refreshedPage.isSuccessMsgDisplayed(), "Validate success message appears");
		
		//Logout
		TestReporter.logStep("Logout of application");
		TopNavigationBar topNavigationBar = new TopNavigationBar(driver);
		topNavigationBar.clickLogout();
    }
    
    
}