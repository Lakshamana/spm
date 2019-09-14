package br.ufpa.labes.spm.repository.impl.resources;

import javax.ejb.Stateless;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.resources.IExclusiveDAO;
import br.ufpa.labes.spm.domain.Exclusive;

@Stateless
public class ExclusiveDAO extends BaseDAO<Exclusive, String> implements IExclusiveDAO{

	protected ExclusiveDAO(Class<Exclusive> businessClass) {
		super(businessClass);
	}

	public ExclusiveDAO() {
		super(Exclusive.class);
	}


}
