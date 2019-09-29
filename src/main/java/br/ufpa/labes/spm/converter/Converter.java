package br.ufpa.labes.spm.converter;

import java.util.Collection;

import java.util.List;

import br.ufpa.labes.spm.exceptions.ImplementationException;


/**
 * Interface respons�vel por fornecer os servi�os de convers�o de Entidades em DTO e DTO em Entidades.
 * O uso dos servi�os por esta interface objetivam facilitar os servi�os dos programadores no que diz
 * respeito � convers�o das Entidades dos sistemas nos DTOs que carregam informa��es dessas entidades e
 * vice-versa.
 *
 * @author Breno Fran�a
 * @author Adailton Lima
 * @author Murilo Sales
 * @version 1.0
 */

public interface Converter {

    /**
     * Obt�m as Entidades do modelo a partir de um DTO que contenha informa��es dessas entidades
     * @return Collection
     */

	public Collection<?> getEntities(Object dto) throws ImplementationException;


	/**
     * Obt�m uma Entidade do modelo a partir de um DTO que contenha informa��es dessas entidades
     * Para utilizar este m�todo a classe de retorno tenha a mesma estrutura do DTO (em caso de n�o possuir mapeamento individual e somente a annotation indicando a entidade espelho do DTO )
     * ou
     * tenha que ter todo o mapeamento via anota��o (quando a estruturas das classes for diferente).
     * @return Object
     */
	public Object getEntity(Object dto, Class destinationClass) throws ImplementationException;


	public Object getEntity(Object dto, Object instance) throws ImplementationException;
    /**
     * Obt�m um DTO que cont�m informa��es de uma ou v�rias Entidades do modelo necess�rias para a
     * realiza��o de uma a��o qualquer dentro do sistema.
     * @return Object
     */
	public Object getDTO(List<?> entities, Class<?> dtoClass) throws ImplementationException;

	public Object getDTO(Object entity, Class<?> dtoClass) throws ImplementationException;

}
