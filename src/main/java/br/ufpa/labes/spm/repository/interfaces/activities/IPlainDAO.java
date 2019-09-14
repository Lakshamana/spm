package br.ufpa.labes.spm.repository.interfaces.activities;

import javax.ejb.Local;

import br.ufpa.labes.spm.repository.interfaces.IBaseDAO;
import br.ufpa.labes.spm.domain.Plain;

@Local
public interface IPlainDAO extends IBaseDAO<Plain, String>{

}
