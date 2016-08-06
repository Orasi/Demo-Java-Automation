package com.orasi.bluesource.standalone;

import org.openqa.selenium.By;
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
import com.orasi.apps.bluesource.employeesPage.EmployeesPage;
import com.orasi.utils.Constants;
import com.orasi.utils.Sleeper;
import com.orasi.utils.TestEnvironment;
import com.orasi.utils.TestReporter;
import com.orasi.utils.dataProviders.ExcelDataProvider;

public class ValidateExistingVacations  extends TestEnvironment {

    private String application = "Bluesource";
    
    @DataProvider(name = "dataScenario")
    public Object[][] scenarios() {
	return new ExcelDataProvider(Constants.BLUESOURCE_DATAPROVIDER_PATH
		+ "ManageEmployees.xlsx", "FilterEmployees").getTestData();
    }

    @BeforeTest(groups =  { "validateVacation", "sandbox" })
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

    @AfterMethod(groups = { "validateVacation", "sandbox" })
    public synchronized void closeSession(ITestResult test) {
	endTest(testName);
    }

    /**
     * @throws Exception
     * @Summary: Validates all employee "Vacation Page" to ensure it loads properly
     * @Precondition:NA
     * @Author: Justin Phlegar
     * @Version: 06/29/2015
     * @Return: N/A
     */
    @Features("Vacations")
    @Stories("Validates all employee 'Vacation Page' to ensure it loads properly")
    @Severity(SeverityLevel.MINOR)
    @Title("ValidateExistingVacations")
    @Test(dataProvider = "dataScenario", groups = { "validateVacation", "sandbox" })
    public void validateExistingVacation(@Parameter String testScenario, @Parameter String role) {
	
	testName = new Object() {
	}.getClass().getEnclosingMethod().getName();

	testStart(testName);
	
	// Login
	LoginPage loginPage = new LoginPage(this);
	TestReporter.assertTrue(loginPage.pageLoaded(),"Verify login page is displayed");
	loginPage.login(role);
	TopNavigationBar topNavigationBar = new TopNavigationBar(this);
	topNavigationBar.pageLoaded();
	topNavigationBar.clickEmployeesLink();
	EmployeesPage employeePage = new EmployeesPage(this);
	employeePage.pageLoaded();
	employeePage.uncheckInactiveCheckbox();
	
	Sleeper.sleep(1000);
	int numberEmployees = employeePage.getTotalDisplayedEmployees();
	setDefaultTestTimeout(0);
	String error  = "";
	for(int x = 1 ; x <= numberEmployees ; x++){
	    TestReporter.log("Validating Employee ID: " + x);
	    getDriver().get("http://bluesourcestaging.herokuapp.com/employees/" + x + "/vacations");
	    topNavigationBar.pageLoaded();
	    if (!topNavigationBar.isLoggedIn()) {
		error = getDriver().findElement(By.xpath("//*[@id='container']/pre[1]/code")).getText();
		TestReporter.logFailure("Employee ID [" + x + "] vacations page is failing for [" +error+"].");
	    }
	}
    }
}