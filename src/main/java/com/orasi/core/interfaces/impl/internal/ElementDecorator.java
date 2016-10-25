package com.orasi.core.interfaces.impl.internal;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.Locatable;
import org.openqa.selenium.internal.WrapsElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.openqa.selenium.support.pagefactory.FieldDecorator;
import org.openqa.selenium.support.pagefactory.internal.LocatingElementListHandler;

import com.orasi.core.by.angular.FindByNG;
import com.orasi.core.interfaces.Element;
import com.orasi.utils.OrasiDriver;
import com.orasi.utils.TestReporter;

/**
 * WrappedElementDecorator recognizes a few things that DefaultFieldDecorator does not.
 * <p/>
 * It is designed to support and return concrete implementations of wrappers for a variety of common html elements.
 */
public class ElementDecorator implements FieldDecorator {
    /**
     * factory to use when generating ElementLocator.
     */
    protected CustomElementLocatorFactory factory;
    protected OrasiDriver driver;

    /**
     * Constructor for an ElementLocatorFactory. This class is designed to replace DefaultFieldDecorator.
     *
     * @param factory for locating elements.
     */
 
    public ElementDecorator(CustomElementLocatorFactory factory) {
        this.factory = factory;
    }
    
    public ElementDecorator(CustomElementLocatorFactory factory, OrasiDriver driver) {
        this.factory = factory;
        this.driver = driver;
    }

    @Override
    public Object decorate(ClassLoader loader, Field field) {
        if (!(WebElement.class.isAssignableFrom(field.getType()) || isDecoratableList(field))) {
            return null;
        }
		TestReporter.logTrace("Entering ElementDecorator#ElementDecorator");
    	final OrasiDriver driverRef = driver;

    	TestReporter.logTrace("Creating proxy for element with field name [ " + field.getName() +" ]");
    	TestReporter.logTrace("Validate element with field name [ " + field.getName() +" ] is type and is decoratable");

    	TestReporter.logTrace("Create locator from CustomElementLocatorFactory");
        ElementLocator locator = factory.createLocator(field);        
        if (locator == null) {
            return null;
        }
        TestReporter.logTrace("Successfully created element locator for field name [ " + field.getName() +" ]");
        
        TestReporter.logTrace("Ensure field interface is Element or inherited from Element");
        Class<?> fieldType = field.getType();
        if (WebElement.class.equals(fieldType)) {
            fieldType = Element.class;
        }

        TestReporter.logTrace("Create Element Proxy for field name [ " + field.getName() +" ]");
        if (WebElement.class.isAssignableFrom(fieldType)) {
        	Object proxy = proxyForLocator(loader, fieldType, locator, driverRef);
            TestReporter.logTrace("Successfully created element Proxy for field name [ " + field.getName() +" ]");
    		TestReporter.logTrace("Exiting ElementDecorator#ElementDecorator");
         	 return proxy;
        } else if (List.class.isAssignableFrom(fieldType)) {
            Class<?> erasureClass = getErasureClass(field);
            Object proxy = proxyForListLocator(loader, erasureClass, locator);
            TestReporter.logTrace("Successfully created element Proxy for field name [ " + field.getName() +" ]");
    		TestReporter.logTrace("Exiting ElementDecorator#ElementDecorator");
            return proxy;
        } else {
    		TestReporter.logTrace("Exiting ElementDecorator#ElementDecorator");
            return null;
        }
    }

    @SuppressWarnings("rawtypes")
	private Class getErasureClass(Field field) {
        // Type erasure in Java isn't complete. Attempt to discover the generic
        // interfaceType of the list.
        Type genericType = field.getGenericType();
        if (!(genericType instanceof ParameterizedType)) {
            return null;
        }
        return (Class) ((ParameterizedType) genericType).getActualTypeArguments()[0];
    }

