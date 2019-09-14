package br.ufpa.labes.spm.repository.interfaces.activities;

import javax.ejb.Local;

import br.ufpa.labes.spm.repository.interfaces.IBaseDAO;
import br.ufpa.labes.spm.domain.Decomposed;

@Local
public interface IDecomposedDAO extends IBaseDAO<Decomposed, String>{

}
