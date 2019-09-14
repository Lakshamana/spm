package br.ufpa.labes.spm.repository.impl.processKnowledge;


import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.processKnowledge.IMetricDefinitionDAO;
import br.ufpa.labes.spm.domain.MetricDefinition;

public class MetricDefinitionDAO extends BaseDAO<MetricDefinition, Integer> implements IMetricDefinitionDAO{

	protected MetricDefinitionDAO(Class<MetricDefinition> businessClass) {
		super(businessClass);
	}

	public MetricDefinitionDAO() {
		super(MetricDefinition.class);
	}


}
