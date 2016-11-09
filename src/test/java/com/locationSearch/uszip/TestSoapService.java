package com.locationSearch.uszip;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.orasi.utils.Randomness;
import com.orasi.utils.TestEnvironment;
import com.orasi.utils.TestReporter;
import com.webservicex.locationSearch.usZip.operations.GetInfoByAreaCode;
import com.webservicex.locationSearch.usZip.operations.GetInfoByCity;
import com.webservicex.locationSearch.usZip.operations.GetInfoByState;
import com.webservicex.locationSearch.usZip.operations.GetInfoByZip;
import com.webservicex.measurementConverter.frequency.operations.ChangeFrequencyUnit;
import com.webservicex.measurementConverter.temperature.operations.ChangeTemperatureUnit;


public class TestSoapService extends TestEnvironment{
    @BeforeClass
    public void setup(){
	TestReporter.setDebugLevel(TestReporter.TRACE);
    }
    
    //@Test
    public void getInfoByAreaCode(){
	GetInfoByAreaCode getInfo = new GetInfoByAreaCode();
	setSoapService(getInfo);
	getInfo.setAreaCode("901");
	getInfo.sendRequest();
	TestReporter.logAPI(getInfo.getNumberOfResults() != 0, "Results were returned", getInfo);
    }

    //@Test
    public void getInfoByCity(){
	GetInfoByCity getInfo = new GetInfoByCity();
	setSoapService(getInfo);
	getInfo.setCity("Greensboro");
	getInfo.sendRequest();
	TestReporter.logAPI(getInfo.getNumberOfResults() != 0, "Results were returned", getInfo);
    }

    //@Test
    public void getInfoByState(){
	GetInfoByState getInfo = new GetInfoByState();
	setSoapService(getInfo);
	getInfo.setState("NC");
	getInfo.sendRequest();
	TestReporter.logAPI(getInfo.getNumberOfResults() != 0, "Results were returned", getInfo);
    }
    
    //@Test
    public void getInfoByZip(){
	GetInfoByZip getInfo = new GetInfoByZip();
	setSoapService(getInfo);
	getInfo.setZip("27410");
	getInfo.sendRequest();
	TestReporter.logAPI(getInfo.getNumberOfResults() != 0, "Results were returned", getInfo);
    }
	

    //@Test
    public void frequencyConvertTest_SpecificResult() {
	ChangeFrequencyUnit change = new ChangeFrequencyUnit("FreqConvertRadPMinToFres");
	change.setFreqValue("17");
	change.sendRequest();
	TestReporter.logAPI(change.getResult().equals("45093900355.000404"), "Results were returned", change);
    }
    
    //@Test
    public void frequencyConvertTest_RandomResult() {
	ChangeFrequencyUnit change = new ChangeFrequencyUnit("FreqConvertRadPMinToFres");
	change.setFreqValue(Randomness.randomNumber(2));
	change.sendRequest();
	TestReporter.logAPI(change.getResult() != "", "Result not empty", change);
    }
    
    //@Test
    public void temperatureConvertTest_SpecificResult() {
	ChangeTemperatureUnit change = new ChangeTemperatureUnit("TempConvertCToF");
	change.setTemperature("32");
	change.sendRequest();
	TestReporter.logAPI(change.getResult().equals("89.6"), "Result not empty", change);
    }

    @Test
    public void temperatureConvertTest_RandomResult() {
	ChangeTemperatureUnit change = new ChangeTemperatureUnit("TempConvertCToF");
	change.setTemperature(Randomness.randomNumber(2));
	change.sendRequest();
	TestReporter.logAPI(change.getResult() != "", "Result not empty", change);
    }
}
