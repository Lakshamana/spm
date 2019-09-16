package br.ufpa.labes.spm.repository.impl.tools;


import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.tools.IToolParameterDAO;
import br.ufpa.labes.spm.domain.ToolParameter;

public class ToolParametersDAO extends BaseDAO<ToolParameters, Integer> implements IToolParameterDAO{

	protected ToolParametersDAO(Class<ToolParameter> businessClass) {
		super(businessClass);
	}

	public ToolParameterDAO() {
		super(ToolParameter.class);
	}


}
