package com.orasi.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPException;
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

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.orasi.api.soapServices.exceptions.SoapException;
import com.orasi.exception.automation.XPathNotFoundException;


public class XMLTools{
	
	/**
	 * Adds an Attribute to the node on the location of the xpath 
	 * @author Justin Phlegar
	 * @version Created: 08/28/2014
	 * @param doc Document: XML Document that will be updated
	 * @param nodeName String: Name of the attribute to add to the node
	 * @param xpath String: Path of the Node in the XML to add the Attribute
	 */
	public static Document addAttribute(Document doc, String attributeName, String xpath) {
		TestReporter.logTrace("Entering XMLTools#addAttribute");
		TestReporter.logDebug("Adding Attribute [ " + attributeName+ " ] to XPath [ " + xpath+ " ]");
		XPathFactory xPathFactory = XPathFactory.newInstance();
		XPath xPath = xPathFactory.newXPath();
		XPathExpression expr;
		NodeList nList = null;
		try {
			TestReporter.logTrace("Checking validity of xpath");
			expr = xPath.compile(xpath);
			nList = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
		} catch (XPathExpressionException xpe) {
			throw new RuntimeException("Xpath evaluation failed with xpath [ " + xpath + " ] ", xpe.getCause());	
		}

		TestReporter.logTrace("XPath is valid. Checking for nodes with desired xpath");
		//Ensure an element was found, if not then throw error and fail
		if (nList.item(0) == null) throw new RuntimeException("No xpath was found with the path [ " + xpath + " ] ");
			
		//Create new XML document based on XPath
		Element element  = (Element) nList.item(0);
		element.setAttribute(attributeName, "");

		TestReporter.logTrace("At least one node found on Xpath. Adding Attribute to Node");
		TestReporter.logTrace("Successfully added Attribute [ " + attributeName+ " ]");
		TestReporter.logTrace("Exiting XMLTools#addAttribute");
		return doc;
	}

	/**
	 * Removes an Attribute to the node on the location of the xpath 
	 * @author Justin Phlegar
	 * @version Created: 08/28/2014
	 * @param doc Document: XML Document that will be updated
	 * @param nodeName String: Name of the attribute to remove to the node
	 * @param xpath String: Path of the Node in the XML to add the Attribute
	 */
	public static Document removeAttribute(Document doc, String attributeName, String xpath) {
		TestReporter.logTrace("Entering XMLTools#removeAttribute");
		TestReporter.logDebug("Removing Attribute [ " + attributeName+ " ] to XPath [ " + xpath+ " ]");
		XPathFactory xPathFactory = XPathFactory.newInstance();
		XPath xPath = xPathFactory.newXPath();
		XPathExpression expr;
		NodeList nList = null;
		try {
			TestReporter.logTrace("Checking validity of xpath");
			expr = xPath.compile(xpath);
			nList = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
		} catch (XPathExpressionException xpe) {
			throw new RuntimeException("Xpath evaluation failed with xpath [ " + xpath + " ] ", xpe.getCause());	
		}

		TestReporter.logTrace("XPath is valid. Checking for nodes with desired xpath");
		//Ensure an element was found, if not then throw error and fail
		if (nList.item(0) == null) throw new RuntimeException("No xpath was found with the path [ " + xpath + " ] ");

		TestReporter.logTrace("At least one node found on Xpath. Removing Attribute from Node");
		//Create new XML document based on XPath
		Element element  = (Element) nList.item(0);
		element.removeAttribute(attributeName);

		TestReporter.logTrace("Successfully removed Attribute [ " + attributeName+ " ]");
		TestReporter.logTrace("Exiting XMLTools#removeAttribute");
		return doc;
	}

