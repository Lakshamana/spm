package br.ufpa.labes.spm.repository.interfaces.agent;

import br.ufpa.labes.spm.repository.interfaces.IBaseDAO;
import br.ufpa.labes.spm.service.dto.AgentDTO;
import br.ufpa.labes.spm.domain.Agent;

public interface IAgentDAO extends IBaseDAO<Agent, Integer> {

  public AgentDTO login(String login, String senha);
}
