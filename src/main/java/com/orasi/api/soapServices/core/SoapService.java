package com.orasi.api.soapServices.core;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFault;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.apache.xmlbeans.XmlException;
import org.testng.Reporter;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.eviware.soapui.impl.wsdl.WsdlInterface;
import com.eviware.soapui.impl.wsdl.WsdlOperation;
import com.eviware.soapui.impl.wsdl.WsdlProject;
import com.eviware.soapui.impl.wsdl.support.wsdl.WsdlImporter;
import com.eviware.soapui.model.iface.Interface;
import com.eviware.soapui.support.SoapUIException;
import com.orasi.api.soapServices.core.exceptions.SoapException;
import com.orasi.api.soapServices.core.exceptions.XPathNotFoundException;
import com.orasi.api.soapServices.core.exceptions.XPathNullNodeValueException;
import com.orasi.exception.AutomationException;
import com.orasi.utils.Randomness;
import com.orasi.utils.Regex;
import com.orasi.utils.Sleeper;
import com.orasi.utils.TestReporter;
import com.orasi.utils.XMLTools;

import groovy.util.logging.Log4j;

public abstract class SoapService{
    private static WsdlProject project = null;
	private String strServiceName;
	private String strOperationName;
	private String url = null;
	private String intResponseStatusCode = null;
	private Document requestDocument = null;
	private Document responseDocument = null;
	protected StringBuffer buffer = new StringBuffer();
	private String soapVersion = SOAPConstants.SOAP_1_1_PROTOCOL;
	private Map<String, String> functionMap = new HashMap<String, String>();
	
	/*****************************
	 **** Start Gets and Sets ****
	 *****************************/

	/**
	 *  Takes the current Request XML Document stored in memory and
	 *          return it as a string for simple output
	 * @precondition Requires XML Document to be loaded by using
	 *               {@link #setRequestDocument}
	 * @author Justin Phlegar
	 * @version Created: 08/28/2014
	 * @return Will return the current Request XML as a string
	 */
	public String getRequest() {	
		TestReporter.logTrace("Entering SoapService#getRequest");	
		String request = XMLTools.transformXmlToString(getRequestDocument());
		TestReporter.logTrace("Exiting SoapService#getRequest");	
		return request;
	}

	/**
	 *  Takes the current Response XML Document stored in memory and
	 *          return it as a string for simple output
	 * @precondition Requires XML Document to be loaded by using
	 *               {@link #setResponseDocument}
	 * @author Justin Phlegar
	 * @version Created 08/28/2014
	 * @return Will return the current Response XML as a string
	 */
	public String getResponse() {
		TestReporter.logTrace("Entering SoapService#getResponse");	
		String response = XMLTools.transformXmlToString(getResponseDocument());		
		TestReporter.logTrace("Exiting SoapService#getResponse");	
		return response;
	}

	/**
	 *  After a service request has been sent, if the Status code of the
	 *          response has been stored, then this function can be used to
	 *          retrieve that status code
	 * @precondition The Response Status Code needs to be set by
	 *               {@link #setRepsonseStatusCode(String)}
	 * @author Justin Phlegar
	 * @version Created: 08/28/2014
	 * @return Returns the Status Code of a response as a String
	 */
	public String getResponseStatusCode() {
		return intResponseStatusCode;
	}

	/**
	 *  Return the URL of the service under test
	 * @precondition The Service URL needs to be set by
	 *               {@link #setServiceURL(String)}
	 * @author Justin Phlegar
	 * @version Created: 08/28/2014
	 * @return Returns the Service URL as a String
	 */
	public String getServiceURL() {
		return url;
	}
	
	/**
	 *  Return the Service Name of the service under test
	 * @author Justin Phlegar
	 * @version Created: 08/28/2014
	 * @return Returns the Service Name as a String
	 */
	public String getServiceName() {
		return strServiceName;
	}

	/**
	 *  Return the Service Operation Name of the service under test
	 * @author Justin Phlegar
	 * @version Created: 08/28/2014
	 * @return Returns the Service Operation Name as a String
	 */
	public String getOperationName() {
		return strOperationName;
	}
	/**
	 *  This is used to retrieve the current XML Document as it is in
	 *          memory.
	 * @precondition The XML Document needs to be set by
	 *               {@link #setRequestDocument(Document)}
	 * @author Justin Phlegar
	 * @version Created: 08/28/2014
	 * @return Returns the stored Request XML as a Document
	 */
	protected Document getRequestDocument() {
		return requestDocument;
	}

