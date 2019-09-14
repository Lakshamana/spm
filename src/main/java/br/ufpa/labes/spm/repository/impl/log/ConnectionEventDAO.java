package br.ufpa.labes.spm.repository.impl.log;

import javax.ejb.Stateless;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.log.IConnectionEventDAO;
import br.ufpa.labes.spm.domain.ConnectionEvent;

@Stateless
public class ConnectionEventDAO extends BaseDAO<ConnectionEvent, Integer> implements IConnectionEventDAO{

	protected ConnectionEventDAO(Class<ConnectionEvent> businessClass) {
		super(businessClass);
	}

	public ConnectionEventDAO() {
		super(ConnectionEvent.class);
	}


}
