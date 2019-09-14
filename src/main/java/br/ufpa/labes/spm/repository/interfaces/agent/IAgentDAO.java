package br.ufpa.labes.spm.repository.interfaces.agent;

import javax.ejb.Local;

import br.ufpa.labes.spm.repository.interfaces.IBaseDAO;
import org.qrconsult.spm.dtos.formAgent.AgentDTO;
import br.ufpa.labes.spm.domain.Agent;

@Local
public interface IAgentDAO extends IBaseDAO<Agent, Integer>{

	public AgentDTO login(String login, String senha);

}
