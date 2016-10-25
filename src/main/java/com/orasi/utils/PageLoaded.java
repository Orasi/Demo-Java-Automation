package com.orasi.utils;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriverException;
import com.orasi.core.interfaces.Element;
import com.orasi.core.interfaces.impl.internal.ElementFactory;
import com.orasi.exception.automation.PageInitialization;

/**
 * Several different methods of waiting for a page to finish loading.
 * 
 * 
 * @version 10/16/2014
 * @author Justin Phlegar
 * 
 */
public class PageLoaded {
	/**
	 * This waits for a specified element on the page to be found on the page by
	 * the driver Uses the default test time out set by WebDriverSetup
	 * 
	 * @param clazz
	 *            the class calling this method - used so can initialize the
	 *            page class repeatedly
	 * @param oDriver
	 *            The webDriver
	 * @param obj
	 *            The element you are waiting to display on the page
	 * @version 10/16/2014
	 * @author Justin Phlegar
	 * @return False if the element is not found after the timeout, true if is
	 *         found
	 */
	@SuppressWarnings("rawtypes")
	public static boolean isElementLoaded(Class clazz, OrasiDriver oDriver, Element obj) {		
		return isElementLoaded(clazz, oDriver, obj, oDriver.getElementTimeout());
	}

	/**
	 * Overloaded method where you can specify the timeout This waits for a
	 * specified element on the page to be found on the page by the driver
	 * 
	 * 
	 * @param clazz
	 *            the class calling this method - used so can initialize the
	 *            page class repeatedly
	 * @param oDriver
	 *            The webDriver
	 * @param obj
	 *            The element you are waiting to display on the page
	 * @version 12/16/2014
	 * @author Jessica Marshall
	 * @return False if the element is not found after the timeout, true if is
	 *         found
	 */
	@SuppressWarnings("rawtypes")
	public static boolean isElementLoaded(Class clazz, OrasiDriver oDriver, Element obj, int timeout) {
		int count = 0;
		int driverTimeout = oDriver.getElementTimeout();
		// set the timeout for looking for an element to 1 second as we are
		// doing a loop and then refreshing the elements
		oDriver.setElementTimeout(1, TimeUnit.MILLISECONDS);

		try {

			while (!obj.elementWired()) {
				if (count == timeout) {
					break;
				} else {
					count++;
					initializePage(clazz, oDriver);
				}
			}

		} catch (NullPointerException | NoSuchElementException | StaleElementReferenceException | PageInitialization e){
			return false;
		} finally{
			// set the timeout for looking for an element back to the default timeout
			oDriver.setElementTimeout(driverTimeout, TimeUnit.SECONDS);
		}

		if (count < timeout) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * This uses the HTML DOM readyState property to wait until a page is
	 * finished loading. It will wait for the ready state to be either
	 * 'interactive' or 'complete'.
	 *
	 * @version 12/16/2014
	 * @author Jessica Marshall
	 * @return False if the element is not found after the timeout, true if is
	 *         found
	 */
	public static boolean isDomInteractive(OrasiDriver oDriver) {
		return isDomInteractive(oDriver, oDriver.getPageTimeout());
	}

	/**
	 * Overloaded method - gives option of specifying a timeout. This uses the
	 * HTML DOM readyState property to wait until a page is finished loading. It
	 * will wait for the ready state to be either 'interactive' or 'complete'.
	 * 
	 * @param oDriver
	 *            The webDriver
	 * @param timeout
	 *            Integer value of number seconds to wait for a page to finish
	 *            loaded before quiting
	 * @version 12/16/2014
	 * @author Jessica Marshall
	 * @return False if the element is not found after the timeout, true if is
	 *         found
	 */
	public static boolean isDomInteractive(OrasiDriver oDriver, int timeout) {
		int count = 0;
		Object obj = null;

		do {
			// this returns a boolean
			obj = oDriver.executeJavaScript(
					"var result = document.readyState; return (result == 'complete' || result == 'interactive');");
			if (count == timeout)
				break;
			else {
				Sleeper.sleep(500);
				count++;

			}
		} while (obj.equals(false));

		if (count < timeout * 2) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * This uses protractor method to wait until a page is ready -
	 * notifyWhenNoOutstandingRequests
	 *
	 * @version 10/16/2014
	 * @author Justin Phlegar
	 * 
	 */
	public static void isAngularComplete(OrasiDriver oDriver) {
		try {
			oDriver.executeAsyncJavaScript("var callback = arguments[arguments.length - 1];"
					+ "angular.element(document.body).injector().get('$browser').notifyWhenNoOutstandingRequests(callback);");
		} catch (WebDriverException wde) {
			TestReporter.logFailure(
					"Unable to perform Angular sync. This is most likely because the $browser service is not injected within the Angular Controller. Performing a IsDomComplete instead");
			isDomComplete(oDriver);
		}

	}


	/**
	 * A more strict version of isDomInteractive. This uses the HTML DOM
	 * readyState property to wait until a page is finished loading. It will
	 * wait for the ready state to be 'complete'.
	 * 
	 * @param oDriver
	 *            The webDriver
	 * @version 12/16/2014
	 * @author Jessica Marshall
	 * @return False if the element is not found after the timeout, true if is
	 *         found
	 */
	public static boolean isDomComplete(OrasiDriver oDriver) {
		return isDomComplete(oDriver, oDriver.getPageTimeout());
	}

	/**
	 * Overloaded method - gives option of specifying a timeout. A more strict
	 * version of isDomInteractive This uses the HTML DOM readyState property to
	 * wait until a page is finished loading. It will wait for the ready state
	 * to be 'complete'.
	 * 
	 * @param oDriver
	 *            The webDriver
	 * @param timeout
	 *            Integer value of number seconds to wait for a page to finish
	 *            loaded before quiting
	 * @version 12/16/2014
	 * @author Jessica Marshall
	 * @return False if the element is not found after the timeout, true if is
	 *         found
	 */
	public static boolean isDomComplete(OrasiDriver oDriver, int timeout) {
		int count = 0;
		Object obj = null;

		do {
			// this returns a boolean
			obj = oDriver.executeJavaScript("var result = document.readyState; return (result == 'complete');");

			if (count == timeout)
				break;
			else {
				Sleeper.sleep(500);
				count++;    
			}

		} while (obj.equals(false));

		if (count < timeout * 2) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @summary Used to create all page objects WebElements as proxies (not
	 *          actual objects, but rather placeholders) or to reinitialize all
	 *          page object WebElements to allow for the state of a page to
	 *          change and not fail a test
	 * @return N/A
	 * @param clazz - page class that is calling this method for which to initialize elements
	 * 
	 * @param oDriver - The webDriver
	 */
	public static void initializePage(Class<?> clazz, OrasiDriver oDriver) {
		try {
			ElementFactory.initElements(oDriver, clazz.getConstructor(TestEnvironment.class));
		} catch (NoSuchMethodException | SecurityException e) {
			throw new PageInitialization("Unable to initialize page", oDriver);
		}
	}

}