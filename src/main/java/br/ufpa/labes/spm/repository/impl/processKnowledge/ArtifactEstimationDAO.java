package br.ufpa.labes.spm.repository.impl.processKnowledge;

import javax.ejb.Stateless;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.processKnowledge.IArtifactEstimationDAO;
import br.ufpa.labes.spm.domain.ArtifactEstimation;

@Stateless
public class ArtifactEstimationDAO extends BaseDAO<ArtifactEstimation, Integer> implements IArtifactEstimationDAO{

	protected ArtifactEstimationDAO(Class<ArtifactEstimation> businessClass) {
		super(businessClass);
	}

	public ArtifactEstimationDAO() {
		super(ArtifactEstimation.class);
	}


}
