package br.ufpa.labes.spm.repository.impl.processKnowledge;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.processKnowledge.IEstimationDAO;
import br.ufpa.labes.spm.domain.Estimation;

public class EstimationDAO extends BaseDAO<Estimation, Integer> implements IEstimationDAO {

  protected EstimationDAO(Class<Estimation> businessClass) {
    super(businessClass);
  }

  public EstimationDAO() {
    super(Estimation.class);
  }
}
