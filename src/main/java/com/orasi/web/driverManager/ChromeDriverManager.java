package com.orasi.web.driverManager;

import java.io.File;
import java.net.URL;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.orasi.DriverManager;
import com.orasi.DriverType;
import com.orasi.web.WebDriverConstants;
import com.orasi.web.WebException;

public class ChromeDriverManager extends DriverManager {

    private ChromeOptions options = null;

    public ChromeDriverManager() {
        options = new ChromeOptions();
        options.setPageLoadStrategy(WebDriverConstants.DEFAULT_CHROME_PAGE_LOAD_STRATEGY);
    }

    public ChromeDriverManager(ChromeOptions options) {
        this.options = options;
    }

    @Override
    public void startService() {
        if (null == driverService.get()) {
            try {
                driverService.set(new ChromeDriverService.Builder()
                        .usingDriverExecutable(new File(getDriverLocation(WebDriverConstants.DRIVER_EXE_NAME_CHROME)))
                        .usingAnyFreePort()
                        .build());
                driverService.get().start();
            } catch (Exception e) {
                throw new WebException("Failed to start Chrome driver service", e);
            }
        }
    }

    @Override
    public void createDriver() {
        driver = new ChromeDriver((ChromeDriverService) driverService.get(), options);
    }

    @Override
    public void createDriver(URL url) {
        driver = new RemoteWebDriver(url, options);
    }

    @Override
    public DriverType getDriverType() {
        return DriverType.CHROME;
    }
}