package com.orasi.utils;

import java.io.File;
import java.io.IOException;
import java.lang.ref.PhantomReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;

import com.orasi.core.interfaces.impl.internal.ElementFactory;
import com.saucelabs.common.SauceOnDemandAuthentication;

/**
 * 
 * @author Justin Phlegar & Waightstill W Avery
 * @summary This class is designed to be extended by page classes and
 *          implemented by test classes. It houses test environment data and
 *          associated getters and setters, setup for both local and remote
 *          WebDrivers as well as page class methods used to sync page behavior.
 *          The need for this arose due to the parallel behavior that indicated
 *          that WebDriver instances were crossing threads and testing on the
 *          wrong os/browser configurations
 * @date April 5, 2015
 *
 */
public class TestEnvironment {
    /*
     * Test Environment Fields
     */
    protected String applicationUnderTest = "";
    protected String browserUnderTest = "";
    protected String browserVersion = "";
    protected String operatingSystem = "";
    protected String runLocation = "";
    protected String environment = "";
    protected String testName = "";
    /*
     * WebDriver Fields
     */
    protected WebDriver driver; /*
				 * Define a collection of webdrivers and test
				 * names inside a Map. This allows for more than
				 * one driver to be used within a test class.
				 * This also allows for a particular driver to
				 * be tied to a specific test based on test
				 * name.
				 */
    protected Map<String, WebDriver> drivers = new HashMap<String, WebDriver>();
    /*
     * URL and Credential Repository Field
     */
    protected ResourceBundle appURLRepository = ResourceBundle
	    .getBundle(Constants.ENVIRONMENT_URL_PATH);
    /*
     * Selenium Hub Field
     */
    protected String seleniumHubURL = "http://10.238.242.50:4444/wd/hub";
    
/*    protected String sauceLabsURL = "http://" + Base64Coder.decodeString(appURLRepository.getString("SAUCELABS_USERNAME"))
    + ":"+ Base64Coder.decodeString(appURLRepository.getString("SAUCELABS_KEY")) + "@ondemand.saucelabs.com:80/wd/hub";*/
    
    /*
     * Sauce Labs Fields
     */
    /**
     * Constructs a {@link com.saucelabs.common.SauceOnDemandAuthentication}
     * instance using the supplied user name/access key. To use the
     * authentication supplied by environment variables or from an external
     * file, use the no-arg
     * {@link com.saucelabs.common.SauceOnDemandAuthentication} constructor.
     */
    protected SauceOnDemandAuthentication authentication = new SauceOnDemandAuthentication(
	    Base64Coder.decodeString(appURLRepository
		    .getString("SAUCELABS_USERNAME")),
	    Base64Coder.decodeString(appURLRepository
		    .getString("SAUCELABS_KEY")));
    
    protected String sauceLabsURL = "http://" + authentication.getUsername() + ":" + authentication.getAccessKey() + "@ondemand.saucelabs.com:80/wd/hub";
	
    /**
     * ThreadLocal variable which contains the {@link WebDriver} instance which
     * is used to perform browser interactions with.
     */
    protected ThreadLocal<WebDriver> webDriver = new ThreadLocal<WebDriver>();
    /**
     * ThreadLocal variable which contains the Sauce Job Id.
     */
    protected ThreadLocal<String> sessionId = new ThreadLocal<String>();

    /*
     * Constructors for TestEnvironment class
     */
    public TestEnvironment() {
    };

    public TestEnvironment(String application, String browserUnderTest,
	    String browserVersion, String operatingSystem,
	    String setRunLocation, String environment) {
	setApplicationUnderTest(application);
	setBrowserUnderTest(browserUnderTest);
	setBrowserVersion(browserVersion);
	setOperatingSystem(operatingSystem);
	setRunLocation(setRunLocation);
	setTestEnvironment(environment);
	setSeleniumHubURL(seleniumHubURL);
    }

    public TestEnvironment(TestEnvironment te) {
	setApplicationUnderTest(te.getApplicationUnderTest());
	setBrowserUnderTest(te.getBrowserUnderTest());
	setBrowserVersion(te.getBrowserVersion());
	setOperatingSystem(te.getOperatingSystem());
	setRunLocation(te.getRunLocation());
	setTestEnvironment(te.getTestEnvironment());
	setTestName(te.getTestName());
	setSeleniumHubURL(seleniumHubURL);
	setDriver(te.getDriver());
    }

    /*
     * Getter and setter for application under test
     */
    protected void setApplicationUnderTest(String aut) {
	applicationUnderTest = aut;
    }

    public String getApplicationUnderTest() {
	return applicationUnderTest;
    }

    /*
     * Getter and setter for browser under test
     */
    protected void setBrowserUnderTest(String but) {
	if (but.equalsIgnoreCase("jenkinsParameter")) {
	    browserUnderTest = System.getProperty("jenkinsBrowser").trim();
	} else {
	    browserUnderTest = but;
	}
    }

