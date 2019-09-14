package br.ufpa.labes.spm.repository.impl.plannerInfo;

import javax.ejb.Stateless;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
 import br.ufpa.labes.spm.repository.interfaces.plannerInfo.IPeopleInstantiationSuggestionDAO;
import br.ufpa.labes.spm.domain.PeopleInstantiationSuggestion;

@Stateless
public class PeopleInstantiationSuggestionDAO extends BaseDAO<PeopleInstantiationSuggestion, Integer> implements IPeopleInstantiationSuggestionDAO{

	protected PeopleInstantiationSuggestionDAO(Class<PeopleInstantiationSuggestion> businessClass) {
		super(businessClass);
	}

	public PeopleInstantiationSuggestionDAO() {
		super(PeopleInstantiationSuggestion.class);
	}


}
