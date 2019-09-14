package br.ufpa.labes.spm.repository.impl.policies.instantiationPolicies;


import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.policies.instantiationPolicies.IAbilityPolParamDAO;
import br.ufpa.labes.spm.domain.AbilityPolParam;

public class AbilityPolParamDAO extends BaseDAO<AbilityPolParam, Integer> implements IAbilityPolParamDAO{

	protected AbilityPolParamDAO(Class<AbilityPolParam> businessClass) {
		super(businessClass);
	}

	public AbilityPolParamDAO() {
		super(AbilityPolParam.class);
	}


}
