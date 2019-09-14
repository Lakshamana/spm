package br.ufpa.labes.spm.repository.impl.policies.staticPolicies;


import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.policies.staticPolicies.IPolConnectionDAO;
import br.ufpa.labes.spm.domain.PolConnection;

public class PolConnectionDAO extends BaseDAO<PolConnection, Integer> implements IPolConnectionDAO{

	protected PolConnectionDAO(Class<PolConnection> businessClass) {
		super(businessClass);
	}

	public PolConnectionDAO() {
		super(PolConnection.class);
	}


}
