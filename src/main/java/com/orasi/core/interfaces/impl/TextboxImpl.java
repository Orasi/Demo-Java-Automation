package com.orasi.core.interfaces.impl;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;

import com.orasi.core.interfaces.Textbox;
import com.orasi.utils.Base64Coder;
import com.orasi.utils.OrasiDriver;
import com.orasi.utils.TestReporter;

/**
 * TextInput wrapper.
 */
public class TextboxImpl extends ElementImpl implements Textbox {
	/**
	 * Creates a Element for a given WebElement.
	 * 
	 * @param element
	 *            element to wrap up
	 */
	public TextboxImpl(WebElement element) {
		super(element);
	}


	public TextboxImpl(OrasiDriver driver, By by) {
		super(driver, by);
		//	element = driver.findWebElement(by);
	}


	/**
	 * @summary - Gets the value of an input field. Overrides default clear().
	 * @see org.openqa.selenium.WebElement#clear()
	 */
	@Override
	public void clear() {
		TestReporter.logTrace("Entering TextboxImpl#clear");
		try {
			getWrappedElement().clear();
			TestReporter.interfaceLog("Clear text from Textbox [<b>" + getElementLocatorInfo() + " </b>]");
		} catch (RuntimeException rte) {
			TestReporter.interfaceLog("Clear text from Textbox [<b>" + getElementLocatorInfo() + " </b>]",
					true);
			TestReporter.logTrace("Exiting TextboxImpl#clear");
			throw rte;
		}
		TestReporter.logTrace("Exiting TextboxImpl#clear");
	}

	/**
	 * @summary - If the text parameter is not an empty string, this method
	 *          clears any existing values and performs a "sendKeys(text)" to
	 *          simulate typing the value. If the text parameter is an empty
	 *          string, this step is skipped.
	 * @param text
	 *            - text to enter into the field
	 */
	@Override
	public void set(String text) {
		TestReporter.logTrace("Entering TextboxImpl#set");
		if (!text.isEmpty()) {
			try {
				getWrappedElement().clear();
				getWrappedElement().sendKeys(text);
			} catch (RuntimeException rte) {
				TestReporter.interfaceLog("Send Keys [ <b>" + text.toString() + "</b> ] to Textbox [ <b>"
						+ getElementLocatorInfo() + " </b>  ]", true);
				TestReporter.logTrace("Exiting TextboxImpl#set");
				throw rte;
			}
		} else {
			TestReporter.interfaceLog("Skipping input to Textbox [ <b>" + getElementLocatorInfo() + " </b> ]");
		}
		TestReporter.logTrace("Exiting TextboxImpl#set");
	}


	/**
	 * @summary - If the text parameter is not an empty string, this method
	 *          clears any existing values and performs a "sendKeys(text)" to
	 *          simulate typing the value. If the text parameter is an empty
	 *          string, this step is skipped.
	 * @param text
	 *            - text to enter into the field
	 */

	@Override
	public void jsSet( String text) {
		TestReporter.logTrace("Entering TextboxImpl#jsSet");
		if (text == null) text = "";
		if (!text.isEmpty()){
			if (text.equalsIgnoreCase("<blank>") || text.equalsIgnoreCase("(blank)")){
				TestReporter.interfaceLog(" Request to blank text field sent. Clearing Textbox [ <b>" + getElementLocatorInfo()  + " </b> ]");
				getWrappedElement().clear();
			}else{
				TestReporter.interfaceLog(" Send Keys [ <b>" + text.toString() + "</b> ] to Textbox [ <b>" + getElementLocatorInfo()  + " </b> ]");
				try{
					getWrappedDriver().executeJavaScript("arguments[0].scrollIntoView(true);arguments[0].setAttribute('value', arguments[1])", getWrappedElement(), text);
				}catch(WebDriverException wde){
					getWrappedElement().clear();
					getWrappedElement().sendKeys(text);
				}
			}
		}else{
			TestReporter.interfaceLog("Skipping input to Textbox [ <b>" + getElementLocatorInfo()  + " </b> ]");
		}
		TestReporter.logTrace("Exiting TextboxImpl#jsSet");
	}

	/**
	 * @summary - Overloads overridden set() method. If the text parameter is
	 *          not an empty string, this method uses a JavascriptExecutor to
	 *          scroll the text field into view and click the text field, then
	 *          uses Selenium to clear any existing values and performs a
	 *          "sendKeys(text)" to simulate typing the value. If the text
	 *          parameter is an empty string, this step is skipped.
	 * @param text
	 *            - text to enter into the field
	 */
	@Override
	public void scrollAndSet(String text) {
		TestReporter.logTrace("Entering TextboxImpl#scrollAndSet");
		if (!text.isEmpty()) {
			try {
				getWrappedDriver().executeJavaScript("arguments[0].scrollIntoView(true);arguments[0].click();",	getWrappedElement());
				getWrappedElement().clear();
				getWrappedElement().sendKeys(text);
				TestReporter.interfaceLog(" Send Keys [ <b>" + text + "</b> ] to Textbox [ <b>"
						+ getElementLocatorInfo() + " </b> ]");

			} catch (RuntimeException rte) {
				TestReporter.interfaceLog("Send Keys [ <b>" + text + "</b> ] to Textbox [ <b>"
						+ getElementLocatorInfo() + " </b> ]", true);
				TestReporter.logTrace("Exiting TextboxImpl#scrollAndSet");
				throw rte;
			}
		} else {
			TestReporter.interfaceLog("Skipping input to Textbox [ <b>" + getElementLocatorInfo() + " </b> ]");
		}
		TestReporter.logTrace("Exiting TextboxImpl#scrollAndSet");
	}

