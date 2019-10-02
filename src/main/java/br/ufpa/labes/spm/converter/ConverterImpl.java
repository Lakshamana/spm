package br.ufpa.labes.spm.converter;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import br.ufpa.labes.spm.annotations.DTOMapping;
import br.ufpa.labes.spm.annotations.DTOToEntityRequired;
import br.ufpa.labes.spm.annotations.EntityToDTORequired;
import br.ufpa.labes.spm.annotations.IgnoreMapping;
import br.ufpa.labes.spm.exceptions.ImplementationException;

/**
 * Classe respons�vel por implementar os servi�os disponibilizados na Interface Converter. Nesta
 * classe est�o implementados os servi�os de convers�o de Entidades em DTO e DTO em Entidades. A
 * finalidade desses servi�os � facilitar o trabalho do programador na convers�o dos referidos
 * tipos, uma vez que o progamador n�o precisa fazer todo o trabalho manualmente.
 *
 * @author Breno Fran�a
 * @author Adailton Lima
 * @author Murilo Sales
 */
public class ConverterImpl implements Converter {

  /**
   * Instances of input entities Targeted DTO
   *
   * <p>M�todo getDTO. Este m�todo recebe uma lista de Entidades, que possuem informa��es
   * necess�rias para a execu��o de alguma a��o dentro do sistema, e retorna um DTO que cont�m todas
   * as informa��es relevantes provenientes das Entidades passadas como par�metro. O mapeamento de
   * quais informa��es de uma entidade dever�o estar presentes no DTO � feito atrav�s de Annotation
   * que s�o inseridas no DTO e a partir das quais � poss�vel fazer a convers�o tanto de Entidades
   * em DTO, quanto de um DTO para Entidades.
   *
   * @param entities - entidades que possuem informa��es que dever�o compor o DTO
   * @throws - Exce��o no caso de um DTO de um determinado tipo n�o puder ser instanciado
   * @throws - Exce��o no caso de um determinado atributo da classe do DTO a ser instanciado n�o
   *     estiver mapeado
   * @throws - Exce��o no caso de um atributo mapeado no DTO n�o possuir m�todo get p�blico em sua
   *     Entidade de origem
   * @throws - Exce��o no caso de um atributo mapeado n�o existir na Entidade referida
   * @return Object
   */
  public Object getDTO(List<?> entities, Class<?> dtoClass) throws ImplementationException {
    Field[] fields = dtoClass.getDeclaredFields();

    Object targetDTO = null;
    try {
      targetDTO = (Object) dtoClass.newInstance();
    } catch (InstantiationException e) {
      throw new ImplementationException(
          "O objeto DTO da classe '" + dtoClass + "' nao pode ser instanciado!");
    } catch (IllegalAccessException e) {
      throw new ImplementationException(
          "O objeto DTO da classe '"
              + dtoClass
              + "' nao pode ser instanciado! Verifique se existe construtor publico vazio.");
    }

    for (int i = 0; i < fields.length; i++) {
      Field field = fields[i];
      // Anotacao responsavel pelo mapeamento

      boolean ignoreField = (field.getAnnotation(IgnoreMapping.class) == null) ? false : true;
      if (ignoreField) continue; // simplesmente pula para proximo campo, pois este ser� ignorado

      DTOMapping mapping = field.getAnnotation(DTOMapping.class);
      boolean isRequired = (field.getAnnotation(EntityToDTORequired.class) == null) ? false : true;

      // Campo n�o possui annotaion para ser ignorada e n�o possui mapeamento, portanto est� errado
      if (mapping == null) {
        throw new ImplementationException(
            "O atributo '"
                + field.getName()
                + "' da classe '"
                + dtoClass
                + "' nao possui mapeamento. \n Adicione @IgnoreMapping ou o mapeamento correspondente.");
      }

      // Entity class
      Class<?> eClass = mapping.entityClass();
      // obtem objeto que contem atributo a ser adicionado ao DTO
      Object entity =
          this.getElementOfClass(
              entities,
              eClass); // TODO: COLOCAR TESTE PRA ANNOTATION, PARA VERIFICAR SE MESMO SENDO NULO VAI
                       // IGNORAR OU VAI SER OBRIGATORIO
      if (entity == null) {
        if (isRequired) {
          // excecao pq um campo required nao tem classe que possa preenche-lo
          throw new ImplementationException(
              "O atributo '"
                  + field.getName()
                  + "' da classe '"
                  + dtoClass
                  + "' � obrigatorio. \n Uma inst�ncia da classe '"
                  + eClass
                  + "' precisa ser par�metro desta convers�o.");
        } else {
          // ignora campo, pq mesmo sendo nulo nao eh obrigatorio
          continue;
        }
      }
      // obtem nome do atributo
      String attribute = mapping.attribute();
      Method entityGetter = null;

      entityGetter = this.getter(entity.getClass(), attribute);

      if (entityGetter == null) {
        // erro, pois o mapeamento aponta para um atributo que nao
        // possui get ou nao existe
        throw new ImplementationException(
            "Nao existe um metodo 'get' para o atributo '"
                + attribute
                + "' na classe '"
                + entity.getClass().getSimpleName()
                + "' .");
      }
      Method dtoSetter = this.setter(dtoClass, field.getName());
      try {
        // Recuperando propriedade da entidade
        Object value = entityGetter.invoke(entity, null);
        // Setando a propriedade no DTO
        dtoSetter.invoke(targetDTO, value);
      } catch (IllegalArgumentException e) {
        throw new ImplementationException(
            "Nao foi possivel chamar m�todo 'get'. Este metodo nao deve possuir parametros.");
      } catch (IllegalAccessException e) {
        throw new ImplementationException(
            "Nao foi possivel chamar atributo. O mesmo nao existe ou nao possui permissao de acesso public 'get'.");
      } catch (InvocationTargetException e) {
        throw new ImplementationException(
            "Nao foi possivel chamar atributo que nao existe ou nao possui um metodo 'get'.");
      }
    }

    return targetDTO;
  }

