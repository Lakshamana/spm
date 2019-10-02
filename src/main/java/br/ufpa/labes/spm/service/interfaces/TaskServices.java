package br.ufpa.labes.spm.service.interfaces;

import java.util.List;

import br.ufpa.labes.spm.service.dto.AgendaEventDTO;
import br.ufpa.labes.spm.domain.Normal;
import br.ufpa.labes.spm.domain.ProcessModel;

public interface TaskServices {

  public void executeProcess(String processId);

  public void beginTask(String agentId, String activityId);

  public void determineProcessModelStates(ProcessModel processModel);

  public void createTasks(ProcessModel processModel);

  public void createTasksNormal(Normal activityNormal);

  public void searchForReadyActivities(ProcessModel processModel);

  public void pauseTask(String agentId, String activityId);

  public boolean delegateTask(String from_agent_id, String act_id, String to_agent_id);

  public void pauseActiveTasks(String agentIdent);

  public void finishTask(String agentId, String activityId);

  public List<AgendaEventDTO> getAgendaEventsForTask(String normalIdent, String agentIdent);
}
