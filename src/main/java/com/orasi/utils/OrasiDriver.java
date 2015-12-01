package com.orasi.utils;
import java.net.URL;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;

import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.javascript.background.DefaultJavaScriptExecutor;
import com.gargoylesoftware.htmlunit.javascript.background.JavaScriptExecutor;
import com.orasi.core.by.angular.ByNG;
import com.orasi.core.by.angular.ByNG.ByNGButton;
import com.orasi.core.by.angular.ByNG.ByNGController;
import com.orasi.core.by.angular.ByNG.ByNGModel;
import com.orasi.core.by.angular.ByNG.ByNGRepeater;
import com.orasi.core.by.angular.ByNG.ByNGShow;
import com.orasi.core.by.angular.internal.ByAngular;
import com.orasi.core.interfaces.Button;
import com.orasi.core.interfaces.Checkbox;
import com.orasi.core.interfaces.Element;
import com.orasi.core.interfaces.Label;
import com.orasi.core.interfaces.Link;
import com.orasi.core.interfaces.Listbox;
import com.orasi.core.interfaces.RadioGroup;
import com.orasi.core.interfaces.Textbox;
import com.orasi.core.interfaces.Webtable;
import com.orasi.core.interfaces.impl.ButtonImpl;
import com.orasi.core.interfaces.impl.CheckboxImpl;
import com.orasi.core.interfaces.impl.ElementImpl;
import com.orasi.core.interfaces.impl.LabelImpl;
import com.orasi.core.interfaces.impl.LinkImpl;
import com.orasi.core.interfaces.impl.ListboxImpl;
import com.orasi.core.interfaces.impl.RadioGroupImpl;
import com.orasi.core.interfaces.impl.TextboxImpl;
import com.orasi.core.interfaces.impl.WebtableImpl;

public class OrasiDriver implements WebDriver,   JavaScriptExecutor, TakesScreenshot {
    /**
     * 
     */
    private static final long serialVersionUID = -657563735440878909L;
    private WebDriver driver;
    private int currentPageTimeout = Constants.PAGE_TIMEOUT;
    private int currentElementTimeout = Constants.ELEMENT_TIMEOUT;
    private int currentScriptTimeout = Constants.DEFAULT_GLOBAL_DRIVER_TIMEOUT;
    public OrasiDriver(DesiredCapabilities caps){
	setDriverWithCapabilties(caps);	
     }
    

    public OrasiDriver(DesiredCapabilities caps, URL url){
	driver = new RemoteWebDriver(url, caps);
     }


    public WebDriver getDriver(){
	return driver;
    }
    
    @Override
    public void get(String url) {
	driver.get(url);
	
    }

    @Override
    public String getCurrentUrl() {
	return driver.getCurrentUrl();
    }

    @Override
    public String getTitle() {	
	return driver.getTitle();
    }

    public void setScriptTimeout(int timeout){
	setScriptTimeout(timeout, TimeUnit.SECONDS);
    }
    
    public void setScriptTimeout(int timeout, TimeUnit timeUnit){
   	this.currentScriptTimeout = timeout;
   	driver.manage().timeouts().setScriptTimeout(timeout, timeUnit);
       }
    
    public int getScriptTimeout(){
	return currentScriptTimeout;
    }
    
    public void setPageTimeout(int timeout){
	setPageTimeout(timeout, TimeUnit.SECONDS);
    }
    
    public void setPageTimeout(int timeout, TimeUnit timeUnit){
	if (driver instanceof SafariDriver || driver.toString().contains("safari")){
	    System.out.println("SafariDriver does not support pageLoadTimeout");
	}else{
	    this.currentPageTimeout = timeout;
	    driver.manage().timeouts().pageLoadTimeout(timeout, timeUnit);
	}
    }
    
    public int getPageTimeout(){
	return currentPageTimeout;
    }
    

    public void setElementTimeout(int timeout){
	setElementTimeout(timeout, TimeUnit.SECONDS);
    }
    
