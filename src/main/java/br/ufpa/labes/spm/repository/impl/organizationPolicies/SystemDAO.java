package br.ufpa.labes.spm.repository.impl.organizationPolicies;

import javax.ejb.Stateless;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.organizationPolicies.ISystemDAO;
import br.ufpa.labes.spm.domain.System;

@Stateless
public class SystemDAO extends BaseDAO<System, String> implements ISystemDAO{

	protected SystemDAO(Class<System> businessClass) {
		super(businessClass);
	}

	public SystemDAO() {
		super(System.class);
	}


}
