package com.soapui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.xmlbeans.XmlException;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.eviware.soapui.impl.wsdl.WsdlProject;
import com.eviware.soapui.model.support.PropertiesMap;
import com.eviware.soapui.model.testsuite.TestCase;
import com.eviware.soapui.model.testsuite.TestRunner;
import com.eviware.soapui.model.testsuite.TestSuite;
import com.eviware.soapui.support.SoapUIException;
import com.orasi.api.soapServices.exceptions.SoapException;
import com.orasi.utils.Sleeper;
import com.orasi.utils.TestReporter;

public class SoapUITestNGReports {

	WsdlProject project = null;

	@DataProvider(name = "testlist")
	public String[][] testlist() {
		List<TestSuite> suiteList = new ArrayList<TestSuite>();
		List<TestCase> caseList = new ArrayList<TestCase>();

		// Create a new WSDL project
		try {
			project = new WsdlProject("src/main/resources/soapui/Demo2-soapui-project.xml");
		} catch (XmlException | IOException | SoapUIException e) {
			throw new SoapException("Failed to start SoapUI project", e);
		}
		// get a list of all test suites on the project
		suiteList = project.getTestSuiteList();

		// you can use for each loop
		for (int i = 0; i < suiteList.size(); i++) {
			// get a list of all test cases on the "i"-test suite
			caseList.addAll(suiteList.get(i).getTestCaseList());
		}

		String[][] dataprovider = new String[caseList.size()][2];
		for (int i = 0; i < suiteList.size(); i++) {
			for (int k = 0; k < caseList.size(); k++) {
				dataprovider[k][0] = caseList.get(k).getParent().getName();
				dataprovider[k][1] = caseList.get(k).getName();
			}
		}
		return dataprovider;
	}

	@Test(dataProvider = "testlist")
	public void testSoapProject_ByTest(String suiteName, String testName)
			throws XmlException, IOException, SoapUIException {

		Sleeper.sleep(250);

		TestRunner runner = project.getTestSuiteByName(suiteName).getTestCaseByName(testName).run(new PropertiesMap(),
				false);
		TestReporter.assertTrue(runner.getStatus().toString().equals("FINISHED"), "Test was [ " + runner.getStatus()
				+ " ] and expected [ FINISHED ]. Error reason [ " + runner.getReason() + " ]");

	}
}
