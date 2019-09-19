package br.ufpa.labes.spm.repository.impl.resources;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.resources.IShareableDAO;
import br.ufpa.labes.spm.domain.Shareable;

public class ShareableDAO extends BaseDAO<Shareable, Long> implements IShareableDAO {

  protected ShareableDAO(Class<Shareable> businessClass) {
    super(businessClass);
  }

  public ShareableDAO() {
    super(Shareable.class);
  }
}
