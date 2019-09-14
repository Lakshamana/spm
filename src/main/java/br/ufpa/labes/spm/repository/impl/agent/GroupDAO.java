package br.ufpa.labes.spm.repository.impl.agent;

import javax.ejb.Stateless;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.agent.IGroupDAO;
import br.ufpa.labes.spm.domain.Group;

@Stateless
public class GroupDAO  extends BaseDAO<Group, String> implements IGroupDAO{

	protected GroupDAO(Class<Group> businessClass) {
		super(businessClass);
	}

	public GroupDAO() {
		super(Group.class);
	}

}
