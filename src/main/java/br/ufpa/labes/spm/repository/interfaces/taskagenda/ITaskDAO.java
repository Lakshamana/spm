package br.ufpa.labes.spm.repository.interfaces.taskagenda;


import br.ufpa.labes.spm.repository.interfaces.IBaseDAO;
import org.qrconsult.spm.dtos.dashboard.Time;
import br.ufpa.labes.spm.domain.Task;

public interface ITaskDAO extends IBaseDAO<Task, Integer> {
	public float getWorkingHoursForTask( String normalIdent, String agentIdent );

	public Time getWorkingHoursForTask2(String normalIdent, String agentIdent);

}
