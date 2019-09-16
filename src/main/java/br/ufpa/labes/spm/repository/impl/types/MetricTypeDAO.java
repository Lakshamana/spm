package br.ufpa.labes.spm.repository.impl.types;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.types.IMetricTypeDAO;
import br.ufpa.labes.spm.domain.MetricType;

public class MetricTypeDAO extends BaseDAO<MetricType, String> implements IMetricTypeDAO {

  protected MetricTypeDAO(Class<MetricType> businessClass) {
    super(businessClass);
  }

  public MetricTypeDAO() {
    super(MetricType.class);
  }
}
