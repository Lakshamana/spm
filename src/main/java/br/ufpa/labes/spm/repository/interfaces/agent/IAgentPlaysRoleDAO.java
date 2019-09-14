package br.ufpa.labes.spm.repository.interfaces.agent;

import javax.ejb.Local;

import br.ufpa.labes.spm.repository.interfaces.IBaseDAO;
import br.ufpa.labes.spm.domain.AgentPlaysRole;

@Local
public interface IAgentPlaysRoleDAO extends IBaseDAO<AgentPlaysRole, Integer>{

}
