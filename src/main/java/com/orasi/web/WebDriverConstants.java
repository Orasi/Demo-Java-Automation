package com.orasi.web;

import static com.orasi.utils.Constants.CURRENT_DIR;
import static com.orasi.utils.Constants.DIR_SEPARATOR;

import org.openqa.selenium.PageLoadStrategy;

public class WebDriverConstants {
    /*
     * Driver Constants
     */
    public final static String DRIVERS_PATH_LOCAL = "/drivers/";
    public final static String DRIVERS_PATH_REMOTE = "C:\\Selenium\\WebDrivers\\";

    public final static String DRIVER_EXE_NAME_CHROME = "chromedriver";
    public final static String DRIVER_EXE_NAME_EDGE = "MicrosofWebDriver";
    public final static String DRIVER_EXE_NAME_FIREFOX = "geckodriver";
    public final static String DRIVER_EXE_NAME_INTERNET_EXPLORER = "IEDriverServer";
    public final static String DRIVER_EXE_NAME_SAFARI = "IEDriverServer";

    public final static String SCREENSHOT_FOLDER = CURRENT_DIR + "selenium-reports" + DIR_SEPARATOR + "html" + DIR_SEPARATOR + "screenshots";
    public final static long DEFAULT_GLOBAL_DRIVER_TIMEOUT = 10000;
    public final static long ELEMENT_TIMEOUT = 3000;
    public final static long PAGE_TIMEOUT = 10000;
    public final static int MILLISECONDS_TO_POLL_FOR_ELEMENT = 250;

    public final static PageLoadStrategy DEFAULT_CHROME_PAGE_LOAD_STRATEGY = PageLoadStrategy.NONE;
    public final static PageLoadStrategy DEFAULT_EDGE_PAGE_LOAD_STRATEGY = PageLoadStrategy.NONE;
    public final static PageLoadStrategy DEFAULT_FIREFOX_PAGE_LOAD_STRATEGY = PageLoadStrategy.NONE;
    public final static PageLoadStrategy DEFAULT_INTERNET_EXPLORER_PAGE_LOAD_STRATEGY = PageLoadStrategy.NONE;
    public final static PageLoadStrategy DEFAULT_SAFARI_PAGE_LOAD_STRATEGY = PageLoadStrategy.NONE;

    public static boolean SAFARI_USE_TECHNOLOGY_PREVIEW = true;

}