	/**
	 * Adds an Namespace to the node on the location of the xpath 
	 * @author Justin Phlegar
	 * @version Created: 08/28/2014
	 * @param doc Document: XML Document that will be updated
	 * @param namespace String: Name of the namespace to add to the node
	 * @param xpath String: Path of the Node in the XML to add the Namespace
	 */
	public static Document addNamespace(Document doc, String namespace, String xpath) {
		TestReporter.logTrace("Entering XMLTools#addNamespace");
		TestReporter.logDebug("Adding Namespace [ " + namespace+ " ] to XPath [ " + xpath+ " ]");
		XPathFactory xPathFactory = XPathFactory.newInstance();
		XPath xPath = xPathFactory.newXPath();
		XPathExpression expr;
		NodeList nList = null;
		String[] values = namespace.split(",");
		String namespaceName = values[0];
		String namespaceURL = values[1];
		try {
			TestReporter.logTrace("Checking validity of xpath");
			expr = xPath.compile(xpath);
			nList = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
		} catch (XPathExpressionException xpe) {
			throw new RuntimeException("Xpath evaluation failed with xpath [ " + xpath + " ] ", xpe.getCause());	
		}

		TestReporter.logTrace("XPath is valid. Checking for nodes with desired xpath");
		//Ensure an element was found, if not then throw error and fail
		if (nList.item(0) == null) throw new RuntimeException("No xpath was found with the path [ " + xpath + " ] ");

		TestReporter.logTrace("At least one node found on Xpath. Adding Namespace on Xpath");
		//Create new XML document based on XPath
		Element element  = (Element) nList.item(0);
		element.setAttributeNS("http://www.w3.org/2000/xmlns/", namespaceName, namespaceURL);

		TestReporter.logTrace("Successfully added namespace [ " + namespace+ " ]");
		TestReporter.logTrace("Exiting XMLTools#addNamespace");
		return doc;
	}
	
	/**
	 *  Takes an xpath and adds a node to the location of the xpath and name of tagName
	 * @author Justin Phlegar
	 * @version Created: 08/28/2014
	 * @param doc Document: XML Document that will be updated
	 * @param nodeName String: Name of the node to add to the XML Document
	 * @param xpath String: Path in the XML to add the node
	 */
	public static Document addNode(Document doc, String nodeName, String xpath) {
		TestReporter.logTrace("Entering XMLTools#addNode");
		TestReporter.logDebug("Adding Node [ " + nodeName + " ] on XPath [ " + xpath + " ]");
		XPathFactory xPathFactory = XPathFactory.newInstance();
		XPath xPath = xPathFactory.newXPath();
		XPathExpression expr;
		NodeList nList = null;

		try {
			TestReporter.logTrace("Checking validity of xpath");
			expr = xPath.compile(xpath);
			nList = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
		} catch (XPathExpressionException xpe) {
			throw new RuntimeException("Xpath evaluation failed with xpath [ " + xpath + " ] ", xpe.getCause());	
		}

		TestReporter.logTrace("XPath is valid. Checking for nodes with desired xpath");
		//Ensure an element was found, if not then throw error and fail
		if (nList.item(0) == null) throw new RuntimeException("No xpath was found with the path [ " + xpath + " ] ");

		TestReporter.logTrace("At least one node found on Xpath. Adding node to Document on Xpath");
		//Create new XML document based on XPath
		Document dom = nList.item(0).getOwnerDocument();

		// Create a new Node with the given tag name
		Node node = dom.createElement(nodeName);

		// Add the new node structure to the previous parent node
		nList.item(0).appendChild(node);

		TestReporter.logTrace("Successfully added node [ " + nodeName + " ]");
		TestReporter.logTrace("Exiting XMLTools#addNode");
		return doc;
	}

	 /**  Takes an xpath and removes a node to the location of the xpath 
	 * @author Justin Phlegar
	 * @version Created: 08/28/2014
	 * @param doc Document: XML Document that will be updated
	 * @param xpath String: Path in the XML to add the node
	 * @return Document xml with removed Node
	 */
	public static Document removeNode(Document doc, String xpath) {
		TestReporter.logTrace("Entering XMLTools#removeNode");
		XPathFactory xPathFactory = XPathFactory.newInstance();
		XPath xPath = xPathFactory.newXPath();
		XPathExpression expr;
		NodeList nList = null;
		
		TestReporter.logDebug("Remove node from xpath [ " + xpath + " ]");
		try {
			TestReporter.logTrace("Checking validity of xpath");
			expr = xPath.compile(xpath);
			nList = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
		} catch (XPathExpressionException e1) {
			throw new XPathNotFoundException("Xpath evaluation failed with xpath [ " + xpath + " ] ");	
		}

		TestReporter.logTrace("XPath is valid. Removing node");
		Element element = (Element) nList.item(0);
		element.getParentNode().removeChild(element);

		TestReporter.logTrace("Node removal successful. Normalizing document");
		doc.normalize();
		
		TestReporter.logTrace("Exiting XMLTools#removeNode");
		return doc;
	}
	 
