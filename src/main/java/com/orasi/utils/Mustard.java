package com.orasi.utils;

import java.util.ResourceBundle;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.openqa.selenium.OutputType;
import org.testng.ITestResult;

import com.orasi.api.restServices.RestService;
import com.orasi.api.restServices.Headers.HeaderType;
import com.orasi.api.soapServices.SoapService;
import com.orasi.core.Beta;
import com.orasi.utils.debugging.MustardResult;
import com.saucelabs.common.SauceOnDemandAuthentication;


@Beta
public class Mustard {
	private static String mustardURL = "https://api.mustard.orasi.com/results";
    	private static String mustardKey = "a12f51a946d9cb52eb6d8c9298877590"; //dev key da8f8779749cfb27bbba1fb9f136c1cf
	

    @Beta
	public static void postResultsToMustard(OrasiDriver driver, ITestResult result, String runLocation){
		
		RestService request = new RestService();
		
		String device_id = driver.getDriverCapability().platform().name()  + "_" +driver.getDriverCapability().browserName() + "_" + driver.getDriverCapability().browserVersion().replace(".", "_");
		//String os_version = driver.getDriverCapability().browserVersion();
		//String device_platform = driver.getDriverCapability().platform().family().name();
		String test_name = result.getTestClass().getName();
		test_name = test_name.substring(test_name.lastIndexOf(".") + 1, test_name.length())+ "_" +result.getMethod().getMethodName() ;
		String status = "";
		if (result.getStatus() == ITestResult.SUCCESS) status = "pass";
		else if (result.getStatus() == ITestResult.SKIP) status = "skip";
		else status = "fail";
		String sauceURL = "";
		MustardResult mustardResult = new MustardResult();
		
		mustardResult.getResult().setProjectId(mustardKey);
		mustardResult.getResult().setResultType("automated");
		mustardResult.getResult().setEnvironmentId(device_id);
		mustardResult.getResult().setTestcaseId(test_name);
		mustardResult.getResult().setStatus(status);
		mustardResult.getResult().setDisplayName(test_name);
		/*MultipartEntityBuilder multipartEntity = MultipartEntityBuilder.create();
		multipartEntity.addTextBody("project_id",mustardKey);
		multipartEntity.addTextBody("result_type","automated");
		multipartEntity.addTextBody("environment_id",device_id);
		multipartEntity.addTextBody("testcase_id",test_name);
		multipartEntity.addTextBody("status",status);
		multipartEntity.addTextBody("display_name",test_name);*/
		
		
		if(runLocation.toLowerCase().equals("sauce")){
		    sauceURL = "https://saucelabs.com/beta/tests/" + driver.getSessionId().toString();
		    mustardResult.getResult().setLink(sauceURL);
		   // multipartEntity.addTextBody("link",sauceURL);
		}
		
		if(status.equals("fail")) {
		    mustardResult.getResult().setComment(result.getThrowable().getMessage());
		    mustardResult.getResult().setStacktrace(ExceptionUtils.getFullStackTrace(result.getThrowable()));
		  //  multipartEntity.addTextBody("comment",result.getThrowable().getMessage());
		   // multipartEntity.addTextBody("stacktrace",ExceptionUtils.getFullStackTrace(result.getThrowable()));
		    //multipartEntity.addBinaryBody("screenshot", driver.getScreenshotAs(OutputType.FILE), ContentType.create("image/jpeg"), Randomness.randomAlphaNumeric(32));
		  //  multipartEntity.addTextBody("screenshot", Base64Coder.encodeLines(driver.getScreenshotAs(OutputType.FILE).));
		}
	
		
		TestReporter.setDebugLevel(TestReporter.TRACE);
		request.sendPostRequest(mustardURL , HeaderType.JSON, RestService.getJsonFromObject(mustardResult));
		
	}
    public static void postResultsToMustard(SoapService soapService, String environment, ITestResult result){
	
	RestService request = new RestService();
	
	//String device_id = info;
	//String os_version = driver.getDriverCapability().browserVersion();
	//String device_platform = driver.getDriverCapability().platform().family().name();
	String test_name = soapService.getServiceName() + "#" + soapService.getOperationName() + " - " + result.getTestClass().getName();
	test_name = test_name.substring(test_name.lastIndexOf(".") + 1, test_name.length())+ "_" +result.getMethod().getMethodName() ;
	String status = "";
	if (result.getStatus() == ITestResult.SUCCESS) status = "pass";
	else if (result.getStatus() == ITestResult.SKIP) status = "skip";
	else status = "fail";
	String sauceURL = "";
	MustardResult mustardResult = new MustardResult();
	
	mustardResult.getResult().setProjectId(mustardKey);
	mustardResult.getResult().setResultType("automated");
	mustardResult.getResult().setEnvironmentId(environment);
	mustardResult.getResult().setTestcaseId(test_name);
	mustardResult.getResult().setStatus(status);
	mustardResult.getResult().setDisplayName(test_name);
	//mustardResult.getResult().setComment("Request: \n " + soapService.getRequest() + " \n\nResponse\n" + soapService.getResponse());
	/*MultipartEntityBuilder multipartEntity = MultipartEntityBuilder.create();
	multipartEntity.addTextBody("project_id",mustardKey);
	multipartEntity.addTextBody("result_type","automated");
	multipartEntity.addTextBody("environment_id",device_id);
	multipartEntity.addTextBody("testcase_id",test_name);
	multipartEntity.addTextBody("status",status);
	multipartEntity.addTextBody("display_name",test_name);*/

	
	if(status.equals("fail")) {
	    mustardResult.getResult().setComment(result.getThrowable().getMessage());
	    mustardResult.getResult().setStacktrace(ExceptionUtils.getFullStackTrace(result.getThrowable()));
	  //  multipartEntity.addTextBody("comment",result.getThrowable().getMessage());
	   // multipartEntity.addTextBody("stacktrace",ExceptionUtils.getFullStackTrace(result.getThrowable()));
	    //multipartEntity.addBinaryBody("screenshot", driver.getScreenshotAs(OutputType.FILE), ContentType.create("image/jpeg"), Randomness.randomAlphaNumeric(32));
	  //  multipartEntity.addTextBody("screenshot", Base64Coder.encodeLines(driver.getScreenshotAs(OutputType.FILE).));
	}

	
	TestReporter.setDebugLevel(TestReporter.TRACE);
	request.sendPostRequest(mustardURL , HeaderType.JSON, RestService.getJsonFromObject(mustardResult));
	
}
}
