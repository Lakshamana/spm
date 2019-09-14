package br.ufpa.labes.spm.repository.impl.types;

import javax.ejb.Stateless;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.types.IConnectionTypeDAO;
import br.ufpa.labes.spm.domain.ConnectionType;

@Stateless
public class ConnectionTypeDAO extends BaseDAO<ConnectionType, String> implements IConnectionTypeDAO{

	protected ConnectionTypeDAO(Class<ConnectionType> businessClass) {
		super(businessClass);
	}

	public ConnectionTypeDAO() {
		super(ConnectionType.class);
	}

}
