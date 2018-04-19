package com.orasi.web.webelements.impl;

import static com.orasi.utils.TestReporter.interfaceLog;
import static com.orasi.utils.TestReporter.logTrace;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Quotes;

import com.orasi.web.OrasiDriver;
import com.orasi.web.WebException;
import com.orasi.web.exceptions.OptionNotInListboxException;
import com.orasi.web.webelements.Element;
import com.orasi.web.webelements.Listbox;

/**
 * Wrapper around a WebElement for the Select class in Selenium.
 */
@SuppressWarnings("unchecked")
public class ListboxImpl extends ElementImpl implements Listbox {
    private String optionTag = "";
    private String clickableTag = "";
    private Boolean multiple;

    /**
     * @summary - Wraps a WebElement with listbox functionality.
     * @param element
     *            - element to wrap up
     */

    public ListboxImpl(OrasiDriver driver, By by) {
        super(driver, by);

        logTrace("Entering ListboxImpl#init");

        if (element != null) {
            optionTag = determineOptionTag();
            multiple = isMultiple();
        }

        logTrace("Exiting ListboxImpl#init");
    }

    @Override
    public void overrideOptionTag(String tag) {
        if (isBlank(tag)) {
            throw new WebException("Option tag cannot be null or empty", driver);
        }
        optionTag = tag.trim();
    }

    @Override
    public void overrideClickableTag(String tag) {
        if (isBlank(tag)) {
            throw new WebException("Clickable tag cannot be null or empty", driver);
        }
        clickableTag = tag.trim();
    }

    /**
     * @summary - Click option with text
     * @param text
     *            - visible text to select
     */
    @Override
    public void select(String text) {
        logTrace("Entering ListboxImpl#select");

        if (isNotBlank(text)) {
            // In the case when the Listbox was create, but element was not found, then optionTag is not set
            // Ensure optionTag is set
            if (isBlank(optionTag)) {
                optionTag = determineOptionTag();
            }

            // Use normalize-space on the element itself (.) to limit text search to just the element
            // Using text() would get all text in child elements as well
            List<Element> options = getWrappedElement().findElements(By.xpath(
                    ".//" + optionTag + "[normalize-space(.) = " + Quotes.escape(text) + "]"));

            // If no options found for requested text, collect all option values and report out
            if (options.isEmpty()) {
                String optionList = getOptionTextValues().stream().collect(Collectors.joining(" | "));

                interfaceLog(" The value of <b>[ " + text + "</b> ] was not found in Listbox [  <b>"
                        + getElementLocatorInfo() + " </b>]. Acceptable values are " + optionList + " ]");
                logTrace("Exiting ListboxImpl#select");
                throw new OptionNotInListboxException("The value of [ " + text + " ] was not found in Listbox [  "
                        + getElementLocatorInfo() + " ]. Acceptable values are " + optionList, driver);
            }

            // for each matching element, set to true
            for (Element option : options) {
                setSelected(option, true);
                if (!isMultiple()) {
                    return;
                }
            }

        } else {
            interfaceLog("Skipping selection of option in Listbox [ <b>" + getElementLocatorInfo() + " </b> ]");
        }

        logTrace("Exiting ListboxImpl#select");
    }

    /**
     * @summary - Click option with attribute with specific value
     * @param text
     *            - value option to select
     */
    @Override
    public void selectValue(String value) {
        logTrace("Entering ListboxImpl#selectValue");
        if (isNotBlank(value)) {

            // In the case when the Listbox was create, but element was not found, then optionTag is not set
            // Ensure optionTag is set
            if (isBlank(optionTag)) {
                optionTag = determineOptionTag();
            }

            List<Element> options = getWrappedElement().findElements(By.xpath(
                    ".//" + optionTag + "[@value = " + Quotes.escape(value) + "]"));

            // If no options found for requested value, collect all option values and report out
            if (options.isEmpty()) {
                String optionList = getOptionValues().stream().collect(Collectors.joining(" | "));

                interfaceLog(" The value of <b>[ " + value + "</b> ] was not found in Listbox [  <b>"
                        + getElementLocatorInfo() + " </b>]. Acceptable values are " + optionList + " ]");
                logTrace("Exiting ListboxImpl#selectValue");
                throw new OptionNotInListboxException("The value of [ " + value + " ] was not found in Listbox [  "
                        + getElementLocatorInfo() + " ]. Acceptable values are " + optionList, driver);
            }

            // for each matching element, set to true
            for (Element option : options) {
                setSelected(option, true);
                if (!isMultiple()) {
                    return;
                }
            }

        } else {
            interfaceLog("Skipping input to Textbox [ <b>" + getElementLocatorInfo() + " </b> ]");
        }
        logTrace("Exiting ListboxImpl#selectValue");
    }

