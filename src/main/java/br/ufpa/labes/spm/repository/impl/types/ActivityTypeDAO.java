package br.ufpa.labes.spm.repository.impl.types;

import javax.ejb.Stateless;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.types.IActivityTypeDAO;
import br.ufpa.labes.spm.domain.ActivityType;

@Stateless
public class ActivityTypeDAO extends BaseDAO<ActivityType, String> implements IActivityTypeDAO{

	protected ActivityTypeDAO(Class<ActivityType> businessClass) {
		super(businessClass);
	}

	public ActivityTypeDAO() {
		super(ActivityType.class);
	}


}
