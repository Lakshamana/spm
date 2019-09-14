package br.ufpa.labes.spm.repository.impl.log;

import javax.ejb.Stateless;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.log.IModelingActivityEventDAO;
import br.ufpa.labes.spm.domain.ModelingActivityEvent;

@Stateless
public class ModelingActivityEventDAO extends BaseDAO<ModelingActivityEvent, Integer> implements IModelingActivityEventDAO{

	protected ModelingActivityEventDAO(Class<ModelingActivityEvent> businessClass) {
		super(businessClass);
	}

	public ModelingActivityEventDAO() {
		super(ModelingActivityEvent.class);
	}


}
