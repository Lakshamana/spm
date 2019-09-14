package br.ufpa.labes.spm.repository.impl.types;

import javax.ejb.Stateless;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.types.IPolicyTypeDAO;
import br.ufpa.labes.spm.domain.PolicyType;

@Stateless
public class PolicyTypeDAO extends BaseDAO<PolicyType, String> implements IPolicyTypeDAO{

	protected PolicyTypeDAO(Class<PolicyType> businessClass) {
		super(businessClass);
	}

	public PolicyTypeDAO() {
		super(PolicyType.class);
	}

}
