package de.gmxhome.golkonda.howto.jse.jvm;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Definiert den Provider, der die Liste der Versionsnummern
 * (siehe {@linkplain JavaVersionInterface#getVersion()} bereitstellt.
 * Standardmäßig wird {@linkplain Runtime#version()} verwendet.
 * Dieser Provider wird über die Klasse {@linkplain RuntimeVersionProvider}
 * bereitgestellt.
 * @author mbeier
 * @see JavaVersionImpl
 */
@Retention(RUNTIME)
@Target(TYPE)
public @interface VersionProvider {
	public Class<?> provider() default RuntimeVersionProvider.class;
}
