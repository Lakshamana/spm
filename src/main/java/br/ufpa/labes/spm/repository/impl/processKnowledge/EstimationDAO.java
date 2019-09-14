package br.ufpa.labes.spm.repository.impl.processKnowledge;

import javax.ejb.Stateless;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.processKnowledge.IEstimationDAO;
import br.ufpa.labes.spm.domain.Estimation;

@Stateless
public class EstimationDAO extends BaseDAO<Estimation, Integer> implements IEstimationDAO{

	protected EstimationDAO(Class<Estimation> businessClass) {
		super(businessClass);
	}

	public EstimationDAO() {
		super(Estimation.class);
	}


}
