package br.ufpa.labes.spm.repository.impl.types;

import javax.ejb.Stateless;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.types.IArtifactTypeDAO;
import br.ufpa.labes.spm.domain.ArtifactType;

@Stateless
public class ArtifactTypeDAO extends BaseDAO<ArtifactType, String> implements IArtifactTypeDAO{

	protected ArtifactTypeDAO(Class<ArtifactType> businessClass) {
		super(businessClass);
	}

	public ArtifactTypeDAO() {
		super(ArtifactType.class);
	}


}
