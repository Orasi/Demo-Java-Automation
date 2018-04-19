package com.orasi.utils.dataHelpers.personFactory;

import com.orasi.utils.Randomness;

public class Email {
    private boolean isPrimary = false;
    private String locatorId = "0";
    private String country = "";
    private String type = "";
    private String email = "";
    private boolean optIn = false;

    public Email() {
        this.country = "USA";
        this.type = "Personal";
        this.email = Randomness.randomString(1).substring(0, 1).toUpperCase() + Randomness.randomString(11).substring(1).toLowerCase() + "@test.com";
        this.optIn = false;
    }

    public boolean isPrimary() {
        return isPrimary;
    }

    public void setPrimary(boolean isPrimary) {
        this.isPrimary = isPrimary;
    }

    public String getLocatorId() {
        return locatorId;
    }

    public void setLocatorId(String locatorId) {
        this.locatorId = locatorId;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isOptIn() {
        return optIn;
    }

    public void setOptIn(boolean optIn) {
        this.optIn = optIn;
    }
}
