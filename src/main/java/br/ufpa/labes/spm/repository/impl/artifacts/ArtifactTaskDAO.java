package br.ufpa.labes.spm.repository.impl.artifacts;

import javax.ejb.Stateless;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.artifacts.IArtifactTaskDAO;
import br.ufpa.labes.spm.domain.ArtifactTask;

@Stateless
public class ArtifactTaskDAO extends BaseDAO<ArtifactTask, Integer> implements IArtifactTaskDAO{

	protected ArtifactTaskDAO(Class<ArtifactTask> businessClass) {
		super(businessClass);
	}

	public ArtifactTaskDAO() {
		super(ArtifactTask.class);
	}

}
