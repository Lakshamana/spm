package br.ufpa.labes.spm.repository.impl.organizationPolicies;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.organizationPolicies.ISystemDAO;
import br.ufpa.labes.spm.domain.DevelopingSystem;

public class SystemDAO extends BaseDAO<DevelopingSystem, String> implements ISystemDAO {

  protected SystemDAO(Class<DevelopingSystem> businessClass) {
    super(businessClass);
  }

  public SystemDAO() {
    super(DevelopingSystem.class);
  }
}
