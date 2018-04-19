package com.orasi.utils.dataProviders;

import java.net.URL;

import com.orasi.utils.exception.DataProviderInputFileNotFound;
import com.orasi.utils.io.ExcelDocumentReader;

public class ExcelDataProvider {
    private String filePath;
    private String sheetName;
    private int row;

    @Deprecated
    /**
     * Move to use static ExcelDataProvider.getTestData
     * 
     * @param filePath
     * @param sheetName
     */
    public ExcelDataProvider(String filePath, String sheetName) {
        this(filePath, sheetName, -1);
    }

    @Deprecated
    /**
     * Move to use static ExcelDataProvider.getTestData
     * 
     * @param filePath
     * @param sheetName
     */
    public ExcelDataProvider(String filePath, String sheetName, int rowToRead) {
        URL file = getClass().getResource(filePath);
        if (file == null) {
            throw new DataProviderInputFileNotFound("Failed to find a file in path [ " + filePath + " ]");
        }

        this.filePath = file.getPath();
        this.sheetName = sheetName;
        this.row = rowToRead;
    }

    @Deprecated
    /**
     * Move to use static ExcelDataProvider.getTestData
     * 
     * @param filePath
     * @param sheetName
     */
    public Object[][] getTestData() {
        return ExcelDocumentReader.readData(this.filePath, this.sheetName, this.row);
    }

    public static Object[][] getTestData(String filePath, String sheetName) {
        return getTestData(filePath, sheetName, -1);
    }

    public static Object[][] getTestData(String filePath, String sheetName, int rowToRead) {
        return ExcelDocumentReader.readData(filePath, sheetName, rowToRead);
    }
}
