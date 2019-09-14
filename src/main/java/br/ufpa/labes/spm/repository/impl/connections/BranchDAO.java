package br.ufpa.labes.spm.repository.impl.connections;

import javax.ejb.Stateless;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.connections.IBranchDAO;
import br.ufpa.labes.spm.domain.Branch;

@Stateless
public class BranchDAO extends BaseDAO<Branch, String> implements IBranchDAO{

	protected BranchDAO(Class<Branch> businessClass) {
		super(businessClass);
	}

	public BranchDAO() {
		super(Branch.class);
	}


}
