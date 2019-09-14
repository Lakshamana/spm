package br.ufpa.labes.spm.repository.impl.log;

import javax.ejb.Stateless;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.log.IAgendaEventDAO;
import br.ufpa.labes.spm.domain.AgendaEvent;

@Stateless
public class AgendaEventDAO extends BaseDAO<AgendaEvent, Integer> implements IAgendaEventDAO{

	protected AgendaEventDAO(Class<AgendaEvent> businessClass) {
		super(businessClass);
	}

	public AgendaEventDAO() {
		super(AgendaEvent.class);
	}

}
