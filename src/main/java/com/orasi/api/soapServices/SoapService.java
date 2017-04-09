package com.orasi.api.soapServices;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
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
import javax.xml.ws.WebServiceException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.testng.Reporter;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import com.orasi.api.soapServices.exceptions.HeaderNotFoundException;
import com.orasi.api.soapServices.exceptions.MissingFunctionParameterValueException;
import com.orasi.api.soapServices.exceptions.SoapException;
import com.orasi.exception.automation.XPathInvalidExpression;
import com.orasi.exception.automation.XPathNotFoundException;
import com.orasi.exception.automation.XPathNullNodeValueException;
import com.orasi.utils.ExcelDocumentReader;
import com.orasi.utils.Randomness;
import com.orasi.utils.Regex;
import com.orasi.utils.TestReporter;
import com.orasi.utils.XMLTools;
import com.orasi.utils.dataProviders.CSVDataProvider;

public abstract class SoapService{
	private String strServiceName;
	private String strOperationName;
	private String url = null;
	private String intResponseStatusCode = null;
	private Document requestDocument = null;
	private Document responseDocument = null;
	protected StringBuffer buffer = new StringBuffer();
	private String soapVersion = SOAPConstants.SOAP_1_1_PROTOCOL;
	private Map<String, String> requestHeaders = new HashMap<String, String>();
	private MimeHeaders responseHeaders = null;

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


	public String getResponseHeader(String name){
		try{
			return responseHeaders.getHeader(name)[0];
		}catch(NullPointerException throwAway){}
		//If code reaches this point, header was not found
		throw new HeaderNotFoundException("Response Header [ " +name+" ] was not found");		
	}

