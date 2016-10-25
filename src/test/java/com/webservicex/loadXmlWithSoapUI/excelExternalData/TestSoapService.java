package com.webservicex.loadXmlWithSoapUI.excelExternalData;

import org.testng.annotations.Test;

import com.orasi.utils.Randomness;
import com.orasi.utils.TestReporter;
import com.webservicex.measurementConverter.frequency.operations.ChangeFrequencyUnit;
import com.webservicex.measurementConverter.temperature.operations.ChangeTemperatureUnit;


public class TestSoapService {

    @Test
    public void frequencyConvertTest_SpecificResult() {
	ChangeFrequencyUnit change = new ChangeFrequencyUnit("FreqConvertRadPMinToFres");
	change.setFreqValue("17");
	change.sendRequest();
	TestReporter.logAPI(change.getResult().equals("45093900355.000404"), "Result was not [ 45093900355.000404 ]", change);
    }
    
    @Test
    public void frequencyConvertTest_RandomResult() {
	ChangeFrequencyUnit change = new ChangeFrequencyUnit("FreqConvertRadPMinToFres");
	change.setFreqValue(Randomness.randomNumber(2));
	change.sendRequest();
	TestReporter.logAPI(change.getResult() != "", "Result not empty", change);
    }
    
    @Test
    public void temperatureConvertCtoF_SpecificResult() {
	ChangeTemperatureUnit change = new ChangeTemperatureUnit("TempConvertCToF");
	change.setTemperature("32");
	change.sendRequest();
	TestReporter.logAPI(change.getResult().equals("89.6"), "Result not [ 89.6 ]", change);
    }

    @Test
    public void temperatureConvertCtoF_RandomResult() {
	ChangeTemperatureUnit change = new ChangeTemperatureUnit("TempConvertCToF");
	change.setTemperature(Randomness.randomNumber(2));
	change.sendRequest();
	TestReporter.logAPI(change.getResult() != "", "Result not empty", change);
    }
    

    @Test
    public void temperatureConvertFotC_SpecificResult() {
	ChangeTemperatureUnit change = new ChangeTemperatureUnit("TempConvertFromFToC");
	change.setTemperature("32");
	change.sendRequest();
	TestReporter.logAPI(change.getResult().equals("0"), "Result not [ 0 ]", change);
    }

    @Test
    public void temperatureConvertFotC_RandomResult() {
	ChangeTemperatureUnit change = new ChangeTemperatureUnit("TempConvertFromFToC");
	change.setTemperature(Randomness.randomNumber(2));
	change.sendRequest();
	TestReporter.logAPI(change.getResult() != "", "Result not empty", change);
    }
}
