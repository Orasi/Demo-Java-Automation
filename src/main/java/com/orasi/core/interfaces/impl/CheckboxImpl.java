package com.orasi.core.interfaces.impl;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.orasi.core.interfaces.Checkbox;
import com.orasi.utils.OrasiDriver;
import com.orasi.utils.TestReporter;

/**
 * Wrapper class like Select that wraps basic checkbox functionality.
 */ 
public class CheckboxImpl extends ElementImpl implements Checkbox {
	//private java.util.Date dateAfter= new java.util.Date();
	/**
	 * Wraps a WebElement with checkbox functionality.
	 *
	 * @param element to wrap up
	 */
	public CheckboxImpl(WebElement element) {
		super(element);
	}

	public CheckboxImpl(OrasiDriver driver, By by) {
		super(driver, by);
		//	element = driver.findWebElement(by);
	}

	@Override
	public void toggle() {
		TestReporter.logTrace("Entering CheckboxImpl#toggle");
		getWrappedElement().click();
		TestReporter.logTrace("Exiting CheckboxImpl#toggle");
	}

	@Override
	public void jsToggle() {
		TestReporter.logTrace("Entering CheckboxImpl#jsToggle");
		getWrappedDriver().executeJavaScript("if( document.createEvent ) {var click_ev = document.createEvent('MouseEvents'); click_ev.initEvent('click', true , true )"
							+ ";arguments[0].dispatchEvent(click_ev);} else { arguments[0].click();}", element);
		TestReporter.logTrace("Exiting CheckboxImpl#jsToggle");
	}

	@Override
	public void check() {
		TestReporter.logTrace("Entering CheckboxImpl#check");
		if (!isChecked()) {
			try{
				toggle();
			}catch(RuntimeException rte){
				TestReporter.interfaceLog(" Checking the Checkbox [ <b>" + getElementLocatorInfo()  + " </b>]", true);
				TestReporter.logTrace("Exiting CheckboxImpl#uncheck");
				throw rte;
			}
			TestReporter.interfaceLog(" Checking the Checkbox [ <b>" + getElementLocatorInfo()  + " </b>]");
		}
		TestReporter.logTrace("Exiting CheckboxImpl#check");
	}

	@Override
	public void uncheck() {
		TestReporter.logTrace("Entering CheckboxImpl#uncheck");
		if (isChecked()) {
			try{
				toggle();
			}catch(RuntimeException rte){
				TestReporter.interfaceLog(" Unchecking the Checkbox [ <b>" + getElementLocatorInfo()  + " </b>]", true);
				TestReporter.logTrace("Exiting CheckboxImpl#uncheck");
				throw rte;
			}

			TestReporter.interfaceLog(" Unchecking the Checkbox [ <b>" + getElementLocatorInfo()  + " </b>]");
		}
		TestReporter.logTrace("Exiting CheckboxImpl#uncheck");
	}

	@Override
	public boolean isChecked() {
		TestReporter.logTrace("Entering CheckboxImpl#isChecked");
		boolean checked = getWrappedElement().isSelected();
		TestReporter.logTrace("Exiting CheckboxImpl#isChecked");
		return checked;
	}
}