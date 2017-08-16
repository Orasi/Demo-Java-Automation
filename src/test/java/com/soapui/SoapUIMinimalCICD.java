package com.soapui;

import java.io.IOException;

import org.apache.xmlbeans.XmlException;
import org.testng.annotations.Test;

import com.eviware.soapui.impl.wsdl.WsdlProject;
import com.eviware.soapui.model.support.PropertiesMap;
import com.eviware.soapui.model.testsuite.TestRunner;
import com.eviware.soapui.support.SoapUIException;
import com.orasi.utils.TestReporter;

public class SoapUIMinimalCICD {

	@Test
	public void testSoapProject_AllAtOnce() throws XmlException, IOException, SoapUIException {
		// Create a new WSDL project
		WsdlProject project = new WsdlProject("src/main/resources/soapui/Demo2-soapui-project.xml");
		TestRunner runner = project.run(new PropertiesMap(), false);
		TestReporter.assertTrue(runner.getStatus().toString().equals("FINISHED"),
				"Test did not finish successfully but was [ " + runner.getStatus() + " ]. Error reason [ "
						+ runner.getReason() + " ]");

	}

}
