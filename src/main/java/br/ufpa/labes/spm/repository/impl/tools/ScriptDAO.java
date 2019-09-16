package br.ufpa.labes.spm.repository.impl.tools;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.tools.IScriptDAO;
import br.ufpa.labes.spm.domain.Script;

public class ScriptDAO extends BaseDAO<Script, String> implements IScriptDAO {

  protected ScriptDAO(Class<Script> businessClass) {
    super(businessClass);
  }

  public ScriptDAO() {
    super(Script.class);
  }
}
