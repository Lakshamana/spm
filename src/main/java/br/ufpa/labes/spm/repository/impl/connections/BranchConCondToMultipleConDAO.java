package br.ufpa.labes.spm.repository.impl.connections;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.connections.IBranchConCondToMultipleConDAO;
import br.ufpa.labes.spm.domain.BranchConCondToMultipleCon;

public class BranchConCondToMultipleConDAO extends BaseDAO<BranchConCondToMultipleCon, Integer>
    implements IBranchConCondToMultipleConDAO {

  protected BranchConCondToMultipleConDAO(Class<BranchConCondToMultipleCon> businessClass) {
    super(businessClass);
  }

  public BranchConCondToMultipleConDAO() {
    super(BranchConCondToMultipleCon.class);
  }
}
