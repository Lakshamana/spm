package br.ufpa.labes.spm.repository.impl.agent;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.agent.IAgentAffinityAgentDAO;
import br.ufpa.labes.spm.domain.AgentAffinityAgent;

public class AgentAffinityAgentDAO extends BaseDAO<AgentAffinityAgent, Integer>
    implements IAgentAffinityAgentDAO {

  protected AgentAffinityAgentDAO(Class<AgentAffinityAgent> businessClass) {
    super(businessClass);
  }

  public AgentAffinityAgentDAO() {
    super(AgentAffinityAgent.class);
  }
}
