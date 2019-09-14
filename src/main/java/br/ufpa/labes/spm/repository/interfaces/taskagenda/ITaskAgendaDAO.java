package br.ufpa.labes.spm.repository.interfaces.taskagenda;

import javax.ejb.Local;

import br.ufpa.labes.spm.repository.interfaces.IBaseDAO;
import br.ufpa.labes.spm.domain.TaskAgenda;

@Local
public interface ITaskAgendaDAO extends IBaseDAO<TaskAgenda, Integer>{

}
