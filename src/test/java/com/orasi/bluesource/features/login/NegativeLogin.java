package com.orasi.bluesource.features.login;

import org.testng.ITestContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import ru.yandex.qatools.allure.annotations.Features;
import ru.yandex.qatools.allure.annotations.Parameter;
import ru.yandex.qatools.allure.annotations.Severity;
import ru.yandex.qatools.allure.annotations.Stories;
import ru.yandex.qatools.allure.annotations.Title;
import ru.yandex.qatools.allure.model.SeverityLevel;

import com.orasi.apps.bluesource.LoginPage;
import com.orasi.utils.Constants;
import com.orasi.utils.OrasiDriver;
import com.orasi.utils.TestEnvironment;
import com.orasi.utils.TestReporter;
import com.orasi.utils.dataProviders.ExcelDataProvider;

public class NegativeLogin  extends TestEnvironment {

    private OrasiDriver driver = null;
    @DataProvider(name = "negativeDataScenario")
    public Object[][] negativeScenarios() {
	return new ExcelDataProvider(Constants.BLUESOURCE_DATAPROVIDER_PATH + "Login.xlsx", "LoginNegative").getTestData();
    }
    
    @BeforeTest( alwaysRun=true)
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

    @AfterMethod(  alwaysRun=true)
    public void closeSession(ITestContext test) {
	endTest(testName, test, driver);
    }    

    @Features("Login")
    @Stories("Failing to login will not let me leave the Login page")
    @Severity(SeverityLevel.BLOCKER)
    @Title("Login - Login with incorrect information")
    @Test(dataProvider = "negativeDataScenario", groups = { "regression", "login" })
    public void testFailedLogin(@Parameter String testScenario, @Parameter String role) {
	
	testName = new Object() {
	}.getClass().getEnclosingMethod().getName();

	testStart(testName);
	driver = getDriver();
	// Login
	TestReporter.log("Login to Bluesource with incorrect information");
	LoginPage loginPage = new LoginPage(getDriver());
	TestReporter.assertTrue(loginPage.pageLoaded(),"Verify login page is displayed");
	loginPage.login(role);
	TestReporter.assertTrue(loginPage.isNotLoggedIn(), "Validate the user did not log in successfully");

    }
}