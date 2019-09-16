package br.ufpa.labes.spm.repository.impl.processKnowledge;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.processKnowledge.IOrganizationEstimationDAO;
import br.ufpa.labes.spm.domain.OrganizationEstimation;

public class OrganizationEstimationDAO extends BaseDAO<OrganizationEstimation, Integer>
    implements IOrganizationEstimationDAO {

  protected OrganizationEstimationDAO(Class<OrganizationEstimation> businessClass) {
    super(businessClass);
  }

  public OrganizationEstimationDAO() {
    super(OrganizationEstimation.class);
  }
}
