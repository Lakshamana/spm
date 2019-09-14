package br.ufpa.labes.spm.repository.interfaces.agent;

import javax.ejb.Local;

import br.ufpa.labes.spm.repository.interfaces.IBaseDAO;
import br.ufpa.labes.spm.domain.AgentHasAbility;

@Local
public interface IAgentHasAbilityDAO extends IBaseDAO<AgentHasAbility, Integer>{

}
