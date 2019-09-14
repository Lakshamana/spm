package br.ufpa.labes.spm.repository.impl.types;

import javax.ejb.Stateless;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.types.IAbilityTypeDAO;
import br.ufpa.labes.spm.domain.AbilityType;

@Stateless
public class AbilityTypeDAO extends BaseDAO<AbilityType, String> implements IAbilityTypeDAO{

	protected AbilityTypeDAO(Class<AbilityType> businessClass) {
		super(businessClass);
	}

	public AbilityTypeDAO() {
		super(AbilityType.class);
	}


}
