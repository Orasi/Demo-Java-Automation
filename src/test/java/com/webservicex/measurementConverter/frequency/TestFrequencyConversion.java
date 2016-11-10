package com.webservicex.measurementConverter.frequency;

import org.testng.annotations.Test;

import com.orasi.utils.Randomness;
import com.orasi.utils.TestEnvironment;
import com.orasi.utils.TestReporter;
import com.webservicex.measurementConverter.frequency.operations.ChangeFrequencyUnit;
import com.webservicex.measurementConverter.temperature.operations.ChangeTemperatureUnit;


public class TestFrequencyConversion extends TestEnvironment{

    @Test
    public void frequencyConvertTest_SpecificResult() {
	ChangeFrequencyUnit change = new ChangeFrequencyUnit("FreqConvertRadPMinToFres");
	setSoapService(change);
	change.setFreqValue("17");
	change.sendRequest();
	TestReporter.logAPI(change.getResult().equals("45093900355.000404"), "Result is [ 45093900355.000404 ]", change);
    }
    
    @Test
    public void frequencyConvertTest_RandomResult() {
	ChangeFrequencyUnit change = new ChangeFrequencyUnit("FreqConvertRadPMinToFres");
	setSoapService(change);
	change.setFreqValue(Randomness.randomNumber(2));
	change.sendRequest();
	TestReporter.logAPI(change.getResult() != "", "Result not empty", change);
    }
   }
