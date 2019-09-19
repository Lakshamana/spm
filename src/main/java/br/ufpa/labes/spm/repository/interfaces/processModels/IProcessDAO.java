package br.ufpa.labes.spm.repository.interfaces.processModels;

import br.ufpa.labes.spm.repository.interfaces.IBaseDAO;
import br.ufpa.labes.spm.domain.Process;
import br.ufpa.labes.spm.service.util.SimpleActivityQueryResult;

public interface IProcessDAO extends IBaseDAO<Process, Long> {

  public SimpleActivityQueryResult[] getAllNormalActivitiesFromProcess(String processIdent);

  public SimpleActivityQueryResult[] getAllActivitiesFromProcess(String processIdent);
}
