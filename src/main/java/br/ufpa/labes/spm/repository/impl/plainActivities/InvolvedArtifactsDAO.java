package br.ufpa.labes.spm.repository.impl.plainActivities;


import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.plainActivities.IInvolvedArtifactsDAO;
import br.ufpa.labes.spm.domain.InvolvedArtifact;

public class InvolvedArtifactsDAO extends BaseDAO<InvolvedArtifact, Integer> implements IInvolvedArtifactsDAO{

	protected InvolvedArtifactsDAO(Class<InvolvedArtifact> businessClass) {
		super(businessClass);
	}

	public InvolvedArtifactsDAO() {
		super(InvolvedArtifact.class);
	}

}
