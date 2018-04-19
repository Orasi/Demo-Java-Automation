package com.orasi.web;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Platform;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteExecuteMethod;
import org.openqa.selenium.remote.RemoteKeyboard;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;

import com.orasi.DriverType;
import com.orasi.utils.Constants;
import com.orasi.utils.JavaUtilities;
import com.orasi.utils.TestReporter;
import com.orasi.utils.dataHelpers.DataWarehouse;
import com.orasi.web.debugging.Colors;
import com.orasi.web.debugging.Highlight;
import com.orasi.web.webelements.Button;
import com.orasi.web.webelements.Checkbox;
import com.orasi.web.webelements.Element;
import com.orasi.web.webelements.Label;
import com.orasi.web.webelements.Link;
import com.orasi.web.webelements.Listbox;
import com.orasi.web.webelements.RadioGroup;
import com.orasi.web.webelements.Textbox;
import com.orasi.web.webelements.Webtable;
import com.orasi.web.webelements.impl.ButtonImpl;
import com.orasi.web.webelements.impl.CheckboxImpl;
import com.orasi.web.webelements.impl.ElementImpl;
import com.orasi.web.webelements.impl.LabelImpl;
import com.orasi.web.webelements.impl.LinkImpl;
import com.orasi.web.webelements.impl.ListboxImpl;
import com.orasi.web.webelements.impl.RadioGroupImpl;
import com.orasi.web.webelements.impl.TextboxImpl;
import com.orasi.web.webelements.impl.WebtableImpl;

public class OrasiDriver implements WebDriver, TakesScreenshot {
    private WebDriver driver;
    private DataWarehouse dataWarehouse;
    private int currentPageTimeout;
    private int currentElementTimeout;
    private int currentScriptTimeout;
    public static boolean DEFAULT_SYNC_HANDLER = true;
    private DriverType driverType;

    public OrasiDriver() {
    }

    /**
     * Constructor for OrasiDriver
     * Example usage: OrasiDriver oDriver = new OrasiDriver(caps);
     *
     * @param caps
     *            - Selenium desired capabilities, used to configure the OrasiDriver
     * @deprecated - Should use DriverManagerFactory to create OrasiDriver
     */
    @Deprecated
    public OrasiDriver(DesiredCapabilities caps) {
        currentPageTimeout = Constants.PAGE_TIMEOUT;
        currentElementTimeout = Constants.ELEMENT_TIMEOUT;
        currentScriptTimeout = Constants.DEFAULT_GLOBAL_DRIVER_TIMEOUT;
        setDriverWithCapabilties(caps);
    }

    /**
     * Constructor for OrasiDriver, specifically used to generate a remote WebDriver
     * Example usage: OrasiDriver oDriver = new OrasiDriver(caps, url);
     *
     * @param caps
     *            - Selenium desired capabilities, used to configure the OrasiDriver
     * @param url
     * @deprecated - Should use DriverManagerFactory to create OrasiDriver
     */
    @Deprecated
    public OrasiDriver(DesiredCapabilities caps, URL url) {
        currentPageTimeout = Constants.PAGE_TIMEOUT;
        currentElementTimeout = Constants.ELEMENT_TIMEOUT;
        currentScriptTimeout = Constants.DEFAULT_GLOBAL_DRIVER_TIMEOUT;
        driver = new RemoteWebDriver(url, caps);
    }

    /**
     * Method to return the current OrasiDriver
     * Example usage: getDriver().getDriver();
     *
     * @return - current OrasiDriver
     */
    public WebDriver getWebDriver() {
        return driver;
    }

    public void setDriver(WebDriver driver) {
        this.driver = driver;
    }

    public DriverType getDriverType() {
        return driverType;
    }

    public void setDriverType(DriverType driverType) {
        this.driverType = driverType;
    }

    /**
     * Method to navigate to a user-defined URL
     * Example usage: getDriver().get(url);
     *
     * @param url
     *            - URL to which to navigate
     * @see http://selenium.googlecode.com/git/docs/api/java/org/openqa/selenium/WebDriver.html#get-java.lang.String-
     */
    @Override
    public void get(String url) {
        driver.get(url);

    }

