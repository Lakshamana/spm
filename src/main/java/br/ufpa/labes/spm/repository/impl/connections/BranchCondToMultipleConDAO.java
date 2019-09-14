package br.ufpa.labes.spm.repository.impl.connections;


import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.connections.IBranchCondToMultipleConDAO;
import br.ufpa.labes.spm.domain.BranchCondToMultipleCon;

public class BranchCondToMultipleConDAO extends BaseDAO<BranchCondToMultipleCon, Integer> implements IBranchCondToMultipleConDAO{

	protected BranchCondToMultipleConDAO(Class<BranchCondToMultipleCon> businessClass) {
		super(businessClass);
	}

	public BranchCondToMultipleConDAO() {
		super(BranchCondToMultipleCon.class);
	}


}
