package br.ufpa.labes.spm.repository.impl.agent;

import javax.ejb.Stateless;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.agent.IRoleNeedsAbilityDAO;
import br.ufpa.labes.spm.domain.RoleNeedsAbility;

@Stateless
public class RoleNeedsAbilityDAO extends BaseDAO<RoleNeedsAbility, Integer> implements IRoleNeedsAbilityDAO{

	protected RoleNeedsAbilityDAO(Class<RoleNeedsAbility> businessClass) {
		super(businessClass);
	}

	public RoleNeedsAbilityDAO() {
		super(RoleNeedsAbility.class);
	}

}
