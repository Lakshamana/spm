package br.ufpa.labes.spm.repository.impl.policies.staticPolicies;

import javax.ejb.Stateless;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.policies.staticPolicies.IPolOperatorDAO;
import br.ufpa.labes.spm.domain.PolOperator;

@Stateless
public class PolOperatorDAO extends BaseDAO<PolOperator, Integer> implements IPolOperatorDAO{

	protected PolOperatorDAO(Class<PolOperator> businessClass) {
		super(businessClass);
	}

	public PolOperatorDAO() {
		super(PolOperator.class);
	}

}
