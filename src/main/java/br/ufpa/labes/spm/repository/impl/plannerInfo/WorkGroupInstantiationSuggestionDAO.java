package br.ufpa.labes.spm.repository.impl.plannerInfo;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.plannerInfo.IWorkGroupInstantiationSuggestionDAO;
import br.ufpa.labes.spm.domain.WorkGroupInstSug;

public class GroupInstantiationSuggestionDAO extends BaseDAO<WorkGroupInstSug, Integer>
    implements IWorkGroupInstantiationSuggestionDAO {

  protected WorkGroupInstantiationSuggestionDAO(Class<WorkGroupInstSug> businessClass) {
    super(businessClass);
  }

  public WorkGroupInstantiationSuggestionDAO() {
    super(WorkGroupInstSug.class);
  }
}
