package br.ufpa.labes.spm.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Adailton Lima
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface DTOClassMapping {

	/*
	 * Attribute where should be setted the Entity class name.
	 */
	Class<?> sourceClass();

}
