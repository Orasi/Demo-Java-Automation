package com.orasi.utils;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.logging.LogEntries;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.TestNGException;

import com.orasi.AutomationException;
import com.orasi.DriverManager;
import com.orasi.DriverType;
import com.orasi.api.restServices.RestResponse;
import com.orasi.api.restServices.exceptions.RestException;
import com.orasi.api.soapServices.SoapService;
import com.orasi.api.soapServices.exceptions.SoapException;
import com.orasi.utils.date.SimpleDate;
import com.orasi.web.OrasiDriver;

public class TestReporter {
    private static boolean printToConsole = true;
    private static boolean printClassPath = true;
    private static ThreadLocal<Boolean> assertFailed = new ThreadLocal<Boolean>();
    /**
     * No additional info printed to console
     */
    public static final int NONE = 0;

    /**
     * Will print some useful information to console such as URL's, parameters, and RQ/RS
     */
    public static final int INFO = 1;

    /**
     * Will print helpful debugging logs to console
     */
    public static final int DEBUG = 2;
    /**
     * Will print some low-level granular steps to console from framework
     */
    public static final int TRACE = 3;

    private static ThreadLocal<Integer> debugLevel = new ThreadLocal<>();

    /**
     *
     * @param level
     *            - Options below <br/>
     *            TestReporter.NONE : (Default) - No additional info printed to console <br/>
     *            TestReporter.INFO : Will print useful information to console such as URL's, parameters, and RQ/RS<br/>
     *            TestReporter.DEBUG : Will print debugging information to console <br/>
     *            TestReporter.TRACE: Will print low level information to console from the framework<br/>
     */
    public static void setDebugLevel(int level) {
        debugLevel.set(level);
    }

    public static int getDebugLevel() {
        return debugLevel.get() == null ? 0 : debugLevel.get();
    }

    private static String getTimestamp() {
        String date = SimpleDate.getTimestamp().toString().substring(11);
        date = (date + "00").substring(0, 12);
        return date + " :: ";
    }

    private static String trimHtml(String log) {
        return log.replaceAll("<[^>]*>", "");
    }

    private static String getClassPath() {
        String path = " > ";
        if (getPrintFullClassPath()) {
            StackTraceElement[] elements = Thread.currentThread().getStackTrace();
            int x = 0;
            String filename = "";
            for (StackTraceElement element : elements) {
                filename = element.getClassName();
                if (x == 0 || x == 1 || x == 2) {
                    x++;
                    continue;
                } else if (!filename.matches("sun.reflect|com.orasi.utils.TestReporter|com.orasi.utils.TestReporter|com.orasi.utils.PageLoaded|java.lang.reflect|"
                        + "java.lang.Thread|com.sun.proxy|org.testng.internal|java.util.concurrent.ThreadPoolExecutor|com.orasi.utils.debugging")) {
                    path = element.getClassName() + "#" + element.getMethodName();
                    break;
                }

            }
        }
        return path + " > ";
    }

    public static void setPrintToConsole(boolean printToConsole) {
        TestReporter.printToConsole = printToConsole;
    }

    public static boolean getPrintToConsole() {
        return printToConsole;
    }

    public static void setPrintFullClassPath(boolean printClassPath) {
        TestReporter.printClassPath = printClassPath;
    }

    public static boolean getPrintFullClassPath() {
        return printClassPath;
    }

    public synchronized static void logStep(String step) {
        Reporter.log("<br/><b><font size = 4>Step: " + step
                + "</font></b><br/>");
        if (getPrintToConsole()) {
            System.out.println(step);
        }
    }

    public synchronized static void logScenario(String scenario) {
        Reporter.log("<br/><b><font size = 4>Data Scenario: " + scenario
                + "</font></b><br/>");
        if (getPrintToConsole()) {
            System.out.println(getTimestamp() + trimHtml(scenario));
        }
    }

    public synchronized static void interfaceLog(String message) {
        logInfo(message);
    }

    public synchronized static void interfaceLog(String message, boolean failed) {
        logInfo("<font size = 2 color=\"red\">" + message + "</font>");
    }

    public synchronized static void log(String message) {
        Reporter.log(getTimestamp() + " <i><b>" + getClassPath() + message + "</b></i><br />");
        if (getPrintToConsole()) {
            System.out.println(getTimestamp() + getClassPath() + trimHtml(message));
        }
    }

