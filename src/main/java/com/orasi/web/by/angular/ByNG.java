package com.orasi.web.by.angular;
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
public abstract class ByNG extends By {

    /**
     * @param ngController
     *            The value of the "controller" attribute to search for
     * @return a By which locates elements by the value of the "controller" attribute.
     */
    public static ByNG controller(final String controller) {
        if (!JavaUtilities.isValid(controller)) {
            throw new IllegalArgumentException(
                    "Cannot find elements when controller text is null.");
        }

        return new ByNGController(controller);
    }

    /**
     * @param model
     *            The value of the "model" attribute to search for
     * @return a By which locates elements by the value of the "model" attribute.
     */
    public static ByNG model(final String model) {
        if (!JavaUtilities.isValid(model)) {
            throw new IllegalArgumentException(
                    "Cannot find elements when model text is null.");
        }

        return new ByNGModel(model);
    }

    /**
     * @param eepeater
     *            The value of the "repeater" attribute to search for
     * @return a By which locates elements by the value of the "repeater" attribute.
     */
    public static ByNG repeater(final String repeater) {
        if (!JavaUtilities.isValid(repeater)) {
            throw new IllegalArgumentException(
                    "Cannot find elements when repeater text is null.");
        }

        return new ByNGRepeater(repeater);
    }

    /**
     * @param show
     *            The value of the "show" attribute to search for
     * @return a By which locates elements by the value of the "show" attribute.
     */
    @Beta
    public static ByNG show(final String show) {
        if (!JavaUtilities.isValid(show)) {
            throw new IllegalArgumentException(
                    "Cannot find elements when show text is null.");
        }

        return new ByNGShow(show);
    }

    public static class ByNGModel extends ByNG implements Serializable {

        private static final long serialVersionUID = 376317282960469555L;

        private final String model;

        public ByNGModel(String model) {
            this.model = ".//*[@ng-model=\"" + model + "\"]";

            // Uncomment to enable deep searches
            // this.model = ".//*[(@ng-model=\"" + model + "\") or (@ng_model=\"" + model + "\") or (@data-ng-model=\"" + model + "\") or (@x-ng-model=\"" +
            // model +
            // "\")]";
        }

        @Override
        public List<WebElement> findElements(SearchContext context) {
            return ((FindsByXPath) context).findElementsByXPath(model);
        }

        @Override
        public String toString() {
            return "By.model: " + model;
        }
    }

    @Beta
    public static class ByNGShow extends ByNG implements Serializable {

        private static final long serialVersionUID = 376317282960469555L;

        private final String show;

        public ByNGShow(String show) {
            this.show = ".//*[@ng-show=\"" + show + "\"]";

            // Uncomment to enable deep searches
            // this.show = ".//*[(@ng-show=\"" + show + "\") or (@ng_show=\"" + show + "\") or (@data-ng-show=\"" + show + "\") or (@x-ng-show=\"" + show +
            // "\")]";
        }

        @Override
        public List<WebElement> findElements(SearchContext context) {
            return ((FindsByXPath) context).findElementsByXPath(show);
        }

        @Override
        public String toString() {
            return "By.show: " + show;
        }
    }

    public static class ByNGController extends ByNG implements Serializable {

        private static final long serialVersionUID = 376317282960469555L;

        private final String controller;

        public ByNGController(String controller) {
            this.controller = ".//*[@ng-controller=\"" + controller + "\"]";

            // Uncomment to enable deep searches
            // this.model = ".//*[(@ng-controller=\"" + controller + "\") or (@ng_controller=\"" + controller + "\") or (@data-ng-controller=\"" + controller +
            // "\")
            // or (@x-ng-controller=\"" + controller + "\")]";
        }

        @Override
        public List<WebElement> findElements(SearchContext context) {
            return ((FindsByXPath) context).findElementsByXPath(controller);
        }

        @Override
        public String toString() {
            return controller;
        }
    }

    public static class ByNGRepeater extends ByNG implements Serializable {
        private static final long serialVersionUID = 376317282960469555L;

        private final String repeater;

        public ByNGRepeater(String repeater) {
            this.repeater = ".//*[@ng-repeater=\"" + repeater + "\" or (@ng-repeat=\"" + repeater + "\")]";

            // Uncomment to enable deep searches
            // this.repeater = ".//*[(@ng-repeater=\"" + repeater + "\") or (@ng_repeater=\"" + repeater + "\") or (@data-ng-repeater=\"" + repeater + "\") or
            // (@x-ng-repeater=\"" + repeater"\")]";
        }

        @Override
        public List<WebElement> findElements(SearchContext context) {
            return ((FindsByXPath) context).findElementsByXPath(repeater);
        }

        @Override
        public String toString() {
            return "By.repeater: " + repeater;
        }
    }
}