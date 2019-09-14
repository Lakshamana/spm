package br.ufpa.labes.spm.repository.impl.processKnowledge;

import javax.ejb.Stateless;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.processKnowledge.IArtifactMetricDAO;
import br.ufpa.labes.spm.domain.ArtifactMetric;

@Stateless
public class ArtifactMetricDAO extends BaseDAO<ArtifactMetric, Integer> implements IArtifactMetricDAO{

	protected ArtifactMetricDAO(Class<ArtifactMetric> businessClass) {
		super(businessClass);
	}

	public ArtifactMetricDAO() {
		super(ArtifactMetric.class);
	}


}
