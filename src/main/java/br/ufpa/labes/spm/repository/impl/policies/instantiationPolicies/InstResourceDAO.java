package br.ufpa.labes.spm.repository.impl.policies.instantiationPolicies;

import javax.ejb.Stateless;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.policies.instantiationPolicies.IInstResourceDAO;
import br.ufpa.labes.spm.domain.InstResource;

@Stateless
public class InstResourceDAO extends BaseDAO<InstResource, String> implements IInstResourceDAO{

	protected InstResourceDAO(Class<InstResource> businessClass) {
		super(businessClass);
	}

	public InstResourceDAO() {
		super(InstResource.class);
	}


}
