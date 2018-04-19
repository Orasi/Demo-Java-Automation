package com.orasi.web.driverManager;

import java.io.File;
import java.net.URL;

import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeDriverService;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.orasi.DriverManager;
import com.orasi.DriverType;
import com.orasi.web.WebDriverConstants;
import com.orasi.web.WebException;

public class EdgeDriverManager extends DriverManager {

    private EdgeOptions options = null;

    public EdgeDriverManager() {
        options = new EdgeOptions();
        options.setPageLoadStrategy(WebDriverConstants.DEFAULT_EDGE_PAGE_LOAD_STRATEGY.toString());
    }

    public EdgeDriverManager(EdgeOptions options) {
        this.options = options;
    }

    @Override
    public void startService() {
        if (null == driverService.get()) {
            try {
                driverService.set(new EdgeDriverService.Builder()
                        .usingDriverExecutable(new File(getDriverLocation(WebDriverConstants.DRIVER_EXE_NAME_EDGE)))
                        .usingAnyFreePort()
                        .build());
                driverService.get().start();
            } catch (Exception e) {
                throw new WebException("Failed to start Edge driver service", e);
            }
        }
    }

    @Override
    public void createDriver() {
        driver = new EdgeDriver((EdgeDriverService) driverService.get(), options);
    }

    @Override
    public void createDriver(URL url) {
        driver = new RemoteWebDriver(url, options);
    }

    @Override
    public DriverType getDriverType() {
        return DriverType.EDGE;
    }
}