	/**
	 *  This is used to retrieve the current Response Document as it is
	 *          in memory
	 * @precondition The Response Document needs to be set by
	 *               {@link #setResponseDocument(Document)}
	 * @author Justin Phlegar
	 * @version Created: 08/28/2014
	 * @return Returns the stored Response XML as a Document object
	 */
	protected Document getResponseDocument() {
		return responseDocument;
	}

	/**
	 *  Used to store the XML file as a Document object in memory. Can
	 *          be retrieved using {@link #getRequestDocument()}
	 * @author Justin Phlegar
	 * @version Created: 08/28/2014
	 * @param doc
	 *            Document XML file of the Request to be stored in memory
	 */
	protected void setRequestDocument(Document doc) {
		requestDocument = doc;
	}

	/**
	 *  Set a Response XML Document to be stored in memory to be
	 *          retrieved and edited easily. Retrieve XML Document using
	 *          {@link #getResponseDocument()} or as a String using
	 *          {@link #getResponse()}
	 * @precondition Requires valid XML Document to be sent
	 * @author Justin Phlegar
	 * @version Created 08/28/2014
	 * @param doc
	 *            Document: XML file of the Response to be stored in memory
	 */
	protected void setResponseDocument(Document doc) {
		responseDocument = doc;
	}

	/**
	 *  Used to store URL of the Service Under Test in memory. Can be
	 *          retrieved using {@link #getServiceURL())}
	 * @author Justin Phlegar
	 * @version Created: 08/28/2014
	 * @param url
	 *            String: URL Endpoint of the Service Under Test
	 */
	protected void setServiceURL(String url) {
		this.url = url;
	}

	/**
	 *  Used to store Service Name Under Test in memory. Can be
	 *          retrieved using {@link #getServiceName())}
	 * @author Justin Phlegar
	 * @version Created: 08/28/2014
	 * @param url
	 *            String: Service Name of the Service Under Test
	 */
	protected void setServiceName(String name) {
		strServiceName = name;
	}

	/**
	 *  Used to store the Service Operation Name Under Test in memory. Can be
	 *          retrieved using {@link #getOperationName())}
	 * @author Justin Phlegar
	 * @version Created: 08/28/2014
	 * @param url
	 *            String: Operation Name of the Service Under Test
	 */
	protected void setOperationName(String name) {
		strOperationName = name;
	}
	
	/**
	 * Used to define Soap Version to use. Default is 1.2
	 * Can by changed using SOAPConstants.SOAP_1_1_PROTOCOL or SOAPConstants.SOAP_1_2_PROTOCOL
	 * @author Justin Phlegar
	 * @version Created: 09/12/2016
	 * @param version SOAPConstants.SOAP_1_1_PROTOCOL or SOAPConstants.SOAP_1_2_PROTOCOL         
	 */
	protected void setSoapVersion(String version){
		soapVersion = version;
	}
	
	/***************************
	 **** End Gets and Sets ****
	 ***************************/

	/*************************************
	 ********* Public Methods ************
	 *************************************
	/**
	 * Determine how many nodes exist using queried XPath in the Request
	 * @author Justin Phlegar
	 * @version Created: 09/12/2016
	 * @param xpath Valid XPath to look for
	 * @return Number of node found on XPath. If XPath is not found, return 0         
	 */
	public int getNumberOfRequestNodesByXPath(String xpath){
		TestReporter.logTrace("Entering SoapService#getNumberOfRequestNodesByXPath");
		int count = 0;
		try{
			count = XMLTools.getNodeList(getRequestDocument(), xpath).getLength();
		}catch(XPathNotFoundException e){}
		TestReporter.logTrace("Exiting SoapService#getNumberOfRequestNodesByXPath");
		return count;
	}

	/**
	 * Determine how many nodes exist using queried XPath in the Response
	 * @author Justin Phlegar
	 * @version Created: 09/12/2016
	 * @param xpath Valid XPath to look for
	 * @return Number of node found on XPath. If XPath is not found, return 0         
	 */
	public int getNumberOfResponseNodesByXPath(String xpath){
		TestReporter.logTrace("Entering SoapService#getNumberOfResponseNodesByXPath");
		int count = 0;
		try{
			count = XMLTools.getNodeList(getResponseDocument(), xpath).getLength();
		}catch(XPathNotFoundException e){}
		TestReporter.logTrace("Exiting SoapService#getNumberOfResponseNodesByXPath");
		return count;
	}
	/**
	 *  Takes an xpath and return the value if found in the request
	 * @author Justin Phlegar
	 * @version Created: 08/28/2014
	 * @param xpath
	 *            String: xpath to evaluate
	 */
	public String getRequestNodeValueByXPath(String xpath) {
		TestReporter.logTrace("Entering SoapService#getRequestNodeValueByXPath");
		String value = XMLTools.getValueByXpath(getRequestDocument(), xpath); 
		TestReporter.logTrace("Exiting SoapService#getRequestNodeValueByXPath");
		return value;
	}

