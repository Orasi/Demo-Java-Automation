package com.orasi.web.webelements.impl.internal;

import static com.orasi.utils.TestReporter.logTrace;
import static com.orasi.web.webelements.impl.internal.ImplementedByProcessor.getWrapperClass;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.support.pagefactory.ElementLocator;

import com.orasi.web.OrasiDriver;
import com.orasi.web.WebException;
import com.orasi.web.by.angular.AngularElementLocator;
import com.orasi.web.webelements.Element;

/**
 * Replaces DefaultLocatingElementHandler. Simply opens it up to descendants of the WebElement interface, and other
 * mix-ins of WebElement and Locatable, etc. Saves the wrapping type for calling the constructor of the wrapped classes.
 */
public class ElementHandler implements InvocationHandler {
    private final ElementLocator locator;
    private final Class<?> wrappingType;
    private OrasiDriver driver;

    /**
     * Generates a handler to retrieve the WebElement from a locator for a given WebElement interface descendant.
     *
     * @param interfaceType
     *            Interface wrapping this class. It contains a reference the the implementation.
     * @param locator
     *            Element locator that finds the element on a page.
     * @param <T>
     *            type of the interface
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
        logTrace("Entering ElementHandler#invoke");
        logTrace("Attempting to invoke method [ " + method.getName() + " ]");

        By by = null;
        Field elementField = null;

        logTrace("Get locator By information");
        try {
            elementField = locator.getClass().getDeclaredField("by");
            elementField.setAccessible(true);
            by = (By) elementField.get(locator);
        } catch (Exception e) {
            throw new WebException("Failed to obtain element locator", driver);
        }

        if ("getWrappedElement".equals(method.getName())) {
            logTrace("Returning internal element");
            return locator.findElement();
        }

        if ("getWrappedDriver".equals(method.getName())) {
            logTrace("Returning internal driver");
            return driver;
        }

        logTrace("Generate constructor for element");
        Constructor cons = null;
        cons = wrappingType.getConstructor(OrasiDriver.class, By.class);
        logTrace("Successfully created constructor");

        logTrace("Creating new instance of element");
        Object thing = null;
        if (locator instanceof AngularElementLocator) {
            thing = cons.newInstance(driver, by);
        } else {
            thing = cons.newInstance(driver, by);
        }
        logTrace("Successfully created element instance");
        try {
            logTrace("Calling method [ " + method.getName() + " ]");
            Object response = method.invoke(wrappingType.cast(thing), objects);
            logTrace("Successfully called method [ " + method.getName() + " ]");
            logTrace("Exitting ElementHandler#invoke");
            return response;
        } catch (InvocationTargetException e) {
            // Unwrap the underlying exception
            throw e.getCause();
        } catch (WebDriverException e) {
            return false;
        }
    }
}