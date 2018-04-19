package com.orasi.utils;

import static com.orasi.utils.TestReporter.logDebug;
import static com.orasi.utils.TestReporter.logTrace;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
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

import com.orasi.AutomationException;
import com.orasi.api.soapServices.exceptions.MissingFunctionParameterValueException;
import com.orasi.api.soapServices.exceptions.SoapException;
import com.orasi.utils.exception.XMLTransformException;
import com.orasi.utils.exception.XPathInvalidExpression;
import com.orasi.utils.exception.XPathNotFoundException;

public class XMLTools {

    /**
     * Adds an Attribute to the node on the location of the xpath
     *
     * @author Justin Phlegar
     * @version Created: 08/28/2014
     * @param doc
     *            Document: XML Document that will be updated
     * @param nodeName
     *            String: Name of the attribute to add to the node
     * @param xpath
     *            String: Path of the Node in the XML to add the Attribute
     */
    public static Document addAttribute(Document doc, String attributeName, String xpath) {
        logTrace("Entering XMLTools#addAttribute");
        logDebug("Adding Attribute [ " + attributeName + " ] to XPath [ " + xpath + " ]");
        XPathFactory xPathFactory = XPathFactory.newInstance();
        XPath xPath = xPathFactory.newXPath();
        XPathExpression expr;
        NodeList nList = null;
        try {
            logTrace("Checking validity of xpath");
            expr = xPath.compile(xpath);
            nList = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
        } catch (XPathExpressionException xpe) {
            throw new XPathInvalidExpression("Xpath evaluation failed with xpath [ " + xpath + " ] ", xpe);
        }

        logTrace("XPath is valid. Checking for nodes with desired xpath");
        // Ensure an element was found, if not then throw error and fail
        if (nList.item(0) == null) {
            throw new XPathNotFoundException("No xpath was found with the path [ " + xpath + " ] ");
        }

        // Create new XML document based on XPath
        Element element = (Element) nList.item(0);
        element.setAttribute(attributeName, "");

        logTrace("At least one node found on Xpath. Adding Attribute to Node");
        logTrace("Successfully added Attribute [ " + attributeName + " ]");
        logTrace("Exiting XMLTools#addAttribute");
        return doc;
    }

    /**
     * Removes an Attribute to the node on the location of the xpath
     *
     * @author Justin Phlegar
     * @version Created: 08/28/2014
     * @param doc
     *            Document: XML Document that will be updated
     * @param nodeName
     *            String: Name of the attribute to remove to the node
     * @param xpath
     *            String: Path of the Node in the XML to add the Attribute
     */
    public static Document removeAttribute(Document doc, String attributeName, String xpath) {
        logTrace("Entering XMLTools#removeAttribute");
        logDebug("Removing Attribute [ " + attributeName + " ] to XPath [ " + xpath + " ]");
        XPathFactory xPathFactory = XPathFactory.newInstance();
        XPath xPath = xPathFactory.newXPath();
        XPathExpression expr;
        NodeList nList = null;
        try {
            logTrace("Checking validity of xpath");
            expr = xPath.compile(xpath);
            nList = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
        } catch (XPathExpressionException xpe) {
            throw new XPathInvalidExpression("Xpath evaluation failed with xpath [ " + xpath + " ] ", xpe);
        }

        logTrace("XPath is valid. Checking for nodes with desired xpath");
        // Ensure an element was found, if not then throw error and fail
        if (nList.item(0) == null) {
            throw new XPathNotFoundException("No xpath was found with the path [ " + xpath + " ] ");
        }

        logTrace("At least one node found on Xpath. Removing Attribute from Node");
        // Create new XML document based on XPath
        Element element = (Element) nList.item(0);
        element.removeAttribute(attributeName);

        logTrace("Successfully removed Attribute [ " + attributeName + " ]");
        logTrace("Exiting XMLTools#removeAttribute");
        return doc;
    }

