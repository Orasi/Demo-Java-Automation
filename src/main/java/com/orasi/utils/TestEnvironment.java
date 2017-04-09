package com.orasi.utils;

import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import com.orasi.api.soapServices.SoapService;
/**
 * 
 * @author Justin Phlegar & Waightstill W Avery
 * @summary This class is designed to be extended by page classes and
 *          implemented by test classes. It houses test environment data and
 *          associated getters and setters, setup for both local and remote
 *          WebDrivers as well as page class methods used to sync page behavior.
 *          The need for this arose due to the parallel behavior that indicated
 *          that WebDriver instances were crossing threads and testing on the
 *          wrong os/browser configurations
 * @date April 5, 2015
 *
 */
public class TestEnvironment {
	/*
	 * Test Environment Fields
	 */

	protected String environment = "";

	protected ThreadLocal<SoapService> soapService = new ThreadLocal<SoapService>();

	@BeforeSuite(alwaysRun=true)
	@Parameters({ "environment"})
	public void suiteSetup(@Optional String environment){
	    this.environment = environment;
		try{
			String debugLevel = System.getenv("debugLevel");
			debugLevel = (debugLevel == null) ? System.getProperty("debugLevel") : debugLevel;
			int level = 0;
			switch (debugLevel.toLowerCase()) {
			case "none":
				break;
			case "info":
				level = 1;
				break;
			case "debug":
				level = 2;
				break;
			case "trace":
				level = 3;
				break;
			}
			TestReporter.setDebugLevel(level);
		}catch(Exception throwAway){}		
		//TestReporter.setDebugLevel(2);
	}
	
	@BeforeMethod
	public void setup(){
	    Sleeper.sleep(250);
	}
	
	@AfterMethod
	public void teardown(ITestResult testResults){
	    String result = "";
	    switch (testResults.getStatus()){
	    case ITestResult.FAILURE: result = "FAILED"; break;
	    case ITestResult.SKIP: result = "SKIPPED"; break;
	    case ITestResult.SUCCESS: result = "PASSED"; break;

	    }
	    System.out.println(testResults.getMethod().getTestClass().getName()+ "#"+testResults.getName() + " Completed... Result Status: " + result); 
	}
	
	/*
	 * Mustard Fields
	 */
	protected boolean reportToMustard = false;

	/*
	 * Constructors for TestEnvironment class
	 */

	public TestEnvironment() {
	};

	protected void setTestEnvironment(String env) {
		environment = env;
	}
	public String getTestEnvironment() {
		return environment;
	}

	/*
	 * Mustard specific 
	 */
	public boolean isReportingToMustard() {
	    return reportToMustard;
	}
	
	public void setReportToMustard(boolean reportToMustard) {
	    this.reportToMustard = reportToMustard;
	}

	protected void setSoapService(SoapService soapService) {
	    this.soapService.set(soapService);
		
	}

	public SoapService getSoapService() {
		return soapService.get();
	
	}
}