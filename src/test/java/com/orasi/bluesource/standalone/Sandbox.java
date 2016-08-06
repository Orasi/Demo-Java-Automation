package com.orasi.bluesource.standalone;

import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;

import com.orasi.utils.OrasiDriver;
import com.orasi.utils.TestEnvironment;
import com.orasi.utils.TestReporter;

public class Sandbox extends TestEnvironment{
    OrasiDriver driver = null;
    @Test
    public void main() throws MalformedURLException{
	TestReporter.setPrintToConsole(true);
	DesiredCapabilities caps = DesiredCapabilities.android();
	caps.setCapability("deviceName","Samsung Galaxy Note 10.1 Emulator");
	caps.setCapability("deviceOrientation", "portrait");
	caps.setCapability("browserName", "Android");
	caps.setCapability("platform", "Linux");
	caps.setCapability("version", "4.1");
	/*DesiredCapabilities caps = DesiredCapabilities.iphone();
	caps.setCapability("appiumVersion", "1.4.15");
	caps.setCapability("deviceName","iPad Air");
	caps.setCapability("deviceOrientation", "portrait");
	caps.setCapability("platformVersion","9.1");
	caps.setCapability("platformName", "iOS");
	caps.setCapability("browserName", "Safari");*/
	    
	    
	    caps.setCapability("name", "Android - Samsung Galaxy Note 10.1 Emulator");
	    driver = new OrasiDriver(caps, new URL(sauceLabsURL));
	    driver.setElementTimeout(10);
	    driver.get("https://bluesourcestaging.herokuapp.com/");
	    
	    driver.findTextbox(By.id("employee_username")).set("company.admin");
	    driver.findTextbox(By.id("employee_password")).set("blah");
	    driver.findButton(By.xpath("//input[@value='Login']")).click();
	    
	    driver.findLink(By.xpath("//a[text() = 'Logout']")).syncPresent();
	    System.out.println();
    }
    @AfterTest
    public void tearDown(){
	driver.quit();
    }
}
