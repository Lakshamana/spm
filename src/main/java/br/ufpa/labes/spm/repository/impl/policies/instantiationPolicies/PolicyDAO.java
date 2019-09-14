package br.ufpa.labes.spm.repository.impl.policies.instantiationPolicies;

import javax.ejb.Stateless;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.policies.instantiationPolicies.IPolicyDAO;
import br.ufpa.labes.spm.domain.Policy;

@Stateless
public class PolicyDAO extends BaseDAO<Policy, String> implements IPolicyDAO{

	protected PolicyDAO(Class<Policy> businessClass) {
		super(businessClass);
	}

	public PolicyDAO() {
		super(Policy.class);
	}


}