    public String getBrowserUnderTest() {
	return browserUnderTest;
    }

    /*
     * Getter and setter for browser version
     */
    protected void setBrowserVersion(String bv) {
	if (bv.equalsIgnoreCase("jenkinsParameter")) {
	    if (System.getProperty("jenkinsBrowserVersion") == null
		    || System.getProperty("jenkinsBrowserVersion") == "null") {
		this.browserVersion = "";
	    } else {
		this.browserVersion = System.getProperty(
			"jenkinsBrowserVersion").trim();
	    }
	} else {
	    this.browserVersion = bv;
	}
    }

    public String getBrowserVersion() {
	return browserVersion;
    }

    /*
     * Getter and setter for operating system
     */
    protected void setOperatingSystem(String os) {
	if (os.equalsIgnoreCase("jenkinsParameter")) {
	    this.operatingSystem = System.getProperty("jenkinsOperatingSystem")
		    .trim();
	} else {
	    this.operatingSystem = os;
	}
    }

    public String getOperatingSystem() {
	return operatingSystem;
    }

    /*
     * Getter and setter for run location
     */
    protected void setRunLocation(String rl) {
	this.runLocation = rl;
    }

    public String getRunLocation() {
	return runLocation;
    }

    /*
     * Getter and setter for environment
     */
    protected void setTestEnvironment(String env) {
	this.environment = env;
    }

    public String getTestEnvironment() {
	return environment;
    }

    /*
     * Getter and setter for test name
     */
    protected void setTestName(String tn) {
	this.testName = tn;
    }

    public String getTestName() {
	return testName;
    }

    /*
     * Getter and setter for default test timeout
     */
    public void setDefaultTestTimeout(int timeout) {
	Constants.DEFAULT_GLOBAL_DRIVER_TIMEOUT = timeout;
	driver.manage()
	.timeouts()
	.setScriptTimeout(timeout, TimeUnit.SECONDS)
	.implicitlyWait(timeout, TimeUnit.SECONDS);
    }

    public static int getDefaultTestTimeout() {
	return Constants.DEFAULT_GLOBAL_DRIVER_TIMEOUT;
    }

    /*
     * Getter and setter for the Selenium Hub URL
     */
    public String getRemoteURL() {
	if(getRunLocation().equalsIgnoreCase("sauce") | getRunLocation().equalsIgnoreCase("remote")) return sauceLabsURL;
	else if (getRunLocation().equalsIgnoreCase("grid")) return seleniumHubURL;
	else return "";
    }

    protected void setSeleniumHubURL(String url) {
	this.seleniumHubURL = url;
    }

    // ************************************
    // ************************************
    // ************************************
    // WEBDRIVER SETUP
    // ************************************
    // ************************************
    // ************************************

    /*
     * Getter and setter for the actual WebDriver
     */
    private void setDriver(WebDriver driverSession) {
	driver = driverSession;
    }

    public WebDriver getDriver() {
	return driver;
    }

    /*
     * Method to retrive the URL and Credential Repository
     */
    protected ResourceBundle getEnvironmentURLRepository() {
	return appURLRepository;
    }

    /**
     * Launches the application under test. Gets the URL from an environment
     * properties file based on the application.
     * 
     * @param None
     * @version 12/16/2014
     * @author Justin Phlegar
     * @return Nothing
     */
   // @Step("Launch \"{0}\"")
    protected void launchApplication(String URL) {
	String reportInfo = getBrowserUnderTest().toUpperCase() ;
	if (!getBrowserVersion().isEmpty()) reportInfo += " v." + getBrowserVersion();
	TestReporter.log("Launch " + reportInfo + " in OS " + getOperatingSystem().toUpperCase() + " with URL: " + URL);
	driver.get(URL);
    }

    private void launchApplication() {
 	if (this.getTestEnvironment().isEmpty()) {
 	   launchApplication(appURLRepository.getString(getApplicationUnderTest()
 		    .toUpperCase()));
 	} else {
 	   launchApplication(appURLRepository.getString(getApplicationUnderTest()
 		    .toUpperCase() + "_" + getTestEnvironment().toUpperCase()));
 	}
     }
    

    /**
     * @return the {@link WebDriver} for the current thread
     */
    public WebDriver getWebDriver() {
	// System.out.println("WebDriver" + webDriver.get());
	return webDriver.get();
    }

    /**
     * Initializes the webdriver, sets up the run location, driver type,
     * launches the application.
     * @param testName - Name of the test
     * @version 12/16/2014
     * @author Jessica Marshall
     */
    protected void testStart(String testName)  {
	// Uncomment the following line to have TestReporter outputs output to
	// the console
	TestReporter.setPrintToConsole(true);
	driverSetup();
	launchApplication();
	drivers.put(testName, driver);
	setDriver(drivers.get(testName));
    }

