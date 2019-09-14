package br.ufpa.labes.spm.repository.interfaces.tools;


import javax.ejb.Local;
import javax.lang.model.type.PrimitiveType;

import br.ufpa.labes.spm.repository.interfaces.IBaseDAO;

@Local
public interface IPrimitiveTypeDAO extends IBaseDAO<PrimitiveType, String>{

}
