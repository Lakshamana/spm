package br.ufpa.labes.spm.repository.interfaces.plannerInfo;


import javax.ejb.Local;

import br.ufpa.labes.spm.repository.interfaces.IBaseDAO;
import br.ufpa.labes.spm.domain.AgentWorkingLoad;

@Local
public interface IAgentWorkingLoadDAO  extends IBaseDAO<AgentWorkingLoad, Integer>{

}
