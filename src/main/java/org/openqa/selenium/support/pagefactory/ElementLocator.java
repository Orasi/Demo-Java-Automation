package org.openqa.selenium.support.pagefactory;

import java.util.List;

import org.openqa.selenium.WebElement;

public interface ElementLocator {
    <T extends WebElement> T findElement();

    <T extends WebElement> List<T> findElements();
}
