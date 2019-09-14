package br.ufpa.labes.spm.repository.interfaces.types;

import javax.ejb.Local;
import br.ufpa.labes.spm.repository.interfaces.IBaseDAO;
import br.ufpa.labes.spm.domain.EventType;

@Local
public interface IEventTypeDAO extends IBaseDAO<EventType, String>{

}
