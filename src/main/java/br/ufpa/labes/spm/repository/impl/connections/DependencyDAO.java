package br.ufpa.labes.spm.repository.impl.connections;

import javax.ejb.Stateless;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.connections.IDependencyDAO;
import br.ufpa.labes.spm.domain.Dependency;

@Stateless
public class DependencyDAO extends BaseDAO<Dependency, Integer> implements IDependencyDAO{

	protected DependencyDAO(Class<Dependency> businessClass) {
		super(businessClass);
	}

	public DependencyDAO() {
		super(Dependency.class);
	}


}
