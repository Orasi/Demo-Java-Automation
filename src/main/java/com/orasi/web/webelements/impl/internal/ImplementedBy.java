package com.orasi.web.webelements.impl.internal;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.orasi.web.webelements.impl.ElementImpl;

/**
 * Sets the default implementing class for the annotated interface.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ImplementedBy {
    /**
     * Class implementing the interface. (by default)
     */
    @SuppressWarnings("rawtypes")
    Class value() default ElementImpl.class;
}