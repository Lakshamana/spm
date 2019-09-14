package br.ufpa.labes.spm.repository.interfaces.resources;

import javax.ejb.Local;

import br.ufpa.labes.spm.repository.interfaces.IBaseDAO;
import br.ufpa.labes.spm.domain.Exclusive;

@Local
public interface IExclusiveDAO  extends IBaseDAO<Exclusive, String>{

}
