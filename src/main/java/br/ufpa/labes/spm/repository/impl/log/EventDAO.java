package br.ufpa.labes.spm.repository.impl.log;

import javax.ejb.Stateless;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.log.IEventDAO;
import br.ufpa.labes.spm.domain.Event;

@Stateless
public class EventDAO extends BaseDAO<Event, Integer> implements IEventDAO{

	protected EventDAO(Class<Event> businessClass) {
		super(businessClass);
	}

	public EventDAO() {
		super(Event.class);
	}


}
