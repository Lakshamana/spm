package br.ufpa.labes.spm.repository.interfaces.plainActivities;


import javax.ejb.Local;

import br.ufpa.labes.spm.repository.interfaces.IBaseDAO;
import br.ufpa.labes.spm.domain.ReqAgent;

@Local
public interface IReqAgentDAO extends IBaseDAO<ReqAgent, Integer>{

	public ReqAgent findReqAgentFromProcessModel(String agentIdent, String roleIdent, String normalIdent);
}
