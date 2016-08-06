## BlueSource Selenium-Java Project with Feature Reporting

This is the homepage for the Orasi-developed Selenium-Java Bluesource project. These libraries contain both original code created by Orasi developers and open source code from various other projects which are listed further down. With these libraries, consumers have access to extended functionality for creating testing suites for Web Applications and API Web Services.

## Orasi Software Inc
Orasi is a software and professional services company focused on software quality testing and management.  As an organization, we are dedicated to best-in-class QA tools, practices and processes. We are agile and drive continuous improvement with our customers and within our own business.

## License
Licensed under [BSD License](https://github.com/Orasi/java-automation-bs/blob/master/License)

## Web Application Testing

The core code uses the Java-based Selenium webdriver for GUI testing. It takes the standard Selenium-defined WebElement and divides them into more consumer-friendly Elements, whose names are commonly found in HTML DOMs. The following are Elements, and the prefixes used in the code to identify them, as well as examples using the naming conventions:

|Element|Prefix|Example|
|-------|------|-------|
|Button|btn|btnContinue|
|Checkbox|chk|chkAgreeToTermsAndConditions|
|Label|lbl|lblHeader|
|Link|lnk|lnkCancel|
|Listbox|lst|lstStates|
|Radiogroup|rad|radYesOrNo|
|Textbox|txt|txtUsername|
|Webtable|tab|tabMemberNames|
|Element (Generic Web Element)|ele|eleImage|

All elements utilize the Orasi-developed [TestReporter](https://github.com/Orasi/Selenium-Toyota-POC/blob/master/src/main/java/com/orasi/utils/TestReporter.java) which extends the [TestNG](https://github.com/cbeust/testng) Reporter by concatenating a timestamp and HTML formatting for use in viewing the report in a web browser. This allows for a functional audit which can ensure requirements are being met by the automated test, as well as provide steps to reproduce a defect when one occurs.

## API Web Service Testing

###SOAP Services 
 
These libraries contain a SOAP API testing solution which leverages Java and W3C functionalities to dynamically build requests at runtime, modify the requests, transmit and receive SOAP messages, and parse responses for field-level values with which validation can be performed. 

###REST Services  
  
These libraries contain a SOAP API testing solution which leverages the Apache HttpClient libraries. Full XML and JSON support is included to parse responses for field-level values with which validation can be performed. 

## Jenkins Build Tool
This project is designed to have a Jenkins CI instance hook into this repository and detect when changes are pushed to the Master branch. Ideally, this trigger will be changed to look for code changes pushed by developers, thereby triggering a new build to test the new application code.

## Sauce Labs Remote VM Farm

The capabilities of the Sauce Labs VM farm are harnessed to test the OS-Browser configurations required by the project. The Jenkins instance uses a local VM to spin up a webdriver for a test and the webdriver sends the commands to the remote VM to be executed.

## Reporting

* <strong>Test NG:</strong> Contains results for individual tests using the TestNG-extended Orasi reporter.

* <strong>Jenkins CI:</strong> Contains results for individual tests using the TestNG-extended Orasi reporter.  Also, keeps a build history to show the relative stability of past builds.

* <strong>Allure Reports:</strong> Contains results for individual tests using Allure Reporter. Reports generate can range from Automation issues, Application issues, grouping by Features and Stories as well is high level Steps giving high level Executive Reports.  

* <strong>Sauce Labs:</strong> Contains results for individual tests as well as captures videos and screenshots.

## [TestEnvironment.java](https://github.com/Orasi/Selenium-Toyota-POC/blob/master/src/main/java/com/orasi/utils/TestEnvironment.java)

This is a crucial class for this framework. This class is designed to be extended by page classes and implemented by test classes. It houses test environment data and remote WebDrivers as well as page class methods used to sync page behavior.  The need for this arose due to the parallel behavior that indicated that WebDriver instances were crossing threads and testing on the wrong os/browser configurations.

Also contained in this class is the determination as to whether the test should be executed locally or remotely based on a TestNG parameter. Once this is established, a webdriver is created and executed appropriately.

## Page Class

The Page Object Model was utilized for this core. With this framework, a single GUI web application page and all of the objects and interactions contained therein, are mapped to a single Java class. Selenium web elements for a particular page are redefined using the user-defined elements such as button, link , etc. Below is an example:

	@FindBy(xpath = "//*[@id=\"tcom-main-nav\"]/ul/li[3]/button")
	private Button btnYourLocation;

When a page class is instantiated, all *@FindBy* elements are initialized as proxies, or skeletons, which are not full objects that can be manipulated or interacted with, but do inherit all properties of a the object type (here, btnYourLocation inherits all methods and fields from the Button class). When an element is actually invoked

	btnYourLocation.click();
 
Selenium grabs the actual object to allow the test/user to interact with the element.

## Test Class

A flow through an web application is represented by a test class. Each test class is comprised of:

	* TestNG annotated methods (e.g. @BeforeMethod, @Test, @AfterMethod, etc.)
	* Creation of a DataProvider method to data-drive tests 	
	* Instantiation of the TestEnvironment class (i.e. create the web driver)
	* Instantiation of Page Class objects and associated methods to carry out the 
	  automated flow

## TestNG XMLs

Execution of tests is conducted through the TestNG framework which, given our Test Class framework, requires a TestNG XML.
An example XML follows:

	<?xml version="1.0" encoding="UTF-8"?>
	<!DOCTYPE suite SYSTEM "http://testng.org/testng-1.0.dtd">
	<suite name="Suite" parallel="tests" thread-count="8">
		<parameter name="browserUnderTest" value="firefox" />
		<parameter name="environment" value="staging" />
		<parameter name="runLocation" value="local" />
		<parameter name="browserVersion" value="" />
		<parameter name="operatingSystem" value="windows" />
		<test name="Test">
			<classes>
				<class name="com.orasi.bluesource.features.manageEmployees.AddEmployee" />
			</classes>
		</test> <!-- Test -->
	</suite> <!-- Suite -->

The various parts are described below:

|Attributes|Description|Acceptable Values|
|-----|-----------|-----------------|
|name="AddEmployee"|Contains the test suite name used by TestNG reporting|Any value that helps uniquely identify the suite|
|parallel="false"|Determines what, if anything, is to be executed in parallel| false, methods, tests, classes, instances|
|parameter name="browserUnderTest" value="chrome"|parameter and value used to create the driver; here, the browser type is defined|Sauce Labs: safari, chrome, firefox, iexplore|
|parameter name="environment" value=""|defines a particular environment to test|User-defined in [EnvironmentURLs.properties](https://github.com/Orasi/Java-Automation-Framework/blob/justin/src/test/resources/EnvironmentURLs.properties) file|
|parameter name="runLocation" value="local"|defines the run location|local or remote|
|parameter name="browserVersion" value=""|defines the browser version to use|Any value available is valid; with Sauce Labs, a blank value will trigger the latest version to be used|
|parameter name="operatingSystem" value="windows"|defines the operating system to use|Sauce Labs: MAC OS X 10.10, MAC OS X 10.9, Windows 7, Windows 8.1|
|test name="AddEmployee"|Contains the test name used by TestNG reporting|Any value that helps uniquely identify the test and is not used by another test in the same suite.|
|class name="com.orasi.bluesource.features.manageEmployees.AddEmployee"|Path of the class to be executed|User-defined and dependent of the build path|

## Third Party Resources
These resources are being used directly, or have been extended upon.
* [Selenium 2.43.1](https://github.com/SeleniumHQ/selenium): The base library that allows for automation of web browsers.
* [TestNG 6.8.21](https://github.com/cbeust/testng/): Test execution framework that extends JUnit tests and allows more flexibility for testing.
* [Smartbear SoapUI 4.5.0](https://github.com/SmartBear/soapui): Allows consumer to build requests files at runtime and sends request through HTTPClient
* [Sauce Labs 2.1.18](https://github.com/saucelabs/sauce-java): Facilitates the use of using the Sauce Lab VM farm as a remote Selenium grid on which to execute tests.
* [Apache HttpClient 4.3.1](https://hc.apache.org/httpcomponents-client-ga/httpclient/apidocs/index.html) Used to transmit and receive SOAP and REST message requests and responses.
* [Allure Reports for TestNG 1.4.13](http://allure.qatools.ru/): Generates high level executive reporting
