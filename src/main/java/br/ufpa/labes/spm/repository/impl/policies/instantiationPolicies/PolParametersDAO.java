package br.ufpa.labes.spm.repository.impl.policies.instantiationPolicies;


import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.policies.instantiationPolicies.IPolParametersDAO;
import br.ufpa.labes.spm.domain.PolParameters;

public class PolParametersDAO extends BaseDAO<PolParameters, Integer> implements IPolParametersDAO{

	protected PolParametersDAO(Class<PolParameters> businessClass) {
		super(businessClass);
	}

	public PolParametersDAO() {
		super(PolParameters.class);
	}


}
