package br.ufpa.labes.spm.repository.interfaces.plainActivities;

import javax.ejb.Local;
import br.ufpa.labes.spm.repository.interfaces.IBaseDAO;
import br.ufpa.labes.spm.domain.Automatic;

@Local
public interface IAutomaticDAO extends IBaseDAO<Automatic, String>{

}