    /**
     * Method to return the current URL
     * Example usage: getDriver().getCurrentUrl();
     *
     * @return - current URL
     * @see http://selenium.googlecode.com/git/docs/api/java/org/openqa/selenium/WebDriver.html#getCurrentUrl--
     */
    @Override
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    /**
     * Method to return the title of the current page
     * Example usage: getDriver().getTitle();
     *
     * @return - title of the current page
     * @see http://selenium.googlecode.com/git/docs/api/java/org/openqa/selenium/WebDriver.html#getTitle--
     */
    @Override
    public String getTitle() {
        return driver.getTitle();
    }

    /**
     * Method to set the script timeout
     *
     * @param timeout
     *            - timeout with which to set the script timeout
     * @see http://selenium.googlecode.com/git/docs/api/java/org/openqa/selenium/WebDriver.Timeouts.html#setScriptTimeout-long-java.util.concurrent.TimeUnit-
     */
    public void setScriptTimeout(int timeout) {
        setScriptTimeout(timeout, TimeUnit.SECONDS);
    }

    /**
     * Method to set the script timeout
     *
     * @param timeout
     *            - timeout with which to set the script timeout
     * @param timeUnit
     *            -Java TimeUnit, used to determine the unit of time to be associated with the timeout
     * @see http://docs.oracle.com/javase/8/docs/api/java/util/concurrent/TimeUnit.html
     * @see http://selenium.googlecode.com/git/docs/api/java/org/openqa/selenium/WebDriver.Timeouts.html#setScriptTimeout-long-java.util.concurrent.TimeUnit-
     */
    public void setScriptTimeout(int timeout, TimeUnit timeUnit) {
        this.currentScriptTimeout = timeout;
        driver.manage().timeouts().setScriptTimeout(timeout, timeUnit);
    }

    /**
     * Method to return the script timeout
     *
     * @return - script timeout
     */
    public int getScriptTimeout() {
        return currentScriptTimeout;
    }

    /**
     * Method to set the page timeout
     *
     * @param timeout
     *            - timeout with which to set the page timeout
     * @see http://selenium.googlecode.com/git/docs/api/java/org/openqa/selenium/WebDriver.Timeouts.html#pageLoadTimeout-long-java.util.concurrent.TimeUnit-
     */
    public void setPageTimeout(int timeout) {
        setPageTimeout(timeout, TimeUnit.SECONDS);
    }

    /**
     * Method to set the page timeout
     *
     * @param timeout
     *            - timeout with which to set the page timeout
     * @param timeUnit
     *            -Java TimeUnit, used to determine the unit of time to be associated with the timeout
     * @see http://docs.oracle.com/javase/8/docs/api/java/util/concurrent/TimeUnit.html
     * @see http://selenium.googlecode.com/git/docs/api/java/org/openqa/selenium/WebDriver.Timeouts.html#pageLoadTimeout-long-java.util.concurrent.TimeUnit-
     */
    public void setPageTimeout(int timeout, TimeUnit timeUnit) {
        if (!(driver instanceof SafariDriver) || !driver.toString().contains("safari")) {
            this.currentPageTimeout = timeout;
            driver.manage().timeouts().pageLoadTimeout(timeout, timeUnit);
        }
    }

    /**
     * Method to return the page timeout
     *
     * @return - page timeout
     */
    public int getPageTimeout() {
        return currentPageTimeout;
    }

    /**
     * Method to set element timeout
     *
     * @param timeout
     *            - timeout with which to set the element timeout
     * @see http://selenium.googlecode.com/git/docs/api/java/org/openqa/selenium/WebDriver.Timeouts.html#implicitlyWait-long-java.util.concurrent.TimeUnit-
     */
    public void setElementTimeout(int timeout) {
        setElementTimeout(timeout, TimeUnit.SECONDS);
    }

