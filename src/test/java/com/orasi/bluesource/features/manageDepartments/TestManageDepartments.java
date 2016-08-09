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
    @Stories("Given when I login as an admin role, I can create a Department")
    @Severity(SeverityLevel.BLOCKER)
    @Title("ManageDepartments - Create Department")
    @Test(dataProvider = "dataScenario", groups = { "demo", "regression","manageDepartments", "qaOnly" })
    public void testAddDepartment(@Parameter String testScenario, @Parameter String role,
	    @Parameter String newDept) {
	
   	setTestName("Manage Departments_" + getBrowserUnderTest() + "_" + getOperatingSystem());
	
	departmentName = newDept;
	if (departmentName.equalsIgnoreCase("RANDOM")){
		departmentName = Randomness.randomAlphaNumeric(11);
	}

	testStart(testName);
	driver = getDriver();
	
	// Login
	TestReporter.log("Login to Bluesource");
	LoginPage loginPage = new LoginPage(driver);
	TestReporter.assertTrue(loginPage.pageLoaded(),"Verify login page is displayed");
	loginPage.login(role);

	// Verify user is logged in
	TestReporter.log("Verify successful login");
	TopNavigationBar topNavigationBar = new TopNavigationBar(driver);
	TestReporter.assertTrue(topNavigationBar.isLoggedIn(), "Validate the user logged in successfully");

	// Navigate to the dept page
	TestReporter.log("Navigate to Departments page");
	topNavigationBar.clickAdminLink();
	topNavigationBar.clickDepartmentsLink();

	// Verify navigated to the dept page
	ListingDepartmentsPage deptPage = new ListingDepartmentsPage(driver);
	TestReporter.assertTrue(deptPage.pageLoaded(),"Verify list of departments page is displayed");
	
	// Add a new dept
	TestReporter.log("Add a new department");
	deptPage.clickAddDeptLink();
	ManageDepartmentPage manageDepartmentPage = new ManageDepartmentPage(driver);
	TestReporter.assertTrue(manageDepartmentPage.pageLoaded(), "Verify manage department page is displayed");
	manageDepartmentPage.createDepartment(departmentName);

	TestReporter.log("Verify successful department creation");
	// Verify the dept is added
	TestReporter.assertTrue(deptPage.isSuccessMsgDisplayed(), "Validate success message appears");

	// Verify the dept is displayed on the dept results table
	TestReporter.assertTrue(deptPage.searchTableByDept(departmentName), "Validate new department exists in table");
	
    }
    
    @Features("Manage Departments")
    @Stories("Given when I login as an admin role, I can modify a Departments name")
    @Severity(SeverityLevel.BLOCKER)
    @Title("ManageDepartments - Modify Department Name")
    @Test(groups = {"demo",  "regression","manageDepartments", "qaOnly"   },dependsOnMethods= "testAddDepartment")
    public void testModifyDepartmentName() {
	
	
	ListingDepartmentsPage deptPage = new ListingDepartmentsPage(driver);
	TestReporter.assertTrue(deptPage.pageLoaded(driver),"Verify list of departments page is displayed");

	// Add a new dept
	TestReporter.log("Modify the new department's name");
	deptPage.clickModifyDepartment(departmentName);
	ManageDepartmentPage manageDepartmentPage = new ManageDepartmentPage(driver);
	TestReporter.assertTrue(manageDepartmentPage.pageLoaded(), "Verify manage department page is displayed");
	departmentName += "_modified";
	manageDepartmentPage.modifyDepartmentName(departmentName);

	// Verify the success message
	TestReporter.log("Verify successful name modification");
	TestReporter.assertTrue(deptPage.isSuccessMsgDisplayed(), "Validate success message appears");

	// Verify the dept is displayed on the dept results table
	TestReporter.assertTrue(deptPage.searchTableByDept(departmentName), "Validate modified department exists in table");

    }
    
    @Features("Manage Departments")
    @Stories("Given when I login as an admin role, I can modify a Departments parent")
    @Severity(SeverityLevel.NORMAL)
    @Title("ManageDepartments - Modify Department Parent")
    @Test(groups = { "demo", "regression","manageDepartments" , "qaOnly" },dependsOnMethods= "testModifyDepartmentName")
    public void testModifyDepartmentParent() {
	
	
	ListingDepartmentsPage deptPage = new ListingDepartmentsPage(driver);
	TestReporter.assertTrue(deptPage.pageLoaded(),"Verify list of departments page is displayed");

	// Add a new dept
	TestReporter.log("Modify new department's parent");
	deptPage.clickModifyDepartment(departmentName);
	ManageDepartmentPage manageDepartmentPage = new ManageDepartmentPage(driver);
	TestReporter.assertTrue(manageDepartmentPage.pageLoaded(), "Verify manage department page is displayed");
	manageDepartmentPage.modifyDepartmentsParent(mainDepartment);

	// Verify the success message
	TestReporter.assertTrue(deptPage.isSuccessMsgDisplayed(), "Validate success message appears");

	// Verify the dept is displayed on the dept results table
	TestReporter.log("Verify new department's parent was associated");
	TestReporter.assertTrue(deptPage.isSubdepartment(departmentName, mainDepartment), "Verify new department is a direct Subdepartment");
	deptPage.clickModifyDepartment(departmentName);
	TestReporter.assertTrue(manageDepartmentPage.validateCorrectParentDepartment("Rural Testing"), "Verify correct Parent department is displayed");
	
	manageDepartmentPage.clickUpdateButton();
    }
    
    @Features("Manage Departments")
    @Stories("Given when I login as an admin role, I can modify a Departments Increment Hours")
    @Severity(SeverityLevel.NORMAL)
    @Title("ManageDepartments - Modify Department Increment Hours")
    @Test(groups = {"demo",  "regression","manageDepartments", "qaOnly"  },dependsOnMethods= "testModifyDepartmentParent")
    public void testModifyDeparmentIncrementHours() {
	
	
	ListingDepartmentsPage deptPage = new ListingDepartmentsPage(driver);
	TestReporter.assertTrue(deptPage.pageLoaded(),"Verify list of departments page is displayed");

	// Add a new dept
	TestReporter.log("Modify new department's Increment Hours");
	deptPage.clickModifyDepartment(departmentName);
	ManageDepartmentPage manageDepartmentPage = new ManageDepartmentPage(driver);
	TestReporter.assertTrue(manageDepartmentPage.pageLoaded(), "Verify manage department page is displayed");
	manageDepartmentPage.modifyDepartmentsIncrementHours("8");

	// Verify the success message
	TestReporter.assertTrue(deptPage.isSuccessMsgDisplayed(), "Validate success message appears");

	// Verify the dept is displayed on the dept results table
	TestReporter.log("Verify new department's Increment Hours updated");
	TestReporter.assertTrue(deptPage.isSubdepartment(departmentName, mainDepartment), "Verify new department is a direct Subdepartment");
	deptPage.clickModifyDepartment(departmentName);
	TestReporter.assertTrue(manageDepartmentPage.validateCorrectIncrementHours("8"), "Verify correct Increment Hours is displayed");
	
	manageDepartmentPage.clickUpdateButton();
    }
    
    @Features("Manage Departments")
    @Stories("Given when I login as an admin role, I can add a Subdepartment")
    @Severity(SeverityLevel.NORMAL)
    @Title("ManageDepartments - Add Subdepartment")
    @Test(groups = {"demo",  "regression","manageDepartments", "qaOnly" },dependsOnMethods= "testModifyDeparmentIncrementHours")
    public void testAddSubdepartment() {
	
	subDepartment = departmentName + "_subdepartment";
	ListingDepartmentsPage deptPage = new ListingDepartmentsPage(driver);
	TestReporter.assertTrue(deptPage.pageLoaded(),"Verify list of departments page is displayed");

	// Add a new dept
	TestReporter.log("Add new sub-deparment to new department");
	deptPage.clickAddSubDepartment(departmentName);
	ManageDepartmentPage manageDepartmentPage = new ManageDepartmentPage(driver);
	TestReporter.assertTrue(manageDepartmentPage.pageLoaded(), "Verify manage department page is displayed");
	TestReporter.assertTrue(manageDepartmentPage.validateCorrectParentDepartment(departmentName), "Verify correct Parent department is displayed");
	
	manageDepartmentPage.createDepartment(subDepartment);

	// Verify the success message
	TestReporter.assertTrue(deptPage.isSuccessMsgDisplayed(), "Validate success message appears");

	
	// Verify the dept is displayed on the dept results table
	TestReporter.log("Verify sub-deparment was created");
	TestReporter.assertTrue(deptPage.isSubdepartment(subDepartment, departmentName), "Verify new department is a direct Subdepartment");
    }
    
    @Features("Manage Departments")
    @Stories("Given when I login as an admin role, I can delete departments")
    @Severity(SeverityLevel.NORMAL)
    @Title("ManageDepartments - Delete Department")
    @Test(groups = { "demo", "regression","manageDepartments", "qaOnly" },dependsOnMethods= "testAddSubdepartment")
    public void testDeleteDepartment() {
	
	ListingDepartmentsPage deptPage = new ListingDepartmentsPage(driver);
	TestReporter.assertTrue(deptPage.pageLoaded(),"Verify list of departments page is displayed");
	
	TestReporter.log("Delete new department");
	// Delete the new dept
	deptPage.deleteDepartment(departmentName);

	// Verify the title is deleted
	ListingDepartmentsPage refreshedPage = new ListingDepartmentsPage(driver);
	TestReporter.assertTrue(refreshedPage.isSuccessMsgDisplayed(), "Validate success message appears");
	
	TestReporter.log("Verify new department is deleted and sub-department is moved up a domain");
	// Verify the dept is displayed on the dept results table
	TestReporter.assertTrue(deptPage.isSubdepartment(subDepartment, mainDepartment), "Verify subdepartment has moved to the parents level");
	
	
	// Delete the new dept
	TestReporter.log("Delete sub-department");
	deptPage.deleteDepartment(subDepartment);
	
	TopNavigationBar topNavigationBar = new TopNavigationBar(driver);
	topNavigationBar.clickLogout();
    }
    
    
}