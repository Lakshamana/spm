package br.ufpa.labes.spm.repository.interfaces.resources;

import javax.ejb.Local;

import br.ufpa.labes.spm.repository.interfaces.IBaseDAO;
import br.ufpa.labes.spm.domain.Shareable;

@Local
public interface IShareableDAO  extends IBaseDAO<Shareable, String>{

}
