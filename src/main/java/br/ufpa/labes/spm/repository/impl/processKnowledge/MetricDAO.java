package br.ufpa.labes.spm.repository.impl.processKnowledge;


import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.processKnowledge.IMetricDAO;
import br.ufpa.labes.spm.domain.Metric;

public class MetricDAO extends BaseDAO<Metric, Integer> implements IMetricDAO{

	protected MetricDAO(Class<Metric> businessClass) {
		super(businessClass);
	}

	public MetricDAO() {
		super(Metric.class);
	}


}