	/**
	 *  Takes an xpath and return the value found
	 * @author Justin Phlegar
	 * @version Created: 08/28/2014	 * 
	 * @param doc Document: XML Document that will be queried
	 * @param xpath String: xpath to evaluate
	 */
	public static String getValueByXpath(Document doc, String xpath) {
		TestReporter.logTrace("Entering XMLTools#getValueByXpath");
		TestReporter.logDebug("Get value from XPath [ " + xpath + " ]");
		XPathFactory xPathFactory = XPathFactory.newInstance();
		XPath xPath = xPathFactory.newXPath();
		XPathExpression expr;
		NodeList nList = null;		

		//Evaluate the xpath 
		try {
			TestReporter.logTrace("Checking validity of xpath");
			expr = xPath.compile(xpath);
			nList = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
		} catch (XPathExpressionException xpe) {
			throw new RuntimeException("Xpath evaluation failed with xpath [ " + xpath + " ] ", xpe.getCause());	
		}

		TestReporter.logTrace("XPath is valid. Checking for nodes with desired xpath");
		//Ensure an element was found, if not then throw error and fail
		if (nList.item(0) == null) throw new XPathNotFoundException(xpath );

		TestReporter.logTrace("At least one node found on Xpath. Returning data in Node");
		TestReporter.logTrace("Exiting XMLTools#getValueByXpath");
		//If no errors, then return the value found
		return nList.item(0).getTextContent();
	}

	
	/**
	 * Generate an XML Document from SOAPMessage
	 * @author Justin Phlegar
	 * @version Created: 08/28/2014
	 * @param soapXML  SOAPMessage to transform to XML
	 * @return Document xml of SOAPMessage
	 */
	public static Document makeXMLDocument(SOAPMessage soapXML) {
		TestReporter.logTrace("Entering XMLTools#makeXMLDocument");
		TestReporter.logTrace("Creating Document factory"); 
		Document doc = null;

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try {
			TestReporter.logTrace("Attempting to transform SoapMessage to XML");
			soapXML.writeTo(outputStream);
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			factory.setNamespaceAware(false);
			factory.setIgnoringElementContentWhitespace(true);
			DocumentBuilder builder = factory.newDocumentBuilder();
			doc = builder.parse(new ByteArrayInputStream(outputStream.toByteArray()));
		} catch (ParserConfigurationException pce) {
			throw new RuntimeException("Failed to create a Document Builder Factory", pce.getCause());
		} catch (SAXException saxe) {
			 throw new RuntimeException("Failed to parse the xml", saxe.getCause());
		} catch (IOException ioe) {
			throw new RuntimeException("Failed to find the source XML", ioe.getCause());
		} catch (SOAPException e) {
			throw new RuntimeException("Failed to get source XML from Soap Message", e.getCause());
		}

		TestReporter.logTrace("Successfully transformed SoapMessage to XML. Normalize document");
		doc.getDocumentElement().normalize();
		TestReporter.logTrace("Exiting XMLTools#makeXMLDocument");
		return doc;
	}
	
	/**
	 * Generate an XML Document from String
	 * @author Justin Phlegar
	 * @version Created: 08/28/2014
	 * @param xml  String of XML to transform to XML Document
	 * @return Document xml of String
	 */
	public static Document makeXMLDocument(String xml)  {
		TestReporter.logTrace("Entering XMLTools#makeXMLDocument");
		TestReporter.logTrace("Creating Document factory"); 
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(false);
		factory.setIgnoringElementContentWhitespace(true);
		DocumentBuilder builder;
		Document doc = null;
		try {
			TestReporter.logTrace("Attempting to transform String to XML");
			builder = factory.newDocumentBuilder();
			InputSource source = new InputSource(new ByteArrayInputStream(xml
					.toString().getBytes()));
			 doc = builder.parse(source);
		} catch (ParserConfigurationException pce) {
			throw new RuntimeException("Failed to create a Document Builder Factory", pce.getCause());
		} catch (SAXException saxe) {
			 throw new RuntimeException("Failed to parse the xml", saxe.getCause());
		} catch (IOException ioe) {
			throw new RuntimeException("Failed to find the source XML", ioe.getCause());
		}

		TestReporter.logTrace("Successfully transformed String to XML. Normalize document");
		doc.getDocumentElement().normalize();
		TestReporter.logTrace("Exiting XMLTools#makeXMLDocument");
		return doc;

	}


