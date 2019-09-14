package br.ufpa.labes.spm.repository.impl.plainActivities;


import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.plainActivities.IAutomaticDAO;
import br.ufpa.labes.spm.domain.Automatic;

public class AutomaticDAO extends BaseDAO<Automatic, String> implements IAutomaticDAO{

	protected AutomaticDAO(Class<Automatic> businessClass) {
		super(businessClass);
	}

	public AutomaticDAO() {
		super(Automatic.class);
	}


}
