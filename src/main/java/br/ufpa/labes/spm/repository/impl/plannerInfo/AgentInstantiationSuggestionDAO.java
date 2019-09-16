package br.ufpa.labes.spm.repository.impl.plannerInfo;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.plannerInfo.IAgentInstantiationSuggestionDAO;
import br.ufpa.labes.spm.domain.AgentInstSug;

public class AgentInstantiationSuggestionDAO extends BaseDAO<AgentInstSug, Integer>
    implements IAgentInstantiationSuggestionDAO {

  protected AgentInstantiationSuggestionDAO(Class<AgentInstSug> businessClass) {
    super(businessClass);
  }

  public AgentInstantiationSuggestionDAO() {
    super(AgentInstSug.class);
  }
}
