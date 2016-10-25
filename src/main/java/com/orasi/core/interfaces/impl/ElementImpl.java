package com.orasi.core.interfaces.impl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.time.StopWatch;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitWebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.internal.Coordinates;
import org.openqa.selenium.internal.Locatable;
import org.openqa.selenium.internal.WrapsElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.orasi.core.Beta;
import com.orasi.core.by.angular.ByNG;
import com.orasi.core.interfaces.Element;
import com.orasi.exception.AutomationException;
import com.orasi.exception.automation.ElementAttributeValueNotMatchingException;
import com.orasi.exception.automation.ElementCssValueNotMatchingException;
import com.orasi.exception.automation.ElementNotDisabledException;
import com.orasi.exception.automation.ElementNotEnabledException;
import com.orasi.exception.automation.ElementNotHiddenException;
import com.orasi.exception.automation.ElementNotVisibleException;
import com.orasi.exception.automation.TextInElementNotPresentException;
import com.orasi.utils.Constants;
import com.orasi.utils.ExtendedExpectedConditions;
import com.orasi.utils.OrasiDriver;
import com.orasi.utils.TestReporter;
import com.orasi.utils.debugging.Highlight;

/**
 * An implementation of the Element interface. Delegates its work to an
 * underlying WebElement instance for custom functionality.
 */
public class ElementImpl implements Element {

	protected WebElement element;
	protected By by;
	protected ByNG byNG;
	protected OrasiDriver driver;

	public ElementImpl(final WebElement element) {
		this.element = element;
		driver = getWrappedDriver();
		by = getElementLocator();

	}

	public ElementImpl( final OrasiDriver driver, final By by) {
		this.by= by;
		this.driver = driver;
		try{
			TestReporter.logTrace("Entering ElementImpl#init");
			TestReporter.logTrace("Inital search for element [ " + by + "]");
			WebDriverWait wait = new WebDriverWait(driver.getWebDriver(), 1);
			element = wait.until(ExpectedConditions.presenceOfElementLocated(by));
			TestReporter.logTrace("Element [ " + by + "] found and stored");			
		}catch(NoSuchElementException throwAway){
			TestReporter.logTrace("Element [ " + by + "] NOT found intially, will search again later");
		}
		TestReporter.logTrace("Exiting ElementImpl#init");
	}



	/**
	 * @see org.openqa.selenium.WebElement#click()
	 */
	public void click() {
		TestReporter.logTrace("Entering ElementImpl#click");
		try {
			getWrappedElement().click();
		} catch (RuntimeException rte) {
			TestReporter.interfaceLog("Clicked [ <font size = 2 color=\"red\"><b> " + getElementLocatorInfo() + " </font></b>]");
			throw rte;
		}
		TestReporter.interfaceLog("Clicked [ <b>" + getElementLocatorInfo() + " </b>]");
		TestReporter.logTrace("Exiting ElementImpl#click");
	}

	public void jsClick() {
		TestReporter.logTrace("Entering ElementImpl#clicjsClickk");
		getWrappedDriver().executeJavaScript("arguments[0].scrollIntoView(true);arguments[0].click();", getWrappedElement());
		TestReporter.interfaceLog("Clicked [ <b>" + getElementLocatorInfo() + " </b>]");
		TestReporter.logTrace("Exiting ElementImpl#jsClick");
	}

	@Override
	public void focus() {
		TestReporter.logTrace("Entering ElementImpl#focus");
		new Actions(getWrappedDriver()).moveToElement(getWrappedElement()).perform();
		TestReporter.interfaceLog("Focus on  [ <b>" + getElementLocatorInfo() + " </b>]");
		TestReporter.logTrace("Exiting ElementImpl#focus");
	}

	@Override
	public void focusClick() {
		TestReporter.logTrace("Entering ElementImpl#focusClick");
		new Actions(getWrappedDriver()).moveToElement(getWrappedElement()).click().perform();
		TestReporter.interfaceLog("Focus Clicked [ <b>" + getElementLocatorInfo() + " </b>]");
		TestReporter.logTrace("Exiting ElementImpl#focusClick");
	}

	@Override
	public void onBlur(){
		TestReporter.logTrace("Entering ElementImpl#onBlur");
		String jsFireEvent = "if ('createEvent' in document) { " +
				" var evt = document.createEvent('HTMLEvents'); " +
				" evt.initEvent('change', false, true); " +
				" arguments[0].dispatchEvent(evt); " +
				" } else arguments[0].fireEvent('onblur');";

		try{
			getWrappedDriver().executeJavaScript(jsFireEvent, getWrappedElement());
		}catch(WebDriverException wde){}		
		TestReporter.logTrace("Exiting ElementImpl#onBlur");
	}

	/**
	 * @see org.openqa.selenium.WebElement#getLocation()
	 */
	@Override
	public Point getLocation() {
		TestReporter.logTrace("Entering ElementImpl#getLocation");
		Point point = getWrappedElement().getLocation();
		TestReporter.logInfo("Location of element: X = [ " + point.getX() + " ], Y = [ " + point.getY() + " ] ");
		TestReporter.logTrace("Exiting ElementImpl#getLocation");
		return point;
	}

	/**
	 * @see org.openqa.selenium.WebElement#submit()
	 */
	@Override
	public void submit() {
		TestReporter.logTrace("Entering ElementImpl#submit");
		getWrappedElement().submit();
		TestReporter.logTrace("Exiting ElementImpl#submit");
	}

	/**
	 * @see org.openqa.selenium.WebElement#getAttribute(String)
	 */
	@Override
	public String getAttribute(String name) {
		TestReporter.logTrace("Entering ElementImpl#getAttribute");
		String value = getWrappedElement().getAttribute(name);
		TestReporter.logInfo("Attribute value for [ " + name + " ] is [ " +value+" ]");
		TestReporter.logTrace("Exiting ElementImpl#getAttribute");
		return value;
	}

	/**
	 * @see org.openqa.selenium.WebElement#getCssValue(String)
	 */
	@Override
	public String getCssValue(String propertyName) {
		TestReporter.logTrace("Entering ElementImpl#getCssValue");
		String value = getWrappedElement().getCssValue(propertyName);
		TestReporter.logInfo("CSS property value for [ " + propertyName + " ] is [ " +value+" ]");
		TestReporter.logTrace("Exiting ElementImpl#getCssValue");
		return value;
	}

	/**
	 * @see org.openqa.selenium.WebElement#getSize()
	 */
	@Override
	public Dimension getSize() {
		TestReporter.logTrace("Entering ElementImpl#getSize");
		Dimension dimension = getWrappedElement().getSize();
		TestReporter.logInfo("Location of element: height = [ " + dimension.getHeight() + " ], width = [ " + dimension.getWidth() + " ] ");
		TestReporter.logTrace("Exiting ElementImpl#getSize");
		return dimension;
	}

