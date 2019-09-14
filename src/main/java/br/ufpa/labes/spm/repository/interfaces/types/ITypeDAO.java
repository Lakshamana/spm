package br.ufpa.labes.spm.repository.interfaces.types;


import javax.ejb.Local;

import br.ufpa.labes.spm.repository.interfaces.IBaseDAO;
import br.ufpa.labes.spm.domain.Type;

@Local
public interface ITypeDAO extends IBaseDAO<Type, String>{

}
