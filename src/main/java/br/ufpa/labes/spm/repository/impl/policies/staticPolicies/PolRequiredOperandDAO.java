package br.ufpa.labes.spm.repository.impl.policies.staticPolicies;


import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.policies.staticPolicies.IPolRequiredOperandDAO;
import br.ufpa.labes.spm.domain.PolRequiredOperand;

public class PolRequiredOperandDAO extends BaseDAO<PolRequiredOperand, Integer> implements IPolRequiredOperandDAO{

	protected PolRequiredOperandDAO(Class<PolRequiredOperand> businessClass) {
		super(businessClass);
	}

	public PolRequiredOperandDAO() {
		super(PolRequiredOperand.class);
	}

}
