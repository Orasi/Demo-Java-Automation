package com.orasi.web.webelements.impl.internal;

import static com.orasi.utils.TestReporter.logTrace;
import static com.orasi.web.webelements.impl.internal.ImplementedByProcessor.getWrapperClass;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ElementLocator;

import com.orasi.web.OrasiDriver;
import com.orasi.web.WebException;
import com.orasi.web.webelements.Element;

/**
 * Wraps a list of WebElements in multiple wrapped elements.
 */
public class ElementListHandler implements InvocationHandler {

    private final ElementLocator locator;
    private final Class<?> wrappingType;
    private OrasiDriver driver;

    /**
     * Given an interface and a locator, apply a wrapper over a list of elements.
     *
     * @param interfaceType
     *            interface type we're trying to wrap around the element.
     * @param locator
     *            locator on the page for the elements.
     * @param <T>
     *            type of the interface.
     */
    public <T> ElementListHandler(Class<T> interfaceType, ElementLocator locator) {
        this.locator = locator;
        if (!Element.class.isAssignableFrom(interfaceType)) {
            throw new RuntimeException("interface not assignable to Element.");
        }
        this.wrappingType = getWrapperClass(interfaceType);

    }

    public <T> ElementListHandler(Class<T> interfaceType, ElementLocator locator, OrasiDriver driver) {
        this.locator = locator;
        this.driver = driver;
        if (!Element.class.isAssignableFrom(interfaceType)) {
            throw new RuntimeException("interface not assignable to Element.");
        }
        this.wrappingType = getWrapperClass(interfaceType);

    }

    /**
     * Executed on invoke of the requested proxy. Used to gather a list of wrapped WebElements.
     *
     * @param o
     *            object to invoke on
     * @param method
     *            method to invoke
     * @param objects
     *            parameters for method
     * @return return value from method
     * @throws Throwable
     *             when frightened.
     */
    @SuppressWarnings("rawtypes")
    @Override
    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
        logTrace("Entering ElementListHandler#invoke");
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
        cons = wrappingType.getConstructor(OrasiDriver.class, By.class, WebElement.class);
        logTrace("Successfully created constructor");

        List<Object> wrappedList = new ArrayList<Object>();

        for (WebElement element : locator.findElements()) {
            Object thing = cons.newInstance(driver, by, element);
            wrappedList.add(wrappingType.cast(thing));
        }
        logTrace("Successfully created element instance");
        try {
            logTrace("Calling method [ " + method.getName() + " ]");
            Object response = method.invoke(wrappedList, objects);
            logTrace("Successfully called method [ " + method.getName() + " ]");
            logTrace("Exitting ElementListHandler#invoke");
            return response;
        } catch (InvocationTargetException e) {
            // Unwrap the underlying exception
            throw e.getCause();
        } catch (WebDriverException e) {
            return false;
        }
    }

}