	/**
	 * @summary - If the text parameter is not an empty string, the text field
	 *          is clicked, then sendKeys() is used to select any/all existing
	 *          text, type the text passed in the parameter and send a "TAB"
	 *          key. This is useful if moving from an element is required to
	 *          trigger underlying JavaScript. If the text parameter is an empty
	 *          string, this step is skipped.
	 * @param text
	 *            - text to enter into the field
	 */
	@Override
	public void safeSet(String text) {
		TestReporter.logTrace("Entering TextboxImpl#safeSet");
		if (!text.isEmpty()) {
			try {

				getWrappedDriver().executeJavaScript("arguments[0].setAttribute('value', arguments[1])", getWrappedElement(), "");
				getWrappedElement().sendKeys(text);
				getWrappedElement().sendKeys(Keys.TAB);
				TestReporter.interfaceLog(" Send Keys [ <b>" + text.toString() + "</b> ] to Textbox [  <b>"
						+ getElementLocatorInfo() + " </b> ]");
			} catch (RuntimeException rte) {
				TestReporter.interfaceLog("Send Keys [ <b>" + text.toString() + "</b> ] to Textbox [  <b>"
						+ getElementLocatorInfo() + " </b> ]", true);
				throw rte;
			}
		} else {
			TestReporter.interfaceLog("Skipping input to Textbox [ <b>" + getElementLocatorInfo() + " </b> ]");
		}
		TestReporter.logTrace("Exiting TextboxImpl#safeSet");
	}

	/**
	 * @summary - If the text parameter is not an empty string, the parameter is
	 *          decoded using the Base64Coder class and a sendKeys() is used to
	 *          simulate typing the text. If the text parameter is an empty
	 *          string, this step is skipped.
	 * @param text
	 *            - text to enter into the field
	 */
	@Override
	public void setSecure(String text) {
		TestReporter.logTrace("Entering TextboxImpl#setSecure");
		if (!text.isEmpty()) {
			try {
				getWrappedElement().sendKeys(Base64Coder.decodeString(text).toString());
				TestReporter.interfaceLog(" Send encoded text [ <b>" + text.toString()
				+ "</b> ] to Textbox [  <b>" + getElementLocatorInfo() + " </b> ]");
			} catch (RuntimeException rte) {
				TestReporter.interfaceLog("Send encoded text [ <b>" + text.toString()
				+ "</b> ] to Textbox [  <b>" + getElementLocatorInfo() + " </b> ]", true);
				throw rte;
			}
		} else {
			TestReporter.interfaceLog("Skipping input to Textbox [ <b>" + getElementLocatorInfo() + " </b> ]");
		}
		TestReporter.logTrace("Exiting TextboxImpl#setSecure");
	}

	/**
	 * @summary - If the text parameter is not an empty string, the text field
	 *          is clicked, then sendKeys() is used to select any/all existing
	 *          text, type the decoded parameter (decode the parameter using the
	 *          Base64Coder) in the text field and send a "TAB" key. This is
	 *          useful if moving from an element is required to trigger
	 *          underlying JavaScript. If the text parameter is an empty string,
	 *          this step is skipped.
	 * @param text
	 *            - text to enter into the field
	 */
	@Override
	public void safeSetSecure(String text) {
		TestReporter.logTrace("Entering TextboxImpl#safeSetSecure");
		if (!text.isEmpty()) {
			try {
				getWrappedDriver().executeJavaScript("arguments[0].setAttribute('value', arguments[1])", getWrappedElement(), "");
				getWrappedElement().sendKeys(Base64Coder.decodeString(text).toString());
				getWrappedElement().sendKeys(Keys.TAB);
				TestReporter.log(" Send encoded text [ <b>" + text.toString() + "</b> ] to Textbox [  <b>"
						+ getElementLocatorInfo() + " </b> ]");
			} catch (RuntimeException rte) {
				TestReporter.interfaceLog("Send encoded text [ <b>" + text.toString()
				+ "</b> ] to Textbox [  <b>" + getElementLocatorInfo() + " </b> ]", true);
				TestReporter.logTrace("Exiting TextboxImpl#safeSetSecure");
				throw rte;
			}

		} else {
			TestReporter.interfaceLog("Skipping input to Textbox [ <b>" + getElementLocatorInfo() + " </b> ]");
		}

		TestReporter.logTrace("Exiting TextboxImpl#safeSetSecure");
	}

	/**
	 * @summary - Gets the value of the attribute "value" of an input field.
	 * @return String with the value of the field.
	 */
	@Override
	public String getText() {
		TestReporter.logTrace("Entering TextboxImpl#getText");
		String text = getWrappedElement().getAttribute("value");
		TestReporter.logTrace("Exiting TextboxImpl#getText");
		return text;
	}
}