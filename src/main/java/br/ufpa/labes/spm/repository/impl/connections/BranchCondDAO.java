package br.ufpa.labes.spm.repository.impl.connections;

import javax.ejb.Stateless;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.connections.IBranchCondDAO;
import br.ufpa.labes.spm.domain.BranchCond;

@Stateless
public class BranchCondDAO extends BaseDAO<BranchCond, String> implements IBranchCondDAO{

	protected BranchCondDAO(Class<BranchCond> businessClass) {
		super(businessClass);
	}

	public BranchCondDAO() {
		super(BranchCond.class);
	}

}