    /**
     * @summary - Deselect all selection options only if multi-select Listbox
     */
    @Override
    public void deselectAll() {
        logTrace("Entering ListboxImpl#deselectAll");
        if (!isMultiple()) {
            throw new WebException("You may only deselect all options of a multi-select");
        }

        for (Element option : getOptions()) {
            setSelected(option, false);
        }
        logTrace("Exiting ListboxImpl#deselectAll");
    }

    /**
     * @summary - Click option with text
     * @param text
     *            - visible text to select
     */
    @Override
    public void deselectByVisibleText(String text) {
        logTrace("Entering ListboxImpl#deselectByVisibleText");
        if (!isMultiple()) {
            throw new WebException("You may only deselect options of a multi-select");
        }

        List<Element> options = getWrappedElement().findElements(By.xpath(
                ".//" + optionTag + "[normalize-space(.) =" + Quotes.escape(text) + "]"));

        for (Element option : options) {
            setSelected(option, false);
        }
        logTrace("Exiting ListboxImpl#deselectByVisibleText");
    }

    @Override
    public List<Element> getAllSelectedOptions() {
        logTrace("Entering ListboxImpl#getAllSelectedOptions");
        List<Element> options = getOptions().stream().filter(Element::isSelected).collect(toList());
        logTrace("Exiting ListboxImpl#getAllSelectedOptions");
        return options;
    }

    /**
     * @summary - return first option that is select in list
     * @return first option that is select in list
     */
    @Override
    public Element getFirstSelectedOption() {
        logTrace("Entering ListboxImpl#getFirstSelectedOption");
        Element option = getAllSelectedOptions().stream().findFirst().orElse(null);
        logTrace("Exiting ListboxImpl#getFirstSelectedOption");
        return option;
    }

    /**
     * @summary - return list of all options in the select
     * @return list of all options in the select.
     */
    @Override
    public List<Element> getOptions() {
        logTrace("Entering ListboxImpl#getOptions");
        List<Element> options = getWrappedElement().findElements(By.xpath(".//" + optionTag));
        logTrace("Exiting ListboxImpl#getOptions");
        return options;
    }

    /**
     * @summary - list of all option values in the select.
     * @return list of all option values in the select.
     */
    @Override
    public List<String> getOptionValues() {
        logTrace("Entering ListboxImpl#getOptionValues");
        // Get attribute of value from each option and return in a List
        List<String> values = getOptions().stream().map(e -> e.getAttribute("value")).map(String::trim).collect(toList());
        logTrace("Exiting ListboxImpl#getOptionValues");
        return values;
    }

    /**
     * @summary - Wraps Selenium's method.
     * @return list of all option values in the select.
     */
    @Override
    public List<String> getOptionTextValues() {
        logTrace("Entering ListboxImpl#getOptionTextValues");
        // Get text value from each option and return in a List
        List<String> values = getOptions().stream().map(WebElement::getText).map(String::trim).collect(toList());
        logTrace("Exiting ListboxImpl#getOptionTextValues");
        return values;
    }

    @Override
    public boolean isSelected(String option) {
        logTrace("Entering ListboxImpl#isSelected");
        boolean selected = getAllSelectedOptions().stream().anyMatch(selectedOption -> selectedOption.getText().equals(option));
        logTrace("Exiting ListboxImpl#isSelected");
        return selected;
    }

    @Override
    public boolean isMultiple() {
        logTrace("Entering ListboxImpl#isMultiple");
        if (multiple == null) {
            String value = getWrappedElement().getAttribute("multiple");

            // The atoms normalize the returned value, but check for "false"
            multiple = (value != null && !"false".equals(value));
        }
        logTrace("Exiting ListboxImpl#isMultiple");
        return multiple;
    }

    private String determineOptionTag() {
        String tagName = getWrappedElement().getTagName();

        switch (tagName.toLowerCase()) {
            case "ul":
                return "li";

            case "select":
            case "datalist":
            default:
                return "option";
        }
    }

    /**
     * Select or deselect specified option
     *
     * @param option
     *            The option which state needs to be changed
     * @param select
     *            Indicates whether the option needs to be selected (true) or
     *            deselected (false)
     */
    private void setSelected(final Element option, boolean select) {
        boolean isSelected = option.isSelected();
        if ((!isSelected && select) || (isSelected && !select)) {
            if (isBlank(clickableTag)) {
                option.click();
            } else {
                String currentXpath = option.getElementLocatorInfo().replace("By.xpath: ", "");
                option.findElement(By.xpath(currentXpath + "/.//" + clickableTag)).click();
            }

        }
    }
}