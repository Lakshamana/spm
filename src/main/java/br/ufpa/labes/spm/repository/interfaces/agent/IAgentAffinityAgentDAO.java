package br.ufpa.labes.spm.repository.interfaces.agent;

import javax.ejb.Local;

import br.ufpa.labes.spm.repository.interfaces.IBaseDAO;
import br.ufpa.labes.spm.domain.AgentAffinityAgent;

@Local
public interface IAgentAffinityAgentDAO extends IBaseDAO<AgentAffinityAgent, Integer>{

}
