package com.orasi.bluesource.features.manageTitles;

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

public class TestManageTitles extends TestEnvironment{

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
    @Stories("Given when I login as an admin role, I can add Title")
    @Title("Manage Titles - Create")
    @Severity(SeverityLevel.CRITICAL)
    @Test(dataProvider = "dataScenario", groups = {"demo",  "regression", "manageTitles" , "qaOnly"})
    public void testCreateTitle(@Parameter String testScenario, @Parameter String role,
	    @Parameter String newTitle) {
    	
	testName = "Manage Titles_" + getBrowserUnderTest() + "_" + getOperatingSystem();
	
	title = newTitle;
	if (title.equalsIgnoreCase("RANDOM")){
		title = Randomness.randomAlphaNumeric(11);
	}
	
	testStart(testName);
	
	//driver = getDriver();

	// Login
	LoginPage loginPage = new LoginPage( getDriver());
	TestReporter.assertTrue(loginPage.pageLoaded(), "Verify login page is displayed");
	loginPage.login(role);

	// Verify user is logged in
	TopNavigationBar topNavigationBar = new TopNavigationBar( getDriver());
	TestReporter.assertTrue(topNavigationBar.isLoggedIn(), "Validate the user logged in successfully");

	// Navigate to the title page
	topNavigationBar.clickAdminLink();
	topNavigationBar.clickTitlesLink();

	// Verify navigated to the title page
	ListingTitlesPage listingTitlesPage = new ListingTitlesPage(getDriver());
	TestReporter.assertTrue(listingTitlesPage.pageLoaded(),"Verify listing titles page is displayed");
	//listingTitlesPage.ensureNoExistingTitle(title);
	// Click new title
	listingTitlesPage.clickNewTitle();

	// Instantiate the Manage titles page and create a new title
	ManageTitlePage manageTitlePage = new ManageTitlePage( getDriver());
	TestReporter.assertTrue(manageTitlePage.pageLoaded(),"Verify manage title page is displayed");
	manageTitlePage.createNewTitle(title);

	// Verify the title was created
	TestReporter.assertTrue(listingTitlesPage.isSuccessMsgDisplayed(), "Validate success message appears");
	TestReporter.log("New Title was created: " + title);

	// Verify the title is displayed on the title results table
	TestReporter.assertTrue(listingTitlesPage.searchTableByTitle(title), "Validate new title appears in table");
    }

    @Features("Manage Titles")
    @Stories("Given when I login as an admin role, I can modify a Title")
    @Title("Manage Titles - Modify")
    @Severity(SeverityLevel.CRITICAL)
    @Test( groups = { "demo", "regression", "manageTitles" , "qaOnly" } ,dependsOnMethods= "testCreateTitle")
    public void testModifyTitle() {

	ListingTitlesPage listingTitlesPage = new ListingTitlesPage(getDriver());
	listingTitlesPage.clickModifyTitle(title);
	title += "_modified";

	// Instantiate the Manage titles page and modify the new title
	ManageTitlePage manageTitlePage = new ManageTitlePage( getDriver());
	TestReporter.assertTrue(manageTitlePage.pageLoaded(),"Verify manage title page is displayed");
	manageTitlePage.modifyTitle(title);
	
	// Verify the title was created
	TestReporter.assertTrue(listingTitlesPage.isSuccessMsgDisplayed(), "Validate success message appears");
		
	// Verify the title is displayed on the title results table
	TestReporter.assertTrue(listingTitlesPage.searchTableByTitle(title), "Validate modified title appears in table");
    }
    
    @Features("Manage Titles")
    @Stories("Given when I login as an admin role, I can delete a Title")
    @Title("Manage Titles - Delete")
    @Severity(SeverityLevel.CRITICAL)
    @Test( groups = { "demo", "regression", "manageTitles" , "qaOnly" } ,dependsOnMethods= "testModifyTitle")
    public void testDeleteTitle() {
	
	// Delete the new title
	ListingTitlesPage listingTitlesPage = new ListingTitlesPage( getDriver());
	listingTitlesPage.deleteTitle(title);

	// Verify the title is deleted
	ListingTitlesPage refreshedPage = new ListingTitlesPage( getDriver());
	TestReporter.assertTrue(refreshedPage.isSuccessMsgDisplayed(), "Validate success message appears");
	
	// logout
	TopNavigationBar topNavigationBar = new TopNavigationBar( getDriver());
	topNavigationBar.clickLogout();
    }
}