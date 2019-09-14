package br.ufpa.labes.spm.repository.impl.policies.staticPolicies;


import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.policies.staticPolicies.IPolMethodOperatorDAO;
import br.ufpa.labes.spm.domain.PolMethodOperator;

public class PolMethodOperatorDAO extends BaseDAO<PolMethodOperator, String> implements IPolMethodOperatorDAO{

	protected PolMethodOperatorDAO(Class<PolMethodOperator> businessClass) {
		super(businessClass);
	}

	public PolMethodOperatorDAO() {
		super(PolMethodOperator.class);
	}


}
