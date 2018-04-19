package com.orasi.web;

import static com.orasi.utils.TestReporter.logFailure;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.orasi.AutomationException;
import com.orasi.DriverManager;
import com.orasi.utils.Constants;
import com.orasi.web.webelements.Element;

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
     * @deprecated - No longer required as elements have internal reload functionality
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
    @Deprecated
    public static boolean isElementLoaded(Class clazz, OrasiDriver oDriver, Element obj) {
        return isElementLoaded(clazz, oDriver, obj, oDriver.getElementTimeout());
    }

    /**
     * Overloaded method where you can specify the timeout This waits for a
     * specified element on the page to be found on the page by the driver
     *
     * @deprecated - No longer required as elements have internal reload functionality
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
    @Deprecated
    public static boolean isElementLoaded(Class clazz, OrasiDriver oDriver, Element obj, int timeout) {

        return obj.elementWired();

        // All other code unneeded since Element will reload internally

        /*
         * int count = 0;
         * int driverTimeout = oDriver.getElementTimeout();
         * // set the timeout for looking for an element to 1 second as we are
         * // doing a loop and then refreshing the elements
         * oDriver.setElementTimeout(1, TimeUnit.MILLISECONDS);
         *
         * try {
         *
         * while (!obj.elementWired()) {
         * if (count == timeout) {
         * break;
         * }
         * count++;
         *
         * }
         *
         * } catch (NullPointerException | NoSuchElementException | StaleElementReferenceException | PageInitialization e) {
         * return false;
         * } finally {
         * // set the timeout for looking for an element back to the default timeout
         * oDriver.setElementTimeout(driverTimeout, TimeUnit.SECONDS);
         * }
         *
         * if (count < timeout) {
         * return true;
         * }
         *
         * return false;
         */

    }

    /**
     * This uses the HTML DOM readyState property to wait until a page is
     * finished loading. It will wait for the ready state to be either
     * 'interactive' or 'complete'.
     *
     * @deprecated user vresio without driver call
     * @version 12/16/2014
     * @author Jessica Marshall
     * @return False if the element is not found after the timeout, true if is
     *         found
     */
    @Deprecated
    public static boolean isDomInteractive(OrasiDriver oDriver) {
        return isDomInteractive(oDriver, oDriver.getPageTimeout());
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
    public static boolean isDomInteractive() {
        OrasiDriver driver = DriverManager.getDriver();
        return isDomInteractive(driver.getPageTimeout());
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
    public static boolean isDomInteractive(int timeout) {
        String js = "var result = document.readyState; return (result == 'complete' || result == 'interactive');";
        OrasiDriver driver = DriverManager.getDriver();

        // Create internal ExpectedCondition via Lambda
        ExpectedCondition<Boolean> jQueryLoad = ((WebDriver dr) -> ((Boolean) ((JavascriptExecutor) dr).executeScript(js)));
        WebDriverWait wait = new WebDriverWait(driver.getWebDriver(), timeout);
        try {
            wait.pollingEvery(Constants.MILLISECONDS_TO_POLL_FOR_ELEMENT, TimeUnit.MILLISECONDS).until(jQueryLoad);
        } catch (WebDriverException e) {
            return false;
        }
        return true;

    }

    /**
     * Overloaded method - gives option of specifying a timeout. This uses the
     * HTML DOM readyState property to wait until a page is finished loading. It
     * will wait for the ready state to be either 'interactive' or 'complete'.
     *
     * @deprecated user version without the driver in parameter
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
    @Deprecated
    public static boolean isDomInteractive(OrasiDriver oDriver, int timeout) {
        return isDomInteractive(timeout);

    }

    /**
     * This uses protractor method to wait until a page is ready -
     * notifyWhenNoOutstandingRequests
     *
     * @version 10/16/2014
     * @author Justin Phlegar
     *
     */
    public static boolean isAngularComplete() {
        OrasiDriver driver = DriverManager.getDriver();
        try {
            driver.executeAsyncJavaScript("var callback = arguments[arguments.length - 1];"
                    + "angular.element(document.body).injector().get('$browser').notifyWhenNoOutstandingRequests(callback);");
        } catch (WebDriverException wde) {
            logFailure(
                    "Unable to perform Angular sync. This is most likely because the $browser service is not injected within the Angular Controller. Performing a IsDomComplete instead");
            return isDomComplete();
        }
        return true;
    }

    /**
     * This uses protractor method to wait until a page is ready -
     * notifyWhenNoOutstandingRequests
     * 
     * @deprecated user version without driver in parameter
     * @version 10/16/2014
     * @author Justin Phlegar
     *
     */
    @Deprecated
    public static boolean isAngularComplete(OrasiDriver oDriver) {
        return isAngularComplete();
    }

    /**
     * Run four checks for jQuery readyState. <br/>
     * <br/>
     * Validate jQuery is defined
     * <br/>
     * Validate jQuery is accessible globally
     * <br/>
     * Validate no active jQuery actions
     * <br/>
     * Validate DOM readystate is complete
     *
     * @return true or false if jQuery was done within time frame. <br/>
     *         If jQuery is not defined on page after sync, throw exception to warn user not to use this method
     */
    public static boolean isJQueryComplete() {
        return isJQueryComplete(DriverManager.getDriver().getPageTimeout());
    }

    /**
     * Run four checks for jQuery readyState. <br/>
     * <br/>
     * Validate jQuery is defined
     * <br/>
     * Validate jQuery is accessible globally
     * <br/>
     * Validate no active jQuery actions
     * <br/>
     * Validate DOM readystate is complete
     *
     * @return true or false if jQuery was done within time frame. <br/>
     *         If jQuery is not defined on page after sync, throw exception to warn user not to use this method
     */
    public static boolean isJQueryComplete(int timeout) {
        String js = "return typeof jQuery != 'undefined' && !!window.jQuery && jQuery.active==0 && document.readyState == 'complete'";
        OrasiDriver driver = DriverManager.getDriver();

        // Create internal ExpectedCondition via Lambda
        ExpectedCondition<Boolean> jQueryLoad = ((WebDriver dr) -> ((Boolean) ((JavascriptExecutor) dr).executeScript(js)));
        WebDriverWait wait = new WebDriverWait(driver.getWebDriver(), timeout);
        try {
            wait.pollingEvery(Constants.MILLISECONDS_TO_POLL_FOR_ELEMENT, TimeUnit.MILLISECONDS).until(jQueryLoad);
        } catch (WebDriverException e) {

            // Check to see if user needs to call isJQueryComplete on this page
            boolean jQueryDefined = (Boolean) driver.executeJavaScript("return typeof jQuery == 'undefined'");
            if (jQueryDefined) {
                // No jQuery defined on page, throw exception warning user not to use command
                throw new AutomationException("No jQuery found on page. Use isDomComplete here instead", e);
            } else {
                return false;
            }
        }
        return true;
    }

    /**
     * A more strict version of isDomInteractive. This uses the HTML DOM
     * readyState property to wait until a page is finished loading. It will
     * wait for the ready state to be 'complete'.
     *
     * @param driver
     *            The webDriver
     * @version 12/16/2014
     * @author Jessica Marshall
     * @return False if the element is not found after the timeout, true if is
     *         found
     */
    public static boolean isDomComplete() {
        OrasiDriver driver = DriverManager.getDriver();
        return isDomComplete(driver, driver.getPageTimeout());
    }

    /**
     * A more strict version of isDomInteractive. This uses the HTML DOM
     * readyState property to wait until a page is finished loading. It will
     * wait for the ready state to be 'complete'.
     *
     * @deprecated use version without driver parameter
     * @param driver
     *            The webDriver
     * @version 12/16/2014
     * @author Jessica Marshall
     * @return False if the element is not found after the timeout, true if is
     *         found
     */
    @Deprecated
    public static boolean isDomComplete(OrasiDriver driver) {
        return isDomComplete(driver, driver.getPageTimeout());
    }

    /**
     * Overloaded method - gives option of specifying a timeout. A more strict
     * version of isDomInteractive This uses the HTML DOM readyState property to
     * wait until a page is finished loading. It will wait for the ready state
     * to be 'complete'.
     *
     * @param driver
     *            The webDriver
     * @param timeout
     *            Integer value of number seconds to wait for a page to finish
     *            loaded before quiting
     * @version 12/16/2014
     * @author Jessica Marshall
     * @return False if the element is not found after the timeout, true if is
     *         found
     */
    public static boolean isDomComplete(int timeout) {
        String js = "var result = document.readyState; return (result == 'complete');";
        OrasiDriver driver = DriverManager.getDriver();

        // Create internal ExpectedCondition via Lambda
        ExpectedCondition<Boolean> jQueryLoad = ((WebDriver dr) -> ((Boolean) ((JavascriptExecutor) dr).executeScript(js)));
        WebDriverWait wait = new WebDriverWait(driver.getWebDriver(), timeout);
        try {
            wait.pollingEvery(Constants.MILLISECONDS_TO_POLL_FOR_ELEMENT, TimeUnit.MILLISECONDS).until(jQueryLoad);
        } catch (WebDriverException e) {
            return false;
        }
        return true;
    }

    /**
     * Overloaded method - gives option of specifying a timeout. A more strict
     * version of isDomInteractive This uses the HTML DOM readyState property to
     * wait until a page is finished loading. It will wait for the ready state
     * to be 'complete'.
     * 
     * @deprecated user version without driver call
     * @param driver
     *            The webDriver
     * @param timeout
     *            Integer value of number seconds to wait for a page to finish
     *            loaded before quiting
     * @version 12/16/2014
     * @author Jessica Marshall
     * @return False if the element is not found after the timeout, true if is
     *         found
     */
    @Deprecated
    public static boolean isDomComplete(OrasiDriver driver, int timeout) {
        return isDomComplete(timeout);
    }
}