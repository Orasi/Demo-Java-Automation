package com.orasi.bluesource.features.manageDepartments;

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
import com.orasi.apps.bluesource.departmentsPage.ListingDepartmentsPage;
import com.orasi.apps.bluesource.departmentsPage.ManageDepartmentPage;
import com.orasi.utils.Constants;
import com.orasi.utils.TestEnvironment;
import com.orasi.utils.TestReporter;
import com.orasi.utils.dataProviders.ExcelDataProvider;

public class TestManageDepartments  extends TestEnvironment {

    private String application = "Bluesource";
    private String departmentName = "";
    private String subDepartment = "";
    private String mainDepartment = "Services";
    @DataProvider(name = "dataScenario")
    public Object[][] scenarios() {
	return new ExcelDataProvider(Constants.BLUESOURCE_DATAPROVIDER_PATH
		+ "TestAddNewDept.xlsx", "TestAddNewDept").getTestData();
    }

    @BeforeTest(groups = { "regression" ,"manageDeartments"})
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

    @AfterMethod(groups = { "regression","manageDeartments"  })
    public synchronized void closeSession(ITestResult result){
	if(!result.isSuccess() || result.getMethod().getMethodName().equals("testDeleteDepartment"))
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
    @Features("Manage Departments")
    @Stories("Given when I login as an admin role, I can create a Department")
    @Severity(SeverityLevel.BLOCKER)
    @Title("ManageDepartments - Create Department")
    @Test(dataProvider = "dataScenario", groups = { "regression","manageDeartments"  })
    public void testAddDepartment(@Parameter String testScenario, @Parameter String role,
	    @Parameter String newDept) {
	
	setTestName("Manage Departments");

	testStart(testName);
	departmentName = newDept;
	// Login
	LoginPage loginPage = new LoginPage(this);
	TestReporter.assertTrue(loginPage.pageLoaded(),"Verify login page is displayed");
	loginPage.login(role);

	// Verify user is logged in
	TopNavigationBar topNavigationBar = new TopNavigationBar(this);
	TestReporter.assertTrue(topNavigationBar.isLoggedIn(), "Validate the user logged in successfully");

	// Navigate to the dept page
	topNavigationBar.clickAdminLink();
	topNavigationBar.clickDepartmentsLink();

	// Verify navigated to the dept page
	ListingDepartmentsPage deptPage = new ListingDepartmentsPage(this);
	TestReporter.assertTrue(deptPage.pageLoaded(),"Verify list of departments page is displayed");
	// Add a new dept
	deptPage.clickAddDeptLink();
	ManageDepartmentPage manageDepartmentPage = new ManageDepartmentPage(this);
	TestReporter.assertTrue(manageDepartmentPage.pageLoaded(), "Verify manage department page is displayed");
	manageDepartmentPage.createDepartment(departmentName);

	// Verify the dept is added
	TestReporter.assertTrue(deptPage.isSuccessMsgDisplayed(), "Validate success message appears");

	// Verify the dept is displayed on the dept results table
	TestReporter.assertTrue(deptPage.searchTableByDept(departmentName), "Validate new department exists in table");
	
    }
    
    @Features("Manage Departments")
    @Stories("Given when I login as an admin role, I can modify a Departments name")
    @Severity(SeverityLevel.BLOCKER)
    @Title("ManageDepartments - Modify Department Name")
    @Test(groups = { "regression","manageDeartments"  },dependsOnMethods= "testAddDepartment")
    public void testModifyDepartmentName() {
	
	
	ListingDepartmentsPage deptPage = new ListingDepartmentsPage(this);
	TestReporter.assertTrue(deptPage.pageLoaded(),"Verify list of departments page is displayed");

	// Add a new dept
	deptPage.clickModifyDepartment(departmentName);
	ManageDepartmentPage manageDepartmentPage = new ManageDepartmentPage(this);
	TestReporter.assertTrue(manageDepartmentPage.pageLoaded(), "Verify manage department page is displayed");
	departmentName += "_modified";
	manageDepartmentPage.modifyDepartmentName(departmentName);

	// Verify the success message
	TestReporter.assertTrue(deptPage.isSuccessMsgDisplayed(), "Validate success message appears");

	// Verify the dept is displayed on the dept results table
	TestReporter.assertTrue(deptPage.searchTableByDept(departmentName), "Validate modified department exists in table");

    }
    
    @Features("Manage Departments")
    @Stories("Given when I login as an admin role, I can modify a Departments parent")
    @Severity(SeverityLevel.NORMAL)
    @Title("ManageDepartments - Modify Department Parent")
    @Test(groups = { "regression","manageDeartments" },dependsOnMethods= "testModifyDepartmentName")
    public void testModifyDepartmentParent() {
	
	
	ListingDepartmentsPage deptPage = new ListingDepartmentsPage(this);
	TestReporter.assertTrue(deptPage.pageLoaded(),"Verify list of departments page is displayed");

	// Add a new dept
	deptPage.clickModifyDepartment(departmentName);
	ManageDepartmentPage manageDepartmentPage = new ManageDepartmentPage(this);
	TestReporter.assertTrue(manageDepartmentPage.pageLoaded(), "Verify manage department page is displayed");
	manageDepartmentPage.modifyDepartmentsParent(mainDepartment);

	// Verify the success message
	TestReporter.assertTrue(deptPage.isSuccessMsgDisplayed(), "Validate success message appears");

	// Verify the dept is displayed on the dept results table
	TestReporter.assertTrue(deptPage.isSubdepartment(departmentName, mainDepartment), "Verify new department is a direct Subdepartment");
	deptPage.clickModifyDepartment(departmentName);
	TestReporter.assertTrue(manageDepartmentPage.validateCorrectParentDepartment("Services"), "Verify correct Parent department is displayed");
	
	manageDepartmentPage.clickUpdateButton();
    }
    
    @Features("Manage Departments")
    @Stories("Given when I login as an admin role, I can modify a Departments Increment Hours")
    @Severity(SeverityLevel.NORMAL)
    @Title("ManageDepartments - Modify Department Increment Hours")
    @Test(groups = { "regression","manageDeartments" },dependsOnMethods= "testModifyDepartmentParent")
    public void testModifyDeparmentIncrementHours() {
	
	
	ListingDepartmentsPage deptPage = new ListingDepartmentsPage(this);
	TestReporter.assertTrue(deptPage.pageLoaded(),"Verify list of departments page is displayed");

	// Add a new dept
	deptPage.clickModifyDepartment(departmentName);
	ManageDepartmentPage manageDepartmentPage = new ManageDepartmentPage(this);
	TestReporter.assertTrue(manageDepartmentPage.pageLoaded(), "Verify manage department page is displayed");
	manageDepartmentPage.modifyDepartmentsIncrementHours("8");

	// Verify the success message
	TestReporter.assertTrue(deptPage.isSuccessMsgDisplayed(), "Validate success message appears");

	// Verify the dept is displayed on the dept results table
	TestReporter.assertTrue(deptPage.isSubdepartment(departmentName, mainDepartment), "Verify new department is a direct Subdepartment");
	deptPage.clickModifyDepartment(departmentName);
	TestReporter.assertTrue(manageDepartmentPage.validateCorrectIncrementHours("8"), "Verify correct Increment Hours is displayed");
	
	manageDepartmentPage.clickUpdateButton();
    }
    
    @Features("Manage Departments")
    @Stories("Given when I login as an admin role, I can add a Subdepartment")
    @Severity(SeverityLevel.NORMAL)
    @Title("ManageDepartments - Add Subdepartment")
    @Test(groups = { "regression","manageDeartments"},dependsOnMethods= "testModifyDeparmentIncrementHours")
    public void testAddSubdepartment() {
	
	subDepartment = departmentName + "_subdepartment";
	ListingDepartmentsPage deptPage = new ListingDepartmentsPage(this);
	TestReporter.assertTrue(deptPage.pageLoaded(),"Verify list of departments page is displayed");

	// Add a new dept
	deptPage.clickAddSubDepartment(departmentName);
	ManageDepartmentPage manageDepartmentPage = new ManageDepartmentPage(this);
	TestReporter.assertTrue(manageDepartmentPage.pageLoaded(), "Verify manage department page is displayed");
	TestReporter.assertTrue(manageDepartmentPage.validateCorrectParentDepartment(departmentName), "Verify correct Parent department is displayed");
	
	manageDepartmentPage.createDepartment(subDepartment);

	// Verify the success message
	TestReporter.assertTrue(deptPage.isSuccessMsgDisplayed(), "Validate success message appears");

	// Verify the dept is displayed on the dept results table
	TestReporter.assertTrue(deptPage.isSubdepartment(subDepartment, departmentName), "Verify new department is a direct Subdepartment");
    }
    
    @Features("Manage Departments")
    @Stories("Given when I login as an admin role, I can delete departments")
    @Severity(SeverityLevel.NORMAL)
    @Title("ManageDepartments - Delete Department")
    @Test(groups = { "regression","manageDeartments"},dependsOnMethods= "testAddSubdepartment")
    public void testDeleteDepartment() {
	
	ListingDepartmentsPage deptPage = new ListingDepartmentsPage(this);
	TestReporter.assertTrue(deptPage.pageLoaded(),"Verify list of departments page is displayed");

	// Delete the new dept
	deptPage.deleteDepartment(departmentName);

	// Verify the title is deleted
	ListingDepartmentsPage refreshedPage = new ListingDepartmentsPage(this);
	TestReporter.assertTrue(refreshedPage.isSuccessMsgDisplayed(), "Validate success message appears");
	
	// Verify the dept is displayed on the dept results table
	TestReporter.assertTrue(deptPage.isSubdepartment(subDepartment, mainDepartment), "Verify subdepartment has moved to the parents level");
	
	// Delete the new dept
	deptPage.deleteDepartment(subDepartment);
	
	TopNavigationBar topNavigationBar = new TopNavigationBar(this);
	topNavigationBar.clickLogout();
    }
    
    
}