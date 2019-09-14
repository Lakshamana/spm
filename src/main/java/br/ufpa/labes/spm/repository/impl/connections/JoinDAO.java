package br.ufpa.labes.spm.repository.impl.connections;

import javax.ejb.Stateless;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.connections.IJoinDAO;
import br.ufpa.labes.spm.domain.Join;

@Stateless
public class JoinDAO extends BaseDAO<Join, String> implements IJoinDAO{

	protected JoinDAO(Class<Join> businessClass) {
		super(businessClass);
	}

	public JoinDAO() {
		super(Join.class);
	}


}