    public synchronized static void logFailure(String message) {
        Reporter.log(getTimestamp() + " <font size = 2 color=\"red\"><b><u> ERROR :: " + getClassPath() + message + "</font></u></b><br />");
        if (getPrintToConsole()) {
            System.out.println(getTimestamp() + trimHtml("ERROR :: " + getClassPath() + trimHtml(message)));
        }
    }

    /**
     * Use to output low-level granular steps
     *
     * @param message
     */
    public synchronized static void logTrace(String message) {
        if (getDebugLevel() >= TRACE) {
            Reporter.log(getTimestamp() + "TRACE :: " + getClassPath() + message + "<br />");
            System.out.println(getTimestamp() + "TRACE :: " + getClassPath() + (trimHtml(message).trim()));
        }
    }

    /**
     * Use to output useful information such as URL's, parameters, and RQ/RS
     *
     * @param message
     */
    public synchronized static void logInfo(String message) {
        if (getDebugLevel() >= INFO) {
            Reporter.log(getTimestamp() + " INFO :: " + getClassPath() + message + "<br />");
            System.out.println(getTimestamp() + " INFO :: " + getClassPath() + trimHtml(message).trim());
        }
    }

    /**
     * Use to output useful information such as URL's, parameters, and RQ/RS
     *
     * @param message
     */
    public synchronized static void logDebug(String message) {
        if (getDebugLevel() >= DEBUG) {
            Reporter.log(getTimestamp() + "DEBUG :: " + getClassPath() + message + "<br />");
            System.out.println(getTimestamp() + "DEBUG :: " + getClassPath() + trimHtml(message).trim());
        }
    }

    public synchronized static void logNoHtmlTrim(String message) {
        Reporter.log(getTimestamp() + " :: " + getClassPath() + message + "<br />");
        if (getPrintToConsole()) {
            System.out.println(getTimestamp() + getClassPath() + message.trim());
        }
    }

    public synchronized static void logNoXmlTrim(String message) {
        Reporter.setEscapeHtml(true);
        Reporter.log("");
        Reporter.log(message);
        Reporter.setEscapeHtml(false);
        Reporter.log("<br /");
        if (getPrintToConsole()) {
            System.out.println(getTimestamp() + getClassPath() + message.trim());
        }
    }

    public synchronized static void assertTrue(boolean condition, String description) {
        try {
            Assert.assertTrue(condition, description);
        } catch (AssertionError failure) {
            logFailure("Assert True - " + description);
            if (getPrintToConsole()) {
                System.out.println(getTimestamp() + "Assert True - " + trimHtml(description));
            }
            Assert.fail(description);
        }
        Reporter.log(getTimestamp() + " <font size = 2 color=\"green\"><b><u>Assert True - " + description + "</font></u></b><br />");
        if (getPrintToConsole()) {
            System.out.println(getTimestamp() + "Assert True - " + trimHtml(description));
        }
    }

    public synchronized static void assertFalse(boolean condition, String description) {
        try {
            Assert.assertFalse(condition, description);
        } catch (AssertionError failure) {
            logFailure("Assert False - " + description);
            if (getPrintToConsole()) {
                System.out.println(getTimestamp() + "Assert False - " + trimHtml(description));
            }
            Assert.fail(description);
        }
        Reporter.log(getTimestamp() + " <font size = 2 color=\"green\"><b><u>Assert False - " + description + "</font></u></b><br />");
        if (getPrintToConsole()) {
            System.out.println(getTimestamp() + "Assert False - " + trimHtml(description));
        }
    }

    public synchronized static void assertEquals(Object value1, Object value2, String description) {
        try {
            Assert.assertEquals(value1, value2, description);
        } catch (AssertionError failure) {
            logFailure("Assert Equals - " + description);
            if (getPrintToConsole()) {
                System.out.println(getTimestamp() + "Assert Equals - " + trimHtml(description));
            }
            Assert.fail(description);
        }
        Reporter.log(getTimestamp() + " <font size = 2 color=\"green\"><b><u>Assert Equals - " + description + "</font></u></b><br />");
        if (getPrintToConsole()) {
            System.out.println(getTimestamp() + "Assert Equals - " + trimHtml(description));
        }
    }

    public synchronized static void assertNotEquals(Object value1, Object value2, String description) {
        try {
            Assert.assertNotEquals(value1, value2, description);
        } catch (AssertionError failure) {
            logFailure("Assert Not Equals - " + description);
            if (getPrintToConsole()) {
                System.out.println(getTimestamp() + "Assert Not Equals - " + trimHtml(description));
            }
            Assert.fail(description);
        }
        Reporter.log(getTimestamp() + " <font size = 2 color=\"green\"><b><u>Assert Not Equals - " + description + "</font></u></b><br />");
        if (getPrintToConsole()) {
            System.out.println(getTimestamp() + "Assert Not Equals - " + trimHtml(description));
        }
    }

