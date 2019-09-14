package br.ufpa.labes.spm.repository.impl.processModels;

import javax.ejb.Stateless;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.processModels.IProcessModelDAO;
import br.ufpa.labes.spm.domain.ProcessModel;

@Stateless
public class ProcessModelDAO extends BaseDAO<ProcessModel, Integer> implements IProcessModelDAO{

	protected ProcessModelDAO(Class<ProcessModel> businessClass) {
		super(businessClass);
	}

	public ProcessModelDAO() {
		super(ProcessModel.class);
	}


}
