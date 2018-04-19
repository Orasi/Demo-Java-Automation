package com.orasi.web.webelements.impl.internal;

import static com.orasi.utils.TestReporter.logTrace;

import com.orasi.web.webelements.Element;

/**
 * Processes the iface type into a useful class reference for wrapping WebElements.
 */
public final class ImplementedByProcessor {
    private ImplementedByProcessor() {
    }

    /**
     * Gets the wrapper class (descended from ElementImpl) for the annotation @ImplementedBy.
     *
     * @param iface
     *            iface to process for annotations
     * @param <T>
     *            type of the wrapped class.
     * @return The class name of the class in question
     */
    @SuppressWarnings("rawtypes")
    public static <T> Class getWrapperClass(Class<T> iface) {
        logTrace("Entering ImplementedByProcessor#getWrapperClass");
        logTrace("Validate Element Interface has ImplementedBy annotation ");
        if (iface.isAnnotationPresent(ImplementedBy.class)) {
            ImplementedBy annotation = iface.getAnnotation(ImplementedBy.class);
            Class clazz = annotation.value();
            if (Element.class.isAssignableFrom(clazz)) {
                logTrace("Validation successful");
                logTrace("Exiting ImplementedByProcessor#getWrapperClass");
                return annotation.value();
            }
        }
        throw new UnsupportedOperationException("Apply @ImplementedBy interface to your Interface if you want to extend ");
    }

}