  public Object getDTO(Object entity, Class<?> dtoClass) throws ImplementationException {
    Field[] fields = dtoClass.getDeclaredFields();

    Object targetDTO = null;
    try {
      targetDTO = (Object) dtoClass.newInstance();
    } catch (InstantiationException e) {
      throw new ImplementationException(
          "O objeto DTO da classe '" + dtoClass + "' nao pode ser instanciado!");
    } catch (IllegalAccessException e) {
      throw new ImplementationException(
          "O objeto DTO da classe '"
              + dtoClass
              + "' nao pode ser instanciado! Verifique se existe construtor publico vazio.");
    }

    for (int i = 0; i < fields.length; i++) {
      Field field = fields[i];

      // Verifica mapeamento

      boolean ignoreField = (field.getAnnotation(IgnoreMapping.class) == null) ? false : true;
      if (ignoreField) continue; // simplesmente pula para proximo campo, pois este ser� ignorado

      // Entity class
      Class<?> eClass = entity.getClass();

      Method entityGetter = null;

      entityGetter = this.getter(eClass, field.getName());

      if (entityGetter == null) {
        // erro, pois o mapeamento aponta para um atributo que nao
        // possui get ou nao existe
        throw new ImplementationException(
            "Nao existe um metodo 'get' para o atributo '"
                + field.getName()
                + "' na classe '"
                + eClass.getSimpleName()
                + "' .");
      }
      Method dtoSetter = this.setter(dtoClass, field.getName());
      try {
        // Recuperando propriedade da entidade
        Object value = entityGetter.invoke(entity, null);
        // Setando a propriedade no DTO
        dtoSetter.invoke(targetDTO, value);
      } catch (IllegalArgumentException e) {
        throw new ImplementationException(
            "Nao foi possivel chamar m�todo 'get'. Este metodo nao deve possuir parametros.");
      } catch (IllegalAccessException e) {
        throw new ImplementationException(
            "Nao foi possivel chamar atributo. O mesmo nao existe ou nao possui permissao de acesso public 'get'.");
      } catch (InvocationTargetException e) {
        throw new ImplementationException(
            "Nao foi possivel chamar atributo que nao existe ou nao possui um metodo 'get'.");
      }
    }

    return targetDTO;
  }

