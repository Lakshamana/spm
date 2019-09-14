package br.ufpa.labes.spm.repository.impl.connections;


import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.connections.IJoinDAO;
import br.ufpa.labes.spm.domain.Join;

public class JoinDAO extends BaseDAO<Join, String> implements IJoinDAO{

	protected JoinDAO(Class<Join> businessClass) {
		super(businessClass);
	}

	public JoinDAO() {
		super(Join.class);
	}


}
