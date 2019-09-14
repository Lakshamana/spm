package br.ufpa.labes.spm.repository.impl.policies.instantiationPolicies;


import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.policies.instantiationPolicies.IInstGroupDAO;
import br.ufpa.labes.spm.domain.InstGroup;

public class InstGroupDAO extends BaseDAO<InstGroup, String> implements IInstGroupDAO{

	protected InstGroupDAO(Class<InstGroup> businessClass) {
		super(businessClass);
	}

	public InstGroupDAO() {
		super(InstGroup.class);
	}


}
