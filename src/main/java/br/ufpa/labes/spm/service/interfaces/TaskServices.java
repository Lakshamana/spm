package org.qrconsult.spm.services.interfaces;

import java.util.List;

import javax.ejb.Remote;

import org.qrconsult.spm.dtos.agenda.AgendaEventDTO;
import org.qrconsult.spm.dtos.agenda.AgendaEventsDTO;
import org.qrconsult.spm.exceptions.WebapseeException;
import org.qrconsult.spm.model.plainActivities.Normal;
import org.qrconsult.spm.model.processModels.ProcessModel;

@Remote
public interface TaskServices {

	public void executeProcess (String processId);
	
	public void beginTask (String agentId, String activityId);
	
    public void determineProcessModelStates (ProcessModel processModel);

 	public void createTasks(ProcessModel processModel);
 	
 	public void createTasksNormal(Normal activityNormal);
 	
    public void searchForReadyActivities (ProcessModel processModel);

	public void pauseTask(String agentId, String activityId); 
	
	public boolean delegateTask (String from_agent_id, String act_id, String to_agent_id);
	
	public void pauseActiveTasks(String agentIdent);

	public void finishTask(String agentId, String activityId);
	
	public List<AgendaEventDTO> getAgendaEventsForTask(String normalIdent,String agentIdent);
}
