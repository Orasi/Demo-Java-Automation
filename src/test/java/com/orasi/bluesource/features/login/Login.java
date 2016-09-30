package com.orasi.bluesource.features.login;

import org.testng.ITestContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.orasi.bluesource.LoginPage;
import com.orasi.bluesource.commons.TopNavigationBar;
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

public class Login  extends TestEnvironment {

    private OrasiDriver driver = null;
    @DataProvider(name = "dataScenario")
    public Object[][] scenarios() {
	return new ExcelDataProvider(Constants.BLUESOURCE_DATAPROVIDER_PATH + "Login.xlsx", "Login").getTestData();
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
		setThreadDriver(true);
    }

    @AfterMethod( alwaysRun=true)
    public void closeSession(ITestContext test) {
    	endTest(testName, test);
    }    

    @Features("Login")
    @Stories("Logging in will land me on the Homepage")
    @Severity(SeverityLevel.BLOCKER)
    @Title("Login - Login with correct information")
    @Test(dataProvider = "dataScenario", groups = { "regression" , "login" })
    public void testLogin(@Parameter String testScenario, @Parameter String role) {
	
	    testName = "Test Login_" + getBrowserUnderTest() + "_" + getOperatingSystem();
	
	    TestReporter.logScenario("This test logs into the Bluesource application by role & verifies the user was logged in successfully");
		testStart(testName);
		driver =getDriver();
		
		// Login
		TestReporter.logStep("Login to Bluesource using role: [" + role + "]");
		LoginPage loginPage = new LoginPage(getDriver());
		TestReporter.assertTrue(loginPage.pageLoaded(),"Verify login page is displayed");
		loginPage.login(role);
	
		TestReporter.logStep("Verify successful login");
		// Verify user is logged in
		TopNavigationBar topNavigationBar = new TopNavigationBar(getDriver());
		TestReporter.assertTrue(topNavigationBar.isLoggedIn(), "Validate the user logged in successfully");
	
		// logout
		TestReporter.logStep("Logout of application");
		topNavigationBar.clickLogout();
    }
}