    /**
     * Method to set the element timeout
     *
     * @param timeout
     *            - timeout with which to set the element timeout
     * @param timeUnit
     * @see http://docs.oracle.com/javase/8/docs/api/java/util/concurrent/TimeUnit.html
     * @see http://selenium.googlecode.com/git/docs/api/java/org/openqa/selenium/WebDriver.Timeouts.html#implicitlyWait-long-java.util.concurrent.TimeUnit-
     */
    public void setElementTimeout(int timeout, TimeUnit timeUnit) {
        this.currentElementTimeout = timeout;
        driver.manage().timeouts().implicitlyWait(timeout, timeUnit);
    }

    /**
     * Method to return the element timeout
     *
     * @return - element timeout
     */
    public int getElementTimeout() {
        return currentElementTimeout;
    }

    /**
     * Used when you want to upload a local file to the remote webdriver for use
     * on the selenium grid nodes.
     * You would then just use your normal sendKeys() method to populate the upload
     * input with a local file and selenium will transfer file across the wire to the grid
     */
    public void setFileDetector() {
        ((RemoteWebDriver) driver).setFileDetector(new LocalFileDetector());
    }

    /*
     * public List<Element> findElements(By by) { List<WebElement> webElements =
     * driver.findElements(by); List test = webElements; List<Element> elements=
     * (List<Element>)test; return elements; }
     */
    /**
     * Method to find all WebElements for a given page, using a Selenium <b><i>By</i></b> locator
     *
     * @param by
     *            - Selenium <b><i>By</i></b> locator with which to locate WebElements
     * @return - List of WebElements, if any, found by using the Selenium <b><i>By</i></b> locator
     * @see http://docs.oracle.com/javase/8/docs/api/java/util/List.html
     * @see http://selenium.googlecode.com/svn/trunk/docs/api/java/org/openqa/selenium/By.html
     * @see http://selenium.googlecode.com/git/docs/api/java/org/openqa/selenium/WebDriver.html#findElements-org.openqa.selenium.By-
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<Element> findElements(By by) {
        List<WebElement> elements = driver.findElements(by);
        List<Element> el = new ArrayList<>();
        elements.forEach(element -> el.add(new ElementImpl(this, by, element)));
        return el;
    }

    /**
     * Method to find all WebElements for a given page, using a Selenium <b><i>By</i></b> locator
     *
     * @param by
     *            - Selenium <b><i>By</i></b> locator with which to locate WebElements
     * @return - List of WebElements, if any, found by using the Selenium <b><i>By</i></b> locator
     * @see http://docs.oracle.com/javase/8/docs/api/java/util/List.html
     * @see http://selenium.googlecode.com/svn/trunk/docs/api/java/org/openqa/selenium/By.html
     * @see http://selenium.googlecode.com/git/docs/api/java/org/openqa/selenium/WebDriver.html#findElements-org.openqa.selenium.By-
     */
    public List<WebElement> findWebElements(By by) {
        return driver.findElements(by);
    }

    /**
     * Method to find a single Element for a given page, using a Selenium <b><i>By</i></b> locator
     *
     * @param by
     *            - Selenium <b><i>By</i></b> locator with which to locate the Element
     * @return Element, if any, found by using the Selenium <b><i>By</i></b> locator
     * @see http://selenium.googlecode.com/svn/trunk/docs/api/java/org/openqa/selenium/By.html
     * @see http://selenium.googlecode.com/git/docs/api/java/org/openqa/selenium/WebDriver.html#findElement-org.openqa.selenium.By-
     * @see http://github.com/Orasi/Chameleon/blob/master/src/main/java/com/orasi/web/webelements/impl/ElementImpl.java
     */
    @SuppressWarnings("unchecked")
    @Override
    public Element findElement(By by) {
        return new ElementImpl(this, by);
    }

