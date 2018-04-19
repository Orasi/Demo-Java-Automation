package com.orasi.web.driverManager;

import java.net.URL;

import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariDriverService;
import org.openqa.selenium.safari.SafariOptions;

import com.orasi.DriverManager;
import com.orasi.DriverType;
import com.orasi.web.WebDriverConstants;
import com.orasi.web.WebException;

public class SafariDriverManager extends DriverManager {

    private SafariOptions options = null;

    public SafariDriverManager() {
        options = new SafariOptions();
    }

    public SafariDriverManager(SafariOptions options) {
        this.options = options;
    }

    @Override
    public void startService() {
        if (null == driverService.get()) {
            try {
                driverService.set(new SafariDriverService.Builder()
                        .usingAnyFreePort()
                        .usingTechnologyPreview(WebDriverConstants.SAFARI_USE_TECHNOLOGY_PREVIEW)
                        .build());
                driverService.get().start();
            } catch (Exception e) {
                throw new WebException("Failed to start Safari driver service", e);
            }
        }
    }

    @Override
    public void createDriver() {
        driver = new SafariDriver((SafariDriverService) driverService.get(), options);
    }

    @Override
    public void createDriver(URL url) {
        driver = new RemoteWebDriver(url, options);
    }

    @Override
    public DriverType getDriverType() {
        return DriverType.SAFARI;
    }
}