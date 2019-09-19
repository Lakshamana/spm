package br.ufpa.labes.spm.repository.impl.connections;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.connections.IBranchConCondDAO;
import br.ufpa.labes.spm.domain.BranchConCond;

public class BranchConCondDAO extends BaseDAO<BranchConCond, Long> implements IBranchConCondDAO {

  protected BranchConCondDAO(Class<BranchConCond> businessClass) {
    super(businessClass);
  }

  public BranchConCondDAO() {
    super(BranchConCond.class);
  }
}
