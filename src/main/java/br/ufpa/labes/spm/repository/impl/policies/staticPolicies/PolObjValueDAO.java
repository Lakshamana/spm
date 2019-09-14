package br.ufpa.labes.spm.repository.impl.policies.staticPolicies;


import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.policies.staticPolicies.IPolObjValueDAO;
import br.ufpa.labes.spm.domain.PolObjValue;

public class PolObjValueDAO extends BaseDAO<PolObjValue, Integer> implements IPolObjValueDAO{

	protected PolObjValueDAO(Class<PolObjValue> businessClass) {
		super(businessClass);
	}

	public PolObjValueDAO() {
		super(PolObjValue.class);
	}


}
