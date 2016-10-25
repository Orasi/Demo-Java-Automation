package com.orasi.core.interfaces.impl.internal;

import com.orasi.core.interfaces.Element;
import com.orasi.utils.Constants;
import com.orasi.utils.OrasiDriver;
import com.orasi.utils.TestReporter;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ElementLocator;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

import static com.orasi.core.interfaces.impl.internal.ImplementedByProcessor.getWrapperClass;

/**
 * Replaces DefaultLocatingElementHandler. Simply opens it up to descendants of the WebElement interface, and other
 * mix-ins of WebElement and Locatable, etc. Saves the wrapping type for calling the constructor of the wrapped classes.
 */
public class ElementHandler  implements InvocationHandler {
	private final ElementLocator locator;
	private final Class<?> wrappingType;
	private OrasiDriver driver;

	/**
	 * Generates a handler to retrieve the WebElement from a locator for a given WebElement interface descendant.
	 *
	 * @param interfaceType Interface wrapping this class. It contains a reference the the implementation.
	 * @param locator       Element locator that finds the element on a page.
	 * @param <T>           type of the interface
	 */
	public <T> ElementHandler(Class<T> interfaceType, ElementLocator locator, OrasiDriver driver) {
		this.locator = locator;
		this.driver = driver;
		if (!Element.class.isAssignableFrom(interfaceType)) {
			throw new RuntimeException("interface not assignable to Element.");
		}

		this.wrappingType = getWrapperClass(interfaceType);
	}

	public <T> ElementHandler(Class<T> interfaceType, ElementLocator locator) {
		this.locator = locator;
		if (!Element.class.isAssignableFrom(interfaceType)) {
			throw new RuntimeException("interface not assignable to Element.");
		}

		this.wrappingType = getWrapperClass(interfaceType);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Object invoke(Object object, Method method, Object[] objects) throws Throwable {
    	TestReporter.logTrace("Entering ElementHandler#invoke");
    	TestReporter.logTrace("Attempting to invoke method [ " +method.getName() +" ]");
		WebElement element = null;

		By by= null;
		Field elementField = null;
		
		TestReporter.logTrace("Get locator By information");
		elementField = locator.getClass().getDeclaredField("by");
		elementField.setAccessible(true);
		by=(By)elementField.get(locator);

		if ("getWrappedElement".equals(method.getName())) {
			TestReporter.logTrace("Returning internal element");
			return locator.findElement();
		}

		if ("getWrappedDriver".equals(method.getName())) {
			TestReporter.logTrace("Returning internal driver");
			return driver;
		}

		TestReporter.logTrace("Generate constructor for element");
		Constructor cons = wrappingType.getConstructor(OrasiDriver.class, By.class);
		TestReporter.logTrace("Successfully created constructor");
		TestReporter.logTrace("Creating new instance of element");
		Object thing = cons.newInstance(driver, by);
		TestReporter.logTrace("Successfully created element instance");
		try {
			TestReporter.logTrace("Calling method [ " + method.getName() + " ]");
			Object response = method.invoke(wrappingType.cast(thing), objects);
			TestReporter.logTrace("Successfully called method [ " + method.getName() + " ]");
			return response;
		} catch (InvocationTargetException e) {
			// Unwrap the underlying exception
			throw e.getCause();
		} catch (WebDriverException e){
			return false;
		}
	}
}