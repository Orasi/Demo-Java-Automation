package com.orasi.web.by.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface FindByCommon {
    HowCommon howNG() default HowCommon.ID;

    String using() default "";

    String textValueContains() default "";

    String textValue() default "";
}
