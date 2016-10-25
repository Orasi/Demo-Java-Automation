package com.webservicex.loadXmlWithSoapUI.noExternalData;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.orasi.utils.Randomness;
import com.orasi.utils.TestReporter;
import com.webservicex.locationSearch.usZip.operations.GetInfoByAreaCode;
import com.webservicex.locationSearch.usZip.operations.GetInfoByCity;
import com.webservicex.locationSearch.usZip.operations.GetInfoByState;
import com.webservicex.locationSearch.usZip.operations.GetInfoByZip;
import com.webservicex.measurementConverter.frequency.operations.ChangeFrequencyUnit;
import com.webservicex.measurementConverter.temperature.operations.ChangeTemperatureUnit;


public class TestSoapService {
    @BeforeClass
    public void setup(){
	//TestReporter.setDebugLevel(TestReporter.TRACE);
    }
    
    @Test
    public void getInfoByAreaCode(){
	GetInfoByAreaCode getInfo = new GetInfoByAreaCode();
	getInfo.setAreaCode("901");
	getInfo.sendRequest();
	TestReporter.logAPI(getInfo.getNumberOfResults() != 0, "Results were returned", getInfo);
    }

    @Test
    public void getInfoByCity(){
	GetInfoByCity getInfo = new GetInfoByCity();
	getInfo.setCity("Greensboro");
	getInfo.sendRequest();
	TestReporter.logAPI(getInfo.getNumberOfResults() != 0, "Results were returned", getInfo);
    }

    @Test
    public void getInfoByState(){
	GetInfoByState getInfo = new GetInfoByState();
	getInfo.setState("NC");
	getInfo.sendRequest();
	TestReporter.logAPI(getInfo.getNumberOfResults() != 0, "Results were returned", getInfo);
    }
    
    @Test
    public void getInfoByZip(){
	GetInfoByZip getInfo = new GetInfoByZip();
	getInfo.setZip("27410");
	getInfo.sendRequest();
	TestReporter.logAPI(getInfo.getNumberOfResults() != 0, "Results were returned", getInfo);
    }
}
