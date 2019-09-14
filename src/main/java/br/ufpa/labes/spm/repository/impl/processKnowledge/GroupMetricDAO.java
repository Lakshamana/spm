package br.ufpa.labes.spm.repository.impl.processKnowledge;


import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.processKnowledge.IGroupMetricDAO;
import br.ufpa.labes.spm.domain.GroupMetric;

public class GroupMetricDAO extends BaseDAO<GroupMetric, Integer> implements IGroupMetricDAO{

	protected GroupMetricDAO(Class<GroupMetric> businessClass) {
		super(businessClass);
	}

	public GroupMetricDAO() {
		super(GroupMetric.class);
	}


}
