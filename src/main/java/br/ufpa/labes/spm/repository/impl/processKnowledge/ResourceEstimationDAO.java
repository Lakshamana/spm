package br.ufpa.labes.spm.repository.impl.processKnowledge;

import javax.ejb.Stateless;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.processKnowledge.IResourceEstimationDAO;
import br.ufpa.labes.spm.domain.ResourceEstimation;

@Stateless
public class ResourceEstimationDAO extends BaseDAO<ResourceEstimation, Integer> implements IResourceEstimationDAO{

	protected ResourceEstimationDAO(Class<ResourceEstimation> businessClass) {
		super(businessClass);
	}

	public ResourceEstimationDAO() {
		super(ResourceEstimation.class);
	}


}
