package br.ufpa.labes.spm.repository.impl.policies.instantiationPolicies;

import javax.ejb.Stateless;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.policies.instantiationPolicies.IInstPeopleDAO;
import br.ufpa.labes.spm.domain.InstPeople;

@Stateless
public class InstPeopleDAO extends BaseDAO<InstPeople, String> implements IInstPeopleDAO{

	protected InstPeopleDAO(Class<InstPeople> businessClass) {
		super(businessClass);
	}

	public InstPeopleDAO() {
		super(InstPeople.class);
	}


}
