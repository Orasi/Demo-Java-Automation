package com.orasi.bluesource.standalone;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import com.orasi.core.interfaces.impl.ElementImpl;
import com.orasi.utils.Randomness;

public class QuickTest {
	
	@Test
	public void quickTest() throws MalformedURLException, InterruptedException{
			
		WebElement element;
		
		String SAUCE_USERNAME = "OrasiTesting";
		String SAUCE_ACCESS_KEY = "f0a63584-f52e-4d4b-9002-d7aeed40e4c3";
		

		DesiredCapabilities caps = DesiredCapabilities.chrome();
		caps.setCapability("platform", "VISTA");
	    caps.setCapability("name", "Quick chrome test");

	 
	    WebDriver driver = new RemoteWebDriver(new URL("http://" + SAUCE_USERNAME + ":"
							+ SAUCE_ACCESS_KEY + "@ondemand.saucelabs.com:80/wd/hub"), caps);
	    
	    //WebDriver driver = new ChromeDriver();
		WebDriverWait wait = new WebDriverWait(driver, 10);
	    
	    driver.get("http://bluesourcestaging.herokuapp.com/");
		
		driver.findElement(By.id("employee_username")).sendKeys("company.admin");
		driver.findElement(By.id("employee_password")).sendKeys("company");
		driver.findElement(By.name("commit")).click();
		Thread.sleep(2000);
		
		element = driver.findElement(By.xpath("//a[text() = 'Admin ']"));
		wait.until(ExpectedConditions.visibilityOf(element));
		element.click();
		driver.findElement(By.cssSelector("a[href = '/admin/departments']")).click();
		
		element = driver.findElement(By.linkText("Add Department"));
		wait.until(ExpectedConditions.visibilityOf(element));
		element.click();
		
		String departmentName = Randomness.randomAlphaNumeric(10);
		
		driver.findElement(By.id("department_name")).sendKeys(departmentName);
		Select parentDept = new Select(driver.findElement(By.xpath("//select[@id='department_department_id']")));
		parentDept.selectByVisibleText("Rural Testing");
		driver.findElement(By.name("commit")).click();
		
		wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector(".alert-success.alert-dismissable"))));
		
		List<WebElement> elementList = driver.findElements(By.cssSelector(".list-group-item"));
		for(WebElement deptElement:elementList){
			if(deptElement.getText().replace("Add Subdepartment","").trim().equals(departmentName)) {
				deptElement.findElement(By.cssSelector("div > a:nth-child(2)")).click();
				break;
			}
		}
		
		wait.until(ExpectedConditions.alertIsPresent());
		driver.switchTo().alert().accept();
        driver.switchTo().defaultContent();
		  
		wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector(".alert-success.alert-dismissable"))));
		driver.quit();
		

		
		
	}

}
