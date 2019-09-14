package br.ufpa.labes.spm.repository.impl.plannerInfo;

import javax.ejb.Stateless;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.plannerInfo.IResourceInstantiationSuggestionDAO;
import br.ufpa.labes.spm.domain.ResourceInstantiationSuggestion;

@Stateless
public class ResourceInstantiationSuggestionDAO extends BaseDAO<ResourceInstantiationSuggestion, Integer> implements IResourceInstantiationSuggestionDAO{

	protected ResourceInstantiationSuggestionDAO(Class<ResourceInstantiationSuggestion> businessClass) {
		super(businessClass);
	}

	public ResourceInstantiationSuggestionDAO() {
		super(ResourceInstantiationSuggestion.class);
	}


}
