package com.orasi.utils;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.Reporter;

import com.orasi.api.restServices.RestResponse;
import com.orasi.api.restServices.exceptions.RestException;
import com.orasi.api.soapServices.SoapService;
import com.orasi.api.soapServices.exceptions.SoapException;
import com.orasi.utils.date.SimpleDate;

public class TestReporter {
	private static boolean printToConsole = false;
	private static boolean printClassPath= true;
	private static ThreadLocal<Boolean> assertFailed = new ThreadLocal<Boolean>();
	/**
	 * No additional info printed to console
	 */
	public static final int NONE = 0;

	/**
	 * Will print some useful information to console such as URL's, parameters, and RQ/RS
	 */
	public static final int INFO = 1;

	/**
	 *  Will print helpful debugging logs to console 
	 */
	public static final int DEBUG = 2;
	/**
	 *  Will print some low-level granular steps to console from framework
	 */
	public static final int TRACE = 3;

	private static int debugLevel = 0;

	/**
	 * 
	 * @param level - Options below <br/>
	 *  TestReporter.NONE : (Default) - No additional info printed to console <br/> 
	 *  TestReporter.INFO : Will print useful information to console such as URL's, parameters, and RQ/RS<br/>
	 *  TestReporter.DEBUG : Will print debugging information to console <br/>
	 *  TestReporter.TRACE: Will print low level information to console from the framework<br/>
	 */
	public static void setDebugLevel(int level){
		debugLevel = level;
	}

	public static int getDebugLevel(){
		return debugLevel;
	}
	private static String getTimestamp() {
		String date = SimpleDate.getTimestamp().toString().substring(11);
		date = (date + "00").substring(0, 12);
		return  date + " :: ";
	}

	private static String trimHtml(String log){
		return log.replaceAll("<[^>]*>", "");
	}

	private static String getClassPath(){
		String path = " > ";
		if(getPrintFullClassPath()){
			StackTraceElement[] elements = Thread.currentThread().getStackTrace();
			int x = 0;
			String filename = "";
			for(StackTraceElement element : elements){
				filename = element.getClassName().toString();
				if(x == 0 || x == 1 || x == 2) {
					x++;
					continue;
				}else if(!filename.contains("sun.reflect")  &&
						!filename.contains("com.orasi.utils.TestReporter") &&
						!filename.contains("com.orasi.utils.PageLoaded") &&
						!filename.contains("java.lang.reflect") &&
						!filename.contains("java.lang.Thread") &&
						//!filename.contains("com.orasi.core.interfaces") &&
						!filename.contains("com.sun.proxy") && //
						!filename.contains("org.testng.internal") &&
						!filename.contains("java.util.concurrent.ThreadPoolExecutor") &&
						!filename.contains("com.orasi.utils.debugging"))
				{
					path = element.getClassName()+"#"+element.getMethodName();
					break;
				}

			}
		}
		return path + " > ";
	}

	public static void setPrintToConsole(boolean printToConsole){
		TestReporter.printToConsole = printToConsole;
	}

	public static boolean getPrintToConsole(){
		return printToConsole;
	}


	public static void setPrintFullClassPath(boolean printClassPath){
		TestReporter.printClassPath = printClassPath;
	}

	public static boolean getPrintFullClassPath(){
		return printClassPath;
	}

	public static void logStep(String step) {
		Reporter.log("<br/><b><font size = 4>Step: " + step
				+ "</font></b><br/>");
		if(getPrintToConsole()) System.out.println(step);
	}

	public static void logScenario(String scenario) {
		Reporter.log("<br/><b><font size = 4>Data Scenario: " + scenario
				+ "</font></b><br/>");
		if(getPrintToConsole()) System.out.println(getTimestamp() + trimHtml(scenario));
	}

	public static void interfaceLog(String message) {
		logInfo(message);
	}

	public static void interfaceLog(String message, boolean failed) {
		logInfo("<font size = 2 color=\"red\">"  + message + "</font>");
	}

	public static void log(String message) {
		Reporter.log(getTimestamp() + " <i><b>" + getClassPath() + message + "</b></i><br />");
		if(getPrintToConsole()) System.out.println(getTimestamp() + getClassPath() + trimHtml(message));
	}

