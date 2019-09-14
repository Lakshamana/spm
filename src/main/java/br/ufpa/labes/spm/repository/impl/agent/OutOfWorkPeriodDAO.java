package br.ufpa.labes.spm.repository.impl.agent;

import javax.ejb.Stateless;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.agent.IOutOfWorkPeriodDAO;
import br.ufpa.labes.spm.domain.OutOfWorkPeriod;

@Stateless
public class OutOfWorkPeriodDAO extends BaseDAO<OutOfWorkPeriod, Integer> implements IOutOfWorkPeriodDAO{

	protected OutOfWorkPeriodDAO(Class<OutOfWorkPeriod> businessClass) {
		super(businessClass);
	}

	public OutOfWorkPeriodDAO() {
		super(OutOfWorkPeriod.class);
	}

}
