package br.ufpa.labes.spm.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Classe que implementa a Annotation DTOMapping. Esta annotation deve ser usada em todos os
 * atributos de classe que se queira converter de Entidade para DTO ou de DTO para Entidade. Esta
 * annotation possui dois atributos: entityClass e attribute. O 'entityClass' deve ser usado para
 * referir a que classe o atributo no DTO pertence, ou seja, ele se refere � classe que ir� fornerce
 * um atributo para a composi��o do DTO ou � classe que receber� um atributo vindo do DTO,
 * dependendo da dire��o da convers�o. O 'attribute' faz refer�ncia ao atributo que se queira mapear
 * da Entidade para o DTO ou do DTO para uma Entidade. Esta annotation deve ser usada da seguinte
 * forma: "@DTOMapping(entityClass = examples.EntidadeA.class, attribute = "atributo1")" "private
 * String atribA;"
 *
 * @author Adailton Lima
 * @author Murilo Sales
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface DTOMapping {

  /*
   * Attribute where should be setted the Entity class name.
   */
  Class<?> entityClass();

  /*
   * Attribute where should be setted the Entity attribute name.
   */
  String attribute();
}
