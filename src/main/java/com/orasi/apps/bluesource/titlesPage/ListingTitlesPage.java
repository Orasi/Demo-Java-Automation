package com.orasi.apps.bluesource.titlesPage;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import ru.yandex.qatools.allure.annotations.Step;

import com.orasi.api.restServices.blueSource.BlueSource;
import com.orasi.api.restServices.blueSource.titles.Title;
import com.orasi.core.interfaces.Element;
import com.orasi.core.interfaces.Label;
import com.orasi.core.interfaces.Link;
import com.orasi.core.interfaces.Webtable;
import com.orasi.core.interfaces.impl.ElementImpl;
import com.orasi.core.interfaces.impl.internal.ElementFactory;
import com.orasi.utils.AlertHandler;
import com.orasi.utils.TestEnvironment;
import com.orasi.utils.TestReporter;

public class ListingTitlesPage {
    	private TestEnvironment te = null;
	
	//All the page elements
	@FindBy(linkText = "New Title")	
	private Link lnkNewTitle;
	
	@FindBy(xpath = "//h1[text() = 'Listing titles']")
	private Label lblTitle;

	@FindBy(css = ".alert-success.alert-dismissable")
	private Label lblSuccessMsg;
	
	@FindBy(className = "table")
	private Webtable tabTitles;
	
	private By editIcon = By.cssSelector("div:nth-child(1) > a:nth-child(1)");
	private By deleteIcon = By.cssSelector("div:nth-child(1) > a:nth-child(2)");
	
	// *********************
	// ** Build page area **
	// *********************
	public ListingTitlesPage(TestEnvironment te){
	    this.te = te;
	    ElementFactory.initElements(te.getDriver(), this);
	}	
	
	public boolean pageLoaded(){
	    return te.pageLoaded().isElementLoaded(this.getClass(), lnkNewTitle);
	}

	// *****************************************
	// ***Page Interactions ***
	// *****************************************

	@Step("And I click the \"New Title\" link")
	public void clickNewTitle(){
	    lnkNewTitle.click();
	}
		
	public boolean isTitleHeaderDisplayed(){
	    return lblTitle.isDisplayed();
	}
	
	@Step("And I click the \"Edit Title\" icon on the row for title \"{0}\"")
	public void clickModifyTitle(String title){	    
	    Element titleCell = getTitleRowElement(title);
	    new ElementImpl(titleCell.findElement(editIcon)).click();
	}
	
	@Step("Then an alert should appear for confirmation")
	public boolean isSuccessMsgDisplayed() {	 
	    return lblSuccessMsg.syncVisible(te.getDriver());
	}
	
	@Step("And the title \"{0}\" should be found on the Titles table")
	public boolean searchTableByTitle(String title){
	    if(getTitleRowPosition(title) > 0) return true;
	    return false;
	}
	
	@Step("And I can delete the title from the table")
	public void deleteTitle(String title){
	    Element titleCell = getTitleRowElement(title);
	    new ElementImpl(titleCell.findElement(deleteIcon)).click();
	    
	    AlertHandler.handleAlerts(te.getDriver(), 2);
	}

	public void ensureNoExistingTitle(String title){
	    if (searchTableByTitle(title)){
		BlueSource blueSource = new BlueSource("Company.admin");
		List<Title>  titles = blueSource.titles().getAllTitles();
		Title tempTitle = null;
		Iterator<Title> titleIterator = titles.iterator();
		while (titleIterator.hasNext()) {
		    tempTitle = titleIterator.next();
		    if(tempTitle.getName().equals(title)) blueSource.titles().deleteTitle(tempTitle);
		}
		TestReporter.log("The title of \"" + title + "\" previously existed. Deleting previous title");
		
	    }
	}
	
	private Element getTitleRowElement(String title){
	    int titleRow = getTitleRowPosition(title);
	    return new ElementImpl(tabTitles.getCell(te, titleRow, 1));
	}
	
	private int getTitleRowPosition(String title){
	    return tabTitles.getRowWithCellText(te, title, 1,1,false);
	}
}
