package br.ufpa.labes.spm.repository.impl.log;

import javax.ejb.Stateless;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.log.IProcessModelEventDAO;
import br.ufpa.labes.spm.domain.ProcessModelEvent;

@Stateless
public class ProcessModelEventDAO extends BaseDAO<ProcessModelEvent, Integer> implements IProcessModelEventDAO{

	protected ProcessModelEventDAO(Class<ProcessModelEvent> businessClass) {
		super(businessClass);
	}

	public ProcessModelEventDAO() {
		super(ProcessModelEvent.class);
	}


}
