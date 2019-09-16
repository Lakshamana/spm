package br.ufpa.labes.spm.repository.impl.connections;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.connections.IBranchConDAO;
import br.ufpa.labes.spm.domain.BranchCon;

public class BranchConDAO extends BaseDAO<BranchCon, String> implements IBranchConDAO{

	protected BranchConDAO(Class<BranchCon> businessClass) {
		super(businessClass);
	}

	public BranchConDAO() {
		super(BranchCon.class);
	}
}