    public synchronized static void assertGreaterThanZero(int value) {
        try {
            Assert.assertTrue(value > 0);
        } catch (AssertionError failure) {
            logFailure("Assert Greater Than Zero - " + value);
            if (getPrintToConsole()) {
                System.out.println(getTimestamp() + "Assert Greater Than Zero - Assert " + value + " is greater than zero");
            }
            Assert.fail("Assert " + value + " is greater than zero");
        }
        Reporter.log(getTimestamp() + " <font size = 2 color=\"green\"><b><u>Assert Greater Than Zero - Assert " + value + " is greater than zero</font></u></b><br />");
        if (getPrintToConsole()) {
            System.out.println(getTimestamp() + "Assert Greater Than Zero - Assert " + value + " is greater than zero");
        }
    }

    public synchronized static void assertGreaterThanZero(float value) {
        assertGreaterThanZero((int) value);
    }

    public synchronized static void assertGreaterThanZero(double value) {
        assertGreaterThanZero((int) value);
    }

    public synchronized static void assertNull(Object condition, String description) {
        try {
            Assert.assertNull(condition, description);
        } catch (AssertionError failure) {
            logFailure("Assert Null - " + description);
            if (getPrintToConsole()) {
                System.out.println(getTimestamp() + "Assert Null - " + trimHtml(description));
            }
            Assert.fail(description);
        }
        Reporter.log(getTimestamp() + " <font size = 2 color=\"green\"><b><u>Assert Null - " + description + "</font></u></b><br />");
        if (getPrintToConsole()) {
            System.out.println(getTimestamp() + "Assert Null - " + trimHtml(description));
        }
    }

    public synchronized static void assertNotNull(Object condition, String description) {
        try {
            Assert.assertNotNull(condition, description);
        } catch (AssertionError failure) {
            logFailure("Assert Not Null - " + description);
            if (getPrintToConsole()) {
                System.out.println(getTimestamp() + "Assert Not Null - " + trimHtml(description));
            }
            Assert.fail(description);
        }
        Reporter.log(getTimestamp() + "<font size = 2 color=\"green\"><b><u>Assert Not Null - " + description + "</font></u></b><br />");
        if (getPrintToConsole()) {
            System.out.println(getTimestamp() + "Assert Not Null - " + trimHtml(description));
        }
    }

    public synchronized static boolean softAssertTrue(boolean condition, String description) {
        try {
            Assert.assertTrue(condition, description);
            Reporter.log(getTimestamp() + " <font size = 2 color=\"green\"><b><u>Assert True - " + description
                    + "</font></u></b><br />");
            if (getPrintToConsole()) {
                System.out.println(getTimestamp() + "Assert True - " + trimHtml(description));
            }
        } catch (AssertionError failure) {
            Reporter.log(getTimestamp() + "<font size = 2 color=\"red\"><b><u>Assert True - " + description + "</b></u></font><br />");
            if (getPrintToConsole()) {
                System.out.println(getTimestamp() + "Assert True - " + trimHtml(description));
            }
            assertFailed.set(true);
            return false;
        }
        return true;
    }

    public synchronized static boolean softAssertEquals(Object value1, Object value2, String description) {

        try {
            Assert.assertEquals(value1, value2, description);
            Reporter.log(getTimestamp() + " <font size = 2 color=\"green\"><b><u>Assert Equals - " + description
                    + "</font></u></b><br />");
            if (getPrintToConsole()) {
                System.out.println(getTimestamp() + "Assert Equals - " + trimHtml(description));
            }
        } catch (AssertionError failure) {
            Reporter.log(getTimestamp() + "<font size = 2 color=\"red\"><b><u>Assert Equals - " + description + "</b></u></font><br />");
            if (getPrintToConsole()) {
                System.out.println(getTimestamp() + "Assert Equals - " + trimHtml(description));
            }
            assertFailed.set(true);
            return false;
        }
        return true;

    }