	public void addRequestHeader(String header, String value){
		requestHeaders.put(header, value);
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
	 *  Find and open the excel or csv file sent. If successful, look and find
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
		TestReporter.logTrace("Getting file from Resources");
		int startCol, endRow, endCol, ci, cj;
		filePath = getClass().getResource(file).getPath();

		filePath = filePath.replace("%20", " ");
		TestReporter.logTrace("Full file path ["+filePath+"]");

		Object[][] xlsSheet = null; 
		if (filePath.toUpperCase().indexOf(".XLS") > 0) {
			TestReporter.logTrace("Retrieving data from ExcelDocumentReader");
			xlsSheet = new ExcelDocumentReader(filePath).readData("0", -1, 0);
		}
		else {
			TestReporter.logTrace("CSVDataProvider");
			xlsSheet = CSVDataProvider.getData(filePath);
		}
		TestReporter.logTrace("Successfully retrieved data");

		startCol = -1;
		
		TestReporter.logTrace("Finding column with scenario [ " + scenario + " ]");
		for(int column = 0 ; column < xlsSheet[0].length ; column++){
			if (xlsSheet[0][column].toString().contains(scenario)){
				startCol = column;
				break;
			}
		}
		if(startCol == -1) throw new WebServiceException("Failed to find scenario [ " + scenario + " ] in CSV ");
		endCol = startCol + 3;
		TestReporter.logTrace("Found scenario [ " + scenario + " ]");
		TestReporter.logTrace("Start Column [ "+startCol+" ] " );
		TestReporter.logTrace("End Column [ "+(startCol + 3)+" ]" );
		
		TestReporter.logTrace("Determining last row of data in column [ " + (startCol +1)+ " ]");
		endRow = 0;
		for(endRow = xlsSheet.length ; xlsSheet[endRow-1][startCol+1].toString().isEmpty() ; endRow--){}
		TestReporter.logTrace("Found last row of data in column [ " + (startCol +1)+ " ]");
		TestReporter.logTrace("End Row [ "+endRow+" ] " );
		
		tabArray = new String[endRow-1][2];
		ci = 0;

		TestReporter.logTrace("Transfer data from excel sheet to array");
		for (int i = 1; i < endRow; i++, ci++) {
			cj = 0;
			for (int j = startCol + 1; j < endCol; j++, cj++) {
				tabArray[ci][cj] = xlsSheet[i][j].toString();
			}
		}
		TestReporter.logTrace("Successfully transfered data to array");
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

			if(requestHeaders.size() > 0){		
				TestReporter.logTrace("Additional headers to be added");
				for(String key : requestHeaders.keySet()){
					TestReporter.logInfo("Adding header [" + key + " ] with value [" + requestHeaders.get(key) + " ]");
					MimeHeaders soapHeader = request.getMimeHeaders();
					soapHeader.addHeader(key, requestHeaders.get(key));	
				}		
			}

			TestReporter.logTrace("Create Soap Connection Factory");
			connectionFactory = SOAPConnectionFactory.newInstance();

			TestReporter.logTrace("Create Soap Connection");
			connection = connectionFactory.createConnection();

			TestReporter.logTrace("Send request to service");
			response = connection.call(request, url);

			responseHeaders = response.getMimeHeaders();

			TestReporter.logTrace("Successfully sent Soap Message. Normalizing response");
			response.getSOAPBody().normalize();
			responseBody = response.getSOAPBody();
		} catch (UnsupportedOperationException uoe) {
			throw new SoapException("Operation given did not match any operations in the service"+ uoe.getCause());
		} catch (SOAPException soape) {
			throw new SoapException("Soap Connection failure" , soape.getCause());
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
			throw new XPathInvalidExpression("Xpath evaluation failed with xpath [ " + xpath + " ] ", xpe.getCause());	
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
			TestReporter.logDebug("Set value [ " + scenarios[x][1].toString() + " ] to XPath [ " + scenarios[x][0].toString() + " ]");
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
		buffer.setLength(0);
		buffer.append("<table border='1' width='100%'>");
		buffer.append("<tr><td style='width: 100px; color: black; text-align: center;'><b>XPath</b></td>");
		buffer.append("<td style='width: 100px; color: black; text-align: center;'><b>Regex</b></td>");
		buffer.append("<td style='width: 100px; color: black; text-align: center;'><b>Value</b></td>");
		buffer.append("<td style='width: 100px; color: black; text-align: center;'><b>Status</b></td></tr>");
		for (int x = 0; x < scenarios.length; x++) {
			if (!validateNodeValueByXPath(doc, scenarios[x][0].toString(),
					scenarios[x][1].toString())) {
				status = false;
			}
		}
		buffer.append("</table>");
		Reporter.log(buffer.toString()+ "<br/>");
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
		if (errorMessage.isEmpty() && nList.item(0) == null ) {
			errorMessage = "No xpath was found with the path [ " + xpath + " ] ";
		}

		if (errorMessage.isEmpty()){
			xPathValue = nList.item(0).getTextContent();
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
	 * 					<br><b>value="fx:addnode"</b>  -- Add a new node at Xpath position.
	 * 		            <br><b>value="fx:getdatetime"</b>  -- Set date and time in a format accepted by XML. 
	 * 					<br><b>value="fx:getdate"</b>  -- Set date in a format accepted by XML. 					
	 *  				<br><b>value="fx:removenode"</b>  -- Remove a node at Xpath position.
	 *   				<br><b>value="fx:randomnumber"</b>  -- Set a string of random numbers. 
	 *    				<br><b>value="fx:randomstring"</b>  -- Set a string of random characters. 
	 *        			<br><b>value="fx:randomalphanumeric"</b>  -- Set a string of random numbers and characters. 	  	
	 */
	private String handleValueFunction(Document doc, String function, String xpath) {
		String[] params = function.split(";");
		String command = params[0];
		String daysOut = "";
		String length = "";

		switch (command.toLowerCase()) {
		case "fx:getdatetime":
			if(params.length == 1) throw new MissingFunctionParameterValueException("Missing second parameter: Expected format [ fx:getdatetime ; 0] where 0 is Days Out");
			daysOut = params[1].replace("DaysOut:", "");
			return Randomness.generateCurrentXMLDatetime(Integer.parseInt(daysOut));

		case "fx:getdate":
			if(params.length == 1) throw new MissingFunctionParameterValueException("Missing second parameter: Expected format [ fx:getdate ; 0] where 0 is Days Out");
			daysOut = params[1].replace("DaysOut:", "");
			return Randomness.generateCurrentXMLDate(Integer.parseInt(daysOut));
			
		case "fx:addnode":
			if(params.length == 1) throw new MissingFunctionParameterValueException("Missing second parameter: Expected format [ fx:addnode ; nodeName] where nodeName is name of the node to add");
			String tag = params[1].replace("node:", "").replace("Node:", "");
			setRequestDocument(XMLTools.addNode(doc,tag.trim(), xpath));
			return "XMLUpdated";

		case "fx:addnodes":
			if(params.length == 1) throw new MissingFunctionParameterValueException("Missing second parameter: Expected format [ fx:addnodes ; nodeNameList] where nodeNameList is name of the node to add");
			String tags = params[1].replace("node:", "").replace("Node:", "");
			String[] nodes = tags.split("/");
			String appendedXpath = xpath;
			for(String node : nodes){
				setRequestDocument(XMLTools.addNode(doc,node.replaceAll("\\[(.*?)\\]",""), appendedXpath));
				appendedXpath += "/"+node;
			}
			return "XMLUpdated";

		case "fx:addattribute":
			if(params.length == 1) throw new MissingFunctionParameterValueException("Missing second parameter: Expected format [ fx:addattribute ; attributeName] where attributeName is name of the attribute to add");
			String newAttribute = params[1].replace("attribute:", "").replace("Attribute:", "");
			setRequestDocument(XMLTools.addAttribute(doc,newAttribute.trim(), xpath));			
			return "XMLUpdated";

		case "fx:addnamespace":
			if(params.length == 1) throw new MissingFunctionParameterValueException("Missing second parameter: Expected format [ fx:addnamespace ; xmlns:name,url] where name is the name of the namespace to add and url is the URL of the namespace");
			String namespace = params[1].replace("namespace:", "").replace("Namespace:", "");
			setRequestDocument(XMLTools.addNamespace(doc,namespace.trim(), xpath));			
			return "XMLUpdated";


		case "fx:removenode":
			setRequestDocument(XMLTools.removeNode(doc, xpath));
			return "XMLUpdated";

		case "fx:removeattribute":
			String attribute = xpath.substring(xpath.lastIndexOf("@") + 1, xpath.length());
			xpath = xpath.substring(0,xpath.lastIndexOf("@") -1);
			setRequestDocument(XMLTools.removeAttribute(doc,attribute, xpath));
			return "XMLUpdated";

		case "fx:randomnumber":
			if(params.length == 1) throw new MissingFunctionParameterValueException("Missing second parameter: Expected format [ fx:randomnumber ; 4] where 4 is length of numbers to generate");
			length = params[1].replace("node:", "").replace("Node:", "");
			return Randomness.randomNumber(Integer.parseInt(length));
			
		case "fx:randomstring":
			if(params.length == 1) throw new MissingFunctionParameterValueException("Missing second parameter: Expected format [ fx:randomstring ; 4] where 4 is length of string to generate");
			length = params[1].replace("node:", "").replace("Node:", "");
			return Randomness.randomString(Integer.parseInt(length));
			
		case "fx:randomalphanumeric":
			if(params.length == 1) throw new MissingFunctionParameterValueException("Missing second parameter: Expected format [ fx:randomalphanumeric ; 4] where 4 is length of string to generate");
			length = params[1].replace("node:", "").replace("Node:", "");
			return Randomness.randomAlphaNumeric(Integer.parseInt(length));
			
		default:
			throw new SoapException("The command [" + command + " ] is not a valid command");
		}
	}
}