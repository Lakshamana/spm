package br.ufpa.labes.spm.repository.impl.types;


import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.types.IWorkGroupTypeDAO;
import br.ufpa.labes.spm.domain.WorkGroupType;

public class WorkGroupTypeDAO extends BaseDAO<WorkGroupType, String> implements IWorkGroupTypeDAO{

	protected WorkGroupTypeDAO(Class<WorkGroupType> businessClass) {
		super(businessClass);
	}

	public WorkGroupTypeDAO() {
		super(WorkGroupType.class);
	}


}
