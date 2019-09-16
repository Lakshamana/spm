package br.ufpa.labes.spm.repository.impl.plannerInfo;


import br.ufpa.labes.spm.repository.impl.BaseDAO;
 import br.ufpa.labes.spm.repository.interfaces.plannerInfo.IPeopleInstantiationSuggestionDAO;
import br.ufpa.labes.spm.domain.PeopleInstSug;

public class PeopleInstantiationSuggestionDAO extends BaseDAO<PeopleInstSug, Integer> implements IPeopleInstantiationSuggestionDAO{

	protected PeopleInstantiationSuggestionDAO(Class<PeopleInstSug> businessClass) {
		super(businessClass);
	}

	public PeopleInstantiationSuggestionDAO() {
		super(PeopleInstSug.class);
	}


}
