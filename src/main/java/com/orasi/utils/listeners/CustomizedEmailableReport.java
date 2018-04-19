package com.orasi.utils.listeners;

import org.testng.*;
import org.testng.xml.XmlSuite;

import com.orasi.AutomationException;
import com.orasi.utils.JavaUtilities;
import com.orasi.utils.date.DateTimeConversion;
import com.orasi.utils.date.SimpleDate;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class CustomizedEmailableReport implements IReporter {
	
	//Formatter for the Suite detail table rows
	private static final String DETAIL_ROW_TEMPLATE = "<tr class=\"%s\"><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td></tr>";
	//Formatter for the suite summary table rows
	private static final String SUMMARY_ROW_TEMPLATE = "<tr><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td></tr>";
	//Formatter for the suite summary total table row
	private static final String SUMMARY_TOTAL_ROW_TEMPLATE = "<tr class= \"lead\" style=\"background-color:LightGrey\"><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td></tr>";
	//constants for template report & finalized report
	private static final String FINISHED_REPORT_NAME = "automation-email-report";
	private static final String REPORT_TEMPLATE_NAME_PATH = "src/main/resources/html/reportTemplate.html";
	
	//Lists to hold the table rows
	private List<String> suiteSummaryRows = new ArrayList<String>();
	private List<String> suiteDetailRows = new ArrayList<String>();

	/**
	 * Overriden method from IReporter class
	 * generates the custom report
	 * @author Jessica Marshall
	 * @date 9/21/2017
	 */
	@Override
	public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {
		//Get the initial html template
		String reportTemplate = initReportTemplate();
		String htmlString;
		
		//Get all the summary section and detail section details for reporting
		getTestSuiteDetails(suites);
		
		//get the suite summary rows list and combine as a string
		htmlString = suiteSummaryRows.stream().collect(Collectors.joining());
		//add the table rows to the summary table in html report
		reportTemplate = reportTemplate.replaceFirst("<tbody id=\"suiteSummaryTable\">", String.format("%s</tbody>", htmlString));
		//get the suite detail rows list and combine as a string
		htmlString = suiteDetailRows.stream().collect(Collectors.joining());
		//add the table rows to the detail table in html report
		reportTemplate = reportTemplate.replaceFirst("<tbody id=\"suiteDetailTable\">", String.format("%s</tbody>", htmlString));
		//Add Date & time to report
		reportTemplate = reportTemplate.replaceFirst("<h2 id=\"dateTimeStamp\">", "<h2 id=\"dateTimeStamp\">" + DateTimeConversion.convert(SimpleDate.getTimestamp(),"MM/dd/yyyy hh:mm aa"));
		//Update the report template and publish the finalized report in path specified
		updateReportTemplate(outputDirectory, reportTemplate);
	}
	
	/**
	 * Uses the Isuite class to get a mapping of all tests in each suite for totals, 
	 * test name, test results, failure reasons, etc and add to a list of rows to
	 * add to each table (Test execution summary & test execution details)
	 * @author Jessica Marshall
	 * @date 9/25/2017
	 * @param suites
	 */
	private void getTestSuiteDetails (List<ISuite> suites) {

		//summary row
	    float totalTimeInSecs = 0;
		float totalTimeInMS = 0;
	    int numOfTests = 0;
  	  	int numOfSkips = 0;
  	  	int numOfPasses = 0;
  	  	int numOfFailures = 0;
  	  	String testngXMLTestName;
  	  	
  	  	//totals row
  	  	int totalNumOfTests = 0;
  	  	int totalNumOfPasses = 0;
  	  	int totalNumOfFailures = 0;
  	  	int totalNumOfSkips = 0;
  	  	float totalRunTime = 0;
  	  	
  	  	//Start with each suite in testng xml
		for (ISuite suite : suites) {
		      //create map of all testng xml tests
		      Map<String, ISuiteResult> tests = suite.getResults();
		      for (ISuiteResult r : tests.values()) {
		    	  //get all the results of tests
		    	  ITestContext overview = r.getTestContext();
		    	  //Get the testng xml test name 
		    	  testngXMLTestName = overview.getName();
		    	  //get a mapping of each grouping of test - failed, passed & skipped
		    	  Set<ITestResult> failedTests = overview.getFailedTests().getAllResults();
		    	  Set<ITestResult> passedTests = overview.getPassedTests().getAllResults();
		    	  Set<ITestResult> skippedTests = overview.getSkippedTests().getAllResults();
		    	  
		    	  //Get the number of all tests in group
		    	  numOfTests = overview.getAllTestMethods().length;
		    	  //Get number of each grouping
		    	  numOfFailures = failedTests.size();
		    	  numOfSkips = skippedTests.size();
		    	  numOfPasses = passedTests.size();
		          //get the run time of all tests in this group
		          totalTimeInMS = overview.getEndDate().getTime() - overview.getStartDate().getTime();
		          //convert to seconds
		          totalTimeInSecs = totalTimeInMS/1000;
		          
		          //totals row - add to the totals for all test groupings
		          totalNumOfTests += numOfTests;
		          totalNumOfPasses += numOfPasses;
		          totalNumOfFailures += numOfFailures;
		          totalNumOfSkips += numOfSkips;
		          totalRunTime += totalTimeInMS;
		          
		          //add the values to a formatted html row for summary table
		    	  suiteSummaryRows.add(String.format(SUMMARY_ROW_TEMPLATE, testngXMLTestName, numOfTests, 
		    			  numOfPasses, numOfSkips, numOfFailures, String.format("%.3f sec", totalTimeInSecs )));

		    	  //generate a detailed result row for the Test execution details section for each of failed tests, passed test, skipped tests in each of
		    	  //the testng test groups
		    	  generateDetailRow(failedTests, testngXMLTestName);

		    	  generateDetailRow(passedTests, testngXMLTestName);

		    	  generateDetailRow(skippedTests, testngXMLTestName);
		      }
		}
		//get the total Run time of all tests in all suites in seconds
		float totalNumOfSecs = totalRunTime/1000;
		//Add the totals for all tests in all suites to the bottom of the Test execution summary table
		suiteSummaryRows.add(String.format(SUMMARY_TOTAL_ROW_TEMPLATE, "TOTALS",totalNumOfTests, totalNumOfPasses, 
				totalNumOfSkips, totalNumOfFailures, String.format("%.3f sec", totalNumOfSecs )));
	      
	}
	
	/**
	 * This takes a mapping of ITestResult (failed, skipped, or passed tests) so that we can output detailed info
	 * about each of the test methods in that result such as test method name, test class that the method belongs to,
	 * failure reason, etc.  Adds to a list of the detailed table row for outputting to html 
	 * @param tests
	 * @param suiteName
	 * @date 9/25/2017
	 * @author Jessica Marshall
	 */
	private void generateDetailRow (Set<ITestResult> tests, String suiteName) {
		float totalTimeInSecs = 0;
		float totalTimeInMS = 0;
		String testClassName;
		String testMethodName;
		String failureReason;
		//Create an iterator for all of the test methods in the ITestResult grouping
		Iterator<ITestResult> iterator = tests.iterator();
		//Go through all the instances in the iterator
		while(iterator.hasNext()) {
			//move to next one
			ITestResult testElement = iterator.next();
			//Get the test class name for the method
			testClassName = testElement.getTestClass().getName();
			//Get the test method name
			testMethodName = testElement.getName();
			//get run time in ms for test method
			totalTimeInMS = testElement.getEndMillis()-testElement.getStartMillis();
			//convert to seconds
			totalTimeInSecs = totalTimeInMS/1000;
			//Based on the type of result (failure, pass, or skip), format those values differently
			switch (testElement.getStatus()) {
				case ITestResult.FAILURE:
					//Get the assertion or stack trace message if not null
					if (JavaUtilities.isValid(testElement.getThrowable())) {
						failureReason = testElement.getThrowable().getMessage();
					} else {
						failureReason = "NA";
					}
					//Format the detail row using the danger class from bootstrap and add values
					suiteDetailRows.add(String.format(DETAIL_ROW_TEMPLATE, "danger", suiteName, testClassName, testMethodName, "FAILED", 
							failureReason, String.format("%.3f sec", totalTimeInSecs )));
					break;
				case ITestResult.SUCCESS:
					//Format the detail row using the success class from bootstrap and add values
					suiteDetailRows.add(String.format(DETAIL_ROW_TEMPLATE, "success", suiteName, testClassName, testMethodName, "PASSED", 
							"NA", String.format("%.3f sec", totalTimeInSecs ) ));
					break;
                case ITestResult.SKIP:
                	//Format the detail row using the warning class from bootsrap and add values
                	suiteDetailRows.add(String.format(DETAIL_ROW_TEMPLATE, "warning", suiteName, testClassName, testMethodName, "SKIPPED", "NA","NA"));
                	break;
			}
		}
	}
	
	/**
	 * Grab the templated HTML report from the project and convert to a string so that the dynamic
	 * test results can be added
	 * @author Jessica Marshall
	 * @date 9/25/2017
	 * @return
	 */
	private String initReportTemplate() {
		String template = null;
		byte[] reportTemplate;
		try {
			reportTemplate = Files.readAllBytes(Paths.get(REPORT_TEMPLATE_NAME_PATH));
			template = new String(reportTemplate, "UTF-8");
		} catch (IOException e) {
			throw new AutomationException("Could not find the report template file to convert to string.  Error: " + e.getMessage());
		}
		return template;
	}

	/**
	 * Updates the report template with updated version of report template with dynamic testng results.
	 * The name of the finished html report is 'automation-email-report.html'
	 * @author Jessica Marshall
	 * @date 9/25/2017
	 * @param outputDirectory
	 * @param reportTemplate
	 */
	private void updateReportTemplate(String outputDirectory, String reportTemplate) {
		new File(outputDirectory).mkdirs();
		try (PrintWriter reportWriter = new PrintWriter(new BufferedWriter(new FileWriter(new File(outputDirectory, FINISHED_REPORT_NAME + ".html"))), true)){            
            reportWriter.println(reportTemplate);
        } catch (IOException e) {
            throw new AutomationException("Could not write to report template.  Error: " + e.getMessage());
        }
	}

}
