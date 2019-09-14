package br.ufpa.labes.spm.repository.impl.policies.instantiationPolicies;

import javax.ejb.Stateless;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.policies.instantiationPolicies.IRelationalOperatorPolParamDAO;
import br.ufpa.labes.spm.domain.RelationalOperatorPolParam;

@Stateless
public class RelationalOperatorPolParamDAO extends BaseDAO<RelationalOperatorPolParam, Integer> implements IRelationalOperatorPolParamDAO{

	protected RelationalOperatorPolParamDAO(Class<RelationalOperatorPolParam> businessClass) {
		super(businessClass);
	}

	public RelationalOperatorPolParamDAO() {
		super(RelationalOperatorPolParam.class);
	}


}
