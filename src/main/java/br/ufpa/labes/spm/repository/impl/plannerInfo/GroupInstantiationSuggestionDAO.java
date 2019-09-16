package br.ufpa.labes.spm.repository.impl.plannerInfo;


import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.plannerInfo.IGroupInstantiationSuggestionDAO;
import br.ufpa.labes.spm.domain.WorkGroupInstSug;

public class GroupInstantiationSuggestionDAO extends BaseDAO<WorkGroupInstSug, Integer> implements IGroupInstantiationSuggestionDAO{

	protected GroupInstantiationSuggestionDAO(Class<WorkGroupInstSug> businessClass) {
		super(businessClass);
	}

	public GroupInstantiationSuggestionDAO() {
		super(WorkGroupInstSug.class);
	}


}
