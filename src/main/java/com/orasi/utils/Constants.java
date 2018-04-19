package com.orasi.utils;

import java.io.File;
import java.util.logging.Level;

public class Constants {

    /*
     * File system info
     */
    public final static String DIR_SEPARATOR = File.separator;
    public final static String CURRENT_DIR = determineCurrentPath();

    /*
     * Selenium Constants
     */
    public final static String DRIVERS_PATH_LOCAL = "/drivers/";
    public final static String DRIVERS_PATH_REMOTE = "C:\\Selenium\\WebDrivers\\";
    public final static String SCREENSHOT_FOLDER = CURRENT_DIR + "selenium-reports" + DIR_SEPARATOR + "html" + DIR_SEPARATOR + "screenshots";
    public final static int DEFAULT_GLOBAL_DRIVER_TIMEOUT = 10;
    public final static int ELEMENT_TIMEOUT = 3;
    public final static int PAGE_TIMEOUT = 10;
    public final static int MILLISECONDS_TO_POLL_FOR_ELEMENT = 250;

    /*
     * Test constants
     */
    public final static String ENVIRONMENT_URL_PATH = "EnvironmentURLs";
    public final static String USER_CREDENTIALS_PATH = "UserCredentials";
    public final static String SANDBOX_PATH = "/sandbox/";
    public final static String TNSNAMES_PATH = "/database/";
    public final static String TESTNG_PARAM_RUN_LOCATION = "local";
    public final static String TESTNG_PARAM_BROWSER = "FIREFOX";
    public final static String TESTNG_PARAM_ENVIRONMENT = "Staging";

    /*
     * Default Browser Console logging level to report in TestReporter.logConsoleLogging
     */
    public final static Level DEFAULT_BROWSER_LOGGING_LEVEL = Level.SEVERE;

    /**
     * Defaults to "./" if there's an exception of any sort.
     *
     * @warning Exceptions are swallowed.
     * @return Constants.DIR_SEPARATOR
     */
    final private static String determineCurrentPath() {
        try {
            return (new File(".").getCanonicalPath()) + Constants.DIR_SEPARATOR;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "." + Constants.DIR_SEPARATOR;
    }

    /** Location of Excel sheets **/
    final static public String EXCEL_SHEETS = "/excelsheets";

    /** Location of SoapUI projects **/
    final static public String SOAPUI_PROJECTS = "/soapui";

    /** Location of XML files **/
    final static public String XML_FILES = "/xml";
}
