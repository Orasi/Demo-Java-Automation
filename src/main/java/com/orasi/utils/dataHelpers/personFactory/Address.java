package com.orasi.utils.dataHelpers.personFactory;

import com.orasi.utils.Randomness;
import com.orasi.utils.dataHelpers.StateMapper;
import com.orasi.utils.dataHelpers.personFactory.seeds.Streets;
import com.orasi.utils.dataHelpers.personFactory.seeds.USLocations;

public class Address {
    private String locatorId = "0";

    public Address() {
        String location = USLocations.getLocation();
        this.type = "Home";
        this.country = "United States";
        this.countryAbbv = "USA";
        // this.zipCode = String.valueOf(Randomness.randomNumberBetween(10000, 99999));
        this.zipCode = location.split(",")[0];
        this.streetNumber = String.valueOf(Randomness.randomNumberBetween(100, 9999));
        this.streetName = Streets.getStreet();
        this.city = location.split(",")[1];
        this.state = StateMapper.getStateName(location.split(",")[2]);
        this.stateAbbv = location.split(",")[2];
        this.optIn = false;
    }

    private boolean isPrimary = false;
    private String type = null;
    private String country = "";
    private String countryAbbv = "";
    private String zipCode = "";
    private String address1 = "";
    private String address2 = "";
    private String city = "";
    private String state = "";
    private String stateAbbv = "";
    private boolean optIn;
    private String streetNumber = "";
    private String streetName = "";

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountryAbbv() {
        return countryAbbv;
    }

    public void setCountryAbbv(String countryAbbv) {
        this.countryAbbv = countryAbbv;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
        this.address1 = this.streetNumber + " " + this.streetName;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
        this.address1 = this.streetNumber + " " + this.streetName;
    }

    public String getAddress1() {
        if (address1 == "") {
            return this.streetNumber + " " + this.streetName;
        }
        return this.address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
        this.streetNumber = address1.split(" ")[0];
        this.streetName = address1.replace(this.streetNumber, "").trim();
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStateAbbv() {
        return stateAbbv;
    }

    public void setStateAbbv(String stateAbbv) {
        this.stateAbbv = stateAbbv;
    }

    public boolean isOptIn() {
        return optIn;
    }

    public void setOptIn(boolean optIn) {
        this.optIn = optIn;
    }
}
