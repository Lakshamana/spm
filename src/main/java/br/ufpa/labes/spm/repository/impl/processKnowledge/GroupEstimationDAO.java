package br.ufpa.labes.spm.repository.impl.processKnowledge;


import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.processKnowledge.IGroupEstimationDAO;
import br.ufpa.labes.spm.domain.GroupEstimation;

public class GroupEstimationDAO extends BaseDAO<GroupEstimation, Integer> implements IGroupEstimationDAO{

	protected GroupEstimationDAO(Class<GroupEstimation> businessClass) {
		super(businessClass);
	}

	public GroupEstimationDAO() {
		super(GroupEstimation.class);
	}


}
