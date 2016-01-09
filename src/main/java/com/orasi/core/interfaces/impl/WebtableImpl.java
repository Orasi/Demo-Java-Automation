package com.orasi.core.interfaces.impl;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.naming.directory.NoSuchAttributeException;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import com.orasi.core.interfaces.Element;
import com.orasi.core.interfaces.Webtable;
import com.orasi.utils.OrasiDriver;
import com.orasi.utils.TestEnvironment;

/**
 * Wrapper class like Select that wraps basic checkbox functionality.
 */
public class WebtableImpl extends ElementImpl implements Webtable {

    /**
     * @summary - Wraps a WebElement with checkbox functionality.
     * @param element
     *            to wrap up
     */
    public WebtableImpl(WebElement element) {
    	super(element);
    }
    
    public WebtableImpl(WebElement element, OrasiDriver driver) {
        super(element, driver);
    }

    private List<WebElement> getRowCollection(){
	getWrappedDriver().setElementTimeout(1, TimeUnit.SECONDS);
	List<WebElement> rowCollection = this.element.findElements(By.xpath("tr|tbody/tr"));
	getWrappedDriver().setElementTimeout(getWrappedDriver().getElementTimeout(),TimeUnit.SECONDS);
	return rowCollection;
	
    }
    
    private List<WebElement> getColumnCollection( WebElement row){
	String xpath = null;
	
	getWrappedDriver().setElementTimeout(1, TimeUnit.MILLISECONDS);
	List<WebElement> columnCollection = row.findElements(By.xpath("th|td"));
	getWrappedDriver().setElementTimeout(getWrappedDriver().getElementTimeout(),TimeUnit.SECONDS);

	return columnCollection ;
    }
    /**
     * @summary - Attempts to locate the number of child elements with the HTML
     *          tag "tr" using xpath. If none are found, the xpath "tbody/tr" is
     *          used.
     * @param driver
     *            - Current active WebDriver object
     * @return int - number of rows found for a given table
     */
    @Override
    public int getRowCount() {	
	return getRowCollection().size();
    }

    /**
     * @summary - Attempts to locate the number of child elements with the HTML
     *          tag "tr" using xpath. If none are found, the xpath "tbody/tr" is
     *          used. All rows are then iterated through until the desired row,
     *          determined by the parameter, is found.
     * @param driver
     *            - Current active WebDriver object
     * @param row
     *            - Desired row for which to return a column count
     * @return int - number of columns found for a given row
     * @throws NoSuchAttributeException
     */
    @Override
    public int getColumnCount( int row) {	
	return getColumnCollection(getRowCollection().get(row)).size();	
    }

    /**
     * @summary - Attempts to locate the number of child elements with the HTML
     *          tag "tr" using xpath. If none are found, the xpath "tbody/tr" is
     *          used. All rows are then iterated through until the desired row,
     *          determined by the parameter 'row', is found. For this row, all
     *          columns are then iterated through until the desired column,
     *          determined by the parameter 'column', is found.
     * @param te
     *            - TestEnvironment super class that contains the current driver
     * @param row
     *            - Desired row in which to search for a particular cell
     * @param column
     *            - Desired column in which to find the cell
     * @return WebElement - the desired cell
     * @throws NoSuchAttributeException
     */
    @Override
    public Element getCell( int row, int column) {
	int timeout = getWrappedDriver().getElementTimeout();
	getWrappedDriver().setElementTimeout(1, TimeUnit.MILLISECONDS);
	Element cell = new ElementImpl(getWrappedElement().findElement(By.xpath("tbody/tr[" + row + "]/td[" + column + "]|tbody/tr["+ row + "]/th[" + column + "]|tr[" + row + "]/td["+ column + "]|tr[" + row + "]/th[" + column + "]")));
	getWrappedDriver().setElementTimeout(timeout);
	return cell;
    }

    /**
     * @summary - Attempts to locate the number of child elements with the HTML
     *          tag "tr" using xpath. If none are found, the xpath "tbody/tr" is
     *          used. All rows are then iterated through until the desired row,
     *          determined by the parameter 'row', is found. For this row, all
     *          columns are then iterated through until the desired column,
     *          determined by the parameter 'column', is found. The cell found
     *          by the row/column indices is then clicked
     * @param driver
     *            - Current active WebDriver object
     * @param row
     *            - Desired row in which to search for a particular cell
     * @param column
     *            - Desired column in which to find the cell
     * @throws NoSuchAttributeException
     */
    @Override
    public void clickCell( int row, int column) {
	getCell(row, column).click();	
    }

    /**
     * @summary - Attempts to locate the number of child elements with the HTML
     *          tag "tr" using xpath. If none are found, the xpath "tbody/tr" is
     *          used. All rows are then iterated through until the desired row,
     *          determined by the parameter 'row', is found. For this row, all
     *          columns are then iterated through until the desired column,
     *          determined by the parameter 'column', is found.
     * @param driver
     *            - Current active WebDriver object
     * @param row
     *            - Desired row in which to search for a particular cell
     * @param column
     *            - Desired column in which to find the cell
     * @return String - text of cell contents
     * @throws NoSuchAttributeException
     */
    @Override
    public String getCellData( int row, int column) {
	return getCell(row, column).getText();
    }

