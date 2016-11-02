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
import com.orasi.api.soapServices.core.exceptions.SoapException;
import com.orasi.utils.Sleeper;
import com.orasi.utils.TestReporter;

public class SoapUIBetterConsoleReports {

    @Test
    public void testSoapProject_BetterReport() throws XmlException, IOException, SoapUIException{
	String suiteName = "";
	String reportStr = "";

	// variables for getting duration
	long startTime = 0;
	long duration = 0;

	TestRunner runner = null;

	List<TestSuite> suiteList = new ArrayList<TestSuite>();
	List<TestCase> caseList = new ArrayList<TestCase>();


	// Create a new WSDL project
	WsdlProject project = new WsdlProject("src/main/resources/soapui/demo-project.xml");
	// get a list of all test suites on the project
	suiteList = project.getTestSuiteList();

	// you can use for each loop
	for(int i = 0; i < suiteList.size(); i++){

	    // get name of the "i" element in the list of a test suites
	    suiteName = suiteList.get(i).getName();
	    reportStr = reportStr + "\nTest Suite: " + suiteName;

	    // get a list of all test cases on the "i"-test suite
	    caseList = suiteList.get(i).getTestCaseList();


	    for(int k = 0; k < caseList.size(); k++){

		startTime = System.currentTimeMillis();

		// run "k"-test case in the "i"-test suite
		runner = project.getTestSuiteByName(suiteName).getTestCaseByName(caseList.get(k).getName()).run(new PropertiesMap(), false);

		duration = System.currentTimeMillis() - startTime;

		reportStr = reportStr + "\n\tTestCase: " + caseList.get(k).getName() + "\tStatus: " + runner.getStatus() + "\tReason: " + runner.getReason() + "\tDuration: " + duration;
		Sleeper.sleep(250);
	    }

	}

	// string of the results
	System.out.println(reportStr);
	TestReporter.assertTrue(runner.getStatus().toString().equals("FINISHED"), "Test did not finish successfully but was [ " + runner.getStatus() + " ]. Error reason [ " + runner.getReason() + " ]");

    }
}
