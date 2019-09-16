package br.ufpa.labes.spm.repository.impl.log;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.log.IGlobalActivityEventDAO;
import br.ufpa.labes.spm.domain.GlobalActivityEvent;

public class GlobalActivityEventDAO extends BaseDAO<GlobalActivityEvent, Integer>
    implements IGlobalActivityEventDAO {

  protected GlobalActivityEventDAO(Class<GlobalActivityEvent> businessClass) {
    super(businessClass);
  }

  public GlobalActivityEventDAO() {
    super(GlobalActivityEvent.class);
  }
}
