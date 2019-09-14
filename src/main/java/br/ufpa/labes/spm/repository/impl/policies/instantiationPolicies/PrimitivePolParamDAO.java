package br.ufpa.labes.spm.repository.impl.policies.instantiationPolicies;

import javax.ejb.Stateless;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.policies.instantiationPolicies.IPrimitivePolParamDAO;
import br.ufpa.labes.spm.domain.PrimitivePolParam;

@Stateless
public class PrimitivePolParamDAO extends BaseDAO<PrimitivePolParam, Integer> implements IPrimitivePolParamDAO{

	protected PrimitivePolParamDAO(Class<PrimitivePolParam> businessClass) {
		super(businessClass);
	}

	public PrimitivePolParamDAO() {
		super(PrimitivePolParam.class);
	}


}
