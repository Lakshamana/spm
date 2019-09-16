package br.ufpa.labes.spm.repository.impl.processKnowledge;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.processKnowledge.IWorkGroupMetricDAO;
import br.ufpa.labes.spm.domain.WorkGroupMetric;

public class WorkGroupMetricDAO extends BaseDAO<WorkGroupMetric, Integer> implements IWorkGroupMetricDAO {

	protected WorkGroupMetricDAO(Class<WorkGroupMetric> businessClass) {
		super(businessClass);
	}

	public WorkGroupMetricDAO() {
		super(WorkGroupMetric.class);
	}


}
