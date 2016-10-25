package com.orasi.core.interfaces.impl.internal;

import java.lang.reflect.Field;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.pagefactory.DefaultElementLocator;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;

import com.orasi.core.by.angular.AngularElementLocator;
import com.orasi.core.by.angular.FindByNG;
import com.orasi.utils.OrasiDriver;
import com.orasi.utils.TestReporter;

public class CustomElementLocatorFactory implements ElementLocatorFactory {
	private final OrasiDriver driver;
	public CustomElementLocatorFactory(final OrasiDriver driver) {
		this.driver = driver;
	}

	@Override
	public ElementLocator createLocator(final Field field) {
		TestReporter.logTrace("Entering CustomElementLocatorFactory#createLocator");
		if (field.isAnnotationPresent(FindByNG.class)) {
			TestReporter.logTrace("Attempting to create Angular Element Locator");
			AngularElementLocator element = new AngularElementLocator(driver, field);
			TestReporter.logTrace("Successfully created Angular Element Locator");
			TestReporter.logTrace("Exiting CustomElementLocatorFactory#createLocator");
			return element;
		} else {
			TestReporter.logTrace("Attempting to create Default Element Locator");
			DefaultElementLocator element =  new DefaultElementLocator(driver.getWebDriver(), field);
			TestReporter.logTrace("Successfully created Default Element Locator");
			TestReporter.logTrace("Exiting CustomElementLocatorFactory#createLocator");
			return element;
		}
	}
}