package com.orasi.utils.debugging;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.remote.Augmenter;
import org.testng.IReporter;
import org.testng.ISuite;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.TestListenerAdapter;
import org.testng.xml.XmlSuite;

import com.orasi.api.soapServices.SoapService;
import com.orasi.utils.Constants;
import com.orasi.utils.Mustard;
import com.orasi.utils.OrasiDriver;
import com.orasi.utils.TestEnvironment;
import com.orasi.utils.TestReporter;

@Deprecated
public class Screenshot extends TestListenerAdapter implements IReporter{
	private OrasiDriver driver = null;
	private String runLocation = "";
	private String testEnvironment = "";
	private boolean reportToMustard = true;
	private SoapService soapService = null;
	private void init(ITestResult result){

	    Object currentClass = result.getInstance();
	    reportToMustard = ((TestEnvironment) currentClass).isReportingToMustard();
	    try{
		//runLocation = ((TestEnvironment) currentClass).getRunLocation().toLowerCase();	
		testEnvironment = ((TestEnvironment) currentClass).getTestEnvironment().toLowerCase();	
	    }catch (Exception e){}
	    
	    try{
	//	driver = ((TestEnvironment) currentClass).getDriver();
		if (driver == null) {
		    soapService = ((TestEnvironment) currentClass).getSoapService();
		}
	    }catch (Exception e){}
	    
	}
	
	@Override
	public void onTestFailure(ITestResult result) {
		init(result);
		if (driver == null) {
		    if(reportToMustard) Mustard.postResultsToMustard(soapService, testEnvironment, result );
		    return;
		}
		String slash = Constants.DIR_SEPARATOR;
		File directory = new File(".");
		
		String destDir = null;
		try {
			destDir = directory.getCanonicalPath()
					+ slash + "selenium-reports" + slash + "html" + slash + "screenshots";
		} catch (IOException e) {
			e.printStackTrace();
		}
		Reporter.setCurrentTestResult(result);
		
		WebDriver augmentDriver= driver.getWebDriver();
		if(!(augmentDriver instanceof HtmlUnitDriver)){
			if (runLocation == "remote" ){
				augmentDriver = new Augmenter().augment(driver.getWebDriver());
			}
		
			new File(destDir).mkdirs();
			DateFormat dateFormat = new SimpleDateFormat("dd_MMM_yyyy_hh_mm_ssaa");
	
			String destFile = dateFormat.format(new Date()) + ".png";
	
			//Capture a screenshot for TestNG reporting
			TestReporter.logScreenshot(augmentDriver, destDir + slash + destFile, slash, runLocation);
		}		
		//Capture a screenshot for Allure reporting
	//	FailedScreenshot(augmentDriver);
		if(reportToMustard) Mustard.postResultsToMustard(driver, result, runLocation );
		  
	}

	@Override
	public void onTestSkipped(ITestResult result) {
		// will be called after test will be skipped
		init(result);
		if (driver == null) {
		    if(reportToMustard) Mustard.postResultsToMustard(soapService,testEnvironment, result );
		    return;
		}
		if(reportToMustard) Mustard.postResultsToMustard(driver, result, runLocation );
	}

	@Override
	public void onTestSuccess(ITestResult result) {
		// will be called after test will pass
		init(result);
		if (driver == null) {
		    if(reportToMustard) Mustard.postResultsToMustard(soapService, testEnvironment, result );
		    return;
		}
		if(reportToMustard) Mustard.postResultsToMustard(driver, result, runLocation );
	}

	@Override
	public void generateReport(List<XmlSuite> xmlSuites,
		List<ISuite> suites, String outputDirectory) {
	    // TODO Auto-generated method stub
	    
	}

}
