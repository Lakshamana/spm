package br.ufpa.labes.spm.repository.impl.connections;

import javax.ejb.Stateless;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.connections.IMultipleConDAO;
import br.ufpa.labes.spm.domain.MultipleCon;

@Stateless
public class MultipleConDAO extends BaseDAO<MultipleCon, String> implements IMultipleConDAO{

	protected MultipleConDAO(Class<MultipleCon> businessClass) {
		super(businessClass);
	}

	public MultipleConDAO() {
		super(MultipleCon.class);
	}


}