	public static void logFailure(String message){
		Reporter.log(getTimestamp() + " <font size = 2 color=\"red\"><b><u> ERROR :: " +getClassPath() +  message + "</font></u></b><br />");
		if(getPrintToConsole()) System.out.println(getTimestamp() + trimHtml( "ERROR :: " + getClassPath() + trimHtml(message) ));
	}
	/**
	 * Use to output low-level granular steps
	 * @param message
	 */
	public static void logTrace(String message) {
		if(debugLevel >= TRACE){
			Reporter.log(getTimestamp() + "TRACE :: " + getClassPath() + message + "<br />");
			System.out.println(getTimestamp() + "TRACE :: " + getClassPath() +  (trimHtml(message).trim()));
		}
	}
	/**
	 * Use to output useful information such as URL's, parameters, and RQ/RS
	 * @param message
	 */
	public static void logInfo(String message) {
		if(debugLevel >= INFO){
			Reporter.log(getTimestamp() + "INFO  :: "+ getClassPath()  + message + "<br />");
			System.out.println(getTimestamp() + "INFO  :: "  + getClassPath() + trimHtml(message).trim());
		}
	}

	/**
	 * Use to output useful information such as URL's, parameters, and RQ/RS
	 * @param message
	 */
	public static void logDebug(String message) {
		if(debugLevel >= DEBUG){
			Reporter.log(getTimestamp() + "DEBUG :: "+ getClassPath()  + message + "<br />");
			System.out.println(getTimestamp()+ "DEBUG :: "  + getClassPath() + trimHtml(message).trim());
		}
	}

	public static void logNoHtmlTrim(String message) {
		Reporter.log(getTimestamp() + " :: " + getClassPath() + message + "<br />");
		if(getPrintToConsole()) System.out.println(getTimestamp() +getClassPath() + message.trim());
	}
	public static void logNoXmlTrim(String message) {
		Reporter.setEscapeHtml(true);
		Reporter.log("");
		Reporter.log(message);
		Reporter.setEscapeHtml(false);
		Reporter.log("<br /");
		if(getPrintToConsole()) System.out.println(getTimestamp() + getClassPath() +message.trim());
	}
	public static void assertTrue(boolean condition, String description) {	
		try{
			Assert.assertTrue(condition, description);
		}catch (AssertionError failure){
			logFailure("Assert True - " + description );
			if(getPrintToConsole()) System.out.println(getTimestamp() + "Assert True - " + trimHtml(description));
			Assert.fail(description);
		}
		Reporter.log(getTimestamp() + " <font size = 2 color=\"green\"><b><u>Assert True - " + description + "</font></u></b><br />");
		if(getPrintToConsole()) System.out.println(getTimestamp() + "Assert True - " + trimHtml(description));
	}

	public static void assertFalse(boolean condition, String description) {	
		try{
			Assert.assertFalse(condition, description);
		}catch (AssertionError failure){
			logFailure("Assert False - " + description );
			if(getPrintToConsole()) System.out.println(getTimestamp() + "Assert False - " + trimHtml(description));
			Assert.fail(description);
		}
		Reporter.log(getTimestamp() + " <font size = 2 color=\"green\"><b><u>Assert False - " + description + "</font></u></b><br />");
		if(getPrintToConsole()) System.out.println(getTimestamp() + "Assert False - " + trimHtml(description));
	}

	public static void assertEquals(Object value1, Object value2, String description) {
		try{
			Assert.assertEquals(value1, value2,  description);
		}catch (AssertionError failure){
			logFailure("Assert Equals - " + description );
			if(getPrintToConsole()) System.out.println(getTimestamp() + "Assert Equals - " + trimHtml(description));
			Assert.fail(description);
		}
		Reporter.log(getTimestamp() + " <font size = 2 color=\"green\"><b><u>Assert Equals - " + description + "</font></u></b><br />");
		if(getPrintToConsole()) System.out.println(getTimestamp() + "Assert Equals - " + trimHtml(description));
	}

	public static void assertNotEquals(Object value1, Object value2, String description) {
		try{
			Assert.assertNotEquals(value1, value2,  description);
		}catch (AssertionError failure){
			logFailure("Assert Not Equals - " + description );
			if(getPrintToConsole()) System.out.println(getTimestamp() + "Assert Not Equals - " + trimHtml(description));
			Assert.fail(description);
		}
		Reporter.log(getTimestamp() + " <font size = 2 color=\"green\"><b><u>Assert Not Equals - " + description + "</font></u></b><br />");
		if(getPrintToConsole()) System.out.println(getTimestamp() + "Assert Not Equals - " + trimHtml(description));
	}

