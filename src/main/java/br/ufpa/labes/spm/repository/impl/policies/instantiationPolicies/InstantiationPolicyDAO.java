package br.ufpa.labes.spm.repository.impl.policies.instantiationPolicies;

import javax.ejb.Stateless;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.policies.instantiationPolicies.IInstantiationPolicyDAO;
import br.ufpa.labes.spm.domain.InstantiationPolicy;

@Stateless
public class InstantiationPolicyDAO extends BaseDAO<InstantiationPolicy, String> implements IInstantiationPolicyDAO{

	protected InstantiationPolicyDAO(Class<InstantiationPolicy> businessClass) {
		super(businessClass);
	}

	public InstantiationPolicyDAO() {
		super(InstantiationPolicy.class);
	}


}
