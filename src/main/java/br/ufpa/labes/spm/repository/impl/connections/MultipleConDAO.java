package br.ufpa.labes.spm.repository.impl.connections;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.connections.IMultipleConDAO;
import br.ufpa.labes.spm.domain.MultipleCon;

public class MultipleConDAO extends BaseDAO<MultipleCon, Long> implements IMultipleConDAO {

  protected MultipleConDAO(Class<MultipleCon> businessClass) {
    super(businessClass);
  }

  public MultipleConDAO() {
    super(MultipleCon.class);
  }
}