  /**
   * M�todo getEntities. Este m�todo recebe um DTO, que possui informa��es de v�rias Entidades do
   * modelo necess�rias para a execu��o de alguma a��o dentro do sistema, e retorna uma lista com
   * essas diversas Entidades com suas devidas informa��es prevenientes do DTO. O mapeamento de
   * quais informa��es no TDO dever�o estar presentes em quais Entidades � feito atrav�s de
   * Annotation que s�o inseridas no DTO e a partir das quais � poss�vel fazer a convers�o tanto de
   * Entidades em DTO, quanto de um DTO para Entidades.
   *
   * @param dto - dto que que possui as diversas informa��es a serem entregues �s Entidades
   * @throws - Exce��o no caso de um determinado atributo da classe do DTO a ser instanciado n�o
   *     estiver mapeado
   * @throws - Exce��o no caso de uma Entidade qualquer n�o poder ser instanciada
   * @throws - Exce��o no caso de um atributo mapeado n�o existir n�p possuir um m�todo set publico
   * @return List
   */
  public List<?> getEntities(Object dto) throws ImplementationException {
    // Lista com as entidades que devem ser retornadas.
    List<Object> entities = new LinkedList<Object>();

    // Loop principal sobre os atributos do DTO.
    Field[] fields = dto.getClass().getDeclaredFields();
    for (int i = 0; i < fields.length; i++) {
      Field field = fields[i];

      boolean ignoreField = (field.getAnnotation(IgnoreMapping.class) == null) ? false : true;
      if (ignoreField) continue; // simplesmente pula para proximo campo, pois este ser� ignorado

      // Annotacao responsavel pelo mapeamento
      DTOMapping mapping = field.getAnnotation(DTOMapping.class);
      boolean isRequired = (field.getAnnotation(DTOToEntityRequired.class) == null) ? false : true;
      // Campo sem anotacao
      if (mapping == null) {
        throw new ImplementationException(
            "O atributo '"
                + field.getName()
                + "' da classe '"
                + dto.getClass()
                + "' nao possui mapeamento. \n Adicione @IgnoreMapping ou o mapeamento correspondente.");
      }

      // Entity class
      Class<?> eClass = mapping.entityClass();

      Object entity = this.getElementOfClass(entities, eClass);
      // Checando se a entidade ja foi instanciada por outra iteracao
      if (entity == null) {
        try {
          entity = eClass.newInstance();
        } catch (InstantiationException e) {
          throw new ImplementationException(
              "O objeto da classe '" + eClass + "' nao pode ser instanciado!");
        } catch (IllegalAccessException e) {
          throw new ImplementationException(
              "O objeto da classe '"
                  + eClass
                  + "' nao pode ser instanciado! Verifique se existe construtor publico vazio");
        } catch (SecurityException e) {
          throw new ImplementationException(
              "O objeto da classe '"
                  + eClass
                  + "' nao pode ser instanciado! Violacao de seguranca na instanciacao");
        }
        entities.add(entity);
      }

      // Entity attribute
      String attribute = mapping.attribute();

      Method dtoGetter = this.getter(dto.getClass(), field.getName());

      Method eSetter = this.setter(eClass, attribute);

      try {
        Object value = dtoGetter.invoke(dto, null);
        // Setando a propriedade
        eSetter.invoke(entity, value);
      } catch (IllegalArgumentException e) {
        throw new ImplementationException(
            "O mapeamento aponta para um atributo que nao existe ou nao possui um metodo 'set'.");
      } catch (IllegalAccessException e) {
        throw new ImplementationException(
            "O mapeamento aponta para um atributo que nao existe ou nao possui um metodo 'set'.");
      } catch (InvocationTargetException e) {
        throw new ImplementationException(
            "O mapeamento aponta para um atributo que nao existe ou nao possui um metodo 'set'.");
      }
    }
    return entities;
  }

