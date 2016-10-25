package com.orasi.utils;

import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WindowHandler {

	/**
	 * Use FluentWait to halt the script until the window with the desired text in the title is active
	 * to return true. If the window is not found in the default timeout, return false. 
	 * @version Created 01/15/2016
	 * @author 	Justin Phlegar
	 * @param 	driver, title
	 * @return 	true/false
	 */
	public static boolean waitUntilWindowExistsWithTitle(WebDriver driver, String windowNameOrHandle){
	    return waitUntilWindowExistsWithTitle(driver, windowNameOrHandle, Constants.PAGE_TIMEOUT);
	}
	
	/**
	 * Use FluentWait to halt the script until the window with the desired text in the title is active
	 * to return true. If the window is not found in the default timeout, return false. 
	 * @version Created 01/15/2016
	 * @author 	Justin Phlegar
	 * @param 	driver, title
	 * @return 	true/false
	 */
	public static boolean waitUntilWindowExistsWithTitle(WebDriver driver, String windowNameOrHandle, int timeoutInSeconds){
		TestReporter.logTrace("Entering WindowHandler#waitUntilWindowExistsWithTitle");
		//Adding for backwards compatibility for WebDriver
		if(driver instanceof OrasiDriver){
			TestReporter.logTrace("Driver is OrasiDriver, extracting information");
			timeoutInSeconds = ((OrasiDriver)driver).getPageTimeout();
			driver = ((OrasiDriver)driver).getWebDriver();
		}
		
	    try{
	    	TestReporter.logTrace("Initializing WebDriverWait with wait time of [ " + timeoutInSeconds + " ] seconds");
	    	WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
	    	TestReporter.logTrace("Calling findWindowWithTitleAndSwitchToIt(\"" + windowNameOrHandle + "\"");
			wait.until(ExtendedExpectedConditions.findWindowWithTitleAndSwitchToIt(windowNameOrHandle));
			TestReporter.logTrace("Successfully found Window with title of [ " + windowNameOrHandle + " ]");
	    }catch(TimeoutException e){
	    	TestReporter.logTrace("Failed to find Window with title of [ " + windowNameOrHandle + " ]");
			TestReporter.logTrace("Exiting WindowHandler#waitUntilWindowExistsWithTitle");
	    	return false;
	    }
	    TestReporter.logTrace("Exiting WindowHandler#waitUntilWindowExistsWithTitle");
	    return true;
	}
	
	/**
	 * Use FluentWait to halt the script until the window with the desired text is contained in the title and 
	 * active to return true. If the window is not found in the default timeout, return false. 
	 * @version Created 01/15/2016
	 * @author 	Justin Phlegar
	 * @param 	driver, title
	 * @return 	true/false
	 */
	public static boolean waitUntilWindowExistsTitleContains(WebDriver driver, String windowNameOrHandle){
		return waitUntilWindowExistsTitleContains(driver, windowNameOrHandle, Constants.PAGE_TIMEOUT);
	}
	
	/**
	 * Use FluentWait to halt the script until the window with the desired text is contained in the title and 
	 * active to return true. If the window is not found in the default timeout, return false. 
	 * @version Created 01/15/2016
	 * @author 	Justin Phlegar
	 * @param 	driver, title, timeout
	 * @return 	true/false
	 */
	public static boolean waitUntilWindowExistsTitleContains(WebDriver driver, String windowNameOrHandle, int timeoutInSeconds){
		TestReporter.logTrace("Entering WindowHandler#waitUntilWindowExistsTitleContains");
		//Adding for backwards compatibility for WebDriver
		if(driver instanceof OrasiDriver){
			TestReporter.logTrace("Driver is OrasiDriver, extracting information");
			timeoutInSeconds = ((OrasiDriver)driver).getPageTimeout();
			driver = ((OrasiDriver)driver).getWebDriver();
		}
		
	    try{
	    	TestReporter.logTrace("Initializing WebDriverWait with wait time of [ " + timeoutInSeconds + " ] seconds");
			WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
	    	TestReporter.logTrace("Calling findWindowContainsTitleAndSwitchToIt(\"" + windowNameOrHandle + "\"");
			wait.until(ExtendedExpectedConditions.findWindowContainsTitleAndSwitchToIt(windowNameOrHandle));
			TestReporter.logTrace("Successfully found Window with title of [ " + windowNameOrHandle + " ]");
	    }catch(TimeoutException e){
	    	TestReporter.logTrace("Failed to find Window with title of [ " + windowNameOrHandle + " ]");
			TestReporter.logTrace("Entering Exiting#waitUntilWindowExistsTitleContains");
	    	return false;
	    }
		TestReporter.logTrace("Entering Exiting#waitUntilWindowExistsTitleContains");
	    return true;
	}

	/**
	 * Use FluentWait to halt the script until the window with the desired regex pattern is matched in the 
	 * title and the window is active to return true. If the window is not found in the default timeout, return false. 
	 * @version Created 01/15/2016
	 * @author 	Justin Phlegar
	 * @param 	driver, title
	 * @return 	true/false
	 */
	public static boolean waitUntilWindowExistsTitleMatches(WebDriver driver, String regex){
		return waitUntilWindowExistsTitleMatches(driver, regex, Constants.PAGE_TIMEOUT);
	}
	
	/**
	 * Use FluentWait to halt the script until the window with the desired regex pattern is matched in the 
	 * title and the window is active to return true. If the window is not found in the default timeout, return false. 
	 * @version Created 01/15/2016
	 * @author 	Justin Phlegar
	 * @param 	driver, title, timeout
	 * @return 	true/false
	 */
	public static boolean waitUntilWindowExistsTitleMatches(WebDriver driver, String regex, int timeoutInSeconds){
		TestReporter.logTrace("Entering WindowHandler#waitUntilWindowExistsTitleMatches");			
		//Adding for backwards compatibility for WebDriver
		if(driver instanceof OrasiDriver){
			TestReporter.logTrace("Driver is OrasiDriver, extracting information");
			timeoutInSeconds = ((OrasiDriver)driver).getPageTimeout();
			driver = ((OrasiDriver)driver).getWebDriver();
		}
		
	    try{
	    	TestReporter.logTrace("Initializing WebDriverWait with wait time of [ " + timeoutInSeconds + " ] seconds");
	    	WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
	    	TestReporter.logTrace("Calling findWindowMatchesTitleAndSwitchToIt(\"" + regex + "\"");
	    	wait.until(ExtendedExpectedConditions.findWindowMatchesTitleAndSwitchToIt(regex));
			TestReporter.logTrace("Successfully found Window with title of [ " + regex + " ]");
	    }catch( TimeoutException e){
	    	TestReporter.logTrace("Failed to find Window with title of [ " + regex + " ]");
			TestReporter.logTrace("Entering Exiting#waitUntilWindowExistsTitleMatches");
	    	return false;
	    }
		TestReporter.logTrace("Entering Exiting#waitUntilWindowExistsTitleMatches");
	    return true;
	}

	/**
	 * Use FluentWait to halt the script until the specified number of windows are found
	 * @version Created 01/15/2016
	 * @author 	Justin Phlegar
	 * @param 	driver, expectedNumberOfWindows
	 * @return 	true/false
	 */
	public static boolean waitUntilNumberOfWindowsAre(WebDriver driver, int expectedNumberOfWindows){
		return waitUntilNumberOfWindowsAre(driver, expectedNumberOfWindows, Constants.PAGE_TIMEOUT);
	}
	
	/**
	 * Use FluentWait to halt the script until the specified number of windows are found
	 * @version Created 01/15/2016
	 * @author 	Justin Phlegar
	 * @param 	driver, expectedNumberOfWindows
	 * @return 	true/false
	 */
	public static boolean waitUntilNumberOfWindowsAre(WebDriver driver, int expectedNumberOfWindows, int timeoutInSeconds){	
		TestReporter.logTrace("Entering WindowHandler#waitUntilNumberOfWindowsAre");			
		//Adding for backwards compatibility for WebDriver
		if(driver instanceof OrasiDriver){
			TestReporter.logTrace("Driver is OrasiDriver, extracting information");
			timeoutInSeconds = ((OrasiDriver)driver).getPageTimeout();
			driver = ((OrasiDriver)driver).getWebDriver();
		}
		
		try{
	    	TestReporter.logTrace("Initializing WebDriverWait with wait time of [ " + timeoutInSeconds + " ] seconds");
			WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
	    	TestReporter.logTrace("Calling numberOfWindowsToBe(\"" + expectedNumberOfWindows + "\"");
			wait.until(ExpectedConditions.numberOfWindowsToBe(expectedNumberOfWindows));
			TestReporter.logTrace("Successfully found number of windows to be [ " + expectedNumberOfWindows + " ]");
		}catch(TimeoutException e){
	    	TestReporter.logTrace("Failed to find number of windows to be [ " + expectedNumberOfWindows + " ]");
			TestReporter.logTrace("Entering Exiting#waitUntilNumberOfWindowsAre");
			return false;
		}
		TestReporter.logTrace("Entering Exiting#waitUntilNumberOfWindowsAre");
		return true;
	}
}