    /**
     * Adds an Namespace to the node on the location of the xpath
     *
     * @author Justin Phlegar
     * @version Created: 08/28/2014
     * @param doc
     *            Document: XML Document that will be updated
     * @param namespace
     *            String: Name of the namespace to add to the node
     * @param xpath
     *            String: Path of the Node in the XML to add the Namespace
     */
    public static Document addNamespace(Document doc, String namespace, String xpath) {
        logTrace("Entering XMLTools#addNamespace");
        logDebug("Adding Namespace [ " + namespace + " ] to XPath [ " + xpath + " ]");
        XPathFactory xPathFactory = XPathFactory.newInstance();
        XPath xPath = xPathFactory.newXPath();
        XPathExpression expr;
        NodeList nList = null;
        String[] values = namespace.split(",");
        if (values.length == 1) {
            throw new MissingFunctionParameterValueException("Missing expected parameters: Expected format [ fx:addnamespace ; xmlns:name,url] where name is the name of the namespace to add and url is the URL of the namespace");
        }
        String namespaceName = values[0];
        String namespaceURL = values[1];
        try {
            logTrace("Checking validity of xpath");
            expr = xPath.compile(xpath);
            nList = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
        } catch (XPathExpressionException xpe) {
            throw new XPathInvalidExpression("Xpath evaluation failed with xpath [ " + xpath + " ] ", xpe);
        }

        logTrace("XPath is valid. Checking for nodes with desired xpath");
        // Ensure an element was found, if not then throw error and fail
        if (nList.item(0) == null) {
            throw new XPathNotFoundException("No xpath was found with the path [ " + xpath + " ] ");
        }

        logTrace("At least one node found on Xpath. Adding Namespace on Xpath");
        // Create new XML document based on XPath
        Element element = (Element) nList.item(0);
        element.setAttributeNS("http://www.w3.org/2000/xmlns/", namespaceName, namespaceURL);

        logTrace("Successfully added namespace [ " + namespace + " ]");
        logTrace("Exiting XMLTools#addNamespace");
        return doc;
    }

    /**
     * Takes an xpath and adds a node to the location of the xpath and name of tagName
     *
     * @author Justin Phlegar
     * @version Created: 08/28/2014
     * @param doc
     *            Document: XML Document that will be updated
     * @param nodeName
     *            String: Name of the node to add to the XML Document
     * @param xpath
     *            String: Path in the XML to add the node
     */
    public static Document addNode(Document doc, String nodeName, String xpath) {
        logTrace("Entering XMLTools#addNode");
        logDebug("Adding Node [ " + nodeName + " ] on XPath [ " + xpath + " ]");
        XPathFactory xPathFactory = XPathFactory.newInstance();
        XPath xPath = xPathFactory.newXPath();
        XPathExpression expr;
        NodeList nList = null;

        try {
            logTrace("Checking validity of xpath");
            expr = xPath.compile(xpath);
            nList = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
        } catch (XPathExpressionException xpe) {
            throw new XPathInvalidExpression("Xpath evaluation failed with xpath [ " + xpath + " ] ", xpe);
        }

        logTrace("XPath is valid. Checking for nodes with desired xpath");
        // Ensure an element was found, if not then throw error and fail
        if (nList.item(0) == null) {
            throw new XPathNotFoundException("No xpath was found with the path [ " + xpath + " ] ");
        }

        logTrace("At least one node found on Xpath. Adding node to Document on Xpath");
        // Create new XML document based on XPath
        Document dom = nList.item(0).getOwnerDocument();

        // Create a new Node with the given tag name
        Node node = dom.createElement(nodeName);

        // Add the new node structure to the previous parent node
        nList.item(0).appendChild(node);

        logTrace("Successfully added node [ " + nodeName + " ]");
        logTrace("Exiting XMLTools#addNode");
        return doc;
    }

