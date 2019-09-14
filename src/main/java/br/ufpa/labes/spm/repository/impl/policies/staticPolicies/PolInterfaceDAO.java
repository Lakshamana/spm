package br.ufpa.labes.spm.repository.impl.policies.staticPolicies;


import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.policies.staticPolicies.IPolInterfaceDAO;
import br.ufpa.labes.spm.domain.PolInterface;

public class PolInterfaceDAO extends BaseDAO<PolInterface, Integer> implements IPolInterfaceDAO{

	protected PolInterfaceDAO(Class<PolInterface> businessClass) {
		super(businessClass);
	}

	public PolInterfaceDAO() {
		super(PolInterface.class);
	}


}
