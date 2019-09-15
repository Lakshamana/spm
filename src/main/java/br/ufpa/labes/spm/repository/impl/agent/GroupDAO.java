package br.ufpa.labes.spm.repository.impl.agent;


import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.agent.IGroupDAO;
import br.ufpa.labes.spm.domain.WorkGroup;

public class GroupDAO  extends BaseDAO<WorkGroup, String> implements IGroupDAO{

	protected GroupDAO(Class<WorkGroup> businessClass) {
		super(businessClass);
	}

	public GroupDAO() {
		super(WorkGroup.class);
	}

}
