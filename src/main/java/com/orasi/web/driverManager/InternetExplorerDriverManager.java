package com.orasi.web.driverManager;

import java.io.File;
import java.net.URL;

import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerDriverService;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.orasi.DriverManager;
import com.orasi.DriverType;
import com.orasi.web.WebDriverConstants;
import com.orasi.web.WebException;

public class InternetExplorerDriverManager extends DriverManager {

    private InternetExplorerOptions options = null;

    public InternetExplorerDriverManager() {
        options = new InternetExplorerOptions();
        options.setPageLoadStrategy(WebDriverConstants.DEFAULT_INTERNET_EXPLORER_PAGE_LOAD_STRATEGY);
        options.ignoreZoomSettings();
    }

    public InternetExplorerDriverManager(InternetExplorerOptions options) {
        this.options = options;
    }

    @Override
    public void startService() {
        if (null == driverService.get()) {
            try {
                driverService.set(new InternetExplorerDriverService.Builder()
                        .usingDriverExecutable(new File(getDriverLocation(WebDriverConstants.DRIVER_EXE_NAME_INTERNET_EXPLORER)))
                        .usingAnyFreePort()
                        .build());
                driverService.get().start();
            } catch (Exception e) {
                throw new WebException("Failed to start Internet Explorer driver service", e);
            }
        }
    }

    @Override
    public void createDriver() {
        driver = new InternetExplorerDriver((InternetExplorerDriverService) driverService.get(), options);
    }

    @Override
    public void createDriver(URL url) {
        driver = new RemoteWebDriver(url, options);
    }

    @Override
    public DriverType getDriverType() {
        return DriverType.INTERNETEXPLORER;
    }
}