    /**
     * Method to find a single Textbox for a given page, using a Selenium <b><i>By</i></b> locator
     *
     * @param by
     *            - Selenium <b><i>By</i></b> locator with which to locate the Textbox
     * @return Textbox, if any, found by using the Selenium <b><i>By</i></b> locator
     * @see http://selenium.googlecode.com/svn/trunk/docs/api/java/org/openqa/selenium/By.html
     * @see http://selenium.googlecode.com/git/docs/api/java/org/openqa/selenium/WebDriver.html#findElement-org.openqa.selenium.By-
     * @see http://github.com/Orasi/Chameleon/blob/master/src/main/java/com/orasi/web/webelements/impl/TextboxImpl.java
     */
    public Textbox findTextbox(By by) {
        return new TextboxImpl(this, by);
    }

    /**
     * Method to find a single Textbox for a given page, using a Selenium <b><i>By</i></b> locator
     *
     * @param by
     *            - Selenium <b><i>By</i></b> locator with which to locate the Textbox
     * @return Textbox, if any, found by using the Selenium <b><i>By</i></b> locator
     * @see http://selenium.googlecode.com/svn/trunk/docs/api/java/org/openqa/selenium/By.html
     * @see http://selenium.googlecode.com/git/docs/api/java/org/openqa/selenium/WebDriver.html#findElement-org.openqa.selenium.By-
     * @see http://github.com/Orasi/Chameleon/blob/master/src/main/java/com/orasi/web/webelements/impl/TextboxImpl.java
     */
    public List<Textbox> findTextboxes(By by) {
        List<WebElement> elements = findWebElements(by);
        List<Textbox> textboxes = new ArrayList<>();
        elements.forEach(element -> textboxes.add(new TextboxImpl(this, by, element)));
        return textboxes;
    }

    /**
     * Method to find a single Button for a given page, using a Selenium <b><i>By</i></b> locator
     *
     * @param by
     *            - Selenium <b><i>By</i></b> locator with which to locate the Button
     * @return Button, if any, found by using the Selenium <b><i>By</i></b> locator
     * @see http://selenium.googlecode.com/svn/trunk/docs/api/java/org/openqa/selenium/By.html
     * @see http://selenium.googlecode.com/git/docs/api/java/org/openqa/selenium/WebDriver.html#findElement-org.openqa.selenium.By-
     * @see http://github.com/Orasi/Chameleon/blob/master/src/main/java/com/orasi/web/webelements/impl/ButtonImpl.java
     */
    public Button findButton(By by) {
        return new ButtonImpl(this, by);
    }

    /**
     * Method to find Buttons for a given page, using a Selenium <b><i>By</i></b> locator
     *
     * @param by
     *            - Selenium <b><i>By</i></b> locator with which to locate the Buttons
     * @return Button, if any, found by using the Selenium <b><i>By</i></b> locator
     * @see http://selenium.googlecode.com/svn/trunk/docs/api/java/org/openqa/selenium/By.html
     * @see http://selenium.googlecode.com/git/docs/api/java/org/openqa/selenium/WebDriver.html#findElement-org.openqa.selenium.By-
     * @see http://github.com/Orasi/Chameleon/blob/master/src/main/java/com/orasi/web/webelements/impl/ButtonImpl.java
     */
    public List<Button> findButtons(By by) {
        List<WebElement> elements = findWebElements(by);
        List<Button> buttons = new ArrayList<>();
        elements.forEach(element -> buttons.add(new ButtonImpl(this, by, element)));
        return buttons;
    }

    /**
     * Method to find a single Checkbox for a given page, using a Selenium <b><i>By</i></b> locator
     *
     * @param by
     *            - Selenium <b><i>By</i></b> locator with which to locate the Checkbox
     * @return Checkbox, if any, found by using the Selenium <b><i>By</i></b> locator
     * @see http://selenium.googlecode.com/svn/trunk/docs/api/java/org/openqa/selenium/By.html
     * @see http://selenium.googlecode.com/git/docs/api/java/org/openqa/selenium/WebDriver.html#findElement-org.openqa.selenium.By-
     * @see http://github.com/Orasi/Chameleon/blob/master/src/main/java/com/orasi/web/webelements/impl/CheckboxImpl.java
     */
    public Checkbox findCheckbox(By by) {
        return new CheckboxImpl(this, by);
    }

