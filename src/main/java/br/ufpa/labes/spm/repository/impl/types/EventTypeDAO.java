package br.ufpa.labes.spm.repository.impl.types;


import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.types.IEventTypeDAO;
import br.ufpa.labes.spm.domain.EventType;

public class EventTypeDAO extends BaseDAO<EventType, String> implements IEventTypeDAO{

	protected EventTypeDAO(Class<EventType> businessClass) {
		super(businessClass);
	}

	public EventTypeDAO() {
		super(EventType.class);
	}


}