    protected void endTest(String testName){
	WebDriver driver = drivers.get(testName);
	if (driver != null && driver.getWindowHandles().size() > 0) {	    
		driver.quit();
	}
    }
    
    /**
     * Sets up the driver type, location, browser under test, os
     * 
     * @param None
     * @version 12/16/2014
     * @author Justin Phlegar
     * @return Nothing
     * @throws IOException
     * @throws InterruptedException
     */
    private void driverSetup()  {
	driver = null;

	// If the location is local, grab the drivers for each browser type from
	// within the project
	if (getRunLocation().equalsIgnoreCase("local")) {
	    DesiredCapabilities caps = null;
	    File file = null;

	    switch (getOperatingSystem().toLowerCase().trim().replace(" ", "")) {
	    case "windows":
		if (getBrowserUnderTest().equalsIgnoreCase("Firefox")
			|| getBrowserUnderTest().equalsIgnoreCase("FF")) {
		    driver = new FirefoxDriver();
		}
		// Internet explorer
		else if (getBrowserUnderTest().equalsIgnoreCase("IE")
			|| getBrowserUnderTest().replace(" ", "")
				.equalsIgnoreCase("internetexplorer")) {
		    caps = DesiredCapabilities.internetExplorer();
		    caps.setCapability("ignoreZoomSetting", true);
		    caps.setCapability("enablePersistentHover", false);
		    file = new File(this
			    .getClass()
			    .getResource(
				    Constants.DRIVERS_PATH_LOCAL
					    + "IEDriverServer.exe").getPath());
		    System.setProperty("webdriver.ie.driver",
			    file.getAbsolutePath());
		    driver = new InternetExplorerDriver(caps);
		}
		// Chrome
		else if (getBrowserUnderTest().equalsIgnoreCase("Chrome")) {
		    file = new File(this
			    .getClass()
			    .getResource(
				    Constants.DRIVERS_PATH_LOCAL
					    + "ChromeDriver.exe").getPath());
		    System.setProperty("webdriver.chrome.driver",
			    file.getAbsolutePath());
		    driver = new ChromeDriver();
		}
		// Headless - HTML unit driver
		else if (getBrowserUnderTest().equalsIgnoreCase("html")) {
		    driver = new HtmlUnitDriver(true);
		}
		// Headless - HTML unit driver
		else if (getBrowserUnderTest().equalsIgnoreCase("phantom")) {
		    caps = DesiredCapabilities.phantomjs();
		    caps.setCapability("ignoreZoomSetting", true);
		    caps.setCapability("enablePersistentHover", false);
		    file = new File(this.getClass().getResource(Constants.DRIVERS_PATH_LOCAL+ "phantomjs.exe").getPath());
		    caps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY , file.getAbsolutePath());
		    driver = new PhantomJSDriver(caps);
		}
		// Safari
		else if (getBrowserUnderTest().equalsIgnoreCase("safari")) {
		    driver = new SafariDriver();
		} else {
		    throw new RuntimeException(
			    "Parameter not set for browser type");
		}
		break;
	    case "mac":
	    case "macos":
		if (getBrowserUnderTest().equalsIgnoreCase("Firefox")
			|| getBrowserUnderTest().equalsIgnoreCase("FF")) {
		    driver = new FirefoxDriver();
		}
		// Internet explorer
		else if (getBrowserUnderTest().equalsIgnoreCase("IE")
			|| getBrowserUnderTest().replace(" ", "")
				.equalsIgnoreCase("internetexplorer")) {
		    throw new RuntimeException(
			    "Currently there is no support for IE with Mac OS.");
		}
		// Chrome
		else if (getBrowserUnderTest().equalsIgnoreCase("Chrome")) {
		    file = new File(this
			    .getClass()
			    .getResource(
				    Constants.DRIVERS_PATH_LOCAL
					    + "mac/chromedriver").getPath());
		    System.setProperty("webdriver.chrome.driver",
			    file.getAbsolutePath());
		    try {
			// Ensure the permission on the driver include
			// executable permissions
			Process proc = Runtime
				.getRuntime()
				.exec(new String[] { "/bin/bash", "-c",
					"chmod 777 " + file.getAbsolutePath() });
			proc.waitFor();
			driver = new ChromeDriver();
		    } catch (IllegalStateException ise) {
			ise.printStackTrace();
			throw new IllegalStateException(
				"This has been seen to occur when the chromedriver file does not have executable permissions. In a terminal, navigate to the directory to which Maven pulls the drivers at runtime (e.g \"/target/classes/drivers/\") and execute the following command: chmod +rx chromedriver");
		    } catch (IOException ioe) {
			ioe.printStackTrace();
		    } catch (InterruptedException ie) {
			ie.printStackTrace();
		    }
		}
		// Headless - HTML unit driver
		else if (getBrowserUnderTest().equalsIgnoreCase("html")) {
		    driver = new HtmlUnitDriver(true);
		}
		// Safari
		else if (getBrowserUnderTest().equalsIgnoreCase("safari")) {
		    driver = new SafariDriver();
		} else {
		    throw new RuntimeException(
			    "Parameter not set for browser type");
		}
		break;
	    default:
		break;
	    }

	    // Code for running on the selenium grid
	} else if ( getRunLocation().equalsIgnoreCase("grid")) {
	    DesiredCapabilities capabilities = new DesiredCapabilities();
	    capabilities.setCapability(CapabilityType.BROWSER_NAME,
		    getBrowserUnderTest());
	    if (getBrowserVersion() != null) {
		capabilities.setCapability(CapabilityType.VERSION,
			getBrowserVersion());
	    }
	    capabilities.setCapability(CapabilityType.PLATFORM,
		    getGridPlatformByOS(getOperatingSystem()));
	    if (getBrowserUnderTest().toLowerCase().contains("ie")
		    || getBrowserUnderTest().toLowerCase().contains("iexplore")) {
		capabilities.setCapability("ignoreZoomSetting", true);
	    }
	    
	    try {
		driver = new RemoteWebDriver(new URL(getRemoteURL()), capabilities);
	    } catch (MalformedURLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	    
	} else if (getRunLocation().equalsIgnoreCase("remote") | getRunLocation().equalsIgnoreCase("sauce")) {
	    DesiredCapabilities capabilities = new DesiredCapabilities();
	    capabilities.setCapability(CapabilityType.BROWSER_NAME,
		    getBrowserUnderTest());
	    if (getBrowserVersion() != null) {
		capabilities.setCapability(CapabilityType.VERSION,
			getBrowserVersion());
	    }
	    capabilities.setCapability(CapabilityType.PLATFORM,
		    getOperatingSystem());
	    if (getBrowserUnderTest().toLowerCase().contains("ie")
		    || getBrowserUnderTest().toLowerCase().contains("iexplore")) {
		capabilities.setCapability("ignoreZoomSetting", true);
	    }
	    capabilities.setCapability("name", getTestName());
	    try {
		webDriver.set(new RemoteWebDriver(new URL(getRemoteURL()), capabilities));
	    } catch (MalformedURLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	    sessionId.set(((RemoteWebDriver) getWebDriver()).getSessionId()
		    .toString());
	    driver = webDriver.get();
	}else {
	    throw new RuntimeException(
		    "Parameter for run [Location] was not set to 'Local', 'Grid', 'Sauce', or 'Remote'");
	}

	driver.manage()
		.timeouts()
		.setScriptTimeout(Constants.DEFAULT_GLOBAL_DRIVER_TIMEOUT,
			TimeUnit.SECONDS)
		.implicitlyWait(Constants.DEFAULT_GLOBAL_DRIVER_TIMEOUT,
			TimeUnit.SECONDS);
	//setDefaultTestTimeout(Constants.DEFAULT_GLOBAL_DRIVER_TIMEOUT);
	driver.manage().deleteAllCookies();
	driver.manage().window().maximize();
    }

    // ************************************
    // ************************************
    // ************************************
    // PAGE OBJECT METHODS
    // ************************************
    // ************************************
    // ************************************

    public PageLoaded pageLoaded() {
 	return new PageLoaded(this);
     }
    
    /**
     * @summary loops for a predetermined amount of time (defined by
     *          WebDriverSetup.getDefaultTestTimeout()) to determine if the DOM
     *          is in a ready-state
     * @return boolean: true is the DOM is completely loaded, false otherwise
     * @param N
     *            /A
     */
  
    public void initializePage(Class<?> clazz) {
	try {
	    ElementFactory.initElements(driver,clazz.getConstructor(TestEnvironment.class));
	} catch (SecurityException | NoSuchMethodException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }
    
    private Platform getGridPlatformByOS(String os){
	switch(os.toLowerCase()){
	    case "android": return Platform.ANDROID;
	    case "windows": return Platform.WINDOWS;
	    case "win8": return Platform.WIN8;
	    case "win8.1": return Platform.WIN8_1;
	    case "xp": return Platform.XP;
	    case "linux": return Platform.LINUX;
	    case "mac": return Platform.MAC;
	/*    case "mavericks": return Platform.MAVERICKS;
	    case "mountain_lion": return Platform.MOUNTAIN_LION;
	    case "snow_leopard": return Platform.SNOW_LEOPARD;
	    case "yosemite": return Platform.YOSEMITE;*/
	    default: return Platform.ANY;
	}
    }
}
