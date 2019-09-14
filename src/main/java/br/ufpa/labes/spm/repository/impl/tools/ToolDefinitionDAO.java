package br.ufpa.labes.spm.repository.impl.tools;


import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.tools.IToolDefinitionDAO;
import br.ufpa.labes.spm.domain.ToolDefinition;

public class ToolDefinitionDAO extends BaseDAO<ToolDefinition, String> implements IToolDefinitionDAO{

	protected ToolDefinitionDAO(Class<ToolDefinition> businessClass) {
		super(businessClass);
	}

	public ToolDefinitionDAO() {
		super(ToolDefinition.class);
	}


}
