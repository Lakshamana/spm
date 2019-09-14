package br.ufpa.labes.spm.repository.impl.policies.instantiationPolicies;

import javax.ejb.Stateless;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.policies.instantiationPolicies.IPolOrderCriteriaDAO;
import br.ufpa.labes.spm.domain.PolOrderCriteria;

@Stateless
public class PolOrderCriteriaDAO extends BaseDAO<PolOrderCriteria, Integer> implements IPolOrderCriteriaDAO{

	protected PolOrderCriteriaDAO(Class<PolOrderCriteria> businessClass) {
		super(businessClass);
	}

	public PolOrderCriteriaDAO() {
		super(PolOrderCriteria.class);
	}


}
