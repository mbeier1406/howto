package com.github.mbeier1406.howto.jee.event.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.inject.Qualifier;

import com.github.mbeier1406.howto.jee.event.ScannerEvent;
import com.github.mbeier1406.howto.jee.event.ScannerEventSource;

/**
 * Diese Annotation qualifiziert {@linkplain ScannerEvent}s für den Stopp eines Scanners
 * @author mbeier
 * @see ScannerEventSource
 */
@Qualifier
@Retention(RUNTIME)
@Target({PARAMETER, FIELD})
public @interface ScannerStop {

}
