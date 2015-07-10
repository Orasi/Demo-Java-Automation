package com.orasi.utils;

import java.io.File;
import java.util.Calendar;


public class Constants {

	final static public int CURRENT_YEAR = Calendar.getInstance().get(Calendar.YEAR);
    final static public int CURRENT_MONTH = Calendar.getInstance().get(Calendar.MONTH);
    final static public int CURRENT_DAY = Calendar.getInstance().get(Calendar.DATE);
	
    /** Location of the environment URLs properties file */
    final static public String ENVIRONMENT_URL_PATH = "EnvironmentURLs";
   		
    
    /** Location of the user credentials properties file */
    final static public String USER_CREDENTIALS_PATH = "UserCredentials";
  
    
    /** Location of data provider files in the project  */
    final static public String BLUESOURCE_DATAPROVIDER_PATH = "/bluesource/dataProviders/";
    
    /** Location of drivers in project */
    final static public String DRIVERS_PATH_LOCAL = "/drivers/";
    final static public String DRIVERS_PATH_REMOTE = "C:\\Selenium\\WebDrivers\\";
    
    /** Location of tnsnames in project */
    final static public String TNSNAMES_PATH = "/database/";
    		
    /** An alias for File.separator */
    final static public String DIR_SEPARATOR = File.separator;
    
    /** The current path of the project */ 
    final static public String CURRENT_DIR = determineCurrentPath();
		
    /** The global system property line.separator */
    final static public String LINE_SEPARATOR = System.getProperty("line.separator", "\n");
    
    /** An alias for the global system property line.separator */
    final static public String NEW_LINE = LINE_SEPARATOR;
    
    /** The default timeout in seconds, should be a generous default time */
    static public int DEFAULT_GLOBAL_DRIVER_TIMEOUT = 60;
    
    /** The timeout (seconds) for finding web elements on a page, shouldn't be too long */
    final static public int ELEMENT_TIMEOUT = 10;
    
    /** The timeout (seconds) for page/DOM/transitions, should also be a generous */
    final static public int PAGE_TIMEOUT = 60;
    
    /** Selenium hub URL */
    final static public String SELENIUM_HUB_URL = "http://dnhprpptst01.disid.disney.com:4444/wd/hub";
    
    /** System properties */
    public static final String BROWSER_PROPERTY = "selenium.browser";
    public static final String TEST_DRIVER_TIMEOUT = "selenium.test_driver_timeout";
    
	/**
     * Defaults to "./" if there's an exception of any sort.
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



}

