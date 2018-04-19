package com.orasi;

import java.net.URL;

import org.apache.commons.lang3.SystemUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.remote.service.DriverService;

import com.orasi.utils.Constants;
import com.orasi.utils.io.FileLoader;
import com.orasi.web.OrasiDriver;

public abstract class DriverManager {

    protected WebDriver driver = null;
    protected static ThreadLocal<DriverService> driverService = new ThreadLocal<>();
    private static ThreadLocal<OrasiDriver> orasiDriver = new ThreadLocal<>();

    protected abstract void startService();

    protected abstract void createDriver();

    protected abstract void createDriver(URL url);

    protected abstract DriverType getDriverType();

    public static void quitDriver() {
        if (null != orasiDriver && null != orasiDriver.get()) {
            orasiDriver.get().quit();
            orasiDriver.remove();
        }
    }

    public static void stopService() {
        if (null != driverService.get() && driverService.get().isRunning()) {
            driverService.get().stop();
            driverService.remove();
            System.gc();
        }
    }

    public static void setDriver(OrasiDriver driver) {
        orasiDriver.set(driver);
    }

    public void initalizeDriver() {
        try {
            startService();
            createDriver();
            createOrasiDriver();
        } catch (WebDriverException e) {
            // If driver or session fails to create, then it is possible for service to remain open.
            // Explicitly close driver service here to prevent orphaned processes
            stopService();
        }
    }

    public void initalizeDriver(URL url) {
        createDriver(url);
        createOrasiDriver();
    }

    public static OrasiDriver getDriver() {
        if (null == orasiDriver || null == orasiDriver.get()) {
            throw new AutomationException("Driver is null");
        }
        return orasiDriver.get();
    }

    protected String getDriverLocation(String filename) {
        String fileType = "";
        String osFolder = "";

        if (SystemUtils.IS_OS_WINDOWS) {
            osFolder = "windows/";
            fileType = ".exe";
        } else if (SystemUtils.IS_OS_LINUX) {
            osFolder = "linux/";
        } else if (SystemUtils.IS_OS_MAC) {
            osFolder = "mac/";
        }

        String filePath = Constants.CURRENT_DIR + Constants.DRIVERS_PATH_LOCAL + osFolder + filename + fileType;

        return FileLoader.getAbsolutePathForResource(filePath);
    }

    private void createOrasiDriver() {
        OrasiDriver driver = new OrasiDriver();
        driver.setDriver(this.driver);
        driver.setElementTimeout(Constants.ELEMENT_TIMEOUT);
        driver.setPageTimeout(Constants.PAGE_TIMEOUT);
        driver.setScriptTimeout(Constants.DEFAULT_GLOBAL_DRIVER_TIMEOUT);
        driver.setDriverType(getDriverType());
        orasiDriver.set(driver);
    }

}