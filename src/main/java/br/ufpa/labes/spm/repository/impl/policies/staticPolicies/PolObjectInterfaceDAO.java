package br.ufpa.labes.spm.repository.impl.policies.staticPolicies;


import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.policies.staticPolicies.IPolObjectInterfaceDAO;
import br.ufpa.labes.spm.domain.PolObjectInterface;

public class PolObjectInterfaceDAO extends BaseDAO<PolObjectInterface, Integer> implements IPolObjectInterfaceDAO{

	protected PolObjectInterfaceDAO(Class<PolObjectInterface> businessClass) {
		super(businessClass);
	}

	public PolObjectInterfaceDAO() {
		super(PolObjectInterface.class);
	}

}
