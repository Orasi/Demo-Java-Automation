package com.orasi.utils.dataProviders;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.orasi.exception.AutomationException;
import com.orasi.utils.TestReporter;

public class CSVDataProvider {

	/**
	 * This gets the test data from a csv file.  It returns all the data 
	 * as a 2d array
	 * 
	 * @param	filePath		the file path of the CSV file
	 * @version	12/18/2014
	 * @author 	Jessica Marshall
	 * @return 	2d array of test data
	 */
	public static Object[][] getTestScenarioData(String filePath){
		BufferedReader br = null;
		String line = "";
		String csvSplitBy = ",";
		String[][] dataArray = null;
		List<String>csvRowList = new ArrayList<String>();
		String[] rowSplit;
		int columnCount = 0;
		int rowCount = 0;
		
		// Get the file location from the project main/resources folder
		if(!filePath.contains(":")) filePath =  CSVDataProvider.class.getResource(filePath).getPath();

		// in case file path has a %20 for a whitespace, replace with actual
		// whitespace
		filePath = filePath.replace("%20", " ");
		
		
		//open the CSV and add each line into a list		
		try {
			br = new BufferedReader(new FileReader(filePath));
			while((line = br.readLine()) !=null) {
				csvRowList.add(line);
			}
			br.close();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
		//column count, split the first row and get the length 
		columnCount = csvRowList.get(0).split(csvSplitBy).length;
		
		//row count - use size of list -1, since the first row is the header
		rowCount = csvRowList.size()-1;
		
		//create size of array, 
		dataArray = new String[rowCount][columnCount];
		
		
		
		//transform the list into 2d array
		//start at row 1 since, first row 0 is column headings
		for (int rowNum = 1; rowNum <= rowCount; rowNum++) {
			
			//take the row which is a string, and split it
			rowSplit = csvRowList.get(rowNum).split(csvSplitBy);
			
			for (int colNum = 0; colNum < columnCount; colNum++ ) {
				dataArray[rowNum-1][colNum] = rowSplit[colNum];
				//System.out.println(dataArray[rowNum-1][colNum]);
			}
			//System.out.println("");
		}
		
		return dataArray;
	}
	public static Object[][] getData(String filePath){
		BufferedReader bufferedReader = null;
		FileReader fileReader = null;
		String line = "";
		String csvSplitBy = ",";
		String[][] dataArray = null;
		List<String>csvRowList = new ArrayList<String>();
		String[] rowSplit;
		int columnCount = 0;
		int rowCount = 0;
		String delimiter=",";
		// Get the file location from the project main/resources folder
		if(!filePath.contains(":")) filePath =  CSVDataProvider.class.getResource(filePath).getPath();

		// in case file path has a %20 for a whitespace, replace with actual
		// whitespace
		filePath = filePath.replace("%20", " ");
		
		TestReporter.logTrace("File path of CSV to open [ " + filePath + " ]");
		//open the CSV and add each line into a list		
		try {
			TestReporter.logTrace("Loading file into FileReader");
			fileReader = new FileReader(filePath);
			TestReporter.logTrace("Successfully loaded file into FileReader");
			
			TestReporter.logTrace("Loading FileReader object into BufferedReader");
			bufferedReader = new BufferedReader(fileReader);
			TestReporter.logTrace("Successfully loaded FileReader object into BufferedReader");
			
			TestReporter.logTrace("Read in file and load each line into a List");
			while((line = bufferedReader.readLine()) !=null) {
				csvRowList.add(line);
			}
			TestReporter.logTrace("Successfully read in [ " + csvRowList.size() + " ] lines from file");
		} catch (IOException e) {
			throw new AutomationException("Failed to read in CSV file", e);
		}finally{
			try {
				TestReporter.logTrace("Closing BufferedReader");
				bufferedReader.close();
				TestReporter.logTrace("Successfully closed BufferedReader");

				TestReporter.logTrace("Closing FileReader");
				fileReader.close();
				TestReporter.logTrace("Successfully closed FileReader");
			} catch (IOException throwAway) {}
		}
		
		TestReporter.logTrace("Determining column count based on delimiter [ " + delimiter + " ]");
		columnCount = csvRowList.get(0).split(delimiter).length;
		TestReporter.logTrace("Found [ " + columnCount + " ] columns");

		TestReporter.logTrace("Determining row count");
		rowCount = csvRowList.size();
		TestReporter.logTrace("Found [ " + rowCount + " ] rows");
		
		TestReporter.logTrace("Attempting to create an array based on rows and columns. Array to built [" + rowCount + "][" + columnCount + "]");
		dataArray = new String[rowCount][columnCount]; 
		
		//transform the list into 2d array
		//start at row 1 since, first row 0 is column headings
		TestReporter.logTrace("Transferring data to Array");
		for (int rowNum = 0; rowNum <= rowCount-1; rowNum++) {
			
			//take the row which is a string, and split it
			rowSplit = csvRowList.get(rowNum).split(delimiter);
			
			for (int colNum = 0; colNum < columnCount; colNum++ ) {
				dataArray[rowNum][colNum] = rowSplit[colNum];			
			}
		}
		
		TestReporter.logTrace("Exiting CSVDataProvider#getTestScenarioData");
		return dataArray;	
	}
}
