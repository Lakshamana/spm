package br.ufpa.labes.spm.repository.interfaces.tools;


import javax.ejb.Local;

import br.ufpa.labes.spm.repository.interfaces.IBaseDAO;
import br.ufpa.labes.spm.domain.ToolParameters;

@Local
public interface IToolParametersDAO extends IBaseDAO<ToolParameters, Integer>{

}
