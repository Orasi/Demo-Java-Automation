package com.orasi.web.driverManager;

import java.io.File;
import java.net.URL;

import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.GeckoDriverService;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.orasi.DriverManager;
import com.orasi.DriverType;
import com.orasi.web.WebDriverConstants;
import com.orasi.web.WebException;

public class FirefoxDriverManager extends DriverManager {

    private FirefoxOptions options = null;

    public FirefoxDriverManager() {
        FirefoxProfile profile = new FirefoxProfile();
        profile.setAcceptUntrustedCertificates(true);
        profile.setAssumeUntrustedCertificateIssuer(true);

        options = new FirefoxOptions().setProfile(profile);
        options.setAcceptInsecureCerts(true);
        options.setPageLoadStrategy(WebDriverConstants.DEFAULT_FIREFOX_PAGE_LOAD_STRATEGY);
    }

    public FirefoxDriverManager(FirefoxOptions options) {
        this.options = options;
    }

    @Override
    public void startService() {
        if (null == driverService.get()) {
            try {
                driverService.set(new GeckoDriverService.Builder()
                        .usingDriverExecutable(new File(getDriverLocation(WebDriverConstants.DRIVER_EXE_NAME_FIREFOX)))
                        .usingAnyFreePort()
                        .build());
                driverService.get().start();
            } catch (Exception e) {
                throw new WebException("Failed to start Gecko/Firefox driver service", e);
            }
        }
    }

    @Override
    public void createDriver() {
        driver = new FirefoxDriver((GeckoDriverService) driverService.get(), options);
    }

    @Override
    public void createDriver(URL url) {
        driver = new RemoteWebDriver(url, options);
    }

    @Override
    public DriverType getDriverType() {
        return DriverType.FIREFOX;
    }
}