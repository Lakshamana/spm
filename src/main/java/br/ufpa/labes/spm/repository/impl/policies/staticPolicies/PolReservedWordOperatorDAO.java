package br.ufpa.labes.spm.repository.impl.policies.staticPolicies;

import javax.ejb.Stateless;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.policies.staticPolicies.IPolReservedWordOperatorDAO;
import br.ufpa.labes.spm.domain.PolReservedWordOperator;

@Stateless
public class PolReservedWordOperatorDAO extends BaseDAO<PolReservedWordOperator, Integer> implements IPolReservedWordOperatorDAO{

	protected PolReservedWordOperatorDAO(Class<PolReservedWordOperator> businessClass) {
		super(businessClass);
	}

	public PolReservedWordOperatorDAO() {
		super(PolReservedWordOperator.class);
	}

}
