package br.ufpa.labes.spm.repository.interfaces.taskagenda;


import javax.ejb.Local;

import br.ufpa.labes.spm.repository.interfaces.IBaseDAO;
import br.ufpa.labes.spm.domain.Normal;
import br.ufpa.labes.spm.domain.ProcessAgenda;
import br.ufpa.labes.spm.domain.Task;

@Local
public interface IProcessAgendaDAO extends IBaseDAO<ProcessAgenda, Integer>{

	public Task addTask(ProcessAgenda pAgenda, Normal actNorm);

}
