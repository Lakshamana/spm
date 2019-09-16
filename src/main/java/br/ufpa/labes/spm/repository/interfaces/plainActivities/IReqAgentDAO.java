package br.ufpa.labes.spm.repository.interfaces.plainActivities;

import br.ufpa.labes.spm.repository.interfaces.IBaseDAO;
import br.ufpa.labes.spm.domain.ReqAgent;

public interface IReqAgentDAO extends IBaseDAO<ReqAgent, Integer> {

  public ReqAgent findReqAgentFromProcessModel(
      String agentIdent, String roleIdent, String normalIdent);
}
