package com.orasi.utils;

import java.util.ResourceBundle;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.openqa.selenium.OutputType;
import org.testng.ITestResult;

import com.orasi.api.restServices.core.Headers.HeaderType;
import com.orasi.api.restServices.core.RestService;
import com.orasi.core.Beta;
import com.orasi.utils.debugging.MustardResult;
import com.saucelabs.common.SauceOnDemandAuthentication;


@Beta
public class Mustard {
	private static String mustardURL = "https://mustardapi.orasi.com/results";
    	private static String mustardKey = "a70b4e914a58f2cafce05d7c6ecd2612"; //dev key da8f8779749cfb27bbba1fb9f136c1cf
	protected static ResourceBundle appURLRepository = ResourceBundle.getBundle(Constants.ENVIRONMENT_URL_PATH);
	protected static SauceOnDemandAuthentication authentication = new SauceOnDemandAuthentication(
			Base64Coder.decodeString(appURLRepository.getString("SAUCELABS_USERNAME")),
			Base64Coder.decodeString(appURLRepository.getString("SAUCELABS_KEY")));
	

    @Beta
	public static void postResultsToMustard(OrasiDriver driver, ITestResult result, String runLocation){
		
		RestService request = new RestService();
		
		String device_id = driver.getDriverCapability().platform().name()  + "_" +driver.getDriverCapability().browserName() + "_" + driver.getDriverCapability().browserVersion().replace(".", "_");
		//String os_version = driver.getDriverCapability().browserVersion();
		String device_platform = driver.getDriverCapability().platform().family().name();
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
}