    private boolean isDecoratableList(Field field) {
	//	TestReporter.logTrace("Entering ElementDecorator#isDecoratableList");
    //	TestReporter.logTrace("Validate element is assignable from Field");
        if (!List.class.isAssignableFrom(field.getType())) {
            return false;
        }

   // 	TestReporter.logTrace("Validate element is Erasure");
        @SuppressWarnings("rawtypes")
		Class erasureClass = getErasureClass(field);
        if (erasureClass == null) {
            return false;
        }

    //	TestReporter.logTrace("Validate element is assignable from erasure");
        if (!WebElement.class.isAssignableFrom(erasureClass)) {
            return false;
        }


   // 	TestReporter.logTrace("Validate element FindBy is valid");
        if (field.getAnnotation(FindBy.class) == null && field.getAnnotation(FindBys.class) == null  && field.getAnnotation(FindByNG.class) == null) {
            return false;
        }


  //  	TestReporter.logTrace("Successfully validated element");
	//	TestReporter.logTrace("Exiting ElementDecorator#isDecoratableList");
        return true;
    }
    
    protected <T> T proxyForLocator(ClassLoader loader, Class<T> interfaceType, ElementLocator locator, OrasiDriver driver) {
		TestReporter.logTrace("Entering ElementDecorator#proxyForLocator");
    	final OrasiDriver driverRef = driver;
    	
    	TestReporter.logTrace("Create ElementHandler and embbed locator and driver in it");
        InvocationHandler handler = new ElementHandler(interfaceType, locator, driverRef);
    	TestReporter.logTrace("Successfully created ElementHandler");        
        
        T proxy;
        TestReporter.logTrace("Create element proxy with ElementHandler information");
        proxy = interfaceType.cast(Proxy.newProxyInstance(
                loader, new Class[]{interfaceType, WebElement.class, WrapsElement.class, Locatable.class}, handler));
        TestReporter.logTrace("Successfully created element proxy");
		TestReporter.logTrace("Exiting ElementDecorator#proxyForLocator");
        return proxy;
    }
    /**
     * Generate a type-parameterized locator proxy for the element in question. We use our customized InvocationHandler
     * here to wrap classes.
     *
     * @param loader        ClassLoader of the wrapping class
     * @param interfaceType Interface wrapping the underlying WebElement
     * @param locator       ElementLocator pointing at a proxy of the object on the page
     * @param <T>           The interface of the proxy.
     * @return a proxy representing the class we need to wrap.
     */
    protected <T> T proxyForLocator(ClassLoader loader, Class<T> interfaceType, ElementLocator locator) {
		TestReporter.logTrace("Entering ElementDecorator#proxyForLocator");

    	TestReporter.logTrace("Create ElementHandler and embbed locator in it");
        InvocationHandler handler = new ElementHandler(interfaceType, locator);

        T proxy;
        TestReporter.logTrace("Create element proxy with ElementHandler information");
        proxy = interfaceType.cast(Proxy.newProxyInstance(
                loader, new Class[]{interfaceType, WebElement.class, WrapsElement.class, Locatable.class}, handler));
        TestReporter.logTrace("Successfully created element proxy");
		TestReporter.logTrace("Exiting ElementDecorator#proxyForLocator");
        return proxy;
    }

    /**
     * generates a proxy for a list of elements to be wrapped.
     *
     * @param loader        classloader for the class we're presently wrapping with proxies
     * @param interfaceType type of the element to be wrapped
     * @param locator       locator for items on the page being wrapped
     * @param <T>           class of the interface.
     * @return proxy with the same type as we started with.
     */
    @SuppressWarnings("unchecked")
    protected <T> List<T> proxyForListLocator(ClassLoader loader, Class<T> interfaceType, ElementLocator locator) {
        InvocationHandler handler;
        if (interfaceType.getName().contains("orasi")) {
            handler = new ElementListHandler(interfaceType, locator);
        } else {
            handler = new LocatingElementListHandler(locator);
        }
        List<T> proxy;
        proxy = (List<T>) Proxy.newProxyInstance(
                loader, new Class[]{List.class}, handler);
        return proxy;
    }
}