    public List<Checkbox> findCheckboxes(By by) {
        List<WebElement> elements = findWebElements(by);
        List<Checkbox> checkboxes = new ArrayList<>();
        elements.forEach(element -> checkboxes.add(new CheckboxImpl(this, by, element)));
        return checkboxes;
    }

    /**
     * Method to find a single Label for a given page, using a Selenium <b><i>By</i></b> locator
     *
     * @param by
     *            - Selenium <b><i>By</i></b> locator with which to locate the Checkbox
     * @return Label, if any, found by using the Selenium <b><i>By</i></b> locator
     * @see http://selenium.googlecode.com/svn/trunk/docs/api/java/org/openqa/selenium/By.html
     * @see http://selenium.googlecode.com/git/docs/api/java/org/openqa/selenium/WebDriver.html#findElement-org.openqa.selenium.By-
     * @see http://github.com/Orasi/Chameleon/blob/master/src/main/java/com/orasi/web/webelements/impl/LabelImpl.java
     */
    public Label findLabel(By by) {
        return new LabelImpl(this, by);
    }

    public List<Label> findLabels(By by) {
        List<WebElement> elements = findWebElements(by);
        List<Label> labels = new ArrayList<>();
        elements.forEach(element -> labels.add(new LabelImpl(this, by, element)));
        return labels;
    }

    /**
     * Method to find a single Link for a given page, using a Selenium <b><i>By</i></b> locator
     *
     * @param by
     *            - Selenium <b><i>By</i></b> locator with which to locate the Link
     * @return Link, if any, found by using the Selenium <b><i>By</i></b> locator
     * @see http://selenium.googlecode.com/svn/trunk/docs/api/java/org/openqa/selenium/By.html
     * @see http://selenium.googlecode.com/git/docs/api/java/org/openqa/selenium/WebDriver.html#findElement-org.openqa.selenium.By-
     * @see http://github.com/Orasi/Chameleon/blob/master/src/main/java/com/orasi/web/webelements/impl/LinkImpl.java
     */
    public Link findLink(By by) {
        return new LinkImpl(this, by);
    }

    public List<Link> findLinks(By by) {
        List<WebElement> elements = findWebElements(by);
        List<Link> links = new ArrayList<>();
        elements.forEach(element -> links.add(new LinkImpl(this, by, element)));
        return links;
    }

    /**
     * Method to find a single Listbox for a given page, using a Selenium <b><i>By</i></b> locator
     *
     * @param by
     *            - Selenium <b><i>By</i></b> locator with which to locate the Listbox
     * @return Listbox, if any, found by using the Selenium <b><i>By</i></b> locator
     * @see http://selenium.googlecode.com/svn/trunk/docs/api/java/org/openqa/selenium/By.html
     * @see http://selenium.googlecode.com/git/docs/api/java/org/openqa/selenium/WebDriver.html#findElement-org.openqa.selenium.By-
     * @see http://github.com/Orasi/Chameleon/blob/master/src/main/java/com/orasi/web/webelements/impl/ListboxImpl.java
     */
    public Listbox findListbox(By by) {
        return new ListboxImpl(this, by);
    }

    /**
     * Method to find a single RadioGroup for a given page, using a Selenium <b><i>By</i></b> locator
     *
     * @param by
     *            - Selenium <b><i>By</i></b> locator with which to locate the RadioGroup
     * @return RadioGroup, if any, found by using the Selenium <b><i>By</i></b> locator
     * @see http://selenium.googlecode.com/svn/trunk/docs/api/java/org/openqa/selenium/By.html
     * @see http://selenium.googlecode.com/git/docs/api/java/org/openqa/selenium/WebDriver.html#findElement-org.openqa.selenium.By-
     * @see http://github.com/Orasi/Chameleon/blob/master/src/main/java/com/orasi/web/webelements/impl/RadioGroupImpl.java
     */
    public RadioGroup findRadioGroup(By by) {
        return new RadioGroupImpl(this, by);
    }

