package com.orasi.web.webelements.impl;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.orasi.web.OrasiDriver;
import com.orasi.web.webelements.Label;

/**
 * Wraps a label on a html form with some behavior.
 */
public class LabelImpl extends ElementImpl implements Label {
    /**
     * Creates an Element for a given WebElement.
     *
     * @param element
     *            element to wrap up
     */

    public LabelImpl(OrasiDriver driver, By by) {
        super(driver, by);
    }

    public LabelImpl(OrasiDriver driver, By by, WebElement element) {
        super(driver, by, element);
    }

    @Override
    public String getFor() {
        return getWrappedElement().getAttribute("for");
    }
}