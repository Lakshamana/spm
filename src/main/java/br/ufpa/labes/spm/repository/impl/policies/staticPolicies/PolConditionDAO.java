package br.ufpa.labes.spm.repository.impl.policies.staticPolicies;

import javax.ejb.Stateless;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.policies.staticPolicies.IPolConditionDAO;
import br.ufpa.labes.spm.domain.PolCondition;

@Stateless
public class PolConditionDAO extends BaseDAO<PolCondition, Integer> implements IPolConditionDAO{

	protected PolConditionDAO(Class<PolCondition> businessClass) {
		super(businessClass);
	}

	public PolConditionDAO() {
		super(PolCondition.class);
	}


}