	/**
	 *  Takes an xpath and return the value if found in the response
	 * @author Justin Phlegar
	 * @version Created: 08/28/2014
	 * @param xpath
	 *            String: xpath to evaluate
	 */
	public String getResponseNodeValueByXPath(String xpath) {
		TestReporter.logTrace("Entering SoapService#getResponseNodeValueByXPath");
		String value = XMLTools.getValueByXpath(getResponseDocument(), xpath); 
		TestReporter.logTrace("Exiting SoapService#getResponseNodeValueByXPath");
		return value;
	}

	/**
	 *  Find and open the excel file sent. If successful, look and find
	 *          the matching scenario name then return its xpath and value data.
	 * @author Justin Phlegar
	 * @version Created: 08/28/2014
	 * @param file
	 *            String: File location either in the project or the hard disk
	 *            path
	 * @param scenario
	 *            String: Name of the scenario to poll data for
	 */
	protected Object[][] getTestScenario(String file, String scenario) {
		TestReporter.logTrace("Entering SoapService#getTestScenario");
		String[][] tabArray = null;
		String filePath = "";
		try {
			TestReporter.logTrace("Getting file from Resources");
			filePath = getClass().getResource(file).getPath();

			filePath = filePath.replace("%20", " ");
			TestReporter.logDebug("Full file path ["+filePath+"]");
			
			TestReporter.logTrace("Opening excel workbook with file");
			Workbook workbook = Workbook.getWorkbook(new File(filePath));
			
			TestReporter.logTrace("Load default worksheet");
			Sheet sheet = workbook.getSheet(0);
			int startRow, startCol, endRow, endCol, ci, cj;
			
			TestReporter.logTrace("Searching for column containing scenario");
			Cell tableStart = sheet.findCell(scenario);
			
			TestReporter.logTrace("Scenario found");
			startRow = tableStart.getRow();
			startCol = tableStart.getColumn();

			TestReporter.logTrace("Determining last row containing data");
			
			for (int i = startRow + 1;; i++) {
				try {
					if (sheet.getCell(startCol + 1, i).getContents().toString()
							.equals("")) {
						endRow = i;
						break;
					}
				} catch (ArrayIndexOutOfBoundsException arrayError) {
					endRow = i;
					break;
				}

			}
			
			TestReporter.logDebug("Start Column ["+startCol+"]" );
			TestReporter.logDebug("End Row ["+endRow+"]" );
			endCol = startCol + 3;

			TestReporter.logTrace("Create an array based on end row");
			tabArray = new String[endRow - startRow - 1][endCol - startCol - 1];
			ci = 0;


			TestReporter.logTrace("Transfer data from excel sheet to array");
			for (int i = startRow + 1; i < endRow; i++, ci++) {
				cj = 0;
				for (int j = startCol + 1; j < endCol; j++, cj++) {
					tabArray[ci][cj] = sheet.getCell(j, i).getContents();
				}
			}
		} catch (FileNotFoundException fnfe) {
			throw new AutomationException("File not found in path ["+filePath+"]", fnfe.getCause());
		} catch (BiffException be) {
			throw new AutomationException("Unable to read file. Ensure file is [xls] format and not [xlsx]", be.getCause());
		} catch (IOException ioe) {
			throw new RuntimeException("Unable to open file " + file + "\n"
					+ ioe.getCause());
		}
		TestReporter.logTrace("Exiting SoapService#getTestScenario");
		return (tabArray);
	}

