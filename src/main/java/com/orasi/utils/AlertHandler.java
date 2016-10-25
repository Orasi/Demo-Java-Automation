package com.orasi.utils;

import org.openqa.selenium.Alert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.security.Credentials;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.orasi.exception.AutomationException;

public class AlertHandler {
    public static boolean isAlertPresent(WebDriver driver, int timeout){
	try{
	    WebDriverWait wait = new WebDriverWait(driver, timeout);
	    wait.until(ExpectedConditions.alertIsPresent());
	    return true;
	}
	catch(Exception e){
	    return false;
	}
    }

    public static void handleAllAlerts(WebDriver driver, int timeout){
	while (isAlertPresent(driver, timeout)){
	    alertHandler(driver);
	}
    }

    public static void handleAlert(WebDriver driver, int timeout){
	if(isAlertPresent(driver, timeout)) alertHandler(driver);
    }

    public static void handleAlert(WebDriver driver, int timeout, String inputText){
	if(isAlertPresent(driver, timeout)) alertHandler(driver, inputText);
    }

    public static void handleAlert(WebDriver driver, int timeout, Credentials user){
	if(isAlertPresent(driver, timeout)) alertHandler(driver, user);
    }

    private static void alertHandler(WebDriver driver){
	try {
	    Alert alert = driver.switchTo().alert();
	    TestReporter.log("Closing alert popup with text [ <i>" + alert.getText() +" </i> ]<br />");
	    alert.accept();            	        
	} catch (Exception throwAway) {}
    }

    private static void alertHandler(WebDriver driver, String inputText){
	try {
	    Alert alert = driver.switchTo().alert();
	    TestReporter.log("Sending text [ <i>" + inputText +" </i> ] to Alert popup<br />");
	    alert.sendKeys(inputText);
	    alertHandler(driver);
	} catch (Exception throwAway) {}
    }

    private static void alertHandler(WebDriver driver, Credentials user){
	try {
	    Alert alert = driver.switchTo().alert();
	    TestReporter.log("Closing alert popup with text [ <i>" + alert.getText() +" </i> ] with authentication user <br />");
	    alert.authenticateUsing(user);                      
	} catch (Exception throwAway) {}
    }
}
