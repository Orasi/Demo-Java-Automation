package com.orasi.core.interfaces.impl;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.orasi.core.interfaces.Button;
import com.orasi.exception.AutomationException;
import com.orasi.utils.OrasiDriver;
import com.orasi.utils.TestReporter;

/**
 * Wraps a label on a html form with some behavior.
 */
public class ButtonImpl extends ElementImpl implements Button {
	// private java.util.Date date= new java.util.Date();
	/**
	 * Creates a Element for a given WebElement.
	 *
	 * @param element
	 *            - element to wrap up
	 */
	public ButtonImpl(WebElement element) {
		super(element);
		
	}

	public ButtonImpl(OrasiDriver driver, By by) {
		super(driver, by);
		//element = driver.findWebElement(by);
	}

	@Override
	public void click() {
		TestReporter.logTrace("Entering ButtonImpl#click");
		try {
			TestReporter.logTrace("Attempting to invoke method [ Click ] on element [ " +by.toString()+" ] ");
			getWrappedElement().click();
		} catch (RuntimeException rte) {
			TestReporter.interfaceLog("Clicked Button [ <b>" + getElementLocatorInfo() + "</b>]", true);
			throw rte;
		}

		TestReporter.interfaceLog("Clicked Button [ <b>" + getElementLocatorInfo() + "</b>]");
		TestReporter.logTrace("Successfully invoked method [ Click ] on element [ " +by.toString()+" ] ");
		TestReporter.logTrace("Exiting ButtonImpl#click");
	}

	@Override
	public void jsClick() {
		TestReporter.logTrace("Entering ButtonImpl#jsClick");
		try {
			TestReporter.logTrace("Attempting to executed [ jsClick ] on element [ " +by.toString()+" ] ");
			getWrappedDriver().executeJavaScript("arguments[0].click();", getWrappedElement());
			TestReporter.logTrace("Successfully executed [ jsClick ] on element [ " +by.toString()+" ] ");
		} catch (RuntimeException rte) {
			TestReporter.logFailure("Clicked Button [ <b>" + getElementLocatorInfo() + "</b>]");
			TestReporter.logTrace("Failed to execute [ jsClick ] on element [ " +by.toString()+" ] ");
			TestReporter.logTrace("Exiting ButtonImpl#jsClick");
			throw new AutomationException(rte.getMessage(), driver);
		}
		TestReporter.interfaceLog("Clicked Button [ <b>" + getElementLocatorInfo() + "</b>]");
		TestReporter.logTrace("Exiting ButtonImpl#jsClick");

	}
}