	/**
	 *  Takes the pre-built Request XML in memory and sends to the
	 *          service
	 * @author Justin Phlegar
	 * @version Created: 08/28/2014
	 */
	public void sendRequest() {
		TestReporter.logTrace("Entering SoapService#sendRequest");
		SOAPMessage request = null;
		SOAPMessage response = null;
		SOAPConnectionFactory connectionFactory = null;
		SOAPConnection connection = null;
		SOAPBody responseBody = null;
		MessageFactory messageFactory = null;

		try {
			TestReporter.logTrace("Create Soap Message Factory");
			messageFactory = MessageFactory.newInstance(soapVersion);
			TestReporter.logTrace("Message Factory started");
			
			TestReporter.logTrace("Loading request XML into byte stream");
			InputStream in = new ByteArrayInputStream(getRequest().getBytes(Charset.defaultCharset()));
			TestReporter.logTrace("Successfully loaded XML into stream");
			TestReporter.logTrace("Loading byte stream into Message Factory");
			request = messageFactory.createMessage(new MimeHeaders(),in);	
			TestReporter.logTrace("Successfully generated Soap Message");

			TestReporter.logTrace("Create Soap Connection Factory");
			connectionFactory = SOAPConnectionFactory.newInstance();

			TestReporter.logTrace("Create Soap Connection");
			connection = connectionFactory.createConnection();
			
			TestReporter.logTrace("Send request to service");
			response = connection.call(request, url);

			TestReporter.logTrace("Successfully sent Soap Message. Normalizing response");
			response.getSOAPBody().normalize();
			responseBody = response.getSOAPBody();
		} catch (UnsupportedOperationException uoe) {
			throw new SoapException("Operation given did not match any operations in the service "+ uoe.getCause());
		} catch (SOAPException soape) {
			throw new SoapException("Soap Connection failure", soape.getCause());
		} catch (IOException ioe) {
			throw new SoapException("Failed to read the request properly"
					+ ioe.getCause());
		}

		TestReporter.logTrace("Checking for faults");
		if (responseBody.hasFault()) {
			SOAPFault newFault = responseBody.getFault();
			intResponseStatusCode = newFault.getFaultCode();
		} else {
			intResponseStatusCode = "200";
		}

		try {
			TestReporter.logTrace("Closing Soap connection");
			connection.close();
		} catch (SOAPException soape) {
			throw new SoapException("Failed to close Soap Connection" , soape.getCause());
		}


		TestReporter.logTrace("Convert response to XML document and store it");
		Document doc = XMLTools.makeXMLDocument(response);
		doc.normalize();
		setResponseDocument(doc);
		
		TestReporter.logTrace("Successfully converted response to XML document");
		TestReporter.logTrace("Exiting SoapService#sendRequest");
	}	
	
	/**
	 *  Update an XPath node or attribute based on the value. The value
	 *          is not limited to simple values, but may also call various
	 *          functions by adding "fx:" as a prefix. Please see
	 *          {@link #handleValueFunction} for more information
	 * @author Justin Phlegar
	 * @version Created: 08/28/2014
	 * @param xpath
	 *            String: xpath to evaluate
	 * @param value
	 *            String: Depending on value given, will update the xpath value,
	 *            attribute, or call a separate function. 
	 *            <br><br><b>Value syntax expressions:</b>
	 *            <br><b>value="fx:funcName"</b> -- Will call the function "funcName" to be handled in {@link #handleValueFunction}  
	 * @throws XPathExpressionException
	 *             Could not match evaluate xPath
	 * @throws RuntimeException
	 *             Could not match xPath to a node, element or attribute
	 */
	protected  void setRequestNodeValueByXPath(Document doc, String xpath, String value) {
		TestReporter.logTrace("Entering SoapService#setRequestNodeValueByXPath");
		XPathFactory xPathFactory = XPathFactory.newInstance();
		XPath xPath = xPathFactory.newXPath();
		XPathExpression expr;
		NodeList nList = null;
		
		try {
			TestReporter.logTrace("Checking validity of xpath [ "+xpath+" ]");
			expr = xPath.compile(xpath);
			nList = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
		}catch (XPathExpressionException xpe) {		
			throw new RuntimeException("Xpath evaluation failed with xpath [ " + xpath + " ] ", xpe.getCause());	
		}

		TestReporter.logTrace("XPath is valid. Checking for nodes with desired xpath");
		if (nList.item(0) == null){	
			throw new XPathNotFoundException(xpath);
		}
		
		if( value == null || value.isEmpty()){
			throw new XPathNullNodeValueException(xpath);
		}		
	
		if (value.contains("fx:")) {
			TestReporter.logTrace("Executing runtime function [ " + value + " ]");
			value = handleValueFunction(doc, value, xpath);
		}
		
		//If a prior function call previous updated the XML, nothing more is needed.
		if (!value.equalsIgnoreCase("XMLUpdated")) {
			if(value.equalsIgnoreCase("true")) value = "true";
			else if(value.equalsIgnoreCase("false")) value = "false";
			TestReporter.logTrace("Setting value [ "+value+" ] to xpath");
			nList.item(0).setTextContent(value);
		}
	
		
		//Store changes
		setRequestDocument(doc);	

		TestReporter.logTrace("Exiting SoapService#setRequestNodeValueByXPath");
	}

