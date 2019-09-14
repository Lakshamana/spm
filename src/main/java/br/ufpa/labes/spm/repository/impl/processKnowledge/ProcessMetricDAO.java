package br.ufpa.labes.spm.repository.impl.processKnowledge;


import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.processKnowledge.IProcessMetricDAO;
import br.ufpa.labes.spm.domain.ProcessMetric;

public class ProcessMetricDAO extends BaseDAO<ProcessMetric, Integer> implements IProcessMetricDAO{

	protected ProcessMetricDAO(Class<ProcessMetric> businessClass) {
		super(businessClass);
	}

	public ProcessMetricDAO() {
		super(ProcessMetric.class);
	}


}
