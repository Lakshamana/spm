package br.ufpa.labes.spm.repository.impl.connections;


import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.connections.IBranchConDAO;
import br.ufpa.labes.spm.domain.BranchConCon;

public class BranchConDAO extends BaseDAO<BranchConCon, String> implements IBranchConDAO{

	protected BranchConDAO(Class<BranchConCon> businessClass) {
		super(businessClass);
	}

	public BranchConDAO() {
		super(BranchConCon.class);
	}


}
