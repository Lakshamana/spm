package br.ufpa.labes.spm.repository.impl.types;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.types.IArtifactTypeDAO;
import br.ufpa.labes.spm.domain.ArtifactType;

public class ArtifactTypeDAO extends BaseDAO<ArtifactType, Long> implements IArtifactTypeDAO {

  protected ArtifactTypeDAO(Class<ArtifactType> businessClass) {
    super(businessClass);
  }

  public ArtifactTypeDAO() {
    super(ArtifactType.class);
  }
}
