package br.ufpa.labes.spm.repository.interfaces.plannerInfo;


import javax.ejb.Local;

import br.ufpa.labes.spm.repository.interfaces.IBaseDAO;
import br.ufpa.labes.spm.domain.AgentInstantiationSuggestion;

@Local
public interface IAgentInstantiationSuggestionDAO  extends IBaseDAO<AgentInstantiationSuggestion, Integer>{

}
