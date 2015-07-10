//Prepping for removal

/*package com.orasi.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

//import com.orasi.utils.dataFactory.ResortInfo.ResortColumns;
import com.orasi.utils.database.Recordset;

public class Datatable {


	private static Recordset virtualTable = null;
	private static String virtualTablePath;
	private static String virtualTablePage; 
	private static String virtualTableScenario;
	
	private static String datatablePath;

	private static XSSFSheet ExcelWSheet;
	private static XSSFWorkbook ExcelWBook;
	private static XSSFCell Cell;
	private static XSSFRow Row;

	private static String[] listScenarios;
	private static String currentScenario = "";
	private static int currentScenarioRow = 0;


	*//**
	 * ****
	 * Functionality for virtual tables as your datatable option 
	 * ****
	 * *//*
	
	private static Recordset getInfo(String table, String searchTest){
		return VirtualTable.compileJSON(virtualTablePath + table, new VirtualTable().getRows(virtualTablePath+ table, "Scenario", searchTest ));
	}
	
	public static void setVirtualtablePath(String path){
		virtualTablePath = path;
	}
	
	public static void setVirtualtablePage(String page){
		virtualTablePage = page;
	}
	
	public static void setVirtualtableScenario(String scenario){
		virtualTableScenario = scenario;
		virtualTable = getInfo(virtualTablePage, scenario);
	}
	
	public static String getDataParameter(String field) {
		return virtualTable.getValue(field);
	}
	
	public static Object[][] getTestScenarios(String sheetName) {
		Recordset rs = VirtualTable.compileXML("UI_TEST_SCENARIO_DATA_"+WebDriverSetup.getApplicationUnderTest().toUpperCase()+"_" + sheetName, new VirtualTable().getAllTestRows("UI_TEST_SCENARIO_DATA_LILO_"+ sheetName));
		rs.print();
		return removeRowsWithRowNumber(rs.getArray(), 0);
	}
	
	public static Object[][] getTestScenariosByApp(String application, String tableName) {
		if (application.isEmpty()) throw new RuntimeException("The Application is blank");
		if (tableName.isEmpty()) throw new RuntimeException("The Table name is blank");
		Recordset rs = VirtualTable.compileXML("UI_TEST_SCENARIO_DATA_"+application.toUpperCase()+"_" + tableName, new VirtualTable().getAllTestRows("UI_TEST_SCENARIO_DATA_"+application.toUpperCase()+"_" +  tableName));
		rs.print();
		return removeRowsWithRowNumber(rs.getArray(), 0);
	}
	public static Object[][] getEpicTestScenarios(String tableName) {
		if (tableName.isEmpty()) throw new RuntimeException("The Table name is blank");
		Recordset rs = VirtualTable.compileXML("UI_TEST_SCENARIO_DATA_EPIC"+"_" + tableName, new VirtualTable().getAllTestRows("UI_TEST_SCENARIO_DATA_EPIC"+"_" +  tableName));
		rs.print();
		return removeRowsWithRowNumber(rs.getArray(), 0);
	}
	private static  Object[][] removeRowsWithRowNumber(Object[][] array, double rowNotToBeAdd)
	    {
	        List<Object[]> rowsToKeep = new ArrayList<Object[]>(array.length);
	        for( int i =0; i<array.length; i++){
	            if(i!=rowNotToBeAdd){
	            	Object[] row = array[i];
	            rowsToKeep.add(row);
	            }
	        }

	        array= new Object[rowsToKeep.size()][];
	        for(int i=0; i < rowsToKeep.size(); i++)
	        {
	        	array[i] = rowsToKeep.get(i);
	        }
	        return array;
	    }

	public static String getDataParameter(String sheet, String scenario,
		String field) {
		setVirtualtablePage(sheet);
		setVirtualtableScenario(scenario);
		return virtualTable.getValue(field);
	}

	*//**
	 * ****
	 * End of virtual table 
	 * ****
	 * *//*
	
	public static void setDatatablePath(String path) {
		datatablePath = path;
	}




	private void setCurrentScenario(String scenario) {
		currentScenario = scenario;
	}

	private static void addScenarioList(String scenario) {
		if (listScenarios == null) {
			listScenarios = new String[1];
		} else {
			listScenarios = Arrays.copyOf(listScenarios,
					listScenarios.length + 1);
		}
		listScenarios[listScenarios.length - 1] = scenario;
	}
	
	public static int getCurrentScenarioRow(){

		return currentScenarioRow;
	}

	public static String getCurrentScenario() {
		String scenario = listScenarios[currentScenarioRow];
		currentScenarioRow++;
		return scenario;
	}

	public static Object[][] getTableArray(String SheetName, int iTestCaseRow) {
		String[][] tabArray = null;
		try {
			FileInputStream ExcelFile = new FileInputStream(datatablePath);
			// Access the required test data s*heet
			ExcelWBook = new XSSFWorkbook(ExcelFile);
			ExcelWSheet = ExcelWBook.getSheet(SheetName);

			int startCol = 1;
			int ci = 0, cj = 0;
			int totalRows = 1;
			int totalCols = ExcelWSheet.getRow(iTestCaseRow).getLastCellNum() - 1;
			tabArray = new String[totalRows][totalCols];
			for (int j = startCol; j <= totalCols; j++, cj++) {
				if (j == startCol)
					addScenarioList(getCellData(iTestCaseRow, j - 1));
				tabArray[ci][cj] = getCellData(iTestCaseRow, j);
				System.out.println(tabArray[ci][cj]);
			}

		} catch (FileNotFoundException e) {
			System.out.println("Could not read the Excel sheet");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Could not read the Excel sheet");
			e.printStackTrace();
		}
		return (tabArray);
	}

	public static Object[][] getTestScenarios(String FilePath, String SheetName) {

		String[][] tabArray = null;
		if(!FilePath.contains("Z:")){
			// Get the file location from the project main/resources folder
			FilePath =  Datatable.class.getResource(FilePath).getPath();
	
			// in case file path has a %20 for a whitespace, replace with actual
			// whitespace
			FilePath = FilePath.replace("%20", " ");
		}
		try {

			FileInputStream ExcelFile = new FileInputStream(FilePath);

			// Access the required test data sheet

			ExcelWBook = new XSSFWorkbook(ExcelFile);

			ExcelWSheet = ExcelWBook.getSheet(SheetName);

			int startRow = 1;

			int startCol = 1;

			int ci, cj;

			int totalRows = ExcelWSheet.getLastRowNum();

			// you can write a function as well to get Column count

			int totalCols = ExcelWSheet.getRow(startRow).getLastCellNum() - 1;

			tabArray = new String[totalRows][totalCols];

			ci = 0;

			for (int i = startRow; i <= totalRows; i++, ci++) {
				cj = 0;
				System.out.println("Test Scenario: " + getCellData(i, 1));
				for (int j = startCol; j <= totalCols; j++, cj++) {
					if (j == startCol)
						addScenarioList(getCellData(i, j - 1));
					tabArray[ci][cj] = getCellData(i, j);
					System.out.println(getCellData(0,j) + ": " + tabArray[ci][cj]);
				}
				System.out.println("");
			}
		} catch (FileNotFoundException e) {
			System.out.println("Could not read the Excel sheet");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Could not read the Excel sheet");
			e.printStackTrace();
		}
		return (tabArray);
	}

	public static Object[][] getTestScenarios(String FilePath,
			String SheetName, int rowToRun) {

		String[][] tabArray = null;

		try {

			FileInputStream ExcelFile = new FileInputStream(FilePath);

			// Access the required test data sheet

			ExcelWBook = new XSSFWorkbook(ExcelFile);

			ExcelWSheet = ExcelWBook.getSheet(SheetName);

			int startRow = 1;

			int startCol = 1;

			int ci, cj;

			// int totalRows = ExcelWSheet.getLastRowNum();

			// you can write a function as well to get Column count

			int totalCols = ExcelWSheet.getRow(startRow).getLastCellNum() - 1;

			tabArray = new String[1][totalCols];

			ci = 0;

			// for (int i=startRow;i<=totalRows;i++, ci++) {
			cj = 0;
			System.out.println("Test Scenario: " + getCellData(rowToRun, 1));
			for (int j = startCol; j <= totalCols; j++, cj++) {
				if (j == startCol)
					addScenarioList(getCellData(rowToRun, j - 1));
				tabArray[ci][cj] = getCellData(rowToRun, j);
				System.out.println(tabArray[ci][cj]);
			}
			System.out.println("");
		}
		// }
		catch (FileNotFoundException e) {
			System.out.println("Could not read the Excel sheet");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Could not read the Excel sheet");
			e.printStackTrace();
		}
		return (tabArray);
	}

	// This method is to read the test data from the Excel cell, in this we are
	// passing parameters as Row num and Col num
	public static String getCellData(int RowNum, int ColNum) {
		try {
			Cell = ExcelWSheet.getRow(RowNum).getCell(ColNum);
			String CellData = Cell.getStringCellValue();
			return CellData;
		} catch (Exception e) {
			return "";
		}
	}









}
*/