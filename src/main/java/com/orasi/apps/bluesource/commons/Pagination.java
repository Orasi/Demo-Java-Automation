package com.orasi.apps.bluesource.commons;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.orasi.core.interfaces.Button;
import com.orasi.core.interfaces.Element;
import com.orasi.core.interfaces.impl.ButtonImpl;
import com.orasi.core.interfaces.impl.ElementImpl;
import com.orasi.core.interfaces.impl.internal.ElementFactory;
import com.orasi.utils.OrasiDriver;
import com.orasi.utils.TestEnvironment;

/**
 * @author justin.phlegar@orasi.com
 * 
 * @doc.description
 * Class for handling the common Pagination element (page selector) seen on the Bluesource site
 */
public class Pagination {
    @FindBy(className = "pagination")
    private Element pagination;
    
    @SuppressWarnings("unused")
    private OrasiDriver driver = null;

    // *********************
    // ** Build page area **
    // *********************
    /**
     * 
     * @param {@link TestEnvironment} te
     * @doc.description If only TestEnvironment is passed in, use PageFactory method to find the Pagination element
     */
    public Pagination(OrasiDriver driver){
	this.driver = driver;
 	ElementFactory.initElements(driver, this);
 	driver.pageLoaded(this.getClass(), pagination);
    } 	
  
    /**
     * 
     * @param {@link TestEnvironment} te
     * @param Element pagination
     * @doc.description Element pagination passed in will be used for interactions
     */
    public Pagination (OrasiDriver driver, Element pagination){
	this.driver = driver;
	this.pagination = pagination;
    }
    

    /**
     * 
     * @param {@link TestEnvironment} te
     * @param WebElement pagination
     * @doc.description WebElement pagination will be converted to Element for additional low-level reports
     */
    public Pagination (OrasiDriver driver, WebElement pagination){
	this(driver, new ElementImpl(pagination,driver));
    }
    

    /**
     * 
     * @return String - Current page number
     * @doc.description The Pagination button element with the class "active" is the current page.
     * Return the text of the element. 
     */
    
    public String getCurrentPage(){
	return pagination.findElement(By.className("active")).getText();
    }
    
    /**
     * 
     * @return Boolean 
     * @doc.description Returns true if successful page move, false if it stays on the same page
     */
    public boolean moveNext(){
	if(!pagination.isDisplayed()) return false;
	String currentPage = getCurrentPage();
	pagination.findElement(By.cssSelector("li:last-child > a")).click();		
	String nextPage = getCurrentPage();
	if (currentPage.equals(nextPage)) return false;
	return true;
    }
    

    /**
     * 
     * @return Boolean 
     * @doc.description Returns true if successful page move, false if it stays on the same page
     */
    public boolean movePrevious(){
	if(!pagination.isDisplayed()) return false;
	String currentPage = getCurrentPage();
	new ButtonImpl(pagination.findElement(By.cssSelector("li:last-child > a")),(OrasiDriver)pagination.getWrappedDriver()).click();
	String nextPage = getCurrentPage();
	if (currentPage.equals(nextPage)) return false;
	return true;	
    }
}
