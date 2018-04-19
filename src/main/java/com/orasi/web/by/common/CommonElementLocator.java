package com.orasi.web.by.common;

import java.lang.reflect.Field;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ElementLocator;

@SuppressWarnings("unused")
public class CommonElementLocator implements ElementLocator {
    private final WebDriver driver;
    private final ByCommon by;

    public CommonElementLocator(final WebDriver driver, final Field field) {
        this(driver, new CommonAnnotations(field));
    }

    public CommonElementLocator(final WebDriver driver, final CommonAnnotations field) {
        this.driver = driver;
        this.by = field.buildBy();
    }

    @Override
    public WebElement findElement() {
        return null;
    }

    @Override
    public List<WebElement> findElements() {
        return null;
    }
}
