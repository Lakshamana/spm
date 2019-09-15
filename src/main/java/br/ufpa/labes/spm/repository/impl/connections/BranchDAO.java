package br.ufpa.labes.spm.repository.impl.connections;


import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.connections.IBranchDAO;
import br.ufpa.labes.spm.domain.BranchCon;

public class BranchDAO extends BaseDAO<BranchCon, String> implements IBranchDAO{

	protected BranchDAO(Class<BranchCon> businessClass) {
		super(businessClass);
	}

	public BranchDAO() {
		super(BranchCon.class);
	}


}
