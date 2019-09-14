package br.ufpa.labes.spm.repository.impl.connections;


import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.connections.IBranchCondDAO;
import br.ufpa.labes.spm.domain.BranchCond;

public class BranchCondDAO extends BaseDAO<BranchCond, String> implements IBranchCondDAO{

	protected BranchCondDAO(Class<BranchCond> businessClass) {
		super(businessClass);
	}

	public BranchCondDAO() {
		super(BranchCond.class);
	}

}
