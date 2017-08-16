package com.webservicex.locationSearch.usZip;

import org.testng.annotations.Test;

import com.orasi.utils.Constants;
import com.orasi.utils.Sleeper;
import com.orasi.utils.TestEnvironment;
import com.orasi.utils.TestReporter;
import com.webservicex.locationSearch.usZip.operations.GetInfoByAreaCode;
import com.webservicex.locationSearch.usZip.operations.GetInfoByCity;
import com.webservicex.locationSearch.usZip.operations.GetInfoByState;
import com.webservicex.locationSearch.usZip.operations.GetInfoByZip;

public class TestUsZip extends TestEnvironment {

	private String excelFileLocation = Constants.EXCEL_SHEETS + "/usZip";

	@Test
	public void getInfoByAreaCode() {
		GetInfoByAreaCode getInfo = new GetInfoByAreaCode();
		getInfo.setAreaCode("901");
		int tries = 0;
		int maxTries = 10;
		boolean success = false;
		do {
			Sleeper.sleep(1000);
			try {
				getInfo.sendRequest();
				if (getInfo.getResponseStatusCode().equals("200")) {
					success = true;
				}
			} catch (Exception e) {

			}
			tries++;
		} while (tries < maxTries && !success);
		TestReporter.logAPI(getInfo.getNumberOfResults() != 0, "Results were returned", getInfo);
		TestReporter
				.assertTrue(
						getInfo.validateResponse(
								excelFileLocation + "/GetInfoByAreaCode/GetInfoByAreaCodeResponse_CSV.csv", "Main"),
						"Validate Response");
	}

	@Test
	public void getInfoByCity() {
		GetInfoByCity getInfo = new GetInfoByCity();
		getInfo.setCity("Greensboro");
		int tries = 0;
		int maxTries = 10;
		boolean success = false;
		do {
			Sleeper.sleep(1000);
			try {
				getInfo.sendRequest();
				if (getInfo.getResponseStatusCode().equals("200")) {
					success = true;
				}
			} catch (Exception e) {

			}
			tries++;
		} while (tries < maxTries && !success);
		TestReporter.logAPI(getInfo.getNumberOfResults() != 0, "Results were returned", getInfo);
		TestReporter.assertTrue(
				getInfo.validateResponse(excelFileLocation + "/GetInfoByCity/GetInfoByCityResponse_XLSX.xlsx", "Main"),
				"Validate Response");
	}

	@Test
	public void getInfoByState() {
		GetInfoByState getInfo = new GetInfoByState();
		getInfo.setState("NC");
		int tries = 0;
		int maxTries = 10;
		boolean success = false;
		do {
			Sleeper.sleep(1000);
			try {
				getInfo.sendRequest();
				if (getInfo.getResponseStatusCode().equals("200")) {
					success = true;
				}
			} catch (Exception e) {

			}
			tries++;
		} while (tries < maxTries && !success);
		TestReporter.logAPI(getInfo.getNumberOfResults() != 0, "Results were returned", getInfo);
		TestReporter.assertTrue(
				getInfo.validateResponse(excelFileLocation + "/GetInfoByState/GetInfoByStateResponse.xls", "Main"),
				"Validate Response");
	}

	@Test
	public void getInfoByZip() {
		GetInfoByZip getInfo = new GetInfoByZip();
		getInfo.setZip("27410");
		int tries = 0;
		int maxTries = 10;
		boolean success = false;
		do {
			Sleeper.sleep(1000);
			try {
				getInfo.sendRequest();
				if (getInfo.getResponseStatusCode().equals("200")) {
					success = true;
				}
			} catch (Exception e) {

			}
			tries++;
		} while (tries < maxTries && !success);
		TestReporter.logAPI(getInfo.getNumberOfResults() != 0, "Results were returned", getInfo);
		TestReporter.assertTrue(
				getInfo.validateResponse(excelFileLocation + "/GetInfoByZip/GetInfoByZipResponse.xls", "Main"),
				"Validate Response");
	}
}
