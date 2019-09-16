package br.ufpa.labes.spm.repository.impl.tools;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.tools.IToolParameterDAO;
import br.ufpa.labes.spm.domain.ToolParameter;

public class ToolParameterDAO extends BaseDAO<ToolParameter, Integer> implements IToolParameterDAO {

  protected ToolParameterDAO(Class<ToolParameter> businessClass) {
    super(businessClass);
  }

  public ToolParameterDAO() {
    super(ToolParameter.class);
  }
}
