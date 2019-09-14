package br.ufpa.labes.spm.repository.impl.policies.instantiationPolicies;

import javax.ejb.Stateless;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.policies.instantiationPolicies.ITypePolParamDAO;
import br.ufpa.labes.spm.domain.TypePolParam;

@Stateless
public class TypePolParamDAO extends BaseDAO<TypePolParam, Integer> implements ITypePolParamDAO{

	protected TypePolParamDAO(Class<TypePolParam> businessClass) {
		super(businessClass);
	}

	public TypePolParamDAO() {
		super(TypePolParam.class);
	}


}