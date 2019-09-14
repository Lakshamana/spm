package br.ufpa.labes.spm.repository.impl.agent;

import javax.ejb.Stateless;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.agent.IAbilityDAO;
import br.ufpa.labes.spm.domain.Ability;

@Stateless
public class AbilityDAO extends BaseDAO<Ability, String> implements IAbilityDAO {

	protected AbilityDAO(Class<Ability> businessClass) {
		super(businessClass);
	}

	public AbilityDAO() {
		super(Ability.class);
	}

}