    /**
     * Method to find a single Webtable for a given page, using a Selenium <b><i>By</i></b> locator
     *
     * @param by
     *            - Selenium <b><i>By</i></b> locator with which to locate the Webtable
     * @return Webtable, if any, found by using the Selenium <b><i>By</i></b> locator
     * @see http://selenium.googlecode.com/svn/trunk/docs/api/java/org/openqa/selenium/By.html
     * @see http://selenium.googlecode.com/git/docs/api/java/org/openqa/selenium/WebDriver.html#findElement-org.openqa.selenium.By-
     * @see http://github.com/Orasi/Chameleon/blob/master/src/main/java/com/orasi/web/webelements/impl/WebtableImpl.java
     */
    public Webtable findWebtable(By by) {
        return new WebtableImpl(this, by);
    }

    /**
     * Method to find a single WebElement for a given page, using a Selenium <b><i>By</i></b> locator
     *
     * @param by
     *            - Selenium <b><i>By</i></b> locator with which to locate the WebElement
     * @return WebElement, if any, found by using the Orasi <b><i>ByNG</i></b> locator
     * @see http://selenium.googlecode.com/git/docs/api/java/org/openqa/selenium/WebDriver.html#findElement-org.openqa.selenium.By-
     * @see http://selenium.googlecode.com/svn/trunk/docs/api/java/org/openqa/selenium/By.html
     * @see http://selenium.googlecode.com/svn/trunk/docs/api/java/org/openqa/selenium/WebElement.html
     */
    public WebElement findWebElement(By by) {
        return driver.findElement(by);
    }

    /**
     * Method to return the page source of a given current page
     *
     * @return page source of the given current page
     * @see http://selenium.googlecode.com/git/docs/api/java/org/openqa/selenium/WebDriver.html#getPageSource--
     */
    @Override
    public String getPageSource() {
        return driver.getPageSource();
    }

    /**
     * Method to close the current window
     *
     * @see http://selenium.googlecode.com/git/docs/api/java/org/openqa/selenium/WebDriver.html#close--
     */
    @Override
    public void close() {
        driver.close();
    }

    /**
     * Method to quit the driver, closing every associated window
     *
     * @see http://selenium.googlecode.com/git/docs/api/java/org/openqa/selenium/WebDriver.html#quit--
     */
    @Override
    public void quit() {
        driver.quit();
    }

    /**
     * Method to return all window handles contained within a given current driver
     *
     * @return Set of string window handles
     * @see http://selenium.googlecode.com/git/docs/api/java/org/openqa/selenium/WebDriver.html#getWindowHandles--
     */
    @Override
    public Set<String> getWindowHandles() {
        return driver.getWindowHandles();
    }

    /**
     * Method to return the current window handle for a given currnet driver
     *
     * @return Current window handle as a String
     * @see http://selenium.googlecode.com/git/docs/api/java/org/openqa/selenium/WebDriver.html#getWindowHandle--
     */
    @Override
    public String getWindowHandle() {
        return driver.getWindowHandle();
    }

    /**
     * Method to switch to another frame or window
     *
     * @return TargetLocator that can be used to select a frame or window
     * @see http://selenium.googlecode.com/git/docs/api/java/org/openqa/selenium/WebDriver.html#switchTo--
     */
    @Override
    public TargetLocator switchTo() {
        return driver.switchTo();
    }

    /**
     * Method to navigate to a pre-defined URL
     *
     * @return Navigation object that allows the selection of what to do next
     * @see http://selenium.googlecode.com/git/docs/api/java/org/openqa/selenium/WebDriver.html#navigate--
     */
    @Override
    public Navigation navigate() {
        return driver.navigate();
    }

    /**
     * Method to facilitate the management of the driver (e.g. timeouts, cookies, etc)
     *
     * @return Options interface
     * @see http://selenium.googlecode.com/git/docs/api/java/org/openqa/selenium/WebDriver.html#manage--
     */
    @Override
    public Options manage() {
        return driver.manage();
    }

    /**
     * Method to return a current instance of this class as a String
     *
     * @return - current instance of this class as a String
     * @see http://docs.oracle.com/javase/8/docs/api/java/lang/String.html#toString--
     */
    @Override
    public String toString() {
        return super.toString();
    }

