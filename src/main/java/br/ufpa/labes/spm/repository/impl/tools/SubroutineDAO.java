package br.ufpa.labes.spm.repository.impl.tools;


import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.tools.ISubroutineDAO;
import br.ufpa.labes.spm.domain.Subroutine;

public class SubroutineDAO extends BaseDAO<Subroutine, Integer> implements ISubroutineDAO{

	protected SubroutineDAO(Class<Subroutine> businessClass) {
		super(businessClass);
	}

	public SubroutineDAO() {
		super(Subroutine.class);
	}


}
