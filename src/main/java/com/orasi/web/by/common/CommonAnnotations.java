package com.orasi.web.by.common;

import java.lang.reflect.Field;

import org.apache.commons.lang3.StringUtils;

import com.orasi.utils.JavaUtilities;

public class CommonAnnotations {
    private Field field;

    /**
     * @param field
     *            expected to be an element in a Page Object
     */
    public CommonAnnotations(Field field) {
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
    public ByCommon buildBy() {
        ByCommon by = null;

        FindByCommon findByCommon = field.getAnnotation(FindByCommon.class);
        if (JavaUtilities.isValid(findByCommon)) {
            by = buildByNGFindBy(findByCommon);
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
    protected ByCommon buildByNGFindBy(FindByCommon findByCommon) {
        if (StringUtils.isNotEmpty(findByCommon.textValue())) {
            return ByCommon.textValue(findByCommon.textValue());
        } else if (StringUtils.isNotEmpty(findByCommon.textValueContains())) {
            return ByCommon.textValueContains(findByCommon.textValueContains());
        }
        return null;
    }

}
