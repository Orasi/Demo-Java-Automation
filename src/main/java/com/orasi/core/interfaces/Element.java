package com.orasi.core.interfaces;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.internal.Coordinates;
import org.openqa.selenium.internal.Locatable;
import org.openqa.selenium.internal.WrapsElement;
import org.openqa.selenium.support.FindBy;

import com.orasi.core.interfaces.impl.ElementImpl;
import com.orasi.core.interfaces.impl.internal.ImplementedBy;


/**
 * wraps a web element interface with extra functionality. Anything added here will be added to all descendants.
 */
@ImplementedBy(ElementImpl.class)
public interface Element extends WebElement, WrapsElement, Locatable {
    
    /**
     * @author Justin     
     * @see main.java.com.orasi.core.interfaces.impl.ElementImpl#clear()
     * @summary - used to clear text entry areas; has not effect on any other elements
     */
    @Override
    void clear();
    
    /**
     * @author Justin
     * @summary - draws the focus to an object using Actions
     */
    void focus(WebDriver driver);
    
    /**
     * @author Justin
     * @see org.openqa.selenium.WebElement#click()
     * @summary - default Selenium click
     */
    @Override
    void click();	
    
    /**
     * @summary - click an element using a JavascriptExecutor
     * @param driver - Current active WebDriver object
     */
    void jsClick(WebDriver driver);
    
    /**
     * @author Justin
     * @see org.openqa.selenium.WebElement#click()
     * @summary - draws the focus to an object and clicks the object using Actions
     * @param driver - Current active WebDriver object
     */
    void focusClick(WebDriver driver);

    /**
     * @author Justin 
     * @param keysToSend - an array of characters or string literals
     * @see main.java.com.orasi.core.interfaces.impl.ElementImpl#sendKeys(java.lang.CharSequence...)
     * @summary - sends the char sequence to the element if the sequnce is not an empty string
     */
    @Override
    void sendKeys(CharSequence... keysToSend);
    
    /**
     * @author Justin
     * @see org.openqa.selenium.WebElement#submit
     * @summary - submits form to remote server; exception thrown if the element is not within a form
     */
    @Override
    void submit();

    /**
     * @author Justin
     * @param by - Search for specified {@link By} location and return it's {@link WebElement}
     * @return {@link WebElement}
     * @see main.java.com.orasi.core.interfaces.impl.ElementImpl#findElement()
     */
    @Override
    WebElement findElement(By by);

    /**
     * @author Justin
     * @param by - Search for specified {@link By} location and return all elements found in a {@link List}
     * @return {@link List} 
     * @see main.java.com.orasi.core.interfaces.impl.ElementImpl#findElements()
     */
    @Override
    List<WebElement> findElements(By by);
    

    /**
     * @author Justin
     * @param name - Search for specified attribute and return it's value
     * @return {@link String} Value of specified attribute
     * @see main.java.com.orasi.core.interfaces.impl.ElementImpl#getAttribute()
     */    
    @Override
    String getAttribute(String name);

    ArrayList getAllAttributes(WebDriver driver);
    /**
     * @author Justin
     * @param propertyName - Search for specified property and return it's value
     * @return {@link String} Value of specified property
     * @see main.java.com.orasi.core.interfaces.impl.ElementImpl#getCssValue()
     */
   @Override
String getCssValue(String propertyName);


   /**
    * @return {@link Coordinates} 
    * @see org.orasi.chameleon.interfaces.impl.ElementImpl#getCoordinates();
    */
  @Override
Coordinates getCoordinates();

   /**
    * @author Justin
    * @return {@link Point} Return x and y location
    * @see main.java.com.orasi.core.interfaces.impl.ElementImpl#getLocation()
    */
   @Override
Point getLocation();
   
   /**
    * @author Justin
    * @return {@link Dimension} Return height and width of element
    * @see main.java.com.orasi.core.interfaces.impl.ElementImpl#getSize()
    */
   @Override
Dimension getSize();

   /**
    * @author Justin
    * @return {@link String} Text value in element
    * @see main.java.com.orasi.core.interfaces.impl.ElementImpl#getText()
    */
   @Override
String getText();

   /**
    * @author Justin
    * @return {@link String} Tag value in element
    * @see main.java.com.orasi.core.interfaces.impl.ElementImpl#getTagName()
    */
   @Override
String getTagName();
   
   /**
    * @author Justin
    * @return {@link Boolean} Return TRUE if element is enabled, FALSE if it is not 
    * @see main.java.com.orasi.core.interfaces.impl.ElementImpl#isEnabled()
    */
   @Override
boolean isEnabled();
   
   /**
    * @author Justin
    * @return {@link Boolean} Return TRUE if element is Displayed, FALSE if it is not  
    * @see main.java.com.orasi.core.interfaces.impl.ElementImpl#isDisplayed()
    */
   @Override
boolean isDisplayed();
   
    /**
     * @summary - Returns true when the inner element is ready to be used.
     * @author Justin
     * @return boolean true for an initialized WebElement, or false if we were somehow passed a null WebElement.
     */
    boolean elementWired();