	public static void assertGreaterThanZero(int value) {
		try{
			Assert.assertTrue(value > 0);
		}catch (AssertionError failure){
			logFailure("Assert Greater Than Zero - " + value );
			if(getPrintToConsole()) System.out.println(getTimestamp() + "Assert Greater Than Zero - Assert " + value + " is greater than zero");
			Assert.fail("Assert " + value + " is greater than zero");
		}
		Reporter.log(getTimestamp() + " <font size = 2 color=\"green\"><b><u>Assert Greater Than Zero - Assert " + value + " is greater than zero</font></u></b><br />");
		if(getPrintToConsole()) System.out.println(getTimestamp() + "Assert Greater Than Zero - Assert " + value + " is greater than zero");
	}

	public static void assertGreaterThanZero(float value) {
		assertGreaterThanZero((int)value);
	}

	public static void assertGreaterThanZero(double value) {
		assertGreaterThanZero((int)value);
	}

	public static void assertNull(Object condition, String description) {
		try{
			Assert.assertNull(condition, description);
		}catch (AssertionError failure){
			logFailure("Assert Null - " + description );
			if(getPrintToConsole()) System.out.println(getTimestamp() + "Assert Null - " + trimHtml(description));
			Assert.fail(description);
		}
		Reporter.log(getTimestamp() + " <font size = 2 color=\"green\"><b><u>Assert Null - " + description + "</font></u></b><br />");
		if(getPrintToConsole()) System.out.println(getTimestamp() + "Assert Null - " + trimHtml(description));
	}

	public static void assertNotNull(Object condition, String description) {
		try{
			Assert.assertNotNull(condition, description);
		}catch (AssertionError failure){
			logFailure("Assert Not Null - " + description );
			if(getPrintToConsole()) System.out.println(getTimestamp() + "Assert Not Null - " + trimHtml(description));
			Assert.fail(description);
		}
		Reporter.log(getTimestamp() + "<font size = 2 color=\"green\"><b><u>Assert Not Null - " + description + "</font></u></b><br />");
		if(getPrintToConsole()) System.out.println(getTimestamp() + "Assert Not Null - " + trimHtml(description));
	}