    public synchronized static boolean softAssertFalse(boolean condition, String description) {
        try {
            Assert.assertFalse(condition, description);
            Reporter.log(getTimestamp() + " <font size = 2 color=\"green\"><b><u>Assert False - " + description
                    + "</font></u></b><br />");
            if (getPrintToConsole()) {
                System.out.println(getTimestamp() + "Assert False - " + trimHtml(description));
            }
        } catch (AssertionError failure) {
            Reporter.log(getTimestamp() + "<font size = 2 color=\"red\"><b><u>Assert False - " + description + "</b></u></font><br />");
            if (getPrintToConsole()) {
                System.out.println(getTimestamp() + "Assert False - " + trimHtml(description));
            }
            assertFailed.set(true);
            return false;
        }
        return true;
    }

    public synchronized static boolean softAssertNull(Object condition, String description) {
        try {
            Assert.assertNull(condition, description);
        } catch (AssertionError failure) {
            Reporter.log(getTimestamp() + "<font size = 2 color=\"red\"><b>Assert Null - " + description + "</font></u></b><br />");
            if (getPrintToConsole()) {
                System.out.println(getTimestamp() + "Assert Null - " + trimHtml(description));
            }
            assertFailed.set(true);
            return false;
        }
        Reporter.log(getTimestamp() + " <font size = 2 color=\"green\"><b><u>Assert Null - " + description
                + "</font></u></b><br />");
        if (getPrintToConsole()) {
            System.out.println(getTimestamp() + "Assert Null - " + trimHtml(description));
        }

        return true;
    }

    public synchronized static boolean softAssertNotNull(Object condition, String description) {
        try {
            Assert.assertNotNull(condition, description);
        } catch (AssertionError failure) {
            Reporter.log(getTimestamp() + "<font size = 2 color=\"red\"><b>Assert Not Null - " + description + "</font></u></b><br />");
            if (getPrintToConsole()) {
                System.out.println(getTimestamp() + "Assert Not Null - " + trimHtml(description));
            }
            assertFailed.set(true);
            return false;
        }
        Reporter.log(getTimestamp() + "<font size = 2 color=\"green\"><b><u>Assert Not Null - " + description
                + "</font></u></b><br />");
        if (getPrintToConsole()) {
            System.out.println(getTimestamp() + "Assert Not Null - " + trimHtml(description));
        }

        return true;
    }

    public synchronized static void assertAll() {
        boolean failed = assertFailed.get() == null ? false : assertFailed.get();
        if (failed) {
            assertFailed.set(false);
            Reporter.log(getTimestamp() + "<font size = 2 color=\"red\"><b>Soft assertions failed - see failures above</font></u></b><br />");
            Assert.fail("Soft assertions failed - see testNG report for details");
        }
    }

    public synchronized static void logScreenshot(WebDriver driver, String fileName) {
        String slash = Constants.DIR_SEPARATOR;

        String destDir = Constants.SCREENSHOT_FOLDER + slash + fileName.replace(".", slash);
        DateFormat dateFormat = new SimpleDateFormat("dd_MMM_yyyy_hh_mm_ssaa");

        String destFile = destDir + slash + dateFormat.format(new Date()) + ".png";
        logScreenshot(driver, destFile, slash);
    }

    public synchronized static void logScreenshot(WebDriver driver, String fileLocation, String slash) {
        File file = new File("");

        try {
            file = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(file, new File(fileLocation));
        } catch (IOException e) {
            TestReporter.log("Screenshot was not captured - IOException: " + e.getMessage());
        } catch (TestNGException te) {
            TestReporter.log("Screenshot was not captured - TestNGException: " + te.getMessage());
        } catch (Exception e) {
            TestReporter.log("Screenshot was not captured: " + e.getMessage());
        }

        String jenkinsPath = System.getProperty("jenkinsJobUrl");
        String jenkinsName = System.getProperty("jenkinsJobName");
        String jenkinsWorkspace = System.getProperty("jenkinsHome") + slash + "workspace" + slash;

        if (isNotEmpty(jenkinsPath)) {
            TestReporter.logTrace("Jenkins URL [ " + jenkinsPath + " ]");
            TestReporter.logTrace("Job URL [ " + jenkinsName + " ]");
            TestReporter.logTrace("Jenkins workspace Path [ " + jenkinsWorkspace + " ]");

            String webFileLocation = fileLocation.replace(jenkinsWorkspace + jenkinsName, jenkinsPath + "ws/");

            TestReporter.logInfo("Web File Location : " + webFileLocation);
            Reporter.log("<a  target='_blank' href='" + webFileLocation + "'><img src='" + webFileLocation + "' height='200' width='300'/></a>");
        } else {
            TestReporter.logInfo("File Location : " + fileLocation);
            Reporter.log("<a  target='_blank' href='" + fileLocation + "'> <img src='file:///" + fileLocation + "' height='200' width='300'/> </a>");
        }
    }