    /**
     * @summary - Used in conjunction with WebObjectPresent to determine if the desired element is built in the DOM
     * 		Will loop for the number of seconds listed in com.orasi.utils.Constants
     * 		If element is not present in the DOM within the time, throw an error
     * @author Justin
     * @param driver - Current active WebDriver object
     * @return true if the element is found in the DOM, false otherwise
     */
    boolean syncPresent(WebDriver driver);

    /**
     * @summary - Used in conjunction with WebObjectPresent to determine if the desired element is built in the DOM
     * 		Will loop for the amount of seconds passed in the variable TIMEOUT
     * 		If element is not present in the DOM within the time, throw an error
     * @author Justin
     * @param driver - Current active WebDriver object
     * @param timeout - Integer time in seconds used to allow the object to become present in the DOM
     * @return true if the element is found in the DOM, false otherwise
     */
    //boolean syncPresent(WebDriver driver, int timeout);

    /**
     * @summary - Used in conjunction with WebObjectPresent to determine if the desired element is built in the DOM
     * 		Will loop for the amount of seconds passed in the variable TIMEOUT
     * 		If object is not visible within the time, handle the error based on the boolean
     * @author Justin
     * @param driver - Current active WebDriver object
     * @param timeout - Integer time in seconds used to allow the object to become present in the DOM
     * @param returnError - true: fails the test if the element is not present in the DOM after <timeout> seconds
     * @param returnError - false: does not fail the test if the element is not present in the DOM after <timeout> seconds
     * @return true if the element is found in the DOM, false otherwise
     */
    boolean syncPresent(WebDriver driver, int timeout, boolean returnError);

    /**     
     * @summary - Used in conjunction with WebObjectVisible to determine if the desired element is visible on the screen 
     * 		Will loop for the number of seconds listed in com.orasi.utils.Constants
     * 		If object is not visible within the time, throw an error
     * @author Justin
     * @param driver - Current active WebDriver object
     */
    boolean syncVisible(WebDriver driver);
    
    /**
     * @summary - Used in conjunction with WebObjectVisible to determine if the desired element is visible on the screen 
     * 		Will loop for the amount of seconds passed in the variable TIMEOUT
     * 		If object is not visible within the time, throw an error 
     * @author Justin
     * @param driver - Current active WebDriver object
     * @param timeout - Integer time in seconds used to allow the object to become visible on the screen
     */
    //boolean syncVisible(WebDriver driver, int timeout);
    
    /**
     * @summary - Used in conjunction with WebObjectVisible to determine if the desired element is visible on the screen 
     * 		Will loop for the amount of seconds passed in the variable TIMEOUT
     * 		If object is not visible within the time, handle the error based on the boolean
     * @author Justin
     * @param driver - Current active WebDriver object
     * @param timeout - Integer time in seconds used to allow the object to become visible on the screen
     * @param returnError - true: fails the test if the element is visible on the screen after <timeout> seconds
     * @param returnError - false: does not fail the test if the element is visible on the screen after <timeout> seconds
     */
    boolean syncVisible(WebDriver driver, int timeout, boolean returnError);
    
    /**     
     * @summary - Used in conjunction with WebObjectVisible to determine if the desired element is hidden from the screen 
     * 		Will loop for the number of seconds listed in com.orasi.utils.Constants
     * 		If object is not hidden within the time, throw an error
     * @author Justin
     * @param driver - Current active WebDriver object
     */
    boolean syncHidden(WebDriver driver);
    
    /**
     * @summary - Used in conjunction with WebObjectVisible to determine if the desired element is hidden from the screen 
     * 		Will loop for the amount of seconds passed in the variable TIMEOUT
     * 		If object is not hidden within the time, throw an error
     * @author Justin
     * @param driver - Current active WebDriver object
     * @param timeout - Integer time in seconds used to allow the object to become hidden on the screen
     */
    //boolean syncHidden(WebDriver driver, int timeout);
    
    /**
     * @summary - Used in conjunction with WebObjectVisible to determine if the desired element is hidden on the screen 
     * 		Will loop for the amount of seconds passed in the variable TIMEOUT
     * 		If object is still hidden within the time, handle the error based on the boolean
     * @author Justin
     * @param driver - Current active WebDriver object
     * @param timeout - Integer time in seconds used to allow the object to become hidden on the screen
     * @param returnError - true: fails the test if the element is not hidden on the page after <timeout> seconds
     * @param returnError - false: does not fail the test if the element is hidden on the page after <timeout> seconds
     */
    boolean syncHidden(WebDriver driver,int timeout, boolean returnError);
    
    /**     
     * @summary - Used in conjunction with WebObjectEnabled to determine if the desired element is enabled on the screen 
     * 		Will loop for the number of seconds listed in com.orasi.utils.Constants
     * 		If object is not visible within the time, throw an error
     * @author Justin
     * @param driver - Current active WebDriver object
     */
    boolean syncEnabled(WebDriver driver);
    
