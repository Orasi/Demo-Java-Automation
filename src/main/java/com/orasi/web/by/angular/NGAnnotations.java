package com.orasi.web.by.angular;

import java.lang.reflect.Field;

import org.apache.commons.lang3.StringUtils;

import com.orasi.utils.JavaUtilities;

public class NGAnnotations {
    private Field field;

    /**
     * @param field
     *            expected to be an element in a Page Object
     */
    public NGAnnotations(Field field) {
        this.field = field;
    }

    /**
     * {@inheritDoc}
     *
     * Looks for one of {@link org.openqa.selenium.support.FindBy},
     * {@link org.openqa.selenium.support.FindBys} or
     * {@link org.openqa.selenium.support.FindAll} field annotations. In case
     * no annotaions provided for field, uses field name as 'id' or 'name'.
     *
     * @throws IllegalArgumentException
     *             when more than one annotation on a field provided
     */
    public ByNG buildBy() {
        ByNG by = null;

        FindByNG findByNG = field.getAnnotation(FindByNG.class);
        if (JavaUtilities.isValid(findByNG)) {
            by = buildByNGFindBy(findByNG);
        }

        if (by == null) {
            throw new IllegalArgumentException("Cannot determine how to locate element " + field);
        }

        return by;
    }

    /**
     * Return the corresponding ByNG class for the type used in FindByNG
     *
     * @param findByNG
     * @return
     */
    protected ByNG buildByNGFindBy(FindByNG findByNG) {
        if (StringUtils.isNotEmpty(findByNG.ngController())) {
            return ByNG.controller(findByNG.ngController());
        } else if (StringUtils.isNotEmpty(findByNG.ngModel())) {
            return ByNG.model(findByNG.ngModel());
        } else if (StringUtils.isNotEmpty(findByNG.ngRepeater())) {
            return ByNG.repeater(findByNG.ngRepeater());
        } else if (StringUtils.isNotEmpty(findByNG.ngShow())) {
            return ByNG.show(findByNG.ngShow());
        }
        return null;
    }

}
