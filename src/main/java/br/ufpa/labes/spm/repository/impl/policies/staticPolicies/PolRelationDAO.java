package br.ufpa.labes.spm.repository.impl.policies.staticPolicies;


import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.policies.staticPolicies.IPolRelationDAO;
import br.ufpa.labes.spm.domain.PolRelation;

public class PolRelationDAO extends BaseDAO<PolRelation, Integer> implements IPolRelationDAO{

	protected PolRelationDAO(Class<PolRelation> businessClass) {
		super(businessClass);
	}

	public PolRelationDAO() {
		super(PolRelation.class);
	}

}
