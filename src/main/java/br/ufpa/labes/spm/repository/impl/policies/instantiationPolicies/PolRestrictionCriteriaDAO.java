package br.ufpa.labes.spm.repository.impl.policies.instantiationPolicies;

import javax.ejb.Stateless;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.policies.instantiationPolicies.IPolRestrictionCriteriaDAO;
import br.ufpa.labes.spm.domain.PolRestrictionCriteria;

@Stateless
public class PolRestrictionCriteriaDAO extends BaseDAO<PolRestrictionCriteria, Integer> implements IPolRestrictionCriteriaDAO{

	protected PolRestrictionCriteriaDAO(Class<PolRestrictionCriteria> businessClass) {
		super(businessClass);
	}

	public PolRestrictionCriteriaDAO() {
		super(PolRestrictionCriteria.class);
	}


}
