package br.ufpa.labes.spm.repository.impl.connections;


import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.connections.IBranchCondToActivityDAO;
import br.ufpa.labes.spm.domain.BranchCondToActivity;

public class BranchCondToActivityDAO extends BaseDAO<BranchCondToActivity, Integer> implements IBranchCondToActivityDAO{

	protected BranchCondToActivityDAO(Class<BranchCondToActivity> businessClass) {
		super(businessClass);
	}

	public BranchCondToActivityDAO() {
		super(BranchCondToActivity.class);
	}


}
