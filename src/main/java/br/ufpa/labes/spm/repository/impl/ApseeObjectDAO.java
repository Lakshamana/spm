package br.ufpa.labes.spm.repository.impl;


import br.ufpa.labes.spm.repository.interfaces.IApseeObjectDAO;

public class ApseeObjectDAO extends BaseDAO<Object, Integer> implements IApseeObjectDAO {

	protected ApseeObjectDAO(Class<Object> businessClass) {
		super(businessClass);
	}

	public ApseeObjectDAO() {
		super(Object.class);
	}
	
	public <T> T retrieve(Class<T> classe, String key) {
		return this.getPersistenceContext().find(classe, key);
	}
}