    /**
     * Takes an xpath and removes a node to the location of the xpath
     *
     * @author Justin Phlegar
     * @version Created: 08/28/2014
     * @param doc
     *            Document: XML Document that will be updated
     * @param xpath
     *            String: Path in the XML to add the node
     * @return Document xml with removed Node
     */
    public static Document removeNode(Document doc, String xpath) {
        logTrace("Entering XMLTools#removeNode");
        XPathFactory xPathFactory = XPathFactory.newInstance();
        XPath xPath = xPathFactory.newXPath();
        XPathExpression expr;
        NodeList nList = null;

        logDebug("Remove node from xpath [ " + xpath + " ]");
        try {
            logTrace("Checking validity of xpath");
            expr = xPath.compile(xpath);
            nList = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
        } catch (XPathExpressionException e1) {
            throw new XPathInvalidExpression("Xpath evaluation failed with xpath [ " + xpath + " ] ");
        }

        if (nList.item(0) == null) {
            throw new XPathNotFoundException(xpath);
        }
        logTrace("XPath is valid. Removing node");
        Element element = (Element) nList.item(0);
        element.getParentNode().removeChild(element);

        logTrace("Node removal successful. Normalizing document");
        doc.normalize();

        logTrace("Exiting XMLTools#removeNode");
        return doc;
    }

    /**
     * Takes an xpath and return the value found
     *
     * @author Justin Phlegar
     * @version Created: 08/28/2014 *
     * @param doc
     *            Document: XML Document that will be queried
     * @param xpath
     *            String: xpath to evaluate
     */
    public static String getValueByXpath(Document doc, String xpath) {
        logTrace("Entering XMLTools#getValueByXpath");
        logDebug("Get value from XPath [ " + xpath + " ]");
        XPathFactory xPathFactory = XPathFactory.newInstance();
        XPath xPath = xPathFactory.newXPath();
        XPathExpression expr;
        NodeList nList = null;

        // Evaluate the xpath
        try {
            logTrace("Checking validity of xpath");
            expr = xPath.compile(xpath);
            nList = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
        } catch (XPathExpressionException xpe) {
            throw new XPathInvalidExpression("Xpath evaluation failed with xpath [ " + xpath + " ] ", xpe);
        }

        logTrace("XPath is valid. Checking for nodes with desired xpath");
        // Ensure an element was found, if not then throw error and fail
        if (nList.item(0) == null) {
            throw new XPathNotFoundException(xpath);
        }

        logTrace("At least one node found on Xpath. Returning data in Node");
        logTrace("Exiting XMLTools#getValueByXpath");
        // If no errors, then return the value found
        return nList.item(0).getTextContent();
    }

    /**
     * Generate an XML Document from SOAPMessage
     *
     * @author Justin Phlegar
     * @version Created: 08/28/2014
     * @param soapXML
     *            SOAPMessage to transform to XML
     * @return Document xml of SOAPMessage
     */
    public static Document makeXMLDocument(SOAPMessage soapXML) {
        logTrace("Entering XMLTools#makeXMLDocument");
        logTrace("Creating Document factory");
        Document doc = null;

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();) {
            logTrace("Attempting to transform SoapMessage to XML");
            soapXML.writeTo(outputStream);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(false);
            factory.setIgnoringElementContentWhitespace(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            try (ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray())) {
                InputSource source = new InputSource(new ByteArrayInputStream(outputStream.toByteArray()));
                source.setEncoding("ISO-8859-1");
                doc = builder.parse(source);
                source = null;
            }
        } catch (ParserConfigurationException pce) {
            throw new AutomationException("Failed to create a Document Builder Factory", pce);
        } catch (SAXException saxe) {
            throw new AutomationException("Failed to parse the xml", saxe);
        } catch (IOException | SOAPException ioe) {
            throw new AutomationException("Failed to get source XML from Soap Message", ioe);
        }

