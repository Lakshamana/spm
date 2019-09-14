package br.ufpa.labes.spm.repository.impl.processKnowledge;

import javax.ejb.Stateless;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.processKnowledge.IResourceMetricDAO;
import br.ufpa.labes.spm.domain.ResourceMetric;

@Stateless
public class ResourceMetricDAO extends BaseDAO<ResourceMetric, Integer> implements IResourceMetricDAO{

	protected ResourceMetricDAO(Class<ResourceMetric> businessClass) {
		super(businessClass);
	}

	public ResourceMetricDAO() {
		super(ResourceMetric.class);
	}


}
