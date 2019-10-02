package br.ufpa.labes.spm.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Classe que implementa a Annotation EntityToDTORequired. Esta annotation deve ser usada em todos
 * os atributos de classe que se queira transportar na convers�o de uma Entidade para DTO. O uso
 * desta annotation faz com que a convers�o o DTO, garantindo que nenhum atributo no DTO esteja com
 * valor null ap�s a convers�o.
 *
 * @author Adailton Lima
 * @author Murilo Sales
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface EntityToDTORequired {}
