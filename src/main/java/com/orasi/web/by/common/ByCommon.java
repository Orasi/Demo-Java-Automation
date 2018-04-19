package com.orasi.web.by.common;
/*
Copyright 2007-2011 Selenium committers

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

import java.io.Serializable;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.FindsByXPath;

import com.orasi.Beta;
import com.orasi.utils.JavaUtilities;

/**
 * Mechanism used to locate elements within a document. In order to create your own locating
 * mechanisms, it is possible to subclass this class and override the protected methods as required,
 * though it is expected that that all subclasses rely on the basic finding mechanisms provided
 * through static methods of this class:
 *
 * <code>
 * public WebElement findElement(WebDriver driver) {
 *     WebElement element = driver.findElement(By.id(getSelector()));
 *     if (element == null)
 *       element = driver.findElement(By.name(getSelector());
 *     return element;
 * }
 * </code>
 */
public abstract class ByCommon extends By {

    /**
     * @param show
     *            The value of the "show" attribute to search for
     * @return a By which locates elements by the value text() function
     */
    @Beta
    public static ByCommon textValue(final String textValue) {
        if (!JavaUtilities.isValid(textValue)) {
            throw new IllegalArgumentException(
                    "Cannot find elements when show text is null.");
        }

        return new ByTextValue(textValue);
    }

    /**
     * @param show
     *            The value of the "textValueContains" attribute to search for
     * @return a By which locates elements by the string contained from text() function. Will be equivalent to xpath=//*[contains(text(),"textValueContains")].
     */
    @Beta
    public static ByCommon textValueContains(final String textValueContains) {
        if (!JavaUtilities.isValid(textValueContains)) {
            throw new IllegalArgumentException(
                    "Cannot find elements when show text is null.");
        }

        return new ByTextValueContains(textValueContains);
    }

    @Beta
    public static class ByTextValue extends ByCommon implements Serializable {

        private static final long serialVersionUID = 376317282960469555L;

        private final String textValue;

        public ByTextValue(String textValue) {
            this.textValue = ".//*[text()=\"" + textValue + "\"]";
        }

        @Override
        public List<WebElement> findElements(SearchContext context) {
            return ((FindsByXPath) context).findElementsByXPath(textValue);
        }

        @Override
        public String toString() {
            return "By.textValue: " + textValue;
        }
    }

    @Beta
    public static class ByTextValueContains extends ByCommon implements Serializable {

        private static final long serialVersionUID = 376317282960469555L;

        private final String textValueContains;

        public ByTextValueContains(String textValueContains) {
            this.textValueContains = ".//*[contains(text(),\"" + textValueContains + "\")]";
        }

        @Override
        public List<WebElement> findElements(SearchContext context) {
            return ((FindsByXPath) context).findElementsByXPath(textValueContains);
        }

        @Override
        public String toString() {
            return "By.textValueContains: " + textValueContains;
        }
    }

}