	/**
	 *  Update multiple XPath nodes or attributes based on the value. The value
	 *          is not limited to simple values, but may also call various
	 *          functions by adding "fx:" as a prefix. Please see
	 *          {@link #handleValueFunction} for more information
	 * @author Justin Phlegar
	 * @version Created: 08/28/2014
	 * @param xpath
	 *            String: xpath to evaluate
	 * @param value
	 *            String: Depending on value given, will update the xpath value,
	 *            attribute, or call a separate function. 
	 *            <br><br><b>Value syntax expressions:</b>
	 *            <br><b>value="abc"</b>  -- Indirectly states that the node value will be set as "abc"
	 *            <br><b>value="value:abc"</b>  -- Directly states that the node value will be set as "abc"
	 *            <br><b>value="attribute:attrName,abc"</b>  -- Directly states that the node attribute "attrName" will be set as "abc"
	 *            <br><b>value="fx:funcName"</b> -- Will call the function "funcName" to be handled in {@link #handleValueFunction}
	 */
	public void setRequestNodeValueByXPath(String xpath, String value) {
			setRequestNodeValueByXPath(getRequestDocument(),xpath,value);
	}

	/**
	 *  Update multiple XPath nodes or attributes based on the value. The value
	 *          is not limited to simple values, but may also call various
	 *          functions by adding "fx:" as a prefix. Please see
	 *          {@link #handleValueFunction} for more information
	 * @author Justin Phlegar
	 * @version Created: 08/28/2014
	 * @param xpath
	 *            String: xpath to evaluate
	 * @param value
	 *            String: Depending on value given, will update the xpath value,
	 *            attribute, or call a separate function. 
	 *            <br><br><b>Value syntax expressions:</b>
	 *            <br><b>value="abc"</b>  -- Indirectly states that the node value will be set as "abc"
	 *            <br><b>value="value:abc"</b>  -- Directly states that the node value will be set as "abc"
	 *            <br><b>value="attribute:attrName,abc"</b>  -- Directly states that the node attribute "attrName" will be set as "abc"
	 *            <br><b>value="fx:funcName"</b> -- Will call the function "funcName" to be handled in {@link #handleValueFunction}
	 */
	public void setRequestNodeValueByXPath(Object[][] scenarios) {
		for (int x = 0; x < scenarios.length; x++) {
			TestReporter.logDebug("Set value [ " + scenarios[x][0].toString() + " ] to XPath [ " + scenarios[x][1].toString() + " ]");
			setRequestNodeValueByXPath(getRequestDocument(),scenarios[x][0].toString(),
					scenarios[x][1].toString());
		}
	}

	/**
	 *  Validate XML Response and reports findings
	 * @author Justin Phlegar
	 * @version Created: 08/28/2014
	 * @param doc Document: XML Document to evaluate
	 * @param xpath String: xpath to evaluate
	 * @param value String: Depending on value given, will validate the xpath node or attribute value,
	 *  		  	<br><br><b>Value syntax expressions:</b>
	 *	            <br><b>value="abc"</b>  -- Indirectly states that the node value will be validated and expected to be "abc"
	 *  	        <br><b>value="value:abc"</b>  -- Directly states that the node value will be validated and expected to be "abc"
	 *      	    <br><b>value="attribute:attrName,abc"</b>  -- Directly states that the node attribute "attrName" will be validated and expected to be "abc"
	 *            
	 */
	public boolean validateNodeValueByXPath(Document doc, Object[][] scenarios) {
		boolean status = true;
		boolean isArrayActive=false;
		int arrays = 0;
		Map<Integer, String> arrayTracker = new HashMap<Integer, String>();
		
		buffer.setLength(0);
		buffer.append("<table border='1' width='100%'>");
		buffer.append("<tr><td style='width: 100px; color: black; text-align: center;'><b>XPath</b></td>");
		buffer.append("<td style='width: 100px; color: black; text-align: center;'><b>Regex</b></td>");
		buffer.append("<td style='width: 100px; color: black; text-align: center;'><b>Value</b></td>");
		buffer.append("<td style='width: 100px; color: black; text-align: center;'><b>Status</b></td></tr>");
			for (int x = 0; x < scenarios.length; x++) {
			
				if (!validateNodeValueByXPath(doc, scenarios[x][0].toString(), scenarios[x][1].toString())) {
					status = false;
				}
		}
		buffer.append("</table><br/>");
		Reporter.log(buffer.toString());
		return status;
	}
	
