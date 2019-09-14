package br.ufpa.labes.spm.repository.impl.policies.instantiationPolicies;

import javax.ejb.Stateless;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.policies.instantiationPolicies.IInstAgentDAO;
import br.ufpa.labes.spm.domain.InstAgent;

@Stateless
public class InstAgentDAO extends BaseDAO<InstAgent, String> implements IInstAgentDAO{

	protected InstAgentDAO(Class<InstAgent> businessClass) {
		super(businessClass);
	}

	public InstAgentDAO() {
		super(InstAgent.class);
	}


}
