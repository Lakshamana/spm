package br.ufpa.labes.spm.repository.impl.connections;

import javax.ejb.Stateless;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.connections.IBranchANDDAO;
import br.ufpa.labes.spm.domain.BranchAND;

@Stateless
public class BranchANDDAO extends BaseDAO<BranchAND, String> implements IBranchANDDAO{

	protected BranchANDDAO(Class<BranchAND> businessClass) {
		super(businessClass);
	}

	public BranchANDDAO() {
		super(BranchAND.class);
	}

}