    public void setElementTimeout(int timeout, TimeUnit timeUnit){
	this.currentElementTimeout = timeout;
	driver.manage().timeouts().implicitlyWait(timeout, timeUnit);
    }
    
    public int getElementTimeout(){
	return currentElementTimeout;
    }
    
   /* public List<Element> findElements(By by) {
	List<WebElement> webElements = driver.findElements(by);
	List test = webElements;
	List<Element> elements= (List<Element>)test;
	return elements;
    }*/

    @Override
    public List<WebElement> findElements(By by) {
		try{
		    return findWebElements(by);
		}catch(NoSuchElementException nse){	  
		    TestReporter.logFailure("No such Element with context " + by.toString());
		    throw new NoSuchElementException(nse.getMessage());
		}
    }
    
    public List<WebElement> findWebElements(By by){
		try{
		    return driver.findElements(by);
		}catch(NoSuchElementException nse){	  
		    TestReporter.logFailure("No such WebElement with context: " + by.toString());
		    throw new NoSuchElementException(nse.getMessage());
		}
    }
    
    @Override
    public Element findElement(By by) {
		try{
		    return new ElementImpl(driver.findElement(by), this);
		}catch(NoSuchElementException nse){	  
		    TestReporter.logFailure("No such Element with context: " + by.toString());
		    throw new NoSuchElementException(nse.getMessage());
		}
    }
    
	public Element findElement(ByNG by) {
    	try{
    	    return new ElementImpl(driver.findElement(getByNGType(by)), this);
    	}catch(NoSuchElementException nse){	  
    	    TestReporter.logFailure("No such Element with context: " + by.toString());
    	    throw new NoSuchElementException(nse.getMessage());
    	}
    }

    public Textbox findTextbox(By by) {
		try{
		    return new TextboxImpl(driver.findElement(by), this);
		}catch(NoSuchElementException nse){	  
		    TestReporter.logFailure("No such Textbox with context: " + by.toString());
		    throw new NoSuchElementException(nse.getMessage());
		}
    }

    public Textbox findTextbox(ByNG by) {
		try{
		    return new TextboxImpl(driver.findElement(getByNGType(by)), this);
		}catch(NoSuchElementException nse){	  
		    TestReporter.logFailure("No such Textbox with context: " + by.toString());
		    throw new NoSuchElementException(nse.getMessage());
		}
    }
    
    public Button findButton(By by) {
		try{
		    return new ButtonImpl(driver.findElement(by), this);
		}catch(NoSuchElementException nse){	  
		    TestReporter.logFailure("No such Button with context: " + by.toString());
		    throw new NoSuchElementException(nse.getMessage());
		}
    }

	public Button findButton(ByNG by) {
    	try{
    	    return new ButtonImpl(driver.findElement(getByNGType(by)), this);
    	}catch(NoSuchElementException nse){	  
    	    TestReporter.logFailure("No such Button with context: " + by.toString());
    	    throw new NoSuchElementException(nse.getMessage());
    	}
    }
    
    public Checkbox findCheckbox(By by) {
		try{
		    return new CheckboxImpl(driver.findElement(by), this);
		}catch(NoSuchElementException nse){	  
		    TestReporter.logFailure("No such Checkbox with context: " + by.toString());
		    throw new NoSuchElementException(nse.getMessage());
		}
    }
    
    public Checkbox findCheckbox(ByNG by) {
		try{
		    return new CheckboxImpl(driver.findElement(getByNGType(by)), this);
		}catch(NoSuchElementException nse){	  
		    TestReporter.logFailure("No such Checkbox with context: " + by.toString());
		    throw new NoSuchElementException(nse.getMessage());
		}
    }
    
    public Label findLabel(By by) {
		try{
		    return new LabelImpl(driver.findElement(by), this);
		}catch(NoSuchElementException nse){	  
		    TestReporter.logFailure("No such Label with context: " + by.toString());
		    throw new NoSuchElementException(nse.getMessage());
		}
    }
    