        logTrace("Successfully transformed SoapMessage to XML. Normalize document");
        doc.getDocumentElement().normalize();
        logTrace("Exiting XMLTools#makeXMLDocument");
        return doc;
    }

    /**
     * Generate an XML Document from String
     *
     * @author Justin Phlegar
     * @version Created: 08/28/2014
     * @param xml
     *            String of XML to transform to XML Document
     * @return Document xml of String
     */
    public static Document makeXMLDocument(String xml) {
        logTrace("Entering XMLTools#makeXMLDocument");
        logTrace("Creating Document factory");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(false);
        factory.setIgnoringElementContentWhitespace(true);
        DocumentBuilder builder;
        Document doc = null;
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(xml.toString().getBytes());) {
            logTrace("Attempting to transform String to XML");
            builder = factory.newDocumentBuilder();
            InputSource source = new InputSource(inputStream);
            source.setEncoding("ISO-8859-1");
            doc = builder.parse(source);
            source = null;
        } catch (ParserConfigurationException pce) {
            throw new AutomationException("Failed to create a Document Builder Factory", pce);
        } catch (SAXException saxe) {
            throw new AutomationException("Failed to parse the xml", saxe);
        } catch (IOException ioe) {
            throw new AutomationException("Failed to find the source XML", ioe);
        }

        logTrace("Successfully transformed String to XML. Normalize document");
        doc.getDocumentElement().normalize();
        logTrace("Exiting XMLTools#makeXMLDocument");
        return doc;

    }

    /**
     * Generate an XML Document from String
     *
     * @author Justin Phlegar
     * @version Created: 08/28/2014
     * @param xml
     *            String of XML to transform to XML Document
     * @return Document xml of String
     */
    public static Document makeXMLDocument(File file) {
        logTrace("Entering XMLTools#makeXMLDocument");
        logTrace("Creating Document factory");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(false);
        factory.setIgnoringElementContentWhitespace(true);
        Document doc = null;
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            logTrace("Attempting to open file and save as to XML. File [ " + file.getPath() + " ]");
            doc = builder.parse(file);
        } catch (SAXException saxe) {
            throw new AutomationException("Failed to parse the xml", saxe);
        } catch (IOException ioe) {
            throw new AutomationException("Failed to find the source XML", ioe);
        } catch (ParserConfigurationException pce) {
            throw new AutomationException("Failed to create a Document Builder Factory", pce);
        }

        logTrace("Successfully transformed String to XML. Normalize document");
        doc.getDocumentElement().normalize();
        logTrace("Exiting XMLTools#makeXMLDocument");
        return doc;

    }

    /**
     * Transform a XML Document to String
     *
     * @author Justin Phlegar
     * @version Created: 08/28/2014
     * @param doc
     *            XML Document to transform to String
     * @return xml in String format
     */
    public static String transformXmlToString(Document doc) {
        logTrace("Entering XMLTools#transformXmlToString");

        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = null;
        logTrace("Starting XML to String transformer");
        try {
            transformer = tf.newTransformer();
        } catch (TransformerConfigurationException e) {
            throw new SoapException("Failed to create XML Transformer", e);
        }

        logTrace("Adding XML transformer properties");
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

        String xml;
        try (StringWriter sw = new StringWriter()) {

            logTrace("Attempting to transform XML to String ");
            transformer.transform(new DOMSource(doc), new StreamResult(sw));
            xml = sw.toString();
        } catch (TransformerException | IOException e) {
            logTrace("Failed to transform XML to String ");
            throw new XMLTransformException(
                    "Failed to transform Request XML Document. Ensure XML Document has been successfully loaded.", e);
        }

        logTrace("Successfully transformed XML to String");
        logTrace("Exiting XMLTools#transformXmlToString");
        return xml;
    }

    /**
     * A recursive method that will iterate through all nodes and delete Comment nodes
     *
     * @author Justin Phlegar
     * @version Created: 08/28/2014
     * @param node
     *            XML document
     * @return self
     */
    public static Node removeComments(Node node) {
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
     *
     * @author Justin Phlegar
     * @version Created: 08/28/2014
     * @param doc
     *            XML Document
     * @return xml Document after updates
     */
    public static Document removeWhiteSpace(Document doc) {
        logTrace("Entering XMLTools#removeWhiteSpace");
        XPath xp = XPathFactory.newInstance().newXPath();
        NodeList nl = null;
        try {
            logTrace("Get Node list of all whitespace nodes");
            nl = (NodeList) xp.evaluate("//text()[normalize-space(.)='']", doc,
                    XPathConstants.NODESET);
        } catch (XPathExpressionException e) {
            throw new XPathNotFoundException("Xpath evaluation failed to normalize white space");
        }

        logTrace("Iterate through of all whitespace nodes and remove them from XML");
        for (int i = 0; i < nl.getLength(); ++i) {
            Node node = nl.item(i);
            node.getParentNode().removeChild(node);
        }

        logTrace("Removed all whitespace nodes");
        logTrace("Exiting XMLTools#removeWhiteSpace");
        return doc;
    }

    /**
     * Transform a XML Document to String
     *
     * @author Waightstill Avery
     * @version Created: 08/28/2016
     * @param doc
     *            XML Document
     * @param xpath
     *            Xpath to search and return all nodes found
     * @return Nodelist of all nodes found on xpath
     */
    public static NodeList getNodeList(Document doc, String xpath) {
        logTrace("Entering XMLTools#getNodeList");
        XPathFactory xPathFactory = XPathFactory.newInstance();
        XPath xPath = xPathFactory.newXPath();
        XPathExpression expr;
        NodeList nList = null;
        try {
            logTrace("Checking validity of xpath");
            expr = xPath.compile(xpath);
            nList = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
        } catch (XPathExpressionException xpe) {
            throw new XPathInvalidExpression("Xpath evaluation failed with xpath [ " + xpath + " ] ");
        }

        logTrace("XPath is valid. Checking for nodes with desired xpath");

        // Ensure an element was found, if not then throw error and fail
        if (nList.item(0) == null) {
            throw new XPathNotFoundException("No xpath was found with the path [ " + xpath + " ] ");
        }

        logTrace("At least one node found on Xpath. Returning as list");
        logTrace("Exiting XMLTools#getNodeList");
        return nList;
    }

    /**
     * Transform a XML Document to String
     *
     * @author Waightstill Avery
     * @version Created: 08/28/2016
     * @param nodeList
     *            Nodelist to look in
     * @param xpath
     *            Xpath to search and return all nodes found
     * @return Nodelist of all nodes found on xpath
     */
    public static NodeList getNodeList(Node nodeList, String xpath) {
        logTrace("Entering XMLTools#getNodeList");
        XPathFactory xPathFactory = XPathFactory.newInstance();
        XPath xPath = xPathFactory.newXPath();
        XPathExpression expr;
        NodeList nList = null;
        try {
            logTrace("Checking validity of xpath");
            expr = xPath.compile(xpath);
            nList = (NodeList) expr.evaluate(nodeList, XPathConstants.NODESET);
        } catch (XPathExpressionException xpe) {
            throw new XPathInvalidExpression("Xpath evaluation failed with xpath [ " + xpath + " ] ", xpe);
        }

        logTrace("XPath is valid. Checking for nodes with desired xpath");
        // Ensure an element was found, if not then throw error and fail
        if (nList.item(0) == null) {
            throw new XPathNotFoundException("No xpath was found with the path [ " + xpath + " ] ");
        }

        logTrace("At least one node found on Xpath. Returning as list");
        logTrace("Exiting XMLTools#getNodeList");
        return nList;
    }

    /**
     * Transform a XML Document to String
     *
     * @author Waightstill Avery
     * @version Created: 08/28/2016
     * @param nodeList
     *            Nodelist to look in
     * @param xpath
     *            Xpath to search and return all nodes found
     * @return Node found on xpath
     */
    public static Node getNode(Node nodeList, String xpath) {
        logTrace("Entering XMLTools#getNode");
        XPathFactory xPathFactory = XPathFactory.newInstance();
        XPath xPath = xPathFactory.newXPath();
        XPathExpression expr;
        NodeList nList = null;
        try {
            logTrace("Checking validity of xpath");
            expr = xPath.compile(xpath);
            nList = (NodeList) expr.evaluate(nodeList, XPathConstants.NODESET);
        } catch (XPathExpressionException xpe) {
            throw new XPathInvalidExpression("Xpath evaluation failed with xpath [ " + xpath + " ] ", xpe);
        }

        logTrace("XPath is valid. Checking for nodes with desired xpath");
        // Ensure an element was found, if not then throw error and fail
        if (nList.item(0) == null) {
            throw new XPathNotFoundException("No xpath was found with the path [ " + xpath + " ] ");
        }
        logTrace("At least one node found on Xpath. Returning first node");
        logTrace("Exiting XMLTools#getNodeList");
        return nList.item(0);
    }
}