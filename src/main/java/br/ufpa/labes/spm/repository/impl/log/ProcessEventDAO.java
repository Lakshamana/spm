package br.ufpa.labes.spm.repository.impl.log;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.log.IProcessEventDAO;
import br.ufpa.labes.spm.domain.ProcessEvent;

public class ProcessEventDAO extends BaseDAO<ProcessEvent, Integer> implements IProcessEventDAO {

  protected ProcessEventDAO(Class<ProcessEvent> businessClass) {
    super(businessClass);
  }

  public ProcessEventDAO() {
    super(ProcessEvent.class);
  }
}
