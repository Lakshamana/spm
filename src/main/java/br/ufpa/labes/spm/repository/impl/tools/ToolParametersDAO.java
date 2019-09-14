package br.ufpa.labes.spm.repository.impl.tools;


import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.tools.IToolParametersDAO;
import br.ufpa.labes.spm.domain.ToolParameters;

public class ToolParametersDAO extends BaseDAO<ToolParameters, Integer> implements IToolParametersDAO{

	protected ToolParametersDAO(Class<ToolParameters> businessClass) {
		super(businessClass);
	}

	public ToolParametersDAO() {
		super(ToolParameters.class);
	}


}
