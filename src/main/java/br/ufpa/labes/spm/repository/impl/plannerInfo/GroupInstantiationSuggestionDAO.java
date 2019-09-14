package br.ufpa.labes.spm.repository.impl.plannerInfo;


import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.plannerInfo.IGroupInstantiationSuggestionDAO;
import br.ufpa.labes.spm.domain.GroupInstantiationSuggestion;

public class GroupInstantiationSuggestionDAO extends BaseDAO<GroupInstantiationSuggestion, Integer> implements IGroupInstantiationSuggestionDAO{

	protected GroupInstantiationSuggestionDAO(Class<GroupInstantiationSuggestion> businessClass) {
		super(businessClass);
	}

	public GroupInstantiationSuggestionDAO() {
		super(GroupInstantiationSuggestion.class);
	}


}
