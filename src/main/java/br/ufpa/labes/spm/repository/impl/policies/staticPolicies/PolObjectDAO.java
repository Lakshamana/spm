package br.ufpa.labes.spm.repository.impl.policies.staticPolicies;

import javax.ejb.Stateless;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.policies.staticPolicies.IPolObjectDAO;
import br.ufpa.labes.spm.domain.PolObject;

@Stateless
public class PolObjectDAO extends BaseDAO<PolObject, Integer> implements IPolObjectDAO{

	protected PolObjectDAO(Class<PolObject> businessClass) {
		super(businessClass);
	}

	public PolObjectDAO() {
		super(PolObject.class);
	}


}