    /**
     * Method to execute a user-defined JavaScript
     *
     * @param script
     *            script to be executed
     * @param parameters
     *            any parameters that may need to be referenced by the script
     * @return Return value types vary based on the return type of the script
     * @see http://selenium.googlecode.com/svn/trunk/docs/api/java/org/openqa/selenium/JavascriptExecutor.html
     */
    public Object executeJavaScript(String script, Object... parameters) {
        return ((JavascriptExecutor) driver).executeScript(script, parameters);
    }

    /**
     * Method to execute a user-defined JavaScript
     *
     * @param script
     *            script to be executed
     * @param parameters
     *            any parameters that may need to be referenced by the script
     * @return Return value types vary based on the return type of the script
     * @see http://selenium.googlecode.com/svn/trunk/docs/api/java/org/openqa/selenium/JavascriptExecutor.html
     */
    public Object executeAsyncJavaScript(String script, Object... parameters) {
        return ((JavascriptExecutor) driver).executeAsyncScript(script, parameters);
    }

    /**
     * Method to return the RemoteWebDriver session ID
     *
     * @return RemotWebDriver session ID as a String
     * @see https://github.com/SeleniumHQ/selenium/blob/master/java/client/src/org/openqa/selenium/remote/SessionId.java
     */
    public String getSessionId() {
        if (JavaUtilities.isValid(getRemoteWebDriver().getSessionId())) {
            return getRemoteWebDriver().getSessionId().toString();
        } else {
            return null;
        }
    }

    /**
     * Method to return the RemoteWebDriver
     *
     * @return RemotWebDriver
     * @see https://github.com/SeleniumHQ/selenium/blob/master/java/client/src/org/openqa/selenium/remote/RemoteKeyboard.java
     */
    public RemoteKeyboard getKeyboard() {
        return new RemoteKeyboard(new RemoteExecuteMethod(getRemoteWebDriver()));
    }

    /**
     * Method to set the capabilities for a driver, based on the browser type
     *
     * @param caps
     *            - Selenium DesiredCapabilities to be used to generate a WebDriver
     * @see http://selenium.googlecode.com/svn/trunk/docs/api/java/org/openqa/selenium/firefox/FirefoxDriver.html
     * @see http://selenium.googlecode.com/svn/trunk/docs/api/java/org/openqa/selenium/ie/InternetExplorerDriver.html
     * @see http://selenium.googlecode.com/svn/trunk/docs/api/java/org/openqa/selenium/chrome/ChromeDriver.html
     * @see http://selenium.googlecode.com/svn/trunk/docs/api/java/org/openqa/selenium/safari/SafariDriver.html
     * @see http://selenium.googlecode.com/svn/trunk/docs/api/java/org/openqa/selenium/htmlunit/HtmlUnitDriver.html
     * @see http://msdn.microsoft.com/en-us/library/mt188085(v=vs.85).aspx
     */
    private void setDriverWithCapabilties(DesiredCapabilities caps) {
        switch (caps.getBrowserName().toLowerCase()) {
            case "firefox":
                driver = new FirefoxDriver(caps);
                break;
            case "internet explorer":
            case "iexplore":
            case "ie":
                driver = new InternetExplorerDriver(caps);
                // driver = new SynchronizedInternetExplorerDriver(caps);
                break;
            case "chrome":
                driver = new ChromeDriver(caps);
                break;

            case "safari":
                driver = new SafariDriver(caps);
                break;
            case "htmlunit":
                /*
                 * case "html":
                 * driver = new HtmlUnitDriver(true);
                 * break;
                 */
            case "edge":
            case "microsoftedge":
                driver = new EdgeDriver(caps);
                break;
            default:
                break;
        }
    }

    /**
     * Method to return the Selenium DesiredCapabilities
     *
     * @return Selenium DesiredCapabilitie
     */
    public Capabilities getDriverCapability() {
        return new Capabilities();
    }