    public synchronized static void logAPI(boolean pass, String message, SoapService sp) {
        String failFormat = "";
        if (!pass) {
            failFormat = "<font size = 2 color=\"red\">";
            logFailure(message);
        }
        String request = sp.getRequest().replaceAll("</*>", "</*>");
        String response = sp.getResponse();
        Reporter.log("<font size = 2><b>Endpoint: " + sp.getServiceURL() + "</b></font><br/>" + failFormat + "<b><br/> SOAP REQUEST [ " + sp.getServiceName() + "#" + sp.getOperationName() + " ] </b></font>");
        Reporter.setEscapeHtml(true);
        Reporter.log(request);
        Reporter.setEscapeHtml(false);
        Reporter.log("<br/><br/>");
        Reporter.log(failFormat + "<b> SOAP RESPONSE [ " + sp.getServiceName() + "#" + sp.getOperationName() + " ]. Execution time: [ " + sp.getExecutionTime() + " ]</b></font>");
        Reporter.setEscapeHtml(true);
        Reporter.log(response);
        Reporter.setEscapeHtml(false);
        Reporter.log("<br/>");

        if (!pass) {
            throw new SoapException(message);
        }
    }

    public synchronized static void logAPI(boolean pass, String message, RestResponse rs) {
        String failFormat = "";
        if (!pass) {
            failFormat = "<font size = 2 color=\"red\">";
            logFailure(message);
        }
        Reporter.log("<font size = 2><b>Endpoint: " + rs.getMethod() + " " + rs.getURL() + "</b><br/>" + failFormat + "<b>REST REQUEST </b></font>");
        Reporter.setEscapeHtml(true);
        Reporter.log(rs.getRequestBody().replaceAll("</*>", "</*>"));
        Reporter.setEscapeHtml(false);
        Reporter.log("<br/>");
        Reporter.log(failFormat + "<br/><b>REST RESPONSE. Execution time: [ " + rs.getExecutionTime() + " ]</b></font>");
        Reporter.setEscapeHtml(true);
        Reporter.log(rs.getResponse());
        Reporter.setEscapeHtml(false);
        Reporter.log("<br/>");

        if (!pass) {
            throw new RestException(message);
        }
    }

    /**
     * Logs any console errors with level defined in Constant to the test reporter
     * This functionality is only available in the chrome browser.
     * IE browser and Firefox do not support at this time as W3C spec is not defined
     *
     * @see <a href="https://github.com/w3c/webdriver/issues/406">W3C logging spec can be tracked via https://github.com/w3c/webdriver/issues/406</a>
     *
     * @date 07/21/2017
     */
    public static void logConsoleLogging() {
        logConsoleLogging(Constants.DEFAULT_BROWSER_LOGGING_LEVEL);
    }

    /**
     * Logs any console errors with level of SEVERE to the test reporter
     * This functionality is only available in the chrome browser.
     * IE browser and Firefox do not support at this time as W3C spec is not defined
     *
     * @see <a href="https://github.com/w3c/webdriver/issues/406">W3C logging spec can be tracked via https://github.com/w3c/webdriver/issues/406</a>
     *
     * @date 07/21/2017
     * @param level
     */
    public static void logConsoleLogging(Level level) {
        // Only capture logs for chrome browser
        OrasiDriver driver = null;
        try {
            driver = DriverManager.getDriver();
            if (DriverType.CHROME.equals(driver.getDriverType())
                    && driver.getSessionId() != null) {
                Reporter.log("<br/><b><font size = 4>Chrome Browser Console logs for level [ " + level.getName() + " ] </font></b><br/>");
                LogEntries logs = driver.manage().logs().get("browser");

                if (logs.getAll().isEmpty()) {
                    Reporter.log("NO LOGS");
                } else {
                    // Go through all the log entries, and only output the desired level errors
                    logs.forEach(log -> {
                        if (log.getLevel() == level) {
                            Reporter.log(" <font size = 2 color=\"red\"><b> Level :: " + log.getLevel().getName() + "</font></b><br />");
                            Reporter.log(" <font size = 2 color=\"red\"><b> Message :: " + log.getMessage() + "</font></b><br />");
                        }
                    });

                }
            }

        } catch (AutomationException e) {
        }
    }
}