package br.ufpa.labes.spm.repository.interfaces.plainActivities;


import javax.ejb.Local;

import br.ufpa.labes.spm.repository.interfaces.IBaseDAO;
import br.ufpa.labes.spm.domain.EnactionDescription;

@Local
public interface IEnactionDescriptionDAO extends IBaseDAO<EnactionDescription, Integer>{

}
