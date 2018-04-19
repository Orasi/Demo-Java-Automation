package com.orasi.web.webelements.impl;

import static com.orasi.utils.TestReporter.logTrace;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;

import com.orasi.web.OrasiDriver;
import com.orasi.web.webelements.Element;
import com.orasi.web.webelements.Webtable;

/**
 * Wrapper class like Select that wraps basic checkbox functionality.
 */
public class WebtableImpl extends ElementImpl implements Webtable {

    /**
     * Wraps a WebElement with checkbox functionality.
     *
     * @param element
     *            to wrap up
     */

    public WebtableImpl(OrasiDriver driver, By by) {
        super(driver, by);
    }

    private List<Element> getRowCollection() {
        logTrace("Entering WebtableImpl#getRowCollection");
        driver.setElementTimeout(1, TimeUnit.SECONDS);
        List<Element> rowCollection = reload().findElements(By.xpath("tr|tbody/tr"));
        driver.setElementTimeout(driver.getElementTimeout(), TimeUnit.SECONDS);
        logTrace("Exiting WebtableImpl#getRowCollection");
        return rowCollection;

    }

    private List<Element> getColumnCollection(Element row) {
        logTrace("Entering WebtableImpl#getColumnCollection");
        driver.setElementTimeout(1, TimeUnit.MILLISECONDS);
        List<Element> columnCollection = row.findElements(By.xpath("th|td"));
        driver.setElementTimeout(driver.getElementTimeout(), TimeUnit.SECONDS);
        logTrace("Exiting WebtableImpl#getColumnCollection");
        return columnCollection;
    }

    /**
     * Attempts to locate the number of child elements with the HTML
     * tag "tr" using xpath. If none are found, the xpath "tbody/tr" is
     * used.
     *
     * @return int - number of rows found for a given table
     */
    @Override
    public int getRowCount() {
        logTrace("Entering WebtableImpl#getRowCount");
        int rows = getRowCollection().size();
        logTrace("Exiting WebtableImpl#getRowCount");
        return rows;
    }

    /**
     * Attempts to locate the number of child elements with the HTML
     * tag "tr" using xpath. If none are found, the xpath "tbody/tr" is
     * used. All rows are then iterated through until the desired row,
     * determined by the parameter, is found.
     *
     * @param row
     *            - Desired row for which to return a column count
     * @return int - number of columns found for a given row
     */
    @Override
    public int getColumnCount(int row) {
        logTrace("Entering WebtableImpl#getColumnCount");
        int columns = getColumnCollection(getRowCollection().get(row)).size();
        logTrace("Exiting WebtableImpl#getColumnCount");
        return columns;
    }

    /**
     * Attempts to locate the number of child elements with the HTML
     * tag "tr" using xpath. If none are found, the xpath "tbody/tr" is
     * used. All rows are then iterated through until the desired row,
     * determined by the parameter 'row', is found. For this row, all
     * columns are then iterated through until the desired column,
     * determined by the parameter 'column', is found.
     *
     * @param row
     *            - Desired row in which to search for a particular cell
     * @param column
     *            - Desired column in which to find the cell
     * @return WebElement - the desired cell
     */
    @Override
    public Element getCell(int row, int column) {
        logTrace("Entering WebtableImpl#getCell");
        driver.setElementTimeout(1, TimeUnit.SECONDS);
        Element cell = new ElementImpl(driver, By.xpath(getElementIdentifier() + "/tbody/tr[" + row + "]/td[" + column + "]|" + getElementIdentifier() + "/tbody/tr[" + row + "]/th[" + column
                + "]|" + getElementIdentifier() + "/tr[" + row + "]/td[" + column + "]|" + getElementIdentifier() + "/tr[" + row + "]/th[" + column + "]"));
        driver.setElementTimeout(driver.getElementTimeout(), TimeUnit.SECONDS);
        logTrace("Exiting WebtableImpl#getCell");
        return cell;
    }

