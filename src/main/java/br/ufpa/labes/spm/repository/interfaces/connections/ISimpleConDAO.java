package br.ufpa.labes.spm.repository.interfaces.connections;

import javax.ejb.Local;

import br.ufpa.labes.spm.repository.interfaces.IBaseDAO;
import br.ufpa.labes.spm.domain.SimpleCon;

@Local
public interface ISimpleConDAO extends IBaseDAO<SimpleCon, String>{

}