	/**
	 * @see org.openqa.selenium.WebElement#findElement(By)
	 */
	@Override
	public List<WebElement> findElements(By by) {
		TestReporter.logTrace("Entering ElementImpl#findElements");
		List<WebElement> elements = getWrappedElement().findElements(by);
		TestReporter.logTrace("Exiting ElementImpl#findElements");
		return elements;
	}

	/**
	 * @see org.openqa.selenium.WebElement#getText()
	 */
	@Override
	public String getText() {
		TestReporter.logTrace("Entering ElementImpl#getText");
		String text = getWrappedElement().getText();
		TestReporter.logInfo("Text found in element [ " + text + " ]");
		TestReporter.logTrace("Exiting ElementImpl#getText");
		return text;
	}

	/**
	 * @see org.openqa.selenium.WebElement#getTagName()
	 */
	@Override
	public String getTagName() {
		TestReporter.logTrace("Entering ElementImpl#getTagName");
		String name = getWrappedElement().getTagName();
		TestReporter.logInfo("Tagname of element [ " + name + " ]");
		TestReporter.logTrace("Exiting ElementImpl#getTagName");
		return name;
	}

	/**
	 * @see org.openqa.selenium.WebElement#findElement(By)
	 */
	@Override
	public WebElement findElement(By by) {
		TestReporter.logTrace("Entering ElementImpl#findElement");
		WebElement element = getWrappedElement().findElement(by);
		TestReporter.logTrace("Exiting ElementImpl#findElement");
		return element;
	}

	/**
	 * @see org.openqa.selenium.WebElement#isEnabled()
	 */
	@Override
	public boolean isEnabled() {
		TestReporter.logTrace("Entering ElementImpl#isEnabled");
		boolean enabled =  getWrappedElement().isEnabled();
		TestReporter.logTrace("Exiting ElementImpl#isEnabled");
		return enabled;
	}

	/**
	 * @see org.openqa.selenium.WebElement#isDisplayed()
	 */
	@Override
	public boolean isDisplayed() {
		TestReporter.logTrace("Entering ElementImpl#isDisplayed");
		boolean displayed =   getWrappedElement().isDisplayed();
		TestReporter.logTrace("Exiting ElementImpl#isDisplayed");
		return displayed;
	}

	/**
	 * @see org.openqa.selenium.WebElement#isSelected()
	 */
	@Override
	public boolean isSelected() {
		TestReporter.logTrace("Entering ElementImpl#isSelected");
		boolean selected =   getWrappedElement().isSelected();
		TestReporter.logTrace("Exiting ElementImpl#isSelected");
		return selected;
	}

	/**
	 * @see org.openqa.selenium.WebElement#clear()
	 */
	@Override
	public void clear() {
		TestReporter.logTrace("Entering ElementImpl#clear");
		getWrappedElement().clear();
		TestReporter.interfaceLog(" Clear text from Element [ <b>" + getElementLocatorInfo() + " </b> ]");
		TestReporter.logTrace("Exiting ElementImpl#clear");
	}

	/**
	 * @see org.openqa.selenium.WebElement#sendKeys(CharSequence...)
	 */
	@Override
	public void sendKeys(CharSequence... keysToSend) {
		TestReporter.logTrace("Entering ElementImpl#sendKeys");
		if (keysToSend.toString() != "") {
			getWrappedElement().sendKeys(keysToSend);
			TestReporter.interfaceLog(" Send Keys [ <b>" + keysToSend[0].toString() + "</b> ] to Textbox [ <b>"
					+ getElementIdentifier() + " </b> ]");
		}
		TestReporter.logTrace("Exiting ElementImpl#sendKeys");
	}

	@Override
	public WebElement getWrappedElement() {
		TestReporter.logTrace("Entering ElementImpl#getWrappedElement");
		WebElement tempElement = null;
		try{
			TestReporter.logTrace("Validate element [ " + by.toString() + " ] is not null");
			if(element == null){
				TestReporter.logTrace("Element [ " + by.toString() + " ] is null, attempt to reload the element");
				tempElement = reload();
				TestReporter.logTrace("Successfully reloaded element [ " + by.toString() + " ]");
			}else tempElement = element;

			TestReporter.logTrace("Validate element [ " + by.toString() + " ] is not stale");
			tempElement.isEnabled();
			TestReporter.logTrace("Successfully validated element [ " + by.toString() + " ] is usable");
			TestReporter.logTrace("Exiting ElementImpl#getWrappedElement");
			return tempElement;
		}catch(NoSuchElementException |  StaleElementReferenceException| NullPointerException e){

			try{
				TestReporter.logTrace("Element [ " + by.toString() + " ] is stale, attempt to reload the element");
				tempElement=reload();
				TestReporter.logTrace("Successfully reloaded element [ " + by.toString() + " ]");
				TestReporter.logTrace("Exiting ElementImpl#getWrappedElement");
				return tempElement;
			}catch(NullPointerException sere){
				TestReporter.logTrace("Exiting ElementImpl#getWrappedElement");
				return element;
			}
		}
	}