  public Object getEntity(Object dto, Object destinationEntity) throws ImplementationException {
    // Lista com as entidades que devem ser retornadas.

    // Loop principal sobre os atributos do DTO.
    Field[] fields = dto.getClass().getDeclaredFields();
    for (int i = 0; i < fields.length; i++) {
      Field field = fields[i];

      boolean ignoreField = (field.getAnnotation(IgnoreMapping.class) == null) ? false : true;
      if (ignoreField) continue; // simplesmente pula para proximo campo, pois este ser� ignorado

      Method dtoGetter = this.getter(dto.getClass(), field.getName());

      Method eSetter = this.setter(destinationEntity.getClass(), field.getName());

      try {
        Object value = dtoGetter.invoke(dto, null);
        // Setando a propriedade
        eSetter.invoke(destinationEntity, value);
      } catch (IllegalArgumentException e) {
        throw new ImplementationException(
            "O mapeamento aponta para um atributo que nao existe ou nao possui um metodo 'set'.");
      } catch (IllegalAccessException e) {
        throw new ImplementationException(
            "O mapeamento aponta para um atributo que nao existe ou nao possui um metodo 'set'.");
      } catch (InvocationTargetException e) {
        throw new ImplementationException(
            "O mapeamento aponta para um atributo que nao existe ou nao possui um metodo 'set'.");
      }
    }

    return destinationEntity;
  }

  @Override
  public Object getEntity(Object dto, Class destClass) throws ImplementationException {
    // Lista com as entidades que devem ser retornadas.
    Object destinationEntity = null;
    try {
      if (destClass != null) {
        destinationEntity = destClass.newInstance();
      } else {
        throw new ImplementationException(
            "� necess�rio o nome de uma classe v�lida para invocar este m�todo! ");
      }

      // Loop principal sobre os atributos do DTO.
      Field[] fields = dto.getClass().getDeclaredFields();
      for (int i = 0; i < fields.length; i++) {
        Field field = fields[i];

        boolean ignoreField = (field.getAnnotation(IgnoreMapping.class) == null) ? false : true;
        if (ignoreField) continue; // simplesmente pula para proximo campo, pois este ser� ignorado

        Method dtoGetter = this.getter(dto.getClass(), field.getName());

        Method eSetter = this.setter(destClass, field.getName());

        try {

          Object value = dtoGetter.invoke(dto, null);
          // Setando a propriedade
          eSetter.invoke(destinationEntity, value);
        } catch (IllegalArgumentException e) {
          throw new ImplementationException(
              "O mapeamento aponta para um atributo que nao existe ou nao possui um metodo 'set'.");
        } catch (IllegalAccessException e) {
          throw new ImplementationException(
              "O mapeamento aponta para um atributo que nao existe ou nao possui um metodo 'set'.");
        } catch (InvocationTargetException e) {
          throw new ImplementationException(
              "O mapeamento aponta para um atributo que nao existe ou nao possui um metodo 'set'.");
        }
      }

    } catch (InstantiationException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }
    return destinationEntity;
  }

  /**
   * M�todo auxiliar que retorna um objeto se o mesmo for da classe de uma determinada Entidade
   *
   * @param list - lista com diversos objetos
   * @param clazz - classe � qual o objeto deve pertencer para ser retornado
   * @return Object
   */
  private Object getElementOfClass(List<?> list, Class<?> clazz) {
    Iterator<?> iter = list.iterator();
    while (iter.hasNext()) {
      Object element = (Object) iter.next();
      if (element.getClass().getName().equals(clazz.getName())) {
        return element;
      }
    }
    return null;
  }

