package br.ufpa.labes.spm.repository.impl.plainActivities;

import javax.ejb.Stateless;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.plainActivities.IParametersDAO;
import br.ufpa.labes.spm.domain.Parameters;

@Stateless
public class ParametersDAO extends BaseDAO<Parameters, Integer> implements IParametersDAO{

	protected ParametersDAO(Class<Parameters> businessClass) {
		super(businessClass);
	}

	public ParametersDAO() {
		super(Parameters.class);
	}


}
