package br.ufpa.labes.spm.repository.interfaces.tools;


import javax.ejb.Local;

import br.ufpa.labes.spm.repository.interfaces.IBaseDAO;
import br.ufpa.labes.spm.domain.ToolDefinition;

@Local
public interface IToolDefinitionDAO extends IBaseDAO<ToolDefinition, String>{

}