	private boolean validateScenarios(Document doc, Object[][] scenarios, int startPosition){
		boolean status = true;
		boolean isArrayActive=false;
		int arrays = 0;
		Map<Integer, String> arrayTracker = new HashMap<Integer, String>();
		
		String function = "";
		for (int x = startPosition; x < scenarios.length; x++) {
			function = scenarios[x][1].toString();
			if(function.toLowerCase().contains("fx:startarray")){
				arrays++;
				String[] params = function.split(";");
				arrayTracker.put(arrays, params[1] + ":::" + getNumberOfResponseNodesByXPath(scenarios[x][0].toString()) +":::" + scenarios[x][0].toString());
				isArrayActive=true;
				//validateScenarios(doc, scenarios, x);
			}else if(isArrayActive){
				function = scenarios[x][1].toString();
				if(function.toLowerCase().contains("fx:endarray")){
					int numberNodes = Integer.valueOf(arrayTracker.get(arrays).split(":::")[1]);
					for(int index = 1 ; index <= numberNodes ; index ++){
						if (!validateNodeValueByXPath(doc, scenarios[x][0].toString(), function)) {
							status = false;
						}
					}
				}else{
					functionMap.put(arrayTracker.get(arrays).split(":::")[2] + "[{index}]" + scenarios[x][0], function);
				}
			}else{
			
				if (!validateNodeValueByXPath(doc, scenarios[x][0].toString(), function)) {
					status = false;
				}
			}
		}
		return status;
	}
	/**
	 *  Main validation function that validates and reports findings
	 * @author Justin Phlegar
	 * @version Created: 08/28/2014
	 * @param doc Document: XML Document to evalute
	 * @param xpath String: xpath to evaluate
	 * @param value String: Depending on value given, will validate the xpath node or attribute value,
	 *  		  	<br><br><b>Value syntax expressions:</b>
	 *	            <br><b>value="abc"</b>  -- Indirectly states that the node value will be validated and expected to be "abc"
	 *  	        <br><b>value="value:abc"</b>  -- Directly states that the node value will be validated and expected to be "abc"
	 *      	    <br><b>value="attribute:attrName,abc"</b>  -- Directly states that the node attribute "attrName" will be validated and expected to be "abc"
	 *            
	 */
	protected boolean validateNodeValueByXPath(Document doc, String xpath, String regexValue) {
		XPathFactory xPathFactory = XPathFactory.newInstance();
		XPath xPath = xPathFactory.newXPath();
		XPathExpression expr;
		NodeList nList = null;
		String xPathValue = "";
		String errorMessage = "";
		
		//Find the node based on xpath expression
		try {
			expr = xPath.compile(xpath);
			nList = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
		}catch (XPathExpressionException xpe) {
			errorMessage = "Failed to build xpath [ " + xpath + " ]. Please check format.";
		}
		
		//Ensure an element was found, if not then throw error and fail
		if (nList.item(0) == null && errorMessage.isEmpty()) {
			errorMessage = "No xpath was found with the path [ " + xpath + " ] ";
		}
		
		if (errorMessage.isEmpty()){
			//Handle prefix types
			if (regexValue.trim().toLowerCase().contains("value:")) {
				
				//Node value was specifically stated. Find value of node to validate based on xpath
				regexValue = regexValue.substring(regexValue.indexOf(":") + 1,
						regexValue.length()).trim();
				xPathValue = nList.item(0).getTextContent();
			} else if (regexValue.trim().toLowerCase().contains("attribute")) {
				//Node attribute was specifically stated. Find attribute of node to validate based on xpath and attribute name
				regexValue = regexValue.substring(0,
						regexValue.indexOf(":") + 1).trim();
				String[] attributeParams = regexValue.split(",");
				NamedNodeMap attr = nList.item(0).getAttributes();
				Node nodeAttr = attr.getNamedItem(attributeParams[0]);
				xPathValue = nodeAttr.getTextContent();
			} else {
				//Default path. Get node value based on xpath
				xPathValue = nList.item(0).getTextContent();
			}
		}

		//Validate expected value with actual value and report in html table 
		if(!errorMessage.isEmpty()){
			buffer.append("<tr><td style='width: 100px; color: black; text-align: left;'>"
					+ xpath + "</td>");
			buffer.append("<td style='width: 100px; color: black; text-align: center;'>"
					+ regexValue + "</td>");
			buffer.append("<td style='width: 100px; color: black; text-align: center;'>"
					+ errorMessage + "</td>");
			buffer.append("<td style='width: 100px; color: red; text-align: center;'><b>Fail</b></td></tr>");
		}else if (Regex.match(regexValue, xPathValue)) {		
			buffer.append("<tr><td style='width: 100px; color: black; text-align: left;'>"
					+ xpath + "</td>");
			buffer.append("<td style='width: 100px; color: black; text-align: center;'>"
					+ regexValue + "</td>");
			buffer.append("<td style='width: 100px; color: black; text-align: center;'>"
					+ xPathValue + "</td>");
			buffer.append("<td style='width: 100px; color: green; text-align: center;'><b>Pass</b></td></tr>");
		} else {
			buffer.append("<tr><td style='width: 100px; color: black; text-align: left;'>"
					+ xpath + "</td>");
			buffer.append("<td style='width: 100px; color: black; text-align: center;'>"
					+ regexValue + "</td>");
			buffer.append("<td style='width: 100px; color: black; text-align: center;'>"
					+ xPathValue + "</td>");
			buffer.append("<td style='width: 100px; color: red; text-align: center;'><b>Fail</b></td></tr>");
		}
		//return boolean
		return Regex.match(regexValue, xPathValue);
	}

