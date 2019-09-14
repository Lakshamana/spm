package br.ufpa.labes.spm.repository.impl.policies.instantiationPolicies;


import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.policies.instantiationPolicies.IMetricPolParamDAO;
import br.ufpa.labes.spm.domain.MetricPolParam;

public class MetricPolParamDAO extends BaseDAO<MetricPolParam, Integer> implements IMetricPolParamDAO{

	protected MetricPolParamDAO(Class<MetricPolParam> businessClass) {
		super(businessClass);
	}

	public MetricPolParamDAO() {
		super(MetricPolParam.class);
	}

}
