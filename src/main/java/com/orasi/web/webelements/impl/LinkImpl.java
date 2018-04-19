package com.orasi.web.webelements.impl;

import static com.orasi.utils.TestReporter.interfaceLog;
import static com.orasi.utils.TestReporter.logTrace;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.orasi.web.OrasiDriver;
import com.orasi.web.webelements.Link;

/**
 * Wraps a label on a html form with some behavior.
 */
public class LinkImpl extends ElementImpl implements Link {
    /**
     * Creates a Element for a given WebElement.
     *
     * @param element
     *            element to wrap up
     */

    public LinkImpl(OrasiDriver driver, By by) {
        super(driver, by);
    }

    public LinkImpl(OrasiDriver driver, By by, WebElement element) {
        super(driver, by, element);
    }

    @Override
    public void jsClick() {
        logTrace("Entering LinkImpl#jsClick");

        try {
            driver.executeJavaScript(
                    "if( document.createEvent ) {var click_ev = document.createEvent('MouseEvents'); click_ev.initEvent('click', true , true )"
                            + ";arguments[0].dispatchEvent(click_ev);} else { arguments[0].click();}",
                    getWrappedElement());
        } catch (RuntimeException rte) {
            interfaceLog(" Click Link [ <b>" + getElementLocatorInfo() + " </b> ]", true);
            throw rte;
        }
        interfaceLog(" Click Link [ <b>" + getElementLocatorInfo() + " </b> ]");
        logTrace("Exiting CheckboxImpl#jsClick");

    }

    @Override
    public void click() {
        logTrace("Entering LinkImpl#click");
        try {
            getWrappedElement().click();
        } catch (RuntimeException rte) {
            interfaceLog(" Click Link [ <b>" + getElementLocatorInfo() + " </b> ]", true);
            throw rte;
        }
        interfaceLog(" Click Link [ <b>" + getElementLocatorInfo() + " </b> ]");
        logTrace("Exiting LinkImpl#click");
    }

    @Override
    public String getURL() {
        logTrace("Entering LinkImpl#getURL");
        String url = getWrappedElement().getAttribute("href");
        logTrace("Exiting LinkImpl#getURL");
        return url;
    }
}
