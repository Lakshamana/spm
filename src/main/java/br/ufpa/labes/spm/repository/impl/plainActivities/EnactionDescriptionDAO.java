package br.ufpa.labes.spm.repository.impl.plainActivities;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.plainActivities.IEnactionDescriptionDAO;
import br.ufpa.labes.spm.domain.EnactionDescription;

public class EnactionDescriptionDAO extends BaseDAO<EnactionDescription, Integer>
    implements IEnactionDescriptionDAO {

  protected EnactionDescriptionDAO(Class<EnactionDescription> businessClass) {
    super(businessClass);
  }

  public EnactionDescriptionDAO() {
    super(EnactionDescription.class);
  }
}
