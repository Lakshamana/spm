package br.ufpa.labes.spm.repository.impl.connections;

import javax.ejb.Stateless;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.connections.IBranchCondToMultipleConDAO;
import br.ufpa.labes.spm.domain.BranchCondToMultipleCon;

@Stateless
public class BranchCondToMultipleConDAO extends BaseDAO<BranchCondToMultipleCon, Integer> implements IBranchCondToMultipleConDAO{

	protected BranchCondToMultipleConDAO(Class<BranchCondToMultipleCon> businessClass) {
		super(businessClass);
	}

	public BranchCondToMultipleConDAO() {
		super(BranchCondToMultipleCon.class);
	}


}
