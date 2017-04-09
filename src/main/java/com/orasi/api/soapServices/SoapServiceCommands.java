package com.orasi.api.soapServices;

import com.orasi.utils.Randomness;

public class SoapServiceCommands {
	private final static String ADD_NODE = "fx:addnode;";
	private final static String ADD_NODES = "fx:addnodes;";
	private final static String ADD_ATTIRBUTE = "fx:addattribute;";
	private final static String ADD_NAMESPACE = "fx:addnamespace;";
	private final static String GET_DATE_TIME = "fx:getdatetime;";
	private final static String GET_DATE = "fx:getdate;";
	private final static String REMOVE_NODE = "fx:removenode";
	private final static String REMOVE_ATTRIBUTE = "fx:removeattribute";
	private final static String RANDOM_NUMBER = "fx:randomnumber;";
	private final static String RANDOM_STRING = "fx:randomstring;";
	private final static String RANDOM_ALPHANUMBERIC = "fx:randomalphanumeric;";	 
		  
	/**
	 * Build string command to add a new node. 
	 * @param nodeName : Name of the node to create
	 * @return String command to create node
	 */
	public static String addNode(String nodeName){
		return ADD_NODE + nodeName;
	}

	/**
	 * Build string command to add many nested nodes 
	 * @param nodeList : Name of the nodes to create in a parent/child/grandchild entity 
	 * <br> Example:  nodeList = blah/blah2/blah3 will create the following XML 
	 * <br>&lt;blah&gt;
	 * <br>&nbsp;&nbsp;&lt;blah2&gt;
	 * <br>&nbsp;&nbsp;&nbsp;&nbsp;&lt;blah3/&gt;
	 * <br>&nbsp;&nbsp;&lt;/blah2&gt;
	 * <br>&lt;/blah&gt;
	 * @return String command to create nested nodes
	 */
	public static String addNodes(String nodeList){
		return ADD_NODES + nodeList;
	}
	
	/**
	 * Build string command to add a new attribute. 
	 * @param attributeName : Name of the attribute to create
	 * @return String command to create attribute
	 */
	public static String addAttribute(String attributeName){
		return ADD_ATTIRBUTE + attributeName;
	}
	
	/**
	 * Build string command to add a new namespace. 
	 * @param namespaceName : Name of the namespace to create
	 * @return String command to create namespace
	 */
	public static String addNamespace(String namespaceName){
		return ADD_NAMESPACE + namespaceName;
	}
	
	/**
	 * Build string command to get current datetime stamp
	 * @return String command to get current datetime stamp
	 */
	public static String getDateTime(){
		return GET_DATE_TIME + "0";
	}
	
	/**
	 * Build string command to get current date
	 * @return String command to get current date
	 */
	public static String getDate(){
		return GET_DATE + "0";
	}
	
	/**
	 * Build string command to get datetime stamp after calculating date from daysOut
	 * @param daysOut Days from current date. Example: 3 will be three days from today, -7 will be ago
	 * @return String command to get datetime stamp after calculating date from daysOut
	 */
	public static String getDateTime(String daysOut){
		return GET_DATE_TIME + daysOut;
	}
	
	/**
	 * Build string command to get date after calculating date from daysOut
	 * @param daysOut Days from current date. Example: 3 will be three days from today, -7 will be ago
	 * @return String command to get date after calculating date from daysOut
	 */
	public static String getDate(String daysOut){
		return GET_DATE + daysOut;
	}
	
	/**
	 * Build string command to remove a node. 
	 * @return String command to remove node
	 */
	public static String removeNode(){
		return REMOVE_NODE;
	}

	/**
	 * Build string command to remove a attribute. 
	 * @return String command to remove attribute
	 */
	public static String removeAttribute(){
		return REMOVE_ATTRIBUTE;
	}
	
	/**
	 * Build string command to get a random number
	 * @return String command to get a random number
	 */
	public static String getRandomNumber(){
		return RANDOM_NUMBER + Randomness.randomNumberBetween(3, 9);
	}
	
	/**
	 * Build string command to get a random number with a specific length
	 * @param length length of random string to make
	 * @return String command to get a random number with a specific length
	 */
	public static String getRandomNumber(String length){
		return RANDOM_NUMBER + length;
	}
	
	/**
	 * Build string command to get a random string
	 * @return String command to get a random string
	 */
	public static String getRandomString(){
		return RANDOM_STRING + Randomness.randomNumberBetween(3, 9);
	}
	
	/**
	 * Build string command to get a random string with a specific length
	 * @param length length of random string to make
	 * @return String command to get a random string with a specific length
	 */
	public static String getRandomString(String length){
		return RANDOM_STRING + length;
	}
	
	/**
	 * Build string command to get a random alphanumberic string
	 * @return String command to get a random alphanumberic string
	 */
	public static String getRandomAlphaNumeric(){
		return RANDOM_ALPHANUMBERIC + Randomness.randomNumberBetween(3, 9);
	}
	/**
	 * Build string command to get a random alphanumberic string with a specific length
	 * @param length length of random string to make
	 * @return String command to get a random alphanumberic string with a specific length
	 */
	public static String getRandomAlphaNumeric(String length){
		return RANDOM_ALPHANUMBERIC + length;
	}
	
}