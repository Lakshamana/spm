package br.ufpa.labes.spm.repository.impl.plainActivities;

import javax.ejb.Stateless;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.plainActivities.IReqAgentRequiresAbilityDAO;
import br.ufpa.labes.spm.domain.ReqAgentRequiresAbility;

@Stateless
public class ReqAgentRequiresAbilityDAO extends BaseDAO<ReqAgentRequiresAbility, Integer> implements IReqAgentRequiresAbilityDAO{

	protected ReqAgentRequiresAbilityDAO(Class<ReqAgentRequiresAbility> businessClass) {
		super(businessClass);
	}

	public ReqAgentRequiresAbilityDAO() {
		super(ReqAgentRequiresAbility.class);
	}


}
