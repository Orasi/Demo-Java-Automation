package com.orasi.web.webelements;

import java.util.List;

import com.orasi.web.webelements.impl.ListboxImpl;
import com.orasi.web.webelements.impl.internal.ImplementedBy;

/**
 * Interface for a select element.
 */
@ImplementedBy(ListboxImpl.class)
public interface Listbox extends Element {

    /**
     * @summary - Deselect all selection options only if multi-select Listbox
     */
    void deselectAll();

    /**
     * @summary - Click option with text
     * @param text
     *            - visible text to select
     */
    void deselectByVisibleText(String text);

    /**
     * @summary - return first option that is select in list
     * @return first option that is select in list
     */
    Element getFirstSelectedOption();

    /**
     * @summary - return list of all options in the select
     * @return list of all options in the select.
     */
    List<Element> getOptions();

    /**
     * @summary - list of all option values in the select.
     * @return list of all option values in the select.
     */
    List<String> getOptionValues();

    /**
     * @summary - Wraps Selenium's method.
     * @return list of all option values in the select.
     */
    List<String> getOptionTextValues();

    /**
     * @summary - returns list of all selected options in a given listbox
     * @return list of all selected options in a given listbox
     */
    List<Element> getAllSelectedOptions();

    /**
     * @summary - Checks Listbox for multiple attribute
     * @return boolean based on if multiple attribute is found
     */
    boolean isMultiple();

    /**
     * @return TRUE if element is currently selected using Selenium isSelected
     * @see org.openqa.selenium.WebElement#isSelected()
     */
    boolean isSelected(String option);

    /**
     * @param tag
     *            - xpath tag of element that code should search for option text or value attribute
     * @summary - Define the tag where code should search for option text or value attribute
     */
    public void overrideOptionTag(String tag);

    /**
     * @param tag
     *            - xpath tag of element that code should click for found options
     * @summary - Define the tag where code should click for found options
     */
    public void overrideClickableTag(String tag);

    /**
     * @summary - Click option with text
     * @param text
     *            - visible text to select
     */
    void select(String value);

    /**
     * @summary - Click option with attribute with specific value
     * @param text
     *            - value option to select
     */
    void selectValue(String value);
}