package br.ufpa.labes.spm.repository.impl.organizationPolicies;


import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.organizationPolicies.ISystemDAO;
import br.ufpa.labes.spm.domain.System;

public class SystemDAO extends BaseDAO<System, String> implements ISystemDAO{

	protected SystemDAO(Class<System> businessClass) {
		super(businessClass);
	}

	public SystemDAO() {
		super(System.class);
	}


}