    /**
     * Attempts to locate the number of child elements with the HTML
     * tag "tr" using xpath. If none are found, the xpath "tbody/tr" is
     * used. All rows are then iterated through until the desired row,
     * determined by the parameter 'row', is found. For this row, all
     * columns are then iterated through until the desired column,
     * determined by the parameter 'column', is found. The cell found
     * by the row/column indices is then clicked
     *
     * @param row
     *            - Desired row in which to search for a particular cell
     * @param column
     *            - Desired column in which to find the cell
     */
    @Override
    public void clickCell(int row, int column) {
        logTrace("Entering WebtableImpl#clickCell");
        getCell(row, column).click();
        logTrace("Exiting WebtableImpl#clickCell");
    }

    /**
     * Attempts to locate the number of child elements with the HTML
     * tag "tr" using xpath. If none are found, the xpath "tbody/tr" is
     * used. All rows are then iterated through until the desired row,
     * determined by the parameter 'row', is found. For this row, all
     * columns are then iterated through until the desired column,
     * determined by the parameter 'column', is found.
     *
     * @param row
     *            - Desired row in which to search for a particular cell
     * @param column
     *            - Desired column in which to find the cell
     * @return String - text of cell contents
     */
    @Override
    public String getCellData(int row, int column) {
        logTrace("Entering WebtableImpl#getCellData");
        String data = getCell(row, column).getText();
        logTrace("Exiting WebtableImpl#getCellData");
        return data;
    }

    /**
     * Attempts to locate the number of child elements with the HTML
     * tag "tr" using xpath. If none are found, the xpath "tbody/tr" is
     * used. All rows are then iterated through as well as each column
     * for each row until the cell with the desired text, determined by
     * the parameter, is found.
     *
     * @param text
     *            - text for which to search
     * @return int - row number containing the desired text
     */
    @Override
    public int getRowWithCellText(String text) {
        logTrace("Entering WebtableImpl#getRowWithCellText(String text)");
        int row = getRowWithCellText(text, -1, 1, true);
        logTrace("Exiting WebtableImpl#getRowWithCellText(String text)");
        return row;
    }

    /**
     * Attempts to locate the number of child elements with the HTML
     * tag "tr" using xpath. If none are found, the xpath "tbody/tr" is
     * used. All rows are then iterated through as well as each column
     * for each row until the cell with the desired text, determined by
     * the parameter, is found.
     *
     * @param text
     *            - text for which to search
     * @param -
     *            determines if text should match exactly or ignore case
     * @return int - row number containing the desired text
     */
    @Override
    public int getRowWithCellText(String text, boolean exact) {
        logTrace("Entering WebtableImpl#getRowWithCellText(String text, boolean exact)");
        int row = getRowWithCellText(text, -1, 1, exact);
        logTrace("Exiting WebtableImpl#getRowWithCellText(String text, boolean exact)");
        return row;
    }

    /**
     * Attempts to locate the number of child elements with the HTML
     * tag "tr" using xpath. If none are found, the xpath "tbody/tr" is
     * used. All rows are then iterated through as well as each column
     * for each row until the desired cell is located. The cell text is
     * then validated against the parameter 'text'
     *
     * @param text
     *            - text for which to search
     * @param columnPosition
     *            - column number where the desired text is expected
     * @return int - row number containing the desired text
     */
    @Override
    public int getRowWithCellText(String text, int columnPosition) {
        logTrace("Entering WebtableImpl#getRowWithCellText(String text, int columnPosition)");
        int row = getRowWithCellText(text, columnPosition, 1, true);
        logTrace("Exiting WebtableImpl#getRowWithCellText(String text, int columnPosition)");
        return row;
    }

    /**
     * Attempts to locate the number of child elements with the HTML
     * tag "tr" using xpath. If none are found, the xpath "tbody/tr" is
     * used. All rows are then iterated through as well as each column
     * for each row until the desired cell is located. The cell text is
     * then validated against the parameter 'text'
     *
     * @param text
     *
     *            - text for which to search
     * @param columnPosition
     *            - column number where the desired text is expected
     * @param startRow
     *            - row with which to start
     * @return int - row number containing the desired text
     */
    @Override
    public int getRowWithCellText(String text, int columnPosition, int startRow) {
        logTrace("Entering WebtableImpl#getRowWithCellText(String text, int columnPosition, int startRow)");
        int row = getRowWithCellText(text, columnPosition, startRow, true);
        logTrace("Exiting WebtableImpl#getRowWithCellText(String text, int columnPosition, int startRow)");
        return row;
    }

