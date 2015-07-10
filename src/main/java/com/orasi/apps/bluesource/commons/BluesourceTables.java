package com.orasi.apps.bluesource.commons;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.orasi.core.interfaces.Element;
import com.orasi.core.interfaces.Link;
import com.orasi.core.interfaces.Listbox;
import com.orasi.core.interfaces.Webtable;
import com.orasi.core.interfaces.impl.ElementImpl;
import com.orasi.core.interfaces.impl.LinkImpl;
import com.orasi.core.interfaces.impl.WebtableImpl;
import com.orasi.core.interfaces.impl.internal.ElementFactory;
import com.orasi.utils.TestEnvironment;


/**
 * @author justin.phlegar@orasi.com
 * 
 * @doc.description
 * Class for handling the common table element seen on the Bluesource site. Contains methods
 * that are common and valid for all tables on site.
 */

public class BluesourceTables {
    
    @FindBy(className = "table")
    private Webtable table;
    
    @FindBy(id = "loading-section")
    private Element loadingModal;
    
    @FindBy(id = "preference_resources_per_page")
    private Listbox lstRowsPerPage;
    
    private TestEnvironment te = null;
    
    /**
     * 
     * @param {@link TestEnvironment} te
     * @doc.description If only TestEnvironment is passed in, use PageFactory method to find the WebTable element
     */
    public BluesourceTables (TestEnvironment te){
	this.te = te;	
    	ElementFactory.initElements(te.getDriver(), this);
    	te.pageLoaded().isElementLoaded(this.getClass(), table);
    }
    
    /**
     * 
     * @param {@link TestEnvironment} te
     * @param {@link Webtable} table
     * @doc.description  Webtable table passed in will be used for interactions
     */
    public BluesourceTables (TestEnvironment te, Webtable table){
	this.te = te;
	this.table = table;
    }
    
    /**
     * 
     * @param {@link TestEnvironment} te
     * @param {@link WebElement} table
     * @doc.description WebElement table will be converted to WebTable for access to additional methods for WebTable
     */
    public BluesourceTables (TestEnvironment te, WebElement table){
	this(te, new WebtableImpl(table));
    }
   
    /**
     * 
     * @param {@link TestEnvironment} te
     * @param {@link Element} table
     * @doc.description Element table will be converted to WebTable for access to additional methods for WebTable
     */
    public BluesourceTables (TestEnvironment te,Element table){
	this(te, new WebtableImpl(table));
    }
    
    /**
     * @doc.description Sync handler for the Loading modal 
     */
    public void loadingDone(){
	loadingModal.syncHidden(te.getDriver());
    }
    
    /**
     * 
     * @param String column - Column name to search for 
     * @return int - column position
     * @doc.description Just wrapping a reusable action for quicker usability 
     */
    public int getColumnPosition(String column){
	return table.getColumnWithCellText(te, column);
    }
    
    /**
     *         
     * @param String column - Column name to sort on
     * @param {@link SortOrder} order - Sort order for selected column
     * @doc.description 
     */
    public void sortColumn(String column, SortOrder order){
	loadingDone();
	int columnPosition = getColumnPosition(column);
	
	// Create two elements. First is the cell itself. The cell contains a link and an icon.
	// The link is created from its parent cell. To sort, the link needs to be clicked. Once 
	// clicked, an icon in the cell will update with its current sort order  
	Element cell = new ElementImpl(table.getCell(te, 1, columnPosition));
	Link cellLink = new LinkImpl(cell.findElement(By.xpath("a")));
	
	cellLink.click();
	te.pageLoaded().isDomComplete();
	
	String currentOrder = cell.findElement(By.cssSelector("span.glyphicon-sort-by-alphabet")).getAttribute("ng-show");
	if(order == SortOrder.ASCENDING){
	    if(currentOrder.equals("reverse==false")) cellLink.click();
	}else{
	    if(currentOrder.equals("reverse==true")) cellLink.click();
	}
    }    
    
    /**
     * 
     * @param String column - Name of the column to validate
     * @param {@link SortOrder} order - Sort order expected for selected column
     * @return boolean - If sorting was applied properly, then true is returned
     */
    public boolean validateSortColumn(String column, SortOrder order){
	loadingDone();
	int columnPosition = getColumnPosition(column);
	int numberRows = table.getRowCount(te);
	int currentRow = 3;
	boolean movedPage = true;
	boolean result = false;
	String firstValidationValue = "";
	String secondValidationValue = "";
	firstValidationValue = table.getCellData(te, 2, columnPosition);
	
	// Loop through the specified column until it finds a value different from the first cell
	// If it reaches the end of the table, it will click the next button to move to the next
	// page of table values. If it is the last page or the Pagination buttons are not displayed,
	// then all values were equivalent.
	do {	    
	    secondValidationValue = table.getCellData(te, currentRow, columnPosition);
	    currentRow++;
	    
	    if(!firstValidationValue.equals(secondValidationValue)){
		// Found a difference, break out of loop to compare
		break;
	    }
	    
	    if(currentRow==numberRows+1) {
		currentRow = 2;
		movedPage = new Pagination(te).moveNext();
		te.pageLoaded().isDomComplete();
		numberRows = table.getRowCount(te);
	    }
	} while (movedPage);	
	
	// Use the compareTo method to determine if sorting was applied properly
	// CompareTo will return a negative int if the first value is less than the second
	// and return a positive int if the second value was greater than the first. 0 is returned
	// if both values were equivalent
	int compare = firstValidationValue.compareToIgnoreCase(secondValidationValue);
		
	   if( compare < 0 && order.equals(SortOrder.ASCENDING)){
	       //first value is less than second value...EXPECTED RESULT
	       result = true;
	   }else if(compare > 0 && order.equals(SortOrder.ASCENDING)){
	       //second value is less than first value...NOT EXPECTED RESULT
	       result = false;
	   }else if( compare > 0 && order.equals(SortOrder.DESCENDING)){
	       //first value is less than second value...EXPECTED RESULT
	       result = true;
	   }else if(compare < 0 && order.equals(SortOrder.DESCENDING)){
	       //second value is less than first value...NOT EXPECTED RESULT
	       result = false;
	   }else{
	       //values were equivalent... can be expected
	       result = true;
	   }	   
		
	return result;
    }   
    
    public void selectFieldLink(String fieldText){
    	new LinkImpl(table.findElements(By.xpath("tbody/tr/td/a[text()='" + fieldText + "']")).get(0)).click();
    }
 
    public boolean validateTextInTable(String text, String column){
	loadingDone();
	int row = 0;
	int columnPosition = getColumnPosition(column);
	row = table.getRowWithCellText(te, text, columnPosition, 2, false );
	if(row != 0) return true;
	return false;
    }
    
    public String getRowsPerPageDisplayed(){
	return lstRowsPerPage.findElement(By.cssSelector("option[selected='selected']")).getText();
    }
    
    public void setRowsPerPageDisplayed(String value){
	loadingDone();	
	lstRowsPerPage.syncEnabled(te.getDriver());
	lstRowsPerPage.select(value);
    }
    
    public boolean validateRowsPerPageDisplayed(String numberOfRows){
	return numberOfRows.equals(String.valueOf(table.getRowCount(te) - 1));
    }
    
    
}
