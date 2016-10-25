package com.orasi.core.interfaces.impl;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.orasi.core.interfaces.Link;
import com.orasi.utils.OrasiDriver;
import com.orasi.utils.TestReporter;

/**
 * Wraps a label on a html form with some behavior.
 */
public class LinkImpl extends ElementImpl implements Link {
	/**
	 * Creates a Element for a given WebElement.
	 *
	 * @param element
	 *            element to wrap up
	 */
	public LinkImpl(WebElement element) {
		super(element);
	}
	
	public LinkImpl(OrasiDriver driver, By by) {
		super(driver, by);
		//element = driver.findWebElement(by);
	}

	@Override
	public void jsClick() {
		TestReporter.logTrace("Entering LinkImpl#jsClick");

		try {
			getWrappedDriver().executeJavaScript(
					"if( document.createEvent ) {var click_ev = document.createEvent('MouseEvents'); click_ev.initEvent('click', true , true )"
							+ ";arguments[0].dispatchEvent(click_ev);} else { arguments[0].click();}",
					element);
		} catch (RuntimeException rte) {
			TestReporter.interfaceLog(" Click Link [ <b>" + getElementLocatorInfo() + " </b> ]", true);
			throw rte;
		}
		TestReporter.interfaceLog(" Click Link [ <b>" + getElementLocatorInfo() + " </b> ]");
		TestReporter.logTrace("Exiting CheckboxImpl#jsClick");

	}

	@Override
	public void click() {
		TestReporter.logTrace("Entering LinkImpl#click");
		try {
			getWrappedElement().click();
		} catch (RuntimeException rte) {
			TestReporter.interfaceLog(" Click Link [ <b>" + getElementLocatorInfo() + " </b> ]", true);
			throw rte;
		}
		TestReporter.interfaceLog(" Click Link [ <b>" + getElementLocatorInfo() + " </b> ]");
		TestReporter.logTrace("Exiting LinkImpl#click");
	}

	@Override
	public String getURL() {
		TestReporter.logTrace("Entering LinkImpl#getURL");
		String url = getWrappedElement().getAttribute("href");
		TestReporter.logTrace("Exiting LinkImpl#getURL");
		return url;
	}
}
