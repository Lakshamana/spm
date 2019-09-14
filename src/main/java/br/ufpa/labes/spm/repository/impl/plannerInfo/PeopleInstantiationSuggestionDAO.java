package br.ufpa.labes.spm.repository.impl.plannerInfo;


import br.ufpa.labes.spm.repository.impl.BaseDAO;
 import br.ufpa.labes.spm.repository.interfaces.plannerInfo.IPeopleInstantiationSuggestionDAO;
import br.ufpa.labes.spm.domain.PeopleInstantiationSuggestion;

public class PeopleInstantiationSuggestionDAO extends BaseDAO<PeopleInstantiationSuggestion, Integer> implements IPeopleInstantiationSuggestionDAO{

	protected PeopleInstantiationSuggestionDAO(Class<PeopleInstantiationSuggestion> businessClass) {
		super(businessClass);
	}

	public PeopleInstantiationSuggestionDAO() {
		super(PeopleInstantiationSuggestion.class);
	}


}