	/**
	 *  Validate XML Response and reports findings
	 * @author Justin Phlegar
	 * @version Created: 08/28/2014
	 * @param resourcePath: path of file to read
	 * @param scenario String: scenario to validate 
	 */
	public boolean validateResponse(String resourcePath, String scenario) {
		return validateNodeValueByXPath(getResponseDocument(),
				getTestScenario(resourcePath, scenario));
	}

	/**
	 *  Opens the WSDL file that was loaded with the {@link setEnvironmentServiceURL} and load a XML Template for selected operation
	 * @author Justin Phlegar
	 * @version Created: 08/28/2014
	 * @param operation String: operation to load
	 */
	protected String buildRequestFromWSDL(String operation) {
		TestReporter.logTrace("Entering SoapService#buildRequestFromWSDL");
		strOperationName = operation;
		System.setProperty("soapui.log4j.config", this.getClass().getResource("/soapui-log4j.xml").getPath());
		WsdlInterface[] wsdls = null;
		WsdlInterface wsdl = null;
		try {
		    boolean isLoaded = false;
		    TestReporter.logTrace("Starting empty SoapUI Project");
		    project = new WsdlProject();

		    TestReporter.logTrace("Successfully started SoapUI Project");
		    /*if(project.getInterfaceList().size()>0){
			for(Interface soapInterface : project.getInterfaceList()){
			    if(soapInterface.getName().contains(getServiceName())){
				isLoaded = true;
				wsdl = (WsdlInterface) soapInterface;
				break;
			    }
			}
		    }
		    if(isLoaded ==false){*/
			TestReporter.logTrace("Import WSDL into project from URL [ " + url+" ]");
			wsdls = WsdlImporter.importWsdl(project, url);

			/*project.importWsdl(url, true);
			project.save();*/
			wsdl = wsdls[0];
			/*for(Interface soapInterface : project.getInterfaceList()){
			    if(soapInterface.getName().contains(getServiceName())){
				isLoaded = true;
				wsdl = (WsdlInterface) soapInterface;
				break;
			    }
			}*/
			TestReporter.logTrace("Successfully loaded WSDL");
		    //}
		} catch (XmlException xmle) {
			throw new RuntimeException("Error loading XML: " + xmle.getCause());
		} catch (IOException ioe) {
			throw new RuntimeException("Error reading WSDL file: " + ioe.getCause());		
		} catch (SoapUIException e1) {	
			throw new SoapException("Failed to start Soap Project", e1.getCause());
		} catch (Exception e) {
			throw new RuntimeException("Error reading WSDL file: " + e.getCause());
		}
		
		TestReporter.logTrace("Load Operation by name [ "+operation+" ]");
		WsdlOperation wsdlOperation = wsdl.getOperationByName(operation);
		
		TestReporter.logTrace("Successfully loaded operation");
		TestReporter.logTrace("Generate raw request from operation");
		String rawRequest = wsdlOperation.createRequest(true);
		
		TestReporter.logTrace("Successfully generated request");
		TestReporter.logTrace("Exiting SoapService#buildRequestFromWSDL");
		return rawRequest ;
	}

	protected void removeComments() {
		setRequestDocument((Document) XMLTools.removeComments(getRequestDocument()));
	}

	protected void removeWhiteSpace() {
		setRequestDocument(XMLTools.removeWhiteSpace(getRequestDocument()));
	}