    /**
     * Attempts to locate the number of child elements with the HTML
     * tag "tr" using xpath. If none are found, the xpath "tbody/tr" is
     * used. All rows are then iterated through as well as each column
     * for each row until the desired cell is located. The cell text is
     * then validated against the parameter 'text'
     *
     * @param text
     *            - text for which to search
     * @param columnPosition
     *            - column number where the desired text is expected
     * @param startRow
     *            - row with which to start
     * @param exact
     *            - determines if text should match exactly or ignore case
     * @return int - row number containing the desired text
     */
    @Override
    public int getRowWithCellText(String text, int columnPosition, int startRow, boolean exact) {
        logTrace("Entering WebtableImpl#getRowWithCellText(String text, int columnPosition, int startRow, boolean exact)");
        int currentRow = 1, rowFound = 0;

        List<Element> rowCollection = getRowCollection();
        for (Element rowElement : rowCollection) {
            if (startRow > currentRow) {
                currentRow++;
            } else {
                if (currentRow <= rowCollection.size()) {

                    if (columnPosition == -1) {
                        for (Element cell : getColumnCollection(rowElement)) {
                            if (exact) {
                                if (cell.getText().trim().equals(text)) {
                                    logTrace("Exiting WebtableImpl#getRowWithCellText(String text, int columnPosition, int startRow, boolean exact)");
                                    return currentRow;
                                }
                            } else {
                                if (cell.getText().toLowerCase().trim().contains(text.toLowerCase())) {
                                    logTrace("Exiting WebtableImpl#getRowWithCellText(String text, int columnPosition, int startRow, boolean exact)");
                                    return currentRow;
                                }
                            }
                        }
                    } else {
                        Element cell = rowElement.findElements(By.xpath("th|td")).get(columnPosition - 1);
                        if (exact) {
                            if (cell.getText().trim().equals(text)) {
                                logTrace("Exiting WebtableImpl#getRowWithCellText(String text, int columnPosition, int startRow, boolean exact)");
                                return currentRow;
                            }
                        } else {
                            if (cell.getText().toLowerCase().trim().contains(text.toLowerCase())) {
                                logTrace("Exiting WebtableImpl#getRowWithCellText(String text, int columnPosition, int startRow, boolean exact)");
                                return currentRow;
                            }
                        }
                    }
                    currentRow++;
                }
            }
        }

        logTrace("Exiting WebtableImpl#getRowWithCellText(String text, int columnPosition, int startRow, boolean exact)");
        return rowFound;
    }

    /**
     * Attempts to locate the number of child elements with the HTML
     * tag "tr" using xpath. If none are found, the xpath "tbody/tr" is
     * used. All rows are then iterated through as well as each column
     * for each row until the desired cell is located. The cell text is
     * then validated against the parameter 'text'
     *
     * @param text
     *            - text for which to search
     * @return int - column number containing the desired text
     */
    @Override
    public int getColumnWithCellText(String text) {
        logTrace("Entering WebtableImpl#getColumnWithCellText(String text)");
        int column = getColumnWithCellText(text, 1);
        logTrace("Exiting WebtableImpl#getColumnWithCellText(String text)");
        return column;
    }

    /**
     * Attempts to locate the number of child elements with the HTML
     * tag "tr" using xpath. If none are found, the xpath "tbody/tr" is
     * used. All rows are then iterated through until the desired row
     * is found, then all columns are iterated through until the
     *
     * @param text
     *            - text for which to search
     * @param rowPosition
     *            - row where the expected text is anticipated
     * @return int - column number containing the desired text
     */
    @Override
    public int getColumnWithCellText(String text, int rowPosition) {
        logTrace("Entering WebtableImpl#getColumnWithCellText(String text, int rowPosition)");
        int currentColumn = 1;
        List<Element> columns = getColumnCollection(getRowCollection().get(rowPosition - 1));
        for (Element cell : columns) {
            if (currentColumn <= columns.size()) {
                if (cell.getText().trim().equals(text)) {
                    logTrace("Exiting WebtableImpl#getColumnWithCellText(String text, int rowPosition)");
                    return currentColumn;
                }
                currentColumn++;
            }
        }
        logTrace("Exiting WebtableImpl#getColumnWithCellText(String text, int rowPosition)");
        return 0;
    }
}