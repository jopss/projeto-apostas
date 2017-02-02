package com.jopss.apostas.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Anotacao de suporte ao GSON, indicando campos dos objetos que nao sera levado em consideracao na transformacao da String.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.FIELD })
public @interface ExcludeJSon {

}
