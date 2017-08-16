package com.webservicex.measurementConverter.temperature;

import org.testng.annotations.Test;

import com.orasi.utils.Randomness;
import com.orasi.utils.Sleeper;
import com.orasi.utils.TestEnvironment;
import com.orasi.utils.TestReporter;
import com.webservicex.measurementConverter.temperature.operations.ChangeTemperatureUnit;

public class TestTemperatureConversion extends TestEnvironment {

	@Test
	public void temperatureConvertCtoF_SpecificResult() {
		ChangeTemperatureUnit change = new ChangeTemperatureUnit("TempConvertCToF");
		setSoapService(change);
		change.setTemperature("32");
		int tries = 0;
		int maxTries = 10;
		boolean success = false;
		do {
			Sleeper.sleep(1000);
			try {
				change.sendRequest();
				if (change.getResponseStatusCode().equals("200")) {
					success = true;
				}
			} catch (Exception e) {

			}
			tries++;
		} while (tries < maxTries && !success);
		TestReporter.logAPI(change.getResult().equals("89.6"), "Result is [ 89.6 ]", change);
	}

	@Test
	public void temperatureConvertCtoF_RandomResult() {
		ChangeTemperatureUnit change = new ChangeTemperatureUnit("TempConvertCToF");
		setSoapService(change);
		change.setTemperature(Randomness.randomNumber(2));
		int tries = 0;
		int maxTries = 10;
		boolean success = false;
		do {
			Sleeper.sleep(1000);
			try {
				change.sendRequest();
				if (change.getResponseStatusCode().equals("200")) {
					success = true;
				}
			} catch (Exception e) {

			}
			tries++;
		} while (tries < maxTries && !success);
		TestReporter.logAPI(change.getResult() != "", "Result not empty", change);
	}

	@Test
	public void temperatureConvertFotC_SpecificResult() {
		ChangeTemperatureUnit change = new ChangeTemperatureUnit("TempConvertFromFToC");
		setSoapService(change);
		change.setTemperature("32");
		int tries = 0;
		int maxTries = 10;
		boolean success = false;
		do {
			Sleeper.sleep(1000);
			try {
				change.sendRequest();
				if (change.getResponseStatusCode().equals("200")) {
					success = true;
				}
			} catch (Exception e) {

			}
			tries++;
		} while (tries < maxTries && !success);
		TestReporter.logAPI(change.getResult().equals("0"), "Result is [ 0 ]", change);
	}

	@Test
	public void temperatureConvertFotC_RandomResult() {
		ChangeTemperatureUnit change = new ChangeTemperatureUnit("TempConvertFromFToC");
		setSoapService(change);
		change.setTemperature(Randomness.randomNumber(2));
		int tries = 0;
		int maxTries = 10;
		boolean success = false;
		do {
			Sleeper.sleep(1000);
			try {
				change.sendRequest();
				if (change.getResponseStatusCode().equals("200")) {
					success = true;
				}
			} catch (Exception e) {

			}
			tries++;
		} while (tries < maxTries && !success);
		TestReporter.logAPI(change.getResult() != "", "Result not empty", change);
	}
}
