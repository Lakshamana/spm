package br.ufpa.labes.spm.repository.interfaces.tools;


import javax.ejb.Local;

import br.ufpa.labes.spm.repository.interfaces.IBaseDAO;
import br.ufpa.labes.spm.domain.Script;

@Local
public interface IScriptDAO extends IBaseDAO<Script, String>{

}
