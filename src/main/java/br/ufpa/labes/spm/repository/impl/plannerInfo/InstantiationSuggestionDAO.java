package br.ufpa.labes.spm.repository.impl.plannerInfo;

import javax.ejb.Stateless;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.plannerInfo.IInstantiationSuggestionDAO;
import br.ufpa.labes.spm.domain.InstantiationSuggestion;

@Stateless
public class InstantiationSuggestionDAO extends BaseDAO<InstantiationSuggestion, Integer> implements IInstantiationSuggestionDAO{

	protected InstantiationSuggestionDAO(Class<InstantiationSuggestion> businessClass) {
		super(businessClass);
	}

	public InstantiationSuggestionDAO() {
		super(InstantiationSuggestion.class);
	}


}
