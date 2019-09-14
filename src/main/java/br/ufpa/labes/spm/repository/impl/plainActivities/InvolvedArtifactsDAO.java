package br.ufpa.labes.spm.repository.impl.plainActivities;

import javax.ejb.Stateless;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.plainActivities.IInvolvedArtifactsDAO;
import br.ufpa.labes.spm.domain.InvolvedArtifacts;

@Stateless
public class InvolvedArtifactsDAO extends BaseDAO<InvolvedArtifacts, Integer> implements IInvolvedArtifactsDAO{

	protected InvolvedArtifactsDAO(Class<InvolvedArtifacts> businessClass) {
		super(businessClass);
	}

	public InvolvedArtifactsDAO() {
		super(InvolvedArtifacts.class);
	}

}
