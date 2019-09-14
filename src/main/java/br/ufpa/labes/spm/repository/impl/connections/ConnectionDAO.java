package br.ufpa.labes.spm.repository.impl.connections;

import javax.ejb.Stateless;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.connections.IConnectionDAO;
import br.ufpa.labes.spm.domain.Connection;

@Stateless
public class ConnectionDAO extends BaseDAO<Connection, String> implements IConnectionDAO {

	protected ConnectionDAO(Class<Connection> businessClass) {
		super(businessClass);
	}

	public ConnectionDAO() {
		super(Connection.class);
	}

	@Override
	public Connection save(Connection conn) {


		super.save(conn);
		String ident = conn.getIdent()+"."+conn.getOid();
		conn.setIdent(ident);
		this.update(conn);
		return conn;
	}

}