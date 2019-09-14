package br.ufpa.labes.spm.repository.impl.policies.instantiationPolicies;


import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.policies.instantiationPolicies.IPolRestrictionCriteriaDAO;
import br.ufpa.labes.spm.domain.PolRestrictionCriteria;

public class PolRestrictionCriteriaDAO extends BaseDAO<PolRestrictionCriteria, Integer> implements IPolRestrictionCriteriaDAO{

	protected PolRestrictionCriteriaDAO(Class<PolRestrictionCriteria> businessClass) {
		super(businessClass);
	}

	public PolRestrictionCriteriaDAO() {
		super(PolRestrictionCriteria.class);
	}


}
