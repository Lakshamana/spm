package br.ufpa.labes.spm.repository.impl.organizationPolicies;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.organizationPolicies.IDevelopingSystemDAO;
import br.ufpa.labes.spm.domain.DevelopingSystem;

public class DevelopingSystemDAO extends BaseDAO<DevelopingSystem, Long> implements IDevelopingSystemDAO {

  protected DevelopingSystemDAO(Class<DevelopingSystem> businessClass) {
    super(businessClass);
  }

  public DevelopingSystemDAO() {
    super(DevelopingSystem.class);
  }
}
