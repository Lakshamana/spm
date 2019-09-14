package br.ufpa.labes.spm.repository.impl.connections;


import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.connections.IBranchANDDAO;
import br.ufpa.labes.spm.domain.BranchAND;

public class BranchANDDAO extends BaseDAO<BranchAND, String> implements IBranchANDDAO{

	protected BranchANDDAO(Class<BranchAND> businessClass) {
		super(businessClass);
	}

	public BranchANDDAO() {
		super(BranchAND.class);
	}

}
