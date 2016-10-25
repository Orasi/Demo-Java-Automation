package com.orasi.core.interfaces.impl;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import com.orasi.core.interfaces.Listbox;
import com.orasi.exception.automation.OptionNotInListboxException;
import com.orasi.utils.OrasiDriver;
import com.orasi.utils.TestReporter;

/**
 * Wrapper around a WebElement for the Select class in Selenium.
 */
public class ListboxImpl extends ElementImpl implements Listbox {
	private final org.openqa.selenium.support.ui.Select innerSelect;

	/**
	 * @summary - Wraps a WebElement with listbox functionality.
	 * @param element
	 *            - element to wrap up
	 */
	public ListboxImpl(WebElement element) {
		super(element);
		this.innerSelect = new org.openqa.selenium.support.ui.Select(element);
	}

	public ListboxImpl(OrasiDriver driver, By by) {
		super(driver, by);
		//element = driver.findWebElement(by);
		TestReporter.logTrace("Entering ListboxImpl#init");
		this.innerSelect = new org.openqa.selenium.support.ui.Select(driver.findWebElement(by));
		TestReporter.logTrace("Exiting ListboxImpl#init");
	}

	/**
	 * @summary - Wraps Selenium's method.
	 * @param text
	 *            - visible text to select
	 * @see org.openqa.selenium.support.ui.Select#selectByVisibleText(String)
	 */
	@Override
	public void select(String text) {
		TestReporter.logTrace("Entering ListboxImpl#select");
		if (!text.isEmpty()) {
			try {
				try {
					innerSelect.selectByVisibleText(text);
				} catch (RuntimeException rte) {
					TestReporter.interfaceLog("Select option [ <b>" + text.toString()
							+ "</b> ] from Listbox [  <b>" + getElementLocatorInfo() + " </b>]", true);
					throw rte;
				}

				TestReporter.interfaceLog("Select option [ <b>" + text.toString()
						+ "</b> ] from Listbox [  <b>" + getElementLocatorInfo() + " </b>]");
			} catch (NoSuchElementException e) {
				String optionList = "";
				List<WebElement> optionsList = innerSelect.getOptions();
				for (WebElement option : optionsList) {
					optionList += option.getText() + " | ";
				}
				TestReporter.interfaceLog(" The value of <b>[ " + text + "</b> ] was not found in Listbox [  <b>"
								+ getElementLocatorInfo() + " </b>]. Acceptable values are " + optionList + " ]");
				TestReporter.logTrace("Exiting ListboxImpl#select");
				throw new OptionNotInListboxException("The value of [ " + text + " ] was not found in Listbox [  "
						+ getElementLocatorInfo() + " ]. Acceptable values are " + optionList, getWrappedDriver());
			}
		} else {
			TestReporter.interfaceLog("Skipping input to Textbox [ <b>" + getElementLocatorInfo() + " </b> ]");
		}

		TestReporter.logTrace("Exiting ListboxImpl#select");
	}

	/**
	 * @summary - Wraps Selenium's method.
	 * @param value
	 *            - option value to select
	 * @see org.openqa.selenium.support.ui.Select#selectByVisibleText(String)
	 */
	@Override
	public void selectValue(String value) {
		TestReporter.logTrace("Entering ListboxImpl#selectValue");
		if (!value.isEmpty()) {
			try {
				try {
					innerSelect.selectByValue(value);
				} catch (RuntimeException rte) {
					TestReporter.interfaceLog("Select option [ <b>" + value.toString()
							+ "</b> ] from Listbox [  <b>" + getElementLocatorInfo() + " </b>]", true);
					throw rte;
				}

				TestReporter.interfaceLog("Select option [ <b>" + value.toString()
						+ "</b> ] from Listbox [  <b>" + getElementLocatorInfo() + " </b>]");
			} catch (NoSuchElementException e) {
				String optionList = "";
				List<WebElement> optionsList = innerSelect.getOptions();
				for (WebElement option : optionsList) {
					optionList += option.getAttribute("value") + " | ";
				}
				TestReporter
						.interfaceLog(" The value of <b>[ " + value + "</b> ] was not found in Listbox [  <b>"
								+ getElementLocatorInfo() + " </b>]. Acceptable values are " + optionList + " ]");
				TestReporter.logTrace("Exiting ListboxImpl#selectValue");
				throw new OptionNotInListboxException("The value of [ " + value + " ] was not found in Listbox [  "
						+ getElementLocatorInfo() + " ]. Acceptable values are " + optionList, getWrappedDriver());
			}
		} else {
			TestReporter.interfaceLog("Skipping input to Textbox [ <b>" + getElementLocatorInfo() + " </b> ]");
		}
		TestReporter.logTrace("Exiting ListboxImpl#selectValue");
	}

