package com.orasi.web.by.angular;

import java.lang.reflect.Field;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ElementLocator;

@SuppressWarnings("unused")
public class AngularElementLocator implements ElementLocator {
    private final WebDriver driver;
    private final ByNG by;

    public AngularElementLocator(final WebDriver driver, final Field field) {
        this(driver, new NGAnnotations(field));
    }

    public AngularElementLocator(final WebDriver driver, final NGAnnotations field) {
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
