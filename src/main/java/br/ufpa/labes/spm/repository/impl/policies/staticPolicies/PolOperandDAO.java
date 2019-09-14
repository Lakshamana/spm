package br.ufpa.labes.spm.repository.impl.policies.staticPolicies;

import javax.ejb.Stateless;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.policies.staticPolicies.IPolOperandDAO;
import br.ufpa.labes.spm.domain.PolOperand;

@Stateless
public class PolOperandDAO extends BaseDAO<PolOperand, Integer> implements IPolOperandDAO{

	protected PolOperandDAO(Class<PolOperand> businessClass) {
		super(businessClass);
	}

	public PolOperandDAO() {
		super(PolOperand.class);
	}

}
