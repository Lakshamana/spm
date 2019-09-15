package br.ufpa.labes.spm.repository.impl.log;


import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.log.ILogDAO;
import br.ufpa.labes.spm.domain.SpmLog;

public class LogDAO extends BaseDAO<SpmLog, Integer> implements ILogDAO{

	protected LogDAO(Class<SpmLog> businessClass) {
		super(businessClass);
	}

	public LogDAO() {
		super(SpmLog.class);
	}


}
