package com.orasi.web.webelements.impl.internal;

import static com.orasi.utils.TestReporter.logTrace;

import org.openqa.selenium.support.PageFactory;

import com.orasi.web.OrasiDriver;

/**
 * Element factory for wrapped elements. Similar to {@link org.openqa.selenium.support.PageFactory}
 */
public class ElementFactory {

    /**
     * See {@link org.openqa.selenium.support.PageFactory#initElements(org.openqa.selenium.support.pagefactory.FieldDecorator, Object)}
     */
    public static void initElements(OrasiDriver driver, Object page) {
        logTrace("Entering ElementFactory#initElements");
        final OrasiDriver driverRef = driver;
        logTrace("Initialize Page Elements");
        PageFactory.initElements(new ElementDecorator(new CustomElementLocatorFactory(driverRef), driverRef), page);
        logTrace("Successfully created Page Elements");
        logTrace("Exiting ElementFactory#initElements");
    }
}