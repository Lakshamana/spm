package br.ufpa.labes.spm.repository.impl.connections;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.connections.IBranchConCondToActivityDAO;
import br.ufpa.labes.spm.domain.BranchConCondToActivity;

public class BranchConCondToActivityDAO extends BaseDAO<BranchConCondToActivity, Integer>
    implements IBranchConCondToActivityDAO {

  protected BranchConCondToActivityDAO(Class<BranchConCondToActivity> businessClass) {
    super(businessClass);
  }

  public BranchConCondToActivityDAO() {
    super(BranchConCondToActivity.class);
  }
}
