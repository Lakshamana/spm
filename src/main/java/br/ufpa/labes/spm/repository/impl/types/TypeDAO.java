package br.ufpa.labes.spm.repository.impl.types;


import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.types.ITypeDAO;
import br.ufpa.labes.spm.domain.Type;

public class TypeDAO extends BaseDAO<Type, String> implements ITypeDAO{

	protected TypeDAO(Class<Type> businessClass) {
		super(businessClass);
	}

	public TypeDAO() {
		super(Type.class);
	}

}
