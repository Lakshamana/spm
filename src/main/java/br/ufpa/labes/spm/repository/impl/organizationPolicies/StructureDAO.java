package br.ufpa.labes.spm.repository.impl.organizationPolicies;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.organizationPolicies.IStructureDAO;
import br.ufpa.labes.spm.domain.Structure;

public class StructureDAO extends BaseDAO<Structure, Long> implements IStructureDAO {

  protected StructureDAO(Class<Structure> businessClass) {
    super(businessClass);
  }

  public StructureDAO() {
    super(Structure.class);
  }
}
