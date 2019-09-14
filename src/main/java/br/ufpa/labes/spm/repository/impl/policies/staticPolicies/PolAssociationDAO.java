package br.ufpa.labes.spm.repository.impl.policies.staticPolicies;

import javax.ejb.Stateless;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.policies.staticPolicies.IPolAssociationDAO;
import br.ufpa.labes.spm.domain.PolAssociation;

@Stateless
public class PolAssociationDAO extends BaseDAO<PolAssociation, Integer> implements IPolAssociationDAO{

	protected PolAssociationDAO(Class<PolAssociation> businessClass) {
		super(businessClass);
	}

	public PolAssociationDAO() {
		super(PolAssociation.class);
	}


}
