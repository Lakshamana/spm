package br.ufpa.labes.spm.repository.impl.plainActivities;


import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.plainActivities.IParameterDAO;
import br.ufpa.labes.spm.domain.Parameter;
public class ParameterDAO extends BaseDAO<Parameter, Integer> implements IParameterDAO {

	protected ParameterDAO(Class<Parameter> businessClass) {
		super(businessClass);
	}

	public ParameterDAO() {
		super(Parameters.class);
	}


}
