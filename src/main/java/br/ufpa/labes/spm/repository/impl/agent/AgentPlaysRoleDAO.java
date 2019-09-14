package br.ufpa.labes.spm.repository.impl.agent;


import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.agent.IAgentPlaysRoleDAO;
import br.ufpa.labes.spm.domain.AgentPlaysRole;

public class AgentPlaysRoleDAO extends BaseDAO<AgentPlaysRole, Integer> implements IAgentPlaysRoleDAO{

	protected AgentPlaysRoleDAO(Class<AgentPlaysRole> businessClass) {
		super(businessClass);
	}

	public AgentPlaysRoleDAO() {
		super(AgentPlaysRole.class);
	}

}