    public Label findLabel(ByNG by) {
		try{
		    return new LabelImpl(driver.findElement(getByNGType(by)), this);
		}catch(NoSuchElementException nse){	  
		    TestReporter.logFailure("No such Label with context: " + by.toString());
		    throw new NoSuchElementException(nse.getMessage());
		}
    }
    
    public Link findLink(By by) {
		try{
		    return new LinkImpl(driver.findElement(by), this);
		}catch(NoSuchElementException nse){	  
		    TestReporter.logFailure("No such Link with context: " + by.toString());
		    throw new NoSuchElementException(nse.getMessage());
		}
    }    
    
    public Link findLink(ByNG by) {
		try{
		    return new LinkImpl(driver.findElement(getByNGType(by)), this);
		}catch(NoSuchElementException nse){	  
		    TestReporter.logFailure("No such Link with context: " + by.toString());
		    throw new NoSuchElementException(nse.getMessage());
		}
    }    
    
    public Listbox findListbox(By by) {
		try{	
		    return new ListboxImpl(driver.findElement(by), this);
		}catch(NoSuchElementException nse){	  
		    TestReporter.logFailure("No such Listbox with context: " + by.toString());
		    throw new NoSuchElementException(nse.getMessage());
		}
    }    
    
    public Listbox findListbox(ByNG by) {
		try{	
		    return new ListboxImpl(driver.findElement(getByNGType(by)), this);
		}catch(NoSuchElementException nse){	  
		    TestReporter.logFailure("No such Listbox with context: " + by.toString());
		    throw new NoSuchElementException(nse.getMessage());
		}
    }
    
    public RadioGroup findRadioGroup(By by) {
		try{
		    return new RadioGroupImpl(driver.findElement(by), this);
		}catch(NoSuchElementException nse){	  
		    TestReporter.logFailure("No such RadioGroup with context: " + by.toString());
		    throw new NoSuchElementException(nse.getMessage());
		}
    }
    
    public RadioGroup findRadioGroup(ByNG by) {
		try{
		    return new RadioGroupImpl(driver.findElement(getByNGType(by)), this);
		}catch(NoSuchElementException nse){	  
		    TestReporter.logFailure("No such RadioGroup with context: " + by.toString());
		    throw new NoSuchElementException(nse.getMessage());
		}
    }
    
    public Webtable findWebtable(By by) {
		try{
		    return new WebtableImpl(driver.findElement(by), this);
		}catch(NoSuchElementException nse){	  
		    TestReporter.logFailure("No such Webtable with context: " + by.toString());
		    throw new NoSuchElementException(nse.getMessage());
		}
    }
    
    public Webtable findWebtable(ByNG by) {
		try{
		    return new WebtableImpl(driver.findElement(getByNGType(by)), this);
		}catch(NoSuchElementException nse){	  
		    TestReporter.logFailure("No such Webtable with context: " + by.toString());
		    throw new NoSuchElementException(nse.getMessage());
		}
    }
    
    public WebElement findWebElement(By by) {
		try{return driver.findElement(by);
		}catch(NoSuchElementException nse){	  
		    TestReporter.logFailure("No such WebElement with context: " + by.toString());
		    throw new NoSuchElementException(nse.getMessage());
		}
    }
    
    public WebElement findWebElement(ByNG by) {
		try{return driver.findElement(getByNGType(by));
		}catch(NoSuchElementException nse){	  
		    TestReporter.logFailure("No such WebElement with context: " + by.toString());
		    throw new NoSuchElementException(nse.getMessage());
		}
    }
    @Override
    public String getPageSource() {
	return driver.getPageSource();
    }

    @Override
    public void close() {
	driver.close();
    }

    @Override
    public void quit() {
	driver.quit();
    }

    @Override
    public Set<String> getWindowHandles() {
	return driver.getWindowHandles();
    }

