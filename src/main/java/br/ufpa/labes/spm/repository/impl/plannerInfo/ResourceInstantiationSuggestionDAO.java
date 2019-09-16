package br.ufpa.labes.spm.repository.impl.plannerInfo;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.plannerInfo.IResourceInstantiationSuggestionDAO;
import br.ufpa.labes.spm.domain.ResourceInstSug;

public class ResourceInstantiationSuggestionDAO extends BaseDAO<ResourceInstSug, Integer>
    implements IResourceInstantiationSuggestionDAO {

  protected ResourceInstantiationSuggestionDAO(Class<ResourceInstSug> businessClass) {
    super(businessClass);
  }

  public ResourceInstantiationSuggestionDAO() {
    super(ResourceInstSug.class);
  }
}
