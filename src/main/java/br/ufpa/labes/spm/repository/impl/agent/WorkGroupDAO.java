package br.ufpa.labes.spm.repository.impl.agent;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.agent.IWorkGroupDAO;
import br.ufpa.labes.spm.domain.WorkGroup;

public class WorkGroupDAO extends BaseDAO<WorkGroup, String> implements IWorkGroupDAO {

  protected WorkGroupDAO(Class<WorkGroup> businessClass) {
    super(businessClass);
  }

  public WorkGroupDAO() {
    super(WorkGroup.class);
  }
}
