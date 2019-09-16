package br.ufpa.labes.spm.repository.impl.processKnowledge;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.processKnowledge.IWorkGroupEstimationDAO;
import br.ufpa.labes.spm.domain.WorkGroupEstimation;

public class WorkGroupEstimationDAO extends BaseDAO<WorkGroupEstimation, Integer>
    implements IWorkGroupEstimationDAO {

  protected WorkGroupEstimationDAO(Class<WorkGroupEstimation> businessClass) {
    super(businessClass);
  }

  public WorkGroupEstimationDAO() {
    super(WorkGroupEstimation.class);
  }
}
