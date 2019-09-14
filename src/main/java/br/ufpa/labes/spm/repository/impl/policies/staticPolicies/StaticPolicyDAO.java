package br.ufpa.labes.spm.repository.impl.policies.staticPolicies;


import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.policies.staticPolicies.IStaticPolicyDAO;
import br.ufpa.labes.spm.domain.StaticPolicy;

public class StaticPolicyDAO extends BaseDAO<StaticPolicy, String> implements IStaticPolicyDAO{

	protected StaticPolicyDAO(Class<StaticPolicy> businessClass) {
		super(businessClass);
	}

	public StaticPolicyDAO() {
		super(StaticPolicy.class);
	}

}
