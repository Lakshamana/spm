package br.ufpa.labes.spm.repository.impl.agent;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.agent.IAgentHasAbilityDAO;
import br.ufpa.labes.spm.domain.AgentHasAbility;

public class AgentHasAbilityDAO extends BaseDAO<AgentHasAbility, Integer>
    implements IAgentHasAbilityDAO {

  protected AgentHasAbilityDAO(Class<AgentHasAbility> businessClass) {
    super(businessClass);
  }

  public AgentHasAbilityDAO() {
    super(AgentHasAbility.class);
  }
}
