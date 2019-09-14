package br.ufpa.labes.spm.repository.impl.agent;

import javax.ejb.Stateless;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.agent.IAgentAffinityAgentDAO;
import br.ufpa.labes.spm.domain.AgentAffinityAgent;

@Stateless
public class AgentAffinityAgentDAO extends BaseDAO<AgentAffinityAgent, Integer> implements IAgentAffinityAgentDAO{

	protected AgentAffinityAgentDAO(Class<AgentAffinityAgent> businessClass) {
		super(businessClass);
	}

	public AgentAffinityAgentDAO() {
		super(AgentAffinityAgent.class);
	}

}
