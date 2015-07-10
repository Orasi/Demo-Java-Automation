package com.orasi.core.angular;

import org.openqa.selenium.JavascriptExecutor;

import com.orasi.utils.TestEnvironment;

/*
 * Original Code from https://github.com/paul-hammant/ngWebDriver
 */

public class WaitForAngularRequestsToFinish extends TestEnvironment {

    public WaitForAngularRequestsToFinish(TestEnvironment te) {
		super(te);
		// TODO Auto-generated constructor stub
	}

	public static void waitForAngularRequestsToFinish(JavascriptExecutor driver) {
    	//if(!WebDriverSetup.browser.equalsIgnoreCase("IE")){   		    	
    		driver.executeAsyncScript("var callback = arguments[arguments.length - 1];" +
    				"angular.element(document.body).injector().get('$browser').notifyWhenNoOutstandingRequests(callback);");
    	//}else{
    	//	driver.executeAsyncScript("var callback = arguments[arguments.length - 1];" +
    	//			"angular.element(document.body).injector().get('$browser').notifyWhenNoOutstandingRequests(callback);");
    	//}
    }
}