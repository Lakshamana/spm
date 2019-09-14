package br.ufpa.labes.spm.repository.impl.resources;

import javax.ejb.Stateless;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.resources.IShareableDAO;
import br.ufpa.labes.spm.domain.Shareable;

@Stateless
public class ShareableDAO extends BaseDAO<Shareable, String> implements IShareableDAO{

	protected ShareableDAO(Class<Shareable> businessClass) {
		super(businessClass);
	}

	public ShareableDAO() {
		super(Shareable.class);
	}


}
