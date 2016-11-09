package com.orasi.utils;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import com.orasi.api.soapServices.core.SoapService;
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

	@BeforeMethod
	  @Parameters({ "environment"})
	public void setup(@Optional String environment){
	    this.environment = environment;
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