	/**
	 * @summary - Wraps Selenium's method.
	 * @see org.openqa.selenium.support.ui.Select#deselectAll()
	 */
	@Override
	public void deselectAll() {
		TestReporter.logTrace("Entering ListboxImpl#deselectAll");
		innerSelect.deselectAll();
		TestReporter.logTrace("Exiting ListboxImpl#deselectAll");
	}

	/**
	 * @summary - Wraps Selenium's method.
	 * @return list of all options in the select.
	 * @see org.openqa.selenium.support.ui.Select#getOptions()
	 */
	@Override
	public List<WebElement> getOptions() {
		TestReporter.logTrace("Entering ListboxImpl#getOptions");
		List<WebElement> options = innerSelect.getOptions();
		TestReporter.logTrace("Exiting ListboxImpl#getOptions");
		return options;
	}

	/**
	 * @summary - Wraps Selenium's method.
	 * @param text
	 *            text to deselect by visible text
	 * @see org.openqa.selenium.support.ui.Select#deselectByVisibleText(String)
	 */
	@Override
	public void deselectByVisibleText(String text) {
		TestReporter.logTrace("Entering ListboxImpl#deselectByVisibleText");
		innerSelect.deselectByVisibleText(text);
		TestReporter.logTrace("Exiting ListboxImpl#deselectByVisibleText");
	}

	/**
	 * @summary - Wraps Selenium's method.
	 * @return WebElement of the first selected option.
	 * @see org.openqa.selenium.support.ui.Select#getFirstSelectedOption()
	 */
	@Override
	public WebElement getFirstSelectedOption() {
		TestReporter.logTrace("Entering ListboxImpl#getFirstSelectedOption");
		try {
			WebElement option = innerSelect.getFirstSelectedOption();
			TestReporter.logTrace("Exiting ListboxImpl#deselectByVisibleText");
			return option;
		} catch (NoSuchElementException nse) {
			TestReporter.logTrace("Exiting ListboxImpl#deselectByVisibleText");
			return null;
		}
	}

	/**
	 * @see org.openqa.selenium.WebElement#isSelected()
	 */
	@Override
	public boolean isSelected(String option) {
		TestReporter.logTrace("Entering ListboxImpl#isSelected");
		List<WebElement> selectedOptions = innerSelect.getAllSelectedOptions();
		for (WebElement selectOption : selectedOptions) {
			if (selectOption.getText().equals(option)){
				TestReporter.logTrace("Exiting ListboxImpl#isSelected");
				return true;
			}
		}
		TestReporter.logTrace("Exiting ListboxImpl#isSelected");
		return false;
	}

	@Override
	public List<WebElement> getAllSelectedOptions() {
		TestReporter.logTrace("Entering ListboxImpl#getAllSelectedOptions");
		List<WebElement> options = innerSelect.getAllSelectedOptions();
		TestReporter.logTrace("Exiting ListboxImpl#getAllSelectedOptions");
		return options;
	}

	@Override
	public boolean isMultiple() {
		TestReporter.logTrace("Entering ListboxImpl#isMultiple");
		boolean multiple = innerSelect.isMultiple();
		TestReporter.logTrace("Exiting ListboxImpl#isMultiple");
		return multiple;
	}
}