    /**
     * Method to take a screen shot
     *
     * @param target
     *            - image type to capture the screenshot
     * @return Object which si stored information about the screenshot
     * @see http://selenium.googlecode.com/git/docs/api/java/org/openqa/selenium/TakesScreenshot.html#getScreenshotAs-org.openqa.selenium.OutputType-
     */
    @Override
    public <X> X getScreenshotAs(OutputType<X> target) throws WebDriverException {
        return ((TakesScreenshot) driver).getScreenshotAs(target);
    }

    /**
     * Subclass to assist with interacting with a RemoteWebDriver
     *
     * @author Justin Phlegar
     * @see http://selenium.googlecode.com/svn/trunk/docs/api/java/org/openqa/selenium/remote/RemoteWebDriver.html#getCapabilities()
     * @see http://selenium.googlecode.com/svn/trunk/docs/api/java/org/openqa/selenium/Capabilities.html
     */
    public class Capabilities {

        public String browserName() {
            return getRemoteWebDriver().getCapabilities().getBrowserName();
        }

        public String browserVersion() {
            return getRemoteWebDriver().getCapabilities().getVersion();
        }

        public String platformOS() {
            return getRemoteWebDriver().getCapabilities().getPlatform().name() + " "
                    + getRemoteWebDriver().getCapabilities().getPlatform().getMajorVersion() + "."
                    + getRemoteWebDriver().getCapabilities().getPlatform().getMinorVersion();
        }

        public Platform platform() {
            return getRemoteWebDriver().getCapabilities().getPlatform();
        }

    }

    /**
     * Method that return the <b><i>PageLoaded</i></b> class
     *
     * @return <b><i>PageLoaded</i></b> class
     */
    public Page page() {
        return new Page();
    }

    public class Page {
        public boolean isAngularComplete() {
            return PageLoaded.isAngularComplete();
        }

        public boolean isDomComplete() {
            return PageLoaded.isDomComplete();
        }

        public boolean isDomComplete(int timeout) {
            return PageLoaded.isDomComplete(timeout);
        }

        public boolean isJQueryComplete() {
            return PageLoaded.isJQueryComplete();
        }

        public boolean isJQueryComplete(int timeout) {
            return PageLoaded.isJQueryComplete(timeout);
        }

        public boolean isDomInteractive() {
            return PageLoaded.isDomInteractive();
        }

        public boolean isDomInteractive(int timeout) {
            return PageLoaded.isDomInteractive(timeout);
        }
    }

    /*
     * Method that returns the instance of the DataWarehouse
     */
    public DataWarehouse data() {
        if (dataWarehouse == null) {
            dataWarehouse = new DataWarehouse();
        }
        return dataWarehouse;
    }

    /**
     * Method to return this OrasiDriver class
     *
     * @return - current OrasiDriver
     */
    private OrasiDriver getOrasiDriver() {
        return this;
    }

    /**
     * Method to return the RemoteWebDriver
     *
     * @return RemotWebDriver
     * @see https://github.com/SeleniumHQ/selenium/blob/master/java/client/src/org/openqa/selenium/remote/RemoteWebDriver.java
     */
    private RemoteWebDriver getRemoteWebDriver() {
        return ((RemoteWebDriver) driver);
    }

    public Actions actions() {
        return new Actions(driver);
    }

    public Debug debug() {
        return new Debug();
    }

    public class Debug {
        public void setReporterLogLevel(int level) {
            TestReporter.setDebugLevel(level);
        }

        public void setReporterPrintToConsole(boolean print) {
            TestReporter.setPrintToConsole(print);
        }

        public void setHighlightOnSync(boolean highlight) {
            Highlight.setDebugMode(highlight);
        }

        public void setDebugColor(Colors color) {
            Highlight.setDebugColor(color);
        }

        public void setHighlightColor(Colors color) {
            Highlight.setHighlightColor(color);
            ;
        }

        public void setErrorColor(Colors color) {
            Highlight.setErrorColor(color);
        }

        public void setSyncToFailTest(boolean syncHandler) {
            DEFAULT_SYNC_HANDLER = syncHandler;
        }

    }

}
