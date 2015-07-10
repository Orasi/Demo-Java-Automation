package com.orasi.core.by.angular;

import java.lang.reflect.Field;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.pagefactory.ElementLocator;

import com.orasi.core.angular.ByAngular;
@SuppressWarnings("unused")
public class AngularElementLocator implements ElementLocator {
	  private final WebDriver driver;
	  private final FindByNG ngLocator;	  
	  private static ByAngular ng;
		
	  
	  public AngularElementLocator(final WebDriver driver, final Field field) {
	    this.driver =  driver;
	    this.ngLocator = field.getAnnotation(FindByNG.class);
	    ng = new ByAngular(driver);
	  }
	  
	  //@Override
	  @Override
	public WebElement findElement() {
		  RemoteWebElement element = null;
		  if (!ngLocator.ngModel().toString().isEmpty()){
			  element = (RemoteWebElement) driver.findElement(ByAngular.model(ngLocator.ngModel()));
		  }else if(!ngLocator.ngRepeater().toString().isEmpty()){
			  element = (RemoteWebElement) driver.findElement(ByAngular.repeater(ngLocator.ngRepeater()));
		  }else if(!ngLocator.ngButtonText().toString().isEmpty()){
			  element = (RemoteWebElement) driver.findElement(ByAngular.buttonText(ngLocator.ngButtonText()));
		  }
		 // return element.findElement(ng.model(ngLocator.ngModel()));
		  return element;
	  }
	  
	  @Override
	@SuppressWarnings({ })// @Override
	  public List<WebElement> findElements() {
		  
		  List<WebElement> elements = null;
		  if (!ngLocator.ngModel().toString().isEmpty()){
			  elements = driver.findElements(ByAngular.model(ngLocator.ngModel()));
		  }else if(!ngLocator.ngRepeater().toString().isEmpty()){
			  elements = driver.findElements(ByAngular.repeater(ngLocator.ngRepeater()));
		  }else if(!ngLocator.ngButtonText().toString().isEmpty()){
			  elements = driver.findElements(ByAngular.buttonText(ngLocator.ngButtonText()));

		  }
		  
		  return elements;
	  }
}
