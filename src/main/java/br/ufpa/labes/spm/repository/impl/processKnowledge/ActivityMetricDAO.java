package br.ufpa.labes.spm.repository.impl.processKnowledge;

import javax.ejb.Stateless;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.processKnowledge.IActivityMetricDAO;
import br.ufpa.labes.spm.domain.ActivityMetric;

@Stateless
public class ActivityMetricDAO extends BaseDAO<ActivityMetric, Integer> implements IActivityMetricDAO{

	protected ActivityMetricDAO(Class<ActivityMetric> businessClass) {
		super(businessClass);
	}

	public ActivityMetricDAO() {
		super(ActivityMetric.class);
	}


}
