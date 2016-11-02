package com.orasi.bluesource.features;

import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.orasi.bluesource.LoginPage;
import com.orasi.bluesource.commons.TopNavigationBar;
import com.orasi.bluesource.titlesPage.ListingTitlesPage;
import com.orasi.bluesource.titlesPage.ManageTitlePage;
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

public class FailedTest extends TestEnvironment{

   // private OrasiDriver driver = null;
    private String title = "";
    
    @DataProvider(name = "dataScenario")
    public Object[][] scenarios() {
	return new ExcelDataProvider(Constants.BLUESOURCE_DATAPROVIDER_PATH + "TestAddNewTitle.xlsx", "TestAddNewTitle").getTestData();
    }
    
    @BeforeClass(alwaysRun=true)
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

    @AfterClass(alwaysRun=true)
    public void closeSession(ITestContext test) {
		endTest(testName, test);
    }    

    @Features("Manage Titles")
    @Stories("As an admin I should be able to create a title")
    @Title("Manage Titles - Create")
    @Severity(SeverityLevel.CRITICAL)
    @Test(dataProvider = "dataScenario", groups = {"demo",  "regression", "manageTitles" , "qaOnly"})
    public void testCreateTitle(@Parameter String testScenario, @Parameter String role,
	    @Parameter String newTitle) {
    	
		testName = "Failed Test Example";
		
		//Create a unique title
		title = "";
		
		TestReporter.logScenario("This test creates a new title, verifies the title was created successfully,"
				+ " the modifies the title name, then deletes the title & verifies the title was deleted successfully");
		
		//Launch browser & create driver
		testStart(testName);
		//driver = getDriver();
	
		// Login
		TestReporter.logStep("Login to application");
		LoginPage loginPage = new LoginPage( getDriver());
		TestReporter.assertTrue(loginPage.pageLoaded(), "Verify login page is displayed");
		loginPage.login(role);
	
		// Verify user is logged in
		TopNavigationBar topNavigationBar = new TopNavigationBar( getDriver());
		TestReporter.assertTrue(topNavigationBar.isLoggedIn(), "Validate the user logged in successfully");
	
		// Navigate to the title page
		TestReporter.logStep("Navigate to the Titles page");
		topNavigationBar.clickAdminLink();
		topNavigationBar.clickTitlesLink();
	
		// Verify navigated to the title page
		ListingTitlesPage listingTitlesPage = new ListingTitlesPage(getDriver());
		TestReporter.assertTrue(listingTitlesPage.pageLoaded(),"Verify listing titles page is displayed");

		// Click new title
		TestReporter.logStep("Click the button to create a new title");
		listingTitlesPage.clickNewTitle();
	
		// Instantiate the Manage titles page and create a new title
		ManageTitlePage manageTitlePage = new ManageTitlePage( getDriver());
		TestReporter.assertTrue(manageTitlePage.pageLoaded(),"Verify manage title page is displayed");
		TestReporter.logStep("Create a new title: [" + title + "]");
		manageTitlePage.createNewTitle(title);
	
		// Verify the title was created
		TestReporter.assertTrue(listingTitlesPage.isSuccessMsgDisplayed(), "Validate success message appears");
		TestReporter.logStep("New Title was created: " + title);
	
		// Verify the title is displayed on the title results table
		TestReporter.assertTrue(listingTitlesPage.searchTableByTitle(title), "Validate new title appears in table");
    }

    @Features("Manage Titles")
    @Stories("As an admin, I should be able to modify a title")
    @Title("Manage Titles - Modify")
    @Severity(SeverityLevel.CRITICAL)
    @Test( groups = { "demo", "regression", "manageTitles" , "qaOnly" } ,dependsOnMethods= "testCreateTitle")
    public void testModifyTitle() {

		ListingTitlesPage listingTitlesPage = new ListingTitlesPage(getDriver());
		listingTitlesPage.clickModifyTitle(title);
		title += "_modified";
	
		// Instantiate the Manage titles page and modify the new title
		TestReporter.logStep("Modify the existing title to new title name: [" + title + "]");
		ManageTitlePage manageTitlePage = new ManageTitlePage( getDriver());
		TestReporter.assertTrue(manageTitlePage.pageLoaded(),"Verify manage title page is displayed");
		manageTitlePage.modifyTitle(title);
		
		// Verify the title was created
		TestReporter.assertTrue(listingTitlesPage.isSuccessMsgDisplayed(), "Validate success message appears");
			
		// Verify the title is displayed on the title results table
		TestReporter.logStep("Verify the modified title displays in list of titles");
		TestReporter.assertTrue(listingTitlesPage.searchTableByTitle(title), "Validate modified title appears in table");
    }
    
    @Features("Manage Titles")
    @Stories("As an admin, I should be able to delete a title")
    @Title("Manage Titles - Delete")
    @Severity(SeverityLevel.CRITICAL)
    @Test( groups = { "demo", "regression", "manageTitles" , "qaOnly" } ,dependsOnMethods= "testModifyTitle")
    public void testDeleteTitle() {
	
		// Delete the new title
	    TestReporter.logStep("Delete the modified title name: [" + title + "]");
		ListingTitlesPage listingTitlesPage = new ListingTitlesPage( getDriver());
		listingTitlesPage.deleteTitle(title, getBrowserUnderTest());
	
		// Verify the title is deleted
		ListingTitlesPage refreshedPage = new ListingTitlesPage( getDriver());
		TestReporter.assertTrue(refreshedPage.isSuccessMsgDisplayed(), "Validate success message appears");
		
		// logout
		TestReporter.logStep("Logout of application");
		TopNavigationBar topNavigationBar = new TopNavigationBar( getDriver());
		topNavigationBar.clickLogout();
    }
}