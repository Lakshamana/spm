package br.ufpa.labes.spm.repository.impl.types;

import javax.ejb.Stateless;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.types.ITypeDAO;
import br.ufpa.labes.spm.domain.Type;

@Stateless
public class TypeDAO extends BaseDAO<Type, String> implements ITypeDAO{

	protected TypeDAO(Class<Type> businessClass) {
		super(businessClass);
	}

	public TypeDAO() {
		super(Type.class);
	}

}
