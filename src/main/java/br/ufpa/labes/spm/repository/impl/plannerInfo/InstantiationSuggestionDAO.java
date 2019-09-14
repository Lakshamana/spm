package br.ufpa.labes.spm.repository.impl.plannerInfo;


import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.plannerInfo.IInstantiationSuggestionDAO;
import br.ufpa.labes.spm.domain.InstantiationSuggestion;

public class InstantiationSuggestionDAO extends BaseDAO<InstantiationSuggestion, Integer> implements IInstantiationSuggestionDAO{

	protected InstantiationSuggestionDAO(Class<InstantiationSuggestion> businessClass) {
		super(businessClass);
	}

	public InstantiationSuggestionDAO() {
		super(InstantiationSuggestion.class);
	}


}
