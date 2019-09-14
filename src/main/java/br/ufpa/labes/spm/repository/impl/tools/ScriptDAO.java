package br.ufpa.labes.spm.repository.impl.tools;

import javax.ejb.Stateless;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.tools.IScriptDAO;
import br.ufpa.labes.spm.domain.Script;

@Stateless
public class ScriptDAO extends BaseDAO<Script, String> implements IScriptDAO{

	protected ScriptDAO(Class<Script> businessClass) {
		super(businessClass);
	}

	public ScriptDAO() {
		super(Script.class);
	}

}
