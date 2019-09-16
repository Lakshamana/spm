package br.ufpa.labes.spm.repository.impl.plainActivities;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.plainActivities.IArtifactParamDAO;
import br.ufpa.labes.spm.domain.ArtifactParam;

public class ArtifactParamDAO extends BaseDAO<ArtifactParam, Integer> implements IArtifactParamDAO {

  protected ArtifactParamDAO(Class<ArtifactParam> businessClass) {
    super(businessClass);
  }

  public ArtifactParamDAO() {
    super(ArtifactParam.class);
  }
}
