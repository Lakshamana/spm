package br.ufpa.labes.spm.repository.impl.processKnowledge;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.processKnowledge.IOrganizationMetricDAO;
import br.ufpa.labes.spm.domain.OrganizationMetric;

public class OrganizationMetricDAO extends BaseDAO<OrganizationMetric, Integer>
    implements IOrganizationMetricDAO {

  protected OrganizationMetricDAO(Class<OrganizationMetric> businessClass) {
    super(businessClass);
  }

  public OrganizationMetricDAO() {
    super(OrganizationMetric.class);
  }
}
