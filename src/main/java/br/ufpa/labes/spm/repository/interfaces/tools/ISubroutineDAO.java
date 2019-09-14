package br.ufpa.labes.spm.repository.interfaces.tools;

import javax.ejb.Local;
import br.ufpa.labes.spm.repository.interfaces.IBaseDAO;
import br.ufpa.labes.spm.domain.Subroutine;

@Local
public interface ISubroutineDAO extends IBaseDAO<Subroutine, Integer>{

}
