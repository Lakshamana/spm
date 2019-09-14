package br.ufpa.labes.spm.repository.interfaces.processModels;

import javax.ejb.Local;

import br.ufpa.labes.spm.repository.interfaces.IBaseDAO;
import br.ufpa.labes.spm.domain.Description;

@Local
public interface IDescriptionDAO extends IBaseDAO<Description, Integer>{

}
