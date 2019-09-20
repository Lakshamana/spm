package br.ufpa.labes.spm.repository.impl.activities;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.activities.IPlainDAO;
import br.ufpa.labes.spm.domain.Plain;

public class PlainDAO extends BaseDAO<Plain, String> implements IPlainDAO {

  protected PlainDAO(Class<Plain> businessClass) {
    super(businessClass);
  }

  public PlainDAO() {
    super(Plain.class);
  }
}