    /**
     * @summary - Used in conjunction with WebObjectEnabled to determine if the desired element is enabled on the screen 
     * 		Will loop for the amount of seconds passed in the variable TIMEOUT
     * 		If object is not visible within the time, throw an error 
     * @author Justin
     * @param driver - Current active WebDriver object
     * @param timeout - Integer time in seconds used to allow the object to become hidden on the screen
     */
    //boolean syncEnabled(WebDriver driver, int timeout);
    
    /**
     * @summary - Used in conjunction with WebObjectEnabled to determine if the desired element is enabled on the screen 
     * 		Will loop for the amount of seconds passed in the variable TIMEOUT
     * 		If object is not visible within the time, handle the error based on the boolean
     * @author Justin 
     * @param driver - Current active WebDriver object
     * @param timeout - Integer time in seconds used to allow the object to become enabled
     * @param returnError - true: fails the test if the element is not enabled after <timeout> seconds
     * @param returnError - false: does not fail the test if the element is enabled after <timeout> seconds
     */
    boolean syncEnabled(WebDriver driver, int timeout, boolean returnError);
    
    /**     
     * @summary - Used in conjunction with WebObjectEnabled to determine if the desired element is disabled from the screen 
     * 		Will loop for the number of seconds listed in org.orasi.chameleon.CONSTANT.TIMEOUT
     * 		If object is not disabled within the time, throw an error
     * @author Justin
     * @param driver - Current active WebDriver object
     */
    boolean syncDisabled(WebDriver driver);
    
    /**
     * @summary - Used in conjunction with WebObjectDisabled to determine if the desired element is disabled from the screen 
     * 		Will loop for the amount of seconds passed in the variable TIMEOUT
     * 		If object is not disabled within the time, throw an error
     * @author Justin
     * @param driver - Current active WebDriver object
     * @param timeout - Integer time in seconds used to allow the object to become disabled
     */
    //boolean syncDisabled(WebDriver driver, int timeout);
    
    /**
     * @summary - Used in conjunction with WebObjectDisabled to determine if the desired element is disabled  on the screen 
     * 		Will loop for the amount of seconds passed in the variable TIMEOUT
     * 		If object is still disabled within the time, handle the error based on the boolean
     * @author Justin
     * @param driver - Current active WebDriver object
     * @param timeout - Integer time in seconds used to allow the object to become disabled
     * @param returnError - true: fails the test if the element is not disabled after <timeout> seconds
     * @param returnError - false: does not fail the test if the element is disabled after <timeout> seconds
     */
    boolean syncDisabled(WebDriver driver,int timeout, boolean returnError);
    
    /**
     * @summary - Used in conjunction with WebObjectTextPresentto determine if the desired text is displayed in the specified element 
     * 		Will loop for the number of seconds listed in org.orasi.chameleon.CONSTANT.TIMEOUT
     * 		If object is not disabled within the time, throw an error
     * @author Justin
     * @param driver - Current active WebDriver object
     * @param text - Text to search for
     */
    boolean syncTextInElement(WebDriver driver, String text);  
    
    /**
     * @summary - Used in conjunction with WebObjectTextPresentto determine if the desired text is displayed in the specified element 
     * 		Will loop for the amount of seconds passed in the variable TIMEOUT
     * 		If object is not disabled within the time, throw an error
     * @author Justin
     * @param driver - Current active WebDriver object
     * @param text - Text to search for
     * @param timeout - Signify a timeout to be used (10 will wait for ten seconds)
     */
    //boolean syncTextInElement(WebDriver driver, String text, int timeout) ;
 
    /**
     * @summary - Used in conjunction with WebObjectTextPresentto determine if the desired text is displayed in the specified element 
     * 		Will loop for the amount of seconds passed in the variable TIMEOUT
     * 		If object is still disabled within the time, handle the error based on the boolean
     * @author Justin
     * @param driver - Current active WebDriver object
     * @param text - Text to search for
     * @param timeout - Signify a timeout to be used (10 will wait for ten seconds)
     * @param returnError - true to throw and error if object is not visible on screen, false if error is not to be thrown
     */
    boolean syncTextInElement(WebDriver driver, String text,int timeout, boolean returnError);
    
    /**
     * @author Justin
     * @return locator - type of element that was used to create element using {@link FindBy}
     */
    By getElementLocator();
    
    
    /**
     * @author Justin
     * @return {@link By} locator value of element that was used to create element using {@link FindBy}
     */
    String getElementIdentifier();
    
    /**
     * @author Justin
     * @return locator identifier and the value of element that was used to create element using {@link FindBy}
     */
    String getElementLocatorInfo();
    
    /**
     * @summary - Used to highlight and element on a page
     * @author Waits
     * @param driver 
     * @return NA
     */
	public void highlight(WebDriver driver);
    
    /**
     * @summary - Used to highlight and element on a page
     * @author Waits
     * @param driver - Current active WebDriver object
     */
	public void scrollIntoView(WebDriver driver);
}