    @Override
    public String getWindowHandle() {
	return driver.getWindowHandle();
    }

    @Override
    public TargetLocator switchTo() {
	return driver.switchTo();
    }

    @Override
    public Navigation navigate() {
	return driver.navigate();
    }

    @Override
    public Options manage() {
	return driver.manage();
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
	return super.clone();
    }

    @Override
    public boolean equals(Object obj) {
	return super.equals(obj);
    }

    @Override
    protected void finalize() throws Throwable {
	super.finalize();
    }

    @Override
    public int hashCode() {
	return super.hashCode();
    }

    @Override
    public String toString() {
	return super.toString();
    }
   
    
    public Object executeJavaScript(String script, Object... parameters) {
        return ((JavascriptExecutor) driver).executeScript(script, parameters);
    }
    
    public Object executeAsyncJavaScript(String script, Object... parameters) {
        return ((JavascriptExecutor) driver).executeAsyncScript(script, parameters);
    }

    
    @Override
    public void run() {
	((DefaultJavaScriptExecutor) driver).run();
    }

    @Override
    public void addWindow(WebWindow newWindow) {
	((DefaultJavaScriptExecutor) driver).addWindow(newWindow);
    }

    
    @Override
    public void shutdown() {
	((DefaultJavaScriptExecutor) driver).shutdown();
    }

    
    @Override
    public int pumpEventLoop(long timeoutMillis) {
	return ((DefaultJavaScriptExecutor) driver).pumpEventLoop(timeoutMillis);
    }
    
    public String getSessionId(){
	return ((RemoteWebDriver) driver).getSessionId().toString();
    }
    
    private void setDriverWithCapabilties(DesiredCapabilities caps){
	switch (caps.getBrowserName().toLowerCase()) {
	case "firefox":
	    driver = new FirefoxDriver(caps);
	    break;
	case "internet explorer":
	case "ie":
	    driver = new InternetExplorerDriver(caps);
	    break;    
	case "chrome":
	    driver = new ChromeDriver(caps);
	    break;    

	case "safari":
	    driver = new SafariDriver(caps);
	    break;    
	case "htmlunit":
	case "html":
	    driver = new HtmlUnitDriver(true);
	    break;    

	case "edge":
	case "microsoftedge":
	    driver = new EdgeDriver(caps);
	    break;    
	default:
	    break;
	}
    }

    public Capabilities getDriverCapability(){
    	return new Capabilities(); 
    }
    
    @Override
    public <X> X getScreenshotAs(OutputType<X> target)
	    throws WebDriverException {
	return ((TakesScreenshot) driver).getScreenshotAs(target);
    }
    
    class Capabilities{
    	
    	public String browserName(){
    		return ((RemoteWebDriver) driver).getCapabilities().getBrowserName();
    	}
    	
    	public String browserVersion(){
    		return ((RemoteWebDriver) driver).getCapabilities().getVersion();
    	}
    	
    	public String platformOS(){    		
    		return ((RemoteWebDriver) driver).getCapabilities().getPlatform().name() + " " +
    		       ((RemoteWebDriver) driver).getCapabilities().getPlatform().getMajorVersion() + "." +
    		       ((RemoteWebDriver) driver).getCapabilities().getPlatform().getMinorVersion(); 
    	}
    	
    }

    
    @SuppressWarnings("static-access")
	private ByAngular.BaseBy getByNGType(ByNG by){
    	String text = by.toString().replace("By.buttonText:", "").trim();
    	if(by instanceof ByNGButton) return new ByAngular(getDriver()).buttonText(text);
    	if(by instanceof ByNGController) return new ByAngular(getDriver()).controller(text);
    	if(by instanceof ByNGModel) return new ByAngular(getDriver()).model(text);
    	if(by instanceof ByNGRepeater) return new ByAngular(getDriver()).repeater(text);
    	if(by instanceof ByNGShow) return new ByAngular(getDriver()).show(text);
    	return null;
    }
    
}