	public static void logScreenshot(WebDriver driver, String fileLocation, String slash, String runLocation) {
		File file = new File("");

		try {
			file = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(file, new File(fileLocation));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(runLocation.equalsIgnoreCase("remote")){
			fileLocation = fileLocation.replace("/var/lib/jenkins/jobs/OpenSandbox/jobs/Toyota-SauceLabs/workspace/", "job/OpenSandbox/job/Toyota-SauceLabs/ws/");
			//fileLocation = fileLocation.replace("/var/lib/jenkins/jobs/OpenSandbox/jobs/Toyota-SauceLabs/workspace/", "job/OpenSandbox/job/Toyota-SauceLabs/230/testngreports/toyota/TestAllSecondaryNavigations/");
			Reporter.log("<a href='https://jenkins.orasi.com/" + fileLocation + "'>FAILED SCREENSHOT</a>");
		}else{
			TestReporter.log(fileLocation);
			Reporter.log("<a href='" + fileLocation + "'> <img src='file:///" + fileLocation + "' height='200' width='300'/> </a>");
		}
	}
	public static boolean softAssertTrue(boolean condition, String description){
		try {
			Assert.assertTrue(condition, description);
			Reporter.log(getTimestamp() + " <font size = 2 color=\"green\"><b><u>Assert True - " + description
					+ "</font></u></b><br />");
			if (getPrintToConsole())
				System.out.println(getTimestamp() + "Assert True - " + trimHtml(description));
		} catch (AssertionError failure) {
			Reporter.log(getTimestamp() + "<font size = 2 color=\"red\"><b><u>Assert True - " + description + "</b></u></font><br />");
			if (getPrintToConsole())
				System.out.println(getTimestamp() + "Assert True - " + trimHtml(description));
			assertFailed.set(true);
			return false;
		}
		return true;
	}

	public static boolean softAssertEquals(Object value1, Object value2, String description){

		try {
			Assert.assertEquals(value1, value2, description);
			Reporter.log(getTimestamp() + " <font size = 2 color=\"green\"><b><u>Assert Equals - " + description
					+ "</font></u></b><br />");
			if (getPrintToConsole())
				System.out.println(getTimestamp() + "Assert Equals - " + trimHtml(description));
		} catch (AssertionError failure) {
			Reporter.log(getTimestamp() + "<font size = 2 color=\"red\"><b><u>Assert Equals - " + description + "</b></u></font><br />");
			if (getPrintToConsole())
				System.out.println(getTimestamp() + "Assert Equals - " + trimHtml(description));
			assertFailed.set(true);
			return false;
		}
		return true;

	}

	public static boolean softAssertFalse(boolean condition, String description){
		try {
			Assert.assertFalse(condition, description);
			Reporter.log(getTimestamp() + " <font size = 2 color=\"green\"><b><u>Assert False - " + description
					+ "</font></u></b><br />");
			if (getPrintToConsole())
				System.out.println(getTimestamp() + "Assert False - " + trimHtml(description));
		} catch (AssertionError failure) {
			Reporter.log(getTimestamp() + "<font size = 2 color=\"red\"><b><u>Assert False - " + description + "</b></u></font><br />");
			if (getPrintToConsole())
				System.out.println(getTimestamp() + "Assert False - " + trimHtml(description));
			assertFailed.set(true);
			return false;
		}
		return true;
	}

	public static boolean softAssertNull(Object condition, String description) {
		try {
			Assert.assertNull(condition, description);
		} catch (AssertionError failure) {
			Reporter.log(getTimestamp() + "<font size = 2 color=\"red\"><b>Assert Null - " + description+ "</font></u></b><br />");
			if (getPrintToConsole())
				System.out.println(getTimestamp() + "Assert Null - " + trimHtml(description));
			assertFailed.set(true);
			return false;
		}
		Reporter.log(getTimestamp() + " <font size = 2 color=\"green\"><b><u>Assert Null - " + description
				+ "</font></u></b><br />");
		if (getPrintToConsole())
			System.out.println(getTimestamp() + "Assert Null - " + trimHtml(description));

		return true;
	}

	public static boolean softAssertNotNull(Object condition, String description) {
		try {
			Assert.assertNotNull(condition, description);
		} catch (AssertionError failure) {
			Reporter.log(getTimestamp() + "<font size = 2 color=\"red\"><b>Assert Not Null - " + description + "</font></u></b><br />");
			if (getPrintToConsole())
				System.out.println(getTimestamp() + "Assert Not Null - " + trimHtml(description));
			assertFailed.set(true);
			return false;
		}
		Reporter.log(getTimestamp() + "<font size = 2 color=\"green\"><b><u>Assert Not Null - " + description
				+ "</font></u></b><br />");
		if (getPrintToConsole())
			System.out.println(getTimestamp() + "Assert Not Null - " + trimHtml(description));

		return true;
	}

	public static void assertAll(){
		boolean failed = assertFailed.get() == null ? false : assertFailed.get();
		if (failed){
			assertFailed.set(false);
			Reporter.log(getTimestamp() + "<font size = 2 color=\"red\"><b>Soft assertions failed - see failures above</font></u></b><br />");
			Assert.fail("Soft assertions failed - see testNG report for details");
		}
	}

	public static void logAPI(boolean pass, String message, SoapService bs){
		String failFormat = "";
		if(!pass){
			failFormat = "<font size = 2 color=\"red\">";
			logFailure(message);
		}else{
		    assertTrue(true, message);
		}
		String request =bs.getRequest(); 
		String response =bs.getResponse();
		Reporter.log("<font size = 2><b>Endpoint: " + bs.getServiceURL() + "</b></font><br/>"+failFormat+ "<b><br/> SOAP REQUEST [ " + bs.getServiceName() + "#" + bs.getOperationName() + " ] </b></font>");
		Reporter.setEscapeHtml(true);
		Reporter.log(request);
		Reporter.setEscapeHtml(false);
		Reporter.log("<br/><br/>");
		Reporter.log(failFormat + "<b> SOAP RESPONSE [ " + bs.getServiceName() + "#" + bs.getOperationName() + " ] </b></font>" );
		Reporter.setEscapeHtml(true);
		Reporter.log(response);
		Reporter.setEscapeHtml(false);
		Reporter.log("<br/>");

		if(!pass){
			throw new SoapException(message);
		}
	}
	
	public static void logAPI(boolean pass, String message, RestResponse rs){
		String failFormat = "";
		if(!pass){
			failFormat = "<font size = 2 color=\"red\">";
			logFailure(message);
		}
		Reporter.log("<font size = 2><b>Endpoint: " + rs.getMethod() + " " + rs.getURL() + "</b><br/>"+failFormat+ "<b>REST REQUEST </b></font>");
		Reporter.setEscapeHtml(true);
		Reporter.log(rs.getRequestBody().replaceAll("</*>", "</*>"));
		Reporter.setEscapeHtml(false);
		Reporter.log("<br/>");
		Reporter.log(failFormat + "<br/><b>REST RESPONSE</b></font>" );
		Reporter.setEscapeHtml(true);
		Reporter.log(rs.getResponse());
		Reporter.setEscapeHtml(false);
		Reporter.log("<br/>");

		if(!pass){
			throw new RestException(message);
		}
	}
}