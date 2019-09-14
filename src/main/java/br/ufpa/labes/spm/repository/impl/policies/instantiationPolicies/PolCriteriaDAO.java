package br.ufpa.labes.spm.repository.impl.policies.instantiationPolicies;


import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.policies.instantiationPolicies.IPolCriteriaDAO;
import br.ufpa.labes.spm.domain.PolCriteria;

public class PolCriteriaDAO extends BaseDAO<PolCriteria, Integer> implements IPolCriteriaDAO{

	protected PolCriteriaDAO(Class<PolCriteria> businessClass) {
		super(businessClass);
	}

	public PolCriteriaDAO() {
		super(PolCriteria.class);
	}


}
