package br.ufpa.labes.spm.repository.impl.connections;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.connections.ISimpleConDAO;
import br.ufpa.labes.spm.domain.SimpleCon;

public class SimpleConDAO extends BaseDAO<SimpleCon, Long> implements ISimpleConDAO {

  protected SimpleConDAO(Class<SimpleCon> businessClass) {
    super(businessClass);
  }

  public SimpleConDAO() {
    super(SimpleCon.class);
  }
}