  /**
   * M�todo auxiliar que retorna um
   *
   * @param clazz - Classe na qual est� implementado o m�todo 'get' que se deseja recuperar
   * @param attribute - Nome do atributo o qual o m�todo 'get' busca na classe. O nome do atributo
   *     ir� compor o nome do m�todo 'get' ficando este da seguinte forma: getNomeDoAtributo.
   * @throws - Exce��o no caso do m�todo 'get' n�o estiver com permiss�o de acesso public
   * @throws - Exce��o no caso do m�todo n�o estar implementado
   * @throws - Exce��o no caso do atribulto n�o existir
   * @return Method
   */
  private Method getter(Class<?> clazz, String attribute) throws ImplementationException {
    Method getter = null;
    String methodName = null;
    try {
      Field field = getField(clazz, attribute);
      if (field != null)
        if (field.getType().equals(boolean.class) || field.getType().equals(Boolean.class)) {
          methodName = "is" + attribute.toUpperCase().charAt(0) + attribute.substring(1);
          getter = clazz.getMethod(methodName);
        } else {
          methodName = "get" + attribute.toUpperCase().charAt(0) + attribute.substring(1);
          getter = clazz.getMethod(methodName);
        }
    } catch (SecurityException e) {
      throw new ImplementationException(
          "Problema de seguranca ao acessar o m�todo '"
              + methodName
              + "' na classe '"
              + clazz.getSimpleName()
              + "'.");
    } catch (NoSuchMethodException e) {
      throw new ImplementationException(
          "O m�todo '"
              + methodName
              + "' n�o foi encontrado na classe '"
              + clazz.getSimpleName()
              + "'.");
    }
    return getter;
  }

  /**
   * M�todo auxiliar que retorna um
   *
   * @param clazz - Classe na qual est� implementado o m�todo 'set' que se deseja recuperar
   * @param attribute - Nome do atributo o qual o m�todo 'set' ir� setar na classe. O nome do
   *     atributo ir� compor o nome do m�todo 'set' ficando este da seguinte forma:
   *     setNomeDoAtributo.
   * @throws - Exce��o no caso do m�todo 'set' n�o estiver com permiss�o de acesso public
   * @throws - Exce��o no caso do m�todo n�o estar implementado
   * @throws - Exce��o no caso do atribulto n�o existir
   * @return Method
   */
  private Method setter(Class<?> clazz, String attribute) throws ImplementationException {
    Method setter = null;
    String methodName = null;
    Field field = null;
    try {
      field = getField(clazz, attribute);
      if (field != null) {
        methodName = "set" + attribute.toUpperCase().charAt(0) + attribute.substring(1);
        setter = clazz.getMethod(methodName, field.getType());
      }
    } catch (SecurityException e) {
      throw new ImplementationException(
          "Problema de seguranca ao acessar o m�todo '"
              + methodName
              + "' na classe '"
              + clazz.getSimpleName()
              + "' com parametro do tipo '"
              + field.getType().getName()
              + "'.");
    } catch (NoSuchMethodException e) {
      throw new ImplementationException(
          "O m�todo '"
              + methodName
              + "' n�o foi encontrado na classe '"
              + clazz.getSimpleName()
              + "'.");
    }
    return setter;
  }

  private Field getField(Class<?> clazz, String name) throws ImplementationException {
    try {
      return clazz.getDeclaredField(name);
    } catch (SecurityException e) {
      throw new ImplementationException(
          "Problema de seguranca ao acessar o atributo '"
              + name
              + "' na classe '"
              + clazz.getSimpleName()
              + "'.");
    } catch (NoSuchFieldException e) {
      Class<?> superclass = clazz.getSuperclass();
      if (superclass != null) return getField(superclass, name);
    }
    return null;
  }
}
