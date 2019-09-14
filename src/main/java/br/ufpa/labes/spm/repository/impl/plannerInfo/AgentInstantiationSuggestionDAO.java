package br.ufpa.labes.spm.repository.impl.plannerInfo;


import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.plannerInfo.IAgentInstantiationSuggestionDAO;
import br.ufpa.labes.spm.domain.AgentInstantiationSuggestion;

public class AgentInstantiationSuggestionDAO  extends BaseDAO<AgentInstantiationSuggestion, Integer> implements IAgentInstantiationSuggestionDAO{

	protected AgentInstantiationSuggestionDAO(Class<AgentInstantiationSuggestion> businessClass) {
		super(businessClass);
	}

	public AgentInstantiationSuggestionDAO() {
		super(AgentInstantiationSuggestion.class);
	}


}
