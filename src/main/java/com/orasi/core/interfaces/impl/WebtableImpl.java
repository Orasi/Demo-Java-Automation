package com.orasi.core.interfaces.impl;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.naming.directory.NoSuchAttributeException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import com.orasi.core.interfaces.Element;
import com.orasi.core.interfaces.Webtable;
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

    private List<WebElement> getRowCollection(TestEnvironment te){
	te.getDriver().manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
	List<WebElement> rowCollection = this.element.findElements(By.xpath("tr"));

	if (rowCollection.size() == 0) {
	    rowCollection = this.element.findElements(By.xpath("tbody/tr"));
	}
	te.getDriver().manage().timeouts().implicitlyWait(te.getDefaultTestTimeout(),TimeUnit.SECONDS);
	return rowCollection;
	
    }
    
    private List<WebElement> getColumnCollection(TestEnvironment te, WebElement row){
	String xpath = null;
	te.getDriver().manage().timeouts().implicitlyWait(0, TimeUnit.MILLISECONDS);

	if (row.findElements(By.xpath("th")).size() != 0) {
	    xpath = "th";
	} else if (row.findElements(By.xpath("td")).size() != 0) {
	    xpath = "td";
	} else {
	    throw new RuntimeException(
		    "No child element with the HTML tag \"th\" or \"td\" were found for the parent webtable [ <b>@FindBy: "
			    + getElementLocatorInfo() + " </b>]");
	}
	
	te.getDriver().manage().timeouts().implicitlyWait(te.getDefaultTestTimeout(),TimeUnit.SECONDS);

	return row.findElements(By.xpath(xpath));
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
    public int getRowCount(TestEnvironment te) {	
	return getRowCollection(te).size();
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
    public int getColumnCount(TestEnvironment te, int row) {	
	return getColumnCollection(te, getRowCollection(te).get(row)).size();	
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
    public Element getCell(TestEnvironment te, int row, int column) {
	te.getDriver().manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
	Element cell = new ElementImpl(getWrappedElement().findElement(By.xpath("tbody/tr[" + row + "]/td[" + column + "]|tbody/tr["+ row + "]/th[" + column + "]|tr[" + row + "]/td["+ column + "]|tr[" + row + "]/th[" + column + "]")));
	te.getDriver().manage().timeouts().implicitlyWait(te.getDefaultTestTimeout(), TimeUnit.SECONDS);
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
    public void clickCell(TestEnvironment te, int row, int column) {
	getCell(te, row, column).click();	
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
    public String getCellData(TestEnvironment te, int row, int column) {
	return getCell(te, row, column).getText();
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
    public int getRowWithCellText(TestEnvironment te, String text) {
	return getRowWithCellText(te, text, -1);
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
    public int getRowWithCellText(TestEnvironment te, String text, int columnPosition) {
	return getRowWithCellText(te, text, columnPosition, 1);
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
    public int getRowWithCellText(TestEnvironment te, String text, int columnPosition, int startRow) {
	return getRowWithCellText(te, text, columnPosition, startRow, true);
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
    public int getRowWithCellText(TestEnvironment te, String text, int columnPosition, int startRow, boolean exact) {   

	int currentRow = 1,rowFound = 0;
	String xpath = null;
	Boolean found = false;

	List<WebElement> rowCollection = getRowCollection(te);
	for (WebElement rowElement : rowCollection) {
	    if (startRow > currentRow) {
		currentRow++;
	    } else {
		if (currentRow <= rowCollection.size()) {
		    te.getDriver().manage().timeouts().implicitlyWait(0, TimeUnit.MILLISECONDS);
		    if (rowElement.findElements(By.xpath("th")).size() != 0) {
			xpath = "th";
		    } else if (rowElement.findElements(By.xpath("td")).size() != 0) {
			xpath = "td";
		    }

		    te.getDriver().manage().timeouts().implicitlyWait(te.getDefaultTestTimeout(),TimeUnit.SECONDS);

		    if (columnPosition == -1) {			
			for (WebElement cell : getColumnCollection(te, rowElement)) {
			    if (exact){
				if (cell.getText().trim().equals(text)) return currentRow;
			    }else{
				if (cell.getText().toLowerCase().trim().contains(text.toLowerCase())) return currentRow;
			    }
			}
		    } else {
			WebElement cell = rowElement.findElements(By.xpath(xpath)).get(columnPosition - 1);
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
	
	return 0;
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
    public int getColumnWithCellText(TestEnvironment te, String text) {
	return getColumnWithCellText(te, text, 1);
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
    public int getColumnWithCellText(TestEnvironment te, String text, int rowPosition) {
	int currentColumn = 1;
	List<WebElement> columns = getColumnCollection(te, getRowCollection(te).get(rowPosition-1));
	for (WebElement cell : columns) {	  
	    if (currentColumn <= columns.size()) {
		if (cell.getText().trim().equals(text)) return currentColumn;
		currentColumn++;
	    }
	}	
	return 0;
    }
    
    
    /**
     * Deprecated methods
     */
    
    /**
     * @summary - Attempts to locate the number of child elements with the HTML
     *          tag "tr" using xpath. If none are found, the xpath "tbody/tr" is
     *          used.
     * @param driver
     *            - Current active WebDriver object
     * @return int - number of rows found for a given table
     */
    @Deprecated
    public int getRowCount(WebDriver driver) {
	driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
	List<WebElement> rowCollection = this.element.findElements(By
		.xpath("tr"));

	if (rowCollection.size() == 0) {
	    rowCollection = this.element.findElements(By.xpath("tbody/tr"));
	}
	driver.manage()
		.timeouts()
		.implicitlyWait(TestEnvironment.getDefaultTestTimeout(),
			TimeUnit.SECONDS);
	return rowCollection.size();
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
     * @return WebElement - the desired cell
     * @throws NoSuchAttributeException
     * @deprecated
     */
    @Deprecated
    public WebElement getCell(WebDriver driver, int row, int column) {
	driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
	List<WebElement> rowCollection = this.element.findElements(By
		.xpath("tr"));

	if (rowCollection.size() == 0) {
	    rowCollection = this.element.findElements(By.xpath("tbody/tr"));
	}
	driver.manage()
		.timeouts()
		.implicitlyWait(TestEnvironment.getDefaultTestTimeout(),
			TimeUnit.SECONDS);
	WebElement elementCell = null;

	int currentRow = 1, currentColumn = 1;
	String xpath = null;
	Boolean found = false;
	List<WebElement> columnCollection = null;

	for (WebElement rowElement : rowCollection) {
	    if (row != currentRow) {
		currentRow++;
	    } else {
		driver.manage().timeouts()
			.implicitlyWait(0, TimeUnit.MILLISECONDS);
		if (rowElement.findElements(By.xpath("th")).size() != 0) {
		    xpath = "th";
		} else if (rowElement.findElements(By.xpath("td")).size() != 0) {
		    xpath = "td";
		} else {
		    throw new RuntimeException(
			    "No child element with the HTML tag \"th\" or \"td\" were found for the parent webtable [ <b>@FindBy: "
				    + getElementLocatorInfo() + " </b>]");
		}

		driver.manage()
			.timeouts()
			.implicitlyWait(
				TestEnvironment.getDefaultTestTimeout(),
				TimeUnit.SECONDS);

		columnCollection = rowElement.findElements(By.xpath(xpath));
		for (WebElement cell : columnCollection) {
		    if (column != currentColumn) {
			currentColumn++;
		    } else {
			elementCell = cell;
			found = true;
			break;
		    }
		}
		if (found) {
		    break;
		}
	    }
	}
	Assert.assertEquals(Boolean.valueOf(found), Boolean.TRUE,
		"No cell was found for row [" + String.valueOf(row)
			+ "] and column [" + String.valueOf(column)
			+ "]. The column count for row [" + String.valueOf(row)
			+ "] is [" + String.valueOf(columnCollection.size())
			+ "]");

	return elementCell;
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
    @Deprecated
    public int getColumnCount(WebDriver driver, int row) {
	driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
	List<WebElement> rowCollection = this.element.findElements(By
		.xpath("tr"));
	boolean rowFound = false;

	if (rowCollection.size() == 0) {
	    rowCollection = this.element.findElements(By.xpath("tbody/tr"));
	}
	driver.manage()
		.timeouts()
		.implicitlyWait(TestEnvironment.getDefaultTestTimeout(),
			TimeUnit.SECONDS);

	int currentRow = 1;
	int columnCount = 0;
	String xpath = null;

	for (WebElement rowElement : rowCollection) {
	    if (row == currentRow) {
		rowFound = true;
		driver.manage().timeouts()
			.implicitlyWait(0, TimeUnit.MILLISECONDS);

		if (rowElement.findElements(By.xpath("th")).size() != 0) {
		    xpath = "th";
		} else if (rowElement.findElements(By.xpath("td")).size() != 0) {
		    xpath = "td";
		} else {
		    throw new RuntimeException(
			    "No child element with the HTML tag \"th\" or \"td\" were found for the parent webtable [ <b>@FindBy: "
				    + getElementLocatorInfo() + " </b>]");
		}
		driver.manage()
			.timeouts()
			.implicitlyWait(
				TestEnvironment.getDefaultTestTimeout(),
				TimeUnit.SECONDS);

		List<WebElement> columnCollection = rowElement.findElements(By
			.xpath(xpath));
		columnCount = columnCollection.size();
		break;
	    } else {
		currentRow++;
	    }
	}
	Assert.assertEquals(
		rowFound,
		true,
		"The expected row ["
			+ String.valueOf(row)
			+ "] was not found. The number of rows found for webtable [ <b>@FindBy: "
			+ getElementLocatorInfo() + " </b>] is ["
			+ String.valueOf(rowCollection.size()) + "].");

	return columnCount;
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
    @Deprecated
    public void clickCell(WebDriver driver, int row, int column) {
	driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
	List<WebElement> rowCollection = this.element.findElements(By
		.xpath("tr"));

	if (rowCollection.size() == 0) {
	    rowCollection = this.element.findElements(By.xpath("tbody/tr"));
	}
	driver.manage()
		.timeouts()
		.implicitlyWait(TestEnvironment.getDefaultTestTimeout(),
			TimeUnit.SECONDS);

	int currentRow = 1, currentColumn = 1;
	String xpath = null;
	Boolean found = false;
	List<WebElement> columnCollection = null;

	for (WebElement rowElement : rowCollection) {
	    if (row != currentRow) {
		currentRow++;
	    } else {
		driver.manage().timeouts()
			.implicitlyWait(0, TimeUnit.MILLISECONDS);
		if (rowElement.findElements(By.xpath("th")).size() != 0) {
		    xpath = "th";
		} else if (rowElement.findElements(By.xpath("td")).size() != 0) {
		    xpath = "td";
		} else {
		    throw new RuntimeException(
			    "No child element with the HTML tag \"th\" or \"td\" were found for the parent webtable [ <b>@FindBy: "
				    + getElementLocatorInfo() + " </b>]");
		}

		driver.manage()
			.timeouts()
			.implicitlyWait(
				TestEnvironment.getDefaultTestTimeout(),
				TimeUnit.SECONDS);

		columnCollection = rowElement.findElements(By.xpath(xpath));
		for (WebElement cell : columnCollection) {
		    if (column != currentColumn) {
			currentColumn++;
		    } else {
			cell.click();
			found = true;
			break;
		    }
		}
		if (found) {
		    break;
		}
	    }
	}
	Assert.assertEquals(Boolean.valueOf(found), Boolean.TRUE,
		"No cell was found for row [" + String.valueOf(row)
			+ "] and column [" + String.valueOf(column)
			+ "]. The column count for row [" + String.valueOf(row)
			+ "] is [" + String.valueOf(columnCollection.size())
			+ "]");
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
    @Deprecated
    public String getCellData(WebDriver driver, int row, int column) {
	driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
	String cellData = "";
	cellData = this.element.findElement(
		By.xpath("tbody/tr[" + row + "]/td[" + column + "]|tbody/tr["
			+ row + "]/th[" + column + "]|tr[" + row + "]/td["
			+ column + "]|tr[" + row + "]/th[" + column + "]"))
		.getText();
	driver.manage()
		.timeouts()
		.implicitlyWait(TestEnvironment.getDefaultTestTimeout(),
			TimeUnit.SECONDS);
	return cellData;
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
    @Deprecated
    public int getRowWithCellText(WebDriver driver, String text) {
	return getRowWithCellText(driver, text, -1);
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
    @Deprecated
    public int getRowWithCellText(WebDriver driver, String text, int columnPosition) {
	return getRowWithCellText(driver, text, columnPosition, 1);
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
    @Deprecated
    public int getRowWithCellText(WebDriver driver, String text, int columnPosition, int startRow) {

	driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
	List<WebElement> rowCollection = this.element.findElements(By
		.xpath("tr"));

	if (rowCollection.size() == 0) {
	    rowCollection = this.element.findElements(By.xpath("tbody/tr"));
	}
	driver.manage()
		.timeouts()
		.implicitlyWait(TestEnvironment.getDefaultTestTimeout(),
			TimeUnit.SECONDS);

	int currentRow = 1, currentColumn =1, rowFound = 0;
	String xpath = null;
	Boolean found = false;

	for (WebElement rowElement : rowCollection) {
	    if (startRow > currentRow) {
		currentRow++;
	    } else {
		if (currentRow <= rowCollection.size()) {
		    driver.manage().timeouts()
			    .implicitlyWait(0, TimeUnit.MILLISECONDS);
		    if (rowElement.findElements(By.xpath("th")).size() != 0) {
			xpath = "th";
		    } else if (rowElement.findElements(By.xpath("td")).size() != 0) {
			xpath = "td";
		    }

		    driver.manage()
			    .timeouts()
			    .implicitlyWait(
				    TestEnvironment.getDefaultTestTimeout(),
				    TimeUnit.SECONDS);

		    if (columnPosition == -1) {
			List<WebElement> columnCollection = rowElement
				.findElements(By.xpath(xpath));
			for (WebElement cell : columnCollection) {
			    if (cell.getText().trim().equals(text)) {
				rowFound = currentRow;
				found = true;
				break;
			    }
			}
		    } else {
			WebElement cell = rowElement.findElements(
				By.xpath(xpath)).get(columnPosition - 1);
			if (cell.getText().trim().equals(text)) {
			    rowFound = currentRow;
			    found = true;
			    break;
			}
		    }
		    if (found)
			break;
		    currentRow++;
		    currentColumn = 1;
		}
	    }
	}
	Assert.assertEquals(Boolean.valueOf(found), Boolean.TRUE,
		"No cell in column [" + String.valueOf(columnPosition)
			+ "] was found to contain the text [" + text + "].");
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
    @Deprecated
    public int getColumnWithCellText(WebDriver driver, String text) {
	driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
	List<WebElement> rowCollection = this.element.findElements(By
		.xpath("tr"));

	if (rowCollection.size() == 0) {
	    rowCollection = this.element.findElements(By.xpath("tbody/tr"));
	}
	driver.manage()
		.timeouts()
		.implicitlyWait(TestEnvironment.getDefaultTestTimeout(),
			TimeUnit.SECONDS);

	int currentRow = 1, currentColumn = 1, columnFound = 0;
	String xpath = null;
	Boolean found = false;

	for (WebElement rowElement : rowCollection) {
	    if (currentRow <= rowCollection.size()) {
		driver.manage().timeouts()
			.implicitlyWait(0, TimeUnit.MILLISECONDS);
		if (rowElement.findElements(By.xpath("th")).size() != 0) {
		    xpath = "th";
		} else if (rowElement.findElements(By.xpath("td")).size() != 0) {
		    xpath = "td";
		}

		driver.manage()
			.timeouts()
			.implicitlyWait(
				TestEnvironment.getDefaultTestTimeout(),
				TimeUnit.SECONDS);

		List<WebElement> columnCollection = rowElement.findElements(By
			.xpath(xpath));
		for (WebElement cell : columnCollection) {
		    if (currentColumn <= columnCollection.size()) {
			if (cell.getText().trim().equals(text)) {
			    columnFound = currentColumn;
			    found = true;
			    break;
			}
			currentColumn++;
		    }
		}
		if (found) {
		    break;
		}
		currentRow++;
		currentColumn = 1;
	    }
	}
	Assert.assertEquals(Boolean.valueOf(found), Boolean.TRUE,
		"No cell in any column was found to have the text [" + text
			+ "].");
	return columnFound;
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
    @Deprecated
    public int getColumnWithCellText(WebDriver driver, String text, int rowPosition) {
	driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
	List<WebElement> rowCollection = this.element.findElements(By
		.xpath("tr"));

	if (rowCollection.size() == 0) {
	    rowCollection = this.element.findElements(By.xpath("tbody/tr"));
	}
	driver.manage()
		.timeouts()
		.implicitlyWait(TestEnvironment.getDefaultTestTimeout(),
			TimeUnit.SECONDS);

	int currentRow = 1, currentColumn = 1, columnFound = 0;
	String xpath = null;
	Boolean found = false;

	for (WebElement rowElement : rowCollection) {
	    if (rowPosition > currentRow) {
		currentRow++;
	    } else {
		if (currentRow <= rowCollection.size()) {
		    driver.manage().timeouts()
			    .implicitlyWait(0, TimeUnit.MILLISECONDS);
		    if (rowElement.findElements(By.xpath("th")).size() != 0) {
			xpath = "th";
		    } else if (rowElement.findElements(By.xpath("td")).size() != 0) {
			xpath = "td";
		    }

		    driver.manage()
			    .timeouts()
			    .implicitlyWait(
				    TestEnvironment.getDefaultTestTimeout(),
				    TimeUnit.SECONDS);

		    List<WebElement> columnCollection = rowElement
			    .findElements(By.xpath(xpath));
		    for (WebElement cell : columnCollection) {
			if (currentColumn <= columnCollection.size()) {
			    if (cell.getText().trim().equals(text)) {
				columnFound = currentColumn;
				found = true;
				break;
			    }
			    currentColumn++;
			}
		    }
		    if (found) {
			break;
		    }
		    currentRow++;
		    currentColumn = 1;
		}
	    }
	}
	Assert.assertEquals(Boolean.valueOf(found), Boolean.TRUE,
		"No cell in row [" + String.valueOf(rowPosition)
			+ "] was found to have the text [" + text + "].");
	return columnFound;
    }

    /**
     * @summary - Attempts to locate the number of child elements with the HTML
     *          tag "tr" using xpath. If none are found, the xpath "tbody/tr" is
     *          used. All rows are then iterated through and a particular
     *          column, determined by the 'columnPosition' parameter, is grabbed
     *          and the text is validate against the expected text defined by
     *          the parameter 'text'.
     * @param driver
     *            - Current active WebDriver object
     * @param text
     *            - text for which to search
     * @param columnPosition
     *            - column where the expected text is anticipated
     * @return int - column number containing the desired text
     * @deprecated
     */
    @Deprecated
    public int getRowThatContainsCellText(WebDriver driver, String text,
	    int columnPosition) {
	driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
	List<WebElement> rowCollection = this.element.findElements(By
		.xpath("tr"));

	if (rowCollection.size() == 0) {
	    rowCollection = this.element.findElements(By.xpath("tbody/tr"));
	}
	driver.manage()
		.timeouts()
		.implicitlyWait(TestEnvironment.getDefaultTestTimeout(),
			TimeUnit.SECONDS);

	int currentRow = 1, currentColumn = 1, rowFound = 0;
	String xpath = null;
	Boolean found = false;

	for (WebElement rowElement : rowCollection) {
	    if (currentRow <= rowCollection.size()) {
		driver.manage().timeouts()
			.implicitlyWait(0, TimeUnit.MILLISECONDS);
		if (rowElement.findElements(By.xpath("th")).size() != 0) {
		    xpath = "th";
		} else if (rowElement.findElements(By.xpath("td")).size() != 0) {
		    xpath = "td";
		}

		driver.manage()
			.timeouts()
			.implicitlyWait(
				TestEnvironment.getDefaultTestTimeout(),
				TimeUnit.SECONDS);

		List<WebElement> columnCollection = rowElement.findElements(By
			.xpath(xpath));
		for (WebElement cell : columnCollection) {
		    if (currentColumn == columnPosition) {
			if (cell.getText().trim().toUpperCase()
				.contains(text.toUpperCase())) {
			    rowFound = currentRow;
			    found = true;
			    break;
			}
		    } else {
			currentColumn++;
		    }
		}
		if (found) {
		    break;
		}
		currentRow++;
		currentColumn = 1;
	    }
	}
	return rowFound;
    }

}