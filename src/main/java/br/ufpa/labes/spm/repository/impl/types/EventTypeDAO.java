package br.ufpa.labes.spm.repository.impl.types;

import javax.ejb.Stateless;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.types.IEventTypeDAO;
import br.ufpa.labes.spm.domain.EventType;

@Stateless
public class EventTypeDAO extends BaseDAO<EventType, String> implements IEventTypeDAO{

	protected EventTypeDAO(Class<EventType> businessClass) {
		super(businessClass);
	}

	public EventTypeDAO() {
		super(EventType.class);
	}


}