    /**
     * @summary - Attempts to locate the number of child elements with the HTML
     *          tag "tr" using xpath. If none are found, the xpath "tbody/tr" is
     *          used. All rows are then iterated through as well as each column
     *          for each row until the cell with the desired text, determined by
     *          the parameter, is found.
     * @param driver
     *            - Current active WebDriver object
     * @param text
     *            - text for which to search
     * @return int - row number containing the desired text
     */
    @Override
    public int getRowWithCellText( String text) {
	return getRowWithCellText(text, -1);
    }

    /**
     * @summary - Attempts to locate the number of child elements with the HTML
     *          tag "tr" using xpath. If none are found, the xpath "tbody/tr" is
     *          used. All rows are then iterated through as well as each column
     *          for each row until the desired cell is located. The cell text is
     *          then validated against the parameter 'text'
     * @param driver
     *            - Current active WebDriver object
     * @param text
     *            - text for which to search
     * @param columnPosition
     *            - column number where the desired text is expected
     * @return int - row number containing the desired text
     */
    @Override
    public int getRowWithCellText( String text, int columnPosition) {
	return getRowWithCellText(text, columnPosition, 1);
    }

    /**
     * @summary - Attempts to locate the number of child elements with the HTML
     *          tag "tr" using xpath. If none are found, the xpath "tbody/tr" is
     *          used. All rows are then iterated through as well as each column
     *          for each row until the desired cell is located. The cell text is
     *          then validated against the parameter 'text'
     * @param driver
     *            - Current active WebDriver object
     * @param text
     *            - text for which to search
     * @param columnPosition
     *            - column number where the desired text is expected
     * @param startRow
     *            - row with which to start
     * @return int - row number containing the desired text
     */
    @Override
    public int getRowWithCellText( String text, int columnPosition, int startRow) {
	return getRowWithCellText(text, columnPosition, startRow, true);
    }
    /**
     * @summary - Attempts to locate the number of child elements with the HTML
     *          tag "tr" using xpath. If none are found, the xpath "tbody/tr" is
     *          used. All rows are then iterated through as well as each column
     *          for each row until the desired cell is located. The cell text is
     *          then validated against the parameter 'text'
     * @param driver
     *            - Current active WebDriver object
     * @param text
     *            - text for which to search
     * @param columnPosition
     *            - column number where the desired text is expected
     * @param startRow
     *            - row with which to start
     * @param exact
     * 		  - determines if text should match exactly or ignore case
     * @return int - row number containing the desired text
     */
    @Override
    public int getRowWithCellText( String text, int columnPosition, int startRow, boolean exact) {   

	int currentRow = 1,rowFound = 0;
	String xpath = null;
	Boolean found = false;

	List<WebElement> rowCollection = getRowCollection();
	for (WebElement rowElement : rowCollection) {
	    if (startRow > currentRow) {
		currentRow++;
	    } else {
		if (currentRow <= rowCollection.size()) {
	
		    if (columnPosition == -1) {			
			for (WebElement cell : getColumnCollection(rowElement)) {
			    if (exact){
				if (cell.getText().trim().equals(text)) return currentRow;
			    }else{
				if (cell.getText().toLowerCase().trim().contains(text.toLowerCase())) return currentRow;
			    }
			}
		    } else {
			WebElement cell = rowElement.findElements(By.xpath("th|td")).get(columnPosition - 1);
			if (exact){
			    if (cell.getText().trim().equals(text)) return currentRow;
			}else{
			    if (cell.getText().toLowerCase().trim().contains(text.toLowerCase())) return currentRow;
			}			    		
		    }
		    currentRow++;
		}
	    }
	}
	
	return rowFound;
    }

    /**
     * @summary - Attempts to locate the number of child elements with the HTML
     *          tag "tr" using xpath. If none are found, the xpath "tbody/tr" is
     *          used. All rows are then iterated through as well as each column
     *          for each row until the desired cell is located. The cell text is
     *          then validated against the parameter 'text'
     * @param driver
     *            - Current active WebDriver object
     * @param text
     *            - text for which to search
     * @return int - column number containing the desired text
     */
    @Override
    public int getColumnWithCellText( String text) {
	return getColumnWithCellText(text, 1);
    }

    /**
     * @summary - Attempts to locate the number of child elements with the HTML
     *          tag "tr" using xpath. If none are found, the xpath "tbody/tr" is
     *          used. All rows are then iterated through until the desired row
     *          is found, then all columns are iterated through until the
     *          desired text is found.
     * @param driver
     *            - Current active WebDriver object
     * @param text
     *            - text for which to search
     * @param rowPosition
     *            - row where the expected text is anticipated
     * @return int - column number containing the desired text
     */
    @Override
    public int getColumnWithCellText( String text, int rowPosition) {
	int currentColumn = 1;
	List<WebElement> columns = getColumnCollection(getRowCollection().get(rowPosition-1));
	for (WebElement cell : columns) {	  
	    if (currentColumn <= columns.size()) {
		if (cell.getText().trim().equals(text)) return currentColumn;
		currentColumn++;
	    }
	}	
	return 0;
    }
}