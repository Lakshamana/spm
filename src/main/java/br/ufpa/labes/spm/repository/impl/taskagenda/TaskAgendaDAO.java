package br.ufpa.labes.spm.repository.impl.taskagenda;

import javax.ejb.Stateless;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.taskagenda.ITaskAgendaDAO;
import br.ufpa.labes.spm.domain.TaskAgenda;

@Stateless
public class TaskAgendaDAO extends BaseDAO<TaskAgenda, Integer> implements ITaskAgendaDAO{

	protected TaskAgendaDAO(Class<TaskAgenda> businessClass) {
		super(businessClass);
	}

	public TaskAgendaDAO() {
		super(TaskAgenda.class);
	}


}
