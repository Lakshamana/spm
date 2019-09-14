package br.ufpa.labes.spm.repository.impl.log;


import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.log.ILogDAO;
import br.ufpa.labes.spm.domain.Log;

public class LogDAO extends BaseDAO<Log, Integer> implements ILogDAO{

	protected LogDAO(Class<Log> businessClass) {
		super(businessClass);
	}

	public LogDAO() {
		super(Log.class);
	}


}
