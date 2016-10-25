package com.orasi.core.interfaces.impl.internal;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.FieldDecorator;

import com.orasi.exception.automation.PageInitialization;
import com.orasi.utils.OrasiDriver;
import com.orasi.utils.TestReporter;

/**
 * Element factory for wrapped elements. Similar to {@link org.openqa.selenium.support.PageFactory}
 */
public class ElementFactory {

    /**
     *  See {@link org.openqa.selenium.support.PageFactory#initElements(org.openqa.selenium.WebDriver driver, Class)}
     */
    public static <T> T initElements(OrasiDriver driver, Class<T> pageClassToProxy) {
		TestReporter.logTrace("Entering ElementFactory#initElements");
		TestReporter.logTrace("Creating Page Object");
        T page = instantiatePage(driver, pageClassToProxy);
		TestReporter.logTrace("Successfully created Page Object");
        final OrasiDriver driverRef = driver;
		TestReporter.logTrace("Initialize Page Elements");
        PageFactory.initElements(new ElementDecorator(new CustomElementLocatorFactory(driverRef)), page);
		TestReporter.logTrace("Successfully created Page Elements");
		TestReporter.logTrace("Exiting ElementFactory#initElements");
        return page;
    }

    /**
     *  See {@link org.openqa.selenium.support.PageFactory#initElements(org.openqa.selenium.support.pagefactory.FieldDecorator, Object)}
     */
    public static void initElements(OrasiDriver driver, Object page) {
		TestReporter.logTrace("Entering ElementFactory#initElements");
        final OrasiDriver driverRef = driver;
		TestReporter.logTrace("Initialize Page Elements");
        PageFactory.initElements(new ElementDecorator(new CustomElementLocatorFactory(driverRef), driverRef), page);
		TestReporter.logTrace("Successfully created Page Elements");
		TestReporter.logTrace("Exiting ElementFactory#initElements");
    }

    /**
     * see {@link org.openqa.selenium.support.PageFactory#initElements(org.openqa.selenium.support.pagefactory.ElementLocatorFactory, Object)}
     */
    public static void initElements(CustomElementLocatorFactory factory, Object page) {
		TestReporter.logTrace("Entering ElementFactory#initElements");
        final CustomElementLocatorFactory factoryRef = factory;
		TestReporter.logTrace("Initialize Page Elements");
        PageFactory.initElements(new ElementDecorator(factoryRef), page);
		TestReporter.logTrace("Successfully created Page Elements");
		TestReporter.logTrace("Exiting ElementFactory#initElements");
    }

    /**
     * see {@link org.openqa.selenium.support.PageFactory#initElements(org.openqa.selenium.support.pagefactory.ElementLocatorFactory, Object)}
     */
    public static void initElements(FieldDecorator decorator, Object page) {
		TestReporter.logTrace("Entering ElementFactory#initElements");
		TestReporter.logTrace("Initialize Page Elements");
        PageFactory.initElements(decorator, page);
		TestReporter.logTrace("Successfully created Page Elements");
		TestReporter.logTrace("Exiting ElementFactory#initElements");
    }

    /**
     * Copy of {@link org.openqa.selenium.support.PageFactory#instantiatePage(org.openqa.selenium.WebDriver, Class)}
     */
    private static <T> T instantiatePage(WebDriver driver, Class<T> pageClassToProxy) {
		TestReporter.logTrace("Entering ElementFactory#instantiatePage");
        try {
            try {
            	TestReporter.logTrace("Create Constructor of Page object");
                Constructor<T> constructor = pageClassToProxy.getConstructor(WebDriver.class);
            	TestReporter.logTrace("Successfully created Constructor");

            	TestReporter.logTrace("Create new instance of Page object");
            	T instance = constructor.newInstance(driver);
            	TestReporter.logTrace("Successfully created new Page instance");
        		TestReporter.logTrace("Exiting ElementFactory#instantiatePage");
                return instance;
            } catch (NoSuchMethodException e) {
        	try{            		
        		TestReporter.logTrace("Entering ElementFactory#instantiatePage");
        		return pageClassToProxy.newInstance();
        	}catch(InstantiationException ie){ 
        		throw new PageInitialization("Failed to create instance of: " + pageClassToProxy.getName(), driver);
        	}
            }
        } catch (InstantiationException e) {
            throw new PageInitialization("Failed to create instance of: " + pageClassToProxy.getName(), driver);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}