package br.ufpa.labes.spm.repository.impl.plannerInfo;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.plannerInfo.IAgentInstSuggestionToAgentDAO;
import br.ufpa.labes.spm.domain.AgentInstSuggestionToAgent;

public class AgentInstSuggestionToAgentDAO extends BaseDAO<AgentInstSuggestionToAgent, Integer>
    implements IAgentInstSuggestionToAgentDAO {

  protected AgentInstSuggestionToAgentDAO(Class<AgentInstSuggestionToAgent> businessClass) {
    super(businessClass);
  }

  public AgentInstSuggestionToAgentDAO() {
    super(AgentInstSuggestionToAgent.class);
  }
}
