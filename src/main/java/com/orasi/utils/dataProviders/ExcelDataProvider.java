package com.orasi.utils.dataProviders;

import com.orasi.utils.ExcelDocumentReader;

public class ExcelDataProvider {
    private String filepath;
    private String sheetName;
    private int row;

    public ExcelDataProvider(String filepath, String sheetName) {
	this(filepath, sheetName, -1);
    }
    
    public ExcelDataProvider(String filepath, String sheetName, int rowToRead) {
	this.filepath = getClass().getResource(filepath).getPath();
	this.sheetName = sheetName;
	this.row = rowToRead;
    }
    
    public void setDatatablePath(String path) {
	this.filepath = path;
    }

    public void setDatatableSheet(String sheet) {
	this.sheetName = sheet;
    }

    public void setDatatableRow(int rowToRead) {
	this.row = rowToRead;
    }

    public Object[][] getTestData() {
	return new ExcelDocumentReader(this.filepath).readData(this.sheetName, this.row);
    }
}