	/**
	 * Generate an XML Document from String
	 * @author Justin Phlegar
	 * @version Created: 08/28/2014
	 * @param xml  String of XML to transform to XML Document
	 * @return Document xml of String
	 */
	public static Document makeXMLDocument(File file)  {
		TestReporter.logTrace("Entering XMLTools#makeXMLDocument");
		TestReporter.logTrace("Creating Document factory"); 
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(false);
		factory.setIgnoringElementContentWhitespace(true);
		Document doc = null;
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			TestReporter.logTrace("Attempting to from file and save as to XML. File [ " + file.getPath()+ " ]");
			doc = builder.parse(file);
		} catch (SAXException saxe) {
			 throw new RuntimeException("Failed to parse the xml", saxe.getCause());
		} catch (IOException ioe) {
			throw new RuntimeException("Failed to find the source XML", ioe.getCause());
		} catch (ParserConfigurationException pce) {
			throw new RuntimeException("Failed to create a Document Builder Factory", pce.getCause());
		}

		TestReporter.logTrace("Successfully transformed String to XML. Normalize document");
		doc.getDocumentElement().normalize();
		TestReporter.logTrace("Exiting XMLTools#makeXMLDocument");
		return doc;

	}

	/**
	 * Transform a XML Document to String
	 * @author Justin Phlegar
	 * @version Created: 08/28/2014
	 * @param doc XML Document to transform to String
	 * @return xml in String format
	 */
	public static String transformXmlToString(Document doc){
		TestReporter.logTrace("Entering XMLTools#transformXmlToString");
		StringWriter sw = new StringWriter();
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer = null;
		TestReporter.logTrace("Starting XML to String transformer");
		try {
			transformer = tf.newTransformer();
		} catch (TransformerConfigurationException e) {
			throw new SoapException("Failed to create XML Transformer", e.getCause());
		}

		TestReporter.logTrace("Adding XML transformer properties");
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
		transformer.setOutputProperty(OutputKeys.METHOD, "xml");
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

		try {

			TestReporter.logTrace("Attempting to transform XML to String ");
			transformer.transform(new DOMSource(doc),new StreamResult(sw));
		} catch (TransformerException e) {

			TestReporter.logTrace("Failed to transform XML to String ");
			throw new SoapException(
					"Failed to transform Request XML Document. Ensure XML Document has been successfully loaded.", e.getCause());
		}

		TestReporter.logTrace("Successfully transformed XML to String");
		TestReporter.logTrace("Exiting XMLTools#transformXmlToString");
		return sw.toString();
	}

	/**
	 * A recursive method that will iterate through all nodes and delete Comment nodes
	 * @author Justin Phlegar
	 * @version Created: 08/28/2014
	 * @param node XML document
	 * @return self
	 */
	public static Node removeComments(Node  node) {
		if (node.getNodeType() == Node.COMMENT_NODE) {
			node.getParentNode().removeChild(node);
		} else {
			// check the children recursively
			NodeList list = node.getChildNodes();
			for (int i = 0; i < list.getLength(); i++) {
				removeComments(list.item(i));
			}
		}
		node.normalize();
		return node;
	}
	
	/**
	 * Iterate through all nodes and remove white space lines
	 * @author Justin Phlegar
	 * @version Created: 08/28/2014
	 * @param doc XML Document
	 * @return xml Document after updates
	 */
	public static Document removeWhiteSpace(Document doc) {
		TestReporter.logTrace("Entering XMLTools#removeWhiteSpace");
		XPath xp = XPathFactory.newInstance().newXPath();
		NodeList nl = null;
		try {
			TestReporter.logTrace("Get Node list of all whitespace nodes");
			nl = (NodeList) xp.evaluate("//text()[normalize-space(.)='']", doc,
					XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			throw new XPathNotFoundException("Xpath evaluation failed to normalize white space");	
		}

		TestReporter.logTrace("Iterate through of all whitespace nodes and remove them from XML");
		for (int i = 0; i < nl.getLength(); ++i) {
			Node node = nl.item(i);
			node.getParentNode().removeChild(node);
		}

		TestReporter.logTrace("Removed all whitespace nodes");
		TestReporter.logTrace("Exiting XMLTools#removeWhiteSpace");
		return doc;
	}

	/**
	 * Transform a XML Document to String
	 * @author Waightstill Avery
	 * @version Created: 08/28/2016
	 * @param doc XML Document 
	 * @param xpath Xpath to search and return all nodes found
	 * @return Nodelist of all nodes found on xpath
	 */
	public static NodeList getNodeList(Document doc, String xpath) {
		TestReporter.logTrace("Entering XMLTools#getNodeList");
		XPathFactory xPathFactory = XPathFactory.newInstance();
		XPath xPath = xPathFactory.newXPath();
		XPathExpression expr;
		NodeList nList = null;
		try {
			TestReporter.logTrace("Checking validity of xpath");
			expr = xPath.compile(xpath);
			nList = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
		} catch (XPathExpressionException xpe) {
			throw new XPathNotFoundException("Xpath evaluation failed with xpath [ " + xpath + " ] ");	
		}

		TestReporter.logTrace("XPath is valid. Checking for nodes with desired xpath");
		
		//Ensure an element was found, if not then throw error and fail
		if (nList.item(0) == null) throw new XPathNotFoundException("No xpath was found with the path [ " + xpath + " ] ");

		TestReporter.logTrace("At least one node found on Xpath. Returning as list");
		TestReporter.logTrace("Exiting XMLTools#getNodeList");
		return nList;
	}
	
	/**
	 * Transform a XML Document to String
	 * @author Waightstill Avery
	 * @version Created: 08/28/2016
	 * @param nodeList Nodelist to look in
	 * @param xpath Xpath to search and return all nodes found
	 * @return Nodelist of all nodes found on xpath
	 */
	public static NodeList getNodeList(Node nodeList, String xpath) {
		TestReporter.logTrace("Entering XMLTools#getNodeList");
		XPathFactory xPathFactory = XPathFactory.newInstance();
		XPath xPath = xPathFactory.newXPath();
		XPathExpression expr;
		NodeList nList = null;
		try {
			TestReporter.logTrace("Checking validity of xpath");
			expr = xPath.compile(xpath);
			nList = (NodeList) expr.evaluate(nodeList, XPathConstants.NODESET);
		} catch (XPathExpressionException xpe) {
			throw new RuntimeException("Xpath evaluation failed with xpath [ " + xpath + " ] ", xpe.getCause());	
		}

		TestReporter.logTrace("XPath is valid. Checking for nodes with desired xpath");
		//Ensure an element was found, if not then throw error and fail
		if (nList.item(0) == null) throw new RuntimeException("No xpath was found with the path [ " + xpath + " ] ");

		TestReporter.logTrace("At least one node found on Xpath. Returning as list");
		TestReporter.logTrace("Exiting XMLTools#getNodeList");
		return nList;
	}
	
	/**
	 * Transform a XML Document to String
	 * @author Waightstill Avery
	 * @version Created: 08/28/2016
	 * @param nodeList Nodelist to look in
	 * @param xpath Xpath to search and return all nodes found
	 * @return Node found on xpath
	 */
	public static Node getNode(Node nodeList, String xpath) {
		TestReporter.logTrace("Entering XMLTools#getNode");
		XPathFactory xPathFactory = XPathFactory.newInstance();
		XPath xPath = xPathFactory.newXPath();
		XPathExpression expr;
		NodeList nList = null;
		try {
			TestReporter.logTrace("Checking validity of xpath");
			expr = xPath.compile(xpath);
			nList = (NodeList) expr.evaluate(nodeList, XPathConstants.NODESET);
		} catch (XPathExpressionException xpe) {
			throw new RuntimeException("Xpath evaluation failed with xpath [ " + xpath + " ] ", xpe.getCause());	
		}

		TestReporter.logTrace("XPath is valid. Checking for nodes with desired xpath");
		//Ensure an element was found, if not then throw error and fail
		if (nList.item(0) == null) throw new RuntimeException("No xpath was found with the path [ " + xpath + " ] ");
		TestReporter.logTrace("At least one node found on Xpath. Returning first node");
		TestReporter.logTrace("Exiting XMLTools#getNodeList");
		return nList.item(0);
	}
}