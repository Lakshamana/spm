package br.ufpa.labes.spm.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Classe que implementa a Annotation IgnoreMapping. Esta annotation deve ser usada em atributos de
 * classe que se queira ignorar no momento da convers�o. Um tipo de atributo a ser ignorado s�o os
 * atributos tempor�rios, os quais guardam um valor parcial que ser� usado para a composi��o de um
 * relat�rio, por exemplo.
 *
 * @author Adailton Lima
 * @author Murilo Sales
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface IgnoreMapping {}
