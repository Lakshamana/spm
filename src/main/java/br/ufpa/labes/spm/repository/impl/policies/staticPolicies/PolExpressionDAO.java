package br.ufpa.labes.spm.repository.impl.policies.staticPolicies;

import javax.ejb.Stateless;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.policies.staticPolicies.IPolExpressionDAO;
import br.ufpa.labes.spm.domain.PolExpression;

@Stateless
public class PolExpressionDAO extends BaseDAO<PolExpression, Integer> implements IPolExpressionDAO{

	protected PolExpressionDAO(Class<PolExpression> businessClass) {
		super(businessClass);
	}

	public PolExpressionDAO() {
		super(PolExpression.class);
	}


}
