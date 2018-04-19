package com.orasi;

public enum DriverType {
    CHROME("chrome", "ch"),
    FIREFOX("firefox", "ff"),
    INTERNETEXPLORER("internetexplorer", "ie", "iexplorer"),
    SAFARI("safari"),
    EDGE("edge", "microsoftedge"),
    ANDROID("android"),
    IOS("ios"),
    APPIUM("appium"),
    HTML("html");

    private final String[] driverType;

    DriverType(String... type) {
        driverType = type;
    }

    public String[] getDriverType() {
        return driverType;
    }

    public static DriverType fromString(String type) {
        for (DriverType driverType : values()) {
            if (driverType.toString().equalsIgnoreCase(type)) {
                return driverType;
            }
        }

        for (DriverType browser : DriverType.values()) {
            for (String driverType : browser.getDriverType()) {
                if (driverType.equalsIgnoreCase(type)) {
                    return browser;
                }
            }
        }

        throw new AutomationException("No DriverType defined found for requested value [ " + type + " ]");
    }
}