	@Override
	public OrasiDriver getWrappedDriver() {
		if(driver != null) return driver;
		WebDriver ldriver = null;
		Field privateStringField = null;
		if(element == null) getWrappedElement();
		if (driver == null) {
			if (element instanceof ElementImpl) {
				try {
					WebElement wrappedElement = ((WrapsElement) element).getWrappedElement();

					privateStringField = wrappedElement.getClass().getDeclaredField("parent");
					privateStringField.setAccessible(true);
					ldriver =  (WebDriver)privateStringField.get(wrappedElement);
					OrasiDriver oDriver = new OrasiDriver();
					oDriver.setDriver(ldriver);
					return oDriver;

				}catch(NoSuchFieldException | IllegalArgumentException | IllegalAccessException | SecurityException e){
				}	    
			}else{

				try {
					privateStringField = element.getClass().getDeclaredField("parent");
					privateStringField.setAccessible(true);
					ldriver =  (WebDriver)privateStringField.get(element);
					OrasiDriver oDriver = new OrasiDriver();
					oDriver.setDriver(ldriver);
					return oDriver;
				} catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException | SecurityException e) {
					e.printStackTrace();
				}
			}
		}
		return driver;
	}

	/**
	 * @see org.openqa.selenium.internal.Locatable#getCoordinates();
	 */
	@Override
	public Coordinates getCoordinates() {
		return ((Locatable) getWrappedElement()).getCoordinates();
	}

	@Override
	public boolean elementWired() {
		return (getWrappedElement() != null);
	}

	/**
	 * Get the By Locator object used to create this element
	 * 
	 * @author Justin
	 * @return {@link By} Return the By object to reuse
	 */
	@Override
	public By getElementLocator() {
		if(by != null) return this.by;
		By by = null;
		String locator = "";
		try {
			locator = getElementLocatorAsString();
			switch (locator.toLowerCase().replace(" ", "")) {
			case "classname":
				by = By.className(getElementIdentifier());
				break;
			case "cssselector":
				by = By.cssSelector(getElementIdentifier());
				break;
			case "id":
				by = By.id(getElementIdentifier());
				break;
			case "linktext":
				by = By.linkText(getElementIdentifier());
				break;
			case "name":
				by = By.name(getElementIdentifier());
				break;
			case "tagname":
				by = By.tagName(getElementIdentifier());
				break;
			case "xpath":
				by = By.xpath(getElementIdentifier());
				break;
			case "ng-modal":
			case "buttontext":	
			case "ng-controller":	
			case "ng-repeater":
				return null;
			default:
				throw new AutomationException("Unknown Element Locator sent in: " + locator, getWrappedDriver());
			}
			return by;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}


	@Override
	public String getElementIdentifier() {
		String locator = "";
		int startPosition = 0;
		int endPosition = 0;
		if(by == null){
			if (element instanceof HtmlUnitWebElement) {
				startPosition = element.toString().indexOf("=\"") + 2;
				endPosition = element.toString().indexOf("\"", element.toString().indexOf("=\"") + 3);
				if (startPosition == -1 | endPosition == -1)
					locator = element.toString();
				else
					locator = element.toString().substring(startPosition, endPosition);
			} else if (element instanceof ElementImpl) {


				WebElement wrappedElement = ((WrapsElement) element).getWrappedElement(); 
				startPosition = wrappedElement.toString().lastIndexOf(": ") + 2;
				if(startPosition==1){
					startPosition = wrappedElement.toString().indexOf("=\"") + 2;
					endPosition = wrappedElement.toString().indexOf("\"", wrappedElement.toString().indexOf("=\"") + 3);
					if (startPosition == -1 | endPosition == -1)
						locator = wrappedElement.toString();
					else
						locator = wrappedElement.toString().substring(startPosition, endPosition);
				}else{
					locator = wrappedElement.toString().substring(startPosition,wrappedElement.toString().lastIndexOf("]"));
				}


			} else {
				startPosition = element.toString().lastIndexOf(": ") + 2;
				locator = element.toString().substring(startPosition, element.toString().lastIndexOf("]"));
			}
		}else{
			startPosition = by.toString().lastIndexOf(": ") + 2;
			locator = by.toString().substring(startPosition, by.toString().length());
			return locator.trim();
		}

		return locator.trim();
	}

	/**
	 * Get the By Locator object used to create this element
	 * 
	 * @author Justin
	 * @return {@link By} Return the By object to reuse
	 */
	private String getElementLocatorAsString() {
		int startPosition = 0;
		String locator = "";
		if (by ==  null){
			if (element instanceof HtmlUnitWebElement) {
				startPosition = element.toString().indexOf(" ");
				if (startPosition == -1)locator = element.toString();
				else locator = element.toString().substring(startPosition, element.toString().indexOf("="));
			} else if (element instanceof ElementImpl) {
				//Field elementField = null;
				//try {
				WebElement wrappedElement = ((WrapsElement) element).getWrappedElement(); 

				startPosition = wrappedElement.toString().lastIndexOf("->") + 3;
				if(startPosition==2){
					startPosition = wrappedElement.toString().indexOf(" ");
					if (startPosition == -1)
						locator = wrappedElement.toString();
					else
						locator = wrappedElement.toString().substring(startPosition, wrappedElement.toString().indexOf("="));
				}else{
					locator = wrappedElement.toString().substring(startPosition,
							wrappedElement.toString().lastIndexOf(":"));
				}
				//} catch (IllegalAccessException |  SecurityException e) {
				//	e.printStackTrace();
				//}

			} else {

				// if (element instanceof HtmlUnitWebElement)
				startPosition = getWrappedElement().toString().lastIndexOf("->") + 3;
				locator = element.toString().substring(startPosition, element.toString().lastIndexOf(":"));
			}
		}else{
			locator = by.toString().substring(3, by.toString().lastIndexOf(":"));
			return locator.trim();
		}
		locator = locator.trim();
		return locator;

	}


	@Override
	public String getElementLocatorInfo() {
		if (by != null) return by.toString();
		//else return getElementLocatorAsString() + " = " + getElementIdentifier();
		return getElementLocatorAsString() + " = " + getElementIdentifier();
	}

	@Override
	public void highlight() {
		TestReporter.logTrace("Entering ElementImpl#highlight");
		Highlight.highlight(getWrappedDriver(), getWrappedElement());
		TestReporter.logTrace("Exiting ElementImpl#highlight");
	}

	@Override
	public void scrollIntoView() {
		TestReporter.logTrace("Entering ElementImpl#scrollIntoView");
		getWrappedDriver().executeJavaScript("arguments[0].scrollIntoView(true);", element);
		TestReporter.logTrace("Exiting ElementImpl#scrollIntoView");
	}

	@SuppressWarnings("rawtypes")
	@Override
	public ArrayList getAllAttributes() {
		return (ArrayList) driver.executeJavaScript(
				"var s = []; var attrs = arguments[0].attributes; for (var l = 0; l < attrs.length; ++l) { var a = attrs[l]; s.push(a.name + ':' + a.value); } ; return s;",
				getWrappedElement());
	}

	@Beta
	@Override
	public <X> X getScreenshotAs(OutputType<X> target) {
		return ((TakesScreenshot) driver.getWebDriver()).getScreenshotAs(target);
	}

	/**
	 * Used to determine if the desired element is visible on the screen 
	 * Will wait for default element timeout unless new timeout is passed in 
	 * If object is not visible within the time, handle the error based default handler 
	 * or by boolean passed in
	 *
	 * @author Justin
	 * @param args
	 *  		Optional arguments </br>
	 *  		&nbsp;&nbsp;&nbsp;&nbsp;<b>timeout</b> - the maximum time in seconds the method should try to sync. Called 
	 *  							 with syncVisible(10)</br>
	 *  		&nbsp;&nbsp;&nbsp;&nbsp;<b>failTestOnSyncFailure </b>- if TRUE, the test will throw an exception and 
	 *  					fail the script. If FALSE, the script will 
	 *  					not fail, instead a FALSE will be returned 
	 *  					to the calling function. Called with 
	 *  					syncVisible(10, false)
	 */
	public boolean syncVisible(Object... args) {
		TestReporter.logTrace("Entering ElementImpl#syncVisible");
		int timeout = getWrappedDriver().getElementTimeout();
		boolean failTestOnSync = Constants.defaultSyncHandler;
		try{
			if(args[0] != null) timeout = Integer.valueOf(args[0].toString());
			if(args[1] != null) failTestOnSync = Boolean.parseBoolean(args[1].toString());
		}catch(ArrayIndexOutOfBoundsException aiobe){}

		TestReporter.interfaceLog("<i>Syncing to element [<b>" + getElementLocatorInfo()
		+ "</b> ] to be <b>VISIBLE</b> within [ <b>" + timeout + "</b> ] seconds.</i>");

		StopWatch stopwatch = new StopWatch();
		boolean found = false;
		long timeLapse;

		WebDriverWait wait = new WebDriverWait(driver, timeout);
		stopwatch.start();
		try {
			found = wait.pollingEvery(Constants.millisecondsToPollForElement, TimeUnit.MILLISECONDS).until(ExtendedExpectedConditions.elementToBeVisible(reload()));
		} catch (NoSuchElementException | ClassCastException | StaleElementReferenceException | TimeoutException te){}
		stopwatch.stop();
		timeLapse = stopwatch.getTime();
		stopwatch.reset();

		if (!found && failTestOnSync) {
			TestReporter.logTrace("Element not <b>VISIBLE</b> and failTestOnSync is [ TRUE ]");
			TestReporter.interfaceLog("<i>Element [<b>" + getElementLocatorInfo()
			+ " </b>] is not <b>VISIBLE</b> on the page after [ "
			+ (timeLapse) / 1000.0 + " ] seconds.</i>");
			throw new ElementNotVisibleException(
					"Element [ " + getElementLocatorInfo() + " ] is not VISIBLE on the page after [ "
							+ (timeLapse) / 1000.0 + " ] seconds.", driver);
		}
		TestReporter.interfaceLog("<i>Element [<b>" + getElementLocatorInfo()   + " </b>] is <b>VISIBLE</b> on the page after [ " + (timeLapse) / 1000.0 + " ] seconds.</i>");
		if(Highlight.getDebugMode()) Highlight.highlightSuccess(driver, reload());
		TestReporter.logTrace("Exiting ElementImpl#syncVisible");
		return found;

	}

	/**
	 * Used to determine if the desired element is hidden on the screen 
	 * Will wait for default element timeout unless new timeout is passed in 
	 * If object is not hidden within the time, handle the error based default handler 
	 * or by boolean passed in
	 * 
	 * @author Justin
	 * @param args
	 *  		Optional arguments </br>
	 *  		&nbsp;&nbsp;&nbsp;&nbsp;<b>timeout</b> - the maximum time in seconds the method should try to sync. Called 
	 *  							 with syncHidden(10)</br>
	 *  		&nbsp;&nbsp;&nbsp;&nbsp;<b>failTestOnSyncFailure </b>- if TRUE, the test will throw an exception and 
	 *  					fail the script. If FALSE, the script will 
	 *  					not fail, instead a FALSE will be returned 
	 *  					to the calling function. Called with 
	 *  					syncHidden(10, false)
	 */
	public boolean syncHidden(Object... args) {
		TestReporter.logTrace("Entering ElementImpl#syncHidden");
		int timeout = getWrappedDriver().getElementTimeout();
		boolean failTestOnSync = Constants.defaultSyncHandler;
		try{
			if(args[0] != null) timeout = Integer.valueOf(args[0].toString());
			if(args[1] != null) failTestOnSync = Boolean.parseBoolean(args[1].toString());
		}catch(ArrayIndexOutOfBoundsException aiobe){}

		StopWatch stopwatch = new StopWatch();
		TestReporter.interfaceLog("<i>Syncing to element [<b>" + getElementLocatorInfo()
		+ "</b> ] to be <b>HIDDEN</b> within [ <b>" + timeout + "</b> ] seconds.</i>");

		boolean found = false;
		long timeLapse;
		stopwatch.start();
		WebDriverWait wait = new WebDriverWait(driver, timeout);

		try {
			found = wait.pollingEvery(Constants.millisecondsToPollForElement, TimeUnit.MILLISECONDS).until(ExtendedExpectedConditions.elementToBeHidden(reload()));
		} catch (NoSuchElementException | ClassCastException | StaleElementReferenceException | TimeoutException te){}
		stopwatch.stop();
		timeLapse = stopwatch.getTime();
		stopwatch.reset();

		if (!found && failTestOnSync) {
			TestReporter.logTrace("Element not <b>HIDDEN</b> and failTestOnSync is [ TRUE ]");
			TestReporter.interfaceLog("<i>Element [<b>" + getElementLocatorInfo()
			+ " </b>] is not <b>HIDDEN</b> on the page after [ "
			+ (timeLapse) / 1000.0 + " ] seconds.</i>");
			throw new ElementNotHiddenException(
					"Element [ " + getElementLocatorInfo() + " ] is not HIDDEN on the page after [ "
							+ (timeLapse) / 1000.0 + " ] seconds.", driver);
		}
		TestReporter.interfaceLog("<i>Element [<b>" + getElementLocatorInfo()   + " </b>] is <b>HIDDEN</b> on the page after [ " + (timeLapse) / 1000.0 + " ] seconds.</i>");
		TestReporter.logTrace("Exiting ElementImpl#syncHidden");
		return found;
	}

	/**
	 * Used to determine if the desired element is enabled on the screen 
	 * Will wait for default element timeout unless new timeout is passed in 
	 * If object is not enabled within the time, handle the error based default handler 
	 * or by boolean passed in
	 *
	 * @author Justin
	 * @param args
	 *  		Optional arguments </br>
	 *  		&nbsp;&nbsp;&nbsp;&nbsp;<b>timeout</b> - the maximum time in seconds the method should try to sync. Called 
	 *  							 with syncEnabled(10)</br>
	 *  		&nbsp;&nbsp;&nbsp;&nbsp;<b>failTestOnSyncFailure </b>- if TRUE, the test will throw an exception and 
	 *  					fail the script. If FALSE, the script will 
	 *  					not fail, instead a FALSE will be returned 
	 *  					to the calling function. Called with 
	 *  					syncEnabled(10, false)
	 */
	public boolean syncEnabled(Object... args) {
		TestReporter.logTrace("Entering ElementImpl#syncEnabled");
		int timeout = getWrappedDriver().getElementTimeout();
		boolean failTestOnSync = Constants.defaultSyncHandler;
		try{
			if(args[0] != null) timeout = Integer.valueOf(args[0].toString());
			if(args[1] != null) failTestOnSync = Boolean.parseBoolean(args[1].toString());
		}catch(ArrayIndexOutOfBoundsException aiobe){}

		boolean found = false;
		long timeLapse;
		StopWatch stopwatch = new StopWatch();
		TestReporter.interfaceLog("<i>Syncing to element [<b>" + getElementLocatorInfo()
		+ "</b> ] to be <b>ENABLED</b> within [ <b>" + timeout + "</b> ] seconds.</i>");
		stopwatch.start();
		WebDriverWait wait = new WebDriverWait(driver, timeout);

		try {
			if(Highlight.getDebugMode()) Highlight.highlightDebug(driver, reload());
			found = wait.pollingEvery(Constants.millisecondsToPollForElement, TimeUnit.MILLISECONDS).until(ExpectedConditions.elementToBeClickable(reload())) != null;		    
		} catch (NoSuchElementException | ClassCastException | StaleElementReferenceException | TimeoutException te){}
		stopwatch.stop();
		timeLapse = stopwatch.getTime();
		stopwatch.reset();

		if (!found && failTestOnSync) {
			Highlight.highlightError(driver, element);
			TestReporter.interfaceLog("<i>Element [<b>" + getElementLocatorInfo()
			+ " </b>] is not <b>ENABLED</b> on the page after [ "
			+ (timeLapse) / 1000.0 + " ] seconds.</i>");
			throw new ElementNotEnabledException(
					"Element [ " + getElementLocatorInfo() + " ] is not ENABLED on the page after [ "
							+ (timeLapse) / 1000.0 + " ] seconds.", driver);
		}	
		TestReporter.interfaceLog("<i>Element [<b>" + getElementLocatorInfo()
		+ " </b>] is <b>ENABLED</b> on the page after [ "
		+ (timeLapse) / 1000.0 + " ] seconds.</i>");
		if(Highlight.getDebugMode()) Highlight.highlightSuccess(driver, reload());
		TestReporter.logTrace("Exiting ElementImpl#syncEnabled");
		return found;
	}
	/**
	 * Used to determine if the desired element is disabled on the screen 
	 * Will wait for default element timeout unless new timeout is passed in 
	 * If object is not disabled within the time, handle the error based default handler 
	 * or by boolean passed in
	 *
	 * @author Justin
	 * @param args
	 *  		Optional arguments </br>
	 *  		&nbsp;&nbsp;&nbsp;&nbsp;<b>timeout</b> - the maximum time in seconds the method should try to sync. Called 
	 *  							 with syncDisabled(10)</br>
	 *  		&nbsp;&nbsp;&nbsp;&nbsp;<b>failTestOnSyncFailure </b>- if TRUE, the test will throw an exception and 
	 *  					fail the script. If FALSE, the script will 
	 *  					not fail, instead a FALSE will be returned 
	 *  					to the calling function. Called with 
	 *  					syncDisabled(10, false)
	 */
	public boolean syncDisabled(Object... args) {
		TestReporter.logTrace("Entering ElementImpl#syncDisabled");
		int timeout = getWrappedDriver().getElementTimeout();
		boolean failTestOnSync = Constants.defaultSyncHandler;
		try{
			if(args[0] != null) timeout = Integer.valueOf(args[0].toString());
			if(args[1] != null) failTestOnSync = Boolean.parseBoolean(args[1].toString());
		}catch(ArrayIndexOutOfBoundsException aiobe){}

		boolean found = false;
		long timeLapse;
		StopWatch stopwatch = new StopWatch();
		TestReporter.interfaceLog("<i>Syncing to element [<b>" + getElementLocatorInfo()
		+ "</b> ] to be <b>DISABLED</b> within [ <b>" + timeout + "</b> ] seconds.</i>");
		stopwatch.start();
		WebDriverWait wait = new WebDriverWait(driver, timeout);

		try {
			if(Highlight.getDebugMode()) Highlight.highlightDebug(driver, element);
			found = wait.pollingEvery(Constants.millisecondsToPollForElement, TimeUnit.MILLISECONDS).until(ExpectedConditions.not(ExpectedConditions.elementToBeClickable(reload()))) != null;		    
		} catch (NoSuchElementException | ClassCastException | StaleElementReferenceException | TimeoutException te){}
		stopwatch.stop();
		timeLapse = stopwatch.getTime();
		stopwatch.reset();

		if (!found && failTestOnSync) {
			Highlight.highlightError(driver, reload());
			TestReporter.interfaceLog("<i>Element [<b>" + getElementLocatorInfo()
			+ " </b>] is not <b>DISABLED</b> on the page after [ "
			+ (timeLapse) / 1000.0 + " ] seconds.</i>");
			throw new ElementNotDisabledException(
					"Element [ " + getElementLocatorInfo() + " ] is not DISABLED on the page after [ "
							+ (timeLapse) / 1000.0 + " ] seconds.", driver);
		}
		TestReporter.interfaceLog("<i>Element [<b>" + getElementLocatorInfo()
		+ " </b>] is <b>DISABLED</b> on the page after [ "
		+ (timeLapse) / 1000.0 + " ] seconds.</i>");
		if(Highlight.getDebugMode()) Highlight.highlightSuccess(driver, reload());
		TestReporter.logTrace("Exiting ElementImpl#syncDisabled");
		return found;
	}


	/**
	 * Sync for the Element's text or it's value attribute contains the desired text.
	 * Additional parameters can be added to override the default timeout and if the 
	 * test should fail if the sync fails
	 * 
	 * @param text
	 *  		(Required) The text the element should contain in either its text or value attribute
	 * @param args
	 *  		Optional arguments </br>
	 *  		&nbsp;&nbsp;&nbsp;&nbsp;<b>timeout</b> - the maximum time in seconds the method should try to sync. Called 
	 *  							 with syncTextInElement("text", 10)</br>
	 *  		&nbsp;&nbsp;&nbsp;&nbsp;<b>failTestOnSyncFailure </b>- if TRUE, the test will throw an exception and 
	 *  					fail the script. If FALSE, the script will 
	 *  					not fail, instead a FALSE will be returned 
	 *  					to the calling function. Called with 
	 *  					syncTextInElement("text", 10, false)
	 */
	public boolean syncTextInElement(String text, Object... args) {
		TestReporter.logTrace("Entering ElementImpl#syncTextInElement");
		int timeout = getWrappedDriver().getElementTimeout();
		boolean failTestOnSync = Constants.defaultSyncHandler;
		try{
			if(args[0] != null) timeout = Integer.valueOf(args[0].toString());
			if(args[1] != null) failTestOnSync = Boolean.parseBoolean(args[1].toString());
		}catch(ArrayIndexOutOfBoundsException aiobe){}

		boolean found = false;
		long timeLapse;
		StopWatch stopwatch = new StopWatch();
		TestReporter.interfaceLog("<i>Syncing to text [<b>" + text + "</b> ] in element [<b>"
				+ getElementLocatorInfo() + "</b> ] to be displayed within [ <b>" + timeout + "</b> ] seconds.</i>");

		WebDriverWait wait = new WebDriverWait(driver, 0);
		stopwatch.start();
		if(Highlight.getDebugMode()) Highlight.highlightDebug(driver, reload());
		do {
			try {
				if (wait.until(ExpectedConditions.textToBePresentInElement(reload(), text)) != null) found= true;
				else if (wait.until(ExpectedConditions.textToBePresentInElementValue(reload(), text)) != null) found = true;		
				if(found) break;
			} catch (NoSuchElementException | ClassCastException | StaleElementReferenceException | TimeoutException te){}
		} while (stopwatch.getTime() / 1000.0 < (long) timeout);

		stopwatch.stop();
		timeLapse = stopwatch.getTime();
		stopwatch.reset();

		if (!found && failTestOnSync) {
			Highlight.highlightError(driver, element);
			TestReporter.interfaceLog(
					"<i>Element [<b>" + getElementLocatorInfo() + " </b>] did not contain the text [ " + text
					+ " ] after [ " + (timeLapse) / 1000.0 + " ] seconds.</i>");
			throw new TextInElementNotPresentException(
					"Element [ " + getElementLocatorInfo() + " ] did not contain the text [ " + text
					+ " ] after [ " + (timeLapse) / 1000.0 + " ] seconds.", driver);
		}
		TestReporter.interfaceLog(
				"<i>Element [<b>" + getElementLocatorInfo() + " </b>] contains the text [ " + text
				+ " ] after [ " + (timeLapse) / 1000.0 + " ] seconds.</i>");
		if(Highlight.getDebugMode()) Highlight.highlightSuccess(driver, reload());
		TestReporter.logTrace("Exiting ElementImpl#syncTextInElement");
		return found;
	}

	/**
	 * Sync for the Element's text or it's value attribute contains the desired text.
	 * Additional parameters can be added to override the default timeout and if the 
	 * test should fail if the sync fails
	 * 
	 * @param regex
	 *  		(Required) The text the element should contain in either its text or value attribute
	 * @param args
	 *  		Optional arguments </br>
	 *  		&nbsp;&nbsp;&nbsp;&nbsp;<b>timeout</b> - the maximum time in seconds the method should try to sync. Called 
	 *  							 with syncTextMatchesInElement("text", 10)</br>
	 *  		&nbsp;&nbsp;&nbsp;&nbsp;<b>failTestOnSyncFailure </b>- if TRUE, the test will throw an exception and 
	 *  					fail the script. If FALSE, the script will 
	 *  					not fail, instead a FALSE will be returned 
	 *  					to the calling function. Called with 
	 *  					syncTextMatchesInElement("text", 10, false)
	 */
	public boolean syncTextMatchesInElement(String regex, Object... args) {
		TestReporter.logTrace("Entering ElementImpl#syncTextMatchesInElement");
		int timeout = getWrappedDriver().getElementTimeout();
		boolean failTestOnSync = Constants.defaultSyncHandler;
		try{
			if(args[0] != null) timeout = Integer.valueOf(args[0].toString());
			if(args[1] != null) failTestOnSync = Boolean.parseBoolean(args[1].toString());
		}catch(ArrayIndexOutOfBoundsException aiobe){}

		boolean found = false;
		long timeLapse;
		StopWatch stopwatch = new StopWatch();
		TestReporter.interfaceLog("<i>Syncing to text regular expression [<b>" + regex + "</b> ] in element [<b>"
				+ getElementLocatorInfo() + "</b> ] to be displayed within [ <b>" + timeout + "</b> ] seconds.</i>");
		stopwatch.start();
		WebDriverWait wait = new WebDriverWait(driver, 0);
		do {

			try {
				if(Highlight.getDebugMode()) Highlight.highlightDebug(driver, reload());
				if(wait.until(ExtendedExpectedConditions.textToMatchInElement(reload(), regex)) != null) found = true;
				else if (wait.until(ExtendedExpectedConditions.textToMatchInElementAttribute(reload(), "value", regex)) != null) found = true;
				if(found) break;
			} catch (NoSuchElementException | ClassCastException | StaleElementReferenceException | TimeoutException e) {} 
		}while (stopwatch.getTime() / 1000.0 < (long) timeout);
		stopwatch.stop();
		timeLapse = stopwatch.getTime();
		stopwatch.reset();

		if (!found && failTestOnSync) {
			Highlight.highlightError(driver, reload());
			TestReporter.interfaceLog(
					"<i>Element [<b>" + getElementLocatorInfo() + " </b>] did not contain the text [ " + regex
					+ " ] after [ " + (timeLapse) / 1000.0 + " ] seconds.</i>");
			throw new TextInElementNotPresentException(
					"Element [ " + getElementLocatorInfo() + " ] did not contain the text [ " + regex
					+ " ] after [ " + (timeLapse) / 1000.0 + " ] seconds.", driver);
		}
		TestReporter.interfaceLog(
				"<i>Element [<b>" + getElementLocatorInfo() + " </b>] contains the text [ " + regex
				+ " ] after [ " + (timeLapse) / 1000.0 + " ] seconds.</i>");
		if(Highlight.getDebugMode()) Highlight.highlightSuccess(driver, reload());
		TestReporter.logTrace("Exiting ElementImpl#syncTextMatchesInElement");
		return found;	
	}


	/**
	 * Sync for the Element's text or it's value attribute contains the desired text.
	 * Additional parameters can be added to override the default timeout and if the 
	 * test should fail if the sync fails
	 * 
	 * @param attribute (Required) - Element attribute to view
	 * @param value	(Required) - The text the element attribute should contain in either its text or value attribute
	 * @param args
	 *  		Optional arguments </br>
	 *  		&nbsp;&nbsp;&nbsp;&nbsp;<b>timeout</b> - the maximum time in seconds the method should try to sync. Called 
	 *  							 with syncAttributeContainsValue("text", 10)</br>
	 *  		&nbsp;&nbsp;&nbsp;&nbsp;<b>failTestOnSyncFailure </b>- if TRUE, the test will throw an exception and 
	 *  					fail the script. If FALSE, the script will 
	 *  					not fail, instead a FALSE will be returned 
	 *  					to the calling function. Called with 
	 *  					syncAttributeContainsValue("text", 10, false)
	 */
	public boolean syncAttributeContainsValue(String attribute, String value, Object... args) {
		TestReporter.logTrace("Entering ElementImpl#syncAttributeContainsValue");
		int timeout = getWrappedDriver().getElementTimeout();
		boolean failTestOnSync = Constants.defaultSyncHandler;
		try{
			if(args[0] != null) timeout = Integer.valueOf(args[0].toString());
			if(args[1] != null) failTestOnSync = Boolean.parseBoolean(args[1].toString());
		}catch(ArrayIndexOutOfBoundsException aiobe){}
		boolean found = false;
		long timeLapse;
		StopWatch stopwatch = new StopWatch();
		TestReporter.interfaceLog("<i>Syncing to attribute [<b> " + attribute + "</b> ] to contain [<b> " + value + "</b> ] in element [<b>"
				+ getElementLocatorInfo() + "</b> ] to be displayed within [ <b> " + timeout + "</b> ] seconds.</i>");
		stopwatch.start();
		WebDriverWait wait = new WebDriverWait(driver, timeout);

		try {
			if(Highlight.getDebugMode()) Highlight.highlightDebug(driver, reload());
			found =  wait.pollingEvery(Constants.millisecondsToPollForElement, TimeUnit.MILLISECONDS).until(ExtendedExpectedConditions.textToBePresentInElementAttribute(reload(), attribute, value));
		} catch (NoSuchElementException | ClassCastException | StaleElementReferenceException | TimeoutException te){}
		stopwatch.stop();
		timeLapse = stopwatch.getTime();
		stopwatch.reset();

		if (!found && failTestOnSync) {
			Highlight.highlightError(driver, reload());
			TestReporter.interfaceLog(
					"<i>Element [<b>" + getElementLocatorInfo() + " </b>] attribute [<b>" + attribute + "</b> ] did not contain the text [ " + value
					+ " ] after [ " + (timeLapse) / 1000.0 + " ] seconds.</i>");
			throw new ElementAttributeValueNotMatchingException(
					"Element [ " + getElementLocatorInfo() + " ]attribute [" + attribute + "] did not contain the text [ " + value
					+ " ] after [ " + (timeLapse) / 1000.0 + " ] seconds.", driver);
		}
		TestReporter.interfaceLog(
				"<i>Element [<b>" + getElementLocatorInfo() + " </b>] attribute [<b>" + attribute + "</b> ] contains the text [ " + value
				+ " ] after [ " + (timeLapse) / 1000.0 + " ] seconds.</i>");
		if(Highlight.getDebugMode()) Highlight.highlightSuccess(driver, reload());
		TestReporter.logTrace("Exiting ElementImpl#syncAttributeContainsValue");
		return found;
	}

	/**
	 * Sync for the Element's text or it's value attribute contains the desired text.
	 * Additional parameters can be added to override the default timeout and if the 
	 * test should fail if the sync fails
	 * 
	 * @param attribute (Required) - Element attribute to view
	 * @param regex	(Required) - The regular expression that should match the text of the element attribute 
	 * @param args
	 *  		Optional arguments </br>
	 *  		&nbsp;&nbsp;&nbsp;&nbsp;<b>timeout</b> - the maximum time in seconds the method should try to sync. Called 
	 *  							 with syncAttributeMatchesValue("text", 10)</br>
	 *  		&nbsp;&nbsp;&nbsp;&nbsp;<b>failTestOnSyncFailure </b>- if TRUE, the test will throw an exception and 
	 *  					fail the script. If FALSE, the script will 
	 *  					not fail, instead a FALSE will be returned 
	 *  					to the calling function. Called with 
	 *  					syncAttributeMatchesValue("text", 10, false)
	 */
	public boolean syncAttributeMatchesValue(String attribute, String regex, Object... args) {
		TestReporter.logTrace("Entering ElementImpl#syncAttributeMatchesValue");
		int timeout = getWrappedDriver().getElementTimeout();
		boolean failTestOnSync = Constants.defaultSyncHandler;
		try{
			if(args[0] != null) timeout = Integer.valueOf(args[0].toString());
			if(args[1] != null) failTestOnSync = Boolean.parseBoolean(args[1].toString());
		}catch(ArrayIndexOutOfBoundsException aiobe){}

		boolean found = false;
		long timeLapse;
		StopWatch stopwatch = new StopWatch();
		TestReporter.interfaceLog("<i>Syncing to attribute [<b> " + attribute + "</b> ] to match the regular expression of [<b> " + regex + "</b> ] in element [<b>"
				+ getElementLocatorInfo() + "</b> ] to be displayed within [ <b> " + timeout + "</b> ] seconds.</i>");
		WebDriverWait wait = new WebDriverWait(driver, timeout);


		stopwatch.start();
		try {
			if(Highlight.getDebugMode()) Highlight.highlightDebug(driver, reload());
			found =  wait.pollingEvery(Constants.millisecondsToPollForElement, TimeUnit.MILLISECONDS).until(ExtendedExpectedConditions.textToMatchInElementAttribute(reload(), attribute, regex));
		} catch (NoSuchElementException | ClassCastException | StaleElementReferenceException | TimeoutException te){
			found =  false;
		}
		stopwatch.stop();
		timeLapse = stopwatch.getTime();
		stopwatch.reset();

		if (!found && failTestOnSync) {
			Highlight.highlightError(driver, reload());
			TestReporter.interfaceLog(
					"<i>Element [<b>" + getElementLocatorInfo() + " </b>] attribute [<b>" + attribute + "</b> ] did not match the regular expression of [ " + regex
					+ " ] after [ " + (timeLapse) / 1000.0 + " ] seconds.</i>");
			throw new ElementAttributeValueNotMatchingException(
					"Element [ " + getElementLocatorInfo() + " ]attribute [" + attribute + "] did not match the regular expression of [ " + regex
					+ " ] after [ " + (timeLapse) / 1000.0 + " ] seconds.", driver);
		}
		TestReporter.interfaceLog(
				"<i>Element [<b>" + getElementLocatorInfo() + " </b>] attribute [<b>" + attribute + "</b> ] matches the regular expression of [ " + regex
				+ " ] after [ " + (timeLapse) / 1000.0 + " ] seconds.</i>");
		if(Highlight.getDebugMode()) Highlight.highlightSuccess(driver, reload());
		TestReporter.logTrace("Exiting ElementImpl#syncAttributeMatchesValue");
		return found;
	}

	/**
	 * Sync for the Element's text or it's value attribute contains the desired text.
	 * Additional parameters can be added to override the default timeout and if the 
	 * test should fail if the sync fails
	 * 
	 * @param cssProperty (Required) - Element CSS Property to view
	 * @param value	(Required) - The text the element attribute should contain in either its text or value attribute
	 * @param args
	 *  		Optional arguments </br>
	 *  		&nbsp;&nbsp;&nbsp;&nbsp;<b>timeout</b> - the maximum time in seconds the method should try to sync. Called 
	 *  							 with syncCssContainsValue("text", 10)</br>
	 *  		&nbsp;&nbsp;&nbsp;&nbsp;<b>failTestOnSyncFailure </b>- if TRUE, the test will throw an exception and 
	 *  					fail the script. If FALSE, the script will 
	 *  					not fail, instead a FALSE will be returned 
	 *  					to the calling function. Called with 
	 *  					syncCssContainsValue("text", 10, false)
	 */
	public boolean syncCssPropertyContainsValue(String cssProperty, String value, Object... args) {
		TestReporter.logTrace("Entering ElementImpl#syncCssPropertyContainsValue");
		int timeout = getWrappedDriver().getElementTimeout();
		boolean failTestOnSync = Constants.defaultSyncHandler;
		try{
			if(args[0] != null) timeout = Integer.valueOf(args[0].toString());
			if(args[1] != null) failTestOnSync = Boolean.parseBoolean(args[1].toString());
		}catch(ArrayIndexOutOfBoundsException aiobe){}

		boolean found = false;
		long timeLapse;
		StopWatch stopwatch = new StopWatch();
		TestReporter.interfaceLog("<i>Syncing to CSS Property [<b> " + cssProperty + "</b> ] to contain [<b> " + value + "</b> ] in element [<b>"
				+ getElementLocatorInfo() + "</b> ] to be displayed within [ <b> " + timeout + "</b> ] seconds.</i>");

		WebDriverWait wait = new WebDriverWait(driver, timeout);
		stopwatch.start();
		try {
			if(Highlight.getDebugMode()) Highlight.highlightDebug(driver, reload());
			found =  wait.pollingEvery(Constants.millisecondsToPollForElement, TimeUnit.MILLISECONDS).until(ExtendedExpectedConditions.textToBePresentInElementCssProperty(reload(), cssProperty, value));
		} catch (NoSuchElementException | ClassCastException | StaleElementReferenceException | TimeoutException te){}
		stopwatch.stop();
		timeLapse = stopwatch.getTime();
		stopwatch.reset();

		if (!found && failTestOnSync) {
			Highlight.highlightError(driver, reload());
			TestReporter.interfaceLog(
					"<i>Element [<b>" + getElementLocatorInfo() + " </b>] CSS Property [<b>" + cssProperty  + "</b> ] did not contain the text [ " + value
					+ " ] after [ " + (timeLapse) / 1000.0 + " ] seconds.</i>");
			throw new ElementCssValueNotMatchingException( "Element [ " + getElementLocatorInfo() + " ] CSS Property [" + cssProperty  + " ] did not contain the text [ " + value
					+ " ] after [ " + (timeLapse) / 1000.0 + " ] seconds.", driver);
		}

		TestReporter.interfaceLog(
				"<i>Element [<b>" + getElementLocatorInfo() + " </b>] CSS Property [<b>" + cssProperty  + "</b> ] contains the text [ " + value
				+ " ] after [ " + (timeLapse) / 1000.0 + " ] seconds.</i>");
		if(Highlight.getDebugMode()) Highlight.highlightSuccess(driver, reload());
		TestReporter.logTrace("Exiting ElementImpl#syncCssPropertyContainsValue");
		return found;
	}

	/**
	 * Sync for the Element's text or it's value attribute contains the desired text.
	 * Additional parameters can be added to override the default timeout and if the 
	 * test should fail if the sync fails
	 * 
	 * @param cssProperty (Required) - Element CSS Property to match
	 * @param regex	(Required) - The regular expression that should match the text of the element CSS Property 
	 * @param args	Optional arguments </br>
	 *  		&nbsp;&nbsp;&nbsp;&nbsp;<b>timeout</b> - the maximum time in seconds the method should try to sync. Called 
	 *  							 with syncCssMatchesValue("text", 10)</br>
	 *  		&nbsp;&nbsp;&nbsp;&nbsp;<b>failTestOnSyncFailure </b>- if TRUE, the test will throw an exception and 
	 *  					fail the script. If FALSE, the script will 
	 *  					not fail, instead a FALSE will be returned 
	 *  					to the calling function. Called with 
	 *  					syncCssMatchesValue("text", 10, false)
	 */
	public boolean syncCssPropertyMatchesValue(String cssProperty, String regex, Object... args) {
		TestReporter.logTrace("Entering ElementImpl#syncCssPropertyMatchesValue");
		int timeout = getWrappedDriver().getElementTimeout();
		boolean failTestOnSync = Constants.defaultSyncHandler;
		try{
			if(args[0] != null) timeout = Integer.valueOf(args[0].toString());
			if(args[1] != null) failTestOnSync = Boolean.parseBoolean(args[1].toString());
		}catch(ArrayIndexOutOfBoundsException aiobe){}

		boolean found = false;
		long timeLapse;
		StopWatch stopwatch = new StopWatch();
		TestReporter.interfaceLog("<i>Syncing to CSS Property [<b> " + cssProperty + "</b> ] to contain [<b> " + regex + "</b> ] in element [<b>"
				+ getElementLocatorInfo() + "</b> ] to be displayed within [ <b> " + timeout + "</b> ] seconds.</i>");
		WebDriverWait wait = new WebDriverWait(driver, 0);
		stopwatch.start();

		try {
			if(Highlight.getDebugMode()) Highlight.highlightDebug(driver, reload());
			found =  wait.pollingEvery(Constants.millisecondsToPollForElement, TimeUnit.MILLISECONDS).until(ExtendedExpectedConditions.textToMatchInElementCssProperty(reload(), cssProperty, regex));
		} catch (TimeoutException te){}
		stopwatch.stop();
		timeLapse = stopwatch.getTime();
		stopwatch.reset();

		if (!found && failTestOnSync) {
			Highlight.highlightError(driver, reload());
			TestReporter.interfaceLog(
					"<i>Element [<b>" + getElementLocatorInfo() + " </b>] CSS Property [<b>" + cssProperty  + "</b> ] did not match the regular expression of [ " + regex
					+ " ] after [ " + (timeLapse) / 1000.0 + " ] seconds.</i>");
			throw new ElementCssValueNotMatchingException(
					"Element [ " + getElementLocatorInfo() + " ] CSS Property [" + cssProperty  + "] did not match the regular expression of [ " + regex
					+ " ] after [ " + (timeLapse) / 1000.0 + " ] seconds.", driver);
		}
		TestReporter.interfaceLog(
				"<i>Element [<b>" + getElementLocatorInfo() + " </b>] CSS Property [<b>" + cssProperty  + "</b> ] matches the regular expression of [ " + regex
				+ " ] after [ " + (timeLapse) / 1000.0 + " ] seconds.</i>");
		if(Highlight.getDebugMode()) Highlight.highlightSuccess(driver, reload());
		TestReporter.logTrace("Exiting ElementImpl#syncCssPropertyMatchesValue");
		return found;
	}

	@Beta
	protected WebElement reload(){
		TestReporter.logTrace("Entering ElementImpl#reload");
		TestReporter.logTrace("Search DOM for element [ " + by.toString() + " ]");
		WebElement el = getWrappedDriver().findWebElement(by);
		TestReporter.logTrace("Found element [ " + by.toString() + " ]");
		TestReporter.logTrace("Exiting ElementImpl#reload");
		return el;
	}
}