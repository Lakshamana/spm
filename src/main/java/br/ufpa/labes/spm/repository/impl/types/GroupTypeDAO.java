package br.ufpa.labes.spm.repository.impl.types;

import javax.ejb.Stateless;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.types.IGroupTypeDAO;
import br.ufpa.labes.spm.domain.GroupType;

@Stateless
public class GroupTypeDAO extends BaseDAO<GroupType, String> implements IGroupTypeDAO{

	protected GroupTypeDAO(Class<GroupType> businessClass) {
		super(businessClass);
	}

	public GroupTypeDAO() {
		super(GroupType.class);
	}


}
