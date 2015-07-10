package com.orasi.core.interfaces;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.orasi.core.interfaces.impl.WebtableImpl;
import com.orasi.core.interfaces.impl.internal.ImplementedBy;
import com.orasi.utils.TestEnvironment;

/**
 * Interface that wraps a WebElement in CheckBox functionality.
 */
@ImplementedBy(WebtableImpl.class)
public interface Webtable extends Element {
    /**
     * Deprecated... should be removed
     */
    /**
     * @summary - Return the Cell of the specified row and Column in a Webtable <br>
     * Deprecated 6/1/2015<br>
     * New Method {@link #getCell(TestEnvironment, int, int)}
     */
    @Deprecated
    WebElement getCell( WebDriver driver, int row, int column) ;
    
    /**
     * @summary - Get the row count of the Webtable
     * Deprecated 6/1/2015<br>
     * New Method {@link #getRowCount(TestEnvironment)}
     */
    @Deprecated
    int getRowCount(WebDriver driver);
    
    /**
     * @summary - Get cell data of the specified row and Column in a Webtable
     * Deprecated 6/1/2015<br>
     * New Method {@link #getCellData(TestEnvironment, int, column)}
     */
    @Deprecated
    String getCellData( WebDriver driver, int row, int column) ;
    
    /**
     * @summary - Get the column count for the Webtable on a specified Row
     * Deprecated 6/1/2015<br>
     * New Method {@link #getColumnCount(TestEnvironment, int)}
     */
    @Deprecated
    int getColumnCount(WebDriver driver, int row) ;

    /**
     * @summary - Click cell in the specified row and Column in a Webtable
     * Deprecated 6/1/2015<br>
     * New Method {@link #clickCell(TestEnvironment, int, int)}
     */
    @Deprecated
    void clickCell( WebDriver driver, int row, int column)  ;
    /**
     * @summary - Get Row number where text is found
     * Deprecated 6/1/2015<br>
     * New Method {@link #getRowWithCellText(TestEnvironment, String)}
     */

    @Deprecated
    int getRowWithCellText(WebDriver driver, String text);

    /**
     * @summary - Get Row number where text is found in a specific column
     * Deprecated 6/1/2015<br>
     * New Method {@link #getRowWithCellText(TestEnvironment, String, int)}
     */    

    @Deprecated
    int getRowWithCellText( WebDriver driver, String text, int columnPosition);

    /**
     * @summary - Get Row number where text is found in a specific column and starting row
     * Deprecated 6/1/2015<br>
     * New Method {@link #getRowWithCellText(TestEnvironment, String, int, int)}
     */    

    @Deprecated
    int getRowWithCellText( WebDriver driver, String text, int columnPosition, int startRow);


    /**
     * @summary - Get Column number where text is found
     * Deprecated 6/1/2015<br>
     * New Method {@link #getColumnWithCellText(TestEnvironment, String)}
     */  
    @Deprecated
    int getColumnWithCellText(WebDriver driver, String text);
    
    /**
     * @summary - Get Column number where text is found in a specific row
     * Deprecated 6/1/2015<br>
     * New Method {@link #getColumnWithCellText(TestEnvironment, String, int)}
     */  
    @Deprecated
    int getColumnWithCellText(WebDriver driver, String text, int rowPosition);
    
    /**
     * @summary - Get Row number where text is found within a specific column - using 'contains'
     * Deprecated 6/1/2015<br>
     * New Method {@link #getRowWithCellText(TestEnvironment, String , int , int , boolean)}
     */    
    @Deprecated
    int getRowThatContainsCellText( WebDriver driver, String text, int columnPosition);
    /**
     * End of Deprecations
     */
    
    /**
     * @summary - Get the row count of the Webtable
     */
    int getRowCount(TestEnvironment te);

    /**
     * @summary - Get the column count for the Webtable on a specified Row
     */
    int getColumnCount(TestEnvironment te, int row) ;

    /**
     * @summary - Get cell data of the specified row and Column in a Webtable
     */
    String getCellData( TestEnvironment te, int row, int column) ;
    

   
    /**
     * @summary - Return the Cell of the specified row and Column in a Webtable
     */
    WebElement getCell( TestEnvironment te, int row, int column) ;
    

    /**
     * @summary - Click cell in the specified row and Column in a Webtable
     */
    void clickCell( TestEnvironment te, int row, int column)  ;
    
    
    /**
     * @summary - Get Row number where text is found
     */
    int getRowWithCellText(TestEnvironment te, String text);

    /**
     * @summary - Get Row number where text is found in a specific column
     */    
    int getRowWithCellText(TestEnvironment te, String text, int columnPosition);

    /**
     * @summary - Get Row number where text is found in a specific column and starting row
     */    
    int getRowWithCellText(TestEnvironment te, String text, int columnPosition, int startRow);
    
    /**
     * @summary - Get Row number where text is found in a specific column and starting row and case can be ignored
     */    
    int getRowWithCellText(TestEnvironment te, String text, int columnPosition, int startRow, boolean exact);
    
    /**
     * @summary - Get Column number where text is found
     */  
    int getColumnWithCellText(TestEnvironment te, String text);
    
    /**
     * @summary - Get Column number where text is found in a specific row
     */  
    int getColumnWithCellText(TestEnvironment te, String text, int rowPosition);
    
}