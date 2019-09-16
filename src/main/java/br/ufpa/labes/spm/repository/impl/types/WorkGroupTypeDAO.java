package br.ufpa.labes.spm.repository.impl.types;


import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.types.IWorkGroupTypeDAO;
import br.ufpa.labes.spm.domain.WorkGroupType;

public class GroupTypeDAO extends BaseDAO<GroupType, String> implements IWorkGroupTypeDAO{

	protected GroupTypeDAO(Class<WorkGroupType> businessClass) {
		super(businessClass);
	}

	public WorkGroupTypeDAO() {
		super(GroupType.class);
	}


}
