package com.orasi.utils.dataProviders;

import static com.orasi.utils.TestReporter.logTrace;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.orasi.AutomationException;
import com.orasi.utils.exception.DataProviderInputFileNotFound;
import com.orasi.utils.io.FileLoader;

public class CSVDataProvider {
    private static String delimiter = ",";

    /**
     * This gets the test data from a csv file. It returns all the data
     * as a 2d array
     *
     * @param filePath
     *            the file path of the CSV file
     * @version 12/18/2014
     * @author Jessica Marshall
     * @return 2d array of test data
     */
    public static Object[][] getData(String filePath, boolean excludeHeaderRow) {
        logTrace("Entering CSVDataProvider#getData");
        String line = "";
        String[][] dataArray = null;
        List<String> csvRowList = new ArrayList<String>();
        String[] rowSplit;
        int columnCount = 0;
        int rowCount = 0;

        // Get the file location from the project main/resources folder
        if (!(filePath.contains(":") || filePath.startsWith("/"))) {
            URL file = CSVDataProvider.class.getResource(filePath);
            if (file == null) {
                throw new DataProviderInputFileNotFound("No file was found on path [ " + filePath + " ]");
            }
            filePath = file.getPath();
        }

        // in case file path has a %20 for a whitespace, replace with actual
        // whitespace
        filePath = filePath.replace("%20", " ");

        logTrace("File path of CSV to open [ " + filePath + " ]");
        // open the CSV and add each line into a list
        try (BufferedReader bufferedReader = FileLoader.openTextFileFromProject(filePath)) {
            logTrace("Successfully loaded FileReader object into BufferedReader");

            logTrace("Read in file and load each line into a List");
            while ((line = bufferedReader.readLine()) != null) {
                csvRowList.add(line);
            }
            logTrace("Successfully read in [ " + csvRowList.size() + " ] lines from file");
        } catch (IOException e) {
            throw new AutomationException("Failed to read in CSV file", e);
        }

        if (excludeHeaderRow) {
            // Remove first line of headers
            csvRowList.remove(0);
        }

        logTrace("Determining column count based on delimiter [ " + delimiter + " ]");
        columnCount = csvRowList.get(0).split(delimiter).length;
        logTrace("Found [ " + (columnCount + 1) + " ] columns");

        logTrace("Determining row count");
        rowCount = csvRowList.size();
        logTrace("Found [ " + (rowCount + 1) + " ] rows");

        logTrace("Attempting to create an array based on rows and columns. Array to built [" + (rowCount + 1) + "][" + (columnCount) + "]");
        dataArray = new String[rowCount][columnCount];

        // transform the list into 2d array
        // start at row 1 since, first row 0 is column headings
        logTrace("Transferring data to Array");
        for (int rowNum = 0; rowNum <= rowCount - 1; rowNum++) {

            // take the row which is a string, and split it
            rowSplit = csvRowList.get(rowNum).split(delimiter);

            for (int colNum = 0; colNum < columnCount; colNum++) {
                try {
                    dataArray[rowNum][colNum] = rowSplit[colNum];
                } catch (ArrayIndexOutOfBoundsException e) {
                    dataArray[rowNum][colNum] = "";
                }
            }
        }

        logTrace("Exiting CSVDataProvider#getData");
        return dataArray;
    }

    public static Object[][] getData(String filePath, String delimiterValue, boolean excludeHeaderRow) {
        logTrace("Overriding default delimiter of [ , ] to be [ " + delimiter + " ]");
        delimiter = delimiterValue;
        return getData(filePath, excludeHeaderRow);
    }

    public static Object[][] getData(String filePath, String delimiterValue) {
        logTrace("Overriding default delimiter of [ , ] to be [ " + delimiter + " ]");
        delimiter = delimiterValue;
        return getData(filePath, true);
    }

    public static Object[][] getData(String filePath) {
        return getData(filePath, true);
    }
}