	/**
	 *  Call functions during setting of the xpath
	 * @author Justin Phlegar
	 * @version Created: 08/28/2014
	 * @param xpath String: Xpath to run the function on 
	 * @param function String: function to call
	 * 					<br><br><b>Supported Functions:</b>
	 * 					<br><b>value="fx:addnode"</b>  -- Add a new node at Xpath position. Expects "Node:nodeName" where nodeName will be the name given to the node
	 * 		            <br><b>value="fx:getdatetime"</b>  -- Set date and time in a format accepted by XML. Expects "DaysOut:x" where x is the number of days out
	 * 					<br><b>value="fx:getdate"</b>  -- Set date in a format accepted by XML. Expects "DaysOut:x" where x is the number of days out 					
	 *  				<br><b>value="fx:removenode"</b>  -- Remove a node at Xpath position.
	 *   				<br><b>value="fx:randomnumber"</b>  -- Set a string of random numbers. Expects "Node:x" where x is the length of the string to output
	 *    				<br><b>value="fx:randomstring"</b>  -- Set a string of random characters. Expects "Node:x" where x is the length of the string to output
	 *        			<br><b>value="fx:randomalphanumeric"</b>  -- Set a string of random numbers and characters. Expects "Node:x" where x is the length of the string to output 		  	
	 */
	private String handleValueFunction(Document doc, String function, String xpath) {
		String[] params = function.split(";");
		String command = params[0];
		String[] length = new String[2];
		String[] tagName = new String[2];
		String[] daysOut = new String[2];

		switch (command.toLowerCase()) {
		case "fx:getdatetime":
			daysOut = params[1].split(":");
			if (daysOut[0].trim().equalsIgnoreCase("DaysOut")) {
				return Randomness.generateCurrentXMLDatetime(Integer.parseInt(daysOut[1]));
			} else {
				// report error
			}
			
		case "fx:getdate":
			daysOut = params[1].split(":");
			if (daysOut[0].trim().equalsIgnoreCase("DaysOut")) {
				return Randomness.generateCurrentXMLDate(Integer.parseInt(daysOut[1]));
			} else{
				// report error 
			}
		case "fx:addnode":
			String tag = params[1].replace("node:", "").replace("Node:", "");
			setRequestDocument(XMLTools.addNode(doc,tag.trim(), xpath));
			return "XMLUpdated";
			
		case "fx:addnodes":
			tagName = params[1].split(":");
			if (tagName[0].toLowerCase().trim().contains("node")) {
				String[] nodes = tagName[1].split("/");
				String appendedXpath = xpath;
				for(String node : nodes){
					setRequestDocument(XMLTools.addNode(doc,node.replaceAll("\\[(.*?)\\]",""), appendedXpath));
					appendedXpath += "/"+node;
				}
			} else {
				// report error
			}
			return "XMLUpdated";

		case "fx:addattribute":
			tagName = params[1].split(":",2);
			if (tagName[0].trim().equalsIgnoreCase("attribute")) {
				setRequestDocument(XMLTools.addAttribute(doc,tagName[1].trim(), xpath));
			} else {
				// report error
			}
			return "XMLUpdated";
			
		case "fx:addnamespace":
			tagName = params[1].split(":",2);
			if (tagName[0].trim().equalsIgnoreCase("namespace")) {
				setRequestDocument(XMLTools.addNamespace(doc,tagName[1].trim(), xpath));
			} else {
				// report error
			}
			return "XMLUpdated";


		case "fx:removenode":
			setRequestDocument(XMLTools.removeNode(doc, xpath));
			return "XMLUpdated";
			
		case "fx:removeattribute":
			String attribute = xpath.substring(xpath.lastIndexOf("@") + 1, xpath.length());
			xpath = xpath.substring(0,xpath.lastIndexOf("@") -1);
			setRequestDocument(XMLTools.removeAttribute(doc,attribute, xpath));
			return "XMLUpdated";
/*
		case "fx:dbquery":
			break;

		case "fx:dbresult":
			break;
*/

		case "fx:randomnumber":
			length = params[1].split(":");
			if (length[0].trim().equalsIgnoreCase("Node")) {
				return Randomness.randomNumber(Integer.parseInt(length[1]));
			} else {
				// report error
			}

		case "fx:randomstring":
			length = params[1].split(":");
			if (length[0].trim().equalsIgnoreCase("Node")) {
				return Randomness.randomString(Integer.parseInt(length[1]));
			} else {
				return Randomness.randomString(Integer.parseInt(length[0]));
		}

		case "fx:randomalphanumeric":
			length = params[1].split(":");
			if (length[0].trim().equalsIgnoreCase("Node")) {
				return Randomness.randomAlphaNumeric(Integer.parseInt(length[1]));
			} else {
				// report error
			}
		
		default:
			throw new RuntimeException("The command [" + command + " ] is not a valid command");
		}
	}
}