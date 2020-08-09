package de.gmxhome.golkonda.howto.jee.event.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.inject.Qualifier;

import de.gmxhome.golkonda.howto.jee.event.ScannerEvent;
import de.gmxhome.golkonda.howto.jee.event.ScannerEventSource;

/**
 * Diese Annotation qualifiziert {@linkplain ScannerEvent}s für den Start eines Scanners
 * @author mbeier
 * @see ScannerEventSource
 */
@Qualifier
@Retention(RUNTIME)
@Target({PARAMETER, FIELD})
public @interface ScannerStart {

}
